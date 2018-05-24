/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage.about.pageimpl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.abdracmd.Consts;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;

/**
 * Components about page.
 * 
 * @author Andras Belicza
 */
public class ComponentsPage extends BasePage {
	
    /**
     * Creates a new ComponentsPage.
     */
    public ComponentsPage() {
    	super( TextKey.ABOUT$PAGE$COMPONENTS$NAME, XIcon.F_CATEGORIES );
    }
    
	@Override
	public JComponent createPage() {
		final Map< String, Object > map = new LinkedHashMap<>(); // LinkedHashMap to preserve order
		
		map.put( get( TextKey.ABOUT$PAGE$COMPONENTS$APP_FULL_VERSION                ), Consts.APP_VERSION_STRING );
		map.put( get( TextKey.ABOUT$PAGE$COMPONENTS$APP_RELEASE_DATE                ), formatDate( Consts.APP_RELEASE_DATE ) );
		map.put( get( TextKey.ABOUT$PAGE$COMPONENTS$CURRENT_LANGUAGE                ), getLanguage().name );
		map.put( get( TextKey.ABOUT$PAGE$COMPONENTS$CURRENT_LANGUAGE_TRANSLATORS    ), AcUtils.getTranslatorListString( getLanguage() ) );
		map.put( get( TextKey.ABOUT$PAGE$COMPONENTS$CURRENT_LANGUAGE_TARGET_VERSION ), getLanguage().getTarget() );
		map.put( get( TextKey.ABOUT$PAGE$COMPONENTS$CURRENT_LANGUAGE_VERSION        ), getLanguage().getVersion() );
		map.put( get( TextKey.ABOUT$PAGE$COMPONENTS$UPDATER_VERSION                 ), "0.0" );
		
		return GuiUtils.createNameValueTable( map.entrySet(), false );
	}
	
}
