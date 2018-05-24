/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd;

import hu.belicza.andras.util.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.abdracmd.util.OperatingSystem;
import com.abdracmd.util.bean.person.Contact;
import com.abdracmd.util.bean.person.Contact.ContactType;
import com.abdracmd.util.bean.person.Person;
import com.abdracmd.util.bean.person.PersonNamePart;
import com.abdracmd.util.bean.person.PersonNamePart.PersonNamePartType;

/**
 * Application wide constants.
 * 
 * @author Andras Belicza
 */
public class Consts {
	
	/** Encoding used application wide. */
	public static final String ENCODING = "UTF-8";
	
	
	
	/** Running operating system. */
	public static final OperatingSystem OS = OperatingSystem.detect();
	
	/** Tells if application runs in developer mode.    */
	public static final boolean DEV_MODE      = System.getProperty( "com.abdracmd.dev-mode" ) != null;
	
	/** Tells in what way was the application launched. */
	public static final String  LAUNCHED_WITH = System.getProperty( "com.abdracmd.launched-with" );
	
	
	/** Default language name. */
	public static final String DEFAULT_LANGUAGE_NAME = "English_US";
	
	
	/** Application short name, used in file path names (therefore should never be changed!). */
	public static final String APP_SHORT_NAME = "Abdracmd";
	
	/** Application name.          */
	public static final String APP_NAME = "Abdra Commander";
	
	/** Application author.        */
	public static final Person APP_AUTHOR = new Person();
	static {
		APP_AUTHOR.addNamePart( new PersonNamePart( PersonNamePartType.FIRST, "András"  ) );
		APP_AUTHOR.addNamePart( new PersonNamePart( PersonNamePartType.LAST , "Belicza" ) );
		APP_AUTHOR.addContact( new Contact( ContactType.POSTAL_ADDRESS, "Budapest, Hungary" ) );
		APP_AUTHOR.addContact( new Contact( ContactType.EMAIL         , new String( new char[] { 'i', 'c', 'z', 'a', 'a', 'a', '@', 'g', 'm', 'a', 'i', 'l', '.', 'c', 'o', 'm' } ) ) );
		APP_AUTHOR.addContact( new Contact( ContactType.GOOGLE_PLUS   , "https://plus.google.com/u/0/117032417609730418656/posts" ) );
		APP_AUTHOR.addContact( new Contact( ContactType.FACEBOOK      , "https://www.facebook.com/andras.belicza.9" ) );
		APP_AUTHOR.addContact( new Contact( ContactType.YOUTUBE       , "http://www.youtube.com/user/iczaaa" ) );
		
		// Load app author photo
		try ( final InputStream           in     = Consts.class.getResourceAsStream( "app_author_photo.jpg" );
			  final ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
			int data;
			while ( ( data = in.read() ) != -1 )
				buffer.write( data );
			APP_AUTHOR.setPhotoImageData( buffer.toByteArray() );
		} catch ( final IOException ie ) {
			System.err.println( "Failed to load app author photo!" );
			ie.printStackTrace();
		}
	}
	
	/** Application major version.       */
	public static final int    APP_VERSION_MAJOR       = 0;
	/** Application minor version.       */
	public static final int    APP_VERSION_MINOR       = 6;
	/** Application maintenance version. */
	public static final int    APP_VERSION_MAINTENANCE = 0;
	/**
	 * Application build version.
	 * Loaded from the file <code>"build"</code>.
	 */
	public static final int    APP_VERSION_BUILD;
	/** Complete application version string. */
	public static final String APP_VERSION_STRING;
	/** Short application version string (only major and minor versions plus maintenance in case if it's not 0). */
	public static final String APP_VERSION_STRING_SHORT;
	/**
	 * Current version release date.
	 * Loaded from the file <code>"build"</code>.
	 */
	public static final Date   APP_RELEASE_DATE;
	/** Application release date+time pattern. */
	public static final String APP_RELEASE_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS [z XXX]";
	static {
		try ( final BufferedReader in = new BufferedReader( new InputStreamReader( Consts.class.getResourceAsStream( "build" ), ENCODING ) ) ) {
			APP_VERSION_BUILD = Integer.parseInt( in.readLine() );
			APP_RELEASE_DATE  = new SimpleDateFormat( APP_RELEASE_DATE_PATTERN ).parse( in.readLine() );
		} catch ( final Exception e ) {
	        throw new RuntimeException( e );
        }
		
		APP_VERSION_STRING = APP_VERSION_MAJOR + "." + APP_VERSION_MINOR
			+ "." + APP_VERSION_MAINTENANCE + "." + APP_VERSION_BUILD;
		
		// "new Object() == null" constant condition is there so no "Unused code" warning is given when maintenance version is 0
		APP_VERSION_STRING_SHORT = APP_VERSION_MAJOR + "." + APP_VERSION_MINOR
			+ ( new Object() == null || APP_VERSION_MAINTENANCE > 0 ? "." + APP_VERSION_MAINTENANCE : Utils.EMPTY_STRING );
	}
	
	
	
