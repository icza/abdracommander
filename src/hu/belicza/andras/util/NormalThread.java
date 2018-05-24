/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util;

/**
 * A normal thread which does not take the inherited priority but instead sets {@link Thread#NORM_PRIORITY}.
 * 
 * @author Andras Belicza
 */
public class NormalThread extends Thread {
	
	/**
	 * Creates a new NormalThread.
	 * @param name name of the thread
	 */
	public NormalThread( final String name ) {
		this( null, name );
	}
	
	/**
	 * Creates a new NormalThread.
	 * @param target object whose <code>run()</code> method to call when this thread is started
	 * @param name   name of the thread
	 */
	public NormalThread( final Runnable target, final String name ) {
		super( target, name );
		
		setPriority( NORM_PRIORITY );
	}
	
}
