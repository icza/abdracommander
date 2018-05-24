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

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.util.GuiUtils;

/**
 * Java about page.
 * 
 * @author Andras Belicza
 */
public class JavaPage extends BasePage {
	
    /**
     * Creates a new JavaPage.
     */
    public JavaPage() {
    	super( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$NAME, XIcon.MISC_JAVA );
    	
    	addChildPage( new SystemPropertiesPage() );
    }
    
	@Override
	public JComponent createPage() {
		final Map< String, Object > map = new LinkedHashMap<>(); // LinkedHashMap to preserve order
		
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVENDOR       ), System.getProperty( "java.vendor"                ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVERSION      ), System.getProperty( "java.version"               ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JHOME         ), System.getProperty( "java.home"                  ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JRT_NAME      ), System.getProperty( "java.runtime.name"          ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JRT_VERSION   ), System.getProperty( "java.runtime.version"       ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JSPEC_VENDOR  ), System.getProperty( "java.specification.vendor"  ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JSPEC_NAME    ), System.getProperty( "java.specification.name"    ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JSPEC_VERSION ), System.getProperty( "java.specification.version" ) );
		
		final RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMSPEC_VENDOR  ), bean.getSpecVendor () );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMSPEC_NAME    ), bean.getSpecName   () );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMSPEC_VERSION ), bean.getSpecVersion() );
		
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMIMPL_VENDOR  ), bean.getVmVendor () );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMIMPL_NAME    ), bean.getVmName   () );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMIMPL_VERSION ), bean.getVmVersion() );
		
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVM_INPUT_ARGS  ), bean.getInputArguments() );
		
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$BOOT_CLASS_PATH ), bean.getBootClassPath() );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$LIBRARY_PATH    ), bean.getLibraryPath  () );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$CLASS_PATH      ), bean.getClassPath    () );
		
		
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JSTART_TIME ), new Date( bean.getStartTime() ) );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$JUPTIME     ), formatNumber( bean.getUptime() / 1000 ) + " sec" );
		
		final ClassLoadingMXBean clBean = ManagementFactory.getClassLoadingMXBean();
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$LOADED_CLASS_COUNT       ), clBean.getLoadedClassCount     () );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$TOTAL_LOADED_CLASS_COUNT ), clBean.getTotalLoadedClassCount() );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$JAVA$UNLOADED_CLASS_COUNT     ), clBean.getUnloadedClassCount   () );
		
		return GuiUtils.createNameValueTable( map.entrySet(), false );
	}
	
}
