/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder.tableimpl;

import hu.belicza.andras.util.Utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.abdracmd.service.icon.Icons;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.settings.SettingKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.service.settings.SettingSetChangeListener;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderPresenter;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntity;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntityFlag;
import com.abdracmd.util.AcUtils;

/**
 * Folder presenter Table implementation.
 * 
 * @author Andras Belicza
 */
public class FolderPresenterTable extends JTable implements SettingSetChangeListener {
	
    /** Default serial version ID. */
	private static final long serialVersionUID = 1L;
    
    /** Reference to the folder presenter. */
	private final FolderPresenter folderPresenter;
	
	/** Our implementation of table cell renderer. */
    private final FolderPresenterTableCellRenderer tableCellRenderer;
    
	/** Tells if the table is active.
	 * Not necessarily the "has focus" state (we might be the active even if <code>hasFocus()</code> returns false). */
	private boolean active;
	
    /**
     * Creates a new FolderPresenterTable.
     * @param folderPresenter reference to the folder presenter
     * @param tableScrollPane reference to the table scroll pane
     */
    public FolderPresenterTable( final FolderPresenter folderPresenter, final JScrollPane tableScrollPane ) {
    	this.folderPresenter = folderPresenter;
    	
    	tableCellRenderer = new FolderPresenterTableCellRenderer( folderPresenter );
    	
    	setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    	
    	// Whether there's a grid is LAF dependent, override it:
    	setShowGrid( false );
    	
    	// Even if grid is not shown, space is left for it, so set its size to 0 (setShowGrid() is required even if size is 0!)
    	setIntercellSpacing( new Dimension( 0, 0 ) );
    	
    	getTableHeader().setDefaultRenderer( createTableHeaderRenderer( folderPresenter ) );
    	
    	// Register GENERAL hotkeys for the table
    	registerGeneralHotkeys();
    	
    	tableScrollPane.setViewportView( this );
		
    	// Setting listening:
		final Set< SettingKey< ? > > listenedSettingKeySet = Utils.< SettingKey< ? > >asNewSet( SettingKeys.FOLDER_TABLE$THEME );
		AcUtils.CAH.addChangeListener( listenedSettingKeySet, this );
		// Init setting dependent code:
		valuesChanged( listenedSettingKeySet );
    }
    
