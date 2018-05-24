/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.startup;

import hu.belicza.andras.util.NormalThread;
import hu.belicza.andras.util.Utils;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.abdracmd.AbdraCmd;
import com.abdracmd.Consts;
import com.abdracmd.service.icon.Icons;
import com.abdracmd.service.language.Language;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.log.LogLevel;
import com.abdracmd.service.log.Logging;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.service.settings.Settings;
import com.abdracmd.smp.BaseShell;
import com.abdracmd.smp.BaseModel;
import com.abdracmd.smp.dialog.multipage.about.AboutShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderPresenter;
import com.abdracmd.util.AcUtils;

/**
 * Shell of the startup process.
 * 
 * @author Andras Belicza
 */
public class StartupShell extends BaseShell< BaseModel, StartupPresenter > {
	
	/** Active log file path. */
	private Path activeLogFilePath;
	
	/** Future task loading registration info. */
	private FutureTask< Void > futureRegInfoLoadTask;
	
	/**
	 * Creates a new StartupShell.
	 */
	public StartupShell() {
	}
	
	/**
	 * Launches the startup process.
	 * @throws IllegalStateException if the current startup phase is not {@link StartupPhase#NOT_STARTED}
	 */
	public synchronized void initiate() {
		if ( mainModel.getStartupPhase() != StartupPhase.NOT_STARTED )
			throw new IllegalStateException( "Startup process can only be started once!" );
		
		// First set up logging so we will see logs if error occurs during the startup process...
		setupLogging();
		
		try ( final StartupPresenter sp = presenter = StartupPresenter.create( this ) ) {
			
			// Load "English_US" as default language
			// This must be the first, and startup phase cannot be set prior
			// because that would require getting the text for the StartupPhase enum value which requires a language... 
			mainModel.setLanguage( new Language( "English_US" ) ); // Use explicitly "English_US" instead of default app language setting (which might be changed later for localized downloads...)
			if ( !getLanguage().load() ) {
                error( "Failed to initialize default language!" );
                throw new RuntimeException();
			}
			
			// A language is loaded now:
			presenter.displayCopyrightText();
			
			changeStartupPhase( StartupPhase.STARTED );
			
			// Settings are the second most important, because everything might use settings...
			changeStartupPhase( StartupPhase.INITIALIZING_SETTINGS );
			initializeSettings();
			
			// Loading registration info requires time-consuming RSA decryption.
			// This does not rely on anything, so start this as soon as possible to minimize required time to wait for its completion.
			// Initiate parallel, asynchronous registration info loading
			if ( Files.exists( Consts.PATH_REG_INFO_DATA ) )
				initiateAsyncRegInfoLoading();
			
			deleteOldLogFiles();
			
			// Load chosen language if not the setting default
			if ( !mainModel.getLanguage().name.equals( get( SettingKeys.APP$LANGUAGE ) ) ) {
				changeStartupPhase( StartupPhase.INITIALIZING_LANGUAGE );
				final Language chosenLanguage = new Language( get( SettingKeys.APP$LANGUAGE ), mainModel.getLanguage() );
				if ( chosenLanguage.load() )
					mainModel.setLanguage( chosenLanguage );
			}
			
			AcUtils.initCachedTexts();
			
			// Trace all settings after loading the language, so the enum settings will be in the selected language 
			if ( testLevel( LogLevel.TRACE ) ) {
				trace( "ALL SETTINGS:" );
				trace( getSettings().packAllSettings() );
			}
			
			// Load icons
			changeStartupPhase( StartupPhase.INITIALIZING_ICONS );
			mainModel.setIcons( new Icons() );
			
			// Initialize built-in registries
			changeStartupPhase( StartupPhase.INITIALIZING_BUILTIN_REGISTRIES );
			initializeBuiltinRegistries();
			
			// TODO Initialize plugins... (if reg info is published to plugin API, put this section after reg info is loaded)
			
			// Wait for completion of registration info loading
			if ( futureRegInfoLoadTask != null ) {
				changeStartupPhase( StartupPhase.LOADING_REGISTRATION_INFO );
				try {
					futureRegInfoLoadTask.get();
				} catch ( final InterruptedException | ExecutionException e ) {
					error( "Unexpected error!", e );
				}
			}
			
			changeStartupPhase( StartupPhase.FINISHED );
			
			// Closing the StartupDisplay does not hide the splash screen...
			 
			if ( presenter != null )
				presenter.displayText( get( TextKey.STARTUP$INITIALIZING_MAIN_WINDOW ) );
		}
	}
	
