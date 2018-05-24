/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder;

import javax.swing.JTable;

import com.abdracmd.util.AcUtils;

/**
 * Auto resize mode.
 * 
 * @author Andras Belicza
 */
public enum AutoResizeMode {
	
	/** Off.                */
	OFF               ( JTable.AUTO_RESIZE_OFF                ),
	/** Next column.        */
	NEXT_COLUMN       ( JTable.AUTO_RESIZE_NEXT_COLUMN        ),
	/** Subsequent columns. */
	SUBSEQUENT_COLUMNS( JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS ),
	/** Last column.        */
	LAST_COLUMN       ( JTable.AUTO_RESIZE_LAST_COLUMN        ),
	/** All columns.        */
	ALL_COLUMNS       ( JTable.AUTO_RESIZE_ALL_COLUMNS        );
	
	/** Table constant associated with this auto resize mode. */
	public final int tableConst;
	
    /**
     * Creates a new AutoResizeMode.
     * @param tableConst table constant associated with this auto resize mode
     */
    private AutoResizeMode( final int tableConst ) {
    	this.tableConst = tableConst;
    }
	
    /**
     * Returns the auto resize mode specified by the table constant.
     * @param tableConst table constant to return the auto resize mode for
     * @return the auto resize mode specified by the table constant; or <code>null</code> if no such auto resize mode exists
     */
    public static AutoResizeMode getFromTableConst( final int tableConst ) {
    	for ( final AutoResizeMode autoResizeMode : values() )
    		if ( autoResizeMode.tableConst == tableConst )
    			return autoResizeMode;
    	
    	return null;
    }
    
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
