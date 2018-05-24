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
 * Show Tool bar action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ShowToolBarAction extends BaseAction {
	
	/** Singleton instance. */
	public static final ShowToolBarAction INSTANCE = new ShowToolBarAction();
	
	/**
	 * Creates a new ShowToolBarAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private ShowToolBarAction() {
		super( XIcon.F_UI_TOOLBAR, TextKey.ACTION$SHOW_TOOL_BAR );
		
		putValue( SELECTED_KEY, CAH.get( SettingKeys.MAIN_FRAME$SHOW_COMMAND_BAR ) );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	CAH.set( SettingKeys.MAIN_FRAME$SHOW_TOOL_BAR, (Boolean) getValue( SELECTED_KEY ) );
    	
    	MainShell.INSTANCE.getActiveMainFrameShell().getPresenter().refreshToolBarVisibility();
    }
	
}
