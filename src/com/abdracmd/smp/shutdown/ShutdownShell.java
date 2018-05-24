/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.shutdown;

import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.smp.BaseShell;
import com.abdracmd.smp.BaseModel;
import com.abdracmd.smp.main.MainShell;

/**
 * Shell of the shutdown process.
 * 
 * @author Andras Belicza
 */
public class ShutdownShell extends BaseShell< BaseModel, ShutdownPresenter > {
	
	/**
	 * Creates a new ShutdownShell.
	 */
	public ShutdownShell() {
	}
	
	/**
	 * Launches the shutdown process.
	 * @throws IllegalStateException if the current shutdown phase is not {@link ShutdownPhase#NOT_STARTED}
	 */
	public synchronized void initiate() {
		// TODO check background jobs, ask confirmation if needed
		
		if ( mainModel.getShutdownPhase() != ShutdownPhase.NOT_STARTED )
			throw new IllegalStateException( "Shutdown process can only be started once!" );
		
		try ( final ShutdownPresenter sp = presenter = ShutdownPresenter.create() ) {
			
			changeShutdownPhase( ShutdownPhase.STARTED );
			
			changeShutdownPhase( ShutdownPhase.CLOSING_ALL_WINDOWS );
			MainShell.INSTANCE.shutdownAllMainFrameShells();
			
			if ( get( SettingKeys.APP$SAVE_SETTINGS_ON_EXIT ) ) {
    			changeShutdownPhase( ShutdownPhase.SAVING_SETTINGS );
    			getSettings().saveSettings();
			}
			
			changeShutdownPhase( ShutdownPhase.FINISHED );
		}
		
		// We're good to go (away)...
		// Note:
		// Calling System.exit() will case "Exception while removing reference: java.lang.InterruptedException" sometimes.
		// Reason: the Disposer thread (a daemon thread) is not being shut down naturally, and might be blocked in a remove() call
		// (while removing platform native resources) when System.exit() is called.
		// Calling System.gc() and waiting a little seems to help
		System.gc();
		try {
			Thread.sleep( 100 );
		} catch ( final InterruptedException ie ) {
		}
		System.exit( 0 );
	}
	
	/**
	 * Changes the shutdown phase.
	 * @param newPhase new shutdown phase to change to
	 */
	private void changeShutdownPhase( final ShutdownPhase newPhase ) {
		debug( "Entering shutdown phase: " + newPhase );
		
		mainModel.enterShutdownPhase( newPhase );
		
		if ( presenter != null )
			presenter.displayPhase( newPhase );
	}
	
}
