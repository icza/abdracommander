/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.fileop.impl;

import com.abdracmd.util.AcUtils;

/**
 * Default action on error.
 * 
 * @author Andras Belicza
 */
public enum ActionOnError {
	
	/** Ask.    */
	ASK,
	/** Skip.   */
	SKIP,
	/** Cancel. */
	CANCEL;
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
