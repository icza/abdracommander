/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.util;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.secure.DecryptUtil;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.abdracmd.Consts;
import com.abdracmd.DevConsts;
import com.abdracmd.service.language.Language;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.service.settings.SettingSetChangeListener;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.IFolderPresenterTheme;
import com.abdracmd.util.bean.person.Person;
import com.abdracmd.util.bean.reginfo.RegistrationInfo;

/**
 * General utilities used in Abdra Commander related to Abdra Commander.
 * 
 * @author Andras Belicza
 */
public class AcUtils {
	
	/** A common assess helper instance used locally. */
	public static final CommonAccessHelper CAH = new CommonAccessHelper();
	
	
	/** Global (application wide) setting set change listener. */
	private static final SettingSetChangeListener globalSettingSetChangeListener = new SettingSetChangeListener() {
		@Override
		public void valuesChanged( final Set< SettingKey< ? > > settingKeySet ) {
			if ( settingKeySet.contains( SettingKeys.UI$TOOL_TIP_INITIAL_DELAY ) || settingKeySet.contains( SettingKeys.UI$TOOL_TIP_DISMISS_DELAY ) ) {
				ToolTipManager.sharedInstance().setInitialDelay( CAH.get( SettingKeys.UI$TOOL_TIP_INITIAL_DELAY ) );
				ToolTipManager.sharedInstance().setDismissDelay( CAH.get( SettingKeys.UI$TOOL_TIP_DISMISS_DELAY ) );
			}
			
			if ( settingKeySet.contains( SettingKeys.APP$LOG_LEVEL ) )
				CAH.getLogging().setLogLevel( CAH.get( SettingKeys.APP$LOG_LEVEL ) );
			
			if ( settingKeySet.contains( SettingKeys.APP$LOOK_AND_FEEL ) )
				refreshLookAndFeel();
		}
	};
	
