/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.service.icon;

import hu.belicza.andras.util.Utils;

import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import com.abdracmd.Consts;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.util.OperatingSystem;

/**
 * Icons.
 * 
 * <p>Provides lazily initialized/created icon management and access.
 * Since {@link XIcon}s are the keys, we'll know at startup if icons are missing.
 * Since icons are immutable, we'll share already created icons.</p>
 * 
 * @author Andras Belicza
 */
public class Icons {
	
	/**
	 * Extension - system icon map. We cache system icons here.
	 * This is a required performance boost because {@link FileSystemView} does not implement any caching...
	 * Without this getting the same icon (even for the same file not just for the same type) will result in a newly allocated image icon.
	 */
	private static final Map< String, Icon > EXT_SYSTEM_ICON_MAP = new HashMap<>();
	
	/**
	 * Root - system icon map. We cache system icons here.
	 * This is a required performance boost because {@link FileSystemView} does not implement any caching...
	 * Without this getting the same icon (even for the same file not just for the same type) will result in a newly allocated image icon.
	 */
	private static final Map< String, Icon > ROOT_SYSTEM_ICON_MAP = new HashMap<>();
	
	/** Extensions for which do not cache system icon. */
	private static final Set< String > NON_CACHABLE_EXT_SET = new HashSet<>();
	static {
		if ( Consts.OS == OperatingSystem.WINDOWS ) {
			Collections.addAll( NON_CACHABLE_EXT_SET, "exe", "Exe", "eXe", "exE", "EXe", "ExE", "eXE", "EXE" );
			Collections.addAll( NON_CACHABLE_EXT_SET, "ico", "Ico", "iCo", "icO", "ICo", "IcO", "iCO", "ICO" );
		}
	}
	
	/** Reference to the file system view. */
	private static final FileSystemView FSV = FileSystemView.getFileSystemView();
	
	/** Cache of the folder system icon. */
	private static final Icon FOLDER_SYSTEM_ICON;
	
	
	static {
		// Pre-cache folder system icon
		final Path tempDir = Consts.PATH_TEMP_DIR.resolve( Double.toString( Math.random() ).replace( '.', '_' ) );
		
		try {
			Files.createDirectories( tempDir );
		} catch ( final IOException ie ) {
			MainShell.INSTANCE.error( "Could not create temp directory: " + Utils.getPathString( tempDir ), ie );
		}
		FOLDER_SYSTEM_ICON = FSV.getSystemIcon( tempDir.toFile() );
		try {
			Files.deleteIfExists( tempDir );
		} catch ( final IOException ie ) {
			MainShell.INSTANCE.error( "Could not delete temp directory: " + Utils.getPathString( tempDir ), ie );
		}
		
		// Pre-cache empty (no-extension) system icon
		EXT_SYSTEM_ICON_MAP.put( Utils.EMPTY_STRING, getExtensionSystemIcon( Utils.EMPTY_STRING ) );
	}
	
	/**
	 * Returns the system icon associated with the specified extension.
	 * A temp file is created with the specified extension, and then its system icon is queried.
	 * @param ext extension whose associated system icon to be returned
	 * @return the system icon associated with the specified extension
	 */
	private static Icon getExtensionSystemIcon( final String ext ) {
		final String fileNameWithoutExt = Double.toString( Math.random() ).replace( '.', '_' );
		final Path tempFile = Consts.PATH_TEMP_DIR.resolve( fileNameWithoutExt + ( ext.isEmpty() ? Utils.EMPTY_STRING : "." + ext ) );
		
		try {
			Files.createFile( tempFile );
		} catch ( final IOException ie ) {
			MainShell.INSTANCE.error( "Could not create temp file: " + Utils.getPathString( tempFile ), ie );
		}
		
		final Icon systemIcon = FSV.getSystemIcon( tempFile.toFile() );
		
		try {
			Files.delete( tempFile );
		} catch ( final IOException ie ) {
			MainShell.INSTANCE.error( "Could not delete temp file: " + Utils.getPathString( tempFile ), ie );
		}
		
		return systemIcon;
	}
	
