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

import hu.belicza.andras.util.NormalThread;
import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.abdracmd.fileop.impl.base.BaseExecContext;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.smp.dialog.BaseDialogPresenter;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;
import com.abdracmd.util.SizeFormat;

/**
 * This is the File operation presenter.
 * 
 * @author Andras Belicza
 */
class FileOpPresenter extends BaseDialogPresenter< FileOpModel, FileOpShell > {
	
	/** Current task name label.   */
	private final JLabel       taskNameAndStatusLabel = GuiUtils.changeFontToBold( new JLabel( Utils.SPACE_STRING ) ); // Use a space to preserve space...
	/** Current file label.        */
	private final JLabel       currentFileLabel       = new JLabel();
	/** Destination file label.    */
	private final JLabel       destinationFileLabel;
	
	/** Current file progress bar. */
	private final JProgressBar currentProgressBar     = new JProgressBar();
	/** Total size progress bar.   */
	private final JProgressBar totalSizeProgressBar   = new JProgressBar();
	/** Total count progress bar.  */
	private final JProgressBar totalCountProgressBar  = new JProgressBar();
	
	/** Show completion percent in title. */
	private final boolean showCompletionInTitle = get( SettingKeys.FILE_OP$SHOW_COMPLETION_PERCENT_IN_TITLE );
	
	/** The execution context. */
	protected final BaseExecContext execContext = new BaseExecContext() {
		{
			setPresenterDialog( component );
		}
		@Override
		public void publishTotal( final int totalCount, final long totalSize ) {
			super.publishTotal( totalCount, totalSize );
			totalCountString  = null;
			totalSizeString   = null;
			
			totalCountProgressBar.setMaximum( totalCount );
			totalSizeProgressBar .setMaximum( (int) ( totalSize < Integer.MAX_VALUE ? totalSize : totalSize >> 16 ) );
			
			refreshCurrentProgressBar   ();
			refreshTotalSizeProgressBar ();
			refreshTotalCountProgressBar();
		}
		
		@Override
		public void publishProgress( final long processedSize ) {
			super.publishProgress( processedSize );
			refreshCurrentProgressBar  ();
			refreshTotalSizeProgressBar();
		}
		
		@Override
		public void publishTaskNameAndStatus( final String taskNameAndStatus ) {
			taskNameAndStatusLabel.setText( taskNameAndStatus );
		}
		
		@Override
		public void publishStarted( final Path path, final long size ) {
			super.publishStarted( path, size );
			currentFileLabel.setText( Utils.getPathString( path ) );
			currentProgressBar.setMaximum(  (int) ( size < Integer.MAX_VALUE ? size : size >> 16 ) );
			refreshCurrentProgressBar();
		}
		
		@Override
		public void publishOutputPath( final Path outputPath ) {
			super.publishOutputPath( outputPath );
			if ( destinationFileLabel != null )
				destinationFileLabel.setText( Utils.getPathString( outputPath ) );
		}
		
		@Override
		public void publishFinished() {
			super.publishFinished();
			currentProgressBar.setValue( currentProgressBar.getMaximum() );
			refreshCurrentProgressBar   ();
			refreshTotalSizeProgressBar ();
			refreshTotalCountProgressBar();
		}
		
		@Override
		public void publishSkipped( final boolean error ) {
			super.publishSkipped( error );
			refreshCurrentProgressBar   ();
			refreshTotalSizeProgressBar ();
			refreshTotalCountProgressBar();
		}
		
		@Override
		public void reportInputPathProcessed( final Path inputPath ) {
			model.folderShell.getPresenter().receiveInputPathProcessed( inputPath );
		}
	};
	
