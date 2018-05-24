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
import com.abdracmd.smp.main.MainShell;

/**
 * Show Command bar action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ShowCommandBarAction extends BaseAction {
	
	/** Singleton instance. */
	public static final ShowCommandBarAction INSTANCE = new ShowCommandBarAction();
	
	/**
	 * Creates a new ShowCommandBarAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private ShowCommandBarAction() {
		super( XIcon.F_UI_STATUS_BAR, TextKey.ACTION$SHOW_COMMAND_BAR );
		
		putValue( SELECTED_KEY, CAH.get( SettingKeys.MAIN_FRAME$SHOW_COMMAND_BAR ) );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	CAH.set( SettingKeys.MAIN_FRAME$SHOW_COMMAND_BAR, (Boolean) getValue( SELECTED_KEY ) );
    	
    	MainShell.INSTANCE.getActiveMainFrameShell().getPresenter().refreshCommandBarVisibility();
    }
	
}
