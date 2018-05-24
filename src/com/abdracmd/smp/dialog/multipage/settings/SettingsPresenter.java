/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage.settings;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.abdracmd.DevConsts;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.service.settings.SettingLevel;
import com.abdracmd.service.settings.SettingSetChangeListener;
import com.abdracmd.service.settings.SettingText;
import com.abdracmd.service.settings.Settings;
import com.abdracmd.service.settings.viewhint.ViewHints;
import com.abdracmd.smp.dialog.multipage.BaseMultiPageDialogModel;
import com.abdracmd.smp.dialog.multipage.BaseMultiPageDialogPresenter;
import com.abdracmd.smp.dialog.multipage.settings.pageimpl.SettingsNodePage;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;

/**
 * This is the Settings presenter.
 * 
 * @author Andras Belicza
 */
class SettingsPresenter extends BaseMultiPageDialogPresenter< BaseMultiPageDialogModel, SettingsShell > implements SettingSetChangeListener {
	
	/** Temp settings to work with until changes are applied. */
	private final Settings tempSettings;
	
	/** The apply button. */
	private final JXButton applyButton = new JXButton();
	
	/**
	 * Creates the setting page list (hierarchy) filtered by the setting level taken from the settings.
	 * 
	 * @param searchText search text, only show settings whose text contains containing this
	 * 
	 * @return the setting page list (hierarchy) filtered by the setting level taken from the settings.
	 */
	private List< SettingsNodePage > createSettingPageList( String searchText ) {
		if ( searchText != null )
			searchText = searchText.toLowerCase();
		
		final SettingLevel settingLevel = get( SettingKeys.APP$SETTING_LEVEL );
		
		final List< SettingsNodePage > settingPageList = new ArrayList<>();
		
		for ( final SettingKey< ? > settingKey : SettingKey.getSettingKeyList() ) {
			// Filter by setting level
			if ( settingLevel.compareTo( settingKey.level ) < 0 )
				continue;
			
			if ( searchText != null ) {
				boolean matches = false;
				// Filter by search text
				if ( AcUtils.getEnumText( SettingText.valueOf( settingKey.name ) ).toLowerCase().contains( searchText ) )
					matches = true;
				if ( !matches )
					for ( final TextKey textKey : settingKey.viewHints.getValueList( ViewHints.SUBSEQUENT_TEXT ) )
						if ( get( textKey ).toLowerCase().contains( searchText ) ) {
							matches = true;
							break;
						}
				if ( !matches )
					continue;
			}
			
			// Search for setting node page, create all parent node pages in the path if not yet exist
			SettingsNodePage nodePage = null;
			List< SettingsNodePage > searchPageList = settingPageList;
			
			for ( int i = settingKey.name.indexOf( DevConsts.HIERARCHY_SEPARATOR ); i >= 0; i = settingKey.name.indexOf( DevConsts.HIERARCHY_SEPARATOR, i+1 ), searchPageList = Utils.cast( nodePage.getChildPageList() ) ) {
				final SettingText settingText = SettingText.valueOf( settingKey.name.substring( 0, i ) );
				boolean found = false;
				
				if ( searchPageList != null )
					for ( final SettingsNodePage searchPage : searchPageList )
						if ( searchPage.settingText == settingText ) {
							nodePage = searchPage;
							found = true;
							break;
						}
				
				if ( !found ) {
					// Create page
					final SettingsNodePage newNodePage = new SettingsNodePage( settingText, tempSettings );
					if ( nodePage == null ) // Root level
						settingPageList.add( newNodePage );
					else
						nodePage.addChildPage( newNodePage );
					nodePage = newNodePage;
				}
			}
			
			nodePage.addSettingKey( settingKey );
		}
		
		return settingPageList;
	}
	
