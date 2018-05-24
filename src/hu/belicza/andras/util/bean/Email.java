/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.bean;

/**
 * A simple wrapper class indicating the wrapped value is an email address.
 * 
 * @author Andras Belicza
 */
public class Email {
	
	/** The email address value. */
	public final String value;
	
	/**
	 * Creates a new Email;
	 * @param value email address value
	 */
	public Email( final String value ) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
}
