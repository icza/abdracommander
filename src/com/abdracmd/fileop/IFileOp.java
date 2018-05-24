/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.fileop;

import hu.belicza.andras.util.iface.HasDisplayName;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.abdracmd.fileop.impl.base.BaseFileOp;

/**
 * A file operation.
 * 
 * <p>Instances of file operation implementations are shared and therefore must be thread safe!</p>
 * 
 * @author Andras Belicza
 * 
 * @see BaseFileOp
 */
public interface IFileOp extends HasDisplayName {
	
	/**
	 * Returns the image icon of the file operation.
	 * @return the image icon of the file operation
	 */
	ImageIcon getImageIcon();
	
	/**
	 * Returns a confirmation message before executing the file operation on the specified input paths.
	 * @param inputPaths  input paths to get confirmation for
	 * @param execContext execution context
	 * @return a confirmation message before executing the file operation on the specified input paths
	 */
	String getConfirmationMessage( Path[] inputPaths, IExecContext execContext );
	
	/**
	 * Tells if the file operation uses the destination path.
	 * @param execContext execution context
	 * @return true if the file operation uses the destination path; false otherwise
	 */
	boolean usesDestinationPath( IExecContext execContext );
	
	/**
	 * If the file operation produces only one destination file, its suggested name must be returned here.<br>
	 * This method is only called if {@link #usesDestinationPath(IExecContext)} returns <code>true</code>.
	 * 
	 * <p>The returned file name is presented to the user who might edit it.
	 * The edited file name can be accessed via {@link IExecContext#getEditedSingleDestinationFileName()}.</p>
	 * 
	 * @param inputPaths  input paths
	 * @param execContext execution context
	 * 
	 * @return the suggested file name of the destination file if the operation produces only 1 destination file; <code>null</code> otherwise
	 * 
	 * @see IExecContext#getEditedSingleDestinationFileName()
	 */
	String suggestSingleDestinationFileName( Path[] inputPaths, IExecContext execContext );
	
	/**
	 * The file operation can add custom setting components to the specified panel which is displayed on the
	 * setting / confirmation dialog before the {@link #execute(Path[], IExecContext)} method is called.
	 * 
	 * <p>The panel is preconfigured with a {@link GridBagLayout}, first column is for the setting name,
	 * second column is for the editor component.</p>
	 * 
	 * <p>The implementation is responsible to store the editor components (which are added to the panel)
	 * in the execution context in order to have access to them later during the execution.</p>
	 * 
	 * <p>Example of properly adding a text field setting component:
	 * <blockquote><pre>
	 * void addCustomSettingComponents( JPanel panel, GridBagConstraints c, IExecContext execContext ) {
	 *     // First a label:
	 *     c.gridy++;
	 *     panel.add( new JLabel( "My setting:" ), c );
	 *     
	 *     // Now the text field:
	 *     c.gridwidth = GridBagConstraints.REMAINDER; // give it whatever space left
	 *     JTextField mySettingTextField = new JTextField( "initial value", 10 );
	 *     panel.add( mySettingTextField, c );
	 *     c.gridwidth = 1;                            // properly reset the grid width...
	 *     
	 *     // Store the text field component to have access to its edited text when needed during execution:
	 *     execContext.put( "mySettingKey", mySettingTextField );
	 * }
	 * </pre></blockquote></p>
	 * 
	 * @param panel       panel to add custom setting components to
	 * @param c           preconfigured grid bag constraints
	 * @param execContext execution context
	 */
	void addCustomSettingComponents( JPanel panel, GridBagConstraints c, IExecContext execContext );
	
	/**
	 * Executes the file operation on the specified input paths.
	 * @param inputPaths  input paths to execute the file operation on
	 * @param execContext execution context
	 */
	void execute( Path[] inputPaths, IExecContext execContext );
	
}
