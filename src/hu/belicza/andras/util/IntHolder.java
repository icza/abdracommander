/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util;

/**
 * A simple <code>int</code> wrapper.<br>
 * It is here to fill the void of the lack of a modifiable {@link Integer}.
 * 
 * @author Andras Belicza
 */
public class IntHolder {
	
	/** The holded <code>int</code> value. */
	public int value;
	
	/**
	 * Creates a new IntHolder.
	 */
	public IntHolder() {
	}
	
	/**
	 * Creates a new IntHolder.
	 * @param value initial value
	 */
	public IntHolder( final int value ) {
		this.value = value;
	}
	
}
