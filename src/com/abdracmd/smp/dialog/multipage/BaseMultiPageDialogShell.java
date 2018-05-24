/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage;

import java.util.List;

import com.abdracmd.smp.dialog.BaseDialogShell;

/**
 * This is the base shell of multi-page dialogs.
 * 
 * @param < Model     > model     type associated with this multi-page dialog shell
 * @param < Presenter > presenter type associated with this multi-page dialog shell
 * 
 * @author Andras Belicza
 */
public class BaseMultiPageDialogShell< Model extends BaseMultiPageDialogModel, Presenter extends BaseMultiPageDialogPresenter< Model, ? extends BaseMultiPageDialogShell< Model, Presenter > > >
	extends BaseDialogShell< Model, Presenter > {
	
    /**
     * Creates a new BaseMultiPageDialogShell.
     */
    public BaseMultiPageDialogShell() {
    }
    
    /**
     * Creates a new BaseMultiPageDialogShell.
     * @param model reference to the model
     */
    public BaseMultiPageDialogShell( final Model model ) {
    	super( model );
    }
    
	/**
	 * Sets the page list to be included and rebuilds the presenter's page tree (if the presenter has already been set).
	 * @param pageList the page list to be included
	 */
	public void setPageList( final List< ? extends IPage > pageList ) {
		model.pageList = pageList;
		
		if ( presenter != null )
			presenter.rebuildPageTree();
	}
	
}
