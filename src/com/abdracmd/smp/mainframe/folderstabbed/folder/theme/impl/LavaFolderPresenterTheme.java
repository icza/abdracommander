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
 * Lava (redish) folder presenter theme.
 * 
 * @author Andras Belicza
 */
public class LavaFolderPresenterTheme extends BaseFolderPresenterTheme {
	
	/**
	 * Creates a new LavaFolderPresenterTheme.
	 */
	public LavaFolderPresenterTheme() {
		super( TextKey.FOLDER_TABLE$THEME$LAVA );
		
		author = Consts.APP_AUTHOR;
		
		updateUI();
		
		// Palette (baseA: redish, baseB: blue-ish)
		final Color baseAColor    = new Color( 197,  34,  53 );
		final Color baseAColor2   = new Color( 183,  46,  62 );
		final Color baseBColor    = new Color(   5,  47, 109 );
		final Color selectedColor = new Color( 166, 162,   0 );
		final Color inactBgColor  = new Color(  94,  46,  86 );
		
		Set< ColorEntityFlag > flags;
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE );
		setColor( baseBColor   , ColorEntity.TEXT      , flags );
		setColor( baseAColor2  , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.ALTERNATE );
		setColor( baseBColor   , ColorEntity.TEXT      , flags );
		setColor( baseAColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED );
		setColor( baseAColor2  , ColorEntity.TEXT      , flags );
		setColor( baseBColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.ALTERNATE );
		setColor( baseAColor2  , ColorEntity.TEXT      , flags );
		setColor( baseBColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.SELECTED );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( baseAColor2  , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( baseAColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( baseBColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( baseBColor   , ColorEntity.BACKGROUND, flags );
		
		scaleActiveColorsToInactive( ColorEntity.TEXT      , 0.85f );
		scaleActiveColorsToInactive( ColorEntity.BACKGROUND, 0.85f );
		
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED ) );
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.ALTERNATE ) );
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED ) );
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE ) );
	}
	
	@Override
	public void updateUI() {
		// We want to use the LAF's default font.
		// Using the default also results in faster startup, no new font will be loaded!
		font = UIManager.getFont( "Table.font" );
	}
	
}
