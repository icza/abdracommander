/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe;

import com.abdracmd.smp.BaseModel;
import com.abdracmd.smp.mainframe.folderstabbed.FoldersTabbedShell;

/**
 * This is the Main frame model.
 * 
 * @author Andras Belicza
 */
class MainFrameModel extends BaseModel {
	
	/** Frame sequence number. */
	protected volatile Integer frameSequenceNumber;
	
	/** Current job's completion percent. */
	protected volatile Integer completionPercent;
	
	/** Left folders tabbed shell.                    */
	protected final FoldersTabbedShell leftFoldersTabbedShell;
	/** Right folders tabbed shell.                   */
	protected final FoldersTabbedShell rightFoldersTabbedShell;
	/** Reference to the active folders tabbed shell. */
	protected FoldersTabbedShell       activeFoldersTabbedShell;
	
	/**
	 * Creates a new MainFrameModel.
	 * @param shell reference to the shell
	 */
	protected MainFrameModel( final MainFrameShell shell ) {
		// Shell's model ref is used before this constructor could complete... so:
		shell.setModelRef( this );
		
		// Instantiate RIGHT folders tabbed shell first, because we want the LEFT shell to be focused when a window is opened.
		// Since shells request focus on their table, the shell that was created later will have the focus in the end.
		rightFoldersTabbedShell = new FoldersTabbedShell( shell );
		leftFoldersTabbedShell  = new FoldersTabbedShell( shell );
	}
	
}
