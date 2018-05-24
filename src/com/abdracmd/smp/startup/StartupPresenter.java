/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.startup;

import hu.belicza.andras.util.Utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.SplashScreen;

import com.abdracmd.Consts;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.BaseShell;
import com.abdracmd.smp.BaseModel;
import com.abdracmd.smp.BasePresenter;
import com.abdracmd.util.bean.person.Contact;
import com.abdracmd.util.bean.person.Contact.ContactType;

/**
 * Startup process presenter.
 * 
 * @author Andras Belicza
 */
class StartupPresenter extends BasePresenter< BaseModel, BaseShell< BaseModel, StartupPresenter >, SplashScreen > implements AutoCloseable {
	
	/**
	 * Creates and returns a new StartupPresenter.
	 * @param shell reference to the shell
	 * @return a new StartupPresenter or <code>null</code> if it is not supported
	 */
	@SuppressWarnings( "resource" )
	protected static StartupPresenter create( final StartupShell shell ) {
		final SplashScreen splashScreen = SplashScreen.getSplashScreen();
		return splashScreen == null ? null : new StartupPresenter( shell, splashScreen );
	}
	
	/** Graphics context of the splash screen. */
	private final Graphics2D splashG2;
	
	/** Y coordinate of the title. */
	private final int titleY;
	
	/**
	 * Creates a new StartupPresenter.
	 * @param shell        reference to the shell
	 * @param splashScreen reference to the splash screen
	 */
	private StartupPresenter( final StartupShell shell, final SplashScreen splashScreen ) {
		super( shell, splashScreen );
		
		splashG2  = splashScreen.createGraphics();
		
		// Make view nice:
		splashG2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		
		// Draw app name
		final Font oldFont = splashG2.getFont();
		splashG2.setFont( oldFont.deriveFont( Font.BOLD | Font.ITALIC, 35 ) );
		
		final String text = Consts.APP_NAME + Utils.SPACE_STRING + Consts.APP_VERSION_STRING_SHORT;
		
		titleY = component.getSize().height / 3;
		
		splashG2.setColor( Color.BLACK );
		splashG2.drawString( text, getXForCenteredText( text ) + 2, titleY + 2 );
		splashG2.setColor( Color.WHITE );
		splashG2.drawString( text, getXForCenteredText( text ), titleY );
		
		splashG2.setFont( oldFont );
		
		// Copyright text cannot be drawn yet: mainModel.language is not yet set
	}
	
	/**
	 * Displays the copyright text.
	 */
	protected void displayCopyrightText() {
		final Font oldFont = splashG2.getFont();
		splashG2.setFont( oldFont.deriveFont( Font.BOLD | Font.ITALIC, 14 ) );
		
		String email = Utils.EMPTY_STRING;
		for ( final Contact c : Consts.APP_AUTHOR.getContactList() )
			if ( c.getType() == ContactType.EMAIL ) {
				email = c.getValue();
				break;
			}
		
		final String text = "Copyright © " + Consts.COPYRIGHT_YEAR_INFO + Utils.SPACE_STRING + getPersonName( Consts.APP_AUTHOR )
				+ " <" + email + ">";
		
		splashG2.setColor( Color.BLACK );
		splashG2.drawString( text, getXForCenteredText( text ) + 1, titleY + 40 + 1 );
		
		splashG2.setColor( Color.WHITE );
		splashG2.drawString( text, getXForCenteredText( text ), titleY + 40 );
		
		splashG2.setFont( oldFont );
	}
	
	/**
	 * Displays the specified startup phase.
	 * @param phase startup phase to be displayed
	 */
	protected void displayPhase( final StartupPhase phase ) {
		displayText( get( TextKey.STARTUP$PHASE, phase ) );
	}
	
	/**
	 * Displays the specified text.
	 * @param text text to be displayed
	 */
	protected void displayText( final String text ) {
		if ( !component.isVisible() )
			return;
		
		final Dimension size = component.getSize();
		final int fontHeight = splashG2.getFontMetrics().getHeight();
		
		// Erase previous text
		splashG2.setBackground( new Color( 161, 139, 89 ) );
		splashG2.clearRect( 0, size.height - 20 - fontHeight, size.width, fontHeight + splashG2.getFontMetrics().getDescent() + 3 );
		
		// Draw new text
		splashG2.setColor( Color.BLACK );
		splashG2.drawString( text, getXForCenteredText( text ), size.height - 20 );
		
		component.update();
	}
	
	/**
	 * Returns the X coordinate where the specified text must be drawn in order to be centered.
	 * @param text text to be centered
	 * @return the X coordinate where the specified text must be drawn in order to be centered.
	 */
	private int getXForCenteredText( final String text) {
		return ( component.getSize().width - splashG2.getFontMetrics().stringWidth( text ) ) >> 1;
	}
	
	/**
	 * Releases resources allocated for the startup presenter, but does not hide the splash screen.
	 */
	@Override
	public void close() {
		splashG2.dispose();
	}
	
}
