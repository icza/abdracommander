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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

import javax.swing.JPanel;

import com.abdracmd.fileop.IExecContext;
import com.abdracmd.fileop.impl.base.BaseFileOp;
import com.abdracmd.fileop.impl.base.ExecResult;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.util.AcUtils;

/**
 * Copy file operation.
 * 
 * @author Andras Belicza
 */
public class CopyFileOp extends BaseFileOp {
	
    /**
     * Creates a new CopyFileOp.
     */
    public CopyFileOp() {
    	super( XIcon.COPY, TextKey.FILE_OP$COPY$NAME, TextKey.FILE_OP$COPY$MAIN_TASK, true );
    }
    
	@Override
	public String getConfirmationMessage( final Path[] inputPaths, final IExecContext execContext ) {
		return inputPaths.length == 1 ? AcUtils.CAH.get( TextKey.FILE_OP$COPY$CONFIRM_MSG )
				: AcUtils.CAH.get( TextKey.FILE_OP$COPY$CONFIRM_MSG2, inputPaths.length );
	}
    
	@Override
	public String suggestSingleDestinationFileName( final Path[] inputPaths, final IExecContext execContext ) {
		return inputPaths.length == 1 && Files.isRegularFile( inputPaths[ 0 ] ) ? Utils.getFileName( inputPaths[ 0 ] ) : null;
	}
	
	@Override
	public void addCustomSettingComponents( final JPanel panel, final GridBagConstraints c, final IExecContext execContext ) {
    	// TODO Setting for copy: "Keep date, attrs?"
	}
	
    @Override
    protected ExecResult execute( final Path file, final BasicFileAttributes attrs, final IExecContext execContext ) {
    	final Path outputFile = execContext.deriveOutputPath( file );
    	execContext.publishOutputPath( outputFile );
    	
    	// High is very good: not only bigger buffer, but native copy will be used more often!
    	final int bufferSize = execContext.getSuggestedBufferSize();
    	
    	final ActionWhenFileExists actionWhenFileExists = execContext.getActionWhenFileExists();
    	
    	try {
	    	// If file size fits in the buffer, use the native copy method which is faster.
	    	// We don't lose progress indication or state listening (e.g. suspended, cancelled)
	    	// Because those are only refreshed and checked after a buffer is processed!
    		
	    	if ( attrs.size() <= bufferSize ) {
	    		final CopyOption[] copyOptions = actionWhenFileExists == ActionWhenFileExists.OVERWRITE
	    				? new CopyOption[] { StandardCopyOption.REPLACE_EXISTING }
	    				: new CopyOption[] {};
				Files.copy( file, outputFile, copyOptions );
	    	}
	    	else {
	    		final OpenOption[] openOptions = actionWhenFileExists == ActionWhenFileExists.OVERWRITE
	    				? new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE }
	    				: new OpenOption[] { StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE };
		    	try ( final InputStream  in  = Files.newInputStream( file );
		    		  final OutputStream out = Files.newOutputStream( outputFile, openOptions ) ) {
		    		
		        	long copiedBytes = 0;
		    		
		    		final byte[] buffer = execContext.getSharedBuffer( bufferSize );
		    		
		    		int bytesRead;
		    		
		    		while ( !execContext.isCancelRequested() ) {
		    			if ( execContext.waitIfSuspended() )
		    				continue; // If execution was suspended, "continue" so cancellation will be checked again first
		    			
		    			if ( ( bytesRead = in.read( buffer ) ) < 0 )
		    				break;
		    			out.write( buffer, 0, bytesRead );
		    			
		    			execContext.publishProgress( copiedBytes += bytesRead );
		    		}
				}
	    	}
		} catch ( final FileAlreadyExistsException fae ) {
    		return execContext.getSharedExecResult().setFileExists( file, outputFile );
		} catch ( final IOException ie ) {
			AcUtils.CAH.debug( "Error copying file: " + Utils.getPathString( file ) + " to: " + Utils.getPathString( outputFile ), ie );
			return execContext.getSharedExecResult().setError( file, outputFile, AcUtils.CAH.get( TextKey.FILE_OP$COPY$ERR$COULD_NOT_COPY_FILE ) );
		}
    	
		return null;
    }
    
	@Override
	protected ExecResult preVisitFolder( final Path folder, final BasicFileAttributes attrs, final IExecContext execContext ) {
    	final Path outputFolder = execContext.deriveOutputPath( folder );
    	execContext.publishOutputPath( outputFolder );
    	
		try {
			if ( !Files.exists( outputFolder ) )
				Files.createDirectory( outputFolder );
			return null;
		} catch ( final IOException ie ) {
			AcUtils.CAH.debug( "Error creating folder: " + Utils.getPathString( outputFolder ), ie );
			return execContext.getSharedExecResult().setError( folder, outputFolder, AcUtils.CAH.get( TextKey.FILE_OP$COPY$ERR$COULD_NOT_CREATE_FOLDER ) );
		}
	}
	
	@Override
	protected ExecResult postVisitFolder( final Path folder, final IExecContext execContext ) {
		return null;
	}
	
}
