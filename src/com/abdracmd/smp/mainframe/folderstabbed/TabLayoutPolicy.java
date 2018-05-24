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
 * Tab layout policy.
 * 
 * @author Andras Belicza
 */
public enum TabLayoutPolicy {
	
	/** Tabs in multi-line. */
	MULTI_LINE( JTabbedPane.WRAP_TAB_LAYOUT   ),
	/** Scroll tabs.        */
	SCROLL    ( JTabbedPane.SCROLL_TAB_LAYOUT );
	
	/** Tabbed pane constant associated with this tab layout policy. */
	public final int tabbedPaneConst;
	
    /**
     * Creates a new TabLayoutPolicy.
     * @param tabbedPaneConst tabbed pane constant associated with this tab layout policy
     */
    private TabLayoutPolicy( final int tabbedPaneConst ) {
    	this.tabbedPaneConst = tabbedPaneConst;
    }
	
    /**
     * Returns the tab layout policy specified by the tabbed pane constant.
     * @param tabbedPaneConst tabbed pane constant to return the tab layout policy for
     * @return the tab layout policy specified by the tabbed pane constant; or <code>null</code> if no such tab layout policy exists
     */
    public static TabLayoutPolicy getFromTabbedPaneConst( final int tabbedPaneConst ) {
    	for ( final TabLayoutPolicy tabLayoutPolicy : values() )
    		if ( tabLayoutPolicy.tabbedPaneConst == tabbedPaneConst )
    			return tabLayoutPolicy;
    	
    	return null;
    }
    
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
