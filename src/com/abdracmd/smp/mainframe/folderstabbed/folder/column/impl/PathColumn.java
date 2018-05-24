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

import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IRowContext;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base.BaseColumn;

/**
 * Path column.
 * 
 * <p>This column is hidden and its only purpose is to store the Path object for rows.</p>
 * 
 * @author Andras Belicza
 */
public class PathColumn extends BaseColumn< Path > {
	
    /**
     * Creates a new PathColumn.
     */
    public PathColumn() {
    	super( Utils.EMPTY_STRING, Utils.EMPTY_STRING, Path.class, Type.INTERNAL );
    }
	
	@Override
	public Path getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
		return path;
	}
	
}
