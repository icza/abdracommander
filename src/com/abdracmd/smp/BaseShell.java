/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.abdracmd.util.CommonAccessHelper;

/**
 * This is the base of all Shells (SMP architecture).
 * 
 * <p>Implements property change support.<br>
 * Property change support is implemented here so model does not need to be published.
 * This also allows listening to the presenter's properties.</p>
 * 
 * <hr>
 * 
 * <p><b>Shell-Model-Presenter architectural pattern</b></p>
 * 
 * <p>The Shell-Model-Presenter (SMP) is an architectural pattern used in software engineering, developed by Andr&aacute;s Belicza.<br>
 * <br>
 * The idea behind SMP is code reusability and separation of concerns.<br>
 * <br>
 * SMP is designed to build from components where the way of (other) code accessing and using
 * the component is separated from the way the user sees it and interacts with it (obviously these 2 "entities"
 * differ from each other in every aspect and ways), while -since we're talking about the same component- they can share data
 * they work with and provide to their clients.<br>
 * <br>
 * The <b>Shell</b> is the shell of an SMP component: provides an interface of the SMP component toward other codes,
 * and it is an entry point into the SMP component (and to its creation).<br> 
 * The <b>Model</b> stores configuration and state information similar to the model component of the MVC architecture.<br>
 * The <b>Presenter</b> is responsible to provide a view to the user, <i>and</i> interact with the user.<br>
 * <br>
 * The main differences compared to the MVC are that the Model only stores the data related to the function and purpose
 * provided/implemented by the SMP component, and additionally presenter configuration data.
 * The Presenter is responsible to store its own state and data which are related to the presentation (view).
 * The Presenter is also responsible to handle user (inter)actions - without the intervention of the Shell.
 * The Model is completely unaware of the Shell and Presenter.  
 * </p>
 * 
 * <p><b>The paradigms of the SMP pattern are the following:</b>
 * <ul>
 * 	<li>The Shell is the "entry point", the visible part toward the (code) clients.
 * 	<li>The Model does not know anything about the rest of the SMP component.
 * 	<li>The Shell knows about the Model and about the Presenter.
 * 	<li>The Presenter knows about the Model and about the Shell. 
 * </ul>
 * </p>
 * 
 * <div style='background:#90ff90;line-height:100%'><pre>
 *                           -------------                        --------------
 *         ------------------|   SHELL   |<---accesses and uses---| OTHER CODE |
 *         |                 -------------                        --------------
 *         |                     |   ^
 *   creates, uses       creates,|   |
 *         |               uses  |   |uses
 *         |                     |   |
 *         V                     V   |
 *   -------------           -------------                        --------------
 *   |   MODEL   |<---uses---| PRESENTER |<-----sees and uses-----|    USER    |
 *   -------------           -------------                        --------------
 * 
 * </pre></div>
 * 
 * <p><b>The initiation work flow of an SMP component is the following:</b>
 * <ol>
 * 	<li>The Shell is visible to the "world". The client instantiates the Shell.
 * 	<li>The constructor of the Shell instantiates the Model and stores its reference
 * 		either by passing it to the base shell's constructor
 * 		or assigning its reference to the {@link BaseShell#model}
 * 		(if the Model cannot be created before the <code>super()</code> call).
 * 	<li>Next in the constructor of the the Shell, the Shell instantiates the Presenter (passing the Shell's reference (<code>this</code>) as a constructor argument).
 * 	<li>The constructor of the Presenter stores the references of the Shell and the Model (accessed from the Shell).
 *  <li>When the constructor of the Shell returns, the SMP component is ready for action. 
 * </ol>
 * </p>
 * 
 * <p><b>Implementation tips and guidelines:</b>
 * <ul>
 * 	<li>Only the Shell part of an SMP component should be public.
 * 		The Shell also must be public in order for the SMP component to be reachable.<br>
 * 		Doing so will ensure that the clients will remain immune to the (future) changes in the internal working of the SMP component.<br>
 * 		Exception is when a part of the SMP component (or the whole SMP component) is designed to be
 * 		(or there is a chance it will be) reused (extended) by another SMP component.
 * 	<li>An SMP component should form a Java package, the 3 main parts of an SMP component (Shell-Model-Presenter)
 * 		should be put in one package so "internal" (regarding to the SMP component) methods and properties can be protected.
 * 	<li>It is possible to create a new SMP component deriving from an existing one and not subclassing (extending)
 * 		all the 3 main parts. A good example is when a sub-SMP component only wants to extend the shell's or the presenter's capabilities
 * 		but do not want to add new data to the Model. The same model class can be used in the new SMP component without modification (assumed it is published
 * 		for this reusing purpose).
 * </ul>
 * </p>
 * 
 * @param < Model     > model     type associated with this shell
 * @param < Presenter > presenter type associated with this shell
 * 
 * @author Andras Belicza
 */
public class BaseShell< Model extends BaseModel, Presenter extends BasePresenter< Model, ? extends BaseShell< Model, Presenter >, ? > >
	extends CommonAccessHelper {
	
	/** Property change support. */
	private PropertyChangeSupport propChangeSupport;
	
	
	/** Reference to the model.     */
	protected Model     model;
	/** Reference to the presenter. */
	protected Presenter presenter;
	
    /**
     * Creates a new BaseShell.
     */
    protected BaseShell() {
    }
    
    /**
     * Creates a new BaseShell.
     * @param model reference to the model
     */
    protected BaseShell( final Model model ) {
    	this.model = model;
    }
    
    /**
     * Returns the presenter.
     * @return the presenter
     */
    public Presenter getPresenter() {
    	return presenter;
    }
    
	/**
	 * Returns the lazily created property change support.
	 * @return the lazily created property change support
	 */
	private PropertyChangeSupport getPropChangeSupport() {
		if ( propChangeSupport == null )
			propChangeSupport = new PropertyChangeSupport( this );
		
		return propChangeSupport;
	}
	
	/**
	 * Adds a new {@link PropertyChangeListener}.
	 * @param listener listener to be added
	 */
	public synchronized void addPropertyChangeListener( final PropertyChangeListener listener ) {
		getPropChangeSupport().addPropertyChangeListener( listener );
	}
	
	/**
	 * Adds a new {@link PropertyChangeListener}.
	 * @param property property to bound to
	 * @param listener listener to be added
	 */
	public synchronized void addPropertyChangeListener( final String property, final PropertyChangeListener listener ) {
		getPropChangeSupport().addPropertyChangeListener( property, listener );
	}
	
	/**
	 * Removes a new {@link PropertyChangeListener}.
	 * @param listener listener to be removed
	 */
	public synchronized void removePropertyChangeListener( final PropertyChangeListener listener ) {
		getPropChangeSupport().removePropertyChangeListener( listener );
	}
	
	/**
	 * Removes a new {@link PropertyChangeListener}.
	 * @param property the listener was bound to
	 * @param listener listener to be removed
	 */
	public synchronized void removePropertyChangeListener( final String property, final PropertyChangeListener listener ) {
		getPropChangeSupport().removePropertyChangeListener( property, listener );
	}
	
	/**
	 * Fires a property change event (@link {@link PropertyChangeEvent}).
	 * @param property name of property that changed
	 * @param oldValue old value of the property
	 * @param newValue new value of the property
	 */
	public void firePropertyChange( final String property, final Object oldValue, final Object newValue ) {
		if ( propChangeSupport == null )
			return; // No listeners, who would we fire to?
		
		propChangeSupport.firePropertyChange( property, oldValue, newValue );
	}
    
}
