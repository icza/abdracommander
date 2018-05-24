/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.service.language;

import hu.belicza.andras.util.Utils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.abdracmd.Consts;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.util.bean.person.Person;
import com.abdracmd.util.bean.person.PersonNamePart;
import com.abdracmd.util.bean.person.PersonNamePart.PersonNamePartType;

/**
 * Language service.
 * 
 * @author Andras Belicza
 */
public class Language {
	
	/** Extension of language files. */
	public static final String LANGUAGE_FILE_EXT = "xml";
	
	/** Max number of fraction digits to be allowed/handled. */
	public static final int MAX_FRACTION_DIGITS = 5;
	
	/** Name of the language. */
	public final String name;
	
	/**
	 * Optional parent language.
	 * If provided, missing texts will be acquired from the parent language.
	 */
	public final Language parentLanguage;
	
	/**
	 * Target version of the language file.
	 * This is to be matched with {@link Consts#APP_VERSION_STRING_SHORT}.
	 */
	private String target;
	/** Version of the language file.        */
	private String version;
	
	/** List of translators. */
	private List< Person > translatorList = new ArrayList<>();
	
	/** Person name format: the order of the name part types. */
	private PersonNamePartType[] personNameFormat;
	
	/** Decimal format used to format numbers. */
	private DecimalFormat   decimalFormat;
	/** Decimal formats used to format numbers with different fraction digits.
	 * The decimal format instance at index <code>i</code> will use a number of fraction digits <code>i</code>. */
	private DecimalFormat[] fractionDecimalFormats;
	
	/** Date format to format dates.      */
	private SimpleDateFormat dateFormat;
	/** Date format to format date+times. */
	private SimpleDateFormat dateTimeFormat;
	/** Date format to format times.      */
	private SimpleDateFormat timeFormat;
	
	/** Language name in the language itself. */
	private String localName;
	
	/** Text properties.                          */
	private Properties                      textProperties;
	/** Cached values of texts with mnemonic map. */
	private Map< String, TextWithMnemonic > textWithMnemonicMap = new HashMap<>();
	
	/**
	 * Creates a new Language.
	 * @param name name of the language
	 */
	public Language( final String name ) {
		this( name, null );
	}
	
	/**
	 * Creates a new Language.
	 * @param name           name of the language
	 * @param parentLanguage optional parent language
	 */
	public Language( final String name, final Language parentLanguage ) {
		this.name           = name;
		this.parentLanguage = parentLanguage;
		
		textProperties      = parentLanguage == null ? new Properties() : new Properties( parentLanguage.textProperties );
	}
	
	/**
	 * Loads the language from its file.
	 * @return true if the language was successfully loaded from its file; false otherwise
	 */
	public boolean load() {
		return load( true );
	}
	
