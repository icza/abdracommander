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

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.util.CommonAccessHelper;
import com.abdracmd.util.GuiUtils;

/**
 * Base of Actions.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public abstract class BaseAction extends AbstractAction {
	
	/** A common assess helper instance that may be used in actions. */
	protected static final CommonAccessHelper CAH = new CommonAccessHelper();
	
	/**
	 * Creates a new BaseAction.
	 * @param xicon   optional xicon of the action
	 * @param textKey text key of the action's text
	 * @param params  parameters of the action's text
	 */
	public BaseAction( final XIcon xicon, final TextKey textKey, final Object...params ) {
		this( null, xicon, textKey, params );
	}
	
	/**
	 * Creates a new BaseAction.
	 * @param hotkey  optional hotkey of the action
	 * @param xicon   optional xicon of the action
	 * @param textKey text key of the action's text
	 * @param params  parameters of the action's text
	 */
	public BaseAction( final KeyStroke hotkey, final XIcon xicon, final TextKey textKey, final Object...params ) {
		// Set hokey first because GuiUtils.initAction() uses it to build tool tip!
		if ( hotkey != null )
			putValue( ACCELERATOR_KEY, hotkey );
		
		GuiUtils.initAction( this, xicon, textKey, params );
	}
	
}
