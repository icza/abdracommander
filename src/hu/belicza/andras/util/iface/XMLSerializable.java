/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.iface;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Defines an interface for beans to save to XML (append to a {@link Document}) or load from XML (from an {@link Element}). 
 * 
 * @author Andras Belicza
 */
public interface XMLSerializable {
	
	/**
	 * Loads the object state from XML content, from the specified element.
	 * @param element element to load the state from
	 */
	void loadFromXML( Element element );
	
	/**
	 * Saves the object as XML content, appends the state to a parent element.
	 * @param document reference to the XML document 
	 * @param parent   parent element to append to
	 */
	void saveToXML( Document document, Element parent );
	
}