	/**
	 * Loads the language from its file.
	 * @param loadTexts tells whether to load texts too or only meta data
	 * @return true if the language was successfully loaded from its file; false otherwise
	 */
	public boolean load( final boolean loadTexts ) {
		final Path languageFilePath = Consts.PATH_LANGUAGES.resolve( name + '.' + LANGUAGE_FILE_EXT );
		if ( !Files.exists( languageFilePath ) ) {
			MainShell.INSTANCE.getModel().getLogging().error( "Language file does not exist: " + languageFilePath );
			return false;
		}
		
		try ( final InputStream xmlInput = Files.newInputStream( languageFilePath ) ) {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments( true );
			final Document document = factory.newDocumentBuilder().parse( xmlInput );
			
			final Element docElement = document.getDocumentElement();
			
			// Version info
			final Element versionElement = (Element) docElement.getElementsByTagName( "version" ).item( 0 );
			target  = versionElement.getAttribute( "target"  );
			version = versionElement.getAttribute( "version" );
			
			// Translators
			translatorList.clear();
			final Element translatorsElement = (Element) docElement.getElementsByTagName( "translators" ).item( 0 );
			if ( translatorsElement != null ) {
				final NodeList personNodeList = translatorsElement.getElementsByTagName( "person" );
				for ( int i = 0; i < personNodeList.getLength(); i++ ) {
					final Person translator = new Person();
					translator.loadFromXML( (Element) personNodeList.item( i ) );
					translatorList.add( translator );
				}
			}
			
			// Person name format
			final Element nameFormatElement = (Element) docElement.getElementsByTagName( "personNameFormat" ).item( 0 );
			final String[] nameFormatElements = nameFormatElement.getTextContent().split( Utils.SPACE_STRING );
			personNameFormat = new PersonNamePartType[ nameFormatElements.length ];
			for ( int i = 0; i < personNameFormat.length; i++ )
				personNameFormat[ i ] = PersonNamePartType.valueOf( nameFormatElements[ i ] );
			
			// Number format
			decimalFormat = new DecimalFormat();
			final Element numberFormatElement = (Element) docElement.getElementsByTagName( "numberFormat" ).item( 0 );
			if ( numberFormatElement != null ) {
				final int groupingSize  = Integer.parseInt( numberFormatElement.getAttribute( "groupingSize" ) );
				if ( groupingSize == 0 )
					decimalFormat.setGroupingUsed( false );
				else
					decimalFormat.setGroupingSize( groupingSize );
				
				final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
				dfs.setGroupingSeparator( numberFormatElement.getAttribute( "groupingSeparator" ).charAt( 0 ) );
				dfs.setDecimalSeparator ( numberFormatElement.getAttribute( "decimalSeparator"  ).charAt( 0 ) );
				decimalFormat.setDecimalFormatSymbols( dfs );
			}
			fractionDecimalFormats = new DecimalFormat[ MAX_FRACTION_DIGITS+1 ];
			for ( int i = 0; i < fractionDecimalFormats.length; i++ ) {
				fractionDecimalFormats[ i ] = (DecimalFormat) decimalFormat.clone();
				fractionDecimalFormats[ i ].setMinimumFractionDigits( i );
				fractionDecimalFormats[ i ].setMaximumFractionDigits( i );
			}
			
			// Date+time formats
			final Element dateFormatElement     = (Element) docElement.getElementsByTagName( "dateFormat"     ).item( 0 );
			dateFormat     = new SimpleDateFormat( dateFormatElement     == null ? "yyyy-MM-dd"          : dateFormatElement    .getTextContent() );
			final Element dateTimeFormatElement = (Element) docElement.getElementsByTagName( "dateTimeFormat" ).item( 0 );
			dateTimeFormat = new SimpleDateFormat( dateTimeFormatElement == null ? "yyyy-MM-dd HH:mm:ss" : dateTimeFormatElement.getTextContent() );
			final Element timeFormatElement     = (Element) docElement.getElementsByTagName( "timeFormat"     ).item( 0 );
			timeFormat     = new SimpleDateFormat( timeFormatElement     == null ? "HH:mm:ss"            : timeFormatElement    .getTextContent() );
			
			// Local language name
			final Element localNameElement = (Element) docElement.getElementsByTagName( "localName" ).item( 0 );
			setLocalName( localNameElement == null ? name : localNameElement.getTextContent() );
			
			if ( !loadTexts ) // If texts are not needed, we are done here
				return true;
			
			// Texts
			textProperties     .clear();
			textWithMnemonicMap.clear();
			
			final NodeList textElements = docElement.getElementsByTagName( "t" );
			final int textsCount = textElements.getLength();
			for ( int i = 0; i < textsCount; i++ ) {
				final Element textElement = (Element) textElements.item( i );
				textProperties.setProperty( textElement.getAttribute( "k" ), textElement.getTextContent() );
			}
			
			return true;
		} catch ( final Exception e ) {
			MainShell.INSTANCE.getModel().getLogging().error( "Failed to process language file: " + languageFilePath, e );
			return false;
		}
	}
	
	/**
	 * Returns the text associated with the specified string key.
	 * 
	 * <p><b>WARNING!</b> This method does not check if the specified text requires parameters.<br>
	 * Use of {@link #get(TextKey)} is recommended! This is for internal use to implement enum auto-text resolving!</p>
	 * 
	 * @param key key whose text to return
	 * @return the text associated with the specified string key
	 */
	public String get( final String key ) {
		return textProperties.getProperty( key );
	}
	
	/**
	 * Returns the text associated with the specified key.
	 * 
	 * @param key key whose text to return
	 * @return the text associated with the specified key
	 * 
	 * @throws IllegalArgumentException if the specified text requires parameters
	 */
	public String get( final TextKey key ) {
		return get( key, Utils.EMPTY_ARRAY );
	}
	
