/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog;

import javax.swing.JDialog;

import com.abdracmd.smp.BaseShell;

/**
 * This is the base shell of dialog SMP components (whose presenter component is a {@link JDialog}).
 * 
 * @param < Model     > model     type associated with this base dialog shell
 * @param < Presenter > presenter type associated with this base dialog shell
 * 
 * @author Andras Belicza
 */
public class BaseDialogShell< Model extends BaseDialogModel, Presenter extends BaseDialogPresenter< Model, ? extends BaseDialogShell< Model, Presenter > > >
	extends BaseShell< Model, Presenter > {
	
    /**
     * Creates a new BaseDialogShell.
     */
    public BaseDialogShell() {
    }
    
    /**
     * Creates a new BaseDialogShell.
     * @param model reference to the model
     */
    public BaseDialogShell( final Model model ) {
    	super( model );
    }
    
}
