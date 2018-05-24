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

import hu.belicza.andras.util.iface.HasType;

/**
 * Base implementation of {@link HasType}.
 * 
 * @param < T > the "type of type"
 * 
 * @author Andras Belicza
 */
public class BaseHasType< T > implements HasType< T > {
	
	/** The type. */
	public final Class< T > type;
	
    /**
     * Creates a new BaseHasType.
     * @param type type
     */
    public BaseHasType( final Class< T > type ) {
    	this.type = type;
    }
    
	@Override
	public Class< T > getType() {
		return type;
	}
	
}
