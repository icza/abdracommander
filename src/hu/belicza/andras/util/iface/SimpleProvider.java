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
 * A simple provider.
 * 
 * <p>Provides an output value.</p>
 * 
 * @param <O> provided (output) value type 
 *  
 * @author Andras Belicza
 */
public interface SimpleProvider< O > {
	
	/**
	 * Provides the output value.
	 * @return the provided output value for the specified input value
	 */
	O provide();
	
}
