/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.main;

import java.util.List;

import javax.swing.SwingUtilities;

import com.abdracmd.Consts;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.smp.BaseShell;
import com.abdracmd.smp.dialog.options.OptionsShell;
import com.abdracmd.smp.dialog.options.OptionsShell.BodyIcon;
import com.abdracmd.smp.dialog.options.OptionsShell.Option;
import com.abdracmd.smp.mainframe.MainFrameShell;
import com.abdracmd.smp.shutdown.ShutdownPhase;
import com.abdracmd.smp.shutdown.ShutdownShell;
import com.abdracmd.smp.startup.StartupShell;

/**
 * This is the main shell of Abdra Commander.
 * 
 * @author Andras Belicza
 */
public class MainShell extends BaseShell< MainModel, MainPresenter > {
	
	/** The singleton instance of the main shell. */
	public static final MainShell INSTANCE = new MainShell();
	
    /**
     * Creates a new MainShell.<br>
     * Private because this is a singleton class.
     */
    private MainShell() {
    	super( new MainModel() );
    	
		// BaseShell stores a reference to the main model (our model) which did not exist when assigned.
		// Set it for consistency.
    	mainModel = model;
    	
    	presenter = new MainPresenter( this );
    }
	
    /**
     * Returns the main model.
     * @return the main model
     */
    public MainModel getModel() {
    	return model;
    }
    
    /**
     * Launches the startup process.
     */
    public synchronized void startup() {
    	
    	// The startup process have to be executed in the EDT!
    	// Static initializator of Consts loads app author photo which deals with image observer, sync and such...
    	// Also a mainframe shell is launched => a main frame is created and made visible.
    	
    	SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				new StartupShell().initiate();
				
				// We need at least 1 main shell
		    	startNewMainFrameShell();
		    	
		    	// In dev mode run self diagnostics if "secret" property is set 
		    	if ( System.getProperty( "com.abdracmd.selftest" ) != null ) {
		    		debug( "Running self diagnostics." );
		    		try {
						final Class<?> diagUtilClass = Class.forName( "com.abdracmd.selftest.SelfDiagnostics" );
						diagUtilClass.getMethod( "runAllTests" ).invoke( null );
					} catch ( final ClassNotFoundException cnfe ) {
						debug( "Failed to start self diagnostics!", cnfe );
					} catch ( final Throwable e ) {
						// Do nothing, self diagnostics terminates on fail anyway
					}
				}
		    	
			}
    	} );
    }
    
    /**
     * Starts a new main frame shell.
     */
    public void startNewMainFrameShell() {
    	final List< MainFrameShell > mainFrameShellList = model.getMainFrameShellList();
    	
    	synchronized ( mainFrameShellList ) {
    		mainFrameShellList.add( new MainFrameShell() );
        	renameMainFramePresenters();
    	}
    }
    
    /**
     * Called when a main frame shell is about to close.
     * @return true if the close is allowed; false otherwise (e.g. user veto)
     */
    public boolean checkMainFrameShellClosing() {
    	// If shutdown has been initiated, exit has been checked.
    	// Do not check again before each main frame...
    	if ( model.getShutdownPhase() == ShutdownPhase.NOT_STARTED ) {
	    	if ( model.getMainFrameShellList().size() == 1 ) {
	    		// Closing the last main frame means exit:
	    		return checkExit();
	    	}
		}
		
    	return true;
    }
    
    /**
     * Removes a main frame shell.
     * @param mainFrameShell main frame shell to be removed
     */
    public void removeMainFrameShell( final MainFrameShell mainFrameShell ) {
    	final List< MainFrameShell > mainFrameShellList = model.getMainFrameShellList();
    	
    	synchronized ( mainFrameShellList ) {
    		mainFrameShellList.remove( mainFrameShell );
        	
        	if ( mainFrameShellList.isEmpty() ) {
        		// Only call exit on closing last frame if shutdown process is not in progress (which naturally closes all main frames)
        		if ( model.getShutdownPhase() == ShutdownPhase.NOT_STARTED )
        			exit( true ); // Check is performed before closing the last window
        	}
        	
        	renameMainFramePresenters();
    	}
    }
    
    /**
     * Renames the main frame presenters when the list of main frame changes (e.g. a new main frame is opened).
     */
    private void renameMainFramePresenters() {
    	final List< MainFrameShell > mainFrameShellList = model.getMainFrameShellList();
    	
    	synchronized ( mainFrameShellList ) {
        	final int mainFrameShellsCount = mainFrameShellList.size();
        	
        	int i = 0;
        	for ( final MainFrameShell mainFrameShell : mainFrameShellList )
        		mainFrameShell.setFrameSequenceNumber( mainFrameShellsCount == 1 ? null : ++i );
    	}
    }
    
    /**
     * Shuts down all main frame shells.
     * To be called upon shutdown process.
     */
    public void shutdownAllMainFrameShells() {
    	while ( !model.getMainFrameShellList().isEmpty() )
    		model.getMainFrameShellList().get( 0 ).shutdown();
    }
    
    /**
     * Returns the active main frame shell.
     * @return the active main frame shell or <code>null</code> if none of the main frames are active
     */
    public MainFrameShell getActiveMainFrameShell() {
    	// FIXME: if a dialog is active, all main frame shells will say they're not active.
    	// In this case return the parent main frame of such active dialog!
    	
    	final List< MainFrameShell > mainFrameShellList = model.getMainFrameShellList();
    	
    	synchronized ( mainFrameShellList ) {
        	for ( final MainFrameShell mainFrameShell : mainFrameShellList )
        		if ( mainFrameShell.getPresenter().getComponent().isActive() )
        			return mainFrameShell;
    	}
    	
    	return null;
    }
    
    /**
     * Called when about to exit.
     * @return true if operation is allowed; false otherwise (e.g. user veto)
     */
    public synchronized boolean checkExit() {
    	if ( get( SettingKeys.APP$CONFIRM_EXIT ) ) {
    		// show confirmation dialog
    		if ( Option.OK != OptionsShell.withOptionList( null, XIcon.EXIT, TextKey.MAIN_FRAME$EXIT, BodyIcon.QUESTION,
    				"Are you sure you want to exit?", OptionsShell.OPTIONS_OK_CANCEL, Consts.APP_NAME ).getOption() )
    			return false;
    	}
    	return true;
    }
    
    /**
     * Launches the shutdown process.
     * @param skipCheck tells if exit condition is to be skipped (because it was called right before for example)
     */
    public synchronized void exit( final boolean skipCheck ) {
		// Double check (Exit action might be called while we're already in a shutdown process...)
		if ( model.getShutdownPhase() != ShutdownPhase.NOT_STARTED )
			return;
		
    	if ( !skipCheck )
    		if ( !checkExit() )
    			return;
    	
    	new ShutdownShell().initiate();
    }
    
}
