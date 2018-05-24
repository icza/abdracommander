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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IRowContext;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base.BaseColumn;

/**
 * Owner column.
 * 
 * @author Andras Belicza
 */
public class OwnerColumn extends BaseColumn< String > {
	
    /**
     * Creates a new OwnerColumn.
     */
    public OwnerColumn() {
    	super( TextKey.FOLDER_TABLE$COLUMN$OWNER, TextKey.FOLDER_TABLE$COLUMN$OWNER_DESC, String.class, Type.NORMAL );
    }
	
	@Override
	public String getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
		try {
	        return Files.getOwner( path ).getName();
        } catch ( final Exception e ) {
        	// Silently ignore
        	return null;
        }
	}
	
}
