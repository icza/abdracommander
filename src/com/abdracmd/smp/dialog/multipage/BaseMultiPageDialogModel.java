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

import java.util.List;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.iface.Provider;
import hu.belicza.andras.util.objectregistry.ClassNameProvider;

import com.abdracmd.smp.dialog.BaseDialogModel;

/**
 * This is the model used by the {@link BaseMultiPageDialogPresenter}.
 * 
 * @author Andras Belicza
 */
public class BaseMultiPageDialogModel extends BaseDialogModel {
	
	/**
	 * List of pages to be included.
	 * 
	 * <p><code>protected</code> because changing the page list requires other actions to take effect.
	 * See {@link BaseMultiPageDialogShell#setPageList(List)} for details.</p>
	 *
	 * @see BaseMultiPageDialogShell#setPageList(List)
	 */
	public List< ? extends IPage > pageList; 
	
	/** Optional key of of the page to show by default;
	 * if <code>null</code> the first page will be shown. */
	protected Object               defaultPageKey;
	/** Page key provider.                                */
	protected Provider< ?, IPage > pageKeyProvider;
	
	/**
	 * Creates a new BaseMultiPageDialogModel.
     * The active main frame will be used as the parent frame.
	 */
	public BaseMultiPageDialogModel() {
	}
	
	/**
	 * Creates a new BaseMultiPageDialogModel which will use a {@link ClassNameProvider} as the page key provider.
     * The active main frame will be used as the parent frame.
     * 
     * @param pageList             list of pages to be included
     * @param defaultPageClassName optional class name of the page to show by default; if <code>null</code> the first page will be shown
	 */
	public BaseMultiPageDialogModel( final List< ? extends IPage > pageList, final String defaultPageClassName ) {
    	this( pageList, new ClassNameProvider< IPage >(), defaultPageClassName );
	}
	
	/**
	 * Creates a new BaseMultiPageDialogModel.
     * The active main frame will be used as the parent frame.
     * 
     * @param pageList        list of pages to be included
     * @param pageKeyProvider page key provider
     * @param defaultPageKey  optional page key of the page to show by default; if <code>null</code> the first page will be shown
     * 
     * @param <K>             page key type
	 */
	public < K > BaseMultiPageDialogModel( final List< ? extends IPage > pageList, final Provider< K, ? extends IPage > pageKeyProvider, final K defaultPageKey ) {
		this.pageList        = pageList;
    	this.defaultPageKey  = defaultPageKey;
   		this.pageKeyProvider = Utils.cast( pageKeyProvider );
	}
	
}
