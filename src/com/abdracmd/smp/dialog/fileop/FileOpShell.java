/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.fileop;

import hu.belicza.andras.util.Utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.abdracmd.fileop.IFileOp;
import com.abdracmd.fileop.impl.ActionOnError;
import com.abdracmd.fileop.impl.ActionWhenFileExists;
import com.abdracmd.fileop.impl.base.BaseExecContext;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.service.settings.SettingText;
import com.abdracmd.smp.dialog.BaseDialogShell;
import com.abdracmd.smp.dialog.options.OptionsShell;
import com.abdracmd.smp.dialog.options.OptionsShell.BodyIcon;
import com.abdracmd.smp.dialog.options.OptionsShell.Option;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderShell;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;

/**
 * This is the shell of the File operation dialog.
 * 
 * @author Andras Belicza
 */
public class FileOpShell extends BaseDialogShell< FileOpModel, FileOpPresenter > {
	
    /**
     * Creates a new FileOpShell.
     * @param fileOpRegistryKey registry key of the file operation to be executed
     * @param inputPaths        input paths
     * @param sourcePath        source path
     * @param destinationPath   destination path
     * @param folderShell       folder shell to report the processed input files to
     */
    public FileOpShell( final String fileOpRegistryKey, final Path[] inputPaths, final Path sourcePath, final Path destinationPath, final FolderShell folderShell ) {
    	this( MainShell.INSTANCE.getModel().getFileOpRegistry().get( fileOpRegistryKey ), inputPaths, sourcePath, destinationPath, folderShell );
    }
	
    /**
     * Creates a new FileOpShell.
     * @param fileOp          file operation to be executed
     * @param inputPaths      input paths
     * @param sourcePath      source path
     * @param destinationPath destination path
     * @param folderShell     folder shell to report the processed input files to
     */
    public FileOpShell( final IFileOp fileOp, final Path[] inputPaths, final Path sourcePath, Path destinationPath, final FolderShell folderShell ) {
    	super( new FileOpModel( fileOp, inputPaths, folderShell ) );
    	
    	presenter = new FileOpPresenter( this );
    	
    	final BaseExecContext execContext = presenter.execContext;
    	
    	execContext.setSourcePath     ( sourcePath      );
    	execContext.setDestinationPath( destinationPath );
    	
    	// Show config/confirm dialog before executing the file operation
    	final JPanel panel = new JPanel( new GridBagLayout() );
		final GridBagConstraints c = new GridBagConstraints();
		c.gridy  = -1;
		// defaults:
		c.anchor = GridBagConstraints.LINE_START;
		c.fill   = GridBagConstraints.HORIZONTAL;
		c.insets.set( 2, 1, 2, 1 );
    	
    	final JTextField                        destinationTextField;
    	final String                            suggestedSingleDestinationFileName;
    	final JComboBox< ActionWhenFileExists > actionWhenFileExistsComboBox;
    	if ( fileOp.usesDestinationPath( execContext ) ) {
    		// Destination path (editor) setting
    		suggestedSingleDestinationFileName = fileOp.suggestSingleDestinationFileName( inputPaths, execContext );
    		if ( suggestedSingleDestinationFileName != null )
    			try {
    				destinationPath = destinationPath.resolve( suggestedSingleDestinationFileName );
    			} catch ( final Exception e ) {
    				debug( "Invalid destination file name suggested by file operation!", e );
    			}
    		
        	c.gridy++;
        	panel.add( new JLabel( get( TextKey.FILE_OP$DESTINATION ) + Utils.COLON_STRING ), c );
        	c.weightx = 1;
        	destinationTextField = new JTextField( Utils.getPathString( destinationPath ), 25 );
        	destinationTextField.selectAll();
        	panel.add( destinationTextField, c );
        	c.weightx = 0;
        	
        	// Action when file exists setting
        	c.gridy++;
        	panel.add( new JLabel( AcUtils.getEnumText( SettingText.FILE_OP$ACTION_WHEN_FILE_EXISTS ) + Utils.COLON_STRING ), c );
        	c.weightx = 1;
        	actionWhenFileExistsComboBox = GuiUtils.createSettingComboBox( SettingKeys.FILE_OP$ACTION_WHEN_FILE_EXISTS, null );
        	panel.add( actionWhenFileExistsComboBox, c );
        	c.weightx = 0;
    	}
    	else {
    		destinationTextField               = null;
    		suggestedSingleDestinationFileName = null;
    		actionWhenFileExistsComboBox       = null;
    	}
		
		// Setting for all: "Action on error"
    	c.gridy++;
    	panel.add( new JLabel( AcUtils.getEnumText( SettingText.FILE_OP$ACTION_ON_ERROR ) + Utils.COLON_STRING ), c );
    	c.weightx = 1;
    	final JComboBox< ActionOnError > actionOnErrorComboBox = GuiUtils.createSettingComboBox( SettingKeys.FILE_OP$ACTION_ON_ERROR, null );
    	panel.add( actionOnErrorComboBox, c );
    	c.weightx = 0;
    	
    	// Custom file operation setting elements:
    	fileOp.addCustomSettingComponents( panel, c, execContext );
    	
		// Setting for all: "Close dialog when completed"
   		c.gridy++;
   		c.gridwidth = GridBagConstraints.REMAINDER;
   		final JCheckBox closeWhenCompletedCheckBox = GuiUtils.createSettingCheckBox( SettingKeys.FILE_OP$CLOSE_DIALOG_WHEN_COMPLETED, null );
   		panel.add( closeWhenCompletedCheckBox, c );
   		c.gridwidth = 1;
    	
    	if ( Option.OK != OptionsShell.withOptionList( null, fileOp.getImageIcon().getImage(), fileOp.getDisplayName(), BodyIcon.QUESTION, new Object[] {
    			fileOp.getConfirmationMessage( inputPaths, execContext ),
    			Box.createVerticalStrut( 10 ),
	    		panel
	    	}, OptionsShell.OPTIONS_OK_CANCEL ).getOption() )
	    	return;
    	
		// TODO invalid edited path can cause exception!
    	if ( destinationTextField != null ) {
        	execContext.setDestinationPath( Paths.get( destinationTextField.getText() ) );
			if ( suggestedSingleDestinationFileName != null ) {
				execContext.setEditedSingleDestinationFileName( Utils.getFileName( execContext.getDestinationPath() ) );
				execContext.setDestinationPath                ( execContext.getDestinationPath().getParent()          );
			}
    	}
    	if ( actionWhenFileExistsComboBox != null )
        	execContext.setActionWhenFileExists( actionWhenFileExistsComboBox.getItemAt( actionWhenFileExistsComboBox.getSelectedIndex() ) );
    	execContext.setActionOnError( actionOnErrorComboBox.getItemAt( actionOnErrorComboBox.getSelectedIndex() ) );
		
    	presenter.packAndshow();
    }
	
}
