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

/**
 * Context for processing one path.
 * 
 * @author Andras Belicza
 */
class PathContext {
	
    /**
     * In case when file exists and default action is ASK, and the user chooses OVERWRITE (just the current),
     * we set the action when file exists to OVERWRITE, and so to only apply it once (just to the current),
     * we have to clear the action when file exists, after the current file is processed.
     * This intention is stored in this attribute. 
     */
	public boolean clearDefaultOverwriteAction;
	
	/** When the user edits the output path, it only applies to one path.
	 * This attribute stores if it has to be cleared. */
	public boolean clearEditedOutputPath;
	
}
