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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


/**
 * Custom row sorter which supports changing the sort order without firing sort order changed event.
 * We need this because our header cell renderer method changes sort order and therefore it would result in constant redrawing...
 * 
 * @author Andras Belicza
 */
class SmartRowSorter extends TableRowSorter< DefaultTableModel > {
	
	/** Stored sort key list. */
	private List< SortKey > sortKeyList = Utils.getEmptyList();
	
	/**
	 * Creates a new SmartRowSorter.
	 * @param model reference to the table model
	 */
	public SmartRowSorter( final DefaultTableModel model ) {
		super( model );
	}
	
	@Override
	public void setSortKeys( final List< ? extends SortKey > sortKeys ) {
		setSortKeys( sortKeys, true );
	}
	
	/**
	 * Sets the sort keys.
	 * @param sortKeys                  sort key list to be set
	 * @param fireSortOrderChangedEvent tells if sort order changed event has to be fired
	 */
	public void setSortKeys( final List< ? extends SortKey > sortKeys, final boolean fireSortOrderChangedEvent ) {
		if ( sortKeys == null )
			sortKeyList = Utils.getEmptyList();
		else
			sortKeyList = new ArrayList<>( sortKeys );
		
		sortKeyList = Collections.unmodifiableList( sortKeyList );
		
		if ( fireSortOrderChangedEvent )
			fireSortOrderChanged();
		
		sort();
	}
	
	@Override
	public List< ? extends SortKey > getSortKeys() {
		return sortKeyList;
	}
	
}
