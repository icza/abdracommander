/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.uicomponent;

import java.awt.Color;

/**
 * Matte border whose color and insets can be changed after creation.
 * 
 * @author Andras Belicza
 */
public class MatteBorder extends javax.swing.border.MatteBorder {
	
	/** Default serial version ID */
	private static final long serialVersionUID = 1L;
	
	/**
     * Creates a new MatteBorder.
     */
    public MatteBorder() {
        this( 0, 0, 0, 0, null );
    }
	
	/**
     * Creates a new MatteBorder.
     * @param top    the top inset of the border
     * @param left   the left inset of the border
     * @param bottom the bottom inset of the border
     * @param right  the right inset of the border
     * @param color  the color of the border
     */
    public MatteBorder( final int top, final int left, final int bottom, final int right, final Color color ) {
        super( top, left, bottom, right, color );
    }
	
    /**
     * Sets the color of the border.
     * @param color color of the border to be set
     */
    public void setColor( final Color color ) {
    	this.color = color;
    }
    
    /**
     * Sets the insets of the border.
     * @param top    the top inset of the border
     * @param left   the left inset of the border
     * @param bottom the bottom inset of the border
     * @param right  the right inset of the border
     */
    public void setInsets( final int top, final int left, final int bottom, final int right ) {
    	this.top    = top;
    	this.left   = left;
    	this.bottom = bottom;
    	this.right  = right;
    }
    
}
