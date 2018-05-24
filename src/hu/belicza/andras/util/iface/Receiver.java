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
 * Interface describing an object receiver.
 * 
 * @param <T> type of the passed object
 *  
 * @author Andras Belicza
 */
public interface Receiver< T > {
	
	/**
	 * Receives the passed object.
	 * @param value the passed object
	 */
	void receive( T value );
	
}
