/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.service.settings.viewhint;

import hu.belicza.andras.util.bean.BaseHasType;

/**
 * Setting view hint.
 * 
 * @param < T > hint value type
 * 
 * @author Andras Belicza
 */
public class ViewHint< T > extends BaseHasType< T > {
	
	/**
	 * Creates a new ViewHint.
	 * 
	 * @param type type of the hint value
	 */
	public ViewHint( final Class< T > type ) {
		super( type );
	}
	
}
