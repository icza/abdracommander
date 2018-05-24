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
 * Setting key whose value has a valid range.
 * 
 * <p>Instances are immutable if instances of the type parameter T are immutable.</p>
 * 
 * @param < T > setting value type
 * 
 * @author Andras Belicza
 */
public class RangeSettingKey< T extends Comparable< T > > extends SettingKey< T > {
	
	/** Min value of the setting. */
	public final T minValue;
	/** Max value of the setting. */
	public final T maxValue;
	
	/**
	 * Creates a new RangeSettingKey.
	 * 
	 * @param level        setting complexity level
	 * @param name         name of the setting
	 * @param type         type of the setting value
	 * @param defaultValue default value of the setting
	 * @param minValue     min value of the setting
	 * @param maxValue     max value of the setting
	 * 
	 * @throws NullPointerException if any of the default value, min value or max value is <code>null</code>
	 * @throws IllegalArgumentException if the default value is outside of the range defined by <code>minValue..maxValue</code> (both inclusive)
	 */
	public RangeSettingKey( final SettingLevel level, final String name, final Class< T > type, final T defaultValue, final T minValue, final T maxValue ) {
		this( level, name, type, defaultValue, null, minValue, maxValue );
	}
	
	/**
	 * Creates a new RangeSettingKey.
	 * 
	 * @param level        setting complexity level
	 * @param name         name of the setting
	 * @param type         type of the setting value
	 * @param defaultValue default value of the setting
	 * @param viewHints    optional view hints
	 * @param minValue     min value of the setting
	 * @param maxValue     max value of the setting
	 * 
	 * @throws NullPointerException if any of the default value, min value or max value is <code>null</code>
	 * @throws IllegalArgumentException if the default value is outside of the range defined by <code>minValue..maxValue</code> (both inclusive)
	 */
	public RangeSettingKey( final SettingLevel level, final String name, final Class< T > type, final T defaultValue, final ViewHints viewHints, final T minValue, final T maxValue ) {
		super( level, name, type, defaultValue, viewHints );
		
		if ( defaultValue.compareTo( minValue ) < 0 )
			throw new IllegalArgumentException( "Default value cannot be less than the min value!" );
		if ( defaultValue.compareTo( maxValue ) > 0 )
			throw new IllegalArgumentException( "Default value cannot be greater than the max value!" );
		
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
}
