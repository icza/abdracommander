/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.shutdown;

import com.abdracmd.util.AcUtils;

/**
 * Phases of the shutdown process.
 * 
 * @author Andras Belicza
 */
public enum ShutdownPhase {
	
	/** Not yet started.     */
	NOT_STARTED,
	/** Started.             */
	STARTED,
	/** Closing all windows. */
	CLOSING_ALL_WINDOWS,
	/** Saving settings.     */
	SAVING_SETTINGS,
	/** Finished.            */
	FINISHED;
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
