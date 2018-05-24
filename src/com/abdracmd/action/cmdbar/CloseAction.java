/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.action.cmdbar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.abdracmd.action.BaseAction;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.main.MainShell;

/**
 * Close action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class CloseAction extends BaseAction {
	
	/** Singleton instance. */
	public static final CloseAction INSTANCE = new CloseAction();
	
	/**
	 * Creates a new CloseAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private CloseAction() {
		super( KeyStroke.getKeyStroke( KeyEvent.VK_F10, 0 ), XIcon.F_CROSS_BUTTON, TextKey.ACTION$CMD_BAR$CLOSE );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	MainShell.INSTANCE.getActiveMainFrameShell().shutdown();
    }
	
}
