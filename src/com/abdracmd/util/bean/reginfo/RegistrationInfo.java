/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.util.bean.reginfo;

import hu.belicza.andras.util.iface.XMLSerializable;

import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.abdracmd.util.bean.person.Person;

/**
 * Registration info
 * 
 * @author Andras Belicza
 */
public class RegistrationInfo implements XMLSerializable {
	
	/** Person to whom this copy is registered to. */
	private Person registeredPerson;
	
	/** System information this copy is registered for. */
	private SystemInfo systemInfo;
	
	/** Registration date. */
	private Date registrationDate;
	
	/**
	 * Tells if roaming is enabled.
	 * When enabled, system info check will not be performed.
	 * Useful if the application is used from an USB stick or the application is used by multiple users on the same computer.
	 */
	private Boolean roamingEnabled;
	
	
    @Override
    public void loadFromXML( final Element element ) {
		final Element registeredPersonElement = (Element) element.getElementsByTagName( "registeredPerson" ).item( 0 );
		if ( registeredPersonElement != null ) {
			registeredPerson = new Person();
			registeredPerson.loadFromXML( registeredPersonElement );
		}
    	
		final Element systemInfoElement = (Element) element.getElementsByTagName( "systemInfo" ).item( 0 );
		if ( systemInfoElement != null ) {
			systemInfo = new SystemInfo();
			systemInfo.loadFromXML( systemInfoElement );
		}
		
		Node node;
		registrationDate = ( node = element.getElementsByTagName( "registrationDate" ).item( 0 ) ) == null ? null : new Date( Long.valueOf( node.getTextContent() ) );
		roamingEnabled   = ( node = element.getElementsByTagName( "roamingEnabled"   ).item( 0 ) ) == null ? null : Boolean.valueOf( node.getTextContent() );
    }
    
    @Override
    public void saveToXML( final Document document, final Element parent ) {
    	// TODO [IMPLEMENT]
    	throw new UnsupportedOperationException();
    }
	
	
	/**
	 * Returns the person to whom this copy is registered.
	 * @return the person to whom this copy is registered
	 */
	public Person getRegisteredPerson() {
		return registeredPerson;
	}
	
	/**
	 * Sets the person to whom this copy is registered.
	 * @param registeredPerson the person to whom this copy is registered
	 */
	public void setRegisteredPerson( final Person registeredPerson ) {
		this.registeredPerson = registeredPerson;
	}
	
	/**
	 * Returns the system information this copy is registered for.
	 * @return the system information this copy is registered for
	 */
	public SystemInfo getSystemInfo() {
		return systemInfo;
	}
	
	/**
	 * Sets the system information this copy is registered for.
	 * @param systemInfo the system information to be set this copy is registered for
	 */
	public void setSystemInfo( final SystemInfo systemInfo ) {
		this.systemInfo = systemInfo;
	}
	
	/**
	 * Tells if roaming is enabled.
	 * @return true if roaming is enabled; false otherwise
	 */
	public Boolean isRoamingEnabled() {
		return roamingEnabled;
	}
	
	/**
	 * Sets whether roaming is enabled.
	 * @param roamingEnabled roaming enabled value to be set
	 */
	public void setRoamingEnabled( final Boolean roamingEnabled ) {
		this.roamingEnabled = roamingEnabled;
	}

	/**
	 * Returns the registration date.
	 * @return the registration date
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}
	
	/**
	 * Sets the registration date.
	 * @param registrationDate registration date to be set
	 */
	public void setRegistrationDate( final Date registrationDate ) {
		this.registrationDate = registrationDate;
	}
	
}
