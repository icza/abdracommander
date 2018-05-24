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

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.util.AcUtils;

/**
 * Contact of a person.
 * 
 * @author Andras Belicza
 */
public class Contact implements XMLSerializable {
	
	/**
	 * Contact types.
	 * @author Andras Belicza
	 */
	public enum ContactType {
		/** Postal address.     */
		POSTAL_ADDRESS( XIcon.F_MAIL           ),
		/** Email address.      */
		EMAIL         ( XIcon.F_MAIL_AT_SIGN   ),
		/** Facebook.           */
		FACEBOOK      ( XIcon.MISC_FACEBOOK    ),
		/** Google+.            */
		GOOGLE_PLUS   ( XIcon.MISC_GOOGLE_PLUS ),
		/** Twitter.            */
		TWITTER       ( XIcon.MISC_TWITTER     ),
		/** Linkedin.           */
		LINKEDIN      ( XIcon.MISC_LINKEDIN    ),
		/** Youtube.            */
		YOUTUBE       ( XIcon.MISC_YOUTUBE     ),
		/** Other contact type. */
		OTHER         ( XIcon.MY_EMPTY         );
		
		/** XIcon associated with this contact type. */
		public final XIcon xicon;
		
		/**
		 * Creates a new ContactType.
		 * @param xicon xicon associated with this contact type
		 */
		private ContactType( final XIcon xicon ) {
			this.xicon = xicon;
		}
		
		@Override
		public String toString() {
			return AcUtils.getEnumText( this );
		}
	}
	
	/** Type of contact. */
	private ContactType type;
	
	/** Contact value. */
	private String value;
	
	
    @Override
    public void loadFromXML( final Element element ) {
    	type  = ContactType.valueOf( element.getAttribute( "type" ) );
    	value = element.getTextContent();
    }
	
    @Override
    public void saveToXML( final Document document, final Element parent ) {
    	// TODO [IMPLEMENT]
    	throw new UnsupportedOperationException();
    }
    
    
    /**
     * Creates a new Contact.
     */
    public Contact() {
    }
    
    /**
     * Creates a new Contact.
     * @param type  contact type
     * @param value contact value
     */
    public Contact( final ContactType type, final String value ) {
    	this.type  = type;
    	this.value = value;
    }
    
	/**
	 * Returns the contact type.
	 * @return the contact type
	 */
	public ContactType getType() {
	    return type;
    }

	/**
	 * Sets the contact type.
	 * @param type contact type to be set
	 */
	public void setType( ContactType type ) {
	    this.type = type;
    }

	/**
	 * Returns the contact value.
	 * @return the contact value
	 */
	public String getValue() {
	    return value;
    }

	/**
	 * Sets the contact value.
	 * @param value contact value to be set
	 */
	public void setValue( String value ) {
	    this.value = value;
    }

}
