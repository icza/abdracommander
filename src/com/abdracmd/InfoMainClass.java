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

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * An info main class registered as the main class of the jar to display proper usage.
 * 
 * @author Andras Belicza
 */
public class InfoMainClass {
	
	/**
	 * Entry point of the program.
	 * 
	 * <p>Displays an info message about how to start the application.</p>
	 * 
	 * @param arguments used to take arguments from the running environment - not used here
	 */
	public static void main( final String[] arguments ) {
		// Set Look and Feel: from the EDT!
		try {
			SwingUtilities.invokeAndWait( new Runnable() {
				@Override
				public void run() {
					for ( final LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels() )
						if ( "Nimbus".equals( lookAndFeelInfo.getName() ) ) {
							try {
								UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
							} catch ( final Exception e ) {
							}
							break;
						}
				}
			} );
		} catch ( final Exception e ) {
		}
		
		JOptionPane.showMessageDialog( null, "Please run the proper script to start " + Consts.APP_NAME + ".",
			Consts.APP_NAME, JOptionPane.INFORMATION_MESSAGE );
	}
	
}
