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

import javax.swing.Box;
import javax.swing.JLabel;

import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderPresenter;

/**
 * A tab associated with a {@link FolderPresenter}.
 * 
 * @author Andras Belicza
 */
class FolderTab {
	
	/** Reference to the folder shell.               */
	public final FolderShell folderShell;
	/** Title box used as the tab (title) component. */
	public final Box              tabTitleBox = Box.createHorizontalBox();
	/** Label used in the tab's title.               */
	public final JLabel           titleLabel  = new JLabel();
	
	/**
	 * Creates a new FolderTab.
	 * @param folderShell reference to the folder shell
	 */
	public FolderTab( final FolderShell folderShell ) {
		this.folderShell = folderShell;
		
		tabTitleBox.add( titleLabel );
	}
	
}
