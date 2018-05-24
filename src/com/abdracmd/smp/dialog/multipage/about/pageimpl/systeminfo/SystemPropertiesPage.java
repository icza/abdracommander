/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage.about.pageimpl.systeminfo;

import javax.swing.JComponent;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.util.GuiUtils;

/**
 * System properties about page.
 * 
 * @author Andras Belicza
 */
public class SystemPropertiesPage extends BasePage {
	
    /**
     * Creates a new SystemPropertiesPage.
     */
    public SystemPropertiesPage() {
    	super( TextKey.ABOUT$PAGE$SYSINFO$PAGE$SYS_PROPS$NAME, XIcon.F_SCRIPT_TEXT );
    }
    
	@Override
	public JComponent createPage() {
		return GuiUtils.createNameValueTable( System.getProperties().entrySet() );
	}
	
}
