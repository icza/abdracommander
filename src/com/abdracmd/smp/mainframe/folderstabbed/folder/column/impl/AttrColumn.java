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
import java.nio.file.attribute.DosFileAttributes;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IRowContext;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base.BaseColumn;

/**
 * DOS Attributes column.
 * 
 * @author Andras Belicza
 */
public class AttrColumn extends BaseColumn< String > {
	
	/** Index mask for the System property. */
	private static final int MASK_SYSTEM    = 0x01;
	/** Index mask for the System property. */
	private static final int MASK_HIDDEN    = 0x02;
	/** Index mask for the System property. */
	private static final int MASK_ARCHIVE   = 0x04;
	/** Index mask for the System property. */
	private static final int MASK_READ_ONLY = 0x08;
	
	/** Cached attributes string for MAXIMUM performance! */
	private static String[] CACHED_ATTR_STRINGS = new String[ MASK_READ_ONLY << 1 ];
	static {
		for ( int i = 0; i < CACHED_ATTR_STRINGS.length; i++ ) {
			final StringBuilder sb = new StringBuilder();
			
			sb.append( ( i & MASK_READ_ONLY ) > 0 ? 'R' : '-' );
			sb.append( ( i & MASK_ARCHIVE   ) > 0 ? 'A' : '-' );
			sb.append( ( i & MASK_HIDDEN    ) > 0 ? 'H' : '-' );
			sb.append( ( i & MASK_SYSTEM    ) > 0 ? 'S' : '-' );
			
			CACHED_ATTR_STRINGS[ i ] = sb.toString();
		}
	}
	
    /**
     * Creates a new AttrColumn.
     */
    public AttrColumn() {
    	super( TextKey.FOLDER_TABLE$COLUMN$ATTR, TextKey.FOLDER_TABLE$COLUMN$ATTR_DESC, String.class, Type.NORMAL );
    }
	
	@Override
	public String getData( final Path path, final BasicFileAttributes attrs, final IRowContext rowContext ) {
		if ( !( attrs instanceof DosFileAttributes ) )
			return null;
		
		//final DosFileAttributes dosAttributes = attrs instanceof DosFileAttributes ? (DosFileAttributes) attrs : Files.readAttributes( path, DosFileAttributes.class );
		final DosFileAttributes dosAttributes = (DosFileAttributes) attrs;
		
		int index = 0;
		if ( dosAttributes.isReadOnly() )
			index |= MASK_READ_ONLY;
		if ( dosAttributes.isArchive () )
			index |= MASK_ARCHIVE;
		if ( dosAttributes.isHidden  () )
			index |= MASK_HIDDEN;
		if ( dosAttributes.isSystem  () )
			index |= MASK_SYSTEM;
		
        return CACHED_ATTR_STRINGS[ index ];
	}
	
}
