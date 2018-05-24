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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.abdracmd.smp.dialog.options.OptionsShell;

/**
 * Execution result of a single path.
 * 
 * @author Andras Belicza
 */
public class ExecResult {
	
	/**
	 * Execution result sub-type.
	 * @author Andras Belicza
	 */
	public enum Result {
		/** Execution not carried over due to file exists. */
		FILE_EXISTS,
		/** Execution failed.                              */
		ERROR;
	}
	
	/** Result of the execution.    */
	public Result result;
	/** Path being executed.        */
	public Path path;
	/** Output path being executed. */
	public Path outputPath;
	/** Message list to be displayed to the user when user choice or confirmation is required.
	 * Message objects will be passed to the {@link OptionsShell} as-is, with optionally new messages being added. */
	public List< Object > messageList = new ArrayList<>();
	
	// Options?
	
	/**
	 * Configures this execution result for error ({@link Result#ERROR}).
	 * 
	 * @param path       path being executed
	 * @param outputPath output path being executed; should be <code>null</code> if the file operation does not uses output paths 
	 * @param messages   messages to present to the user
	 * 
	 * @return <code>this</code> so the return value can immediately be returned for example
	 */
	public ExecResult setError( final Path path, final Path outputPath, final Object... messages ) {
		result          = Result.ERROR;
		this.path       = path;
		this.outputPath = outputPath;
		
		messageList.clear();
		Collections.addAll( messageList, messages );
		
		return this;
	}
	
	/**
	 * Configures this execution result for file exists ({@link Result#FILE_EXISTS}).
	 * 
	 * @param path       path being executed
	 * @param outputPath output path being executed; should be <code>null</code> if the file operation does not uses output paths
	 * @param messages   messages to present to the user
	 * 
	 * @return <code>this</code> so the return value can immediately be returned for example
	 */
	public ExecResult setFileExists( final Path path, final Path outputPath, final Object... messages ) {
		result          = Result.FILE_EXISTS;
		this.path       = path;
		this.outputPath = outputPath;
		
		messageList.clear();
		Collections.addAll( messageList, messages );
		
		return this;
	}
	
}
