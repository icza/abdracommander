/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.iface.Receiver;
import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.abdracmd.service.icon.Icons;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.smp.BasePresenter;
import com.abdracmd.smp.mainframe.MainFrameShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderShell;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;
import com.abdracmd.util.SizeFormat;

/**
 * Folders tabbed presenter.
 * 
 * @author Andras Belicza
 */
public class FoldersTabbedPresenter extends BasePresenter< FoldersTabbedModel, FoldersTabbedShell, JPanel > implements ChangeListener {
	
	/** Button to display root.          */
	private final JXButton   rootButton        = new JXButton();
	/** Popup menu to display roots.     */
	private final JPopupMenu rootsPopupMenu    = new JPopupMenu();
	/** Text field displaying full path. */
	private final JTextField fullPathTextField = new JTextField();
	
	/** Tabbed pane swing component. */
	private final JTabbedPane tabbedPane = new JTabbedPane();
	
	/** Tells if we're the active folders tabbed presenter.
	 * Not necessarily the "has focus" state (we might be the active even if <code>table.hasFocus()</code> returns false). */
	private boolean active;
	
    /**
     * Creates a new FoldersTabbedPresenter.
     * @param shell reference to the shell
     */
	protected FoldersTabbedPresenter( final FoldersTabbedShell shell ) {
    	super( shell, new JPanel( new BorderLayout() ) );
    	
    	// Build header; shared visual and control components across tabs
    	final JPanel headerPanel = new JPanel( new BorderLayout() );
    	refreshRootsPopupMenu();
    	rootButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
    			showRootsPopupMenu();
			}
		} );
    	headerPanel.add( rootButton, BorderLayout.WEST );
    	fullPathTextField.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event) {
				final FolderShell selectedFolderShell = shell.getSelectedFolderShell();
				try {
					selectedFolderShell.setPath( Paths.get( fullPathTextField.getText() ).toAbsolutePath() );
					selectedFolderShell.getPresenter().getTable().requestFocusInWindow();
				} catch ( final Exception e ) {
					debug( "Could not set entered folder: " + fullPathTextField.getText(), e );
					AcUtils.beepError();
				}
			}
		} );
    	headerPanel.add( fullPathTextField, BorderLayout.CENTER );
    	component.add( headerPanel, BorderLayout.NORTH );
    	
    	component.add( tabbedPane, BorderLayout.CENTER );
    	
    	tabbedPane.addChangeListener( this );
    	
		// Add the "New tab" tab
    	// Tab content: an info/status label:
		final JLabel newTabLabel = new JLabel( get( TextKey.FOLDER_TAB$OPENING_NEW_TAB ), get( XIcon.WAITING ), JLabel.CENTER );
		
		newTabLabel.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentShown( final ComponentEvent event ) {
				shell.addNewTab( shell.getSelectedFolderShell() == null ? null : shell.getSelectedFolderShell().getPath() );
			}
		} );
		tabbedPane.insertTab( null, null, newTabLabel, get( TextKey.FOLDER_TAB$OPEN_NEW_TAB_TTIP ), 0 );
		
		tabbedPane.setTabComponentAt( 0, new JLabel( get( XIcon.F_PLUS_SMALL ) ) );
		
		// Middle click on tab title should close the tab
		tabbedPane.addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				if ( event.getButton() == GuiUtils.MOUSE_BTN_MIDDLE ) {
    				// Find clicked folder tab
    				for ( final FolderTab folderTab : model.getFolderTabList() )
    					if ( folderTab.tabTitleBox.contains( SwingUtilities.convertPoint( component, event.getPoint(), folderTab.tabTitleBox ) ) ) {
    						shell.removeFolderTab( folderTab );
    						break;
    					}
				}
    			else if ( event.getButton() == GuiUtils.MOUSE_BTN_RIGHT )
    				showTabSetupPopupMenu( event );
			};
		} );
		
		refreshTabLayoutPolicy();
		refreshTabPlacement   ();
		
		// Follow the active folders tabbed presenter:
		model.mainFrameShell.addPropertyChangeListener( MainFrameShell.PROP_ACTIVE_FOLDERS_TABBED_SHELL, new PropertyChangeListener() {
			@Override
			public void propertyChange( final PropertyChangeEvent event ) {
				final boolean newActive = event.getNewValue() == shell;
				if ( active != newActive ) {
					active = newActive;
					// On startup once this is null
					// I still set the active state but omit calling the folder presenter's setActive().
					// This has a pleasant side effect of the left folders tabbed table will be rendered as active - properly! :)
					if ( shell.getSelectedFolderShell() != null )
						shell.getSelectedFolderShell().getPresenter().setActive( active );
				}
			}
		} );
		
		// Since newTab is the only tab, adding it will select it, and newTab on being selected (being shown) will auto-add a new tab (but only one)
    }
    
	/**
	 * Refreshes the roots popup menu.
	 */
	public void refreshRootsPopupMenu() {
    	// Populating the root popup menu takes noticeable time, use SwingWorker for performance boost :)
		GuiUtils.runInEDT( new Runnable() {
			@Override
			public void run() {
				rootsPopupMenu.removeAll();
				rootsPopupMenu.setLayout( new FlowLayout() );
				rootsPopupMenu.add( GuiUtils.changeFontToItalic( new JLabel( get( TextKey.FOLDER_TAB$POPULATING ), get( XIcon.WAITING ), SwingConstants.LEFT ) ) );
				rootsPopupMenu.pack();
			}
		} );
		
    	new SwingWorker< List< RootInfo >, Object >() {
			@Override
			protected List< RootInfo > doInBackground() throws Exception {
				final List< RootInfo > rootInfoList = new ArrayList<>();
				
    	    	for ( final Path root : FileSystems.getDefault().getRootDirectories() ) {
    	    		if ( isCancelled() )
    	    			return rootInfoList;
    	    		rootInfoList.add( new RootInfo( root ) );
    	    	}
    	    	
				return rootInfoList;
			}
			
			@Override
			protected void done() {
				List< RootInfo > rootInfoList = null;
				try {
					rootInfoList = get();
				} catch ( final Exception e ) {
					error( "Could not get root info list!", e );
				}
				
				rootsPopupMenu.removeAll();
				rootsPopupMenu.setLayout( new GridLayout( rootInfoList.size() + 2, 2 ) );
				
				for ( final RootInfo rootInfo : rootInfoList ) {
	    	    	final JMenuItem menuItem = new JMenuItem( rootInfo.systemDisplayName.isEmpty() ? rootInfo.rootString : rootInfo.systemDisplayName, Icons.getSystemIcon( rootInfo.root, null ) );
    	    		menuItem.setMnemonic( rootInfo.rootString.charAt( 0 ) );
    	    		menuItem.addActionListener( new ActionListener() {
    					@Override
    					public void actionPerformed( final ActionEvent event ) {
    						shell.getSelectedFolderShell().setPath( rootInfo.root );
    						// Remove focus from the button
    						// rootButton.transferFocus() is not (good) enough because it always focuses the left side!
    						shell.getSelectedFolderShell().getPresenter().getTable().requestFocusInWindow();
    					}
    				} );
    	    		
    	    		rootsPopupMenu.add( menuItem );
    	    		
    	    		if ( rootInfo.totalSpace != null && rootInfo.freeSpace != null ) {
    	    			final JProgressBar pb = new JProgressBar( 0, (int) ( rootInfo.totalSpace >> 20 ) );
    	    			pb.setValue( (int) ( ( rootInfo.totalSpace - rootInfo.freeSpace ) >> 20 ) );
    	    			pb.setStringPainted( true );
    	    			pb.setString( AcUtils.getFormattedSize( SizeFormat.AUTO, rootInfo.totalSpace - rootInfo.freeSpace, 1 )
    	    	    			+ Utils.OUT_OF_STRING + AcUtils.getFormattedSize( SizeFormat.AUTO, rootInfo.totalSpace, 1 ) );
    	    			rootsPopupMenu.add( pb );
    	    		}
    	    		else
    	    			rootsPopupMenu.add( new JLabel() );
				}
				
				// Refresh menu item:
    	    	final JMenuItem refreshMenuItem = new JMenuItem();
    	    	GuiUtils.initButton( refreshMenuItem, XIcon.REFRESH, TextKey.ROOT_POPUP$REFRESH );
    	    	refreshMenuItem.addActionListener( new ActionListener() {
					@Override
					public void actionPerformed( final ActionEvent event ) {
						refreshRootsPopupMenu();
					}
				} );
    	    	rootsPopupMenu.addSeparator();
    	    	rootsPopupMenu.addSeparator();
    	    	rootsPopupMenu.add( refreshMenuItem );
    	    	rootsPopupMenu.add( new JLabel() );
				rootsPopupMenu.pack();
			}
		}.execute();
	}
	
	/**
	 * Shows the roots popup menu.
	 */
	public void showRootsPopupMenu() {
		rootsPopupMenu.show( rootButton, rootButton.getX(), rootButton.getY() + rootButton.getHeight() );
	}
	
    /**
     * Shows the tab setup popup menu.
     * @param event reference to the mouse event
     */
    private void showTabSetupPopupMenu( final MouseEvent event ) {
		final JPopupMenu popupMenu = new JPopupMenu();
		JPanel titlePanel = new JPanel( new GridLayout( 1, 1, 0, 5 ) );
		titlePanel.setBorder( BorderFactory.createEmptyBorder( 3, 10, 3, 10 ) );
		titlePanel.add( GuiUtils.changeFontToBold( new JLabel( get( TextKey.FOLDER_TABLE$TAB_SETUP ) ) ) );
		popupMenu.add( titlePanel );
		
		popupMenu.addSeparator();
		
		final JMenu tabLayoutPolicyMenu = new JMenu( get( TextKey.FOLDER_TABLE$TAB_SETUP$TAB_LAYOUT_POLICY ) );
		titlePanel = new JPanel( new GridLayout( 1, 1, 0, 5 ) );
		titlePanel.setBorder( BorderFactory.createEmptyBorder( 3, 10, 3, 10 ) );
		titlePanel.add( new JLabel( get( TextKey.FOLDER_TABLE$TAB_SETUP$TAB_LAYOUT_POLICY$INFO ) ) );
		tabLayoutPolicyMenu.add( titlePanel );
		tabLayoutPolicyMenu.addSeparator();
		final TabLayoutPolicy currentTabLayoutPolicy = TabLayoutPolicy.getFromTabbedPaneConst( tabbedPane.getTabLayoutPolicy() );
		GuiUtils.createRadioButtonMenuItems( tabLayoutPolicyMenu, TabLayoutPolicy.values(), currentTabLayoutPolicy, new Receiver< TabLayoutPolicy >() {
			@Override
			public void receive( final TabLayoutPolicy selectedTabLayoutPolicy ) {
				set( SettingKeys.FOLDER_TABS$LAYOUT_POLICY, selectedTabLayoutPolicy );
				refreshTabLayoutPolicy();
			}
		} );
		popupMenu.add( tabLayoutPolicyMenu );
		
		final JMenu tabPlacementMenu = new JMenu( get( TextKey.FOLDER_TABLE$TAB_SETUP$TAB_PLACEMENT ) );
		final TabPlacement currentTabPlacement = TabPlacement.getFromTabbedPaneConst( tabbedPane.getTabPlacement() );
		GuiUtils.createRadioButtonMenuItems( tabPlacementMenu, TabPlacement.values(), currentTabPlacement, new Receiver< TabPlacement >() {
			@Override
			public void receive( final TabPlacement selectedTabPlacement ) {
				set( SettingKeys.FOLDER_TABS$PLACEMENT, selectedTabPlacement );
				refreshTabPlacement();
			}
		} );
		popupMenu.add( tabPlacementMenu );
		
		popupMenu.addSeparator();
		
		final JMenuItem restoreDefaultSetupMenuItem = new JMenuItem( get( TextKey.FOLDER_TABLE$TAB_SETUP$RESTORE_DEFAULT_SETUP ) );
		restoreDefaultSetupMenuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				reset( SettingKeys.FOLDER_TABS$LAYOUT_POLICY );
				reset( SettingKeys.FOLDER_TABS$PLACEMENT     );
				
				refreshTabLayoutPolicy();
				refreshTabPlacement   ();
			}
		} );
		popupMenu.add( restoreDefaultSetupMenuItem );
		
		popupMenu.show( event.getComponent(), event.getX(), event.getY() );
    }
    
    /**
     * Refreshes the tab layout policy.
     */
    private void refreshTabLayoutPolicy() {
		tabbedPane.setTabLayoutPolicy( get( SettingKeys.FOLDER_TABS$LAYOUT_POLICY ).tabbedPaneConst );
    }
    
    /**
     * Refreshes the tab placement.
     */
    private void refreshTabPlacement() {
		tabbedPane.setTabPlacement( get( SettingKeys.FOLDER_TABS$PLACEMENT ).tabbedPaneConst );
    }
    
    /**
     * Adds a folder tab to the presenter.
     * @param folderTab folder tab to be added
     */
    protected void addFolderTab( final FolderTab folderTab ) {
		final int tabIndex = tabbedPane.getTabCount() - 1;
		tabbedPane.insertTab( null, null, folderTab.folderShell.getPresenter().getComponent(), null, tabIndex );
		
		final JTable table = folderTab.folderShell.getPresenter().getTable();
		table.addFocusListener( new FocusAdapter() {
			@Override
			public void focusGained( final FocusEvent event) {
				model.mainFrameShell.setActiveFoldersTabbedShell( shell );
			}
		} );
		
		// First clear default focus traversal keys, we handle them on our own
		table.setFocusTraversalKeys( KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS   , Utils.< AWTKeyStroke >getEmptySet() );
		table.setFocusTraversalKeys( KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS  , Utils.< AWTKeyStroke >getEmptySet() );
		table.setFocusTraversalKeys( KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS  , Utils.< AWTKeyStroke >getEmptySet() );
		table.setFocusTraversalKeys( KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS, Utils.< AWTKeyStroke >getEmptySet() );
		
		Object actionKey = new Object();
		
		// Switch side panels for TAB and SHIFT+TAB
		actionKey = new Object();
		table.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_TAB, 0 ), actionKey );
		table.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_TAB, InputEvent.SHIFT_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// Transfer focus: select the next focusable component in the 
				table.transferFocus();
			}
		} );
		
		// Cycle through folder tabs for CTRL+TAB and CTRL+SHIFT+TAB
		actionKey = new Object();
		table.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_TAB, InputEvent.CTRL_MASK ), actionKey );
		table.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_TAB, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( tabbedPane.getTabCount() < 2 )
					return;
				final int dir = ( event.getModifiers() & InputEvent.SHIFT_MASK ) == 0 ? 1 : -1;
				int newIdx = tabbedPane.getSelectedIndex() + dir;
				if ( newIdx < 0 )
					newIdx = tabbedPane.getTabCount() - 2;
				if ( newIdx > tabbedPane.getTabCount() - 2 )
					newIdx = 0;
				tabbedPane.setSelectedIndex( newIdx );
			}
		} );
		
		// CTRL+T to open a new tab
		actionKey = new Object();
		table.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_T, InputEvent.CTRL_MASK ), actionKey );
		table.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				shell.addNewTab( shell.getSelectedFolderShell().getPath() );
			}
		} );
		
		final JLabel closeLabel = new JLabel( get( XIcon.F_CROSS_SMALL ) );
		closeLabel.setToolTipText( get( TextKey.GENERAL$CLOSE_TAB_TTIP ) );
		closeLabel.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		final Action closeTabAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				shell.removeFolderTab( folderTab );
			}
		};
		closeLabel.addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				closeTabAction.actionPerformed( null );
			};
		} );
		
		// Register CTRL+W for tab close
		actionKey = new Object();
		table.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_W, InputEvent.CTRL_MASK ), actionKey );
		table.getActionMap().put( actionKey, closeTabAction );
		
		folderTab.tabTitleBox.add( closeLabel );
		tabbedPane.setTabComponentAt( tabIndex, folderTab.tabTitleBox );
		
		// Follow path changes:
		folderTab.folderShell.addPropertyChangeListener( FolderShell.PROP_PATH, new PropertyChangeListener() {
			{ propertyChange( null ); } // To initialize
			@Override
			public void propertyChange( final PropertyChangeEvent event ) {
	    		final Path   path       = folderTab.folderShell.getPath();
	    		final String pathString = Utils.getPathString( path );
	    		folderTab.titleLabel.setText( path.getNameCount() == 0 ? pathString : Utils.getPathString( path.getFileName() ) );
	    		tabbedPane.setToolTipTextAt( tabbedPane.indexOfTabComponent( folderTab.tabTitleBox ), pathString );
	    		SwingUtilities.invokeLater( new Runnable() {
	    			@Override
	    			public void run() {
	    	    		refreshHeader();
	    			}
	    		} );
			}
		} );
		
		tabbedPane.setSelectedIndex( tabIndex );
		
		// On startup 2 tabs are added: a "New tab" and a default tab.
		// Only request focus if more than 3 tabs to avoid the initial "focus-war".
		if ( tabbedPane.getTabCount() > 2 )
			GuiUtils.requestFocusInWindowLater( table );
    }
    
    /**
     * Removes a folder tab from the presenter.
     * @param folderTab folder tab to be removed
     */
    protected void removeFolderTab( final FolderTab folderTab ) {
    	final int tabIdx = tabbedPane.indexOfTabComponent( folderTab.tabTitleBox );
    	
    	// If the selected tab is being removed and it is the last tab,
    	// first select a (left) neighbor preventing the "New tab" being selected (and auto-adding a new tab)
    	if ( tabIdx == tabbedPane.getSelectedIndex() && tabbedPane.getSelectedIndex() == tabbedPane.getTabCount() - 2 )
    		tabbedPane.setSelectedIndex( tabbedPane.getTabCount() - 3 );
    	
    	tabbedPane.removeTabAt( tabIdx );
    }
    
	/**
	 * Handles tab selection change events.
	 */
    @Override
	public void stateChanged( final ChangeEvent event ) {
    	final int selectedIndex = tabbedPane.getSelectedIndex();
    	
    	// When "New tab" tab is selected, index equals to size()...
    	if ( selectedIndex == model.getFolderTabList().size() )
    		return;
    	
    	final FolderTab folderTab = model.getFolderTabList().get( selectedIndex );
    	model.setSelectedFolderTab( folderTab );
    	folderTab.folderShell.getPresenter().setActive( active );    	
    	
		refreshHeader();
		
    	// Make the selected tab's table focused:
    	model.getTable().requestFocusInWindow();
    	
    	if ( get( SettingKeys.FOLDER_TABS$REFRESH_ON_SELECT ) )
    		folderTab.folderShell.getPresenter().refresh();
	}
	
    /**
     * Refreshes the header components.
     */
    private void refreshHeader() {
    	final Path path = shell.getSelectedFolderShell().getPath();
    	
    	// TODO should take a param "boolean rootMayChanged", and only refresh root button if it's true
    	// Path is absolute so there is always a root
    	
    	if ( Utils.isVirtualFS( path ) ) {
    		// If we're in a VFS, set the icon of the source path of the VFS as the ROOT icon,
    		// and the name of the root of the source path as the root name.
    		// Also include the source path of the VFS in the full path text field.
    		final Path source = Utils.getVirtualFSSource( path );
    		try {
				rootButton.setIcon( Icons.getSystemIcon( source, Files.readAttributes( source, BasicFileAttributes.class ) ) );
			} catch ( final IOException ie ) {
				rootButton.setIcon( null );
			}
        	rootButton.setText( Utils.getPathString( source.getRoot() ) );
        	
        	fullPathTextField.setText( Utils.getPathString( source )+ Utils.getPathString( path ) );
    	}
    	else {
        	final Path root = path.getRoot();
        	rootButton.setIcon( Icons.getSystemIcon( root, null ) );
        	rootButton.setText( Utils.getPathString( root ) );
        	
        	fullPathTextField.setText( Utils.getPathString( path ) );
    	}
    }
    
}
