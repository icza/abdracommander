/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.uicomponent.smarttable;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.bean.Email;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.abdracmd.action.NewEmailAction;
import com.abdracmd.util.AcUtils;

/**
 * A smart table with advanced rendering capabilities and built-in user event handling for links.
 * 
 * @author Andras Belicza
 */
public class SmartTable extends JTable {
	
	/** Default serial version ID. */
	private static final long serialVersionUID = 1L;
	
	/** Our table cell renderer.*/
	private final TableCellRenderer tableCellRenderer = new SmartTableCellRenderer();
	
	/**
	 * Creates a new SmartTable.
	 */
	public SmartTable() {
    	getTableHeader().setDefaultRenderer( new SmartHeaderCellRenderer( this ) );
    	
    	setRowSorter( new SmartRowSorter( (DefaultTableModel) getModel() ) );
    	
    	// To handle links and hand cursor over links
    	final MouseAdapter mouseAdapter = new MouseAdapter() {
    		@Override
    		public void mouseMoved( final MouseEvent event ) {
    			final Object value = getValueAtEvent( event );
    			if ( value == null ) {
    				setCursor( null );
    				return;
    			}
    			
    			if ( value instanceof String && Utils.isUrlText( (String) value )
    					|| value instanceof Email )
    				setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
    			else
    				setCursor( null );
    		}
    		@Override
    		public void mouseClicked( final MouseEvent event ) {
    			final Object value = getValueAtEvent( event );
    			if ( value == null )
    				return;
    			
    			if ( value instanceof String && Utils.isUrlText( (String) value ) )
    				AcUtils.showURLInBrowser( (String) value );
    			else if ( value instanceof Email )
					NewEmailAction.INSTANCE.newMail( value.toString(), null, null );
    		}
    		
    		/**
    		 * Returns the value at the cell under the specified event.
    		 * @param event event to return the value of cell being under this
    		 * @return the value at the cell under the specified event
    		 */
    		private Object getValueAtEvent( final MouseEvent event ) {
    			final int row    = rowAtPoint   ( event.getPoint() );
    			final int column = columnAtPoint( event.getPoint() );
    			if ( row == -1 || column == -1 )
    				return null;
    			
    			// Row and Column are VIEW INDEXES, JTable.getValueAt() requires view indices, so we're good
    			return getValueAt( row, column );
    		}
		};
		
    	addMouseListener      ( mouseAdapter );
    	addMouseMotionListener( mouseAdapter );
	}
	
	@Override
	public boolean isCellEditable( final int row, final int column ) {
		return false;
	}
	
	@Override
	public TableCellRenderer getCellRenderer( final int row, final int column ) {
		return tableCellRenderer;
	}
	
}
