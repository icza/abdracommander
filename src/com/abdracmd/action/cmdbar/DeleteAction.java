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
import com.abdracmd.fileop.impl.DeleteFileOp;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.fileop.FileOpShell;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.MainFrameShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderShell;

/**
 * Delete action.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class DeleteAction extends BaseAction {
	
	/** Singleton instance. */
	public static final DeleteAction INSTANCE = new DeleteAction();
	
	/**
	 * Creates a new DeleteAction.
	 * Private because this is a singleton action (shared between main frames).
	 */
	private DeleteAction() {
		super( KeyStroke.getKeyStroke( KeyEvent.VK_F8, 0 ), XIcon.DELETE, TextKey.ACTION$CMD_BAR$DELETE );
	}
	
    @Override
    public void actionPerformed( final ActionEvent event ) {
    	final MainFrameShell activeMainFrameShell = MainShell.INSTANCE.getActiveMainFrameShell();
    	final FolderShell    activeFolderShell    = activeMainFrameShell.getActiveFolderShell();
    	
    	final Path[] inputPaths = activeFolderShell.getPresenter().getInputPathsForFileOps();
    	if ( inputPaths == null )
    		return;
    	
    	new FileOpShell( DeleteFileOp.class.getName(), inputPaths,
    			activeFolderShell.getPath(),
    			null,
    			activeFolderShell );
    }
	
}
