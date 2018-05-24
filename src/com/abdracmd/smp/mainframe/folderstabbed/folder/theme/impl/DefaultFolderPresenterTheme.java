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
import java.util.EnumSet;
import java.util.Set;

import javax.swing.UIManager;

import com.abdracmd.Consts;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntity;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntityFlag;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl.base.BaseFolderPresenterTheme;

/**
 * Default folder presenter theme.
 * 
 * @author Andras Belicza
 */
public class DefaultFolderPresenterTheme extends BaseFolderPresenterTheme {
	
	/**
	 * Creates a new DefaultFolderPresenterTheme.
	 */
	public DefaultFolderPresenterTheme() {
		super( TextKey.FOLDER_TABLE$THEME$DEFAULT );
		
		author = Consts.APP_AUTHOR;
		
		updateUI();
		
		// Palette
		final Color textColor      = new Color(   0,   0,   0 );
		final Color bgColor        = new Color( 255, 255, 255 );
		final Color bgColor2       = new Color( 235, 235, 235 );
		final Color borderColor    = new Color(   0,   0, 255 );
		final Color selectedColor  = new Color( 240,   0,   0 );
		final Color inactBorderCol = new Color( 100, 100, 170 );
		
		Set< ColorEntityFlag > flags;
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE );
		setColor( textColor    , ColorEntity.TEXT      , flags );
		setColor( bgColor      , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.ALTERNATE );
		setColor( textColor    , ColorEntity.TEXT      , flags );
		setColor( bgColor2     , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED );
		setColor( textColor    , ColorEntity.TEXT      , flags );
		setColor( bgColor      , ColorEntity.BACKGROUND, flags );
		setColor( borderColor  , ColorEntity.BORDER    , flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.ALTERNATE );
		setColor( textColor    , ColorEntity.TEXT      , flags );
		setColor( bgColor2     , ColorEntity.BACKGROUND, flags );
		setColor( borderColor  , ColorEntity.BORDER    , flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.SELECTED );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( bgColor      , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( bgColor2     , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( bgColor      , ColorEntity.BACKGROUND, flags );
		setColor( borderColor  , ColorEntity.BORDER    , flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( bgColor2     , ColorEntity.BACKGROUND, flags );
		setColor( borderColor  , ColorEntity.BORDER    , flags );
		
		scaleActiveColorsToInactive( ColorEntity.TEXT      , 0.90f );
		scaleActiveColorsToInactive( ColorEntity.BACKGROUND, 0.90f );
		
		setColor( inactBorderCol, ColorEntity.BORDER, EnumSet.of( ColorEntityFlag.FOCUSED ) );
		setColor( inactBorderCol, ColorEntity.BORDER, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.ALTERNATE ) );
		setColor( inactBorderCol, ColorEntity.BORDER, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED ) );
		setColor( inactBorderCol, ColorEntity.BORDER, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE ) );
		
	}
	
	@Override
	public void updateUI() {
		// We want to use the LAF's default font.
		// Using the default also results in faster startup, no new font will be loaded!
		font = UIManager.getFont( "Table.font" );
	}
	
}
