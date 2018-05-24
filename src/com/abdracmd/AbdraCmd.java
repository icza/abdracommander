/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd;

import java.util.Locale;

import com.abdracmd.smp.main.MainShell;

/**
 * This is the main class of Abdra Commander.
 * 
 * <p>Application starts in development mode if the <code>"dev-mode"</code> variable is present (see {@link Consts#DEV_MODE} for details).
 * You can achieve this by passing the <code>-Ddev-mode</code> VM argument to the <code>java.exe</code> or <code>javaw.exe</code> process.</p>
 * 
 * @author Andras Belicza
 */
public class AbdraCmd {
	
	/**
	 * This is the entry point of Abdra Commander.
	 * 
	 * @param arguments arguments passed on to us
	 */
	public static void main( final String[] arguments ) {
		Locale.setDefault( Locale.US );
		
		// Kick off the startup process...
		MainShell.INSTANCE.startup();
	}
	
}
