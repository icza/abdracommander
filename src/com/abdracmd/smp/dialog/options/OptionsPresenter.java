/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.options;

import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.abdracmd.smp.dialog.BaseDialogPresenter;
import com.abdracmd.smp.dialog.options.OptionsShell.Option;
import com.abdracmd.util.GuiUtils;

/**
 * This is the Options presenter.
 * 
 * @author Andras Belicza
 */
class OptionsPresenter extends BaseDialogPresenter< OptionsModel, OptionsShell > {
	
	/** Box to put the message elements in. */
	private final Box messageBox = Box.createVerticalBox();
	
    /**
     * Creates a new OptionsPresenter.
     * @param shell reference to the shell
     */
    protected OptionsPresenter( final OptionsShell shell ) {
    	super( shell );
    	
    	component.setTitle( model.title );
    	if ( model.iconImage != null )
    		component.setIconImage( model.iconImage );
    	
    	if ( model.bodyIcon != null )
    		component.getContentPane().add( new JLabel( get( model.bodyIcon.xicon ) ), BorderLayout.WEST );
    	
    	messageBox.setBorder( BorderFactory.createEmptyBorder( 15, 15, 15, 15 ) );
		addMessage( model.message );
		GuiUtils.alignBox( messageBox,  SwingConstants.LEFT );
		component.getContentPane().add( messageBox, BorderLayout.CENTER );
		
		// Options
		
    	initButtonsPanel();
    	boolean first = true;
    	
    	if ( model.optionList != null )
	    	for ( final Option option : model.optionList ) {
	    		final JXButton button = new JXButton();
	    		if ( first ) {
	    			component.getRootPane().setDefaultButton( button );
	    			first = false;
	    		}
	    		GuiUtils.initButton( button, null, option.btnTextKey );
	    		button.addActionListener( new ActionListener() {
					@Override
					public void actionPerformed( final ActionEvent event ) {
						model.chosenOption = option;
						component.dispose();
					}
				} );
	    		buttonsPanel.add( button );
	    	}
    	else
	    	for ( final JButton button : model.buttonList ) {
	    		if ( first ) {
	    			component.getRootPane().setDefaultButton( button );
	    			first = false;
	    		}
	    		button.addActionListener( new ActionListener() {
					@Override
					public void actionPerformed( final ActionEvent event ) {
						model.chosenButton = button;
						component.dispose();
					}
				} );
	    		buttonsPanel.add( button );
	    	}
    }
    
    /**
     * Adds the message: adds it or a representing component to the message box.
     * 
     * <p>Handled message types are documented in the {@link OptionsShell} class.</p>
     * 
     * @param message message to be added
     */
    private void addMessage( final Object message ) {
    	if ( message == null )
    		return;
    	
    	if ( message instanceof String )
    		messageBox.add( new JLabel( (String) message ) );
    	else if ( message instanceof JComponent )
    		messageBox.add( (JComponent) message );
    	else if ( message instanceof Object[] ) {
    		for ( final Object subMessage : (Object[]) message )
    			addMessage( subMessage );
    	} else if ( message instanceof Collection ) {
    		for ( final Object subMessage : (Collection< ? >) message )
    			addMessage( subMessage );
    	}
    	else
    		addMessage( message.toString() );
    }
    
}
