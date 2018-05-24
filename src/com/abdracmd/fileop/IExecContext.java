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

import hu.belicza.andras.util.context.IContext;

import java.nio.file.Path;

import javax.swing.JDialog;

import com.abdracmd.fileop.impl.ActionOnError;
import com.abdracmd.fileop.impl.ActionWhenFileExists;
import com.abdracmd.fileop.impl.base.BaseExecContext;
import com.abdracmd.fileop.impl.base.ExecResult;

/**
 * File operation execution context.
 * 
 * @author Andras Belicza
 * 
 * @see BaseExecContext
 */
public interface IExecContext extends IContext {
	
	/**
	 * Returns the source path.
	 * @return the source path
	 */
	Path getSourcePath();
	
	/**
	 * Returns the destination path.
	 * @return the destination path
	 */
	Path getDestinationPath();
	
	/**
	 * Returns the edited single destination file name.
	 * 
	 * @return the edited single destination file name; or <code>null</code> if no single destination file name was suggested by the file operation
	 * 
	 * @see IFileOp#suggestSingleDestinationFileName(Path[], IExecContext)
	 * @see #deriveOutputPath(Path)
	 */
	String getEditedSingleDestinationFileName();
	
	/**
	 * Tells if execution has been suspended.<br>
	 * 
	 * If execution has been suspended, the implementation must not continue the execution,
	 * and the implementation periodically has to poll whether the suspension state changes
	 * and then continue the execution, or if cancel is requested (see {@link #isCancelRequested()}), abort it.
	 * 
	 * @return true if execution has been suspended; false otherwise
	 * 
	 * @see #waitIfSuspended()
	 */
	boolean isSuspended();
	
	/**
	 * Tells if execution cancel has been requested
	 * and the execution of the file operation has to be aborted as soon as possible.
	 * 
	 * @return true if execution cancel has been requested; false otherwise
	 * 
	 * @see #requestCancel()
	 */
	boolean isCancelRequested();
	
	/**
	 * Requests the execution cancel.
	 * 
	 * @see #isCancelRequested()
	 */
	void requestCancel();
	
	/**
	 * If execution is suspended, this method will block the thread until
	 * the execution is resumed or cancelled.
	 * 
	 * <p>This method must be called when a file operation wants to wait while the execution is suspended,
	 * because time waited inside this method is properly excluded from statistics.<br>
	 * If a file operation implementation waits in some other way (e.g. using custom <code>sleep()</code>,
	 * statistics will be inaccurate.</p>
	 * 
	 * @return true if the execution was suspended and thread was blocked; false otherwise
	 * 
	 * @see #isSuspended()
	 */
	boolean waitIfSuspended();
	
	/**
	 * Returns the presenter dialog of the file operation execution, if any.
	 * It can be used as a parent window if further dialogs are created by the file operation.
	 * @return the presenter dialog of the file operation; or <code>null</code> if there is none
	 */
	JDialog getPresenterDialog();
	
	/**
	 * Publishes the current task name.
	 * @param taskName task name to be published 
	 */
	void publishTaskName( String taskName );
	
	/**
	 * Publishes a status (for example "DONE", "ABORTED", "PAUSED").
	 * @param status status to be published; <code>null</code> value indicates no status 
	 */
	void publishStatus( String status );
	
	/**
	 * Publishes the total count and total size of the input of the file operation.
	 * 
	 * @param totalCount total file count of the input (recursive)
	 * @param totalSize  total file size of the input (recursive)
	 * 
	 * @see #getTotalCount()
	 * @see #getTotalSize()
	 */
	void publishTotal( int totalCount, long totalSize );
	
	/**
	 * Publishes the start of execution of a path.
	 * 
	 * @param path path whose execution has been started
	 * @param size size of the path
	 * 
	 * @see #publishFinished()
	 */
	void publishStarted( Path path, long size );
	
	/**
	 * Publishes the output path of the current path.
	 * @param outputPath output path to be published
	 */
	void publishOutputPath( Path outputPath );
	
	/**
	 * Publishes the execution progress of the current file.
	 * 
	 * @param processedSize the size of the processed part of the file
	 */
	void publishProgress( long processedSize );
	
	/**
	 * Publishes the successful finish of execution of the current path.
	 * 
	 * @see #publishStarted(Path, long)
	 * @see #publishSkipped(boolean)
	 */
	void publishFinished();
	
	/**
	 * Publishes that the execution of the current path is skipped.
	 * 
	 * @param error tells if the current path is skipped due to an error; false otherwise
	 * 
	 *  @see #publishFinished()
	 */
	void publishSkipped( boolean error );
	
	/**
	 * Reports that an input path has been processed.
	 * 
	 * <p>This input path can be therefore deselected in the folder table for example.</p>
	 * 
	 * @param inputPath an input path that has been processed;
	 * 		must be a member of the input paths array passed on as the argument of {@link IFileOp#execute(Path[], IExecContext)}
	 */
	void reportInputPathProcessed( Path inputPath );
	
