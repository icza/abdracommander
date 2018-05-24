/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.context.impl.BaseContext;

import java.io.File;
import java.nio.file.Path;

import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IRowContext;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.SharedDataKey;

/**
 * An {@link IRowContext} implementation.
 * 
 * @author Andras Belicza
 */
public class RowContext extends BaseContext implements IRowContext {
	
	/** Path which is associated with the row context. */
	private Path path;
	
	/**
	 * Initializes the row context, preparing it to be reused for "contexting" a new row.
	 * @param path path which is associated with the row context
	 * @return a transformed path which will be used if it needed correction; else the path that was handed to us
	 */
	public Path init( final Path path ) {
		init();
		this.path = path;
		
    	// ZipFileSystem returns folder entries having names ending with slash. This is not how non VFS folders are displayed.
		// Also this causes trouble when trying to focus a path, so remove these ending slashes.
		if ( Utils.isVirtualFS( path ) ) {
    		final String fileName = getSharedFileName();
    		if ( fileName.endsWith( "/" ) ) {
    			final String correctedFileName = fileName.substring( 0, fileName.length() - 1 );
    			put( SharedDataKey.FILE_NAME, correctedFileName );
    			this.path = path.resolveSibling( correctedFileName );
    		}
    	}
		
		return this.path;
	}
	
	@Override
	public File getSharedFile() {
		File file = get( SharedDataKey.FILE, File.class );
		
		if ( file == null )
			try {
				put( SharedDataKey.FILE, file = path.toFile() );
			} catch ( final UnsupportedOperationException uoe ) {
			}
		
		return file;
	}
	
	@Override
	public String getSharedFileName() {
		String fileName = get( SharedDataKey.FILE_NAME, String.class );
		
		if ( fileName == null )
			put( SharedDataKey.FILE_NAME, fileName = Utils.getFileName( path ) );
		
		return fileName;
	}
	
}
