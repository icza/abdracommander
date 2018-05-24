/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage;

import hu.belicza.andras.util.iface.HasDisplayName;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;



/**
 * A page in a multi-page dialog.
 * 
 * @author Andras Belicza
 * 
 * @see BasePage
 */
public interface IPage extends HasDisplayName {
	
	/**
	 * Returns the icon of the page.
	 * This will appear in the tree.
	 * @return the icon of the page
	 */
	Icon getIcon();
	
	/**
	 * {@link Object#toString()} has to be overridden to return the display name ({@link #getDisplayName()}).
	 * @return the display name ({@link #getDisplayName()})
	 */
	@Override
	String toString();
	
	/**
	 * Creates and returns a new page view component.
	 * This will be wrapped in a {@link JScrollPane} and added to the center of a panel having {@link BorderLayout}.
	 * @return a new page view component
	 */
	JComponent createPage();
	
	/**
	 * Returns the child page list of this page.<br>
	 * If there are child pages returned, this page will be a node in the tree
	 * and the returned child pages will be put under this page.
	 * @return the child page list of this page
	 */
	List< ? extends IPage > getChildPageList();
	
}
