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
 * Move action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class MoveAction extends BaseAction {
	
	/** Singleton instance. */
	public static final MoveAction INSTANCE = new MoveAction();
	
	/**
	 * Creates a new MoveAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private MoveAction() {
		super( KeyStroke.getKeyStroke( KeyEvent.VK_F6, 0 ), XIcon.F_DOCUMENT_EXPORT, TextKey.ACTION$CMD_BAR$MOVE );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	System.out.println( "F6" );
    	MainShell.INSTANCE.getActiveMainFrameShell();
    }
	
}
