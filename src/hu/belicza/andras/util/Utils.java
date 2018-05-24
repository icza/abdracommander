/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * General utilities used in Abdra Commander but not related to Abdra Commander.
 * 
 * @author Andras Belicza
 */
public class Utils {
	
	// Frequently used string constants to recude string allocations...
	
	/** Empty string.                                              */
	public static final String    EMPTY_STRING             = "";
	/** A string containing a space.                               */
	public static final String    SPACE_STRING             = " ";
	/** A string containing a colon.                               */
	public static final String    COLON_STRING             = ":";
	/** A string containing a colon and a space.                   */
	public static final String    COLON_SPACE_STRING       = ": ";
	/** A string containing a space and an (opening) parenthesis.  */
	public static final String    SPACE_PARENTHESIS_STRING = " (";
	/** A value-max separator string, functioning as "x out of y". */
	public static final String    OUT_OF_STRING            = " / ";
	
	/** Empty array.                                   */
	public static final Object[]  EMPTY_ARRAY  = new Object[ 0 ];
	/**  Empty set, unmodifiable.                      */
	public static final Set< ? >  EMPTY_SET    = Collections.unmodifiableSet ( new HashSet<>  () );
	/**  Empty set, unmodifiable.                      */
	public static final List< ? > EMPTY_LIST   = Collections.unmodifiableList( new ArrayList<>() );
	/** Digits used in the hexadecimal representation. */
	public static final char[]    HEX_DIGITS   = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	
	/**
	 * Tests if the specified value is contained in the specified array.
	 * @param array array to search in
	 * @param v value to be looked for
	 * @return true if the value is contained in the specified array; false otherwise
	 */
	public static <T> boolean contains( final T[] array, final T v ) {
		for ( final T e : array )
			if ( e == v || v != null && v.equals( e ) )
				return true;
		
		return false;
	}
	
	/**
	 * Returns the path string ({@link Path#toString()}) of the specified path.
	 * 
	 * <p>This method handles exceptions thrown by {@link Path#toString()} and returns <code>"!error!"</code>.</p> 
	 * 
	 * @param path path whose path string to be returned
	 * @return the path string ({@link Path#toString()}) of the specified path.
	 */
	public static String getPathString( final Path path ) {
		try {
			return path.toString();
		} catch ( final Exception e ) {
			return "!error!";
		}
	}
	
	/**
	 * Returns the file name (name+ext) of the specified file.
	 * @param path file whose file name to be returned
	 * @return the file name of the specified file
	 */
	public static String getFileName( final Path path ) {
		final Path fileNamePath = path.getFileName();
		
		// ZipFileSystem might throw IllegalArgumentException if a zip entry's name is not a valid UTF-8 name...
		try {
			return getPathString( fileNamePath == null ? path : fileNamePath );
		} catch ( final Exception e ) {
			return "!error!";
		}
	}
	
	/**
	 * Returns the file extension of the specified file path.<br>
	 * If file name starts with a dot, it is not treated as name-extension separator.
	 * 
	 * @param path file path whose file extension to be returned
	 * @return the file extension of the specified file path ({@link #EMPTY_STRING} if the specified file name has no extension part)
	 * 
	 * @see #getFileExt(String)
	 */
	public static String getFileExt( final Path path ) {
		return getFileExt( getFileName( path ) );
	}
	
	/**
	 * Returns the extension part of the specified file name.<br>
	 * If file name starts with a dot, it is not treated as name-extension separator.
	 * 
	 * @param fileName file name whose file extension part to be returned
	 * @return the extension part of the specified file name ({@link #EMPTY_STRING} if the specified file name has no extension part)
	 * 
	 * @see #getFileExt(Path)
	 */
	public static String getFileExt( final String fileName ) {
		final int lastDotIdx = fileName.lastIndexOf( '.' );
		// If file name starts with a dot, do not treat that as name-extension separator
		return lastDotIdx > 0 ? fileName.substring( lastDotIdx + 1 ) : EMPTY_STRING;
	}
	
