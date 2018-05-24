/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.iface.Receiver;
import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.abdracmd.Consts;
import com.abdracmd.action.AboutAction;
import com.abdracmd.action.ExitAction;
import com.abdracmd.action.NewEmailAction;
import com.abdracmd.action.NewWindowAction;
import com.abdracmd.action.RegInfoAction;
import com.abdracmd.action.SaveSettingsNowAction;
import com.abdracmd.action.SaveSettingsOnExitAction;
import com.abdracmd.action.SettingsAction;
import com.abdracmd.action.ShowCommandBarAction;
import com.abdracmd.action.ShowToolBarAction;
import com.abdracmd.action.SplitVerticallyAction;
import com.abdracmd.action.SystemInfoAction;
import com.abdracmd.action.cmdbar.CloseAction;
import com.abdracmd.action.cmdbar.CopyAction;
import com.abdracmd.action.cmdbar.DeleteAction;
import com.abdracmd.action.cmdbar.EditAction;
import com.abdracmd.action.cmdbar.MoveAction;
import com.abdracmd.action.cmdbar.NewFolderAction;
import com.abdracmd.action.cmdbar.RefreshAction;
import com.abdracmd.action.cmdbar.ViewAction;
import com.abdracmd.action.url.HomePageAction;
import com.abdracmd.action.url.OfficialForumAction;
import com.abdracmd.action.url.RegistrationPageAction;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.service.settings.SettingSetChangeListener;
import com.abdracmd.smp.BasePresenter;
import com.abdracmd.smp.dialog.regnote.RegNoteShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.IFolderPresenterTheme;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;

/**
 * This is the main frame presenter.
 * 
 * @author Andras Belicza
 */
public class MainFramePresenter extends BasePresenter< MainFrameModel, MainFrameShell, JFrame > implements SettingSetChangeListener {
	
	/** Actions of the command bar (and the 'Command bar' menu). */
	private static final Action[] COMMAND_BAR_ACTIONS = new Action[] { 
		ViewAction     .INSTANCE, EditAction  .INSTANCE, CopyAction   .INSTANCE, MoveAction .INSTANCE,
		NewFolderAction.INSTANCE, DeleteAction.INSTANCE, RefreshAction.INSTANCE, CloseAction.INSTANCE
	};
	
	/** Use this as the content page, and leave side components of the original content pane empty,
	 * so the tool bar may be placed at each side. */
	private final JPanel     contentPane = new JPanel( new BorderLayout() );
	
	/** Tool bar.                                            */
	private final JToolBar   toolBar     = new JToolBar( get( TextKey.TOOLBAR$TITLE ) );
	/** Split pane we use to layout the 2 folder presenters. */
	private final JSplitPane splitPane   = new JSplitPane();
	/** Panel of the command bar.                            */
	private final JPanel     cmdBarPanel = new JPanel( new GridLayout( 1, 8 ) );
	
	/** Reference to the Save settings on exit menu item.    */
	private JCheckBoxMenuItem saveSettingsOnExitMenuItem = new JCheckBoxMenuItem( SaveSettingsOnExitAction.INSTANCE );
	
	/** Reference to the Look and Feels menu. */
	private final JMenu lafsMenu   = new JMenu();
	/** Reference to the Themes menu.         */
	private final JMenu themesMenu = new JMenu();
	
