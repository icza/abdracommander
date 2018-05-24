/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder;

import hu.belicza.andras.util.TypeList;
import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.iface.Receiver;
import hu.belicza.andras.util.objectregistry.ObjectRegistry;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import com.abdracmd.action.cmdbar.DeleteAction;
import com.abdracmd.fileop.IFileOp;
import com.abdracmd.fileop.impl.CopyFileOp;
import com.abdracmd.fileop.impl.DeleteFileOp;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.service.settings.SettingSetChangeListener;
import com.abdracmd.smp.BasePresenter;
import com.abdracmd.smp.dialog.options.OptionsShell;
import com.abdracmd.smp.dialog.options.OptionsShell.BodyIcon;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IColumn.Type;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.AccessedColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.AttrColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.CreatedColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.DateColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.ExtColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.IconColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.MimeTypeColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.NameColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.OwnerColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.PathColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.PrioColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.PrioColumn.Priority;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.SelectedColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.SizeColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.TypeDescColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.tableimpl.FolderPresenterTable;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.IFolderPresenterTheme;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl.AccessibilityFolderPresenterTheme;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl.DefaultFolderPresenterTheme;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl.LavaFolderPresenterTheme;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl.NatureFolderPresenterTheme;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl.RetroFolderPresenterTheme;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;
import com.abdracmd.util.SizeFormat;

/**
 * Folder presenter.
 * 
 * Folder content is displayed in a nice table.
 * 
 * @author Andras Belicza
 */
public class FolderPresenter extends BasePresenter< FolderModel, FolderShell, JPanel > implements SettingSetChangeListener {
	
	/** Default go into types, all lower-cased!
	 * @see SettingKeys#FOLDER_TABLE$GO_INTO_FILES_MODE
	 * @see SettingKeys#FOLDER_TABLE$CUSTOM_TYPES_TO_GO_INTO
	 */
	public static final TypeList< String > DEFAULT_LOWERED_GO_INTO_TYPES = new TypeList<>( String.class, "zip", "war" );
	
	
	/** Selected column. */
	private static final SelectedColumn COL_SELECTED = new SelectedColumn();
	/** Priority column. */
	private static final PrioColumn     COL_PRIO     = new PrioColumn();
	/** Path column.     */
	private static final PathColumn     COL_PATH     = new PathColumn();
	/** Icon column.     */
	private static final IconColumn     COL_ICON     = new IconColumn();
	/** Name column.     */
	private static final NameColumn     COL_NAME     = new NameColumn();
	/** Ext column.      */
	private static final ExtColumn      COL_EXT      = new ExtColumn ();
	/** Size column.     */
	private static final SizeColumn     COL_SIZE     = new SizeColumn();
	
	/** Set of internal columns. */
	private static final Set< IColumn< ? > > INTERNAL_COLUMN_SET = new HashSet<>();
	static {
		INTERNAL_COLUMN_SET.add( COL_SELECTED );
		INTERNAL_COLUMN_SET.add( COL_PRIO     );
		INTERNAL_COLUMN_SET.add( COL_PATH     );
	}
	
	/**
	 * Registers the built-in column implementations.
	 */
	public static void registerBuiltinColumnImpls() {
		final ObjectRegistry< String, IColumn< ? > > columnRegistry = MainShell.INSTANCE.getModel().getFolderPresenterColumnRegistry();
		
		// Internal columns
		for ( final IColumn< ? > column : INTERNAL_COLUMN_SET )
			columnRegistry.register( column );
		
		// Required columns
		columnRegistry.register( COL_ICON );
		columnRegistry.register( COL_NAME );
		columnRegistry.register( COL_EXT  );
		columnRegistry.register( COL_SIZE );
		
		// Built-in normal columns
		columnRegistry.register( new AccessedColumn() );
		columnRegistry.register( new AttrColumn    () );
		columnRegistry.register( new CreatedColumn () );
		columnRegistry.register( new DateColumn    () );
		columnRegistry.register( new MimeTypeColumn() );
		columnRegistry.register( new OwnerColumn   () );
		columnRegistry.register( new TypeDescColumn() );
	}
	
	/**
	 * Registers the built-in themes.
	 */
	public static void registerBuiltinThemes() {
		final ObjectRegistry< String, IFolderPresenterTheme > themeRegistry = MainShell.INSTANCE.getModel().getFolderPresenterThemeRegistry();
		
		themeRegistry.register( new DefaultFolderPresenterTheme      () );
		themeRegistry.register( new NatureFolderPresenterTheme       () );
		themeRegistry.register( new RetroFolderPresenterTheme        () );
		themeRegistry.register( new LavaFolderPresenterTheme         () );
		themeRegistry.register( new AccessibilityFolderPresenterTheme() );
		
		// TODO relocate this to main model
		SettingKeys.FOLDER_TABLE$THEME.validValues = themeRegistry.keyList().toArray( new String[ 0 ] );
	}
	
	/**
	 * Registers the built-in file operations.
	 */
	public static void registerFileOps() {
		final ObjectRegistry< String, IFileOp > fielOpRegistry = MainShell.INSTANCE.getModel().getFileOpRegistry();
		
		fielOpRegistry.register( new CopyFileOp  () );
		fielOpRegistry.register( new DeleteFileOp() );
	}
	
	
	/** Parent folder priority. */
	private static final Priority PARENT_FOLDER_PRIO = Priority.PARENT_FOLDER;
	/** Parent folder name.     */
	private static final String   PARENT_FOLDER_NAME = "..";
	/** Parent folder icon.     */
	private final Icon            PARENT_FOLDER_ICON = get( XIcon.F_ARROW_TURN_090 );
	
	/** Columns of the table view. */
	private final List< IColumn< ? > > columnList = new ArrayList<>();
	
	/** Column identifiers of the table. */
	private final Vector< String > columnIdentifierVector = new Vector<>();
	
	/** Scroll pane wrapping the table.      */
	private final JScrollPane     tableScrollPane = new JScrollPane();
	/** Table displaying the path's content. */
	private final FolderPresenterTable table;
	
