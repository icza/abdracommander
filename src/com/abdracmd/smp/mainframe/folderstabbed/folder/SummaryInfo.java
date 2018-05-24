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

/**
 * Summary info about a file list like selected total files/folders count, selected size etc.
 * 
 * @author Andras Belicza
 */
class SummaryInfo {
	
	/** Total files count.      */
	public int filesCount;
	/** Total folders count.    */
	public int foldersCount;
	/** Selected files count.   */
	public int selectedFilesCount;
	/** Selected folders count. */
	public int selectedFoldersCount;
	
	/** Total size in bytes.    */
	public long totalSize;
	/** Selected size in bytes. */
	public long selectedSize;
	
	/**
	 * Resets all values.
	 */
	public void reset() {
		filesCount           = 0;
		foldersCount         = 0;
		selectedFilesCount   = 0;
		selectedFoldersCount = 0;
		
		totalSize            = 0L;
		selectedSize         = 0L;
	}
	
}
