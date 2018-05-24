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
 * Defines a display name property.
 * 
 * <p>It is recommended for classes that implement this to override {@link Object#toString()} to return the display name.</p>
 * 
 * @author Andras Belicza
 */
public interface HasDisplayName {
	
	/**
	 * Returns the display name.
	 * @return the display name
	 */
	String getDisplayName();
	
}
