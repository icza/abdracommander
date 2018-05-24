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

import hu.belicza.andras.util.Utils;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IRowContext;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base.BaseColumn;

/**
 * Extension column.
 * 
 * @author Andras Belicza
 */
public class ExtColumn extends BaseColumn< String > {
	
	/** Extension to represent a directory. */
	private static final String DIR_EXTENSION = "<DIR>";
	
    /**
     * Creates a new ExtColumn.
     */
    public ExtColumn() {
	    super( TextKey.FOLDER_TABLE$COLUMN$EXT, TextKey.FOLDER_TABLE$COLUMN$EXT_DESC, String.class, Type.REQUIRED );
    }
	
	@Override
	public String getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
	    if ( attrs.isDirectory() )
	    	return DIR_EXTENSION;
	    else {
			return Utils.getFileExt( rowContext.getSharedFileName() );
	    }
	}
	
}
