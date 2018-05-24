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
 * Nature (greenish) folder presenter theme.
 * 
 * @author Andras Belicza
 */
public class NatureFolderPresenterTheme extends BaseFolderPresenterTheme {
	
	/**
	 * Creates a new NatureFolderPresenterTheme.
	 */
	public NatureFolderPresenterTheme() {
		super( TextKey.FOLDER_TABLE$THEME$NATURE );
		
		author = Consts.APP_AUTHOR;
		
		updateUI();
		
		// Palette (baseA: greenish, baseB: purple-ish)
		final Color baseAColor    = new Color(  45, 146,  12 );
		final Color baseAColor2   = new Color(  34, 141,   0 );
		final Color baseBColor    = new Color(  89,  38, 128 );
		final Color selectedColor = new Color( 255, 202, 115 );
		final Color inactBgColor  = new Color(  60,  90,  64 );
		
		Set< ColorEntityFlag > flags;
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE );
		setColor( baseBColor   , ColorEntity.TEXT      , flags );
		setColor( baseAColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.ALTERNATE );
		setColor( baseBColor   , ColorEntity.TEXT      , flags );
		setColor( baseAColor2  , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED );
		setColor( baseAColor   , ColorEntity.TEXT      , flags );
		setColor( baseBColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.ALTERNATE );
		setColor( baseAColor   , ColorEntity.TEXT      , flags );
		setColor( baseBColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.SELECTED );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( baseAColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( baseAColor2  , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( baseBColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( baseBColor   , ColorEntity.BACKGROUND, flags );
		
		scaleActiveColorsToInactive( ColorEntity.TEXT      , 0.90f );
		scaleActiveColorsToInactive( ColorEntity.BACKGROUND, 0.90f );
		
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
