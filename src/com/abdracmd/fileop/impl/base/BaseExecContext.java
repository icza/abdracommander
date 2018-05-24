/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.fileop.impl.base;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.context.impl.BaseContext;

import java.nio.file.Path;

import javax.swing.JDialog;

import com.abdracmd.fileop.IExecContext;
import com.abdracmd.fileop.impl.ActionOnError;
import com.abdracmd.fileop.impl.ActionWhenFileExists;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.util.AcUtils;

/**
 * Base execution context with some help to implement execution contexts.
 * 
 * @author Andras Belicza
 */
public abstract class BaseExecContext extends BaseContext implements IExecContext {
	
	/** Source path.      */
	protected Path sourcePath;
	/** Destination path. */
	protected Path destinationPath;
	
	/** Edited single destination file name. */
	protected String editedSingleDestinationFileName;
	
	/** Suspended state.        */
	protected volatile boolean suspended;
	/** Cancel requested state. */
	protected volatile boolean cancelRequested;
	
	/** Presenter dialog of the file operation execution. */
	protected JDialog presenterDialog;
	
	/** Current task name. */
	protected String taskName;
	/** Current status.    */
	protected String status;
	
	/** Total file count of the input (recursive). */
	protected int  totalCount   = -1;
	/** Total file size  of the input (recursive). */
	protected long totalSize    = -1;
	/** Total processed file count.                */
	protected int  doneCount    = 0;
	/** Total processed file size.                 */
	protected long doneSize     = 0;
	/** Skipped file count.                        */
	protected int  skippedCount = 0;
	/** Skipped file size.                         */
	protected long skippedSize  = 0;
	/** Error file count.                          */
	protected int  errorCount   = 0;
	/** Current path.                              */
	protected Path currentPath;
	/** Current output path.                       */
	protected Path currentOutputPath;
	/** A user-edited output path to be used instead of the current output path. */
	protected Path editedOutputPath;
	/** Size of the current file.                  */
	protected long currentSize;
	/** Processed size of the current file.        */
	protected long currentProcessedSize;
	/** Tells if a file is in progress
	 * (started and not yet finished).             */
	protected boolean inProgress;
	
	/** Action to be taken when file exists. */
	protected ActionWhenFileExists actionWhenFileExists;
	/** Action to be taken on errors.        */
	protected ActionOnError        actionOnError;
	
	/** A shared instance of {@link ExecResult} to be reused to avoid instantiation for each path. */
	protected final ExecResult sharedExecResult = new ExecResult();
	
	/** Suggested buffer size. */
	protected int suggestedBufferSize = -1; // -1 indicates not yet set 
	/** A shared, reusable buffer */
	protected byte[] sharedBuffer;
	
	@Override
	public Path getSourcePath() {
		return sourcePath;
	}
	
	@Override
	public Path getDestinationPath() {
		return destinationPath;
	}
	
	@Override
	public String getEditedSingleDestinationFileName() {
		return editedSingleDestinationFileName;
	}
	
	/**
	 * Sets the edited single destination file name
	 * @param editedSingleDestinationFileName edited single destination file name to be set
	 */
	public void setEditedSingleDestinationFileName( final String editedSingleDestinationFileName ) {
		this.editedSingleDestinationFileName = editedSingleDestinationFileName;
	}
	
	@Override
	public boolean isSuspended() {
		return suspended;
	}
	
	@Override
	public boolean isCancelRequested() {
		return cancelRequested;
	}
	
	@Override
	public void requestCancel() {
		cancelRequested = true;
		publishStatus( AcUtils.CAH.get( TextKey.FILE_OP$ABORTED ) );
	}
	
	@Override
	public boolean waitIfSuspended() {
		if ( suspended ) {
			try {
				do {
					Thread.sleep( 10 );
				} while ( suspended && !cancelRequested );
			} catch ( final InterruptedException ie ) {
				AcUtils.CAH.error( Utils.EMPTY_STRING, ie );
			}
			return true;
		}
		else
			return false;
	}
	
	@Override
	public JDialog getPresenterDialog() {
		return presenterDialog;
	}
	
	/**
	 * Sets the presenter dialog.
	 * @param presenterDialog presenter dialog to be set
	 */
	public void setPresenterDialog( final JDialog presenterDialog ) {
		this.presenterDialog = presenterDialog;
	}
	
	@Override
	public void publishTaskName( final String taskName ) {
		this.taskName = taskName;
		publishTaskNameAndStatus();
	}
	
	@Override
	public void publishStatus( final String status ) {
		this.status = status;
		publishTaskNameAndStatus();
	}
	
	/**
	 * Generates a string from the current task name and status and publishes it
	 * via {@link #publishTaskNameAndStatus(String)}.
	 * @see #publishTaskNameAndStatus(String)
	 */
	private void publishTaskNameAndStatus() {
		publishTaskNameAndStatus( status == null ? taskName + "..." : status   + " [" + taskName + "]" );
		publishTaskNameAndStatus( status == null ? taskName + "..." : taskName + Utils.COLON_SPACE_STRING + status );
	}
	