	/**
	 * Returns the text associated with the specified key.
	 * 
	 * @param key    key whose text to return
	 * @param params parameters of the text
	 * @return the text associated with the specified key
	 * 
	 * @throws IllegalArgumentException if the specified text key requires a different number of parameters than provided
	 * @throws NullPointerException if the text associated with the text key is <code>null</code>
	 * @throws IllegalArgumentException if the text associated with the text key does not contain the templates specified by the text key's params
	 * @throws NullPointerException if the <code>params</code> array contains <code>null</code>
	 */
	public String get( final TextKey key, final Object... params ) {
		checkParams( key, params );
		
		return substituteParams( textProperties.getProperty( key.name() ), key, params );
	}
	
	/**
	 * Returns the text with mnemonic associated with the specified key.
	 * 
	 * @param key key whose text to return
	 * @return the text with mnemonic associated with the specified key
	 * 
	 * @throws IllegalArgumentException if the specified text requires parameters
	 * @throws NullPointerException if the text associated with the text key is <code>null</code>
	 * @throws NullPointerException if the <code>params</code> array contains <code>null</code>
	 */
	public TextWithMnemonic getWithMnemonic( final TextKey key ) {
		return getWithMnemonic( key, Utils.EMPTY_ARRAY );
	}
	
	/**
	 * Returns the text with mnemonic associated with the specified key.
	 * 
	 * @param key key whose text to return
	 * @param params parameters of the text
	 * @return the text with mnemonic associated with the specified key
	 * 
	 * @throws IllegalArgumentException if the specified text key requires a different number of parameters than provided
	 * @throws NullPointerException if the text associated with the text key is <code>null</code>
	 * @throws IllegalArgumentException if the text associated with the text key does not contain the templates specified by the text key's params
	 * @throws NullPointerException if the <code>params</code> array contains <code>null</code>
	 */
	public TextWithMnemonic getWithMnemonic( final TextKey key, final Object... params ) {
		checkParams( key, params );
		
		TextWithMnemonic textWithMnemonic = textWithMnemonicMap.get( key.name() );
		
		if ( textWithMnemonic == null )
			textWithMnemonicMap.put( key.name(), textWithMnemonic = new TextWithMnemonic( textProperties.getProperty( key.name() ) ) );
		
		if ( params.length == 0 )
			return textWithMnemonic; // Instances are immutable, simply return the cached instance
		
		return new TextWithMnemonic( substituteParams( textWithMnemonic.text, key, params ), textWithMnemonic.mnemonic );
	}
	
	/**
	 * Substitutes the parameters of a text.
	 * @param text   text to substitute parameters in
	 * @param key    text key of the text
	 * @param params parameter values to substitute in
	 * @return the text in which parameters have been substituted
	 * 
	 * @throws NullPointerException if the specified text is <code>null</code>
	 * @throws IllegalArgumentException if the specified text does not contain the templates specified by the text's text key
	 * @throws NullPointerException if the <code>params</code> array contains <code>null</code>
	 */
	private static String substituteParams( String text, final TextKey key, final Object... params ) {
		if ( text == null )
			throw new NullPointerException();
		
		for ( int i = 0; i < params.length; i++ ) {
			final String temp = text;
			text = text.replace( key.params[ i ].template, params[ i ].toString() );
			if ( temp == text )
				throw new RuntimeException( "Template not found in " + key + " text: " + key.params[ i ].template );
		}
		
		return text;
	}
	
	/**
	 * Checks the number of required parameters of the specified text key and the number of provided parameters.
	 * 
	 * @param key    key of text whose number of required params to check
	 * @param params provided parameters for the text
	 * 
	 * @throws IllegalArgumentException if the specified text requires a different number of parameters than provided
	 */
	private static void checkParams( final TextKey key, final Object... params ) {
		if ( key.params.length != params.length )
			throw new IllegalArgumentException( "The specified " + key +" text requires " + key.params.length + " parameter"
				+ ( key.params.length == 1 ? Utils.EMPTY_STRING : "s" ) + " but " + params.length
				+ ( params.length == 1 ? " was" : " were" ) + " provided!" );
	}
	
