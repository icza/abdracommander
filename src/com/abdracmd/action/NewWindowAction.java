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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.main.MainShell;

/**
 * New window action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class NewWindowAction extends BaseAction {
	
	/** Singleton instance. */
	public static final NewWindowAction INSTANCE = new NewWindowAction();
	
	/**
	 * Creates a new NewWindowAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private NewWindowAction() {
		super( KeyStroke.getKeyStroke( KeyEvent.VK_N, InputEvent.CTRL_MASK ), XIcon.F_APPLICATION_PLUS, TextKey.ACTION$NEW_WINDOW );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	MainShell.INSTANCE.startNewMainFrameShell();
    }
	
}
