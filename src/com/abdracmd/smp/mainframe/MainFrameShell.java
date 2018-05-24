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

import com.abdracmd.smp.BaseShell;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.folderstabbed.FoldersTabbedShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderShell;

/**
 * This is the shell of the main frame.
 * 
 * @author Andras Belicza
 */
public class MainFrameShell extends BaseShell< MainFrameModel, MainFramePresenter > {
	
	/** Active folders tabbed shell property. */
	public static final String PROP_ACTIVE_FOLDERS_TABBED_SHELL = "activeFoldersTabbedShell";
	
	
    /**
     * Creates a new MainFrameShell.
     */
    public MainFrameShell() {
    	model     = new MainFrameModel    ( this );
    	presenter = new MainFramePresenter( this );
    }
	
    /**
     * Sets the main frame model reference.<br>
     * This is called from the model's constructor, because this model ref needs to be set before the model's constructor is completed!
     * @param model reference to the main frame model
     */
    protected void setModelRef( final MainFrameModel model ) {
    	this.model = model;
    }
    
    /**
     * Shuts down this shell.
     */
    public void shutdown() {
    	if ( !MainShell.INSTANCE.checkMainFrameShellClosing() )
    		return;
    	
    	presenter.close();
    	
    	MainShell.INSTANCE.removeMainFrameShell( this );
    }
    
	/**
	 * Returns the left folders tabbed shell.
	 * @return the left folders tabbed shell
	 */
	public FoldersTabbedShell getLeftFoldersTabbedShell() {
	    return model.leftFoldersTabbedShell;
    }
	
	/**
	 * Returns the right folders tabbed shell.
	 * @return the right folders tabbed shell
	 */
	public FoldersTabbedShell getRightFoldersTabbedShell() {
	    return model.rightFoldersTabbedShell;
    }
	
	/**
	 * Returns the active folders tabbed shell.
	 * @return the active folders tabbed shell
	 */
	public FoldersTabbedShell getActiveFoldersTabbedShell() {
	    return model.activeFoldersTabbedShell;
    }
	
	/**
	 * Sets the active folders tabbed shell.
	 * 
	 * <p>Also fires a property change event with the property name {@link #PROP_ACTIVE_FOLDERS_TABBED_SHELL}.</p>
	 * 
	 * @param activeFoldersTabbedShell active folders tabbed shell to be set
	 */
	public void setActiveFoldersTabbedShell( final FoldersTabbedShell activeFoldersTabbedShell ) {
	    model.activeFoldersTabbedShell = activeFoldersTabbedShell;
	    firePropertyChange( PROP_ACTIVE_FOLDERS_TABBED_SHELL, null, activeFoldersTabbedShell );
    }
	
	/**
	 * Returns the non-active folders tabbed shell.
	 * @return the non-active folders tabbed shell
	 */
	public FoldersTabbedShell getNonActiveFoldersTabbedShell() {
	    return model.activeFoldersTabbedShell == model.leftFoldersTabbedShell ? model.rightFoldersTabbedShell : model.leftFoldersTabbedShell;
    }
	
    /**
     * Returns the active folder shell of the active folders tabbed shell.
     * @return the active folder shell of the active folders tabbed shell
     */
    public FolderShell getActiveFolderShell() {
   		return model.activeFoldersTabbedShell.getSelectedFolderShell();
    }
    
    /**
     * Returns the active folder shell of the non-active folders tabbed shell.
     * @return the active folder shell of the non-active folders tabbed shell
     */
    public FolderShell getNonActiveFolderShell() {
   		return getNonActiveFoldersTabbedShell().getSelectedFolderShell();
    }
    
    /**
     * Sets the frame sequence number.
     * @param number frame sequence number to be set
     */
    public void setFrameSequenceNumber( final Integer number ) {
    	model.frameSequenceNumber = number;
    	presenter.refreshTitle();
    }
    
	/**
	 * Sets the current job's completion percent.
	 * @param completionPercent the current job's completion percent to be set
	 */
	public void setCompletitionPercent( final Integer completionPercent ) {
		model.completionPercent = completionPercent;
		presenter.refreshTitle();
	}
	
}
