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
 * Official Forum action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class OfficialForumAction extends BaseUrlAction {
	
	/** Singleton instance. */
	public static final OfficialForumAction INSTANCE = new OfficialForumAction();
	
	/**
	 * Creates a new OfficialForumAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private OfficialForumAction() {
		super( Consts.URL_FORUM, TextKey.ACTION$OFFICIAL_FORUM );
	}
	
}
