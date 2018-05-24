/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.newfolder;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.ReadOnlyFileSystemException;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.smp.dialog.BaseDialogModel;
import com.abdracmd.smp.dialog.BaseDialogPresenter;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderShell;
import com.abdracmd.util.GuiUtils;

/**
 * This is the New folder presenter.
 * 
 * @author Andras Belicza
 */
class NewFolderPresenter extends BaseDialogPresenter< BaseDialogModel, NewFolderShell > {
	
	/** Text field to enter new folder name. */
	private final JTextField folderNameTextField;
	/** Label displaying error messages.     */
	private final JLabel     errorLabel;
	
    /**
     * Creates a new NewFolderPresenter.
     * @param shell reference to the shell
     */
    protected NewFolderPresenter( final NewFolderShell shell ) {
    	super( shell );
    	
    	setTitle( TextKey.NEW_FOLDER$TITLE );
    	setIcon( XIcon.NEW_FOLDER );
    	
    	final Box contentBox = Box.createVerticalBox();
    	
    	contentBox.add( new JLabel( get( TextKey.NEW_FOLDER$NEW_FOLDER_NAME ) ) );
    	
    	final FolderShell activeFolderShell    = MainShell.INSTANCE.getActiveMainFrameShell().getActiveFolderShell   ();
    	final FolderShell nonActiveFolderShell = MainShell.INSTANCE.getActiveMainFrameShell().getNonActiveFolderShell();
    	final Path parentPath = activeFolderShell.getPath();
    	
    	folderNameTextField = new JTextField( activeFolderShell.getPresenter().getFocusedFileName(), 30 );
    	folderNameTextField.selectAll();
    	contentBox.add( folderNameTextField );
    	
    	contentBox.add( Box.createVerticalStrut( 4 ) );
    	errorLabel = GuiUtils.changeFontToBold( new JLabel( Utils.SPACE_STRING ) ); // Set a space string to reserve space
    	errorLabel.setForeground( Color.RED );
    	contentBox.add( errorLabel );
    	
    	contentBox.add( Box.createVerticalStrut( 4 ) );
    	final JCheckBox createNonexistentCheckBox = GuiUtils.createSettingCheckBox( SettingKeys.NEW_FOLDER$CREATE_NONEXISTENT_PARENTS, null );
    	contentBox.add( createNonexistentCheckBox );
    	
    	contentBox.add( Box.createVerticalStrut( 2 ) );
    	
    	GuiUtils.alignBox( contentBox, SwingConstants.LEFT );
    	
    	final JPanel wrapper = new JPanel( new BorderLayout() );
    	wrapper.add( contentBox, BorderLayout.NORTH );
    	
    	component.getContentPane().add( wrapper, BorderLayout.CENTER );
    	
    	// Buttons
    	initButtonsPanel();
    	final JXButton okButton = new JXButton();
    	GuiUtils.initButton( okButton, null, TextKey.GENERAL$BTN$OK );
    	okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				try {
					
					final Path pathToCreate = parentPath.resolve( folderNameTextField.getText() );
					// FileAlreadyExistsException is not thrown in all cases (for example createDirectories() does not throw it), do it ourselves
					if ( Files.exists( pathToCreate ) )
						throw new FileAlreadyExistsException( Utils.getPathString( pathToCreate ) );
					
					if ( createNonexistentCheckBox.isSelected() )
						Files.createDirectories( pathToCreate );
					else
						Files.createDirectory( pathToCreate );
					
					// Refresh and focus the folder that was just created
					activeFolderShell.getPresenter().refresh( pathToCreate );
					// Also refresh the other table even if not the same path is listed (multiple folders may be created here at once)
					nonActiveFolderShell.getPresenter().refresh();
					component.dispose();
					
				} catch ( final ReadOnlyFileSystemException rofse ) {
					debug( "Could not create folder, read only file system!", rofse );
					setError( TextKey.NEW_FOLDER$ERR$READ_ONLY_FS );
				} catch ( final InvalidPathException ipe ) {
					if ( ipe.getIndex() >= 0 )
						folderNameTextField.setCaretPosition( ipe.getIndex() );
					debug( "Could not create folder, invalid name specified!", ipe );
					setError( TextKey.NEW_FOLDER$ERR$INVALID_FOLDER_NAME );
				} catch ( final FileAlreadyExistsException faee ) {
					debug( "Could not create folder, file already exists!", faee );
					setError( TextKey.NEW_FOLDER$ERR$FILE_ALREADY_EXISTS );
				} catch ( final Exception e ) {
					debug( "Could not create folder!", e );
					setError( TextKey.NEW_FOLDER$ERR$COULD_NOT_CREATE_FOLDER );
				}
			}
		} );
    	buttonsPanel.add( okButton );
    	component.getRootPane().setDefaultButton( okButton );
    	
    	addCloseButton( TextKey.GENERAL$BTN$CANCEL );
    	
    	setFocusTargetOnFirstShow( folderNameTextField );
    }
    
    /**
     * Sets an error message.
     * @param errorTextKey text key of the error message
     */
    private void setError( final TextKey errorTextKey ) {
		errorLabel.setIcon( get( XIcon.F_CROSS_OCTAGON ) );
		errorLabel.setText( get( TextKey.GENERAL$ERROR ) + ": " + get( errorTextKey ) );
		
		folderNameTextField.requestFocusInWindow();
    }
    
}
