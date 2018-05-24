/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder.column;

import hu.belicza.andras.util.iface.HasDisplayName;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;

import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderPresenter;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base.BaseColumn;

/**
 * A column in the table of {@link FolderPresenter}.
 * 
 * <p>Instances of column implementations are shared and therefore must be thread safe!</p>
 * 
 * @param < T > type of objects of the column
 *  
 * @author Andras Belicza
 * 
 * @see BaseColumn
 */
public interface IColumn< T > extends HasDisplayName {
	
	/**
	 * Column type.
	 * @author Andras Belicza
	 */
	public enum Type {
		/** Internal data column which must be included in the table model but must be hidden from the user. */
		INTERNAL,
		/** Required column which cannot be removed but can be moved.                                        */
		REQUIRED,
		/** Normal column: can be moved and removed.                                                         */
		NORMAL;
	}
	
	/**
	 * Returns the description of the column.
	 * This will appear in the column setup popup menu and will be set also as the tool tip of the column header. 
	 * @return the description of the column
	 */
	String getDescription();
	
	/**
	 * Returns the class of objects of the column.
	 * @return the class of objects of the column
	 */
	Class< T > getColumnClass();
	
	/**
	 * Returns the column type.
	 * @see Type
	 * @return the column type.
	 */
	Type getType();
	
	/**
	 * Returns a comparator for the column.
	 * @return a comparator for the column
	 */
	Comparator< T > getComparator();
	
	/**
	 * Returns the cell data for the specified path.
	 * 
	 * @param path       path to return cell data for
	 * @param attrs      attributes of the path
	 * @param rowContext row context to share intermediate results between columns when processing a path 
	 * 
	 * @return the cell data for the specified path
	 * 
	 * @see IRowContext
	 */
	T getData( Path path, BasicFileAttributes attrs, IRowContext rowContext );
	
}
