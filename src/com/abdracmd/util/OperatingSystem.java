/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.util;

import com.abdracmd.service.icon.XIcon;

/**
 * Operating systems.
 * 
 * @author Andras Belicza
 */
public enum OperatingSystem {
	
	/** Windows.  */
	WINDOWS ( XIcon.F_WINDOWS    ),
	/** Unix.     */
	UNIX    ( XIcon.MISC_LINUX   ),
	/** MAC OS-X. */
	MAC_OS_X( XIcon.F_MAC_OS     ),
	/** Solaris.  */
	SOLARIS ( XIcon.MISC_SOLARIS ),
	/** Other.    */
	OTHER   ( XIcon.MY_EMPTY     );
	
	/** XIcon associated with this OS. */
	public final XIcon xicon;
	
	/**
	 * Creates a new OperatingSystem.
	 * @param xicon xicon associated with this OS
	 */
	private OperatingSystem( final XIcon xicon ) {
		this.xicon = xicon;
	}
	
	/**
	 * Detects and returns the running operating system.
	 * @return the running operating system
	 */
	public static OperatingSystem detect() {
		final String osName = System.getProperty( "os.name" ).toLowerCase();
		
		if ( osName.indexOf( "win" ) >= 0 )
			return OperatingSystem.WINDOWS;
		else if ( osName.indexOf( "mac" ) >= 0 )
			return OperatingSystem.MAC_OS_X;
		else if ( osName.indexOf( "nix" ) >= 0 || osName.indexOf( "nux" ) >= 0 )
			return OperatingSystem.UNIX;
		else if ( osName.indexOf( "sunos" ) >= 0 )
			return OperatingSystem.SOLARIS;
		
		return OperatingSystem.OTHER;
	}
	
}
