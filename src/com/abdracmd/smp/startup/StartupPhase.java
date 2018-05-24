/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.startup;

import com.abdracmd.util.AcUtils;

/**
 * Phases of the startup process.
 * 
 * @author Andras Belicza
 */
public enum StartupPhase {
	
	/** Not yet started.                  */
	NOT_STARTED,
	/** Started.                          */
	STARTED,
	/** Initializing settings.            */
	INITIALIZING_SETTINGS,
	/** Initializing language.            */
	INITIALIZING_LANGUAGE,
	/** Initializing icons.               */
	INITIALIZING_ICONS,
	/** Loading registration info.        */
	LOADING_REGISTRATION_INFO,
	/** Initializing built-in registries. */
	INITIALIZING_BUILTIN_REGISTRIES,
	/** Finished.                         */
	FINISHED;
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