	/** Summary info.  */
	private final SummaryInfo summaryInfo           = new SummaryInfo();
	/** Label displaying files count info.   */
	private final JLabel      filesCountInfoLabel   = new JLabel();
	/** Label displaying folders count info. */
	private final JLabel      foldersCountInfoLabel = new JLabel();
	/** Label displaying size info.          */
	private final JLabel      sizeInfoLabel         = new JLabel() {
		private static final long serialVersionUID = 1L;
		// Register this at the Tool tip manager.
		// Simply calling setToolTipText( "" ) does not work because my getToolTipText() always returns a non-null text,
		// and setToolTipText( "" ) only registers at the tool tip manager if previous tool tip is not null!
		{ ToolTipManager.sharedInstance().registerComponent( this ); }
		/**
		 * Override {@link JComponent#getToolTipText()} instead of using {@link JComponent#setToolTipText(String)} for optimization.
		 * This tool tip text is not visible and is not required every time the size info changes
		 * (only required when the user triggers the tool tip).
		 */
		@Override
		public String getToolTipText() {
			return AcUtils.getFormattedSize( SizeFormat.BYTES, summaryInfo.selectedSize, 0 )
	    			+ Utils.OUT_OF_STRING + AcUtils.getFormattedSize( SizeFormat.BYTES, summaryInfo.totalSize, 0 );
		}
	};
	
	/** Custom sorting column. */
	private SortKey customSortKey;
	
	/** Types to go into. */
	private final Set< String > loweredTypesToGoIntoSet = new HashSet<>();
	
    /**
     * Creates a new FolderPresenter.
     * @param shell reference to the shell
     */
	protected FolderPresenter( final FolderShell shell ) {
    	super( shell, new JPanel( new BorderLayout() ) );
    	
    	table = new FolderPresenterTable( this, tableScrollPane );
    	// Clicking on the empty space (belonging to the scroll pane) does not focuses automatically the table, do it:
    	tableScrollPane.addMouseListener( new MouseAdapter() {
    		@Override
    		public void mousePressed( final MouseEvent event ) {
    			table.requestFocusInWindow();
    		}
		} );
    	
    	buildTable();
    	
    	// Summary box:
        final Box summaryBox = Box.createHorizontalBox();
        summaryBox.add( new JLabel( get(TextKey.GENERAL$FILES ) + ": " ) );
        summaryBox.add( filesCountInfoLabel );
		summaryBox.add( Box.createHorizontalStrut( 20 ) );
        summaryBox.add( new JLabel( get(TextKey.GENERAL$FOLDERS ) + ": " ) );
        summaryBox.add( foldersCountInfoLabel );
		summaryBox.add( Box.createHorizontalStrut( 20 ) );
        summaryBox.add( new JLabel( get(TextKey.GENERAL$SIZE ) + ": " ) );
        summaryBox.add( sizeInfoLabel );
		summaryBox.add( new JPanel( new BorderLayout() ) );
    	component.add( summaryBox, BorderLayout.SOUTH );
    	
    	// Setting listening:
		final Set< SettingKey< ? > > listenedSettingKeySet = Utils.< SettingKey< ? > >asNewSet(
				SettingKeys.FILE_LISTING$SIZE_SUMMARY_FORMAT, SettingKeys.FILE_LISTING$SIZE_SUMMARY_FRACTION_DIGITS,
				SettingKeys.FILE_LISTING$SHOW_HIDDEN_SYSTEM_FILES,
				SettingKeys.FOLDER_TABLE$GO_INTO_FILES_MODE, SettingKeys.FOLDER_TABLE$CUSTOM_TYPES_TO_GO_INTO );
		AcUtils.CAH.addChangeListener( listenedSettingKeySet, this );
		// Init setting dependent code:
		valuesChanged( listenedSettingKeySet );
    }
    
	@Override
	public void valuesChanged( final Set< SettingKey< ? > > settingKeySet ) {
		if ( settingKeySet.contains( SettingKeys.FILE_LISTING$SHOW_HIDDEN_SYSTEM_FILES ) )
			refresh();
		if ( settingKeySet.contains( SettingKeys.FILE_LISTING$SIZE_SUMMARY_FORMAT ) || settingKeySet.contains( SettingKeys.FILE_LISTING$SIZE_SUMMARY_FRACTION_DIGITS ) )
			refreshSummaryInfo();
		
		if ( settingKeySet.contains( SettingKeys.FOLDER_TABLE$GO_INTO_FILES_MODE ) || settingKeySet.contains( SettingKeys.FOLDER_TABLE$CUSTOM_TYPES_TO_GO_INTO ) ) {
			loweredTypesToGoIntoSet.clear();
			
			final GoIntoFilesMode goIntoFilesMode = get( SettingKeys.FOLDER_TABLE$GO_INTO_FILES_MODE );
			
			if ( goIntoFilesMode == GoIntoFilesMode.DEFAULT_TYPES || goIntoFilesMode == GoIntoFilesMode.DEFAULT_PLUS_CUSTOM_TYPES )
				loweredTypesToGoIntoSet.addAll( DEFAULT_LOWERED_GO_INTO_TYPES );
			
			if ( goIntoFilesMode == GoIntoFilesMode.CUSTOM_TYPES || goIntoFilesMode == GoIntoFilesMode.DEFAULT_PLUS_CUSTOM_TYPES )
				for ( final String customType : get( SettingKeys.FOLDER_TABLE$CUSTOM_TYPES_TO_GO_INTO ) )
					loweredTypesToGoIntoSet.add( customType.toLowerCase() );
		}
	}
	
