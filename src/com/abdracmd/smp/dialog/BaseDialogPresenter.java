/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog;

import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.BasePresenter;
import com.abdracmd.util.GuiUtils;

/**
 * This is the base of presenters whose component is a {@link JDialog}.
 * 
 * <p><strong>Warning!</strong> Subclasses should note that at the end of their constructor
 * the dialog should not be made visible (with {@link #packAndshow()} for example) because
 * if the dialog is modal (it is by default) the constructor execution will block until the dialog
 * is disposed and therefore the presenter reference will not be set at the shell who creates the presenter!<br>
 * <i>It should be the shell's responsibility to make the dialog visible!</i></p>
 * 
 * @param < Model > model type associated with this base dialog presenter
 * @param < Shell > shell type associated with this base dialog presenter
 * 
 * @author Andras Belicza
 */
public class BaseDialogPresenter< Model extends BaseDialogModel, Shell extends BaseDialogShell< Model, ? extends BaseDialogPresenter< Model, Shell > > >
	extends BasePresenter< Model, Shell, JDialog > {
	
	/** Buttons panel where close buttons are added. */
	protected JPanel     buttonsPanel;
	
	/** Component to be focused on first show. */
	protected JComponent focusTargetOnFirstShow;
	
    /**
     * Creates a new BaseDialogPresenter.
     * @param shell reference to the shell
     */
    protected BaseDialogPresenter( final Shell shell ) {
    	super( shell );
    	
    	component = model.parentWindow instanceof Dialog ? new JDialog( (Dialog) model.parentWindow, true ) : new JDialog( (Frame) model.parentWindow, true );
    	component.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
    	
    	// Default inner border
    	( (JPanel) component.getContentPane() ).setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
    	
    	component.addWindowListener( new WindowAdapter() {
    		@Override
    		public void windowActivated( final WindowEvent event ) {
   				component.removeWindowListener( this );
	    		if ( focusTargetOnFirstShow != null )
	    			GuiUtils.requestFocusInWindowLater( focusTargetOnFirstShow );
    		}
		} );
    	
    	// Register ESC to close the dialog
		final Object actionKey = new Object();
		component.getLayeredPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), actionKey );
		component.getLayeredPane().getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				component.dispose();
			}
		} );
    }
    
    /**
     * Sets the dialog title.
     * @param textKey title text key
     */
    protected void setTitle( final TextKey textKey ) {
    	component.setTitle( get( textKey ) );
    }
    
    /**
     * Sets the dialog title.
     * @param textKey title text key
     * @param params  parameters of the title text
     */
    protected void setTitle( final TextKey textKey, final Object... params ) {
    	component.setTitle( get( textKey, params ) );
    }
    
    /**
     * Sets the dialog icon.
     * @param xicon xicon to be set
     */
    protected void setIcon( final XIcon xicon ) {
    	component.setIconImage( get( xicon ).getImage() );
    }
    
    /**
     * Adds a close button using the {@link TextKey#GENERAL$BTN$CLOSE} text key.
     * @return the close button that was just created and added
     */
    protected JXButton addCloseButton() {
    	return addCloseButton( TextKey.GENERAL$BTN$CLOSE );
    }
    
    /**
     * Adds a close button using the specified text key.
     * @param textKey text key of the close button
     * @return the close button that was just created and added
     */
    protected JXButton addCloseButton( final TextKey textKey ) {
    	initButtonsPanel();
    	
    	final JXButton closeButton = new JXButton();
    	GuiUtils.initButton( closeButton, null, textKey );
    	closeButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				component.dispose();
			}
		} );
    	buttonsPanel.add( closeButton );
    	
    	return closeButton;
    }
    
    /**
     * Initializes the buttons panel.<br>
     * Creates it and adds it to the south of the dialog's content pane.<br>
     * Does nothing if the buttons panel has already been initialized. 
     */
    protected void initButtonsPanel() {
    	if ( buttonsPanel == null ) {
    		buttonsPanel = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
        	component.getContentPane().add( buttonsPanel, BorderLayout.SOUTH );
    	}
    }
    
    /**
     * Sets the component to be focused on first show.
     * @param focusTarget focus target component on first show to be set
     */
    protected void setFocusTargetOnFirstShow( final JComponent focusTarget ) {
    	focusTargetOnFirstShow = focusTarget;
    }
    
    /**
     * Packs, centers to the parent and shows the dialog.
     */
    public void packAndshow() {
    	component.pack();
    	component.setLocationRelativeTo( model.parentWindow ); // Center to the parent window
    	
    	component.setVisible( true );
    }
    
    /**
	 * Resizes the dialog by setting its bounds to maximum that fits inside the default screen having the specified margin around it,
	 * centers it on the screen and shows it.
	 * @param margin  margin to leave around the dialog
	 * @param maxSize optional parameter defining a maximum size
     */
    public void maximizeWindowWithMarginAndShow( final int margin, final Dimension maxSize ) {
    	GuiUtils.maximizeWindowWithMargin( component, 20, new Dimension( 1000, 900 ) );
    	component.setVisible( true );
    }
    
}
