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
 * About action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class SplitVerticallyAction extends BaseAction {
	
	/** Singleton instance. */
	public static final SplitVerticallyAction INSTANCE = new SplitVerticallyAction();
	
	/**
	 * Creates a new SplitVerticallyAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private SplitVerticallyAction() {
		super( XIcon.F_UI_SPLIT_PANEL, TextKey.ACTION$SPLIT_VERTICALLY );
		
		putValue( SELECTED_KEY, CAH.get( SettingKeys.MAIN_FRAME$SPLIT_VERTICALLY ) );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	CAH.set( SettingKeys.MAIN_FRAME$SPLIT_VERTICALLY, (Boolean) getValue( SELECTED_KEY ) );
    	
    	MainShell.INSTANCE.getActiveMainFrameShell().getPresenter().refreshSplitOrientation();
    }
	
}
