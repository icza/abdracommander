/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.service.settings;

import com.abdracmd.util.AcUtils;

/**
 * Setting complexity level.
 * 
 * This level determines whether a setting will appear in the Settings dialog based on the selected complexity level.
 * In short, this is the base of the setting filter.
 * 
 * @author Andras Belicza
 */
public enum SettingLevel {
	
	/** Basic level, for little computer knowledge.                           */
	BASIC,
	/** Advanced level, for advanced computer skills.                         */
	ADVANCED,
	/** Hidden level, the setting will never be shown in the Settings dialog. */
	HIDDEN;
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
