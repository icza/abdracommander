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

import java.util.logging.Level;

import com.abdracmd.util.AcUtils;

/**
 * Log levels.
 * 
 * @author Andras Belicza
 */
public enum LogLevel {
	
	// Order is important! Must be from most severe to finest! 
	
	/** Error.   */
	ERROR  ( Level.SEVERE  ),
	/** Warning. */
	WARNING( Level.WARNING ),
	/** Info.    */
	INFO   ( Level.INFO    ),
	/** Debug.   */
	DEBUG  ( Level.FINE    ),
	/** Trace.   */
	TRACE  ( Level.FINEST  );
	
	/** The associated logger level. */
	public final Level level;
	
    /**
     * Creates a new LogLevel.
     * @param level the associated logger level
     */
    private LogLevel( final Level level ) {
    	this.level = level;
    }
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
