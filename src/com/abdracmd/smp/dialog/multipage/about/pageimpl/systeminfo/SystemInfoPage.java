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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.util.GuiUtils;

/**
 * System information about page.
 * 
 * @author Andras Belicza
 */
public class SystemInfoPage extends BasePage {
	
    /**
     * Creates a new SystemInfoPage.
     */
    public SystemInfoPage() {
    	super( TextKey.ABOUT$PAGE$SYSINFO$NAME, XIcon.F_COMPUTER );
    	
    	addChildPage( new OperatingSystemPage  () );
    	addChildPage( new NetworkInterfacesPage() );
    	addChildPage( new MemoryPage           () );
    	addChildPage( new JavaPage             () );
    	addChildPage( new EnvironmentVarsPage  () );
    }
    
	@Override
	public JComponent createPage() {
		final Map< String, Object > map = new LinkedHashMap<>(); // LinkedHashMap to preserve order
		
		// TODO physical memory (RAM)
		// TODO host name
		
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$OP_SYS             ), System.getProperty( "os.name"       ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVERSION ), System.getProperty( "java.version"  ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$USER_NAME          ), System.getProperty( "user.name"     ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$USER_TIME_ZONE     ), System.getProperty( "user.timezone" ) );
		
		return GuiUtils.createNameValueTable( map.entrySet(), false );
    }
    
}
