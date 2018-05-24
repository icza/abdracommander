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

import hu.belicza.andras.util.IntHolder;
import hu.belicza.andras.util.LongHolder;
import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.bean.BaseHasDisplayName;
import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.abdracmd.fileop.IExecContext;
import com.abdracmd.fileop.IFileOp;
import com.abdracmd.fileop.impl.ActionOnError;
import com.abdracmd.fileop.impl.ActionWhenFileExists;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.options.OptionsShell;
import com.abdracmd.smp.dialog.options.OptionsShell.BodyIcon;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;

/**
 * Base file operation with some help to implement file operations.
 * 
 * <p>This class implements the {@link IFileOp#execute(Path[], IExecContext)} method.
 * The implementation first counts and publishes the total files, then iterates over all files and folders,
 * and calls {@link #preVisitFolder(Path, BasicFileAttributes, IExecContext)} and
 * {@link #postVisitFolder(Path, IExecContext)} with folders and
 * {@link #execute(Path, BasicFileAttributes, IExecContext)} with files.<br>
 * <br>
 * Example of a proper {@link #execute(Path, BasicFileAttributes, IExecContext)} implementation
 * which uses output paths ({@link #usesDestinationPath} returns <code>true</code>):
 * <blockquote><pre>
 * protected ExecResult execute( Path file, BasicFileAttributes attrs, IExecContext execContext ) {
 *     final Path outputPath = deriveOutputPath( file, execContext );
 *     execContext.publishOutputPath( outputPath );
 *     
 *     try {
 *         long processedSize = 0;
 *         
 *         while ( !execContext.isCancelRequested() ) {
 *             if ( execContext.waitIfSuspended() )
 *                 continue; // If execution was suspended, "continue" so cancellation will be checked again first
 *             
 *             if ( done() )
 *                 break;
 *             
 *             processedSize += doSomeWork();
 *             
 *             execContext.publishProgress( processedSize );
 *         }
 *         
 *         return null; // OK
 *     } catch ( final IOException ie ) {
 *         // Log exception and then return error
 *         return execContext.getSharedExecResult().setError( "Some error" );
 *     }
 * }
 * </pre></blockquote></p>
 * </p>
 * 
 * @author Andras Belicza
 */
public abstract class BaseFileOp extends BaseHasDisplayName implements IFileOp {
	
	/** Image icon of the file operation. */
	protected final ImageIcon imageIcon;
	
	/** Tells if the file operation uses the output path. */
	protected final boolean usesDestinationPath;
	
	/** Main task name. */
	protected final String mainTaskName;
	
    /**
     * Creates a new BaseFileOp.
     * 
     * @param xicon               xicon of the file operation
     * @param displayNameTextKey  text key of the display name of the file operation
     * @param mainTaskNameTextKey text key of the main task name of the file operation
     * @param usesDestinationpath tells if the file operation uses the destination path
     */
    public BaseFileOp( final XIcon xicon, final TextKey displayNameTextKey, final TextKey mainTaskNameTextKey, final boolean usesDestinationpath ) {
    	this( MainShell.INSTANCE.getModel().getIcons().get( xicon ),
    			MainShell.INSTANCE.getModel().getLanguage().get( displayNameTextKey ),
    			MainShell.INSTANCE.getModel().getLanguage().get( mainTaskNameTextKey ), usesDestinationpath );
    }
    
    /**
     * Creates a new BaseFileOp.
     * 
     * @param imageIcon     image icon of the file operation
     * @param displayName   display name of the file operation
     * @param mainTaskName  name of the main task of the file operation
     * @param usesDestinationpath tells if the file operation uses the destination path
     */
    public BaseFileOp( final ImageIcon imageIcon, final String displayName, final String mainTaskName, final boolean usesDestinationpath ) {
    	super( displayName );
    	
    	this.imageIcon           = imageIcon;
    	this.usesDestinationPath = usesDestinationpath;
    	this.mainTaskName        = mainTaskName;
    }
    
	@Override
	public ImageIcon getImageIcon() {
		return imageIcon;
	}
    
