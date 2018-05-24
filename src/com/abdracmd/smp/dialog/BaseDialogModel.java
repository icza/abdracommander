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

import java.awt.Window;

import com.abdracmd.smp.BaseModel;
import com.abdracmd.smp.main.MainShell;

/**
 * This is the model used by the {@link BaseDialogPresenter}.
 * 
 * @author Andras Belicza
 */
public class BaseDialogModel extends BaseModel {
	
	/** Reference to the parent window. */
	public final Window parentWindow;
	
	/**
	 * Creates a new BaseDialogModel.
     * The active main frame will be used as the parent window.
	 */
	public BaseDialogModel() {
		this( null );
	}
	
	/**
	 * Creates a new BaseDialogModel.
	 * @param parentWindow optional reference to the parent window; if <code>null</code>, the active main frame will be used as the parent window
	 */
	public BaseDialogModel( final Window parentWindow ) {
		this.parentWindow = parentWindow == null ? MainShell.INSTANCE.getActiveMainFrameShell().getPresenter().getComponent() : parentWindow;
	}
	
}
