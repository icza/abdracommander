/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.action.cmdbar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Path;

import javax.swing.KeyStroke;

import com.abdracmd.action.BaseAction;
import com.abdracmd.fileop.impl.CopyFileOp;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.fileop.FileOpShell;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.MainFrameShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderShell;

/**
 * Copy action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class CopyAction extends BaseAction {
	
	/** Singleton instance. */
	public static final CopyAction INSTANCE = new CopyAction();
	
	/**
	 * Creates a new CopyAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private CopyAction() {
		super( KeyStroke.getKeyStroke( KeyEvent.VK_F5, 0 ), XIcon.COPY, TextKey.ACTION$CMD_BAR$COPY );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	final MainFrameShell activeMainFrameShell = MainShell.INSTANCE.getActiveMainFrameShell();
    	final FolderShell    activeFolderShell    = activeMainFrameShell.getActiveFolderShell();
    	
    	final Path[] inputPaths = activeFolderShell.getPresenter().getInputPathsForFileOps();
    	if ( inputPaths == null )
    		return;
    	
    	new FileOpShell( CopyFileOp.class.getName(), inputPaths,
    			activeFolderShell.getPath(),
    			activeMainFrameShell.getNonActiveFolderShell().getPath(),
    			activeFolderShell );
    }
	
}
