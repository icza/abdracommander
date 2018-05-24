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

import hu.belicza.andras.util.Utils;

import java.awt.Image;
import java.awt.Window;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.BaseDialogShell;
import com.abdracmd.smp.main.MainShell;

/**
 * This is the shell of the Options dialog.
 * 
 * <p>Handled message types:
 * <ul>
 * 	<li>{@link String}: will be displayed as a {@link JLabel}
 * 	<li>{@link JComponent}: will be displayed as-is
 * 	<li><code>Object[]</code>: each of its elements will be processed, recursively
 * 	<li>{@link Collection}: each of its elements will be processed, recursively
 * 	<li>{@link Object}: {@link Object#toString()} will be processed as the message (will be handled as a {@link String})
 * </ul></p>
 * 
 * @author Andras Belicza
 */
public class OptionsShell extends BaseDialogShell< OptionsModel, OptionsPresenter > {
	
	/**
	 * Body icon.
	 * @author Andras Belicza
	 */
	public enum BodyIcon {
		/** Info body icon.     */
		INFO    ( XIcon.F_32_INFORMATION  ),
		/** Warning body icon.  */
		WARNING ( XIcon.F_32_EXCLAMATION  ),
		/** Question body icon. */
		QUESTION( XIcon.F_32_QUESTION     ),
		/** Error body icon.    */
		ERROR   ( XIcon.F_32_CROSS_CIRCLE );
		
		/** Xicon of the body icon. */
		public final XIcon xicon;
		
		/**
		 * Creates a new BodyIcon.
		 * @param xicon xicon of the body icon
		 */
		private BodyIcon( final XIcon xicon ) {
			this.xicon = xicon;
		}
	}
	
	/**
	 * Options (buttons).
	 * @author Andras Belicza
	 *
	 */
	public enum Option {
		/** OK option.     */
		OK    ( TextKey.GENERAL$BTN$OK     ),
		/** Yes option.    */
		YES   ( TextKey.GENERAL$BTN$YES    ),
		/** No option.     */
		NO    ( TextKey.GENERAL$BTN$NO     ),
		/** Cancel option. */
		CANCEL( TextKey.GENERAL$BTN$CANCEL );
		
		/** Text key of the button for this option. */
		public final TextKey btnTextKey;
		
		/**
		 * Creates a new Option.
		 * @param btnTextKey text key of the button for this option
		 */
		private Option( final TextKey btnTextKey ) {
			this.btnTextKey = btnTextKey;
		}
	}
	
	
	/** Option list containing no options (empty list).                                         */
	public static final List< Option > OPTIONS_EMPTY         = Utils.cast( Utils.EMPTY_LIST );
	/** Option list containing {@link Option#OK}.                                               */
	public static final List< Option > OPTIONS_OK            = Collections.unmodifiableList( Utils.asNewList( Option.OK ) );
	/** Option list containing {@link Option#OK} and {@link Option#CANCEL}.                     */
	public static final List< Option > OPTIONS_OK_CANCEL     = Collections.unmodifiableList( Utils.asNewList( Option.OK, Option.CANCEL ) );
	/** Option list containing {@link Option#YES} and {@link Option#NO}.                        */
	public static final List< Option > OPTIONS_YES_NO        = Collections.unmodifiableList( Utils.asNewList( Option.YES, Option.NO ) );
	/** Option list containing {@link Option#YES}, {@link Option#NO} and {@link Option#CANCEL}. */
	public static final List< Option > OPTIONS_YES_NO_CANCEL = Collections.unmodifiableList( Utils.asNewList( Option.YES, Option.NO, Option.CANCEL ) );
	
	
    /**
     * Creates and returns a new OptionsShell initialized with an option list.
     * 
	 * @param parentWindow optional reference to the parent window
	 * @param xicon        optional xicon to be used for the dialog
	 * @param titleTextKey text key of the title of the dialog
	 * @param bodyIcon     optional body icon to be displayed in the dialog
	 * @param message      message to be displayed in the dialog; see the class doc of {@link OptionsShell} for handled message types
	 * @param optionList   option list to show buttons for; the first one will be the default option
	 * @param params       optional parameters of the title text
	 * 
	 * @return the created options shell initialized with an option list
     */
	public static OptionsShell withOptionList( final Window parentWindow, final XIcon xicon, final TextKey titleTextKey, final BodyIcon bodyIcon,
			final Object message, final List< Option > optionList, final Object... params ) {
		return new OptionsShell( parentWindow, xicon == null ? null : MainShell.INSTANCE.get( xicon ).getImage(),
				MainShell.INSTANCE.get( titleTextKey, params ), bodyIcon, message, optionList, null );
	}
	
    /**
     * Creates and returns a new OptionsShell initialized with an option list.
     * 
	 * @param parentWindow optional reference to the parent window
	 * @param iconImage    optional icon image to be used for the dialog
	 * @param title        title of the dialog
	 * @param bodyIcon     optional body icon to be displayed in the dialog
	 * @param message      message to be displayed in the dialog; see the class doc of {@link OptionsShell} for handled message types
	 * @param optionList   option list to show buttons for; the first one will be the default option
	 * 
	 * @return the created options shell initialized with an option list
     */
	public static OptionsShell withOptionList( final Window parentWindow, final Image iconImage, final String title, final BodyIcon bodyIcon,
			final Object message, final List< Option > optionList ) {
		return new OptionsShell( parentWindow, iconImage, title, bodyIcon, message, optionList, null );
	}
	