    @Override
    public boolean usesDestinationPath( final IExecContext execContext ) {
    	return usesDestinationPath;
    }
    
    
    @Override
    public void execute( final Path[] inputPaths, final IExecContext execContext ) {
    	// First count files
    	countFiles( inputPaths, execContext );
    	
    	// And now for action...
    	execContext.publishTaskName( mainTaskName );
    	for ( final Path inputPath : inputPaths ) {
    	    if ( execContext.isCancelRequested() )
    			break;
    	    
		    try {
		    	Files.walkFileTree( inputPath, Utils.< FileVisitOption >getEmptySet(), Integer.MAX_VALUE, new SimpleFileVisitor< Path >() {
		    		
		    		/** Context for processing a path. */
	        	    private final PathContext pathContext = new PathContext();
	        	    
		    		@Override
		    		public FileVisitResult preVisitDirectory( final Path dir, final BasicFileAttributes attrs ) throws IOException {
		        	    ExecResult execResult;
		        	    while ( ( execResult = preVisitFolder( dir, attrs, execContext ) ) != null ) {
			        		if ( handleExecResult( execResult, execContext, pathContext ) )
			        			break;
		        	    }
		        	    postPathJob();
		        	    return execContext.isCancelRequested() ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
		    		}
		    		
		        	@Override
		        	public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
		        		execContext.publishStarted( file, attrs.size() );
		        		
		        	    if ( execContext.waitIfSuspended() )
		        	    	if ( execContext.isCancelRequested() )
		        	    		return FileVisitResult.TERMINATE;
		        		
		        	    ExecResult execResult = null;
		        	    while ( ( execResult = execute( file, attrs, execContext ) ) != null ) {
			        		if ( handleExecResult( execResult, execContext, pathContext ) )
			        			break;
		        	    }
		        		
		        	    postPathJob();
		        	    
		        	    if ( execContext.isCancelRequested() )
		        			return FileVisitResult.TERMINATE;
		        		else {
		        			if ( execResult == null )
		        				execContext.publishFinished();
		        			else
		        				execContext.publishSkipped( execResult.result == ExecResult.Result.ERROR );
		        			return FileVisitResult.CONTINUE;
		        		}
		        	}
		        	
		        	@Override
		        	public FileVisitResult postVisitDirectory( final Path dir, final IOException exc ) throws IOException {
		        	    ExecResult execResult;
		        	    while ( ( execResult = postVisitFolder( dir, execContext ) ) != null ) {
			        		if ( handleExecResult( execResult, execContext, pathContext ) )
			        			break;
		        	    }
		        	    postPathJob();
		        	    return execContext.isCancelRequested() ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
		        	}
		        	
		        	/**
		        	 * Common task to do after processing a path.
		        	 */
		        	private void postPathJob() {
		        	    if ( pathContext.clearDefaultOverwriteAction ) {
		        	    	pathContext.clearDefaultOverwriteAction = false;
		        	    	execContext.setActionWhenFileExists( ActionWhenFileExists.ASK );
		        	    }
		        	    if ( pathContext.clearEditedOutputPath ) {
		        	    	pathContext.clearEditedOutputPath = false;
		        	    	execContext.setEditedOutputPath( null );
		        	    }
		        	}
		        	
		        } );
	        	
	        	if ( !execContext.isCancelRequested() )
	        		execContext.reportInputPathProcessed( inputPath );
	        	
	        } catch ( final IOException ie ) {
	        	AcUtils.beepError();
	        	AcUtils.CAH.warning( "Failed to process files!" );
	        	AcUtils.CAH.debug( "Reason:", ie );
	        }
    	}
    	
