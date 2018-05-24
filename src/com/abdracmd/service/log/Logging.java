/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.service.log;

import hu.belicza.andras.util.Utils;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import com.abdracmd.Consts;
import com.abdracmd.service.settings.SettingKeys;

/**
 * Logging service.
 * 
 * @author Andras Belicza
 */
public class Logging {
	
	/** Log file name prefix. */
	public static final String LOG_FILE_NAME_PREFIX = Consts.APP_SHORT_NAME + Utils.SPACE_STRING;
	/** Log file extension. */
	public static final String LOG_FILE_EXTENSION   = "log";
	
	/** Logger. */
	private Logger   logger;
	
	/** The current logging level. */
	private LogLevel logLevel;
	
    /**
     * Creates a new Logging.
     * @param logger internal logger
     */
    public Logging( final Logger logger ) {
    	this.logger = logger;
    	logger.setUseParentHandlers( false );    // We have our own handlers
        setLogLevel( SettingKeys.APP$LOG_LEVEL.defaultValue );
    }
	
	/**
	 * Returns the internal logger.
	 * @return the internal logger
	 */
	public Logger getLogger() {
	    return logger;
    }
	
	/**
	 * Sets the internal logger.
	 * @param logger the internal logger to be set
	 */
	public void setLogger( final Logger logger ) {
	    this.logger = logger;
    }
	
	/**
	 * Configures and adds a new handler to the internal logger.
	 * @param handler handler to be configured and added
	 */
	public void addHandler( final Handler handler ) {
        handler.setFormatter( new LogFormatter() );
        // FIXME: Java VM bug: without parenthesis it gives "Exception in thread "main" java.lang.VerifyError: Bad type on operand stack in method com.abdracmd.model.log.Logging.addHandler(Ljava/util/logging/Handler;)V at offset 18"
        // Remove later when the bug's fixed by Oracle...
        // Consider keeping this even if it's fixed so for those who do not update their Java this will continue to work. 
        handler.setLevel( ( SettingKeys.APP$LOG_LEVEL.defaultValue ).level );
        //handler.setLevel( SettingKeys.APP_LOG_LEVEL.defaultValue.level ); // This is how it should be...
        
        if ( handler instanceof FileHandler )
	        try {
	            ( (FileHandler) handler ).setEncoding( Consts.ENCODING );
            } catch ( final Exception e ) {
	            e.printStackTrace();
            }
        
        logger.addHandler( handler );
	}
	
	/**
	 * Sets a new logging level.<br>
	 * The level is set to the internal logger and to all its handlers.
	 * @param logLevel log level to be set
	 */
	public void setLogLevel( final LogLevel logLevel ) {
		this.logLevel = logLevel;
		
		logger.setLevel( logLevel.level );
		
		for ( final Handler handler : logger.getHandlers() )
			handler.setLevel( logLevel.level );
	}
	
	/**
	 * Returns the current logging level.
	 * @return the current logging level
	 */
	public LogLevel getLogLevel() {
		return logLevel;
	}
	
	/**
	 * Tests if the specified logging level is being logged based on the currently set logging level.
	 * @param logLevel logging level to be tested
	 * @return true if the specified logging level is being logged based on the currently set logging level
	 */
	public boolean testLevel( final LogLevel logLevel ) {
		return logLevel.compareTo( this.logLevel ) <= 0;
	}
	
	/**
	 * Logs an error message.
	 * @param message message
	 */
	public void error( final String message ) {
		error( message, null );
	}
	
	/**
	 * Logs an error message.
	 * @param message   message
	 * @param throwable throwable
	 */
	public void error( final String message, final Throwable throwable ) {
		logger.log( LogLevel.ERROR.level, message, throwable );
	}
	
	/**
	 * Logs a warning message.
	 * @param message message
	 */
	public void warning( final String message ) {
		warning( message, null );
	}
	
	/**
	 * Logs a warning message.
	 * @param message   message
	 * @param throwable throwable
	 */
	public void warning( final String message, final Throwable throwable ) {
		logger.log( LogLevel.WARNING.level, message, throwable );
	}
	
	/**
	 * Logs an info message.
	 * @param message message
	 */
	public void info( final String message ) {
		info( message, null );
	}
	
	/**
	 * Logs an info message.
	 * @param message   message
	 * @param throwable throwable
	 */
	public void info( final String message, final Throwable throwable ) {
		logger.log( LogLevel.INFO.level, message, throwable );
	}
	
	/**
	 * Logs a debug message.
	 * @param message message
	 */
	public void debug( final String message ) {
		debug( message, null );
	}
	
	/**
	 * Logs a debug message.
	 * @param message   message
	 * @param throwable throwable
	 */
	public void debug( final String message, final Throwable throwable ) {
		logger.log( LogLevel.DEBUG.level, message, throwable );
	}
	
	/**
	 * Logs a trace message.
	 * @param message message
	 */
	public void trace( final String message ) {
		trace( message, null );
	}
	
	/**
	 * Logs a trace message.
	 * @param message   message
	 * @param throwable throwable
	 */
	public void trace( final String message, final Throwable throwable ) {
		logger.log( LogLevel.TRACE.level, message, throwable );
	}
	
}
