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
import com.abdracmd.smp.dialog.newfolder.NewFolderShell;

/**
 * New Folder action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class NewFolderAction extends BaseAction {
	
	/** Singleton instance. */
	public static final NewFolderAction INSTANCE = new NewFolderAction();
	
	/**
	 * Creates a new NewFolderAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private NewFolderAction() {
		super( KeyStroke.getKeyStroke( KeyEvent.VK_F7, 0 ), XIcon.NEW_FOLDER, TextKey.ACTION$CMD_BAR$NEW_FOLDER );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	new NewFolderShell();
    }
	
}
