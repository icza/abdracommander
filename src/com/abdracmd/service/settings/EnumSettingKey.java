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

import com.abdracmd.service.settings.viewhint.ViewHints;

/**
 * Setting key whose value is an enum value and the valid values are all the enum constants.
 * 
 * <p>Tip: if not all enum constants are valid for the setting, use {@link FixedValuesSettingKey} with the 
 * valid subset of the enum constants.</p>
 * 
 * @param < T > setting value type
 * 
 * @author Andras Belicza
 */
public class EnumSettingKey< T extends Enum< T > > extends FixedValuesSettingKey< T > {
	
	/**
	 * Creates a new FixedValuesSettingKey.
	 * 
	 * @param level        setting complexity level
	 * @param name         name of the setting
	 * @param type         type of the setting value
	 * @param defaultValue default value of the setting
	 * 
	 * @throws NullPointerException     if default value is null
	 * @throws IllegalArgumentException if the default value is not included in the valid values
	 */
    public EnumSettingKey( final SettingLevel level, final String name, final Class< T > type, final T defaultValue ) {
		this( level, name, type, defaultValue, null );
	}
	
	/**
	 * Creates a new FixedValuesSettingKey.
	 * 
	 * @param level        setting complexity level
	 * @param name         name of the setting
	 * @param type         type of the setting value
	 * @param defaultValue default value of the setting
	 * @param viewHints    optional view hints
	 * 
	 * @throws NullPointerException     if default value is null or if type is not an enum type
	 * @throws IllegalArgumentException if the default value is not included in the valid values
	 */
    public EnumSettingKey( final SettingLevel level, final String name, final Class< T > type, final T defaultValue, final ViewHints viewHints ) {
		super( level, name, type, defaultValue, viewHints, type.getEnumConstants() );
	}
	
}
