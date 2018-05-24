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
 * View action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ViewAction extends BaseAction {
	
	/** Singleton instance. */
	public static final ViewAction INSTANCE = new ViewAction();
	
	/**
	 * Creates a new ViewAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private ViewAction() {
		super( KeyStroke.getKeyStroke( KeyEvent.VK_F3, 0 ), XIcon.F_OCCLUDER, TextKey.ACTION$CMD_BAR$VIEW );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	System.out.println( "F3" );
    	MainShell.INSTANCE.getActiveMainFrameShell();
    }
	
}
