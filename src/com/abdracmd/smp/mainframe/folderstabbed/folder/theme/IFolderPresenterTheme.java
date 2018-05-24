/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder.theme;

import hu.belicza.andras.util.iface.HasDisplayName;

import java.awt.Color;
import java.awt.Font;
import java.util.Set;

import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl.base.BaseFolderPresenterTheme;
import com.abdracmd.util.bean.person.Person;

/**
 * Folder Presenter graphical theme.
 * 
 * Color design links:
 * <ul>
 *  <li><b>http://colorschemedesigner.com/</b>
 *  <li>http://www.colorspire.com/rgb-color-wheel/
 *  <li>http://www.color-wheel-pro.com/color-theory-basics.html
 *  <li>http://designwashere.com/complementary-colors-in-web-design/
 *  <li>http://www.j6design.com.au/ClientArea/Howtochoosetherightcolour
 * </ul>
 * 
 * @author Andras Belicza
 * 
 * @see BaseFolderPresenterTheme
 */
public interface IFolderPresenterTheme extends HasDisplayName {
	
	/**
	 * Returns the author of the theme.
	 * @return the author of the theme
	 */
	Person getAuthor();
	
	/**
	 * Returns the font to be used for texts.
	 * @return the font to be used for texts
	 */
	Font getFont();
	
	/**
	 * Returns the border width to be used, in pixels.
	 * @return the border width to be used, in pixels
	 */
	int getBorderWidth();
	
	/**
	 * Returns the entity color specified by the flags.
	 * @param entity entity whose color to return
	 * @param flags  modifier flags
	 * @return the entity color specified by the flags
	 */
	Color getColor( ColorEntity entity, Set< ColorEntityFlag > flags );
	
	/**
	 * Called when the folder presenter's UI is updated (due to Look and Feel has been changed for example).
	 * UI dependant properties can be updated here
	 * (for example if JTable's default font is used which is LaF dependant).
	 */
	void updateUI();
	
}
