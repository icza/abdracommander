/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IRowContext;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base.BaseColumn;

/**
 * Name column.
 * 
 * @author Andras Belicza
 */
public class NameColumn extends BaseColumn< String > {
	
    /**
     * Creates a new NameColumn.
     */
    public NameColumn() {
    	super( TextKey.FOLDER_TABLE$COLUMN$NAME, TextKey.FOLDER_TABLE$COLUMN$NAME_DESC, String.class, Type.REQUIRED );
    }
	
	@Override
	public String getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
		final String fileName = rowContext.getSharedFileName();
		
	    if ( attrs.isDirectory() )
	    	return fileName;
	    else {
    		final int lastDotIdx = fileName.lastIndexOf( '.' );
    		// If file name starts with a dot, do not treat that as name-extension separator
    		return lastDotIdx > 0 ? fileName.substring( 0, lastDotIdx ) : fileName;
	    }
	}
	
}
