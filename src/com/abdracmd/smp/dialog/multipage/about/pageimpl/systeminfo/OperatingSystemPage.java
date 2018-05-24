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

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.abdracmd.Consts;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.util.GuiUtils;

/**
 * Operating System about page.
 * 
 * @author Andras Belicza
 */
public class OperatingSystemPage extends BasePage {
	
    /**
     * Creates a new OperatingSystemPage.
     */
    public OperatingSystemPage() {
    	super( TextKey.ABOUT$PAGE$SYSINFO$PAGE$OS$NAME, Consts.OS.xicon );
    }
    
	@Override
	public JComponent createPage() {
		final Map< String, Object > map = new LinkedHashMap<>(); // LinkedHashMap to preserve order
		
		final OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$OS$OS_NAME    ), bean.getName   () );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$OS$OS_VERSION ), bean.getVersion() );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$OS$OS_ARCH    ), bean.getArch   () );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$OS$AVAIL_PROC ), bean.getAvailableProcessors() );
		
		// TODO physical memory (RAM)
		
		final double systemLoadAverage = bean.getSystemLoadAverage();
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$OS$AVG_SYS_LOAD ), systemLoadAverage < 0 ? null : formatNumber( systemLoadAverage, 2 ) );
		
		return GuiUtils.createNameValueTable( map.entrySet(), false );
	}
	
}
