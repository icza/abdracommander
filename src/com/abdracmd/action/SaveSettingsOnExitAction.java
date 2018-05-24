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
import com.abdracmd.service.settings.SettingKeys;

/**
 * Save settings on exit action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class SaveSettingsOnExitAction extends BaseAction {
	
	/** Singleton instance. */
	public static final SaveSettingsOnExitAction INSTANCE = new SaveSettingsOnExitAction();
	
	/**
	 * Creates a new SaveSettingsOnExitAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private SaveSettingsOnExitAction() {
		super( XIcon.F_DISK_ARROW, TextKey.ACTION$SAVE_SETTINGS_ON_EXIT );
		
		putValue( SELECTED_KEY, CAH.get( SettingKeys.APP$SAVE_SETTINGS_ON_EXIT ) );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	CAH.set( SettingKeys.APP$SAVE_SETTINGS_ON_EXIT, (Boolean) getValue( SELECTED_KEY ) );
    }
	
}
