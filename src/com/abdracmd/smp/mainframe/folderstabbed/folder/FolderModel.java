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

import java.nio.file.Path;

import com.abdracmd.smp.BaseModel;
import com.abdracmd.smp.mainframe.MainFrameShell;

/**
 * Folder Model.
 * 
 * @author Andras Belicza
 */
class FolderModel extends BaseModel {
	
	/** Reference to the owner main frame shell. */
	protected final MainFrameShell mainFrameShell;
	
	/** Folder path. */
	protected Path path;
	
    /**
     * Creates a new FolderModel.
     * @param mainFrameShell reference to the owner main frame shell
     */
	protected FolderModel( final MainFrameShell mainFrameShell ) {
		this.mainFrameShell = mainFrameShell;
    }
	
}
