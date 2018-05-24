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
 * Home page action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class HomePageAction extends BaseUrlAction {
	
	/** Singleton instance. */
	public static final HomePageAction INSTANCE = new HomePageAction();
	
	/**
	 * Creates a new HomePageAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private HomePageAction() {
		super( Consts.URL_HOME_PAGE, TextKey.ACTION$HOME_PAGE );
	}
	
}
