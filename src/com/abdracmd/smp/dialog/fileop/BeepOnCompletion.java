/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.fileop;

import com.abdracmd.util.AcUtils;

/**
 * Beep on completion modes.
 * 
 * @author Andras Belicza
 */
public enum BeepOnCompletion {
	
	/** Never.                   */
	NEVER,
	/** When window is inactive. */
	WHEN_WINDOW_INACTIVE,
	/** Always.                  */
	ALWAYS;
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
