/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.uicomponent;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * An extended {@link JButton} which also performs action when the Enter key is pressed.
 * 
 * @author Andras Belicza
 */
public class JXButton extends JButton {
	
	/** Default serial version ID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new JXButton.
	 */
	public JXButton() {
		this( null, null );
	}
	
	/**
	 * Creates a new JXButton.
	 * @param action action of the button
	 */
	public JXButton( final Action action ) {
		super( action );
		registerEnter();
	}
	
	/**
	 * Creates a new JXButton.
	 * @param icon icon of the button
	 */
	public JXButton( final Icon icon ) {
		this( null, icon );
	}
	
	/**
	 * Creates a new JXButton.
	 * @param text text of the button
	 */
	public JXButton( final String text ) {
		this( text, null );
	}
	
	/**
	 * Creates a new JXButton.
	 * @param text text of the button
	 * @param icon icon of the button
	 */
	public JXButton( final String text, final Icon icon ) {
		super( text, icon );
		registerEnter();
	}
	
	/**
	 * Registers the Enter key to perform the button's action.
	 */
	private void registerEnter() {
		final Object actionKey = new Object();
		
		getInputMap( JComponent.WHEN_FOCUSED ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), actionKey );
		getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				doClick( 0 );
			}
		} );
	}
	
}
