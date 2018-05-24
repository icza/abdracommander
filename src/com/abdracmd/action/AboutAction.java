/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.action;

import java.awt.event.ActionEvent;

import com.abdracmd.Consts;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.about.AboutShell;

/**
 * About action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class AboutAction extends BaseAction {
	
	/** Singleton instance. */
	public static final AboutAction INSTANCE = new AboutAction();
	
	/**
	 * Creates a new AboutAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private AboutAction() {
		super( XIcon.F_INFORMATION, TextKey.ACTION$ABOUT, Consts.APP_NAME );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	new AboutShell();
    }
	
}
