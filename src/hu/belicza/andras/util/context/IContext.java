/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.context;

/**
 * A context interface with capabilities of sharing data between multiple parties.
 * 
 * @author Andras Belicza
 */
public interface IContext {
	
	/**
	 * Stores a value in the context.
	 * @param key   key of the value to be stored
	 * @param value the value to be stored
	 * @param <T>   type of the value to be stored
	 */
	< T > void put( Object key, T value );
	
	/**
	 * Returns a stored value from the context.
	 * @param key   key of the value to be returned
	 * @param clazz class type to convert the stored value to
	 * @param <T>   type of the value to be returned
	 * @return the stored value or <code>null</code> if no value is stored for the specified key or the stored value cannot be casted to <code>T</code>
	 */
	< T > T get( Object key, Class< T > clazz );
	
}