	/**
	 * Parses a dynamic values from a string source.
	 * 
	 * <p>Parsing exceptions are intentionally not caught and thrown.</p>
	 * 
	 * @param type  type of the value to parse
	 * @param value string value to be parsed
	 * @return the parsed value or <code>null</code> if <code>type</code> is not supported
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" } )
    public static < T > T simpleValueOf( final Class< T > type, final String value ) {
		final T retValue;
		
		// Try in frequency order
		if ( type == String.class )
			retValue = (T) value;
		else if ( type == Integer.class )
			retValue = (T) Integer.valueOf( value );
		else if ( type == Boolean.class )
			retValue = (T) Boolean.valueOf( value );
		else if ( type.getSuperclass() == Enum.class )
			retValue = (T) Enum.valueOf( (Class< ? extends Enum >) type, value );
		else if ( type == Long.class )
			retValue = (T) Long.valueOf( value );
		else
			retValue = null;
		
		return retValue;
	}
	
	/**
	 * Converts a value to a string.
	 * <p>Basically returns {@link Object#toString()}, but for enums returns the {@link Enum#name()}.</p>
	 * @param value value to be converted to string
	 * @return the string representation of the specified value
	 */
    public static String simpleToString( final Object value ) {
		if ( value.getClass().getSuperclass() == Enum.class )
			return ( (Enum< ? >) value ).name();
		else
			return value.toString();
	}
	
	/**
	 * Returns a new, independent list which contains all the specified elements.
	 * @param elements elements to be added to the new list
	 * @return an independent list which does not rely on the <code>elements</code> array
	 */
	@SafeVarargs
    public static < T > List< T > asNewList( final T... elements ) {
		final List< T > list = new ArrayList<>( elements.length );
		
		for ( final T element : elements )
			list.add( element );
		
		return list;
	}
	
	/**
	 * Returns a new, independent set which contains all the specified elements.
	 * @param elements elements to be added to the new set
	 * @return an independent set which does not rely on the <code>elements</code> array
	 */
	@SafeVarargs
    public static < T > Set< T > asNewSet( final T... elements ) {
		final Set< T > set = new HashSet<>( elements.length );
		
		for ( final T element : elements )
			set.add( element );
		
		return set;
	}
	
	/**
	 * Returns an unmodifiable empty set.
	 * @return an unmodifiable empty set
	 */
	@SuppressWarnings( "unchecked" )
    public static < T > Set< T > getEmptySet() {
		return (Set< T >) EMPTY_SET;
	}
	
	/**
	 * Returns an unmodifiable empty list.
	 * @return an unmodifiable empty list
	 */
	@SuppressWarnings( "unchecked" )
    public static < T > List< T > getEmptyList() {
		return (List< T >) EMPTY_LIST;
	}
	
	/**
	 * Creates an HTML link with the specified text. 
	 * @param text text to be displayed for the link
	 * @return an HTML link
	 */
	public static String createHtmlLink( final String text ) {
		return text == null ? null : "<html><a href='#'>" + text.replace( SPACE_STRING, "&nbsp;" ).replace( "<", "&lt;" ).replace( ">", "&gt;" ) + "</a></html>";
	}
	
	/**
	 * Converts the specified data to hex string.
	 * 
	 * @param data  data to be converted
	 * @param delim optional delimiter between hex representation of bytes (2 hex digits)
	 * 
	 * @return the specified data converted to hex string
	 */
	public static String toHexString( final byte[] data, final String delim ) {
		final StringBuilder hexBuilder = new StringBuilder( data.length << 1 );
		
		for ( final byte b : data ) {
			if ( delim != null && hexBuilder.length() > 0 )
				hexBuilder.append( delim );
			hexBuilder.append( HEX_DIGITS[ ( b & 0xff ) >> 4 ] ).append( HEX_DIGITS[ b & 0x0f ] );
		}
		
		return hexBuilder.toString();
	}
	
