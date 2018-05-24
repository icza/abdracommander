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

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.iface.XMLSerializable;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.abdracmd.smp.main.MainShell;

/**
 * Class representing a person.
 * 
 * @author Andras Belicza
 */
public class Person implements XMLSerializable {
	
	/** Photo size (width and height) in pixels. */
	public static final int PHOTO_SIZE = 100;
	
	/** Person name part list. */
	private List< PersonNamePart > namePartList;
	
	/** Contact list of the person. */
	private List< Contact > contactList;
	
	/** Home page. */
	private String homePage;
	
	/** Comment to/of the person. */
	private String comment;
	
	/** Person photo image data in image format one of JPG, PNG or GIF. */
	private byte[] photoImageData;
	
	@Override
	public void loadFromXML( final Element element ) {
		namePartList = new ArrayList<>( 3 );
		final Element nameElement = (Element) element.getElementsByTagName( "name" ).item( 0 );
		if ( nameElement != null ) {
    		final NodeList namePartNodeList = nameElement.getElementsByTagName( "part" );
    		for ( int i = 0; i < namePartNodeList.getLength(); i++ ) {
    			final PersonNamePart namePart = new PersonNamePart();
    			namePart.loadFromXML( (Element) namePartNodeList.item( i ) );
    			namePartList.add( namePart );
    		}
		}
		
		contactList = new ArrayList<>( 5 );
		final Element  contactsElement = (Element) element.getElementsByTagName( "contacts" ).item( 0 );
		if ( contactsElement != null ) {
    		final NodeList contactNodeList = contactsElement.getElementsByTagName( "contact" );
    		for ( int i = 0; i < contactNodeList.getLength(); i++ ) {
    			final Contact contact = new Contact();
    			contact.loadFromXML( (Element) contactNodeList.item( i ) );
    			contactList.add( contact );
    		}
		}
		
		Node node;
		
		homePage = ( node = element.getElementsByTagName( "homePage" ).item( 0 ) ) == null ? null : node.getTextContent();
		comment  = ( node = element.getElementsByTagName( "comment"  ).item( 0 ) ) == null ? null : node.getTextContent();
		
		final String base64PhotoImageData = ( node = element.getElementsByTagName( "photoImageData"  ).item( 0 ) ) == null ? null : node.getTextContent();
		if ( base64PhotoImageData != null && !base64PhotoImageData.isEmpty() )
			setPhotoImageData( Utils.decodeBase64String( base64PhotoImageData ) );
	}
	
    @Override
    public void saveToXML( final Document document, final Element parent ) {
    	// TODO [IMPLEMENT]
    	throw new UnsupportedOperationException();
    }
	
    
	/**
	 * Adds a name part.
	 * @param namePart name part to be added
	 */
	public void addNamePart( final PersonNamePart namePart ) {
		if ( namePartList == null )
			namePartList = new ArrayList<>( 3 );
		
		namePartList.add( namePart );
	}
	
	/**
	 * Adds a contact.
	 * @param contact contact to be added
	 */
	public void addContact( final Contact contact ) {
		if ( contactList == null )
			contactList = new ArrayList<>();
		
			contactList.add( contact );
	}
	
	/**
	 * Returns the name part list.
	 * @return the name part list
	 */
    public List< PersonNamePart > getNamePartList() {
	    return namePartList;
    }

	/**
	 * Sets the name part list.
	 * @param namePartList name part list to be set
	 */
	public void setNamePartList( List< PersonNamePart > namePartList ) {
	    this.namePartList = namePartList;
    }
	
	/**
	 * Returns the contact list.
	 * @return the contact list
	 */
    public List< Contact > getContactList() {
	    return contactList;
    }

	/**
	 * Sets the contact list.
	 * @param contactList contact list to be set
	 */
	public void setContactList( List< Contact > contactList ) {
	    this.contactList = contactList;
    }
	
    /**
     * Returns the home page.
     * @return the home page
     */
    public String getHomePage() {
    	return homePage;
    }
	
	/**
	 * Sets the home page.
	 * @param homePage home page to be set
	 */
    public void setHomePage( String homePage ) {
    	this.homePage = homePage;
    }

	/**
	 * Returns the comment.
	 * @return the comment
	 */
    public String getComment() {
	    return comment;
    }
    
    /**
     * Sets the comment.
     * @param comment comment to be set
     */
	public void setComment( String comment ) {
	    this.comment = comment;
    }
	
	/**
	 * Returns the photo image data.
	 * @return the photo image data
	 */
	public byte[] getPhotoImageData() {
		return photoImageData;
	}
	
	/**
	 * Sets the photo image data.
	 * 
	 * <p>The image will be resized if not in size of {@value #PHOTO_SIZE}x{@value #PHOTO_SIZE}.</p>
	 *  
	 * @param photoImageData photo image data to be set
	 * @return true if photo is set successfully; false otherwise
	 * 
	 * @throws IllegalArgumentException if the width and height of the image are not equal 
	 */
	public boolean setPhotoImageData( final byte[] photoImageData ) throws IllegalArgumentException {
		final ImageIcon imageIcon = new ImageIcon( photoImageData );
		
		// Check data format (valid image?) and image size
		if ( imageIcon.getIconWidth() != PHOTO_SIZE || imageIcon.getIconHeight() != PHOTO_SIZE ) {
			// Needs resizing:
			return setPhotoImage( imageIcon.getImage() );
		}
		else {
			// We're good
			this.photoImageData = photoImageData;
			return true;
		}
	}
	
	/**
	 * Sets the photo image.
	 * 
	 * <p>The image will be resized if not in size of {@value #PHOTO_SIZE}x{@value #PHOTO_SIZE}.</p>
	 * 
	 * @param image photo image to be set
	 * @return true if photo is set successfully; false otherwise
	 * 
	 * @throws IllegalArgumentException if the width and height of the image are not equal or the width of the image is not positive 
	 */
	public boolean setPhotoImage( Image image ) throws IllegalArgumentException {
		if ( image.getWidth( null ) < 0 || image.getHeight( null ) < 0 ) {
			// Wait for the image to load, we need the image width and height:
			new ImageIcon( image );
		}
		
		if ( image.getWidth( null ) == 0 || image.getHeight( null ) == 0 )
			throw new IllegalArgumentException( "Image size (both width and height) must be greater than 0!" );
		
		if ( image.getWidth( null ) != image.getHeight( null ) )
			throw new IllegalArgumentException( "Image width and height are not equal: " + image.getWidth( null ) + " != " + image.getHeight( null ) );
		
		if ( image.getWidth( null ) != PHOTO_SIZE || image.getHeight( null ) != PHOTO_SIZE )
			image = image.getScaledInstance( PHOTO_SIZE, PHOTO_SIZE, Image.SCALE_AREA_AVERAGING );
		
		final byte[] tempPhotoImageData = Utils.getImageJpegData( image );
		if ( tempPhotoImageData == null ) {
			MainShell.INSTANCE.debug( "Failed to process image!" );
			return false;
		}
		
		photoImageData = tempPhotoImageData;
		
		return true;
	}
	
}