    /**
     * Builds the file table.
     */
    private void buildTable() {
    	table.setModel( createTableModel() );
    	
    	table.setRowSorter( createTableRowSorter() );
    	
    	// We need to manage sorting manually, because the primary sorting column is not visible, and default sorting always starts with ascending (not possible to switch between ascending/descending)
    	table.getTableHeader().addMouseListener( new MouseAdapter() {
    		@Override
    		public void mouseClicked( final MouseEvent event ) {
    			if ( event.getButton() == GuiUtils.MOUSE_BTN_LEFT )
    				changeSorting( event ); // Change sorting
    			else if ( event.getButton() == GuiUtils.MOUSE_BTN_RIGHT )
    				showColumnSetupPopupMenu( event );
    		}
		} );
    	
    	setupColumns();
    	
    	table.getColumnModel().addColumnModelListener( new TableColumnModelListener() {
			@Override
			public void columnMoved( final TableColumnModelEvent event ) {
				// Store new "visual" column order
				final TypeList< String > columnListSetting = new TypeList<>( String.class, table.getColumnCount() );
				for ( int i = 0; i < table.getColumnCount(); i++ )
					columnListSetting.add( columnList.get( table.convertColumnIndexToModel( i ) ).getClass().getName() );
				set( SettingKeys.FOLDER_TABLE$COLUMN_LIST, columnListSetting );
				
		    	table.refreshViewColIdx();
			}
			
			@Override public void columnSelectionChanged( final ListSelectionEvent event ) {}
			@Override public void columnRemoved( final TableColumnModelEvent event ) {}
			@Override public void columnMarginChanged( final ChangeEvent event ) {}
			@Override public void columnAdded( final TableColumnModelEvent event ) {}
		} );
    	
    	final MouseAdapter tableMouseAdapter = new MouseAdapter() {
    		/** Current button states. */
    		private final Map< Integer, Boolean > buttonStateMap = new HashMap<>();
    		/** Stores the state of the last right click selection, this is to be repeated when mouse is dragged with right button. */
    		private Boolean lastRightClickSelectionState;
    		/** Last right click dragged view row index. We need this to provide continuous right click selection.
    		 * (If mouse is moved fast, we might skip a few rows...) */
    		private int     lastRightClickDraggedViewRowIdx;
    		// Implement double click on mouse pressed event instead of mouse clicked
    		// So we start executing right when left button is pressed the 2nd time
    		// This will give a "faster" feeling (on mouse clicked this would only be triggered when the mouse button is released)
    		@Override
    		public void mousePressed( final MouseEvent event ) {
    			buttonStateMap.put( event.getButton(), Boolean.TRUE );
    			
    			switch ( event.getButton() ) {
    			case GuiUtils.MOUSE_BTN_LEFT : {
    				if ( event.getClickCount() == 2 )
    					executeFocusedRow( null );
    				break;
    			}
    			case GuiUtils.MOUSE_BTN_RIGHT : {
    				// Focus row and select/unselect it 
    				int rowIdx = table.rowAtPoint( event.getPoint() );
    				if ( rowIdx == -1 )
    					break;
    				
    				table.setFocusedRow( rowIdx );
    				lastRightClickDraggedViewRowIdx = rowIdx;
    				
    				// VIEW index is returned as selection 
    				rowIdx = table.convertRowIndexToModel( rowIdx );
    				
    				// Store the selection state we just set:
    				lastRightClickSelectionState = table.getValueAtModel( rowIdx, COL_SELECTED ) ? Boolean.FALSE : Boolean.TRUE;
    				setModelRowSelected( rowIdx, lastRightClickSelectionState );
    				
    				break;
    			}
    			}
    		}
    		@Override
    		public void mouseReleased( final MouseEvent event ) {
    			buttonStateMap.put( event.getButton(), Boolean.FALSE );
    		}
    		@Override
    		public void mouseDragged( final MouseEvent event ) {
    			if ( Boolean.TRUE.equals( buttonStateMap.get( GuiUtils.MOUSE_BTN_RIGHT ) ) ) {
    				// Focus row and select/unselect it 
    				final int viewRowIdx = table.rowAtPoint( event.getPoint() );
    				if ( viewRowIdx == -1 )
    					return;
    				
    				// Select all rows continuously since the last dragged row. (Due to fast mouse movement rows might have been skipped.) 
    				final int dir = lastRightClickDraggedViewRowIdx < viewRowIdx ? 1 : -1; 
    				while ( true ) {
	    				table.setFocusedRow( lastRightClickDraggedViewRowIdx );
	    				// VIEW index is returned as selection, table model requires MODEL index 
	    				final int modelRowIdx = table.convertRowIndexToModel( lastRightClickDraggedViewRowIdx );
	    				setModelRowSelected( modelRowIdx, lastRightClickSelectionState );
	    				if ( lastRightClickDraggedViewRowIdx == viewRowIdx )
	    					break;
	    				lastRightClickDraggedViewRowIdx += dir;
    				}
    			}
    		}
		};
		table.addMouseListener( tableMouseAdapter );
		table.addMouseMotionListener( tableMouseAdapter );
    	
		registerHotkeys();
		
		component.add( tableScrollPane, BorderLayout.CENTER );
    	
    	table.refreshViewColIdx();
    }
    
