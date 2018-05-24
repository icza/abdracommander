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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import com.abdracmd.smp.BaseModel;
import com.abdracmd.smp.mainframe.MainFrameShell;

/**
 * Folders tabbed model.
 * 
 * @author Andras Belicza
 */
class FoldersTabbedModel extends BaseModel {
	
	/** Reference to the owner main frame shell. */
	protected final MainFrameShell mainFrameShell;
	
	/**
	 * Folder tab list of the panel.
	 * Since this list is access only from EDT (Event Dispatch Thread),
	 * no need extra synchronization.
	 */
	private final List< FolderTab > folderTabList  = new ArrayList<>();
	
	/** The selected folder tab.
	 * This is actually the selected <b>last</b> folder tab,
	 * useful when the newTab is selected and we need the last selected folder tab. */
	protected FolderTab selectedFolderTab;
	
    /**
     * Creates a new FoldersTabbedModel.
     * @param mainFrameShell reference to the owner main frame shell
     */
	protected FoldersTabbedModel( final MainFrameShell mainFrameShell ) {
		this.mainFrameShell = mainFrameShell;
    }
	
	/**
	 * Returns the folder tab list.
	 * @return the folder tab list
	 */
	protected List< FolderTab > getFolderTabList() {
	    return folderTabList;
    }
	
	/**
	 * Sets the selected folder tab.
	 * @param selectedFolderTab selected folder tab to be set
	 */
	protected void setSelectedFolderTab( final FolderTab selectedFolderTab ) {
	    this.selectedFolderTab = selectedFolderTab;
    }
	
    /**
     * Returns the table component of the selected tab.
     * @return the table component of the selected tab
     */
    public JTable getTable() {
    	return selectedFolderTab.folderShell.getPresenter().getTable();
    }
    
}
