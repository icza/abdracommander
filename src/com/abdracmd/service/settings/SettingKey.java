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

import hu.belicza.andras.util.OneValueSet;
import hu.belicza.andras.util.bean.BaseHasType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.abdracmd.service.settings.viewhint.ViewHints;

/**
 * Setting key.
 * 
 * <p>Instances are immutable if instances of the type parameter <code>T</code> are immutable.</p>
 * 
 * @param < T > setting value type
 * 
 * @author Andras Belicza
 */
public class SettingKey< T > extends BaseHasType< T > {
	
	/** Empty view hints to be returned when no external view hints are provided. */
	private static final ViewHints EMPTY_VIEW_HINTS = new ViewHints();
	
	/** Keep a track of created setting keys. */
	private static final List< SettingKey< ? > >        SETTING_KEY_LIST     = new ArrayList<>();
	/** Created setting keys mapped from name. */
	private static final Map< String, SettingKey< ? > > NAME_SETTING_KEY_MAP = new HashMap<>();
	
	
	/** Unmodifiable set containing only <code>this</code>. */
	public final Set< SettingKey< T > > SELF_CONTAINING_SET = new OneValueSet<>( this );
	
	/** Setting complexity level.     */
	public final SettingLevel level;
	
	/** Name of the setting.          */
	public final String       name;
	
	/** Default value of the setting. */
	public final T            defaultValue;
	
	/** View hints                    */
	public final ViewHints    viewHints;
	
	/**
	 * Creates a new SettingKey.
	 * 
	 * @param level        setting complexity level
	 * @param name         name of the setting
	 * @param type         type of the setting value
	 * @param defaultValue default value of the setting
	 * 
	 * @throws NullPointerException if default value is null
	 */
	public SettingKey( final SettingLevel level, final String name, final Class< T > type, final T defaultValue ) {
		this( level, name, type, defaultValue, null );
	}
	
	/**
	 * Creates a new SettingKey.
	 * 
	 * @param level        setting complexity level
	 * @param name         name of the setting
	 * @param type         type of the setting value
	 * @param defaultValue default value of the setting
	 * @param viewHints    optional view hints
	 * 
	 * @throws NullPointerException if default value is null
	 */
	public SettingKey( final SettingLevel level, final String name, final Class< T > type, final T defaultValue, final ViewHints viewHints ) {
		super( type );
		
		SETTING_KEY_LIST    .add( this       );
		NAME_SETTING_KEY_MAP.put( name, this );
		
		Objects.requireNonNull( defaultValue );
		
		this.level        = level;
		this.name         = name;
		this.defaultValue = defaultValue;
		this.viewHints    = viewHints == null ? EMPTY_VIEW_HINTS : viewHints;
	}
	
	@Override
	public String toString() {
	    return name;
	}
	
	/**
	 * Returns an unmodifiable view of all the created setting keys.
	 * @return an unmodifiable view of all the created setting keys
	 */
	public static List< SettingKey< ? > > getSettingKeyList() {
		return Collections.unmodifiableList( SETTING_KEY_LIST );
	}
	
	/**
	 * Returns an unmodifiable view of the settings map.
	 * @return an unmodifiable view of the settings map
	 */
	public static Map< String, SettingKey< ? > > getSettingMap() {
		return Collections.unmodifiableMap( NAME_SETTING_KEY_MAP );
	}
	
	/**
	 * Returns the setting key specified by its name.
	 * @param name setting key name whose setting key to be returned
	 * @return the setting key specified by its name
	 */
	public static SettingKey< ? > getByName( final String name ) {
		return NAME_SETTING_KEY_MAP.get( name );
	}
	
}
