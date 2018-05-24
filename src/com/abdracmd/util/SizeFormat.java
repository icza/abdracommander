/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.util;

/**
 * Representation formats of a size value.
 * 
 * @author Andras Belicza
 */
public enum SizeFormat {
	
	/** Auto, depends on size value. */
	AUTO,
	/** Bytes.                       */
	BYTES,
	/** KB.                          */
	KB,
	/** MB.                          */
	MB,
	/** GB.                          */
	GB,
	/** TB.                          */
	TB;
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
