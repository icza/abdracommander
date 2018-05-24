/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.fileop;

import java.nio.file.Path;

import com.abdracmd.fileop.IFileOp;
import com.abdracmd.smp.dialog.BaseDialogModel;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderShell;

/**
 * This is the model of the File operation dialog.
 * 
 * @author Andras Belicza
 */
class FileOpModel extends BaseDialogModel {
	
	/** File operation to be executed. */
	protected final IFileOp fileOp;
	/** Input paths. */
	protected final Path[]  inputPaths;
	
	/** Folder shell to report the processed input files to. */
	protected final FolderShell folderShell;
	
    /**
     * Creates a new FileOpModel.
     * @param fileOp          file operation to be executed
     * @param inputPaths      input paths
     * @param folderShell     folder shell to report the processed input files to
     */
    protected FileOpModel( final IFileOp fileOp, final Path[] inputPaths, final FolderShell folderShell ) {
    	this.fileOp          = fileOp;
    	this.inputPaths      = inputPaths;
    	this.folderShell     = folderShell;
    }
	
}
