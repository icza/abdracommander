/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl.base;

import hu.belicza.andras.util.bean.BaseHasDisplayName;

import java.awt.Color;
import java.awt.Font;
import java.util.EnumSet;
import java.util.Set;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntity;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntityFlag;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.IFolderPresenterTheme;
import com.abdracmd.util.bean.person.Person;

/**
 * Base Folder graphical them with some help to implement themes.
 * 
 * @author Andras Belicza
 */
public abstract class BaseFolderPresenterTheme extends BaseHasDisplayName implements IFolderPresenterTheme {
	
	/** Author of the theme.        */
	protected Person author;
	/** Text font.                  */
	protected Font   font;
	/** Width of borders in pixels. */
	protected int    borderWidth;
	
	/**
	 * Creates a new BaseFolderPresenterTheme.
	 * Initializes border width with 1.
     * @param displayNameTextKey text key of the name of the column
	 */
	public BaseFolderPresenterTheme( final TextKey displayNameTextKey ) {
		this( MainShell.INSTANCE.getModel().getLanguage().get( displayNameTextKey ) );
	}
	
	/**
	 * Creates a new BaseFolderPresenterTheme.
	 * Initializes border width with 1.
	 * @param displayName display name of the theme
	 */
	public BaseFolderPresenterTheme( final String displayName ) {
		super( displayName );
		
		borderWidth = 1;
	}
	
	@Override
	public Person getAuthor() {
		return author;
	}
	
	@Override
	public Font getFont() {
		return font;
	}
	
	@Override
	public int getBorderWidth() {
		return borderWidth;
	}
	
	@Override
	public Color getColor( final ColorEntity entity, final Set< ColorEntityFlag > flags ) {
		return entityColors[ getKey( entity, flags ) ];
	}
	
	/**
	 * Usually the inactive colors are the darker versions of the active colors.
	 * Scales all non-null active colors of the specified entity and sets them as the inactive colors.
	 * @param entity entity whose active colors to be scaled
	 * @param factor factor to scale the active colors with, to produce the inactive color
	 */
	protected void scaleActiveColorsToInactive( final ColorEntity entity, final float factor ) {
		final int activeFlagMask = BIT_MASKS[ ColorEntityFlag.ACTIVE.ordinal() ];
		
		final float[] activeRgbComps = new float[ 3 ];
		Color activeColor;
		
		final int start = getKey( entity, EnumSet.noneOf( ColorEntityFlag.class ) );
		final int end   = getKey( entity, EnumSet.allOf ( ColorEntityFlag.class ) );
		for ( int i = start; i <= end; i++ )
			if ( ( i & activeFlagMask ) == 0 && ( activeColor = entityColors[ i + activeFlagMask ] ) != null ) {
				activeColor.getRGBColorComponents( activeRgbComps );
				entityColors[ i ] = new Color( activeRgbComps[ 0 ] * factor, activeRgbComps[ 1 ] * factor, activeRgbComps[ 2 ] * factor );
			}
	}
	
	/**
	 * This implementation does nothing.
	 * Subclasses may override this if reconfiguration is required.
	 */
	@Override
	public void updateUI() {
	}
	
	
	// COLOR handling implementation
	
	/** Number of color entity flags. */
	private static final int FLAGS_COUNT = ColorEntityFlag.values().length;
	
	/** Bit masks to help getting keys. */
	private static final int[] BIT_MASKS = new int[ FLAGS_COUNT ];
	static {
		for ( int i = 0; i < BIT_MASKS.length; i++ )
			BIT_MASKS[ i ] = 0x01 << i;
	}
	
	/** Number of entity colors (number of all entity-flag permutations). */
	private static final int ENTITY_COLORS_COUNT = getKey( ColorEntity.values()[ ColorEntity.values().length - 1 ], EnumSet.allOf( ColorEntityFlag.class ) ) + 1;
	
	/** Entity colors. */
	private final Color[] entityColors = new Color[ ENTITY_COLORS_COUNT ];
	
	/**
	 * Sets the entity color specified by the flags.
	 * @param color  entity color to be set
	 * @param entity entity whose color to set
	 * @param flags  modifier flags
	 */
	protected void setColor( final Color color, final ColorEntity entity, final Set< ColorEntityFlag > flags ) {
		entityColors[ getKey( entity, flags ) ] = color;
	}
	
	/**
	 * Returns the internal array index for the specified entity and for the specified flags
	 * @param entity entity
	 * @param flags  flags
	 * @return the internal array index for the specified entity and for the specified flags
	 */
	private static int getKey( final ColorEntity entity, final Set< ColorEntityFlag > flags ) {
		int key = entity.ordinal() << FLAGS_COUNT;
		
		for ( final ColorEntityFlag flag : flags )
			key += BIT_MASKS[ flag.ordinal() ];
		
		return key;
	}
	
}
