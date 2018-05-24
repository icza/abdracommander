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
import java.util.Date;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IRowContext;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base.BaseColumn;

/**
 * Last modified time column.
 * 
 * @author Andras Belicza
 */
public class DateColumn extends BaseColumn< Date > {
	
    /**
     * Creates a new DateColumn.
     */
    public DateColumn() {
    	super( TextKey.FOLDER_TABLE$COLUMN$DATE, TextKey.FOLDER_TABLE$COLUMN$DATE_DESC, Date.class, Type.NORMAL );
    }
	
	@Override
	public Date getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
		return new Date( attrs.lastModifiedTime().toMillis() );
	}
	
}
