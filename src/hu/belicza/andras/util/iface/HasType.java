/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.iface;

/**
 * Defines a type property.
 * 
 * @param < T > the "type of type"
 * 
 * @author Andras Belicza
 */
public interface HasType< T > {
	
	/**
	 * Returns the type.
	 * @return the type
	 */
	Class< T > getType();
	
}
