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
 * A simple <code>long</code> wrapper.<br>
 * It is here to fill the void of the lack of a modifiable {@link Long}.
 * 
 * @author Andras Belicza
 */
public class LongHolder {
	
	/** The holded <code>long</code> value. */
	public long value;
	
	/**
	 * Creates a new LongHolder.
	 */
	public LongHolder() {
	}
	
	/**
	 * Creates a new LongHolder.
	 * @param value initial value
	 */
	public LongHolder( final long value ) {
		this.value = value;
	}
	
}