    /**
     * Registers the hotkeys for the table.
     */
    private void registerHotkeys() {
    	// Register hotkeys for the table
		Object actionKey;
		
		// Enter to change directory or execute/open active file
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				executeFocusedRow( null );
			}
		} );
		
		// SHIFT+Enter to change directory or execute/open active file, forcing NOT go into the file
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, InputEvent.SHIFT_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				executeFocusedRow( Boolean.FALSE );
			}
		} );
		
		// CTRL+PGDOWN to change directory or to force go into a file
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_PAGE_DOWN, InputEvent.CTRL_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				executeFocusedRow( Boolean.TRUE );
			}
		} );
    	
		// Space to select/deselect active row
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_SPACE, 0 ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final int focusedRow = table.getFocusedRowViewIdx();
				if ( focusedRow < 0 )
					return;
				// VIEW index is returned as selection, table model requires MODEL index 
				final int modelRowIdx = table.convertRowIndexToModel( focusedRow );
				
				setModelRowSelected( modelRowIdx, table.getValueAtModel( modelRowIdx, COL_SELECTED ) ? Boolean.FALSE : Boolean.TRUE );
				
				if ( focusedRow < table.getRowCount() - 1 && get( SettingKeys.FOLDER_TABLE$MOVE_FOCUS_WHEN_USING_SPACE ) )
					table.setFocusedRow( focusedRow + 1 );
			}
		} );
    	
		// Backspace and CTRL+PGUP to go up one level
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), actionKey );
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
		    	final Path parent = Utils.getVirtualAwareParent( model.path );
				if ( parent != null )
					shell.setPath( parent );
			}
		} );
    	
		// CTRL+R to refresh
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_R, InputEvent.CTRL_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				refresh();
			}
		} );
    	
		// SHIFT+UP to invert selection on focused row and move focus up
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_UP, InputEvent.SHIFT_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final int focusedRow = table.getFocusedRowViewIdx();
				if ( focusedRow < 0 )
					return;
				// Invert selection of focused row
				// VIEW index is returned as focused row
				final int modelIdx = table.convertRowIndexToModel( focusedRow );
				setModelRowSelected( modelIdx, table.getValueAtModel( modelIdx, COL_SELECTED ) ? Boolean.FALSE : Boolean.TRUE );
				if ( focusedRow > 0 )
					table.setFocusedRow( focusedRow - 1 );
			}
		} );
		
		// INSERT and SHIFT+DOWN to invert selection on focused row and move focus down
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_INSERT, 0 ), actionKey );
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final int focusedRow = table.getFocusedRowViewIdx();
				if ( focusedRow < 0 )
					return;
				// Invert selection of focused row
				// VIEW index is returned as focused row
				final int modelIdx = table.convertRowIndexToModel( focusedRow );
				setModelRowSelected( modelIdx, table.getValueAtModel( modelIdx, COL_SELECTED ) ? Boolean.FALSE : Boolean.TRUE );
				if ( focusedRow < table.getRowCount() - 1 )
					table.setFocusedRow( focusedRow + 1 );
			}
		} );
		
		// CTRL+A to select all rows
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_A, InputEvent.CTRL_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// Select rows by calling setModelRowSelected() which properly updates the summary info!
				// It also checks not to select "parent folder" row!
				final int rowCount = table.getRowCount();
				for ( int i =  0; i < rowCount; i++ )
					setModelRowSelected( i, Boolean.TRUE );
			}
		} );
		
		// SHIFT+END to select/deselect all rows starting from the focused row 'till the end
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_END, InputEvent.SHIFT_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final int focusedRow = table.getFocusedRowViewIdx();
				// VIEW index is returned as focused row
				if ( focusedRow < 0 )
					return;
				final int modelIdx = table.convertRowIndexToModel( focusedRow );
				
				final Boolean selectionState = table.getValueAtModel( modelIdx, COL_SELECTED ) ? Boolean.FALSE : Boolean.TRUE;
				
				final int rowCount = table.getRowCount();
				for ( int i = focusedRow; i < rowCount; i++ )
					setModelRowSelected( table.convertRowIndexToModel( i ), selectionState );
				
				table.setFocusedRow( rowCount - 1 );
			}
		} );
		
		// SHIFT+HOME to select/deselect all rows starting from the focused row up to the first row
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_HOME, InputEvent.SHIFT_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final int focusedRow = table.getFocusedRowViewIdx();
				if ( focusedRow < 0 )
					return;
				// VIEW index is returned as focused row
				final int modelIdx = table.convertRowIndexToModel( focusedRow );
				
				final Boolean selectionState = table.getValueAtModel( modelIdx, COL_SELECTED ) ? Boolean.FALSE : Boolean.TRUE;
				
				for ( int i = focusedRow; i >= 0; i-- )
					setModelRowSelected( table.convertRowIndexToModel( i ), selectionState );
				
				table.setFocusedRow( 0 );
			}
		} );
		
		// SHIFT+PGDOWN to select/deselect downward 1 page of rows starting from the focused row
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_PAGE_DOWN, InputEvent.SHIFT_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final int focusedRow = table.getFocusedRowViewIdx();
				if ( focusedRow < 0 )
					return;
				// VIEW index is returned as focused row
				final int modelIdx = table.convertRowIndexToModel( focusedRow );
				
				final Boolean selectionState = table.getValueAtModel( modelIdx, COL_SELECTED ) ? Boolean.FALSE : Boolean.TRUE;
				
				final int maxRow = Math.min( focusedRow + ( tableScrollPane.getHeight() / table.getRowHeight() - 3 ), table.getRowCount() - 1 );
				for ( int i = focusedRow; i <= maxRow; i++ )
					setModelRowSelected( table.convertRowIndexToModel( i ), selectionState );
				
				// Move focus 1 row above the last selected/deselected (so SHIFT+PGDOWN can be repeated to continue selecting/deselecting)
				table.setFocusedRow( Math.min( maxRow + 1, table.getRowCount() - 1 ) );
			}
		} );
		
		// SHIFT+PGUP to select/deselect upward 1 page of rows starting from the focused row
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_PAGE_UP, InputEvent.SHIFT_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final int focusedRow = table.getFocusedRowViewIdx();
				if ( focusedRow < 0 )
					return;
				// VIEW index is returned as focused row
				final int modelIdx = table.convertRowIndexToModel( focusedRow );
				
				final Boolean selectionState = table.getValueAtModel( modelIdx, COL_SELECTED ) ? Boolean.FALSE : Boolean.TRUE;
				
				final int minRow = Math.max( focusedRow - ( tableScrollPane.getHeight() / table.getRowHeight() - 3 ), 0 );
				for ( int i = focusedRow; i >= minRow; i-- )
					setModelRowSelected( table.convertRowIndexToModel( i ), selectionState );
				
				// Move focus 1 row below the last selected/deselected (so SHIFT+PGUP can be repeated to continue selecting/deselecting)
				table.setFocusedRow( Math.max( minRow - 1, 0 ) );
			}
		} );
		
		// DELETE to fire the Delete action
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				DeleteAction.INSTANCE.actionPerformed( event );
			}
		} );
		
		// Remove default action for the F8 key (JTable assigns table header focusing, but it is used by the Delete action in Abdra Commander).
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_F8, 0 ), "none" );
    }
    
    /**
     * Creates and returns our custom table model implementation.
     * @return our custom table model implementation
     */
    private DefaultTableModel createTableModel() {
    	return new DefaultTableModel() {
            private static final long serialVersionUID = 1L;
			@Override
    		public Class< ? > getColumnClass( int columnIndex ) {
    			return columnList.get( columnIndex ).getColumnClass();
    		}
    	};
    }
    
    /**
     * Creates and returns our custom table row sorter implementation.
     * @return our custom table row sorter implementation
     */
    private TableRowSorter< DefaultTableModel > createTableRowSorter() {
    	return new TableRowSorter< DefaultTableModel >( (DefaultTableModel) table.getModel() ) {
    		{ setMaxSortKeys( 1 ); } // Init row sorter
    		@Override
    		public Comparator< ? > getComparator( int column ) {
    			final Comparator< ? > customComparator = columnList.get( column ).getComparator();
    			return customComparator == null ? super.getComparator( column ) : customComparator;
    		}
    		@Override
    		public void setSortKeys( final List< ? extends SortKey > sortKeys ) {
    			// SORT KEYS OPERATE ON MODEL INDEX!
    			final List< SortKey > sortKeyList = new ArrayList<>();
    		    // Pre-primary sorting: priority (put directories first)
    		    sortKeyList.add( new SortKey( getColumnModelIndex( COL_PRIO ), SortOrder.ASCENDING ) );
    		    
    		    if ( sortKeys != null ) {
        		    // Allow only 1 custom sorting column:
    		    	final SortKey sortKey = sortKeys.get( 0 );
    		    	set( SettingKeys.FOLDER_TABLE$SORTING_COLUMN, columnList.get( sortKey.getColumn() ).getClass().getName() );
    		    	set( SettingKeys.FOLDER_TABLE$SORT_ORDER    , sortKey.getSortOrder() );
    		    	
        		    sortKeyList.add( sortKey );
        		    
        		    // Secondary sorting
        		    if ( sortKey.getColumn() == getColumnModelIndex( COL_NAME ) ) {
            			// Sorted by NAME, force secondary sort column to be EXT:
           				sortKeyList.add( new SortKey( getColumnModelIndex( COL_EXT ), SortOrder.ASCENDING ) );
        		    }
        		    else {
            			// Force secondary sort column to be NAME:
           				sortKeyList.add( new SortKey( getColumnModelIndex( COL_NAME ), SortOrder.ASCENDING ) );
        		    }
    		    }
   				
    		    super.setSortKeys( sortKeyList );
    		}
    	};
    }
    
    /**
     * Sets up the columns.
     */
    private void setupColumns() {
    	// First clear table to avoid side effects (including exceptions) while reconfiguring columns...
    	( (DefaultTableModel) table.getModel() ).getDataVector().clear();
    	( (DefaultTableModel) table.getModel() ).fireTableDataChanged();
    	
    	columnList            .clear();
    	columnIdentifierVector.clear();
    	
    	columnList.addAll( INTERNAL_COLUMN_SET );
    	
    	for ( final String columnClass : get( SettingKeys.FOLDER_TABLE$COLUMN_LIST ) ) {
    		final IColumn< ? > column = mainModel.getFolderPresenterColumnRegistry().get( columnClass );
    		if ( column == null )
    			warning( "Column implementation not found: " + columnClass );
    		else
    			columnList.add( column );
    	}
    	
    	for ( final IColumn< ? > column : columnList )
    		columnIdentifierVector.add( column.getDisplayName() );
    	
    	( (DefaultTableModel) table.getModel() ).setColumnIdentifiers( columnIdentifierVector );
    	
    	// Column model works with VIEW indices!
    	final TableColumnModel columnModel = table.getColumnModel();
		// Remove internal columns from view
    	for ( final IColumn< ? > column : INTERNAL_COLUMN_SET )
			columnModel.removeColumn( columnModel.getColumn( getColumnViewIndex( column ) ) );
    	
    	// TODO Consider adding maxWidth as Integer to the IColumn, null would mean not specified
    	if ( columnList.contains( COL_ICON ) )
    		columnModel.getColumn( getColumnViewIndex( COL_ICON ) ).setMaxWidth( 16 );
    	
    	// Restore sorting:
    	if ( customSortKey == null ) {
        	final String columnClass = get( SettingKeys.FOLDER_TABLE$SORTING_COLUMN );
    		IColumn< ? > column = mainModel.getFolderPresenterColumnRegistry().get( columnClass );
    		if ( column == null ) {
    			warning( "Column implementation not found: " + columnClass );
    			column = COL_EXT;
    		}
        	customSortKey = new SortKey( getColumnModelIndex( column ), get( SettingKeys.FOLDER_TABLE$SORT_ORDER ) );
    	}
    	table.getRowSorter().setSortKeys( Arrays.asList( customSortKey ) );
    	
    	refreshAutoResizeMode();
    	
    	refresh();
    }
    
    /**
     * Refreshes the auto resize mode.
     */
    private void refreshAutoResizeMode() {
		table.setAutoResizeMode( get( SettingKeys.FOLDER_TABLE$AUTO_RESIZE_MODE ).tableConst );
    }
    
    /**
     * Refreshes the file and folder list displayed in the table,
     * and keeps the focused path if it is still available after refresh.
     */
    public void refresh() {
    	refresh( getFocusedPath() );
    }
    
    /**
     * Refreshes the file and folder list displayed in the table,
     * and keeps the focused path if it is still available after refresh.
     * @param pathToFocus path to focus after the refresh
     */
    public void refresh( final Path pathToFocus ) {
        // Remember selection (store selected paths and restore it in the end) 
    	final Set< Path > selectedPathSet = new HashSet< Path >();
    	getSelectedPaths( selectedPathSet );
    	
    	final DefaultTableModel tmodel = (DefaultTableModel) table.getModel();
    	
		final Vector< Vector< Object > > dataVector = Utils.cast( tmodel.getDataVector() );
        
        // TODO [OPTIMIZE] by reusing vectors currently in the data vector! (to do that, pass the vector to createRowVector())
    	tmodel.getDataVector().clear();
    	
    	summaryInfo.reset();
    	
    	final RowContext rowContext = new RowContext();
    	
    	// First add "go to parent folder" (if not root)
    	final Path parent = Utils.getVirtualAwareParent( model.path );
	    if ( parent != null ) {
	    	try {
	        	rowContext.init( parent );
	    		dataVector.add( createRowVector( parent, Files.readAttributes( parent, BasicFileAttributes.class ), rowContext ) );
	    		dataVector.get( 0 ).set( getColumnModelIndex( COL_PRIO ), PARENT_FOLDER_PRIO );
	    		dataVector.get( 0 ).set( getColumnModelIndex( COL_ICON ), PARENT_FOLDER_ICON );
	    		dataVector.get( 0 ).set( getColumnModelIndex( COL_NAME ), PARENT_FOLDER_NAME );
	    		dataVector.get( 0 ).set( getColumnModelIndex( COL_SIZE ), 0                  ); // Some size is returned, we don't want it
            } catch ( final IOException ie ) {
	            warning( "Could not read parent folder attributes:" + Utils.getPathString( parent ), ie );
            }
	    }
	    
	    final boolean showHiddenSystemFiles = get( SettingKeys.FILE_LISTING$SHOW_HIDDEN_SYSTEM_FILES );
	    try {
	    	Files.walkFileTree( model.path, Utils.< FileVisitOption >getEmptySet(), 1, new SimpleFileVisitor< Path >() {
	        	@Override
	        	public FileVisitResult visitFile( Path file, final BasicFileAttributes attrs ) throws IOException {
	        		file = rowContext.init( file );
	        		
	        		if ( attrs instanceof DosFileAttributes )
	        			if ( !showHiddenSystemFiles && ( ( (DosFileAttributes) attrs ).isHidden() || ( (DosFileAttributes) attrs ).isSystem() ) )
	        				return FileVisitResult.CONTINUE;
	        		
	        		dataVector.add( createRowVector( file, attrs, rowContext ) );
	        	    
	        	    // Update summary info
	        	    summaryInfo.totalSize += attrs.size();
	        	    if ( attrs.isDirectory() )
	        	    	summaryInfo.foldersCount++;
	        	    else
	        	    	summaryInfo.filesCount++;
	        	    
	        	    return FileVisitResult.CONTINUE;
	        	}
	        } );
        } catch ( final IOException ie ) {
        	AcUtils.beepError();
	        warning( "Failed to read folder content: " + Utils.getPathString( model.path ) );
	        debug( "Reason:", ie );
        }
    	
    	tmodel.fireTableDataChanged();
    	
    	boolean foundFocused = false;
    	if ( pathToFocus != null ) {
    		final int pathModelIdx = getColumnModelIndex( COL_PATH );
    		for ( int i = dataVector.size() - 1; i >= 0; i-- )
    			if ( pathToFocus.equals( dataVector.get( i ).get( pathModelIdx ) ) ) {
					table.setFocusedRow( table.convertRowIndexToView( i ) );
					foundFocused = true;
    				break;
    			}
    	}
    	if ( !foundFocused ) {
    		// Focus first row (usually go to parent folder).
    		if ( table.getRowCount() > 0 )
    			table.setFocusedRow( 0 );
    	}
    	
    	// Restore selection
    	// We have to call setModelRowSelected() for each so summary info will be properly updated (recalculated).
    	if ( !selectedPathSet.isEmpty() ) {
        	final int pathColModelIdx = getColumnModelIndex( COL_PATH );
	    	for ( int i = dataVector.size() - 1; i >= 0; i-- ) {
	    		final Vector< Object > row = dataVector.get( i );
	    		if ( selectedPathSet.contains( row.get( pathColModelIdx ) ) )
	    			setModelRowSelected( i, Boolean.TRUE );
	    	}
    	}
		
    	refreshSummaryInfo();
    }
    
    /**
     * Creates a row vector for the specified path.
     * @param file       file path to create row vector for
     * @param attrs      attributes of the path to create row vector for
     * @param rowContext row context to pass on to the columns
     * @return a row vector for the specified path
     */
    private Vector< Object > createRowVector( final Path file, final BasicFileAttributes attrs, final RowContext rowContext ) {
	    final Vector< Object > row = new Vector<>( columnIdentifierVector.size() );
	    
    	for ( final IColumn< ? > column : columnList )
    		row.add( column.getData( file, attrs, rowContext ) );
    	
    	return row;
    }
    
    /**
     * Executes the focused row.
     * In case of directory it means to go into it,
     * in case of a file it means to execute/open it.
     * 
     * @param forceGoInto tells if we have to try go into the focused file regardless of its extension;
     * 		<code>null</code> means not specified (file extension will decide), <code>Boolean.TRUE</code> means to force go into, <code>Boolean.FALSE</code> means do not try to go into
     */
    private void executeFocusedRow( final Boolean forceGoInto ) {
		final Path focusedPath = getFocusedPath();
		if ( focusedPath == null )
			return;
		
		if ( Files.isDirectory( focusedPath ) ) {
			// Go into the folder (make it the current path)
			shell.setPath( focusedPath );
		}
		else {
			// Do not do anything with files inside VFS (for now).
			if ( Utils.isVirtualFS( focusedPath ) )
				return;
			
			boolean goInto;
			if ( forceGoInto == null ) {
				switch ( get( SettingKeys.FOLDER_TABLE$GO_INTO_FILES_MODE ) ) {
				case NEVER  : goInto = false; break;
				case ALWAYS : goInto = true ; break;
				default : {
					final String ext = Utils.getFileExt( focusedPath );
					if ( !( goInto = loweredTypesToGoIntoSet.contains( ext ) ) ) // First check if it is contained without lower-casing it
						goInto = loweredTypesToGoIntoSet.contains( ext.toLowerCase() ); // It wasn't contained, try as lower-cased
					break;
				}
				}
			}
			else
				goInto = forceGoInto;
			
			if ( goInto ) {
				// Go into the ZIP file:
				try {
					final FileSystem fs = FileSystems.newFileSystem( focusedPath, null );
					shell.setPath( fs.getRootDirectories().iterator().next() );
				} catch ( final Throwable t ) { // Catch Throwable instead of Exception because ZipFileInputStream throws ZipError if zip format is invalid
					AcUtils.beepError();
	            	warning( "Failed to go into: " + Utils.getPathString( focusedPath ) );
					debug( "reason:", t );
				}
			}
			else {
				// Execute/Open file
				try {
		            Desktop.getDesktop().open( focusedPath.toFile() );
	            } catch ( final IOException ie ) {
	            	AcUtils.beepError();
	            	warning( "Failed to launch file: " + Utils.getPathString( focusedPath ) );
	    	        debug( "Reason:", ie );
	            }
			}
		}
    }
    
    /**
     * Changes sorting based on the mouse click event on the header.
     * @param event reference to the mouse event
     */
    private void changeSorting( final MouseEvent event ) {
		int column = table.columnAtPoint( event.getPoint() );
		if ( column == -1 )
			return;
		
		// Column is VIEW INDEX, SORT KEYS require model index, so convert it
		column = table.convertColumnIndexToModel( column );
		
		if ( customSortKey.getColumn() == column ) {
			// Invert sorting
			customSortKey = new SortKey( column, customSortKey.getSortOrder() == SortOrder.ASCENDING ? SortOrder.DESCENDING : SortOrder.ASCENDING );
		}
		else {
			// New custom sorting column
			customSortKey = new SortKey( column, SortOrder.ASCENDING );
		}
		
    	table.getRowSorter().setSortKeys( Arrays.asList( customSortKey ) );
    }
    
    /**
     * Shows the column setup popup menu.
     * @param event reference to the mouse event
     */
    private void showColumnSetupPopupMenu( final MouseEvent event ) {
		final JPopupMenu popupMenu = new JPopupMenu();
		JPanel titlePanel = new JPanel( new GridLayout( 3, 1, 0, 5 ) );
		titlePanel.setBorder( BorderFactory.createEmptyBorder( 3, 10, 3, 10 ) );
		titlePanel.add( GuiUtils.changeFontToBold( new JLabel( get( TextKey.FOLDER_TABLE$COLUMN_SETUP ) ) ) );
		titlePanel.add( new JLabel( get( TextKey.FOLDER_TABLE$COLUMN_SETUP$SORTING_INFO ) ) );
		titlePanel.add( new JLabel( get( TextKey.FOLDER_TABLE$COLUMN_SETUP$REARRANGE_INFO ) ) );
		popupMenu.add( titlePanel );
		
		popupMenu.addSeparator();
		
		final List< IColumn< ? > > allColumnList = new ArrayList<>( mainModel.getFolderPresenterColumnRegistry().valueList() );
		allColumnList.removeAll( INTERNAL_COLUMN_SET );
		Collections.sort( allColumnList, new Comparator< IColumn< ? > >() {
			@Override
            public int compare( final IColumn< ? > c1, final IColumn< ? > c2 ) {
                return c1.getDescription().compareToIgnoreCase( c2.getDescription() );
            }
		} );
		
		for ( final IColumn< ? > column : allColumnList ) {
			final JCheckBoxMenuItem checkBoxmenuItem = new JCheckBoxMenuItem( column.getDescription() + Utils.SPACE_PARENTHESIS_STRING + column.getDisplayName() + ')', columnList.contains( column ) );
			checkBoxmenuItem.setEnabled( column.getType() != Type.REQUIRED );
			checkBoxmenuItem.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					final TypeList< String > columnListSetting = get( SettingKeys.FOLDER_TABLE$COLUMN_LIST );
					if ( checkBoxmenuItem.isSelected() )
						columnListSetting.add( column.getClass().getName() );
					else
						columnListSetting.remove( column.getClass().getName() );
					set( SettingKeys.FOLDER_TABLE$COLUMN_LIST, new TypeList<>( String.class, columnListSetting ) );
					setupColumns();
				}
			} );
			popupMenu.add( checkBoxmenuItem );
		}
		
		popupMenu.addSeparator();
		
		final JMenu autoResizeModeMenu = new JMenu( get( TextKey.FOLDER_TABLE$COLUMN_SETUP$AUTO_RESIZE_MODE ) );
		titlePanel = new JPanel( new GridLayout( 1, 1, 0, 5 ) );
		titlePanel.setBorder( BorderFactory.createEmptyBorder( 3, 10, 3, 10 ) );
		titlePanel.add( new JLabel( get( TextKey.FOLDER_TABLE$COLUMN_SETUP$AUTO_RESIZE_MODE$INFO ) ) );
		autoResizeModeMenu.add( titlePanel );
		autoResizeModeMenu.addSeparator();
		final AutoResizeMode currentAutoResizeMode = AutoResizeMode.getFromTableConst( table.getAutoResizeMode() );
		GuiUtils.createRadioButtonMenuItems( autoResizeModeMenu, AutoResizeMode.values(), currentAutoResizeMode, new Receiver< AutoResizeMode >() {
			@Override
			public void receive( final AutoResizeMode selectedAutoResizeMode ) {
				set( SettingKeys.FOLDER_TABLE$AUTO_RESIZE_MODE, selectedAutoResizeMode );
				refreshAutoResizeMode();
			}
		} );
		popupMenu.add( autoResizeModeMenu );
		
		popupMenu.addSeparator();
		
		final JMenuItem restoreDefaultSetupMenuItem = new JMenuItem( get( TextKey.FOLDER_TABLE$COLUMN_SETUP$RESTORE_DEFAULT_SETUP ) );
		restoreDefaultSetupMenuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				reset( SettingKeys.FOLDER_TABLE$COLUMN_LIST      );
	        	reset( SettingKeys.FOLDER_TABLE$SORTING_COLUMN   );
	        	reset( SettingKeys.FOLDER_TABLE$SORT_ORDER       );
	        	reset( SettingKeys.FOLDER_TABLE$AUTO_RESIZE_MODE );
				customSortKey = null;
				setupColumns();
			}
		} );
		popupMenu.add( restoreDefaultSetupMenuItem );
		
		popupMenu.show( event.getComponent(), event.getX(), event.getY() );
    }
    
    /**
     * Refreshes the summary info.
     */
    private void refreshSummaryInfo() {
        filesCountInfoLabel  .setText( formatNumber( summaryInfo.selectedFilesCount   ) + Utils.OUT_OF_STRING + formatNumber( summaryInfo.filesCount   ) );
        foldersCountInfoLabel.setText( formatNumber( summaryInfo.selectedFoldersCount ) + Utils.OUT_OF_STRING + formatNumber( summaryInfo.foldersCount ) );
        
    	final SizeFormat sizeFormat     = get( SettingKeys.FILE_LISTING$SIZE_SUMMARY_FORMAT          );
    	final int        fractionDigits = get( SettingKeys.FILE_LISTING$SIZE_SUMMARY_FRACTION_DIGITS );
    	sizeInfoLabel.setText( AcUtils.getFormattedSize( sizeFormat, summaryInfo.selectedSize, fractionDigits )
    			+ Utils.OUT_OF_STRING + AcUtils.getFormattedSize( sizeFormat, summaryInfo.totalSize, fractionDigits ) );
    }
    
    /**
     * Sets the selected state of the the specified model row: changes the value in the COL_SELECTED column.<br>
     * Also properly updates the summary info and repaints the row.
     * @param modelRowIdx MODEL index of the row whose selected state to be set  
     * @param selected selected state to be set  
     */
    private void setModelRowSelected( final int modelRowIdx, final Boolean selected ) {
    	// Do not select parent folder row...
    	final Priority priority = table.getValueAtModel( modelRowIdx, COL_PRIO );
    	if ( priority == Priority.PARENT_FOLDER )
    		return;
    	
    	final Boolean currentState = table.getValueAtModel( modelRowIdx, COL_SELECTED );
    	if ( currentState.equals( selected ) )
    		return; // Nothing to do
    	
		table.getModel().setValueAt( selected, modelRowIdx, getColumnModelIndex( COL_SELECTED ) );
		
		// Update summary info
		if ( selected ) {
			if ( priority == Priority.FOLDER ) {
				summaryInfo.selectedFoldersCount++;
			}
			else {
				summaryInfo.selectedFilesCount++;
				summaryInfo.selectedSize += table.getValueAtModel( modelRowIdx, COL_SIZE );
			}
		}
		else {
			if ( priority == Priority.FOLDER ) {
				summaryInfo.selectedFoldersCount--;
			}
			else {
				summaryInfo.selectedFilesCount--;
				summaryInfo.selectedSize -= table.getValueAtModel( modelRowIdx, COL_SIZE );
			}
		}
		refreshSummaryInfo();
		
    	// Repaint row:
		( (DefaultTableModel) table.getModel() ).fireTableRowsUpdated( modelRowIdx, modelRowIdx );
    }
    
    /**
     * Sets whether the table is active.
     * @param active true if the table is active
     */
    public void setActive( final boolean active ) {
    	table.setActive( active );
    	// Folder presenter theme allows different colors for active and inactive tables, so repaint the table
    	table.repaint();
    }
    
    /**
     * Returns the table component.
     * @return the table component
     */
    public JTable getTable() {
    	return table;
    }
    
    /**
     * Returns the view index of the specified column.
     * @param column the column whose view index to be returned
     * @return the view index of the specified column
     */
    public int getColumnViewIndex( final IColumn< ? > column ) {
    	return table.getColumnModel().getColumnIndex( column.getDisplayName() );
    }
    
    /**
     * Returns the model index of the specified column.
     * @param column the column whose model index to be returned
     * @return the model index of the specified column
     */
    public int getColumnModelIndex( final IColumn< ? > column ) {
    	return columnList.indexOf( column );
    }
    
    /**
     * Returns the model index of the SELECTED column.
     * @return the model index of the SELECTED column
     */
    public int getSelectedColumnModelIndex() {
    	return getColumnModelIndex( COL_SELECTED );
    }
    
    /**
     * Returns the view index of the SIZE column.
     * @return the view  index of the SIZE column
     */
    public int getSizeColumnViewIndex() {
    	return getColumnViewIndex( COL_SIZE );
    }
    
	/**
	 * Returns the column list.
	 * @return the column list
	 */
    public List< IColumn< ? > > getColumnList() {
		return columnList;
	}
    
	/**
	 * Returns the focused path.
	 * @return the focused path
	 */
    private Path getFocusedPath() {
		final int focusedRow = table.getFocusedRowViewIdx();
		if ( focusedRow < 0 )
			return null;
		
		return table.getValueAtView( focusedRow, COL_PATH );
	}
    
    /**
     * Gets the selected paths.
     * @param result optional collection to put the selected paths in; if <code>null</code>, a new collection will be created and returned
     * @return the collection of selected paths
     */
    private Collection< ? super Path > getSelectedPaths( Collection< ? super Path > result ) {
    	if ( result == null )
    		result = new ArrayList< Path >();
    	
    	final int selectedColModelIdx = getColumnModelIndex( COL_SELECTED );
    	final int pathColModelIdx     = getColumnModelIndex( COL_PATH     );
    	
		final Vector< Vector< Object > > dataVector = Utils.cast( ( (DefaultTableModel) table.getModel() ).getDataVector() );
    	
    	for ( final Vector< Object > row : dataVector ) {
    		if ( Boolean.TRUE.equals( row.get( selectedColModelIdx ) ) )
    			result.add( (Path) row.get( pathColModelIdx ) );
    	}
    	
    	return result;
    }
    
    /**
     * Returns the input paths for a file operation.<br>
     * If there are selected paths, they will be returned, else the focused path will be returned.
     * @return the input paths for a file operation; or <code>null</code> if no input paths can be determined (no selected paths and no focused path)
     */
    public Path[] getInputPathsForFileOps() {
    	final Collection< ? super Path > selectedPaths = getSelectedPaths( null );
    	
    	if ( selectedPaths.isEmpty() ) {
    		// No selected paths, return the focused path...
    		final int focusedRow = table.getFocusedRowViewIdx();
    		if ( focusedRow < 0 )
    			return null;
    		
    		// ...but only if it's not the parent folder row...
    		final Priority priority    = table.getValueAtView( focusedRow, COL_PRIO );
    		final Path     focusedPath = priority == Priority.PARENT_FOLDER ? null : table.getValueAtView( focusedRow, COL_PATH );
    		
    		if ( focusedPath == null ) {
        		OptionsShell.withOptionList( null, XIcon.FILE_OP, TextKey.GENERAL$INFO, BodyIcon.INFO,
        				get( TextKey.FOLDER_TABLE$NO_SELECTED_FILES ), OptionsShell.OPTIONS_OK ).getOption();
    			return null;
    		}
    		
    		return new Path[] { focusedPath };
    	}
    	else
    		return selectedPaths.toArray( new Path[ selectedPaths.size() ] );
    }
    
    /**
     * Receives a notification that an input path has been processed (by a file operation).
     * We remove its selection.
     * @param inputPath input path that has been processed
     */
    public void receiveInputPathProcessed( final Path inputPath ) {
    	final int pathColModelIdx = getColumnModelIndex( COL_PATH );
    	
		final Vector< Vector< Object > > dataVector = Utils.cast( ( (DefaultTableModel) table.getModel() ).getDataVector() );
    	
		for ( int i = dataVector.size() - 1; i >= 0; i-- ) {
    		if ( inputPath.equals( dataVector.get( i ).get( pathColModelIdx ) ) ) {
    	    	// We have to call setModelRowSelected() for each so summary info will be properly updated (recalculated).
    			setModelRowSelected( i, Boolean.FALSE );
    			break;
    		}
    	}
    }
    
	/**
	 * Returns name of the focused file.
	 * @return name of the focused file
	 */
    public String getFocusedFileName() {
		final int focusedRow = table.getFocusedRowViewIdx();
		if ( focusedRow < 0 )
			return null;
		
		return table.getValueAtView( focusedRow, COL_NAME );
	}
    
}