    	if ( !execContext.isCancelRequested() )
    		execContext.publishStatus( AcUtils.CAH.get( TextKey.FILE_OP$DONE ) );
    }
    
	/**
	 * Handles a non-OK execution result and decides what to do next.
	 * 
	 * <p>First automated actions to be taken will be checked.
	 * If those do not decide what to do, we will consult the user by calling
	 * {@link #consultUserWithExecResult(ExecResult, IExecContext, PathContext)} in the EDT.</p>
	 * 
	 * @param execResult  execution result to be handled
	 * @param execContext execution context
	 * @param pathContext path context
	 * 
	 * @return true if execution should NOT be repeated; false otherwise
	 */
	private boolean handleExecResult( final ExecResult execResult, final IExecContext execContext, final PathContext pathContext ) {
		// First check how we are on automated action to be taken for the execution result (this doesn't involve GUI elements):
		switch ( execResult.result ) {
		case ERROR :
			switch ( execContext.getActionOnError() ) {
			case SKIP :
				return true;
			case CANCEL :
	    		execContext.requestCancel();
				return true;
			case ASK :
				// It's ASK if we got this far, that's what we're gonna do...
				break;
			default :
				throw new RuntimeException( "Unhandled Action on error: " + execContext.getActionOnError() );
			}
			break;
		case FILE_EXISTS :
			switch ( execContext.getActionWhenFileExists() ) {
			case SKIP :
				return true;
			case ASK :
				// It's ASK if we got this far, that's what we're gonna do...
				break;
			case OVERWRITE :
				// This basically should never happen, because if the action to be taken is overwrite, the exec() method should silently overwrite!
				// (And if the "silent" overwrite fails, it should yield a result of ERROR and not FILE_EXISTS.)
				// But since it didn't, we'll just ask nicely.
				break;
			default:
				throw new RuntimeException( "Unhandled Action when file exists: " + execContext.getActionWhenFileExists() );
			}
			break;
		default:
			throw new RuntimeException( "Unhandled execution result:" + execResult.result );
		}
		
		// We got this far: we need to consult the user. Do in in the EDT because that involves GUI stuff:
    	final boolean[] ret = new boolean[ 1 ];
    	GuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				ret[ 0 ] = consultUserWithExecResult( execResult, execContext, pathContext );
			}
		} );
    	return ret[ 0 ];
	}
	
	/**
	 * Consults the user about what to do with the execution result.
	 * 
	 * <p><i>Must be called from the EDT due to GUI stuff involved!</i></p>
	 * 
	 * @param execResult  execution result to be handled
	 * @param execContext execution context
	 * @param pathContext path context
	 * 
	 * @return true if execution should NOT be repeated; false otherwise
	 */
	private boolean consultUserWithExecResult( final ExecResult execResult, final IExecContext execContext, final PathContext pathContext ) {
		// TODO More options for file exists: "Auto-rename existing file", "Auto-rename copied file"
		
		// We show a dialog, beep if presenter dialog is not active
		if ( !execContext.getPresenterDialog().isActive() )
			AcUtils.beep();
		
		// Common buttons:
		final JButton skipButton    = new JXButton();
		final JButton skipAllButton = new JXButton();
		final JButton cancelButton  = new JXButton();
		GuiUtils.initButton( skipButton   , null, TextKey.FILE_OP$BTN$SKIP     );
		GuiUtils.initButton( skipAllButton, null, TextKey.FILE_OP$BTN$SKIP_ALL );
		GuiUtils.initButton( cancelButton , null, TextKey.GENERAL$BTN$CANCEL   );
		
		// Specific buttons
		JButton retryButton        = null;
		JButton overwriteButton    = null;
		JButton overwriteAllButton = null;
		JButton renameButton       = null;
		
		final List< JButton > buttonList  = new ArrayList<>();
		final List< Object >  messageList = new ArrayList<>();
		switch ( execResult.result ) {
		case ERROR :
			retryButton = new JXButton();
			GuiUtils.initButton( retryButton, null, TextKey.FILE_OP$BTN$RETRY );
			Collections.addAll( buttonList, retryButton, skipButton, skipAllButton, cancelButton );
			// Add Rename button if file op uses output 
			if ( usesDestinationPath ) {
				renameButton = new JXButton();
				GuiUtils.initButton( renameButton, null, TextKey.FILE_OP$BTN$RENAME );
				buttonList.add( renameButton );
			}
			break;
		case FILE_EXISTS :
			overwriteButton    = new JXButton();
			overwriteAllButton = new JXButton();
			renameButton       = new JXButton();
			GuiUtils.initButton( overwriteButton   , null, TextKey.FILE_OP$BTN$OVERWRITE     );
			GuiUtils.initButton( overwriteAllButton, null, TextKey.FILE_OP$BTN$OVERWRITE_ALL );
			GuiUtils.initButton( renameButton      , null, TextKey.FILE_OP$BTN$RENAME        );
			// TODO Should add nice comparison panel showing old and new file attributes like date, size etc.
			messageList.add( AcUtils.CAH.get( TextKey.FILE_OP$FILE_EXISTS ) );
			Collections.addAll( buttonList, overwriteButton, skipButton, skipAllButton, overwriteAllButton, cancelButton, renameButton );
			break;
		}
		
		messageList.addAll( execResult.messageList );
		// Add a new line
		if ( !messageList.isEmpty() )
			messageList.add( Utils.SPACE_STRING );
		
		// Make first message bold
		if ( !messageList.isEmpty() && messageList.get( 0 ) instanceof String )
			messageList.set( 0, GuiUtils.changeFontToBold( new JLabel( (String) messageList.get( 0 ) ) ) );
		
		messageList.add( AcUtils.CAH.get( TextKey.FILE_OP$CURRENT ) + Utils.COLON_STRING );
		messageList.add( Utils.getPathString( execResult.path ) );
		// If rename button is present, present output path as an editable text field, for simplicity :)
		final JTextField outputPathTextField = renameButton == null ? null : new JTextField();
		if ( usesDestinationPath ) {
			messageList.add( Utils.SPACE_STRING );
			final String destinationLabelText = AcUtils.CAH.get( TextKey.FILE_OP$DESTINATION ) + Utils.COLON_STRING;
			if ( outputPathTextField == null ) {
				messageList.add( destinationLabelText );
			}
			else {
				final JPanel p = new JPanel( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
				p.add( new JLabel( destinationLabelText ) );
				p.add( Box.createHorizontalStrut( 30 ) );
				final JLabel revertEditLinkLabel = GuiUtils.createLinkStyledLabel( AcUtils.CAH.get( TextKey.FILE_OP$REVERT_EDIT ) );
				final Runnable revertEditTask = new Runnable() {
					@Override
					public void run() {
						outputPathTextField.setText( Utils.getPathString( execResult.outputPath ) );
						revertEditLinkLabel.setVisible( false );
						if ( SwingUtilities.getRootPane( outputPathTextField ) != null )
							SwingUtilities.getRootPane( outputPathTextField ).setDefaultButton( buttonList.get( 0 ) );
					}
				};
				revertEditTask.run(); // Initialize
				revertEditLinkLabel.addMouseListener( new MouseAdapter() {
					@Override
					public void mousePressed( final MouseEvent event ) {
						revertEditTask.run();
					}
				} );
		    	// Also revert by pressing ESC
				outputPathTextField.addKeyListener( new KeyAdapter() {
		    		@Override
		    		public void keyPressed( final KeyEvent event ) {
		    			if ( event.getKeyCode() == KeyEvent.VK_ESCAPE ) {
		    				// ESC is also used to hide (close) dialogs:
		    				if ( revertEditLinkLabel.isVisible() ) {
		    					event.consume();
		    					revertEditTask.run();
		    				}
		    			}
		    		}
				} );
				p.add( revertEditLinkLabel );
				messageList.add( p );
				
				final JButton renameButton_ = renameButton;
				outputPathTextField.getDocument().addDocumentListener( new DocumentListener() {
					@Override public void removeUpdate( final DocumentEvent event ) { changedUpdate( event ); }
					@Override public void insertUpdate( final DocumentEvent event ) { changedUpdate( event ); }
					@Override
					public void changedUpdate( final DocumentEvent event ) {
						revertEditLinkLabel.setVisible( true );
						SwingUtilities.getRootPane( outputPathTextField ).setDefaultButton( renameButton_ );
					}
				} );
			}
			messageList.add( outputPathTextField == null ? Utils.getPathString( execResult.outputPath ) : outputPathTextField );
		}
		
    	// Present choices
    	final JButton cb =
			OptionsShell.withButtonList( execContext.getPresenterDialog(), getImageIcon().getImage(), getDisplayName(), BodyIcon.QUESTION, messageList,
					buttonList ).getButton();
    	
    	// Process choice
    	
    	// Choices that can be handled general:
    	if ( cb == null || cb == cancelButton ) { // cb == null means ESC => Cancel
    		execContext.requestCancel();
    		return true;
    	}
    	if ( cb == skipButton )
    		return true;
    	if ( cb == renameButton ) {
    		// TODO invalid edited path can cause exception!
    		// TODO also check if not absolute path is specified (resolve to output path instead of the current (most likely AbdraCmd home) path?)
    		// Register to clear edited output path after processing the current file
    		pathContext.clearEditedOutputPath = true;
    		execContext.setEditedOutputPath( Paths.get( outputPathTextField.getText() ) );
    		return false;
    	}
    	
		switch ( execResult.result ) {
		case ERROR :
	    	if ( cb == retryButton )
	    		return false;                                       // Do so..
	    	if ( cb == skipAllButton ) {
	    		execContext.setActionOnError( ActionOnError.SKIP ); // Skip further ones
	    		return true;                                        // And skip this one
	    	}
	    	break;
		case FILE_EXISTS :
	    	if ( cb == skipAllButton ) {
	    		execContext.setActionWhenFileExists( ActionWhenFileExists.SKIP );      // Skip further ones
	    		return true;                                                           // And skip this one
	    	}
	    	if ( cb == overwriteButton || cb == overwriteAllButton ) {
	    		execContext.setActionWhenFileExists( ActionWhenFileExists.OVERWRITE ); // Overwrite further ones
	    		if ( cb == overwriteButton )
	    			pathContext.clearDefaultOverwriteAction = true;                    // Register to clear overwrite action (set back to ASK) after current file
	    		return false;                                                          // And Overwrite this one (during processing it again) 
	    	}
			break;
		}
    	
	    return true;
	}
    
	/**
	 * Counts the files in the specified paths, recursively,
	 * and sets the result to the execution context.
	 * 
	 * @param paths       paths to count the files in, recursively
	 * @param execContext reference to the execution context
	 */
    protected void countFiles( final Path[] paths, final IExecContext execContext ) {
    	execContext.publishTaskName( AcUtils.CAH.get( TextKey.FILE_OP$COUNTING ) );
    	
    	final IntHolder  count = new IntHolder ();
    	final LongHolder size  = new LongHolder();
    	
    	for ( final Path path : paths )
		    try {
		    	Files.walkFileTree( path, Utils.< FileVisitOption >getEmptySet(), Integer.MAX_VALUE, new SimpleFileVisitor< Path >() {
		        	@Override
		        	public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
	        	    	// We're only called with "real" files (no folders)
		        		count.value++;
	        	    	size.value += attrs.size();
		        	    
		        	    execContext.waitIfSuspended();
		        	    
		        	    return execContext.isCancelRequested() ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
		        	}
		        } );
		    	
		    	// TODO
		    	// Publish sub-results like: "Counting... (xxx so far, yyy MB)"
		    	// This should be published after each 1,000 files block
		    	
	        } catch ( final IOException ie ) {
	        	AcUtils.beepError();
		        AcUtils.CAH.warning( "Failed to count files!" );
		        AcUtils.CAH.debug( "Reason:", ie );
	        }
    	
    	execContext.publishTotal( count.value, size.value );
    }
    
    /**
     * Executes the file operation on the specified file.
     * @param file        file to execute the file operation on
     * @param attrs       basic attributes of the file
     * @param execContext execution context
     * @return the execution result of the specified file if issue(s) arose; <code>null</code> if execution succeeded;
     * 		if the method returns due to an execution cancellation, the returned value will not be examined
     */
    protected abstract ExecResult execute( Path file, BasicFileAttributes attrs, IExecContext execContext );
    
    /**
     * Called for a folder before entries in the folder are executed.
     * 
     * @param folder      folder whose entries are about to be executed
     * @param attrs       basic attributes of the folder
     * @param execContext execution context
     * @return the execution result of the specified folder if issue(s) arose; <code>null</code> if execution succeeded;
     * 		if the method returns due to an execution cancellation, the returned value will not be examined
     */
    protected abstract ExecResult preVisitFolder( Path folder, BasicFileAttributes attrs, IExecContext execContext );
	
    /**
     * Called for a folder after all its entries have been executed successfully, recursively.
     * 
     * @param folder      folder whose entries have been executed successfully, recursively
     * @param execContext execution context
     * @return the execution result of the specified folder if issue(s) arose; <code>null</code> if execution succeeded;
     * 		if the method returns due to an execution cancellation, the returned value will not be examined
     */
    protected abstract ExecResult postVisitFolder( Path folder, IExecContext execContext );
	
}
