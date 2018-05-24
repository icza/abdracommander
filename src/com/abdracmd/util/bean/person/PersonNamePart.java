/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.util.bean.person;

import hu.belicza.andras.util.iface.XMLSerializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.abdracmd.util.AcUtils;

/**
 * Name part of a person.
 * 
 * @author Andras Belicza
 */
public class PersonNamePart implements XMLSerializable {
	
	/**
	 * Name part types.
	 * @author Andras Belicza
	 */
	public enum PersonNamePartType {
		/** First name.  */
		FIRST,
		/** Middle name. */
		MIDDLE,
		/** Last name.   */
		LAST,
		/** Nickname.    */
		NICKNAME;
		
		@Override
		public String toString() {
			return AcUtils.getEnumText( this );
		}
	}
	
	/** Name part type. */
	private PersonNamePartType type;
	
	/** Name part value. */
	private String value;
    
    
	@Override
    public void loadFromXML( final Element element ) {
    	type  = PersonNamePartType.valueOf( element.getAttribute( "type" ) );
    	value = element.getTextContent();
    }
    
    @Override
    public void saveToXML( final Document document, final Element parent ) {
    	// TODO [IMPLEMENT]
    	throw new UnsupportedOperationException();
    }
	
	
    /**
     * Creates a new PersonNamePart.
     */
    public PersonNamePart() {
    }
    
    /**
     * Creates a new PersonNamePart.
     * @param type  name part type
     * @param value name part value
     */
    public PersonNamePart( final PersonNamePartType type, final String value ) {
    	this.type  = type;
    	this.value = value;
    }
    
	/**
	 * Returns the name part type.
	 * @return the name part type
	 */
	public PersonNamePartType getType() {
	    return type;
    }

	/**
	 * Sets the name part type.
	 * @param type name part type to be set
	 */
	public void setType( PersonNamePartType type ) {
	    this.type = type;
    }

	/**
	 * Returns the name part value.
	 * @return the name part value
	 */
	public String getValue() {
	    return value;
    }

	/**
	 * Sets the name part value.
	 * @param value name part value to be set
	 */
	public void setValue( String value ) {
	    this.value = value;
    }

}
