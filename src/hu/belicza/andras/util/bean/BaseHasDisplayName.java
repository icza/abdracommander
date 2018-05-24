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

import hu.belicza.andras.util.iface.HasDisplayName;

/**
 * Base implementation of {@link HasDisplayName}.<br>
 * Also overrides {@link Object#toString()} to return the display name.
 * 
 * @author Andras Belicza
 */
public class BaseHasDisplayName implements HasDisplayName {
	
	/** Display name. */
	protected final String displayName;
	
    /**
     * Creates a new BaseHasDisplayName.
     * @param displayName display name
     */
    public BaseHasDisplayName( final String displayName ) {
    	this.displayName = displayName;
    }
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
}
