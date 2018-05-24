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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import javax.swing.filechooser.FileSystemView;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IRowContext;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base.BaseColumn;

/**
 * Type description column.
 * 
 * @author Andras Belicza
 */
public class TypeDescColumn extends BaseColumn< String > {
	
	/** Reference to the file system view. */
	private static final FileSystemView FSV = FileSystemView.getFileSystemView();
	
    /**
     * Creates a new TypeDescColumn.
     */
    public TypeDescColumn() {
	    super( TextKey.FOLDER_TABLE$COLUMN$TYPE, TextKey.FOLDER_TABLE$COLUMN$TYPE_DESC, String.class, Type.NORMAL );
    }
	
	@Override
	public String getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
		// TODO implement caching (similar to system icons; from extension, folder, root etc)
		final File file = rowContext.getSharedFile();
		return file == null ? null : FSV.getSystemTypeDescription( file );
	}
	
}
