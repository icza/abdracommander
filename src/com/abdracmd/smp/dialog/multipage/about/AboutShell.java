/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage.about;

import hu.belicza.andras.util.objectregistry.ObjectRegistry;

import java.awt.Dimension;

import com.abdracmd.smp.dialog.multipage.BaseMultiPageDialogModel;
import com.abdracmd.smp.dialog.multipage.BaseMultiPageDialogShell;
import com.abdracmd.smp.dialog.multipage.IPage;
import com.abdracmd.smp.dialog.multipage.about.pageimpl.ComponentsPage;
import com.abdracmd.smp.dialog.multipage.about.pageimpl.GeneralPage;
import com.abdracmd.smp.dialog.multipage.about.pageimpl.LicensePage;
import com.abdracmd.smp.dialog.multipage.about.pageimpl.RegInfoPage;
import com.abdracmd.smp.dialog.multipage.about.pageimpl.TranslationsPage;
import com.abdracmd.smp.dialog.multipage.about.pageimpl.systeminfo.SystemInfoPage;
import com.abdracmd.smp.main.MainShell;

/**
 * This is the shell of the About dialog.
 * 
 * @author Andras Belicza
 */
public class AboutShell extends BaseMultiPageDialogShell< BaseMultiPageDialogModel, AboutPresenter > {
	
	/**
	 * Registers the built-in about pages.
	 */
	public static void registerBuiltinAboutPages() {
		final ObjectRegistry< String, IPage > aboutPageRegistry = MainShell.INSTANCE.getModel().getAboutPageRegistry();
		
		aboutPageRegistry.register( new GeneralPage         () );
		aboutPageRegistry.register( new LicensePage         () );
		aboutPageRegistry.register( new RegInfoPage         () );
		aboutPageRegistry.register( new ComponentsPage      () );
		aboutPageRegistry.register( new TranslationsPage    () );
		aboutPageRegistry.register( new SystemInfoPage      () );
	}
	
	
    /**
     * Creates a new AboutShell.
     */
    public AboutShell() {
    	this( null );
    }
	
    /**
     * Creates a new AboutShell.
     * @param defaultPageClassName class name of the about page to show by default
     */
    public AboutShell( final String defaultPageClassName ) {
    	super( new BaseMultiPageDialogModel( MainShell.INSTANCE.getModel().getAboutPageRegistry().valueList(), defaultPageClassName ) );
    	
    	presenter = new AboutPresenter( this );
    	
    	presenter.maximizeWindowWithMarginAndShow( 20, new Dimension( 1000, 900 ) );
    }
	
}