	/**
	 * Returns the current path being processed published by {@link #publishStarted(Path, long)}.
	 * @return the current path being processed
	 * 
	 * @see #publishStarted(Path, long)
	 */
	Path getCurrentPath();
	
	/**
	 * Returns the current output path published by {@link #publishOutputPath(Path)}.
	 * @return the current output path
	 * 
	 * @see #publishOutputPath(Path)
	 */
	Path getCurrentOutputPath();
	
	/**
	 * Sets the user-edited output path to be used instead of the current output path.
	 * 
	 * @param editedOutputPath user-edited output path to be used instead of the current output path
	 * 
	 * @see #getEditedOutputPath()
	 * @see #deriveOutputPath(Path)
	 */
	void setEditedOutputPath( Path editedOutputPath );
	
	/**
	 * In some cases the user is presented with a choice to rename the output path to resolve an error or a name conflict with an existing file.
	 * This method returns if such edited path should be used.
	 * 
	 * @return a user-edited output path to be used instead of the current output path; <code>null</code> otherwise
	 * 
	 * @see #setEditedOutputPath(Path)
	 * @see #deriveOutputPath(Path)
	 */
	Path getEditedOutputPath();
	
	/**
	 * Derives the output path for the specified input path.
	 * 
	 * <p>The output path for an input path is the destination path appended with the relative path from the source path to the input path.</p>
	 * Example:
	 * <table style='border:1px #888888 solid'>
	 * <tr><td>Source path:     <td><code>/source/abc/</code>
	 * <tr><td>Input path:      <td><code>/source/abc/<b>xyz/file.ext</b></code>
	 * <tr><td>Destination path:<td><code>/destin/ghi/</code>
	 * <tr><td>Output path:     <td><code>/destin/ghi/<b>xyz/file.ext</b></code>
	 * </table></p> 
	 * 
	 * <p>Exceptions:
	 * <ul>
	 * 	<li>If {@link #getEditedSingleDestinationFileName()} returns a non-null value, that name will be resolved to the destination path.
	 * 	<li>If {@link #getEditedOutputPath()} returns a non-null value, that will be returned as-is (without being resolved to the destination path).
	 * </ul></p>
	 * 
	 * @param path path to derive the output path for
	 * 
	 * @return the derived output path for the specified input path
	 * 
	 * @see #getEditedSingleDestinationFileName()
	 * @see #getEditedOutputPath()
	 */
    Path deriveOutputPath( final Path path );
	
	/**
	 * Returns the action to be taken when file exists.
	 * 
	 * <p>This method returns the user chosen value if {@link IFileOp#usesDestinationPath(IExecContext)}
	 * returns <code>true</code>; otherwise it returns <code>null</code>.</p>
	 * 
	 * @return the action to be taken when file exists
	 */
	ActionWhenFileExists getActionWhenFileExists();
	
	/**
	 * Sets the action to be taken when file exists.
	 * @param actionWhenFileExists action to be set to be taken when file exists
	 */
	void setActionWhenFileExists( ActionWhenFileExists actionWhenFileExists );
	
	/**
	 * Returns the action to be taken on errors.
	 * @return the action to be taken on errors
	 */
	ActionOnError getActionOnError();
	
	/**
	 * Sets the action to be taken on errors.
	 * @param actionOnError action to be set to be taken on errors
	 */
	void setActionOnError( ActionOnError actionOnError );
	
	/**
	 * Returns a shared instance of {@link ExecResult} to be reused to avoid instantiation for each path.
	 * @return a shared instance of {@link ExecResult} to be reused to avoid instantiation for each path
	 */
	ExecResult getSharedExecResult();
	
    /**
     * Returns the suggested buffer size to be used.
     * <p>The suggested buffer size is a multiple of 1024 and is at least 1 KB.</p>
     * @return the suggested buffer size to be used
     */
    int getSuggestedBufferSize();
    
	/**
	 * Returns a buffer which can be used for any purpose.<br>
	 * 
	 * The buffer might not be a newly created buffer, and it might be shared between calls for optimization purposes.
	 * It should not be assumed that the buffer is initialized with 0 values.
	 * 
	 * <p>Tip: the implementation returns a previously requested buffer if its size is not smaller
	 * than the requested minimum size. If your implementation requests different sizes of buffers,
	 * you can optimize it to request the larger (largest) size first to avoid reallocations.</p>
	 * 
	 * @param minSize minimum size of the requested buffer
	 * @return a buffer
	 */
	byte[] getSharedBuffer( int minSize );
	
	/**
	 * Returns the total file count of the input (recursive).
	 * 
	 * @return the total file count of the input (recursive); or -1 if it has not yet been set (with {@link #publishTotal(int, long)})
	 * 
	 * @see #publishTotal(int, long)
	 */
	int getTotalCount();
	
	/**
	 * Returns the total file size of the input (recursive).
	 * 
	 * @return the total file size of the input (recursive); or -1 if it has not yet been set (with {@link #publishTotal(int, long)})
	 * 
	 * @see #publishTotal(int, long)
	 */
	long getTotalSize();
	
}