    /**
     * Creates a new SettingsPresenter.
     * @param shell reference to the shell
     */
    protected SettingsPresenter( final SettingsShell shell  ) {
    	super( shell );
    	
    	setTitle( TextKey.SETTINGS$TITLE );
    	setIcon( XIcon.F_GEAR );
    	
		// Clone main settings!
		tempSettings = MainShell.INSTANCE.getSettings().cloneSettings();
		tempSettings.setChangeTrackingEnabled( true );
    	
    	// Filters panel
    	final JPanel     filterPanel      = new JPanel( new FlowLayout( FlowLayout.CENTER, 0, 5 ) );
    	final JTextField searchTextField  = new JTextField( 10 );
    	final JLabel     clearSearchLabel = new JLabel( get( XIcon.F_CROSS ) );
    	
    	final ActionListener rebuildPageTreeListener = new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final String searchText = searchTextField.getText();
				
				final boolean searchActive = !searchText.isEmpty();
				shell.setPageList( createSettingPageList( searchActive ? searchText : null ) );
				
				// Indicate with green background if search is active!
				searchTextField.setBackground( searchActive ? Color.GREEN : null );
		    	clearSearchLabel.setEnabled( searchActive );
			}
		};
		
    	// Setting level
    	filterPanel.add( new JLabel( get( TextKey.SETTINGS$VIEW ) + Utils.COLON_STRING ) );
    	filterPanel.add( GuiUtils.createSettingComboBox( SettingKeys.APP$SETTING_LEVEL, rebuildPageTreeListener ) );
    	// Search
    	filterPanel.add( Box.createHorizontalStrut( 15 ) );
    	filterPanel.add( new JLabel( get( TextKey.SETTINGS$SEARCH ) + Utils.COLON_STRING ) );
    	// Clear search by pressing ESC
    	searchTextField.addKeyListener( new KeyAdapter() {
    		@Override
    		public void keyPressed( final KeyEvent event ) {
    			if ( event.getKeyCode() == KeyEvent.VK_ESCAPE ) {
    				// ESC is also used to hide (close) dialogs:
    				if ( !searchTextField.getText().isEmpty() ) {
    					event.consume();
    					searchTextField.setText( null );
    				}
    			}
    		}
		} );
    	searchTextField.getDocument().addDocumentListener( new DocumentListener() {
			@Override public void removeUpdate( final DocumentEvent event ) { changedUpdate( event ); }
			@Override public void insertUpdate( final DocumentEvent event ) { changedUpdate( event ); }
			@Override 
			public void changedUpdate( final DocumentEvent event ) {
				rebuildPageTreeListener.actionPerformed( null );
			}
		} );
    	filterPanel.add( searchTextField );
    	clearSearchLabel.setToolTipText( get( TextKey.SETTINGS$CLEAR_SEARCH_TTIP ) );
    	clearSearchLabel.addMouseListener( new MouseAdapter() {
    		@Override
    		public void mouseClicked( final MouseEvent event ) {
    			searchTextField.setText( null );
    		}
		} );
    	filterPanel.add( clearSearchLabel );
    	component.getContentPane().add( filterPanel, BorderLayout.NORTH );
    	
    	// Build initial settings content.
    	rebuildPageTreeListener.actionPerformed( null );
			
    	// Focus the search text field
    	setFocusTargetOnFirstShow( searchTextField );
    	
    	// Register CTRL+F to focus the search text field
    	// Register hotkeys for the table
		final Object actionKey = new Object();
		component.getLayeredPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_F, InputEvent.CTRL_MASK ), actionKey );
		component.getLayeredPane().getActionMap().put( actionKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed( final ActionEvent event ) {
				searchTextField.requestFocusInWindow();
			}
		} );
    	
		initButtonsPanel();
		final JXButton okButton = new JXButton();
		component.getRootPane().setDefaultButton( okButton );
		GuiUtils.initButton( okButton, null, TextKey.GENERAL$BTN$OK );
		applyButton.setEnabled( false );
		GuiUtils.initButton( applyButton, null, TextKey.GENERAL$BTN$APPLY );
		final ActionListener okApplyActionListener = new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				tempSettings.copyChangedSettingsTo( getSettings() );
				if ( event.getSource() == okButton ) {
					component.dispose();
				}
				else if ( event.getSource() == applyButton ) {
					tempSettings.clearTrackedChanges();
					applyButton.setEnabled( false );
				}
			}
		};
		okButton.addActionListener( okApplyActionListener );
		applyButton.addActionListener( okApplyActionListener );
		buttonsPanel.add( okButton );
		buttonsPanel.add( applyButton );
		addCloseButton( TextKey.GENERAL$BTN$CANCEL );
    	
		// Only enable apply when there are changes!
		tempSettings.addChangeListener( new HashSet<>( SettingKey.getSettingKeyList() ), this );
    }
    
	@Override
	public void valuesChanged( final Set< SettingKey< ? > > settingKeySet ) {
		applyButton.setEnabled( true );
	}
	
}
