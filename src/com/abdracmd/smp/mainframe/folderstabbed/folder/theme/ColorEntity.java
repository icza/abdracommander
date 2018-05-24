/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder.theme;

import com.abdracmd.util.AcUtils;

/**
 * Entities which the theme defines colors for.
 * 
 * @author Andras Belicza
 */
public enum ColorEntity {
	
	/** Text.       */
	TEXT,
	/** Background. */
	BACKGROUND,
	/** Border.     */
	BORDER;
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
