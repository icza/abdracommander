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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Log formatter.
 * 
 * @author Andras Belicza
 */
public class LogFormatter extends Formatter {
	
	/** Date+time format.     */
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat( "yy-MM-dd HH:mm:ss " );
	
	/**
	 * A map from level to our log level name (because we want our log level to be logged).
	 * The name is also post-pended with a separator string that is always appended when formatting a log record.
	 */
	private static final Map< Level, String > LEVEL_NAME_MAP = new HashMap<>();
	static {
		for ( final LogLevel logLevel : LogLevel.values() )
			LEVEL_NAME_MAP.put( logLevel.level, logLevel.name() + ": " );
	}
	
	/** Date to be formatted.        */
	private final Date          date    = new Date( 0 );
	/** Builder to build the output. */
	private final StringBuilder builder = new StringBuilder();
	
	/** String buffer used to format dates, for better performance. */
	private final StringBuffer  dateStringBuffer = new StringBuffer();
	/** Field position required by the date formatting.             */
	// Use the year field because it is the first in our pattern, so field position will be handled early and cost of further checks will be minimized. 
	private final FieldPosition dfFieldPosition  = new FieldPosition( DateFormat.YEAR_FIELD );
	
    @Override
    public synchronized String format( final LogRecord record ) {
    	date.setTime( record.getMillis() );
    	builder.setLength( 0 );
    	
    	dateStringBuffer.setLength( 0 );
    	builder.append( DATE_FORMAT.format( date, dateStringBuffer, dfFieldPosition ) ).append( LEVEL_NAME_MAP.get( record.getLevel() ) );
    	
    	builder.append( record.getMessage() ).append( '\n' );
    	
    	final Throwable thrown = record.getThrown();
    	if ( thrown != null ) {
            final StringWriter sw = new StringWriter();
            try ( final PrintWriter pw = new PrintWriter( sw ) ) {
            	thrown.printStackTrace( pw );
            }
            builder.append( sw.toString() );
    	}
    	
    	return builder.toString();
    }
    
}
