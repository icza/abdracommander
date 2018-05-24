/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage.settings;

import java.awt.Dimension;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.iface.Provider;

import com.abdracmd.service.settings.SettingText;
import com.abdracmd.smp.dialog.multipage.BaseMultiPageDialogShell;
import com.abdracmd.smp.dialog.multipage.BaseMultiPageDialogModel;
import com.abdracmd.smp.dialog.multipage.settings.pageimpl.SettingsNodePage;

/**
 * This is the shell of the Settings dialog.
 * 
 * @author Andras Belicza
 */
public class SettingsShell extends BaseMultiPageDialogShell< BaseMultiPageDialogModel, SettingsPresenter > {
	
    /**
     * Creates a new SettingsShell.
     */
    public SettingsShell() {
    	this( null );
    }
	
    /**
     * Creates a new SettingsShell.
     * @param defaultPageNode page node to show by default
     */
    public SettingsShell( final SettingText defaultPageNode ) {
    	super( new BaseMultiPageDialogModel( Utils.< SettingsNodePage >getEmptyList(), new Provider< SettingText, SettingsNodePage >() {
			@Override
			public SettingText provide( final SettingsNodePage settingsNodePage ) {
				return settingsNodePage.settingText;
			}
    	}, defaultPageNode ) );
    	
    	presenter = new SettingsPresenter( this );
    	
    	// Settings presenter uses a dynamic page list which first is built in its constructor,
    	// it properly calls our setPageList() method which properly rebuilds the tree at the presenter,
    	// but our presenter reference is only set when the constructor returns.
    	// So we manually have to call this again.
    	presenter.rebuildPageTree();
    	
    	presenter.maximizeWindowWithMarginAndShow( 20, new Dimension( 1000, 900 ) );
    }
	
}
