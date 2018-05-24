/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder;

import com.abdracmd.util.AcUtils;

/**
 * Go into files modes.
 * 
 * @author Andras Belicza
 */
public enum GoIntoFilesMode {
	
	/** Never.                */
	NEVER,
	/** Default types.        */
	DEFAULT_TYPES,
	/** Custom types.         */
	CUSTOM_TYPES,
	/** Default+custom types. */
	DEFAULT_PLUS_CUSTOM_TYPES,
	/** Always.               */
	ALWAYS;
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
