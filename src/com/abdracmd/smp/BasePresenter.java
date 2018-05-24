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

import com.abdracmd.util.CommonAccessHelper;

/**
 * This is the base of all presenters (SMP architecture).
 * 
 * <p>Details of the SMP architecture can be found in the documentation of the {@link BaseShell} class.</p>
 * 
 * @param < Model     > model     type associated with this presenter
 * @param < Shell     > shell     type associated with this presenter
 * @param < Component > component type of visual representation used by this presenter
 * 
 * @author Andras Belicza
 * 
 * @see BaseShell
 */
public class BasePresenter< Model extends BaseModel, Shell extends BaseShell< Model, ? extends BasePresenter< Model, Shell, Component > >, Component > extends CommonAccessHelper {
	
	/** Reference to the model. */
	protected Model model;
	/** Reference to the shell. */
	protected Shell shell;
	
	/** Component for visual representation of this presenter. */
	protected Component component;
	
    /**
     * Creates a new BasePresenter.
     * @param shell reference to the shell
     */
    protected BasePresenter( final Shell shell ) {
    	this( shell, null );
    }
    
    /**
     * Creates a new BasePresenter.
     * @param shell     reference to the shell
     * @param component reference to the component
     */
    protected BasePresenter( final Shell shell, final Component component ) {
    	this.shell     = shell;
    	this.model     = shell.model;
    	this.component = component;
    }
    
    /**
     * Returns the component for visual representation of this presenter.
     * This can be added to containers for example if it extends {@link javax.swing.JComponent}.
     * @return the component for visual representation used by this presenter
     */
    public Component getComponent() {
    	return component;
    }
    
}