    /**
     * Creates a new MainFramePresenter.
     * @param shell reference to the shell
     */
	protected MainFramePresenter( final MainFrameShell shell ) {
    	super( shell, new JFrame() );
    	
    	component.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
    	
    	component.setFocusTraversalPolicy( new FocusTraversalPolicy() {
			@Override
			public Component getLastComponent( final Container aContainer ) {
				return model.rightFoldersTabbedShell.getTable();
			}
			@Override
			public Component getFirstComponent( final Container aContainer ) {
				return model.leftFoldersTabbedShell.getTable();
			}
			@Override
			public Component getDefaultComponent( final Container aContainer ) {
				return model.leftFoldersTabbedShell.getTable();
			}
			@Override
			public Component getComponentBefore( final Container aContainer, final Component aComponent ) {
				return aComponent == model.leftFoldersTabbedShell.getTable() ? model.rightFoldersTabbedShell.getTable() : model.leftFoldersTabbedShell.getTable();
			}
			@Override
			public Component getComponentAfter( final Container aContainer, final Component aComponent ) {
				return aComponent == model.leftFoldersTabbedShell.getTable() ? model.rightFoldersTabbedShell.getTable() : model.leftFoldersTabbedShell.getTable();
			}
		} );
    	
    	component.setIconImage( get( XIcon.MY_APP_ICON_BIG ).getImage() );
    	
    	component.addWindowListener( new WindowAdapter() {
    		private boolean first = true;
    		@Override
    		public void windowActivated( final WindowEvent event ) {
    			if ( first ) {
		    		first = false;
		    		SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
		    		    	// Show reg note dialog here because it requires an active main frame (as parent)
		    		    	if ( mainModel.getRegistrationInfo() == null )
		    		    		new RegNoteShell();
						}
					} );
    			}
    			else {
    		    	if ( get( SettingKeys.MAIN_FRAME$REFRESH_ON_ACTIVATE ) ) {
    		    		model.leftFoldersTabbedShell .getSelectedFolderShell().getPresenter().refresh();
    		    		model.rightFoldersTabbedShell.getSelectedFolderShell().getPresenter().refresh();
    		    	}
    			}
    		}
    		@Override
    		public void windowClosing( final WindowEvent event ) {
    			shell.shutdown();
    		}
		} );
    	
    	refreshTitle();
    	
		component.getContentPane().add( contentPane, BorderLayout.CENTER );
    	
    	buildMenuBar();
    	buildToolBar();
    	buildFoldersTabbedPanes();
    	buildCommandBar();
    	
    	// Without a second setVisible(), there's a visible "resize/repack". This second call eliminates that.
    	
    	shell.setActiveFoldersTabbedShell( model.leftFoldersTabbedShell );
		
    	registerWindowHotkeys();
    	
    	// Setting listening:
		final Set< SettingKey< ? > > listenedSettingKeySet = Utils.< SettingKey< ? > >asNewSet(
				SettingKeys.APP$LOOK_AND_FEEL, SettingKeys.FOLDER_TABLE$THEME,
				SettingKeys.MAIN_FRAME$SPLIT_CONTINOUS_LAYOUT,
				SettingKeys.APP$SAVE_SETTINGS_ON_EXIT );
		AcUtils.CAH.addChangeListener( listenedSettingKeySet, this );
		// Init setting dependent code:
		valuesChanged( listenedSettingKeySet );
		
		component.setVisible( true );
    	// Set a default size so if the user uses the "Restore size" (un-maximize), the window will have a useful size.
		// Commented out for now because it results in a visual resize.
		//GuiUtils.maximizeWindowWithMargin( component, 40, null );
		component.setExtendedState( Frame.MAXIMIZED_BOTH );
		component.toFront();
    }
    
	@Override
	public void valuesChanged( final Set< SettingKey< ? > > settingKeySet ) {
		if ( settingKeySet.contains( SettingKeys.APP$LOOK_AND_FEEL ) ) {
			// Select the appropriate menu item
			final String lafName = get( SettingKeys.APP$LOOK_AND_FEEL );
			for ( int i = 0; i < lafsMenu.getMenuComponentCount(); i++ ) {
				final JRadioButtonMenuItem menuItem = (JRadioButtonMenuItem) lafsMenu.getMenuComponent( i );
				if ( menuItem.getText().equals( lafName ) ) {
					menuItem.setSelected( true );
					break;
				}
			}
		}
		
		if ( settingKeySet.contains( SettingKeys.FOLDER_TABLE$THEME ) ) {
			// Select the appropriate menu item
			final String themeName = mainModel.getFolderPresenterThemeRegistry().get( get( SettingKeys.FOLDER_TABLE$THEME ) ).getDisplayName();
			for ( int i = 0; i < themesMenu.getMenuComponentCount(); i++ ) {
				final JRadioButtonMenuItem menuItem = (JRadioButtonMenuItem) themesMenu.getMenuComponent( i );
				if ( menuItem.getText().equals( themeName ) ) {
					menuItem.setSelected( true );
					break;
				}
			}
		}
		
		if ( settingKeySet.contains( SettingKeys.MAIN_FRAME$SPLIT_CONTINOUS_LAYOUT ) )
	    	splitPane.setContinuousLayout( get( SettingKeys.MAIN_FRAME$SPLIT_CONTINOUS_LAYOUT ) );
		
		if ( settingKeySet.contains( SettingKeys.APP$SAVE_SETTINGS_ON_EXIT ) )
			saveSettingsOnExitMenuItem.setSelected( get( SettingKeys.APP$SAVE_SETTINGS_ON_EXIT ) );
	}
	
    /**
     * Builds the menu bar of the main frame.
     */
    private void buildMenuBar() {
    	final JMenuBar menuBar = new JMenuBar();
		// Remove default action for the F10 key (it is used by the Exit action)
    	menuBar.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( KeyStroke.getKeyStroke( KeyEvent.VK_F10, 0 ), "none" );
		
    	JMenu menu;
    	
    	// File menu
    	{
    		menu = new JMenu();
    		GuiUtils.initButton( menu, XIcon.F_DISK, TextKey.MENU$FILE );
    		
    		GuiUtils.initMenuItem( menu.add( new JMenuItem( NewWindowAction.INSTANCE ) ) );
    		
    		menu.addSeparator();
    		
    		GuiUtils.initMenuItem( menu.add( new JMenuItem( SettingsAction.INSTANCE ) ) );
    		GuiUtils.initMenuItem( menu.add( new JMenuItem( SaveSettingsNowAction.INSTANCE ) ) );
    		GuiUtils.initMenuItem( menu.add( saveSettingsOnExitMenuItem ) );
    		
    		menu.addSeparator();
    		
    		GuiUtils.initMenuItem( menu.add( new JMenuItem( ExitAction.INSTANCE ) ) );
    		
    		menuBar.add( menu );
    	}
    	
    	// Command bar menu
    	{
    		menu = new JMenu();
    		GuiUtils.initButton( menu, XIcon.F_UI_STATUS_BAR, TextKey.MENU$COMMAND_BAR );
    		
        	int i = 2;
    		for ( final Action cmdBarAction : COMMAND_BAR_ACTIONS ) {
        		GuiUtils.initMenuItem( menu.add( new JMenuItem( cmdBarAction ) ) );
        		// Group items based on keyboard layout (space between F4-F5, F8-F9)
        		if ( ( ++i & 0x03 ) == 0 )
        			menu.addSeparator();
    		}
        	
    		menuBar.add( menu );
    	}
    	
    	// View menu
    	{
    		menu = new JMenu();
    		GuiUtils.initButton( menu, XIcon.F_APPLICATION_ICON_LARGE, TextKey.MENU$VIEW );
    		
    		// Look and Feel sub menu
    		{
	    		GuiUtils.initButton( lafsMenu, XIcon.F_UI_FLOW, TextKey.MENU$VIEW$LOOK_AND_FEEL );
	    		
	    		GuiUtils.createRadioButtonMenuItems( lafsMenu, SettingKeys.APP$LOOK_AND_FEEL.validValues, get( SettingKeys.APP$LOOK_AND_FEEL ), new Receiver< String >() {
	    			@Override
	    			public void receive( final String selectedLafName ) {
	    				set( SettingKeys.APP$LOOK_AND_FEEL, selectedLafName );
	    			}
	    		} );
	    		
	    		menu.add( lafsMenu );
    		}
    		
    		// Themes sub menu
    		{
	    		GuiUtils.initButton( themesMenu, XIcon.F_COLOR_SWATCHES, TextKey.MENU$VIEW$THEMES );
	    		
	    		final IFolderPresenterTheme[] folderViewThemes = mainModel.getFolderPresenterThemeRegistry().valueList().toArray( new IFolderPresenterTheme[ 0 ] );
	    		GuiUtils.createRadioButtonMenuItems( themesMenu, folderViewThemes, AcUtils.getFolderPresenterTheme(), new Receiver< IFolderPresenterTheme >() {
	    			@Override
	    			public void receive( final IFolderPresenterTheme selectedTheme ) {
	    				set( SettingKeys.FOLDER_TABLE$THEME, selectedTheme.getClass().getName() );
	    			}
	    		} );
	    		menu.add( themesMenu );
    		}
    		
    		menu.addSeparator();
    		
    		GuiUtils.initMenuItem( menu.add( new JCheckBoxMenuItem( ShowToolBarAction   .INSTANCE ) ) );
    		GuiUtils.initMenuItem( menu.add( new JCheckBoxMenuItem( ShowCommandBarAction.INSTANCE ) ) );
    		
    		menu.addSeparator();
    		
    		GuiUtils.initMenuItem( menu.add( new JCheckBoxMenuItem( SplitVerticallyAction.INSTANCE ) ) );
    		
    		menuBar.add( menu );
    	}
    	
    	// Help menu
    	{
    		menu = new JMenu();
    		GuiUtils.initButton( menu, XIcon.F_QUESTION, TextKey.MENU$HELP );
    		
    		GuiUtils.initMenuItem( menu.add( new JMenuItem( HomePageAction.INSTANCE ) ) );
    		GuiUtils.initMenuItem( menu.add( new JMenuItem( RegistrationPageAction.INSTANCE ) ) );
    		GuiUtils.initMenuItem( menu.add( new JMenuItem( OfficialForumAction.INSTANCE ) ) );
    		
    		menu.addSeparator();
    		
    		GuiUtils.initMenuItem( menu.add( new JMenuItem( RegInfoAction.INSTANCE ) ) );
    		GuiUtils.initMenuItem( menu.add( new JMenuItem( SystemInfoAction.INSTANCE ) ) );
    		
    		menu.addSeparator();
    		
    		GuiUtils.initMenuItem( menu.add( new JMenuItem( AboutAction.INSTANCE ) ) );
    		
    		menuBar.add( menu );
    	}
    	
    	component.setJMenuBar( menuBar );
    }
    
    /**
     * Builds the tool bar of the main frame.
     */
    private void buildToolBar() {
    	refreshToolBarVisibility();
    	toolBar.setFloatable( true ); // TODO Make it a setting in popup menu something like "Lock tool bar"
    	
    	for ( final Action action : new Action[] { 
    			NewWindowAction.INSTANCE, NewEmailAction.INSTANCE, SettingsAction.INSTANCE, AboutAction.INSTANCE
    			} )
    		GuiUtils.initToolBarButton( toolBar.add( action ) );
    	
    	// Tool bar goes to the "real" content pane
    	component.getContentPane().add( toolBar, BorderLayout.NORTH );
    }
    
    /**
     * Builds the folders tabbed pane.
     */
    private void buildFoldersTabbedPanes() {
		// Remove default action for the F8 key (it is used by the Delete action)
		splitPane.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_F8, 0 ), "none" );
		// Remove default action for the F6 key (it is used by the Move action)
		splitPane.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_F6, 0 ), "none" );
    	
    	splitPane.setResizeWeight( 0.5 );
    	splitPane.setOneTouchExpandable( true );
    	
		splitPane.setLeftComponent ( model.leftFoldersTabbedShell .getPresenter().getComponent() );
		splitPane.setRightComponent( model.rightFoldersTabbedShell.getPresenter().getComponent() );
    	
    	contentPane.add( splitPane, BorderLayout.CENTER );
    	
    	// Refresh orientation here, when it is already added to the panel, else splitpane's setDividerLocation() doesn't work (does nothing)
    	refreshSplitOrientation();
    }
    
    /**
     * Builds the command bar.
     */
    private void buildCommandBar() {
    	refreshCommandBarVisibility();
    	
    	for ( final Action cmdBarAction : COMMAND_BAR_ACTIONS )
    		cmdBarPanel.add( GuiUtils.initCmdBarButton( new JXButton( cmdBarAction ) ) );
    	
    	contentPane.add( cmdBarPanel, BorderLayout.SOUTH );
    }
    
    /**
     * Registers window-wide hotkeys.
     */
    private void registerWindowHotkeys() {
    	final JComponent hotkeyRoot = component.getLayeredPane();
    	
    	// Register ALT+F1 to show the left root popup menu
    	Object actionKey = new Object();
    	hotkeyRoot.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_F1, InputEvent.ALT_MASK ), actionKey );
    	hotkeyRoot.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				model.leftFoldersTabbedShell.getPresenter().showRootsPopupMenu();
			}
		} );
    	
    	// Register ALT+F2 to show the right root popup menu
		actionKey = new Object();
		hotkeyRoot.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_F2, InputEvent.ALT_MASK ), actionKey );
		hotkeyRoot.getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				model.rightFoldersTabbedShell.getPresenter().showRootsPopupMenu();
			}
		} );
    	
    }
    
    /**
     * Refreshes the slit orientation.
     */
    public void refreshSplitOrientation() {
    	// Setting tells if we have to split with a vertical line, orientation is the opposite!
    	splitPane.setOrientation( get( SettingKeys.MAIN_FRAME$SPLIT_VERTICALLY ) ? JSplitPane.HORIZONTAL_SPLIT : JSplitPane.VERTICAL_SPLIT );
    	splitPane.setDividerLocation( 0.5 );
    }
    
    /**
     * Refreshes the command bar visibility.
     */
    public void refreshCommandBarVisibility() {
    	cmdBarPanel.setVisible( get( SettingKeys.MAIN_FRAME$SHOW_COMMAND_BAR ) );
    }
    
    /**
     * Refreshes the tool bar visibility.
     */
    public void refreshToolBarVisibility() {
    	toolBar.setVisible( get( SettingKeys.MAIN_FRAME$SHOW_TOOL_BAR ) );
    }
    
    /**
     * Refreshes the frame title.
     */
    public void refreshTitle() {
    	final StringBuilder titleBuilder = new StringBuilder();
    	
    	if ( model.completionPercent != null )
    		titleBuilder.append( model.completionPercent ).append( "% - " );
    	
    	titleBuilder.append( Consts.APP_NAME ).append( Utils.SPACE_STRING ).append( Consts.APP_VERSION_STRING_SHORT );
    	
    	if ( model.frameSequenceNumber != null )
    		titleBuilder.append( " [" ).append( model.frameSequenceNumber ).append( ']' );
    	
    	titleBuilder.append( " - " );
    	
    	titleBuilder.append( mainModel.getRegistrationInfo() == null ? get( TextKey.MAIN_FRAME$NOT_REGISTERED ) : getPersonName( mainModel.getRegistrationInfo().getRegisteredPerson() ) );
    	
    	component.setTitle( titleBuilder.toString() );
    }
    
	/**
	 * Closes the main frame presenter.
	 */
    public void close() {
    	component.dispose();
    }
	
}
