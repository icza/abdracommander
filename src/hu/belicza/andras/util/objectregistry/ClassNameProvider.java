/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.objectregistry;

import hu.belicza.andras.util.iface.Provider;

/**
 * A provider which provides the class name for the specified value.
 * 
 * @param <I> input value type
 *  
 * @author Andras Belicza
 */
public class ClassNameProvider< I > implements Provider< String, I > {
	
	@Override
	public String provide( final I input ) {
		return input.getClass().getName();
	}
	
}