	/**
	 * Publishes the current task name and current status.
	 * @param taskNameAndStatus task name and status to be published 
	 */
	public abstract void publishTaskNameAndStatus( String taskNameAndStatus );
	
	@Override
	public void publishTotal( final int totalCount, final long totalSize ) {
		this.totalCount = totalCount;
		this.totalSize  = totalSize;
	}
	
	@Override
	public void publishStarted( final Path path, final long size ) {
		currentPath          = path;
		
		currentSize          = size;
		currentProcessedSize = 0;
		
		inProgress           = true;
	}
	
	@Override
	public void publishOutputPath( final Path outputPath ) {
		this.currentOutputPath = outputPath;
	}
	
	@Override
	public void publishProgress( final long processedSize ) {
		this.currentProcessedSize = processedSize;
	}
	
	@Override
	public void publishFinished() {
		currentProcessedSize = currentSize;
		
		doneCount++;
		doneSize += currentSize;
		
		inProgress = false;
	}
	
	@Override
	public void publishSkipped( final boolean error ) {
		doneCount++;
		
		skippedSize += currentSize;
		skippedCount++;
		
		if ( error )
			errorCount++;
		
		inProgress = false;
	}
	
	@Override
	public Path getCurrentPath() {
		return currentPath;
	}
	
	@Override
	public Path getCurrentOutputPath() {
		return currentOutputPath;
	}
	
	@Override
	public void setEditedOutputPath( final Path editedOutputPath ) {
		this.editedOutputPath = editedOutputPath;
	}
	
	@Override
	public Path getEditedOutputPath() {
		return editedOutputPath;
	}
	
	@Override
    public Path deriveOutputPath( final Path path ) {
    	if ( editedOutputPath != null )
    		return editedOutputPath;
		
    	if ( editedSingleDestinationFileName == null )
    		return destinationPath.resolve( sourcePath.relativize( path ) );
    	else
    		return destinationPath.resolve( editedSingleDestinationFileName );
    }
	
	@Override
	public ActionWhenFileExists getActionWhenFileExists() {
		return actionWhenFileExists;
	}
	
	@Override
	public void setActionWhenFileExists( final ActionWhenFileExists actionWhenFileExists ) {
		this.actionWhenFileExists = actionWhenFileExists;
	}
	
	@Override
	public ActionOnError getActionOnError() {
		return actionOnError;
	}
	
	@Override
	public void setActionOnError( final ActionOnError actionOnError ) {
		this.actionOnError = actionOnError;
	}
	
	@Override
	public ExecResult getSharedExecResult() {
		return sharedExecResult;
	}
	
	@Override
    public int getSuggestedBufferSize() {
    	if ( suggestedBufferSize < 0 )
    		suggestedBufferSize = 1024 * AcUtils.CAH.get( SettingKeys.FILE_OP$BUFFER_SIZE );
    	
    	return suggestedBufferSize;
    }
	
	@Override
	public byte[] getSharedBuffer( final int minSize ) {
		if ( sharedBuffer == null || sharedBuffer.length < minSize )
			sharedBuffer = new byte[ minSize ];
		return sharedBuffer;
	}
	
	@Override
	public int getTotalCount() {
		return totalCount;
	}
	
	@Override
	public long getTotalSize() {
		return totalSize;
	}
	
	/**
	 * Sets the source path.
	 * @param sourcePath source path to be set
	 */
	public void setSourcePath( final Path sourcePath ) {
		this.sourcePath = sourcePath;
	}
	
	/**
	 * Sets the destination path.
	 * @param destinationPath destination path to be set
	 */
	public void setDestinationPath( final Path destinationPath ) {
		this.destinationPath = destinationPath;
	}
	
	/**
	 * Sets the suspended state.
	 * @param suspended suspended state to be set
	 */
	public void setSuspended( final boolean suspended ) {
		this.suspended = suspended;
		publishStatus( suspended ? AcUtils.CAH.get( TextKey.FILE_OP$PAUSED ) : null );
	}
	
	/**
	 * Returns the total processed file count.
	 * @return the total processed file count
	 */
	public int getDoneCount() {
		return doneCount;
	}
	
	/**
	 * Returns the size of the current file.
	 * @return the size of the current file
	 */
	public long getCurrentSize() {
		return currentSize;
	}
	
	/**
	 * Returns the skipped file count.
	 * @return the skipped file count
	 */
	public int getSkippedCount() {
		return skippedCount;
	}
	
	/**
	 * Returns the skipped file size.
	 * @return the skipped file size
	 */
	public long getSkippedSize() {
		return skippedSize;
	}
	
	/**
	 * Returns the error file count.
	 * @return the error file count
	 */
	public int getErrorCount() {
		return errorCount;
	}
	
	/**
	 * Returns the processed size of the current file.
	 * @return the processed size of the current file
	 */
	public long getCurrentProcessedSize() {
		return currentProcessedSize;
	}
	
	/**
	 * Returns the total processed file size, including the currently processed one.
	 * @return the total processed file size, including the currently processed one
	 */
	public long getTotalProcessedSize() {
		return inProgress ? doneSize + currentProcessedSize : doneSize;
	}
	
}
