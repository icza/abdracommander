/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder.column;

import hu.belicza.andras.util.context.IContext;

import java.io.File;

import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderPresenter;

/**
 * Row context interface which is associated with a row in the table of {@link FolderPresenter}.
 * 
 * <p>When a path is processed to create a row in the folder presenter table, a row context is created for it.<br>
 * This row context instance is then shared between the columns, so columns may store and retrieve intermediate results
 * from this context to speed up the construction of the column data objects.</p>
 * 
 * <p><i>The column execution order is not specified and columns should not rely on that.</i><br>
 * If 2 column implementations need the same intermediate result which they share in the row context,
 * they both have to check whether this value is stored, and if not, create it (and store it).<br>
 * This is best done by putting the logic which creates the intermediate result to a method
 * available all of the columns that need it.<br>
 * <p>Example of this:
 * <blockquote><pre>
 * private static final Object KEY = "someKey";
 * 
 * String getSharedData( Path path, BasicFileAttributes attrs, IRowContext rowContext ) {
 *     String sharedData = rowContext.get( KEY, String.class );
 *     if ( sharedData == null ) {
 *         sharedData = "calculate shared data";
 *         rowContext.put( KEY, sharedData );
 *     }
 *     return sharedData;
 * }
 * </pre></blockquote></p>
 * </p>
 * 
 * <p>The row context also has some built-in support for accessing some predefined shared values.
 * They are associated with the values of the {@link SharedDataKey} enum, and they can be created/retrieved
 * with the proper methods (e.g. {@link #getSharedFile()}).</p>
 * 
 * <p>If an intermediate result is constructed by (partially) reading the content of the path, 
 * then it is <strong>strongly</strong> recommended to share the intermediate result in the row context and not read the content
 * of the path by all of the columns that (may) need it. This is the case for content related file type specific column implementations.</p>
 * 
 * <p><b>Use case #1:</b><br>
 * A file name column (file name without extension) and a file extension column can be implemented in a way
 * that they -if not yet stored in the row context- first construct the file name and store it.
 * Then the file name column can return the first part of it (the file name without extension part),
 * the extension column can return the extension part of it.</p>
 * 
 * <p><b>Use case #2:</b><br>
 * An image width column and an image height column can be implemented in a way
 * that they -if not yet stored in the row context- first read the image metadata from the content of the path and store it.
 * This image metadata should contain both the image width and height (once an image is opened and processed, both of these
 * data should be available at no extra cost). Then the image width column can return the image width,
 * and the image height column can return the image height properly.</p>
 * 
 * @author Andras Belicza
 */
public interface IRowContext extends IContext {
	
	/**
	 * Returns the lazily created shared file object representing the path associated with the row context.
	 * 
	 * <p>If present, the file will be returned immediately.
	 * If not, it will be created and stored first (so it can be returned immediately in future calls).</p>
	 * 
	 * <p>The shared file is associated with the {@link SharedDataKey#FILE} key.</p> 
	 * 
	 * @return a file object representing the specified path
	 */
	File getSharedFile();
	
	/**
	 * Returns the lazily created shared file name of the path associated with the row context.
	 * 
	 * <p>If present, the file name will be returned immediately.
	 * If not, it will be created and stored first (so it can be returned immediately in future calls).</p>
	 * 
	 * <p>The shared file name is associated with the {@link SharedDataKey#FILE_NAME} key.</p> 
	 * 
	 * @return the file name of the specified path
	 */
	String getSharedFileName();
	
}
