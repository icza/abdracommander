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
 * Flags for color entities.
 * 
 * @author Andras Belicza
 */
public enum ColorEntityFlag {
	
	/** Focused row.                  */
	FOCUSED,
	/** Selected row.                 */
	SELECTED,
	/** Alternate row (odd or even).  */
	ALTERNATE,
	/** Active panel (left or right). */
	ACTIVE;
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