	/**
	 * Sets up application logging.
	 */
	private void setupLogging() {
		final Logging logging = new Logging( Logger.getLogger( AbdraCmd.class.getName() ) );
		mainModel.setLogging( logging );
		
		if ( Consts.DEV_MODE ) {
			logging.addHandler( new ConsoleHandler() );
		}
		else {
			activeLogFilePath = Consts.PATH_LOGS.resolve( Logging.LOG_FILE_NAME_PREFIX + new SimpleDateFormat( "yyyy-MM-dd HH_mm_ss" ).format( new Date() ) + "."+ Logging.LOG_FILE_EXTENSION );
			
			try {
				Files.createDirectories( Consts.PATH_LOGS );
				logging.addHandler( new FileHandler( activeLogFilePath.toString() ) );
            } catch ( final Exception e ) {
            	System.err.println( "Failed to setup file logger, reverting to console logging: " + activeLogFilePath );
	            e.printStackTrace();
	            
				logging.addHandler( new ConsoleHandler() );
            }
		}
	}
	
	/**
	 * Initializes settings.
	 */
	private void initializeSettings() {
		mainModel.setSettings( new Settings() );
		getSettings().loadSettings();
		
		AcUtils.initializeGlobalSettingSetChangeListener();
	}
	
	/**
	 * Initiates a parallel, asynchronous registration info loading.
	 */
	private void initiateAsyncRegInfoLoading() {
        debug( "Initiating asynchronous registration info loading..." );
        
		futureRegInfoLoadTask = new FutureTask<>( new Callable< Void >() {
			@Override
			public Void call() throws Exception {
				AcUtils.loadRegistrationInfo();
				return null;
			}
		} );
		
		new NormalThread( futureRegInfoLoadTask, "Async registration info loading" ).start();
	}
	
	/**
	 * Delete old log files based on user setting.
	 */
	private void deleteOldLogFiles() {
		final long   oldestAllowedTime = System.currentTimeMillis() - get( SettingKeys.APP$LOG_FILE_LIFETIME ) * 24L*60*60*1000;
		final String logFileEnding     = "." + Logging.LOG_FILE_EXTENSION;
		
		try {
			Files.walkFileTree( Consts.PATH_LOGS, Utils.< FileVisitOption >getEmptySet(), 1, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
					// Check file name pattern (prefix, extension)
					final String fileName = Utils.getFileName( file );
					if ( !fileName.startsWith( Logging.LOG_FILE_NAME_PREFIX ) || !fileName.endsWith( logFileEnding ) )
						return FileVisitResult.CONTINUE;
					// Also do not delete current log file
					if ( file.equals( activeLogFilePath ) )
						return FileVisitResult.CONTINUE;
					
					// Also delete empty log files (regardless of age)
					final boolean isEmpty = attrs.size() == 0;
					if ( isEmpty || attrs.lastModifiedTime().toMillis() < oldestAllowedTime ) {
						debug( "Deleting " + ( isEmpty ? "empty" : "old" ) + " log file: " + file );
						Files.delete( file );
					}
					return FileVisitResult.CONTINUE;
				}
			} );
		} catch ( final IOException ie ) {
			warning( "Error when deleting old log files.", ie );
		}
	}
	
	/**
	 * Initializes built-in registries.
	 */
	private void initializeBuiltinRegistries() {
		FolderPresenter     .registerBuiltinColumnImpls();
		FolderPresenter     .registerBuiltinThemes     ();
		FolderPresenter     .registerFileOps           ();
		AboutShell          .registerBuiltinAboutPages ();
	}
	
	/**
	 * Changes the startup phase.
	 * @param newPhase new startup phase to change to
	 */
	private void changeStartupPhase( final StartupPhase newPhase ) {
		debug( "Entering startup phase: " + newPhase );
		
		mainModel.enterStartupPhase( newPhase );
		
		if ( presenter != null )
			presenter.displayPhase( newPhase );
	}
	
}
