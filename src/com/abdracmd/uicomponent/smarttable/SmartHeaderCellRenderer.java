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

import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.util.AcUtils;

/**
 * A custom header cell renderer which uses our own icons to indicate sorting.<br>
 * Also displays the column name as tool tip.
 * 
 * @author Andras Belicza
 */
class SmartHeaderCellRenderer extends JLabel implements TableCellRenderer {
	
	/** Default serial version ID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * We want headers to look almost the same, but we use our own sort-indicator icons, even for multiple sorting columns.
	 * So I use the default renderer as a delegator which we'll polish a little.
	 */
	private DefaultTableCellRenderer delegateHeaderRenderer;
	
	/** Empty sort key list to nullify sorting while rendering. */
	private final List< SortKey > EMPTY_SORT_KEY_LIST = Utils.getEmptyList();
	
	/**
	 * Creates a new SmartHeaderCellRenderer.
	 * @param table reference to the table
	 */
	public SmartHeaderCellRenderer( final JTable table ) {
		delegateHeaderRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
	}
	
	@Override
	public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, int column ) {
		final List< ? extends SortKey > sortKeys = table.getRowSorter().getSortKeys();
		
		if ( table.getRowSorter() instanceof SmartRowSorter ) {
			final SmartRowSorter smartRowSorter = (SmartRowSorter) table.getRowSorter();
			
			// Nullify sorting, we indicate sorting in our own way!
			smartRowSorter.setSortKeys( EMPTY_SORT_KEY_LIST, false );
			
			delegateHeaderRenderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
			
			// Restore sorting now
			smartRowSorter.setSortKeys( sortKeys, false );
		}
		else
			delegateHeaderRenderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
		
		// Column is view idx, convert it to model idx
		column = table.convertColumnIndexToModel( column );
		
		// Sorting icons (primary and secondary)
		if ( sortKeys.size() > 0 && sortKeys.get( 0 ).getColumn() == column )
			delegateHeaderRenderer.setIcon( AcUtils.CAH.get( sortKeys.get( 0 ).getSortOrder() == SortOrder.ASCENDING ? XIcon.F_ARROW_090_MEDIUM : XIcon.F_ARROW_270_MEDIUM ) );
		else if ( sortKeys.size() > 1 && sortKeys.get( 1 ).getColumn() == column )
			delegateHeaderRenderer.setIcon( AcUtils.CAH.get( sortKeys.get( 1 ).getSortOrder() == SortOrder.ASCENDING ? XIcon.F_ARROW_090_SMALL  : XIcon.F_ARROW_270_SMALL  ) );
		else
			delegateHeaderRenderer.setIcon( null );
		
		delegateHeaderRenderer.setToolTipText( delegateHeaderRenderer.getText() );
		
		return delegateHeaderRenderer;
	}
	
	@Override
	public void updateUI() {
		// Get another instance of delegateHeaderRenderer, which will be initialized with the new UI:
		// Simply delegating the call like SwingUtilities.updateComponentTreeUI( delegateHeaderRenderer ) is not enough!
		delegateHeaderRenderer = (DefaultTableCellRenderer) new JTable().getTableHeader().getDefaultRenderer();
	}
	
}
