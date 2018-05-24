/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage.about.pageimpl;

import hu.belicza.andras.util.Utils;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.abdracmd.Consts;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.uicomponent.PersonCard;
import com.abdracmd.util.GuiUtils;

/**
 * General about page.
 * 
 * @author Andras Belicza
 */
public class GeneralPage extends BasePage {
	
    /**
     * Creates a new GeneralPage.
     */
    public GeneralPage() {
    	super( TextKey.ABOUT$PAGE$GENERAL$NAME, XIcon.MY_APP_ICON_SMALL );
    }
    
	@Override
	public JComponent createPage() {
    	final Box box = Box.createVerticalBox();
    	
		final JLabel logoLabel = GuiUtils.createLinkLabel( null, Consts.URL_HOME_PAGE, XIcon.MY_APP_LOGO );
		logoLabel.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
    	box.add( logoLabel );
    	
    	final JLabel appNameLabel = new JLabel( Consts.APP_NAME + Utils.SPACE_STRING + Consts.APP_VERSION_STRING_SHORT
    			+ " - " + get( TextKey.ABOUT$PAGE$GENERAL$BUILD ) + Utils.SPACE_STRING + Consts.APP_VERSION_BUILD );
    	appNameLabel.setFont( appNameLabel.getFont().deriveFont( Font.BOLD, appNameLabel.getFont().getSize() + 5 ) );
    	box.add( appNameLabel );
    	
    	box.add( GuiUtils.getCopyrightInfoComponent() );
    	
    	box.add( GuiUtils.wrapInPanel( GuiUtils.createLinkLabel( get( TextKey.GENERAL$HOME_PAGE ), Consts.URL_HOME_PAGE, null ) ) );
    	
    	box.add( Box.createVerticalStrut( 10 ) );
    	
    	box.add( new JLabel( get( TextKey.ABOUT$PAGE$GENERAL$THANK_YOU, Consts.APP_NAME ) ) );
    	
    	box.add( Box.createVerticalStrut( 10 ) );
    	
    	box.add( GuiUtils.wrapInPanel( new PersonCard( Consts.APP_AUTHOR, get( TextKey.ABOUT$PAGE$GENERAL$AUTHORS_CARD ) ) ) );
    	
    	GuiUtils.alignBox( box, SwingConstants.CENTER );
		
		return GuiUtils.wrapInPanel( box ); // Wrapping required due to the home page link (which want to consume all available space...)
	}
	
}
