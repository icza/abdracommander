/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.base;

import hu.belicza.andras.util.bean.BaseHasDisplayName;

import java.util.Comparator;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IColumn;

/**
 * Base column with some help to implement columns.
 * 
 * @param < T > type of objects of the column
 * 
 * @author Andras Belicza
 */
public abstract class BaseColumn< T > extends BaseHasDisplayName implements IColumn< T > {
	
	/** Description of the column.          */
	protected final String          description;
	/** Class of objects of the column.     */
	protected final Class< T >      columnClass;
	/** Optional comparator for the column. */
	protected final Comparator< T > comparator;
	/** Column type.                        */
	protected final Type            type;
	
    /**
     * Creates a new BaseColumn.
     * @param displayNameTextKey text key of the display name of the column
     * @param descriptionTextKey text key of the description of the column
     * @param columnClass        class of the column
     * @param type               type of the column
     */
    public BaseColumn( final TextKey displayNameTextKey, final TextKey descriptionTextKey, final Class< T > columnClass, final Type type ) {
    	this( displayNameTextKey, descriptionTextKey, columnClass, type, null );
    }
	
    /**
     * Creates a new BaseColumn.
     * @param displayNameTextKey text key of the display name of the column
     * @param descriptionTextKey text key of the description of the column
     * @param columnClass        class of the column
     * @param type               type of the column
     * @param comparator         optional comparator for the column
     */
    public BaseColumn( final TextKey displayNameTextKey, final TextKey descriptionTextKey, final Class< T > columnClass, final Type type, final Comparator< T > comparator ) {
    	this( MainShell.INSTANCE.getModel().getLanguage().get( displayNameTextKey ), MainShell.INSTANCE.getModel().getLanguage().get( descriptionTextKey ), columnClass, type, comparator );
    }
	
    /**
     * Creates a new BaseColumn.
     * @param displayName display name of the column
     * @param description description of the column
     * @param columnClass class of the column
     * @param type        type of the column
     */
    public BaseColumn( final String displayName, final String description, final Class< T > columnClass, final Type type ) {
    	this( displayName, description, columnClass, type, null );
    }
	
    /**
     * Creates a new BaseColumn.
     * @param displayName display name of the column
     * @param description description of the column
     * @param columnClass class of the column
     * @param type        type of the column
     * @param comparator  optional comparator for the column
     */
    public BaseColumn( final String displayName, final String description, final Class< T > columnClass, final Type type, final Comparator< T > comparator ) {
    	super( displayName );
    	this.description = description;
    	this.columnClass = columnClass;
    	this.type        = type;
    	this.comparator  = comparator;
    }
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public Class< T > getColumnClass() {
		return columnClass;
	}
	
	@Override
	public Type getType() {
		return type;
	}
	
	@Override
	public Comparator< T > getComparator() {
		return comparator;
	}
	
}
