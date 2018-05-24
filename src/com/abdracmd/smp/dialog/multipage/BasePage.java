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

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.util.CommonAccessHelper;

/**
 * Base page with some help to implement pages.
 * 
 * @author Andras Belicza
 */
public abstract class BasePage extends CommonAccessHelper implements IPage {
	
	/** Display name of the page. */
	protected final String  displayName;
	/** Icon of the page.         */
	protected final Icon    icon;
	/** List of child pages.      */
	protected List< IPage > childPageList;
	
    /**
     * Creates a new BasePage.
     * @param displayNameTextKey text key of the display name of the page
     * @param xicon              xicon of the page
     */
    public BasePage( final TextKey displayNameTextKey, final XIcon xicon ) {
    	this( MainShell.INSTANCE.getModel().getLanguage().get( displayNameTextKey ),
    			MainShell.INSTANCE.getModel().getIcons().get( xicon ) );
    }
	
    /**
     * Creates a new BasePage.
     * @param displayName display name of the page
     * @param icon        icon of the page
     */
    public BasePage( final String displayName, final Icon icon ) {
    	this.displayName = displayName;
    	this.icon = icon;
    }
	
	/**
	 * Adds a child page to this page.
	 * @param childPage child page to be added
	 */
    public void addChildPage( final IPage childPage ) {
		if ( childPageList == null )
			childPageList = new ArrayList<>();
			
		childPageList.add( childPage );
	}
    
    @Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public Icon getIcon() {
		return icon;
	}
	
	@Override
	public List< ? extends IPage > getChildPageList() {
		return childPageList;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
}
