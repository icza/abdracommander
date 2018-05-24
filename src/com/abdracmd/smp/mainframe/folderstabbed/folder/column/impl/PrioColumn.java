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
 * Priority column.
 * 
 * <p>This column is hidden and its only purpose is to help sorting directories.</p>
 * 
 * @author Andras Belicza
 */
public class PrioColumn extends BaseColumn< PrioColumn.Priority > {
	
	/**
	 * Possible priorities.
	 * @author Andras Belicza
	 */
	public static enum Priority {
		/** Highest priority: parent folder row. */
		PARENT_FOLDER,
		/** High priority: folder row.           */
		FOLDER,
		/** Low priority: file row.              */
		FILE;
	}
	
    /**
     * Creates a new PrioColumn.
     */
    public PrioColumn() {
    	super( Utils.EMPTY_STRING, Utils.EMPTY_STRING, Priority.class, Type.INTERNAL );
    }
	
	@Override
	public Priority getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
	    return attrs.isDirectory() ? Priority.FOLDER : Priority.FILE;
	}
	
}
