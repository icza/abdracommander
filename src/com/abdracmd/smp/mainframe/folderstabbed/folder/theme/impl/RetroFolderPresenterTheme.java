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
 * Retro folder presenter theme.
 * 
 * @author Andras Belicza
 */
public class RetroFolderPresenterTheme extends BaseFolderPresenterTheme {
	
	/**
	 * Creates a new RetroFolderPresenterTheme.
	 */
	public RetroFolderPresenterTheme() {
		super( TextKey.FOLDER_TABLE$THEME$RETRO );
		
		author = Consts.APP_AUTHOR;
		font   = new Font( Font.MONOSPACED, Font.BOLD, 14 );
		
		// Palette (baseA: dark blue, baseB: cyan-ish)
		final Color baseAColor    = new Color(   4,   2, 172 );
		final Color baseBColor    = new Color(  84, 254, 252 );
		final Color selectedColor = new Color( 252, 254,  84 );
		final Color selBaseAColor = new Color(  64, 202, 201 );
		final Color inactBgColor  = new Color(  44, 128, 214 );
		
		Set< ColorEntityFlag > flags;
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE );
		setColor( baseBColor   , ColorEntity.TEXT      , flags );
		setColor( baseAColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.ALTERNATE );
		setColor( baseBColor   , ColorEntity.TEXT      , flags );
		setColor( baseAColor   , ColorEntity.BACKGROUND, flags );
		
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
		setColor( baseAColor   , ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( selBaseAColor, ColorEntity.BACKGROUND, flags );
		
		flags = EnumSet.of( ColorEntityFlag.ACTIVE, ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE );
		setColor( selectedColor, ColorEntity.TEXT      , flags );
		setColor( selBaseAColor, ColorEntity.BACKGROUND, flags );
		
		scaleActiveColorsToInactive( ColorEntity.TEXT      , 0.85f );
		scaleActiveColorsToInactive( ColorEntity.BACKGROUND, 0.81f );
		
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED ) );
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.ALTERNATE ) );
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED ) );
		setColor( inactBgColor, ColorEntity.BACKGROUND, EnumSet.of( ColorEntityFlag.FOCUSED, ColorEntityFlag.SELECTED, ColorEntityFlag.ALTERNATE ) );
	}
	
}
