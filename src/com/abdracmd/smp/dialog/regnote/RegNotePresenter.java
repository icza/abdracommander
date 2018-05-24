/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.regnote;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.abdracmd.Consts;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.BaseDialogModel;
import com.abdracmd.smp.dialog.BaseDialogPresenter;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.util.GuiUtils;

/**
 * This is the Registration note presenter.
 * 
 * @author Andras Belicza
 */
class RegNotePresenter extends BaseDialogPresenter< BaseDialogModel, RegNoteShell > {
	
    /**
     * Creates a new RegNotePresenter.
     * @param shell reference to the shell
     */
    protected RegNotePresenter( final RegNoteShell shell ) {
    	super( shell );
    	
    	setTitle( TextKey.REG_NOTE$TITLE );
    	setIcon( XIcon.F_LICENSE_KEY );
    	
    	component.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
    	
    	final Box contentBox = Box.createVerticalBox();
    	
    	// App info
    	final JPanel appInfoPanel = new JPanel();
    	appInfoPanel.add( new JLabel( get( XIcon.MY_APP_ICON_BIG ) ) );
    	appInfoPanel.add( Box.createHorizontalStrut( 5 ) );
    	final Box nameBox = Box.createVerticalBox();
    	final JLabel appNameLabel = new JLabel( Consts.APP_NAME + Utils.SPACE_STRING + Consts.APP_VERSION_STRING_SHORT );
    	appNameLabel.setFont( appNameLabel.getFont().deriveFont( Font.BOLD, appNameLabel.getFont().getSize() + 8 ) );
    	nameBox.add( appNameLabel );
    	nameBox.add( GuiUtils.getCopyrightInfoComponent() );
    	nameBox.add( GuiUtils.createLinkLabel( get( TextKey.GENERAL$HOME_PAGE ), Consts.URL_HOME_PAGE, null ) );
    	GuiUtils.alignBox( nameBox, SwingConstants.LEFT );
    	appInfoPanel.add( nameBox );
    	contentBox.add( appInfoPanel );
    	
    	// Registration info
    	contentBox.add( Box.createVerticalStrut( 20 ) );
    	contentBox.add( GuiUtils.changeFontToBold( new JLabel( get( TextKey.REG_NOTE$NOT_REGISTERED, Consts.APP_NAME ) ) ) );
    	contentBox.add( Box.createVerticalStrut( 10 ) );
    	contentBox.add( new JLabel( get( TextKey.REG_NOTE$REGISTRATION_INFO ) ) );
    	contentBox.add( Box.createVerticalStrut( 10 ) );
    	final JPanel row = new JPanel( new FlowLayout( FlowLayout.CENTER, 5, 0 ) );
    	row.add( new JLabel( get( TextKey.REG_NOTE$REGISTRATION_DETAILS ) ) );
    	row.add( GuiUtils.createLinkLabel( get( TextKey.REG_NOTE$REGISTRATION_PAGE ), Consts.URL_REGISTRATION_PAGE, null ) );
    	contentBox.add( row );
    	
    	// Close buttons panel
    	contentBox.add( Box.createVerticalStrut( 20 ) );
    	final int CLOSE_BUTTONS_COUNT = 5;
    	final int realCloseButtonIdx = (int) (Math.random() * CLOSE_BUTTONS_COUNT);
    	contentBox.add( GuiUtils.changeFontToBold( new JLabel( get( TextKey.REG_NOTE$PRESS_BUTTON_TO_HIDE, realCloseButtonIdx + 1 ) ) ) );
    	contentBox.add( Box.createVerticalStrut( 10 ) );
    	final JPanel closeButtonsPanel = new JPanel( new GridLayout( 1, CLOSE_BUTTONS_COUNT ) );
    	final JXButton[] closeButtons = new JXButton[ CLOSE_BUTTONS_COUNT ];
    	final ActionListener closeActionListener = new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( event.getSource() == closeButtons[ realCloseButtonIdx ] )
					component.dispose();
				else
					MainShell.INSTANCE.exit( true );
			}
		};
    	for ( int i = 0; i < CLOSE_BUTTONS_COUNT; i++ ) {
    		final String text = Integer.toString( i + 1 );
    		closeButtons[ i ] = new JXButton( text );
    		if ( i < 9 )
    			closeButtons[ i ].setMnemonic( text.charAt( 0 ) );
    		closeButtons[ i ].addActionListener( closeActionListener );
    		closeButtonsPanel.add( closeButtons[ i ] );
    	}
    	contentBox.add( closeButtonsPanel );
    	
    	GuiUtils.alignBox( contentBox, SwingConstants.CENTER );
    	component.getContentPane().add( contentBox, BorderLayout.CENTER );
    	
    	component.setResizable( false );
    	
    	setFocusTargetOnFirstShow( closeButtons[ 0 ] );
    }
    
}
