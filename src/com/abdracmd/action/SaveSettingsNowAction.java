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

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;

/**
 * Save settings now action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class SaveSettingsNowAction extends BaseAction {
	
	/** Singleton instance. */
	public static final SaveSettingsNowAction INSTANCE = new SaveSettingsNowAction();
	
	/**
	 * Creates a new SaveSettingsNowAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private SaveSettingsNowAction() {
		super( XIcon.F_DISK, TextKey.ACTION$SAVE_SETTINGS_NOW );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	CAH.getSettings().saveSettings();
    }
	
}
