/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.regnote;

import com.abdracmd.smp.dialog.BaseDialogShell;
import com.abdracmd.smp.dialog.BaseDialogModel;

/**
 * This is the shell of the Registration note dialog.
 * 
 * @author Andras Belicza
 */
public class RegNoteShell extends BaseDialogShell< BaseDialogModel, RegNotePresenter > {
	
    /**
     * Creates a new RegNoteShell.
     */
    public RegNoteShell() {
    	super( new BaseDialogModel() );
    	
    	presenter = new RegNotePresenter( this );
    	
    	presenter.packAndshow();
    }
	
}
