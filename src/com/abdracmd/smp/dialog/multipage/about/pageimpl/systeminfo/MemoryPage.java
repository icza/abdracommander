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
import java.lang.management.MemoryMXBean;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;
import com.abdracmd.util.SizeFormat;

/**
 * Memory about page.
 * 
 * @author Andras Belicza
 */
public class MemoryPage extends BasePage {
	
    /**
     * Creates a new MemoryPage.
     */
    public MemoryPage() {
    	super( TextKey.ABOUT$PAGE$SYSINFO$PAGE$MEMORY$NAME, XIcon.F_MEMORY );
    }
    
	@Override
	public JComponent createPage() {
		final Map< String, Object > map = new LinkedHashMap<>(); // LinkedHashMap to preserve order
		
		// TODO physical memory (RAM)
		
		final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$MEMORY$MAX_HEAP          ), AcUtils.getFormattedSize( SizeFormat.AUTO, memoryMXBean.getHeapMemoryUsage   ().getMax      (), 1 ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$MEMORY$COMMITTED_HEAP    ), AcUtils.getFormattedSize( SizeFormat.AUTO, memoryMXBean.getHeapMemoryUsage   ().getCommitted(), 1 ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$MEMORY$USED_HEAP         ), AcUtils.getFormattedSize( SizeFormat.AUTO, memoryMXBean.getHeapMemoryUsage   ().getUsed     (), 1 ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$MEMORY$MAX_NONHEAP       ), AcUtils.getFormattedSize( SizeFormat.AUTO, memoryMXBean.getNonHeapMemoryUsage().getMax      (), 1 ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$MEMORY$COMMITTED_NONHEAP ), AcUtils.getFormattedSize( SizeFormat.AUTO, memoryMXBean.getNonHeapMemoryUsage().getCommitted(), 1 ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$MEMORY$USED_NONHEAP      ), AcUtils.getFormattedSize( SizeFormat.AUTO, memoryMXBean.getNonHeapMemoryUsage().getUsed     (), 1 ) );
		
		return GuiUtils.createNameValueTable( map.entrySet(), false );
	}
	
}
