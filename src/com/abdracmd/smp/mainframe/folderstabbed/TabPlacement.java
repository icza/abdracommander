/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed;

import javax.swing.JTabbedPane;

import com.abdracmd.util.AcUtils;

/**
 * Tab placement.
 * 
 * @author Andras Belicza
 */
public enum TabPlacement {
	
	/** Top.    */
	TOP   ( JTabbedPane.TOP    ),
	/** Bottom. */
	BOTTOM( JTabbedPane.BOTTOM ),
	/** Left.   */
	LEFT  ( JTabbedPane.LEFT   ),
	/** Right.  */
	RIGHT ( JTabbedPane.RIGHT  );
	
	/** Tabbed pane constant associated with this tab placement. */
	public final int tabbedPaneConst;
	
    /**
     * Creates a new TabPlacement.
     * @param tabbedPaneConst tabbed pane constant associated with this tab placement
     */
    private TabPlacement( final int tabbedPaneConst ) {
    	this.tabbedPaneConst = tabbedPaneConst;
    }
	
    /**
     * Returns the tab placement specified by the tabbed pane constant.
     * @param tabbedPaneConst tabbed pane constant to return the tab placement for
     * @return the tab placement specified by the tabbed pane constant; or <code>null</code> if no such tab placement exists
     */
    public static TabPlacement getFromTabbedPaneConst( final int tabbedPaneConst ) {
    	for ( final TabPlacement tabPlacement : values() )
    		if ( tabPlacement.tabbedPaneConst == tabbedPaneConst )
    			return tabPlacement;
    	
    	return null;
    }
    
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
