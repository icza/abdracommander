/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl;

import java.awt.Color;
import java.awt.Font;
import java.util.EnumSet;
import java.util.Set;

import com.abdracmd.Consts;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntity;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntityFlag;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl.base.BaseFolderPresenterTheme;

/**
 * Accessibility folder presenter theme (for visually impaired ones).
 * 
 * @author Andras Belicza
 */
public class AccessibilityFolderPresenterTheme extends BaseFolderPresenterTheme {
	
	/**
	 * Creates a new AccessibilityFolderPresenterTheme.
	 */
	public AccessibilityFolderPresenterTheme() {
		super( TextKey.FOLDER_TABLE$THEME$ACCESSIBILITY );
		
		author      = Consts.APP_AUTHOR;
		font        = new Font( "Arial", Font.BOLD, 16 );
		borderWidth = 3;
		
		// Palette
		final Color textColor     = new Color( 255, 255, 255 );
		final Color bgColor       = new Color(   0,   0,   0 );
		final Color bgColor2      = new Color(  30,  30,  30 );
		final Color borderColor   = new Color( 150,  75,   0 );
		final Color selectedColor = new Color( 255, 255,   0 );
		final Color selFocColor   = new Color( 130, 130,   0 );
		final Color inactBgColor  = new Color( 128, 128, 128 );
		
		Set< ColorEntityFlag > flags;
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE );
		setColor( textColor    , ColorEntity.TEXT      , flags );
		setColor( bgColor      , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.ALTERNATE );
		setColor( textColor    , ColorEntity.TEXT      , flags );
		setColor( bgColor2     , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED );
		setColor( bgColor      , ColorEntity.TEXT      , flags );
		setColor( textColor    , ColorEntity.BACKGROUND, flags );
		setColor( borderColor  , ColorEntity.BORDER    , flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.ALTERNATE );
		setColor( bgColor      , ColorEntity.TEXT      , flags );
		setColor( textColor    , ColorEntity.BACKGROUND, flags );
		setColor( borderColor  , ColorEntity.BORDER    , flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.SELECTED );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( bgColor      , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( bgColor2     , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED );
		setColor( selFocColor  , ColorEntity.TEXT      , flags );
		setColor( textColor    , ColorEntity.BACKGROUND, flags );
		setColor( borderColor  , ColorEntity.BORDER    , flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE );
		setColor( selFocColor  , ColorEntity.TEXT      , flags );
		setColor( textColor    , ColorEntity.BACKGROUND, flags );
		setColor( borderColor  , ColorEntity.BORDER    , flags );
		
		scaleActiveColorsToInactive( ColorEntity.TEXT      , 0.67f );
		scaleActiveColorsToInactive( ColorEntity.BACKGROUND, 0.80f );
		scaleActiveColorsToInactive( ColorEntity.BORDER    , 0.70f );
		
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED ) );
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.ALTERNATE ) );
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED ) );
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE ) );
	}
	
}