	/**
	 * Returns the name of the specified person.
	 * @param person person whose name to be returned
	 * @return the name of the specified person
	 */
	public String getPersonName( final Person person ) {
		final StringBuilder nameBuilder = new StringBuilder();
		
		for ( final PersonNamePartType type : personNameFormat ) {
			for ( final PersonNamePart part : person.getNamePartList() ) {
				if ( part.getType() == type ) {
					if ( nameBuilder.length() > 0 )
						nameBuilder.append( ' ' );
					
					// Nick names must be included between quotes:
					if ( type == PersonNamePartType.NICKNAME )
						nameBuilder.append( '"' ).append( part.getValue() ).append( '"' );
					else
						nameBuilder.append( part.getValue() );
					
					break;
				}
			}
		}
		
		return nameBuilder.toString();
	}
	
	/**
	 * Formats the specified number.
	 * @param n number to be formatted
	 * @return the formatted number
	 */
	public String formatNumber( final long n ) {
		return decimalFormat.format( n );
	}
	
	/**
	 * Formats the specified number.
	 * @param n              number to be formatted
	 * @param fractionDigits number of fraction digits to use
	 * @return the formatted number
	 * 
	 * @throws IllegalArgumentException if fraction digits is negative or greater than {@value #MAX_FRACTION_DIGITS}
	 */
	public String formatNumber( final double n, int fractionDigits ) {
		if ( fractionDigits < 0 )
			throw new IllegalArgumentException( "Fraction digits cannot be negative!" );
		if ( fractionDigits > MAX_FRACTION_DIGITS )
			throw new IllegalArgumentException( "Fraction digits cannot be greater than " + MAX_FRACTION_DIGITS + " (provided: " + fractionDigits + ")!" );
		
		return fractionDecimalFormats[ fractionDigits ].format( n );
	}
	
	/**
	 * Formats the specified date.
	 * @param date date to be formatted
	 * @return the formatted date
	 */
	public String formatDate( final long date ) {
		// Date formats are not synchronized!
		synchronized ( dateFormat ) {
			return dateFormat.format( date );
		}
	}
	
	/**
	 * Formats the specified date.
	 * @param date date to be formatted
	 * @return the formatted date
	 */
	public String formatDate( final Date date ) {
		// Date formats are not synchronized!
		synchronized ( dateFormat ) {
			return dateFormat.format( date );
		}
	}
	
	/**
	 * Formats the specified date+time.
	 * @param dateTime date+time to be formatted
	 * @return the formatted date+time
	 */
	public String formatDateTime( final Date dateTime ) {
		// Date formats are not synchronized!
		synchronized ( dateTimeFormat ) {
			return dateTimeFormat.format( dateTime );
		}
	}
	
	/**
	 * Formats the specified time.
	 * @param time time to be formatted
	 * @return the formatted time
	 */
	public String formatTime( final Date time ) {
		// Date formats are not synchronized!
		synchronized ( timeFormat ) {
			return timeFormat.format( time );
		}
	}
	
	/**
	 * Returns the list of translators.
	 * @return the list of translators
	 */
	public List< Person > getTranslatorList() {
	    return translatorList;
    }
	
	/**
	 * Sets the list of translators.
	 * @param translatorList list of translators to be set
	 */
	public void setTranslatorList( final List< Person > translatorList ) {
		this.translatorList = translatorList;
	}
	
	/**
	 * Returns the target version of the language file.
	 * @return the target version of the language file
	 */
	public String getTarget() {
	    return target;
    }
	
	/**
	 * Sets the target version of the language file.
	 * @param target target version of the language file to be set
	 */
	public void setTarget( String target ) {
	    this.target = target;
    }
	
	/**
	 * Returns the version of the language file.
	 * @return the version of the language file
	 */
	public String getVersion() {
	    return version;
    }
	
	/**
	 * Sets the version of the language file.
	 * @param version the version of the language file to be set
	 */
	public void setVersion( String version ) {
	    this.version = version;
    }
	
	/**
	 * Returns the local language name.
	 * @return the local language name
	 */
	public String getLocalName() {
		return localName;
	}
	
	/**
	 * Sets the local language name.
	 * @param localName local language name to be set
	 */
	public void setLocalName( String localName ) {
		this.localName = localName;
	}
	
}
