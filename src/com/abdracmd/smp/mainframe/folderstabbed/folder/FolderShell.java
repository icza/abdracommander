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

import java.nio.file.Path;
import java.nio.file.Paths;

import com.abdracmd.smp.BaseShell;
import com.abdracmd.smp.mainframe.MainFrameShell;

/**
 * Folder shell.
 * 
 * @author Andras Belicza
 */
public class FolderShell extends BaseShell< FolderModel, FolderPresenter > {
	
	/** Path property. */
	public static final String PROP_PATH = "path";
	
	
    /**
     * Creates a new FolderShell.
     * @param mainFrameShell reference to the owner main frame shell
     */
    public FolderShell( final MainFrameShell mainFrameShell ) {
    	this( mainFrameShell, Paths.get( "W:/" ) ); // TODO take path from setting
    }
    
    /**
     * Creates a new FolderShell.
     * @param mainFrameShell reference to the owner main frame shell
     * @param path           starting path for the new shell
     */
    public FolderShell( final MainFrameShell mainFrameShell, final Path path ) {
    	super( new FolderModel( mainFrameShell ) );
    	
    	model.path = path;
    	
    	presenter = new FolderPresenter( this );
    }
    
	/**
	 * Returns the folder path.
	 * @return the folder path
	 */
	public Path getPath() {
	    return model.path;
    }
	
	/**
	 * Sets the folder path.
	 * 
	 * <p>Also fires a property change event with the property name {@link #PROP_PATH}.</p>
	 * 
	 * @param path the folder path to be set
	 */
	public void setPath( final Path path ) {
		// Store the previous path to focus it after refresh.
		// This is useful so when the user goes up in the folder hierarchy, the folder he/she just went up will be focused.
		Path pathToFocus = model.path;
		
		if ( Utils.isVirtualFS( pathToFocus ) && !Utils.isVirtualFS( path ) ) {
			// Going out of a VFS, use the VFS source as path to focus
			pathToFocus = Utils.getVirtualFSSource( pathToFocus );
		}
		
		model.path = path;
		
		presenter.refresh( pathToFocus );
		
	    firePropertyChange( PROP_PATH, null, path );
    }
	
	/**
	 * Returns the owner main frame shell.
	 * @return the owner main frame shell
	 */
	public MainFrameShell getMainFrameShell() {
		return model.mainFrameShell;
	}
	
}