    /**
     * Creates a new FileOpPresenter.
     * @param shell reference to the shell
     */
    protected FileOpPresenter( final FileOpShell shell ) {
    	super( shell );
    	
    	component.setTitle( model.fileOp.getDisplayName() );
    	component.setIconImage( model.fileOp.getImageIcon().getImage() );
    	
    	final JPanel panel = new JPanel( new GridBagLayout() );
		final GridBagConstraints c = new GridBagConstraints();
		c.gridy  = -1;
		// defaults:
		c.anchor = GridBagConstraints.LINE_START;
		c.fill   = GridBagConstraints.HORIZONTAL;
		c.insets.set( 2, 1, 2, 1 );
    	
    	c.gridy++;
		// Full row, center aligned
    	c.gridwidth = GridBagConstraints.REMAINDER; c.anchor = GridBagConstraints.CENTER; c.fill = GridBagConstraints.NONE;
    	panel.add( taskNameAndStatusLabel, c );
		c.anchor = GridBagConstraints.LINE_START; c.gridwidth = 1; c.fill = GridBagConstraints.HORIZONTAL;
    	
    	c.gridy++;
    	panel.add( new JLabel( get( TextKey.FILE_OP$CURRENT ) + Utils.COLON_STRING ), c );
    	panel.add( currentFileLabel, c );
    	
    	destinationFileLabel = model.fileOp.usesDestinationPath( execContext ) ? new JLabel( Utils.SPACE_STRING ) : null;
    	if ( destinationFileLabel != null ) {
        	c.gridy++;
        	panel.add( new JLabel( get( TextKey.FILE_OP$DESTINATION ) + Utils.COLON_STRING ), c );
    		panel.add( destinationFileLabel, c );
    	}
    	
    	c.gridy++;
    	panel.add( new JLabel( get( TextKey.FILE_OP$CURRENT_SIZE ) + Utils.COLON_STRING ), c );
    	c.weightx = 1;
    	currentProgressBar.setStringPainted( true );
    	currentProgressBar.setString( Utils.EMPTY_STRING );
    	currentProgressBar.setPreferredSize( new Dimension( 400, currentProgressBar.getPreferredSize().height ) );
    	panel.add( currentProgressBar, c );
    	c.weightx = 0;
    	
    	c.gridy++;
    	panel.add( new JLabel( get( TextKey.FILE_OP$TOTAL_SIZE ) + Utils.COLON_STRING ), c );
    	totalSizeProgressBar.setStringPainted( true );
    	totalSizeProgressBar.setString( Utils.EMPTY_STRING );
   		panel.add( totalSizeProgressBar, c );
    	
    	c.gridy++;
    	panel.add( new JLabel( get( TextKey.FILE_OP$TOTAL_COUNT ) + Utils.COLON_STRING ), c );
    	totalCountProgressBar.setStringPainted( true );
    	totalCountProgressBar.setString( Utils.EMPTY_STRING );
   		panel.add( totalCountProgressBar, c );
    	
   		c.gridy++;
   		c.gridwidth = GridBagConstraints.REMAINDER;
   		final JCheckBox closeWhenCompletedCheckBox = GuiUtils.createSettingCheckBox( SettingKeys.FILE_OP$CLOSE_DIALOG_WHEN_COMPLETED, null );
   		panel.add( closeWhenCompletedCheckBox, c );
   		c.gridwidth = 1;
   		
    	component.getContentPane().add( panel, BorderLayout.CENTER );
    	
    	initButtonsPanel();
    	final JXButton pauseButton = new JXButton();
		component.getRootPane().setDefaultButton( pauseButton );
    	GuiUtils.initButton( pauseButton, null, TextKey.FILE_OP$BTN$PAUSE );
    	buttonsPanel.add( pauseButton );
    	
    	final JXButton cancelButton  = new JXButton();
    	GuiUtils.initButton( cancelButton, null, TextKey.GENERAL$BTN$CANCEL );
    	buttonsPanel.add( cancelButton );
    	
    	final Thread executorThread = new NormalThread( "File operation: " + model.fileOp.getDisplayName() ) {
    		@Override
    		public void run() {
    			model.fileOp.execute( model.inputPaths, execContext );
    			
    	    	if ( showCompletionInTitle )
    	    		model.folderShell.getMainFrameShell().setCompletitionPercent( null );
    			
    			if ( !execContext.isCancelRequested() ) {
	    			switch ( get( SettingKeys.FILE_OP$BEEP_ON_COMPLETION ) ) {
	    			case WHEN_WINDOW_INACTIVE :
	    				if ( component.isActive() )
	    					break;
	    			case ALWAYS :
	    				AcUtils.beep();
	    				break;
	    			case NEVER :
	    				break;
	    			}
    			}
    			
    			if ( closeWhenCompletedCheckBox.isSelected() )
    				component.dispose();
    			else {
	    			pauseButton.setEnabled( false );
	    			component.getRootPane().setDefaultButton( cancelButton );
	    	    	GuiUtils.initButton( cancelButton, null, TextKey.GENERAL$BTN$CLOSE );
	    	    	cancelButton.requestFocusInWindow();
    			}
    		}
    	};
    	
    	pauseButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				execContext.setSuspended( !execContext.isSuspended() );
				GuiUtils.initButton( pauseButton, null, execContext.isSuspended() ? TextKey.FILE_OP$BTN$RESUME : TextKey.FILE_OP$BTN$PAUSE );
			}
		} );
    	cancelButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( executorThread.isAlive() )
					execContext.requestCancel();
				else
					component.dispose();
			}
		} );
    	
    	// Start execution only when dialog has been opened (else execution might finish before dialog could even be created
    	// which could falsely beep due to our dialog is not (yet) active).
    	component.addWindowListener( new WindowAdapter() {
    		@Override
    		public void windowOpened( final WindowEvent event ) {
    	   		// This option is presented on the pre-confirmation dialog, where it can be changed,
    			// so refersh the associated checkbox's state:
    			closeWhenCompletedCheckBox.setSelected( get( SettingKeys.FILE_OP$CLOSE_DIALOG_WHEN_COMPLETED ) );
    			
    	    	executorThread.start();
    		}
		} );
    	
    	setFocusTargetOnFirstShow( pauseButton );
    }
    
    /**
     * Refreshes the current progress bar.
     */
    private void refreshCurrentProgressBar() {
    	final long value = execContext.getCurrentProcessedSize();
    	final long max   = execContext.getCurrentSize();
    	
    	currentProgressBar.setValue( (int) ( max < Integer.MAX_VALUE ? value : value >> 16 ) );
    	
    	final String currentSizeString = AcUtils.getFormattedSize( SizeFormat.AUTO, max, 1 );
    	
    	final String valueString = value == 0 ? AcUtils.ZERO_BYTES : value == max ? currentSizeString : AcUtils.getFormattedSize( SizeFormat.AUTO, value, 1 );
    	
    	currentProgressBar.setString( valueString
    			+ Utils.OUT_OF_STRING + currentSizeString
    			+ Utils.SPACE_PARENTHESIS_STRING + ( value == max ? 100 : ( max == 0 ? 0 : value * 100 / max ) ) + " %)" );
    }
    
    /** Cache of total size string (which doesn't really change...).             */
    private String totalSizeString;
    /** Cache of total size string with extras (which doesn't really change...). */
    private String totalSizeExtraString;
    
    /**
     * Refreshes the total size progress bar.
     */
    private void refreshTotalSizeProgressBar() {
    	final long value = execContext.getTotalProcessedSize();
    	final long max   = execContext.getTotalSize();
    	
    	totalSizeProgressBar.setValue( (int) ( max < Integer.MAX_VALUE ? value : value >> 16 ) );
    	
    	if ( totalSizeString == null ) {
    		totalSizeString      = AcUtils.getFormattedSize( SizeFormat.AUTO, max, 1 );
    		totalSizeExtraString = Utils.OUT_OF_STRING + totalSizeString + Utils.SPACE_PARENTHESIS_STRING;
    	}
    	
    	final String valueString = value == 0 ? AcUtils.ZERO_BYTES : value == max ? totalSizeString : AcUtils.getFormattedSize( SizeFormat.AUTO, value, 1 );
    	
    	final int percent = value == max ? 100 : ( max == 0 ? 0 : (int) ( value * 100 / max ) );
    	
    	final StringBuilder builder = new StringBuilder( valueString );
    	builder.append( totalSizeExtraString );
    	builder.append( percent ).append( " %)" );
    	if ( execContext.getSkippedCount() > 0 )
    		builder.append( Utils.SPACE_PARENTHESIS_STRING ).append( get( TextKey.FILE_OP$SKIPPED ) ).append( Utils.COLON_SPACE_STRING )
    			.append( AcUtils.getFormattedSize( SizeFormat.AUTO, execContext.getSkippedSize(), 1 ) ).append( ')' );
    		
    	totalSizeProgressBar.setString( builder.toString() );
    	
    	if ( showCompletionInTitle )
    		model.folderShell.getMainFrameShell().setCompletitionPercent( percent );
    }
    
    /** Cache of total count string (which doesn't really change...).  */
    private String totalCountString;
    
    /**
     * Refreshes the total count progress bar.
     */
    private void refreshTotalCountProgressBar() {
    	totalCountProgressBar.setValue( execContext.getDoneCount() );
    	
    	if ( totalCountString == null )
    		totalCountString = Utils.OUT_OF_STRING + formatNumber( execContext.getTotalCount() ) + Utils.SPACE_PARENTHESIS_STRING;
    	
    	final StringBuilder builder = new StringBuilder( formatNumber( execContext.getDoneCount() ) );
    	builder.append( totalCountString );
    	builder.append( execContext.getTotalCount() == 0 ? 0 : execContext.getDoneCount() * 100 / execContext.getTotalCount()  );
    	builder.append( " %)" );
    	if ( execContext.getSkippedCount() > 0 )
    		builder.append( Utils.SPACE_PARENTHESIS_STRING ).append( get( TextKey.FILE_OP$SKIPPED ) ).append( Utils.COLON_SPACE_STRING )
    			.append( formatNumber( execContext.getSkippedCount() ) )
    			.append( "; " ).append( get( TextKey.GENERAL$ERROR ) ).append( Utils.COLON_SPACE_STRING )
    			.append( formatNumber( execContext.getErrorCount() ) ).append( ')' );
    	
    	totalCountProgressBar.setString( builder.toString() );
    }
    
}