	/**
	 * Refreshes the application look and feel.
	 */
	private static void refreshLookAndFeel() {
		GuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
		        try {
					boolean found = false;
		        	for ( final LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels() )
						if ( lafi.getName().equals( CAH.get( SettingKeys.APP$LOOK_AND_FEEL ) ) ) {
							found = true;
							UIManager.setLookAndFeel( lafi.getClassName() );
						}
		        	if ( !found )
		        		CAH.warning( "Application Look&Feel is not installed: " + CAH.get( SettingKeys.APP$LOOK_AND_FEEL ) );
		        	
		        	// Delegate updateUI() calls to installed folder presenter themes:
		        	// (do this before updating windows which will call back to the current theme)
		        	for ( final IFolderPresenterTheme theme : CAH.mainModel.getFolderPresenterThemeRegistry().valueList() )
		        		theme.updateUI();
		        	
		        } catch ( final Exception e ) {
		        	CAH.error( "Failed to set application Look&Feel: " + CAH.get( SettingKeys.APP$LOOK_AND_FEEL ), e );
		        }
				
		        for ( final Window w : Window.getWindows() )
		        	SwingUtilities.updateComponentTreeUI( w );
			}
		} );
	}
	
	/**
	 * Initializes the global (application wide) setting set change listener.
	 */
	public static void initializeGlobalSettingSetChangeListener() {
		final Set< SettingKey< ? > > listenedSettingKeySet = Utils.< SettingKey< ? > >asNewSet(
				SettingKeys.UI$TOOL_TIP_INITIAL_DELAY, SettingKeys.UI$TOOL_TIP_DISMISS_DELAY,
				SettingKeys.APP$LOG_LEVEL,
				SettingKeys.APP$LOOK_AND_FEEL );
		
		CAH.addChangeListener( listenedSettingKeySet, globalSettingSetChangeListener );
		
		// Init setting dependent code:
		globalSettingSetChangeListener.valuesChanged( listenedSettingKeySet );
	}
	
	/**
	 * Unconditional immediate beep.
	 */
	public static void beep() {
		Toolkit.getDefaultToolkit().beep();
	}
	
	/**
	 * Emits a beep for error events.
	 */
	public static void beepError() {
		if ( CAH.get( SettingKeys.APP$BEEP_ON_ERRORS ) )
			beep();
	}
	
	/** Internal cache of text key name prefixes for different enum types. */
	@SuppressWarnings( "rawtypes" )
    private static Map< Class< ? extends Enum >, String > ENUM_CLASS_TEXT_KEY_NAME_PREFIX_MAP = new HashMap<>();
	
	/**
	 * Returns the text value of an enum.
	 * @param value enum value to return the text of
	 * @return the text value fo an enum
	 */
	public static String getEnumText( final Enum< ? > value ) {
		String namePrefix;
		
		if ( ( namePrefix = ENUM_CLASS_TEXT_KEY_NAME_PREFIX_MAP.get( value.getClass() ) ) == null ) {
			final StringBuilder namePrefixBuilder = new StringBuilder( "ENUM" );
			namePrefixBuilder.append( DevConsts.HIERARCHY_SEPARATOR );
			
			for ( final char ch : value.getClass().getSimpleName().toCharArray() ) {
				if ( Character.isUpperCase( ch ) && namePrefixBuilder.length() > 5 )
					namePrefixBuilder.append( '_' );
				namePrefixBuilder.append( Character.toUpperCase( ch ) );
			}
			namePrefixBuilder.append( DevConsts.HIERARCHY_SEPARATOR );
			
			ENUM_CLASS_TEXT_KEY_NAME_PREFIX_MAP.put( value.getClass(), namePrefix = namePrefixBuilder.toString() );
		}
		
		return CAH.getLanguage().get( namePrefix + value.name() );
	}
	
	/**
	 * Opens the web page specified by the URL in the system's default browser.
	 * @param url URL to be opened
	 */
	public static void showURLInBrowser( final String url ) {
		try {
			if ( Desktop.isDesktopSupported() )
				try {
					Desktop.getDesktop().browse( new URI( url ) );
					return;
				} catch ( final Exception e ) {
					CAH.debug( "Failed to open URL using Desktop: " + url, e );
				}
			
			// Desktop failed, try our own method
			String[] cmdArray = null;
			if ( Consts.OS == OperatingSystem.WINDOWS ) {
				cmdArray = new String[] { "rundll32", "url.dll,FileProtocolHandler", url };
			}
			else {
				// Linux
				final String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
				for ( final String browser : browsers )
					if ( Runtime.getRuntime().exec( new String[] { "which", browser } ).waitFor() == 0 ) {
						cmdArray = new String[] { browser, url };
						break;
					}
			}
			
			if ( cmdArray != null )
				Runtime.getRuntime().exec( cmdArray );
			
		} catch ( final Exception e ) {
			CAH.debug( "Failed to open URL: " + url, e );
		}
	}
	
	/**
	 * Returns the current folder presenter theme.
	 * @return the current folder presenter theme
	 */
	public static IFolderPresenterTheme getFolderPresenterTheme() {
		IFolderPresenterTheme folderPresenterTheme = CAH.mainModel.getFolderPresenterThemeRegistry().get( CAH.get( SettingKeys.FOLDER_TABLE$THEME ) );
		
		if ( folderPresenterTheme == null ) {
			CAH.warning( "Theme not found: " + CAH.get( SettingKeys.FOLDER_TABLE$THEME ) );
			CAH.warning( "Reverting to default theme." );
			
			CAH.reset( SettingKeys.FOLDER_TABLE$THEME );
			
			folderPresenterTheme = CAH.mainModel.getFolderPresenterThemeRegistry().get( CAH.get( SettingKeys.FOLDER_TABLE$THEME ) );
		}
		
		return folderPresenterTheme;
	}
	
	/**
	 * Returns the available language list.
	 * @return the available language list
	 */
	public static List< String > getLanguageNameList() {
		final List< String > languageList = new ArrayList<>();
		
		final String langFileEnding = "." + Language.LANGUAGE_FILE_EXT;
		
		try {
			Files.walkFileTree( Consts.PATH_LANGUAGES, Utils.< FileVisitOption >getEmptySet(), 1, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
					final String fileName = Utils.getFileName( file );
					if ( fileName.endsWith( langFileEnding ) )
						languageList.add( fileName.substring( 0, fileName.lastIndexOf( '.' ) ) );
				    return FileVisitResult.CONTINUE;
				}
			} );
		} catch ( final IOException ie ) {
	        CAH.warning( "Failed to list languages!" );
	        CAH.debug( "Reason:", ie );
		}
		
		// Sort language list: default as the first, the rest in lexicographical order
		Collections.sort( languageList, new Comparator< String >() {
			@Override
			public int compare( final String n1, final String n2 ) {
				if ( Consts.DEFAULT_LANGUAGE_NAME.equals( n1 ) )
					return -1;
				if ( Consts.DEFAULT_LANGUAGE_NAME.equals( n2 ) )
					return 1;
				return n1.compareTo( n2 );
			}
		} );
		
		return languageList;
	}
	
	/**
	 * Returns the installed LAF names sorted by our preference.
	 * @return the installed LAF names sorted by our preference
	 */
	public static String[] getSortedInstalledLAFInfos() {
		// Get the name of the system look and feel:
		String systemLookAndFeelName = Utils.EMPTY_STRING;
		final String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
		
		final LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
		
		for ( final LookAndFeelInfo lafi : installedLookAndFeels )
			if ( systemLookAndFeelClassName.equals( lafi.getClassName() ) ) {
				systemLookAndFeelName = lafi.getName();
				break;
			}
		
    	final String[] lafNames = new String[ installedLookAndFeels.length ];
		for ( int i = 0; i < installedLookAndFeels.length; i++ )
			lafNames[ i ] = installedLookAndFeels[ i ].getName();
		
		final String systemLookAndFeelNameFinal = systemLookAndFeelName;
		Arrays.sort( lafNames, new Comparator< String >() {
			// What we want to prioritize:
			final String[] lafNamesInOrder = new String[] { "Nimbus", "Metal", systemLookAndFeelNameFinal };
			@Override
			public int compare( final String l1, final String l2 ) {
				for ( final String lafName : lafNamesInOrder ) {
					if ( lafName.equals( l1 ) )
						return -1;
					if ( lafName.equals( l2 ) )
						return 1;
				}
				return 0; // The rest is good as is.
			}
		} );
		
		return lafNames;
	}
	
	/**
	 * Returns the list of translators of the specified language as a string.
	 * @param language language whose translators to be returned
	 * @return the list of translators of the specified language as a string
	 */
	public static String getTranslatorListString( final Language language) {
		final StringBuilder sb = new StringBuilder();
		
		for ( final Person translator : language.getTranslatorList() ) {
			if ( sb.length() > 0 )
				sb.append( ", " );
			sb.append( CAH.getPersonName( translator ) );
		}
		
	    return sb.toString();
    }
	
	/**
	 * Loads the registration info.
	 */
	public static void loadRegistrationInfo() {
		try {
			final byte[] regInfoEncData = Files.readAllBytes( Consts.PATH_REG_INFO_DATA );
			final byte[] regInfoXmlData = DecryptUtil.decrypt( regInfoEncData );
			if ( regInfoXmlData == null ) {
				CAH.error( "Invaild or corrupt registration info data in file: " + Consts.PATH_REG_INFO_DATA );
				return;
			}
			
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments( true );
			final Document document = factory.newDocumentBuilder().parse( new ByteArrayInputStream( regInfoXmlData ) );
			
			final Element docElement = document.getDocumentElement();
			
			final RegistrationInfo regInfo = new RegistrationInfo();
			regInfo.loadFromXML( docElement );
			
			CAH.debug( "Registration info loaded (registered to: " + CAH.getPersonName( regInfo.getRegisteredPerson() ) + ")." );
			
			CAH.mainModel.setRegistrationInfo( regInfo );
		} catch ( final Exception e ) {
			CAH.error( "Failed to load registration info data from: " + Consts.PATH_REG_INFO_DATA, e );
		}
    }
	
	// Cache size unit strings!
	/** Cached string of the bytes unit string. */
	private static String UNIT_BYTES;
	/** Cached string of the KB unit string.    */
	private static String UNIT_KB;
	/** Cached string of the MB unit string.    */
	private static String UNIT_MB;
	/** Cached string of the GB unit string.    */
	private static String UNIT_GB;
	/** Cached string of the TB unit string.    */
	private static String UNIT_TB;
	/** Cached string for 0 bytes.              */
	public  static String ZERO_BYTES;
	
	/**
	 * Initializes cached texts.
	 */
	public static void initCachedTexts() {
		UNIT_BYTES = CAH.get( TextKey.GENERAL$UNIT$BYTES );
		UNIT_KB    = CAH.get( TextKey.GENERAL$UNIT$KB    );
		UNIT_MB    = CAH.get( TextKey.GENERAL$UNIT$MB    );
		UNIT_GB    = CAH.get( TextKey.GENERAL$UNIT$GB    );
		UNIT_TB    = CAH.get( TextKey.GENERAL$UNIT$TB    );
		ZERO_BYTES = CAH.formatNumber( 0 ) + Utils.SPACE_STRING + UNIT_BYTES;
	}
	
	/**
	 * Returns a formatted size value.
	 * @param format         required size format
	 * @param size           size to be formatted
	 * @param fractionDigits number of fraction digits if output is not in bytes
	 * @return the formatted size value
	 */
	public static String getFormattedSize( SizeFormat format, final long size, final int fractionDigits ) {
		if ( format == SizeFormat.AUTO ) {
			if ( size < 1000 )
				format = SizeFormat.BYTES;
			else if ( ( size >> 10 ) < 1000 )
				format = SizeFormat.KB;
			else if ( ( size >> 20 ) < 1024 )
				format = SizeFormat.MB;
			else if ( ( size >> 30 ) < 1024 )
				format = SizeFormat.GB;
			else
				format = SizeFormat.TB;
		}
		
		switch ( format ) {
		default : // default is only a syntactical requirement, we've covered all cases...
		case BYTES : return size == 0 ? ZERO_BYTES : CAH.formatNumber( size              ) + Utils.SPACE_STRING + UNIT_BYTES;
		case KB    : return CAH.formatNumber( size /             1_024.0, fractionDigits ) + Utils.SPACE_STRING + UNIT_KB;
		case MB    : return CAH.formatNumber( size /         1_048_576.0, fractionDigits ) + Utils.SPACE_STRING + UNIT_MB;
		case GB    : return CAH.formatNumber( size /     1_073_741_824.0, fractionDigits ) + Utils.SPACE_STRING + UNIT_GB;
		case TB    : return CAH.formatNumber( size / 1_099_511_627_776.0, fractionDigits ) + Utils.SPACE_STRING + UNIT_TB;
		}
	}
	
}
