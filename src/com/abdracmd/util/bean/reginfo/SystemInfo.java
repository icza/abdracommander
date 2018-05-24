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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * System information.
 *  
 * @author Andras Belicza
 */
public class SystemInfo implements XMLSerializable {
	
	/** OS name. */
	private String osName;
	/** OS version. */
	private String osVersion;
	/** OS architecture. */
	private String osArchitecture;
	/** Available processors. */
	private Integer availableProcessors;
	/** User name. */
	private String userName;
	/** User time zone. */
	private String userTimeZone;
	/** Main root size. */
	private Long mainRootSize;
	
	
    @Override
    public void loadFromXML( final Element element ) {
		Node node;
		osName              = ( node = element.getElementsByTagName( "osName"              ).item( 0 ) ) == null ? null : node.getTextContent();
		osVersion           = ( node = element.getElementsByTagName( "osVersion"           ).item( 0 ) ) == null ? null : node.getTextContent();
		osArchitecture      = ( node = element.getElementsByTagName( "osArchitecture"      ).item( 0 ) ) == null ? null : node.getTextContent();
		availableProcessors = ( node = element.getElementsByTagName( "availableProcessors" ).item( 0 ) ) == null ? null : Integer.valueOf( node.getTextContent() );
		userName            = ( node = element.getElementsByTagName( "userName"            ).item( 0 ) ) == null ? null : node.getTextContent();
		userTimeZone        = ( node = element.getElementsByTagName( "userTimeZone"        ).item( 0 ) ) == null ? null : node.getTextContent();
		mainRootSize        = ( node = element.getElementsByTagName( "mainRootSize"        ).item( 0 ) ) == null ? null : Long.valueOf( node.getTextContent() );
    }
    
    @Override
    public void saveToXML( final Document document, final Element parent ) {
    	// TODO [IMPLEMENT]
    	throw new UnsupportedOperationException();
    }
	
    
	/**
	 * Returns the OS name.
	 * @return the OS name
	 */
	public String getOsName() {
		return osName;
	}
	
	/**
	 * Sets the OS name.
	 * @param osName OS name to be set
	 */
	public void setOsName(String osName) {
		this.osName = osName;
	}
	
	/**
	 * Returns the OS version.
	 * @return the OS version
	 */
	public String getOsVersion() {
		return osVersion;
	}
	
	/**
	 * Sets the OS version.
	 * @param osVersion OS version to be set
	 */
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	
	/**
	 * Returns the OS architecture.
	 * @return the OS architecture
	 */
	public String getOsArchitecture() {
		return osArchitecture;
	}
	
	/**
	 * Sets the OS architecture.
	 * @param osArchitecture OS architecture to be set
	 */
	public void setOsArchitecture(String osArchitecture) {
		this.osArchitecture = osArchitecture;
	}
	
	/**
	 * Returns the available processors.
	 * @return the available processors
	 */
	public Integer getAvailableProcessors() {
		return availableProcessors;
	}
	
	/**
	 * Sets the available processors.
	 * @param availableProcessors available processors to be set
	 */
	public void setAvailableProcessors(Integer availableProcessors) {
		this.availableProcessors = availableProcessors;
	}
	
	/**
	 * Returns the user name.
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Sets the user name.
	 * @param userName user name to be set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Returns the user time zone.
	 * @return the user time zone
	 */
	public String getUserTimeZone() {
		return userTimeZone;
	}
	
	/**
	 * Sets the user time zone.
	 * @param userTimeZone user time zone to be set
	 */
	public void setUserTimeZone(String userTimeZone) {
		this.userTimeZone = userTimeZone;
	}
	
	/**
	 * Returns the main root size.
	 * @return the main root size
	 */
	public Long getMainRootSize() {
		return mainRootSize;
	}
	
	/**
	 * Sets the main root size.
	 * @param mainRootSize main root size to be set
	 */
	public void setMainRootSize(Long mainRootSize) {
		this.mainRootSize = mainRootSize;
	}
	
}