    /**
     * Registers GENERAL hotkeys for the table.
     */
    private void registerGeneralHotkeys() {
		Object actionKey;
		
		// Home to select first row
		actionKey = new Object();
		getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_HOME, 0 ), actionKey );
		getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				setFocusedRow( 0 );
			}
		} );
    	
		// End to select last row
		actionKey = new Object();
		getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_END, 0 ), actionKey );
		getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
	    		// Select last row.
				setFocusedRow( getRowCount() - 1 );
			}
		} );
    }
    
	@Override
	public void valuesChanged( final Set< SettingKey< ? > > settingKeySet ) {
		if ( settingKeySet.contains( SettingKeys.FOLDER_TABLE$THEME ) )
			refreshTheme();
	}
	
	@Override
	public void updateUI() {
		super.updateUI();
		
		// Default updateUI() somehow failes to update our renderer...
		if ( tableCellRenderer != null )
			tableCellRenderer.updateUI();
	}
	
    /**
     * Creates and returns a {@link TableCellRenderer} responsible for rendering the header cells.
     * @param folderPresenter reference to the folder presenter
     * @return a {@link TableCellRenderer} responsible for rendering the header cells
     */
    private TableCellRenderer createTableHeaderRenderer( final FolderPresenter folderPresenter ) {
    	// We have to extend Component (JComponent), else the renderer's updateUI() is not called when LaF is changed!
    	
    	class MyHeaderCellRenderer extends JComponent implements TableCellRenderer {
			private static final long serialVersionUID = 1L;
			
    		// We want headers to look almost the same, but we use our own sort-indicator icons, even for multiple sorting columns.
        	// So I use the default renderer as a delegator which we'll polish a little.
        	private DefaultTableCellRenderer delegateHeaderRenderer = (DefaultTableCellRenderer) getTableHeader().getDefaultRenderer();
        	
        	/** Reference to the icons. */
        	private final Icons icons = MainShell.INSTANCE.getIcons();
        	
			@Override
    		public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, int column ) {
				delegateHeaderRenderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
    			
    			// Column is VIEW idx, convert it to MODEL idx
    			column = table.convertColumnIndexToModel( column );
    			
    			// Sorting icons (primary and secondary; excluding the internal PRIO column)
    			final List< ? extends SortKey > sortKeys = table.getRowSorter().getSortKeys();
    			if ( sortKeys.size() > 1 && sortKeys.get( 1 ).getColumn() == column )
   					delegateHeaderRenderer.setIcon( icons.get( sortKeys.get( 1 ).getSortOrder() == SortOrder.ASCENDING ? XIcon.F_ARROW_090_MEDIUM : XIcon.F_ARROW_270_MEDIUM ) );
    			else if ( sortKeys.size() > 2 && sortKeys.get( 2 ).getColumn() == column )
   					delegateHeaderRenderer.setIcon( icons.get( sortKeys.get( 2 ).getSortOrder() == SortOrder.ASCENDING ? XIcon.F_ARROW_090_SMALL  : XIcon.F_ARROW_270_SMALL  ) );
    			else
    				delegateHeaderRenderer.setIcon( null );
    			
    			delegateHeaderRenderer.setToolTipText( folderPresenter.getColumnList().get( column ).getDescription() );
    			
    			return delegateHeaderRenderer;
    		}
			@Override
			public void updateUI() {
				// Get another instance of delegateHeaderRenderer, which will be initialized with the new UI:
				// Simply delegating the call like SwingUtilities.updateComponentTreeUI( delegateHeaderRenderer ) is not enough!
				delegateHeaderRenderer = (DefaultTableCellRenderer) new JTable().getTableHeader().getDefaultRenderer();
			}
    	};
    	
   		return new MyHeaderCellRenderer();
    }
    
	@Override
	public boolean isCellEditable( final int row, final int column ) {
	    return false;
	}
	
	@Override
	public TableCellRenderer getCellRenderer( final int row, final int column ) {
		return tableCellRenderer;
	}
	
    /**
     * Refreshes the view idx of the SIZE column.
     */
    public void refreshViewColIdx() {
    	tableCellRenderer.refreshViewColIdx();
    }
    
    /**
     * Refreshes the theme.
     */
    private void refreshTheme() {
    	tableCellRenderer.refreshTheme();
    	
    	// Determine new row height.
		// Passing row=-1 to let the renderer know about this...
		setRowHeight( tableCellRenderer.getTableCellRendererComponent( this, "Test", false, false,  -1,  0 ).getPreferredSize().height );
		
    	// Set the table scroll pane's background
		getParent().setBackground( AcUtils.getFolderPresenterTheme().getColor( ColorEntity.BACKGROUND, active ? EnumSet.of( ColorEntityFlag.ACTIVE ) : EnumSet.noneOf( ColorEntityFlag.class ) ) );
		
		repaint();
    }
    
    /**
     * Focuses the specified row.<br>
     * Also makes the row visible (scrolls to it).
     * @param viewRowIdx VIEW index of row to be focused 
     */
    public void setFocusedRow( final int viewRowIdx ) {
		// Select first row. VIEW index is specified as selectable index.
		getSelectionModel().setSelectionInterval( viewRowIdx, viewRowIdx );
		
		// Make it visible (scroll to it)
		scrollRectToVisible( new Rectangle( getCellRect( viewRowIdx, 0, true ) ) );
    }
    
    /**
     * Returns the VIEW index of the focuses row, -1 if no row is focused.
     * @return the VIEW index of the focuses row, -1 if no row is focused 
     */
    public int  getFocusedRowViewIdx() {
    	return getSelectedRow();
    }
    
	/**
	 * Returns the data value at the specified row and column.
	 * @param viewRowIdx VIEW row index
	 * @param column     column
	 * @return the data value at the specified row and column
	 */
    public < T > T getValueAtView( final int viewRowIdx, final IColumn< T > column ) {
	    return getValueAtModel( convertRowIndexToModel( viewRowIdx ), column );
	}
    
	/**
	 * Returns the data value at the specified row and column.
	 * @param modelRowIdx MODEL row index
	 * @param column      colum
	 * @return the data value at the specified row and column
	 */
    public < T > T getValueAtModel( final int modelRowIdx, final IColumn< T > column ) {
	    return Utils.cast( getModel().getValueAt( modelRowIdx, folderPresenter.getColumnModelIndex( column ) ) );
	}
    
    /**
     * Sets whether the table is active.
     * @param active true if the table is active
     */
    public void setActive( final boolean active ) {
    	this.active = active;
    	tableCellRenderer.setActive( active );
    	// Set the table scroll pane's background
		getParent().setBackground( AcUtils.getFolderPresenterTheme().getColor( ColorEntity.BACKGROUND, active ? EnumSet.of( ColorEntityFlag.ACTIVE ) : EnumSet.noneOf( ColorEntityFlag.class ) ) );
    }
    
}
