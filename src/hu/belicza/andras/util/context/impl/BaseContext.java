/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.context.impl;

import hu.belicza.andras.util.context.IContext;

import java.util.HashMap;
import java.util.Map;

/**
 * An {@link IContext} implementation.
 * 
 * @author Andras Belicza
 */
public class BaseContext implements IContext {
	
	/** Internal map to store the shared values. */
	private final Map< Object, Object > map = new HashMap<>();
	
	/**
	 * Initializes the context, preparing it to be reused for "contexting" a new "session" or a new "turn".<br>
	 * As part of the initialization process, previously stored values are removed.
	 */
	public void init() {
		map.clear();
	}
	
	@Override
	public < T > void put( final Object key, final T value ) {
		map.put( key, value );
	}
	
	@Override
	public < T > T get( final Object key, final Class< T > clazz ) {
		try {
			return clazz.cast( map.get( key ) );
		} catch ( final ClassCastException cce ) {
			return null;
		}
	}
	
}
