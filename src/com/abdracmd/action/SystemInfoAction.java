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
import com.abdracmd.smp.dialog.multipage.about.AboutShell;
import com.abdracmd.smp.dialog.multipage.about.pageimpl.systeminfo.SystemInfoPage;

/**
 * System info action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class SystemInfoAction extends BaseAction {
	
	/** Singleton instance. */
	public static final SystemInfoAction INSTANCE = new SystemInfoAction();
	
	/**
	 * Creates a new SystemInfoAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private SystemInfoAction() {
		super( XIcon.F_COMPUTER, TextKey.ACTION$SYSTEM_INFO );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	new AboutShell( SystemInfoPage.class.getName() );
    }
	
}
