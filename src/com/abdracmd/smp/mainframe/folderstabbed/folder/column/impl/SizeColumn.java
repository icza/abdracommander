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
 * Size column.
 * 
 * @author Andras Belicza
 */
public class SizeColumn extends BaseColumn< Long > {
	
    /**
     * Creates a new SizeColumn.
     */
    public SizeColumn() {
    	super( TextKey.FOLDER_TABLE$COLUMN$SIZE, TextKey.FOLDER_TABLE$COLUMN$SIZE_DESC, Long.class, Type.REQUIRED );
    }
    
	@Override
	public Long getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
		return Long.valueOf( attrs.size() );
	}
	
}
