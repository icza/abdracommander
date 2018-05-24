/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.newfolder;

import com.abdracmd.smp.dialog.BaseDialogShell;
import com.abdracmd.smp.dialog.BaseDialogModel;

/**
 * This is the shell of the New folder dialog.
 * 
 * @author Andras Belicza
 */
public class NewFolderShell extends BaseDialogShell< BaseDialogModel, NewFolderPresenter > {
	
    /**
     * Creates a new NewFolderShell.
     */
    public NewFolderShell() {
    	super( new BaseDialogModel() );
    	
    	presenter = new NewFolderPresenter( this );
    	
    	presenter.packAndshow();
    }
	
}