	/** Copyright first year info. */
	public static final int    COPYRIGHT_FIRST_YEAR = 2013;
	/** Copyright last year info.  */
	public static final int    COPYRIGHT_LAST_YEAR;
	/** Copyright string.          */
	public static final String COPYRIGHT_YEAR_INFO;
	static {
		final Calendar cal = Calendar.getInstance();
		cal.setTime( APP_RELEASE_DATE );
		COPYRIGHT_LAST_YEAR = cal.get( Calendar.YEAR );
		COPYRIGHT_YEAR_INFO = COPYRIGHT_FIRST_YEAR == COPYRIGHT_LAST_YEAR ? Integer.toString( COPYRIGHT_FIRST_YEAR )
				: COPYRIGHT_FIRST_YEAR + "-" + COPYRIGHT_LAST_YEAR;
	}
	
	
	
	/** Home page URL.         */
	public static final String URL_HOME_PAGE         = "https://sites.google.com/site/abdracommander/";
	/** Registration page URL. */
	public static final String URL_REGISTRATION_PAGE = "https://sites.google.com/site/abdracommander/registration";
	/** Forum URL.             */
	public static final String URL_FORUM             = "https://groups.google.com/d/forum/abdra-commander";
	
	
	
	/** Application path. */
	public static final Path PATH_APP       = Paths.get( "lib" ).toAbsolutePath().getParent();
	
	/** Application logs path. */
	public static final Path PATH_LANGUAGES = PATH_APP.resolve( "Languages" );
	
	/** Application logs path.       */
	public static final Path PATH_TEMP_DIR  = Paths.get( System.getProperty( "java.io.tmpdir" ), APP_SHORT_NAME );
	
	
	/**
	 * Possible locations of the user content. 
	 * @author Andras Belicza
	 */
	public enum UserContentLocation {
		/** User content is stored in the folder <code>"Abdracmd-User-Content"</code> in the user's home folder. */
		IN_USER_HOME_FOLDER,
		/** User content is stored in the folder <code>"User-Content"</code> in the application folder.          */
		IN_APP_FOLDER,
		/** User content is stored in a custom folder.                                                           */
		CUSTOM_FOLDER;
	}
	
	/**
	 * Tells the location of the user content location.
	 * This cannot be a setting because settings are also stored in this location,
	 * and we have to know where to load settings from...
	 * <p>This information is stored in a file called <code>user-content-location.xml</code> in the application folder.
	 * Defaults to {@link UserContentLocation#IN_USER_HOME_FOLDER} if it cannot be acquired from the file <code>user-content-location.xml</code>.</p>
	 * */
	public static UserContentLocation USER_CONTENT_LOCATION;
	/** User Content path. */
	public static final Path PATH_USER_CONTENT;
	static {
		// Acquire USER_CONTENT_LOCATION
		final Path userContentLocationSourcePath = PATH_APP.resolve( "user-content-location.xml" );
		
		Path customPath = null;
		try ( final InputStream xmlInput = Files.newInputStream( userContentLocationSourcePath ) ) {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments( true );
			final Document document = factory.newDocumentBuilder().parse( xmlInput );
			
			final Element docElement = document.getDocumentElement();
			
			final UserContentLocation userContentLocation = UserContentLocation.valueOf( docElement.getElementsByTagName( "locationType" ).item( 0 ).getTextContent().trim() );
			customPath = userContentLocation == UserContentLocation.CUSTOM_FOLDER ?
				Paths.get( docElement.getElementsByTagName( "customFolder" ).item( 0 ).getTextContent().trim() ) : null;
			
			
			if ( userContentLocation == UserContentLocation.CUSTOM_FOLDER )
				if ( customPath.toString().isEmpty() )
					throw new RuntimeException( "CUSTOM_FOLDER user content location is specified but custom folder is not specified!" );
			
			USER_CONTENT_LOCATION = userContentLocation;
			
		} catch ( final Exception e ) {
			System.err.println( "Failed to process \"" + userContentLocationSourcePath + "\" file, reverting to default IN_USER_HOME_FOLDER user content location!" );
			e.printStackTrace( System.err );
			
			USER_CONTENT_LOCATION = UserContentLocation.IN_USER_HOME_FOLDER;
		}
		
		// Set up PATH_USER_CONTENT based on USER_CONTENT_LOCATION
		switch ( USER_CONTENT_LOCATION ) {
		case IN_USER_HOME_FOLDER :
			PATH_USER_CONTENT = Paths.get( System.getProperty( "user.home" ), APP_SHORT_NAME + "-User-Content" ).toAbsolutePath();
			break;
		case IN_APP_FOLDER :
			PATH_USER_CONTENT = PATH_APP.resolve( "User-Content" );
			break;
		case CUSTOM_FOLDER :
			PATH_USER_CONTENT = customPath.toAbsolutePath();
			break;
		default :
			throw new Error( "Unhandled user content location type: " + USER_CONTENT_LOCATION );
		}
		if ( Files.exists( PATH_USER_CONTENT ) ) {
			if ( !Files.isDirectory( PATH_USER_CONTENT ) )
				throw new Error( "User content location is not a folder: " + PATH_USER_CONTENT );
		}
		else {
			try {
				Files.createDirectories( PATH_USER_CONTENT );
			} catch ( final Exception e ) {
				throw new Error( "Could not create user content location: " + PATH_USER_CONTENT );
			}
		}
		if ( !Files.isExecutable( PATH_USER_CONTENT ) )
			System.err.println( "WARNING! User content location is not accessible: " + PATH_USER_CONTENT );
	}
	
	/** Application logs path.       */
	public static final Path PATH_LOGS          = PATH_USER_CONTENT.resolve( "Logs" );
	
	/** Registration info data path. */
	public static final Path PATH_REG_INFO_DATA = PATH_USER_CONTENT.resolve( "reginfo.dat" );
	
}
