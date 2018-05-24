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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IRowContext;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base.BaseColumn;

/**
 * MIME type column.
 * 
 * @author Andras Belicza
 */
public class MimeTypeColumn extends BaseColumn< String > {
	
    /**
     * Creates a new MimeTypeColumn.
     */
    public MimeTypeColumn() {
    	super( TextKey.FOLDER_TABLE$COLUMN$MIME_TYPE, TextKey.FOLDER_TABLE$COLUMN$MIME_TYPE_DESC, String.class, Type.NORMAL );
    }
	
	@Override
	public String getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
		try {
	        return Files.probeContentType( path );
        } catch ( final IOException ie ) {
        	 MainShell.INSTANCE.getModel().getLogging().warning( "Could not probe content: " + path, ie );
        	return null;
        }
	}
	
}
