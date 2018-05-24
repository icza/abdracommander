/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.uicomponent;

import hu.belicza.andras.util.bean.Email;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.uicomponent.smarttable.SmartTable;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;
import com.abdracmd.util.bean.person.Contact;
import com.abdracmd.util.bean.person.Contact.ContactType;
import com.abdracmd.util.bean.person.Person;

/**
 * A component display info about a {@link Person}.
 * 
 * @author Andras Belicza
 */
public class PersonCard extends Box {
	
	/** Default serial version ID */
	private static final long serialVersionUID = 1L;
	
	/** Default photo if no photo is provided for a person. */
	private static final ImageIcon DEFAULT_PHOTO;
	static {
		// Enlarge the icon to the size used by Person:
		final Person p = new Person();
		p.setPhotoImage( AcUtils.CAH.get( XIcon.F_USER_SILHOUETTE_QUESTION ).getImage() );
		DEFAULT_PHOTO = new ImageIcon( p.getPhotoImageData() );
	}
	
	/**
	 * Creates a new PersonCard.
	 * @param person person whose info to display
	 */
	public PersonCard( final Person person ) {
		this( person, null );
	}
	
	/**
	 * Creates a new PersonCard.
	 * @param person    person whose info to display
	 * @param cardTitle title of the card
	 */
	public PersonCard( final Person person, final String cardTitle ) {
		super( BoxLayout.X_AXIS );
		
		setBorder( cardTitle == null ? BorderFactory.createRaisedSoftBevelBorder() : BorderFactory.createTitledBorder( cardTitle ) );
		
		// Photo
		final JLabel photoLabel = new JLabel( person.getPhotoImageData() == null ? DEFAULT_PHOTO : new ImageIcon( person.getPhotoImageData() ) );
		photoLabel.setBorder( BorderFactory.createRaisedSoftBevelBorder() );
		add( photoLabel );
		
		// Person details
		
		/** Data vector of the details table. */
		final Vector< Vector< Object > > dataVector = new Vector<>();
		
		addRow( dataVector, XIcon.F_CARD_ADDRESS, AcUtils.CAH.get( TextKey.PERSON_CARD$NAME ), AcUtils.CAH.getPersonName( person ) );
		
		for ( final Contact c : person.getContactList() )
			addRow( dataVector, c.getType().xicon, c.getType().toString(), c.getType() == ContactType.EMAIL ? new Email( c.getValue() ) : c.getValue() );
		
		if ( person.getHomePage() != null )
			addRow( dataVector, XIcon.F_APPLICATION_BROWSER, AcUtils.CAH.get( TextKey.PERSON_CARD$HOME_PAGE), person.getHomePage() );
		
		// TODO trim comment?
		if ( person.getComment() != null )
			addRow( dataVector, XIcon.F_BALLOON, AcUtils.CAH.get( TextKey.PERSON_CARD$COMMENT ), person.getComment() );
		
		final Vector< Object > columnIdnetifiers = new Vector<>();
		columnIdnetifiers.add( null );
		columnIdnetifiers.add( null );
		columnIdnetifiers.add( null );
		
		final JTable detailsTable = new SmartTable();
		( (DefaultTableModel) detailsTable.getModel() ).setDataVector( dataVector, columnIdnetifiers );
		GuiUtils.packTable( detailsTable );
		
		add( GuiUtils.wrapInBorderPanel( detailsTable ) );
	}
	
	/**
	 * Adds a new row to the details table.
	 * @param dataVector data vector to add the row to
	 * @param xicon      xicon for the row
	 * @param name       name
	 * @param value      value
	 */
	private static void addRow( final Vector< Vector< Object > > dataVector, final XIcon xicon, final String name, final Object value ) {
		final Vector< Object > row = new Vector<>( 3 );
		
		row.add( xicon == null ? null : AcUtils.CAH.get( xicon ) );
		row.add( name );
		row.add( value );
		
		dataVector.add( row );
	}
	
}
