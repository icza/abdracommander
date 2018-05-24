/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.util.AcUtils;

/**
 * New Email action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class NewEmailAction extends BaseAction {
	
	/** Singleton instance. */
	public static final NewEmailAction INSTANCE = new NewEmailAction();
	
	/**
	 * Creates a new NewEmailAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private NewEmailAction() {
		super( XIcon.F_MAIL_PENCIL, TextKey.ACTION$NEW_EMAIL );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	newMail( null );
    }
	
    /**
     * Launches the user's default mail client to write a new email.
     * @param to      addressee
     * @param subject initial subject
     * @param body    initial body
     */
    public void newMail( final String to, final String subject, final String body ) {
		try {
			final StringBuilder uriBuidler = new StringBuilder( "mailto:" );
			uriBuidler.append( to );
			
			if ( subject != null || body != null ) {
				uriBuidler.append( '?' );
				if ( subject != null )
					uriBuidler.append( "subject=" ).append( URLEncoder.encode( subject, "UTF-8" ).replace( "+", "%20" ) );
				
				if ( body != null ) {
					if ( subject != null )
						uriBuidler.append( '&' );
					uriBuidler.append( "body=" ).append( URLEncoder.encode( body, "UTF-8" ).replace( "+", "%20" ) );
				}
			}
			
			newMail( new URI( uriBuidler.toString() ) );
			
		} catch ( final URISyntaxException use ) {
			AcUtils.CAH.warning( "Invalid email parameters!", use );
		} catch ( final UnsupportedEncodingException uee ) {
			// Never to happen
			AcUtils.CAH.error( "Unexpected error:", uee );
		}
    }
    
    /**
     * Launches the user's default mail client to write a new email.
     * @param mailtoURI optional mailto URI
     */
    public void newMail( final URI mailtoURI ) {
		try {
			
			if ( mailtoURI == null )
				Desktop.getDesktop().mail();
			else
				Desktop.getDesktop().mail( mailtoURI );
			
        } catch ( final Exception e ) {
        	AcUtils.beepError();
        	CAH.error( "Failed to launch default mail client.", e );
        }
    }
	
}
