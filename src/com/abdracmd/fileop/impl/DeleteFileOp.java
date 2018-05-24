/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.fileop.impl;

import hu.belicza.andras.util.Utils;

import java.awt.GridBagConstraints;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.abdracmd.fileop.IExecContext;
import com.abdracmd.fileop.impl.base.BaseFileOp;
import com.abdracmd.fileop.impl.base.ExecResult;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;

/**
 * Delete file operation.
 * 
 * @author Andras Belicza
 */
public class DeleteFileOp extends BaseFileOp {
	
	/** Key of the wipe delete check box.             */
	private static final Object KEY_WIPE_DELETE_CHECKBOX = new Object();
	/** Key of the wipe delete {@link Boolean} value. */
	private static final Object KEY_WIPE_DELETE          = new Object();
	/** Key of the randomly initialized buffer.       */
	private static final Object KEY_INITIALIZED_BUFFER   = new Object();
	
    /**
     * Creates a new DeleteFileOp.
     */
    public DeleteFileOp() {
    	super( XIcon.DELETE, TextKey.FILE_OP$DELETE$NAME, TextKey.FILE_OP$DELETE$MAIN_TASK, false );
    }
    
	@Override
	public String getConfirmationMessage( final Path[] inputPaths, final IExecContext execContext ) {
		return inputPaths.length == 1 ? AcUtils.CAH.get( TextKey.FILE_OP$DELETE$CONFIRM_MSG )
				: AcUtils.CAH.get( TextKey.FILE_OP$DELETE$CONFIRM_MSG2, inputPaths.length );
	}
    
	@Override
	public String suggestSingleDestinationFileName( final Path[] inputPaths, final IExecContext execContext ) {
		return null;
	}
	
	@Override
	public void addCustomSettingComponents( final JPanel panel, final GridBagConstraints c, final IExecContext execContext ) {
    	// TODO "Wipe delete (destroy data before delete)", "Move to trash"
		
   		// Wipe delete check box
		c.gridy++;
   		c.gridwidth = GridBagConstraints.REMAINDER;
   		final JCheckBox wipeDeleteCheckBox = GuiUtils.createSettingCheckBox( SettingKeys.FILE_OP$DELETE$WIPE_DELETE, null );
   		panel.add( wipeDeleteCheckBox, c );
   		c.gridwidth = 1;
   		
   		// Store check box in the context:
   		execContext.put( KEY_WIPE_DELETE_CHECKBOX, wipeDeleteCheckBox );
	}
	
	/**
	 * Returns the need of a wipe delete.
	 * @param execContext execution context
	 * @return true if wipe delete is needed, false otherwise
	 */
	private boolean wipeDelete( final IExecContext execContext ) {
		Boolean wipeDelete = execContext.get( KEY_WIPE_DELETE, Boolean.class );
		
		if ( wipeDelete == null )
			execContext.put( KEY_WIPE_DELETE, wipeDelete = Boolean.valueOf( execContext.get( KEY_WIPE_DELETE_CHECKBOX, JCheckBox.class ).isSelected() ) );
		
		return Boolean.TRUE == wipeDelete;
	}
	
	/**
	 * Returns a randomly initialized buffer.
	 * 
	 * @param execContext execution context
	 * 
	 * @return the pattern-initialized buffer
	 */
	private byte[] getInitializedBuffer( final IExecContext execContext ) {
		byte[] buffer = execContext.get( KEY_INITIALIZED_BUFFER, byte[].class );
		
		if ( buffer == null ) {
			execContext.put( KEY_INITIALIZED_BUFFER, buffer = execContext.getSharedBuffer( execContext.getSuggestedBufferSize() ) );
			new Random().nextBytes( buffer );
		}
		
		return buffer;
	}
	
    @Override
    protected ExecResult execute( final Path file, final BasicFileAttributes attrs, final IExecContext execContext ) {
    	try {
    		if ( wipeDelete( execContext ) && Files.exists( file ) ) {
				final byte[] buffer = getInitializedBuffer( execContext );
				final int halfSize = buffer.length >> 1;
				
    			// 2 cycles: first we overwrite the file content with the first half of the buffer,
				// then with the second half of the buffer.
				// I purposefully don't check cancel request here. Wipe is really fast, and do not leave a file half-junked...
				for ( int offset = 0; offset < buffer.length; offset += halfSize ) {
	    			try ( final SeekableByteChannel out = Files.newByteChannel( file, StandardOpenOption.WRITE ) ) {
	    		    	long remaining = attrs.size();
	    		    	while ( remaining > 0 ) {
	    		    		// can't really use Math.min() because remaining is long (and not int)
	    		    		final ByteBuffer bb = ByteBuffer.wrap( buffer, offset, remaining >= halfSize ? halfSize : (int) remaining );
	    		    		remaining -= bb.remaining();
	    		    		// write() does not necessarily writes all available (remaining) bytes:
	    		    		while ( bb.hasRemaining() )
	    		    			out.write( bb );
	    		    	}
	    			}
    			}
    			// TODO also "wipe" file name by renaming it a random name (with same length), and then delete
    		}
    		
			Files.deleteIfExists( file );
			
			return null;
		} catch ( final IOException ie ) {
			AcUtils.CAH.debug( "Error while deleting file: " + Utils.getPathString( file ), ie );
			return execContext.getSharedExecResult().setError( file, null, AcUtils.CAH.get( TextKey.FILE_OP$DELETE$ERR$COULD_NOT_DEL_FILE ) );
		}
    }
    
	@Override
	protected ExecResult preVisitFolder( final Path folder, final BasicFileAttributes attrs, final IExecContext execContext ) {
		return null;
	}
	
	@Override
	protected ExecResult postVisitFolder( final Path folder, final IExecContext execContext ) {
    	try {
			Files.deleteIfExists( folder );
			return null;
		} catch ( final IOException ie ) {
			AcUtils.CAH.debug( "Error while deleting folder: " + Utils.getPathString( folder ), ie );
			return execContext.getSharedExecResult().setError( folder, null, AcUtils.CAH.get( TextKey.FILE_OP$DELETE$ERR$COULD_NOT_DEL_FOLDER ) );
		}
	}
	
}