	/**
	 * Returns the system icon associated with the specified path.
	 * @param path  path to return the system icon for
	 * @param attrs optional basic attributes of the path
	 * @return the system icon associated with the specified path
	 */
	public static Icon getSystemIcon( final Path path, final BasicFileAttributes attrs ) {
	    Icon icon;
	    
		if ( Utils.isVirtualFS( path ) ) {
			// Virtual FS: the path does not exists in the normal FS
		    if ( attrs.isDirectory() )
				icon = FOLDER_SYSTEM_ICON;
		    else {
		    	// It's a file
		    	final String ext = Utils.getFileExt( path );
		    	if ( NON_CACHABLE_EXT_SET.contains( ext ) )
		    		icon = EXT_SYSTEM_ICON_MAP.get( Utils.EMPTY_STRING ); // Return empty icon (associated with no-extension)
		    	else {
	    			if ( ( icon = EXT_SYSTEM_ICON_MAP.get( ext ) ) == null )
	    				EXT_SYSTEM_ICON_MAP.put( ext, icon = getExtensionSystemIcon( ext ) );
		    	}
		    }
		}
		else {
			// Normal file system
			
			// Root (nameCount == 0) is not always directory (isDirectory() == false), example is a non-ready DVD drive...
		    if ( path.getNameCount() == 0 ) {
				// It's a root
	   			if ( ( icon = ROOT_SYSTEM_ICON_MAP.get( path.toString() ) ) == null )
	   				EXT_SYSTEM_ICON_MAP.put( path.toString(), icon = FSV.getSystemIcon( path.toFile() ) );
			}
		    else if ( attrs.isDirectory() ) {
		    	// It's a directory
				icon = FOLDER_SYSTEM_ICON;
		    }
		    else {
		    	// It's a file
		    	final String ext = Utils.getFileExt( path );
		    	
		    	if ( NON_CACHABLE_EXT_SET.contains( ext ) )
		    		icon = FSV.getSystemIcon( path.toFile() );
		    	else {
	    			if ( ( icon = EXT_SYSTEM_ICON_MAP.get( ext ) ) == null )
	    				EXT_SYSTEM_ICON_MAP.put( ext, icon = FSV.getSystemIcon( path.toFile() ) );
		    	}
		    }
		}
	    
		return icon;
	}
	
	/**
	 * Returns a null icon (which does not paint anything) in the specified size.
	 * @param width  width of the null icon to be returned
	 * @param height height of the null icon to be returned
	 * @return a null icon in the specified size
	 */
	public static Icon getNullIcon( final int width, final int height ) {
		return new Icon() {
			@Override
			public void paintIcon( final Component c, final Graphics g, final int x, final int y ) {
				// By definition we do nothing
			}
			@Override
			public int getIconWidth() {
				return width;
			}
			@Override
			public int getIconHeight() {
				return height;
			}
		};
	}
	
	
	/** Internal cache of lazily loaded image icons for xicons. */
	private final Map< XIcon, ImageIcon >  xiconImageIconMap   = new EnumMap<>( XIcon.class );
	
	/** Internal cache of lazily loaded language icons for language names. */
	private final Map< String, Icon >      languageNameIconMap = new HashMap<>();
	
	/**
	 * Returns the lazily loaded image icon for the specified XIcon.
	 * @param xicon image icon key
	 * @return the image icon for the specified XIcon
	 */
	public ImageIcon get( final XIcon xicon ) {
		ImageIcon imageIcon = xiconImageIconMap.get( xicon );
		
		if ( imageIcon == null && xicon.resource != null )
			xiconImageIconMap.put( xicon, imageIcon = new ImageIcon( xicon.resource ) );
		
		return imageIcon;
	}
	
	/**
	 * Returns the lazily loaded icon for the specified language.
	 * @param languageName language name to return the icon for
	 * @return the icon for the specified language
	 */
	public Icon getLanguageIcon( final String languageName ) {
		Icon icon = languageNameIconMap.get( languageName );
		
		if ( icon == null ) {
			final URL iconResourceUrl = Icons.class.getResource( "language/" + languageName + ".gif" );
			languageNameIconMap.put( languageName, icon = iconResourceUrl == null ? getNullIcon( 18, 12 ) : new ImageIcon( iconResourceUrl ) );
		}
		
		return icon;
	}
	
}
