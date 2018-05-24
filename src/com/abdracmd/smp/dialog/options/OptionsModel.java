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

import java.awt.Image;
import java.awt.Window;
import java.util.List;

import javax.swing.JButton;

import com.abdracmd.smp.dialog.BaseDialogModel;
import com.abdracmd.smp.dialog.options.OptionsShell.BodyIcon;
import com.abdracmd.smp.dialog.options.OptionsShell.Option;

/**
 * This is the model of the Options dialog.
 * 
 * @author Andras Belicza
 */
class OptionsModel extends BaseDialogModel {
	
	/** Optional icon image to be used for the dialog. */
	protected final Image           iconImage;
	/** Title of the dialog.                           */
	protected final String          title;
	/** Body icon to be displayed in the dialog.       */
	protected final BodyIcon        bodyIcon;
	/** Message to be displayed in the dialog.         */
	protected final Object          message;
	
	/** Option list to show buttons for.               */
	protected final List< Option >  optionList;
	/** Button list to use as option buttons.          */
	protected final List< JButton > buttonList;
	
	/** Option chosen by the user.       */
	protected Option  chosenOption;
	/** Button chosen by the user.       */
	protected JButton chosenButton;
	
    /**
     * Creates a new OptionsModel setting the common attributes.
     * 
     * <p>One and only one of <code>optionList</code> and <code>buttonList</code> must be provided.</p>
     * 
	 * @param parentWindow optional reference to the parent window
	 * @param iconImage    optional icon image to be used for the dialog
	 * @param title        title of the dialog
	 * @param bodyIcon     optional body icon to be displayed in the dialog
	 * @param message      message to be displayed in the dialog
	 * @param optionList   option list to show buttons for
	 * @param buttonList   button list to use as option buttons
	 * 
	 * @throws IllegalArgumentException if both or none of the option list and button list are provided
     */
    protected OptionsModel( final Window parentWindow, final Image iconImage, final String title, final BodyIcon bodyIcon, final Object message,
    		final List< Option > optionList, final List< JButton > buttonList ) throws IllegalArgumentException {
    	super( parentWindow );
    	
    	if ( !( optionList != null ^ buttonList != null ) ) // XOR
    		throw new IllegalArgumentException( "One and only one of optionList and buttonList must be provided!" );
    	
    	this.iconImage  = iconImage;
    	this.title      = title;
    	this.bodyIcon   = bodyIcon;
    	this.message    = message;
    	
    	this.optionList = optionList;
    	this.buttonList = buttonList;
    }
    
}
