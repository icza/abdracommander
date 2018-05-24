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
import com.abdracmd.smp.dialog.multipage.settings.SettingsShell;

/**
 * Settings action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class SettingsAction extends BaseAction {
	
	/** Singleton instance. */
	public static final SettingsAction INSTANCE = new SettingsAction();
	
	/**
	 * Creates a new SettingsAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private SettingsAction() {
		super( KeyStroke.getKeyStroke( KeyEvent.VK_P, InputEvent.CTRL_MASK ), XIcon.F_GEAR, TextKey.ACTION$SETTINGS );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	new SettingsShell();
    }
	
}
