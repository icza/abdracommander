/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.filechooser.FileSystemView;

/**
 * Info bean about a root.
 * 
 * @author Andras Belicza
 */
class RootInfo {
	
	/** Root path.                              */
	public final Path   root;
	/** String representation of the root path. */
	public final String rootString;
	/** System display name.                    */
	public final String systemDisplayName;
	/** Total space in bytes.                   */
	public Long         totalSpace;
	/** Free space in bytes.                    */
	public Long         freeSpace;
	
	/**
	 * Creates a new RootInfo.
	 * @param root root path
	 */
	public RootInfo( final Path root ) {
		this.root  = root;
		rootString = root.toString();
		
		systemDisplayName = FileSystemView.getFileSystemView().getSystemDisplayName( root.toFile() );
		
		try {
			totalSpace = Files.getFileStore( root ).getTotalSpace();
			freeSpace  = Files.getFileStore( root ).getUnallocatedSpace();
		} catch ( final IOException ie ) {
			// For example "Drive not ready" or subst'ed drive
		}
	}
	
}
