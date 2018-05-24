/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.action.url;

import java.awt.event.ActionEvent;

import com.abdracmd.action.BaseAction;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.util.AcUtils;

/**
 * Action which opens a URL.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class BaseUrlAction extends BaseAction {
	
	/** URL to be opened when action is performed. */
	protected String url;
	
	/**
	 * Creates a new BaseUrlAction.
	 * @param url     URL to be opened when action is performed
	 * @param textKey text key of the action's text
	 * @param params  parameters of the action's text
	 */
	public BaseUrlAction( final String url, final TextKey textKey, final Object...params ) {
		super( XIcon.F_APPLICATION_BROWSER, textKey, params );
		
		this.url = url;
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	AcUtils.showURLInBrowser( url );
    }
	
}
