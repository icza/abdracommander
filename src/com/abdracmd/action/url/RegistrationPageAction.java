/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.action.url;

import com.abdracmd.Consts;
import com.abdracmd.service.language.TextKey;

/**
 * Registration page action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RegistrationPageAction extends BaseUrlAction {
	
	/** Singleton instance. */
	public static final RegistrationPageAction INSTANCE = new RegistrationPageAction();
	
	/**
	 * Creates a new RegistrationPageAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private RegistrationPageAction() {
		super( Consts.URL_REGISTRATION_PAGE, TextKey.ACTION$REGISTRATION_PAGE );
	}
	
}
