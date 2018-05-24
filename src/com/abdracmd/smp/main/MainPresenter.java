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

import com.abdracmd.smp.BasePresenter;

/**
 * This is the main presenter (SMP architecture) of Abdra Commander.
 * 
 * <p><i>This class is only a placeholder and is not (yet) used,
 * because there is no such thing as main presenter in Abdra Commander but instead main frame presenters.</i></p>
 * 
 * @author Andras Belicza
 */
class MainPresenter extends BasePresenter< MainModel, MainShell, Object > {
	
    /**
     * Creates a new MainPresenter.
     * @param shell reference to the shell
     */
	protected MainPresenter( final MainShell shell ) {
		super( shell );
    }
	
}
