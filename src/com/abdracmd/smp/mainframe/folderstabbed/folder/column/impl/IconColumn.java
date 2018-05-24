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

import javax.swing.Icon;

import com.abdracmd.service.icon.Icons;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IRowContext;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base.BaseColumn;

/**
 * Icon column.
 * 
 * @author Andras Belicza
 */
public class IconColumn extends BaseColumn< Icon > {
	
    /**
     * Creates a new IconColumn.
     */
    public IconColumn() {
    	super( TextKey.FOLDER_TABLE$COLUMN$ICON, TextKey.FOLDER_TABLE$COLUMN$ICON_DESC, Icon.class, Type.NORMAL );
    }
	
	@Override
	public Icon getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
		return Icons.getSystemIcon( path, attrs );
	}
	
}
