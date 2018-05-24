/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage.about;

import com.abdracmd.Consts;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BaseMultiPageDialogModel;
import com.abdracmd.smp.dialog.multipage.BaseMultiPageDialogPresenter;

/**
 * This is the About presenter.
 * 
 * @author Andras Belicza
 */
class AboutPresenter extends BaseMultiPageDialogPresenter< BaseMultiPageDialogModel, AboutShell > {
	
    /**
     * Creates a new AboutPresenter.
     * @param shell reference to the shell
     */
    protected AboutPresenter( final AboutShell shell ) {
    	super( shell );
    	
    	setTitle( TextKey.ABOUT$TITLE, Consts.APP_NAME );
    	setIcon( XIcon.MY_APP_ICON_BIG );
    	
    	component.getRootPane().setDefaultButton( addCloseButton() );
    }
    
}