    /**
     * Creates and returns a new OptionsShell initialized with an option list.
     * 
	 * @param parentWindow optional reference to the parent window
	 * @param xicon        optional xicon to be used for the dialog
	 * @param titleTextKey text key of the title of the dialog
	 * @param bodyIcon     optional body icon to be displayed in the dialog
	 * @param message      message to be displayed in the dialog; see the class doc of {@link OptionsShell} for handled message types
	 * @param buttonList   button list to use as option buttons; the first one will be the default option
	 * @param params       optional parameters of the title text
	 * 
	 * @return the created options shell initialized with an option list
     */
	public static OptionsShell withButtonList( final Window parentWindow, final XIcon xicon, final TextKey titleTextKey, final BodyIcon bodyIcon,
			final Object message, final List< JButton > buttonList, final Object... params ) {
		return new OptionsShell( parentWindow, xicon == null ? null : MainShell.INSTANCE.get( xicon ).getImage(),
				MainShell.INSTANCE.get( titleTextKey, params ), bodyIcon, message, null, buttonList );
	}
	
    /**
     * Creates and returns a new OptionsShell initialized with an option list.
     * 
	 * @param parentWindow optional reference to the parent window
	 * @param iconImage    optional icon image to be used for the dialog
	 * @param title        title of the dialog
	 * @param bodyIcon     optional body icon to be displayed in the dialog
	 * @param message      message to be displayed in the dialog; see the class doc of {@link OptionsShell} for handled message types
	 * @param buttonList   button list to use as option buttons; the first one will be the default option
	 * 
	 * @return the created options shell initialized with an option list
     */
	public static OptionsShell withButtonList( final Window parentWindow, final Image iconImage, final String title, final BodyIcon bodyIcon,
			final Object message, final List< JButton > buttonList ) {
		return new OptionsShell( parentWindow, iconImage, title, bodyIcon, message, null, buttonList );
	}
	
	
    /**
     * Creates a new OptionsShell.
     * 
     * <p>One and only one of <code>optionList</code> and <code>buttonList</code> must be provided.</p>
     * 
	 * @param parentWindow optional reference to the parent window
	 * @param iconImage    optional icon image to be used for the dialog
	 * @param title        title of the dialog
	 * @param bodyIcon     optional body icon to be displayed in the dialog
	 * @param message      message to be displayed in the dialog; see the class doc of {@link OptionsShell} for handled message types
	 * @param optionList   option list to show buttons for; the first one will be the default option
	 * @param buttonList   button list to use as option buttons; the first one will be the default option
	 * 
	 * @throws IllegalArgumentException if both or none of the option list and button list are provided
     */
    private OptionsShell( final Window parentWindow, final Image iconImage, final String title, final BodyIcon bodyIcon, final Object message,
    		final List< Option > optionList, final List< JButton > buttonList ) throws IllegalArgumentException {
    	super( new OptionsModel( parentWindow, iconImage, title, bodyIcon, message, optionList, buttonList ) );
    	
    	presenter = new OptionsPresenter( this );
    }
	
    /**
     * Makes the dialog visible, and blocks until the user selects an option.
     * 
     * @return the option selected by the user; may be <code>null</code> if the dialog is closed by pressing the ESC button or by clicking on the Close icon
     * 
     * @throws IllegalStateException if the options shell was not initialized with option list
     * 
     * @see #getButton()
     * @see #getButtonIdx()
     */
    public Option getOption() throws IllegalStateException {
    	if ( model.optionList == null )
    		throw new IllegalStateException( "OptionsShell was not initialized with option list, getOption() cannot be used!" );
    	
    	presenter.packAndshow();
    	
    	return model.chosenOption;
    }
    
    /**
     * Makes the dialog visible, and blocks until the user selects an option (performs action on a button).
     * 
     * @return the button activated by the user; may be <code>null</code> if the dialog is closed by pressing the ESC button or by clicking on the Close icon
     * 
     * @throws IllegalStateException if the options shell was not initialized with button list
     * 
     * @see #getOption()
     * @see #getButtonIdx()
     */
    public JButton getButton() throws IllegalStateException {
    	if ( model.buttonList == null )
    		throw new IllegalStateException( "OptionsShell was not initialized with button list, getButton() cannot be used!" );
    	
    	presenter.packAndshow();
    	
    	return model.chosenButton;
    }
    
    /**
     * Makes the dialog visible, and blocks until the user selects an option (performs action on a button).
     * 
     * @return the button index activated by the user; may be <code>null</code> if the dialog is closed by pressing the ESC button or by clicking on the Close icon
     * 
     * @throws IllegalStateException if the options shell was not initialized with button list
     * 
     * @see #getButton()
     * @see #getOption()
     */
    public Integer getButtonIdx() throws IllegalStateException {
    	getButton();
    	
    	return model.chosenButton == null ? null : model.buttonList.indexOf( model.chosenButton );
    }
    
}
