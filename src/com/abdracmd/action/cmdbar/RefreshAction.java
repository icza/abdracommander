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
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.MainFrameShell;

/**
 * Refresh action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RefreshAction extends BaseAction {
	
	/** Singleton instance. */
	public static final RefreshAction INSTANCE = new RefreshAction();
	
	/**
	 * Creates a new RefreshAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private RefreshAction() {
		super( KeyStroke.getKeyStroke( KeyEvent.VK_F9, 0 ), XIcon.REFRESH, TextKey.ACTION$CMD_BAR$REFRESH );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	final MainFrameShell activeMainFrameShell = MainShell.INSTANCE.getActiveMainFrameShell();
    	
    	if ( CAH.get( SettingKeys.ROOTS_POPUP$REFRESH_ON_CMD_BAR_REFRESH ) ) {
	    	activeMainFrameShell.getLeftFoldersTabbedShell ().getPresenter().refreshRootsPopupMenu();
	    	activeMainFrameShell.getRightFoldersTabbedShell().getPresenter().refreshRootsPopupMenu();
    	}
    	
    	activeMainFrameShell.getActiveFolderShell().getPresenter().refresh();
    }
	
}
