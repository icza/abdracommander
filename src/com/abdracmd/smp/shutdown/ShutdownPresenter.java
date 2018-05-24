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

import com.abdracmd.smp.BaseModel;
import com.abdracmd.smp.BasePresenter;

/**
 * Shutdown process presenter.
 * 
 * @author Andras Belicza
 */
class ShutdownPresenter extends BasePresenter< BaseModel, ShutdownShell, Object > implements AutoCloseable {
	
	/**
	 * Creates and returns a new ShutdownPresenter.
	 * @return a new ShutdownPresenter or <code>null</code> if it is not supported
	 */
	protected static ShutdownPresenter create() {
		return null;
	}
	
	/**
	 * Creates a new ShutdownPresenter.
	 * @param shell reference to the shell
	 */
	private ShutdownPresenter( final ShutdownShell shell ) {
		super( shell );
	}
	
	/**
	 * Enters the specified shutdown phase.
	 * @param phase shutdown phase to be displayed
	 */
	protected void displayPhase( final ShutdownPhase phase ) {
	}
	
	/**
	 * Releases resources allocated for the shutdown presenter.
	 */
	@Override
	public void close() {
	}
	
}
