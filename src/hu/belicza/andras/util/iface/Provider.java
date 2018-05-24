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
 * A provider.
 * 
 * <p>Provides an output value for an input value.</p>
 * 
 * @param <O> provided (output) value type 
 * @param <I> input value type
 *  
 * @author Andras Belicza
 */
public interface Provider< O, I > {
	
	/**
	 * Provides the output value for the specified input value.
	 * @param input input value to provide output value for
	 * @return the provided output value for the specified input value
	 */
	O provide( I input );
	
}
