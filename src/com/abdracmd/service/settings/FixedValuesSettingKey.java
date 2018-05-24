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

import hu.belicza.andras.util.Utils;

import com.abdracmd.service.settings.viewhint.ViewHints;

/**
 * Setting key whose value has a valid range.
 * 
 * @param < T > setting value type
 * 
 * @author Andras Belicza
 */
public class FixedValuesSettingKey< T > extends SettingKey< T > {
	
	/** Valid values of the setting. */
	public T[] validValues;
	
	/**
	 * Creates a new FixedValuesSettingKey.
	 * 
	 * @param level        setting complexity level
	 * @param name         name of the setting
	 * @param type         type of the setting value
	 * @param defaultValue default value of the setting
	 * @param validValues  valid values of the setting
	 * 
	 * @throws NullPointerException     if default value is null
	 * @throws IllegalArgumentException if the default value is not included in the valid values
	 */
	@SafeVarargs
    public FixedValuesSettingKey( final SettingLevel level, final String name, final Class< T > type, final T defaultValue, final T... validValues ) {
		this( level, name, type, defaultValue, null, validValues );
	}
	
	/**
	 * Creates a new FixedValuesSettingKey.
	 * 
	 * @param level        setting complexity level
	 * @param name         name of the setting
	 * @param type         type of the setting value
	 * @param defaultValue default value of the setting
	 * @param viewHints    optional view hints
	 * @param validValues  valid values of the setting
	 * 
	 * @throws NullPointerException     if default value is null
	 * @throws IllegalArgumentException if the default value is not included in the valid values
	 */
	@SafeVarargs
    public FixedValuesSettingKey( final SettingLevel level, final String name, final Class< T > type, final T defaultValue, final ViewHints viewHints, final T... validValues ) {
		super( level, name, type, defaultValue, viewHints );
		
		if ( !Utils.contains( validValues, defaultValue ) )
			throw new IllegalArgumentException( "Default value is not included in valid values!" );
		
		this.validValues = validValues;
	}
	
}