	/**
	 * Decodes a Base64 string.
	 * 
	 * @param base64 Base64 string to be decoded
	 * @return the decoded content of the Base64 string; or <code>null</code> if <code>base64</code> is not a valid Base64 string
	 * 
	 * @see #encodeToBase64String(byte[])
	 */
	public static byte[] decodeBase64String( final String base64 ) {
		try {
			return javax.xml.bind.DatatypeConverter.parseBase64Binary( base64 );
		} catch ( final IllegalArgumentException iae ) {
			return null;
		}
	}
	
	/**
	 * Encodes a byte array to a Base64 string.
	 * 
	 * @param data data to be encoded
	 * @return the Base64 string of the encoded byte array
	 * 
	 * @see #decodeBase64String(String)
	 */
	public static String encodeToBase64String( final byte[] data ) {
		return javax.xml.bind.DatatypeConverter.printBase64Binary( data );
	}
	
	/**
	 * Returns the image data in JPEG format.
	 * @param image image whose data to be returned
	 * @return JPEG image data or <code>null</code> if some error occurs
	 */
	public static byte[] getImageJpegData( final Image image ) {
		final RenderedImage renderedImage;
		
		if ( image instanceof RenderedImage )
			renderedImage = (RenderedImage) image;
		else {
			if ( image.getWidth( null ) < 0 || image.getHeight( null ) < 0 ) {
				// Wait for the image to load, we need the image width and height:
				new ImageIcon( image );
			}
			
			final BufferedImage bi = new BufferedImage( image.getWidth( null ), image.getHeight( null ), BufferedImage.TYPE_3BYTE_BGR );
			final Graphics2D g2 = bi.createGraphics();
			g2.drawImage( image, 0, 0, null );
			g2.dispose();
			renderedImage = bi;
		}
		
		try ( final ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
			ImageIO.write( renderedImage, "jpg", buffer );
			return buffer.toByteArray();
		} catch ( final IOException ie ) {
			return null;
		} finally {
			if ( renderedImage != image )
				( (BufferedImage) renderedImage ).flush();
		}
	}
	
	/**
	 * Tests whether the specified text is a URL.
	 * A text is considered a URL if it starts with <code>"http://"</code> or <code>"https://"</code>.
	 *  
	 * @param text text to be tested
	 * @return true if the specified text is a link; false otherwise
	 */
	public static boolean isUrlText( final String text ) {
		return text.startsWith( "http://" ) || text.startsWith( "https://" );
	}
	
	/**
	 * Tells if the specified path is on a virtual file system or normal file system.
	 * @param path path to be tested
	 * @return true if the specified path is on a virtual file system; false otherwise (normal file system)
	 */
	public static boolean isVirtualFS( final Path path ) {
    	return !"file".equalsIgnoreCase( path.getFileSystem().provider().getScheme() );
	}
	
	/**
	 * Returns the source file of a virtual file system.
	 * @param path a path being on a virtual file system
	 * @return the path specifying the source file of the virtual file system
	 */
	public static Path getVirtualFSSource( final Path path ) {
		return Paths.get( path.getFileSystem().getFileStores().iterator().next().name() );
	}
	
	/**
	 * Returns the parent path of the specified path.<br>
	 * The implementation is virtual FS aware: if the specified path is the root path inside of a VFS,
	 * the parent of the source path of the VFS will be returned.
	 * 
	 * @param path path whose parent to be returned
	 * @return the parent path of the specified path
	 */
	public static Path getVirtualAwareParent( final Path path ) {
    	Path parent = path.getParent();
    	
        if ( parent == null ) {
        	// We might be inside a virtual FS:
	    	if ( isVirtualFS( path ) ) {
	    		// We're at the root of the VFS, add a "go to parent folder" which takes us out of the VFS:
	        	parent = getVirtualFSSource( path ).getParent();
	    	}
        }
        
        return parent;
	}
	
	/**
	 * Casts the specified object to the required type.
	 * 
	 * <p>This utility method is here to suppress <i>unchecked cast</i> warnings...</p>
	 * 
	 * @param o   object to be casted
	 * @param <T> type to cast the specified object to
	 * @return the specified object, casted
	 */
	@SuppressWarnings("unchecked")
	public static < T > T cast( final Object o ) {
		return (T) o;
	}
	
}
