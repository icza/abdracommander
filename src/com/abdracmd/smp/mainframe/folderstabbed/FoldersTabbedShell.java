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

import java.nio.file.Path;

import javax.swing.JTable;

import com.abdracmd.smp.BaseShell;
import com.abdracmd.smp.mainframe.MainFrameShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderShell;

/**
 * Folders tabbed shell.
 * 
 * @author Andras Belicza
 */
public class FoldersTabbedShell extends BaseShell< FoldersTabbedModel, FoldersTabbedPresenter > {
	
    /**
     * Creates a new FolderTabbedShell.
     * @param mainFrameShell reference to the owner main frame shell
     */
    public FoldersTabbedShell( final MainFrameShell mainFrameShell ) {
    	super( new FoldersTabbedModel( mainFrameShell ) );
    	
    	presenter = new FoldersTabbedPresenter( this );
    }
    
    /**
     * Adds a new tab.
     * 
     * <p>If this method is ever made public (e.g. for a NewTabAction), folder tab list of the model has to be synchronized
     * (right now this method is only called from EDT)!</p>
     * 
     * @param path optional starting path for the new tab
     */
    protected void addNewTab( final Path path ) {
		// Add a new tab
		final FolderShell folderShell = path == null ? new FolderShell( model.mainFrameShell ) : new FolderShell( model.mainFrameShell, path );
		
		final FolderTab folderTab = new FolderTab( folderShell );
		model.getFolderTabList().add( folderTab );
		
		presenter.addFolderTab( folderTab );
    }
    
    /**
     * Removes a folder tab.
     * 
     * <p>If this method is ever made public (e.g. for a NewTabAction), folder tab list of the model has to be synchronized
     * (right this method is only called from EDT)!</p>
     * 
     * @param folderTab folder tab to be removed
     */
    protected void removeFolderTab( final FolderTab folderTab ) {
    	// Last tab cannot be removed!
    	if ( model.getFolderTabList().size() > 1 ) {
    		model.getFolderTabList().remove( folderTab );
    		presenter.removeFolderTab( folderTab );
    	}
    }
    
    /**
     * Returns the table component of the selected tab.<br>
     * This is a short-hand for {@link FoldersTabbedModel#getTable()} on our model.
     * @return the table component of the selected tab
     */
    public JTable getTable() {
    	return model.getTable();
    }
    
	/**
	 * Returns the folder shell of the selected folder tab.
	 * @return the folder shell of the selected folder tab
	 */
	public FolderShell getSelectedFolderShell() {
	    return model.selectedFolderTab == null ? null : model.selectedFolderTab.folderShell;
    }
    
}
