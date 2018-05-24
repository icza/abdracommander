/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage.settings.pageimpl;

import hu.belicza.andras.util.TypeList;
import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.iface.SimpleProvider;
import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.FixedValuesSettingKey;
import com.abdracmd.service.settings.RangeSettingKey;
import com.abdracmd.service.settings.SettingKey;
import com.abdracmd.service.settings.SettingText;
import com.abdracmd.service.settings.Settings;
import com.abdracmd.service.settings.viewhint.ViewHints;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;

/**
 * A settings node page.
 * 
 * @author Andras Belicza
 */
public class SettingsNodePage extends BasePage {
	
	/** Setting text of the node. */
	public final SettingText          settingText;
	/** Temp settings to work with until changes are applied. */
	private final Settings            tempSettings;
	/** List of setting keys of the page. */
	protected List< SettingKey< ? > > settingKeyList;
	
    /**
     * Creates a new SettingsNodePage.
     * @param settingText  setting text to specify display text and icon
     * @param tempSettings temp settings to work with until changes are applied
     */
    public SettingsNodePage( final SettingText settingText, final Settings tempSettings ) {
    	super( AcUtils.getEnumText( settingText ), settingText.xicon == null ? null : MainShell.INSTANCE.get( settingText.xicon ) );
    	
    	this.settingText  = settingText;
    	this.tempSettings = tempSettings;
    }
    
	/**
	 * Adds a setting key to this page.
	 * @param settingKey setting key to be added
	 */
    public void addSettingKey( final SettingKey< ? > settingKey ) {
		if ( settingKeyList == null )
			settingKeyList = new ArrayList<>();
		
		settingKeyList.add( settingKey );
	}
    
	@Override
	public JComponent createPage() {
		final JPanel panel = new JPanel( new GridBagLayout() );
		
		final GridBagConstraints c = new GridBagConstraints();
		c.gridy  = -1;
		// defaults:
		c.anchor = GridBagConstraints.LINE_START;
		c.fill   = GridBagConstraints.HORIZONTAL;
		c.insets.set( 2, 1, 2, 1 );
		
		for ( final SettingKey< ? > settingKey : settingKeyList ) {
			c.gridy++;
			
			final ViewHints viewHints = settingKey.viewHints;
			
			final String displayText = AcUtils.getEnumText( SettingText.valueOf( settingKey.name ) );
			
			final JComponent settingComponent;
			
			// For Boolean type we use a JCheckBox with the display text, in all other cases use a JLabel for that: 
			if ( settingKey.type != Boolean.class )
				panel.add( new JLabel( displayText + Utils.COLON_STRING ), c );
			
			if ( settingKey instanceof FixedValuesSettingKey ) {
				
				// Combo box with valid values
				panel.add( settingComponent = GuiUtils.createSettingComboBox( (FixedValuesSettingKey< ? >) settingKey, null, tempSettings ), c );
				
			} else if ( settingKey instanceof RangeSettingKey ) {
				
				// Spinner
				if ( Number.class.isAssignableFrom( settingKey.type ) )
					panel.add( settingComponent = GuiUtils.createSettingSpinner( Utils.< RangeSettingKey< ? extends Number > >cast( settingKey ) , null, tempSettings ), c );
				else
					throw new RuntimeException( "Unhandled range setting value type: " + settingKey.type );
				
			} else if ( settingKey.type == Boolean.class ) {
				
				// Check box
				c.gridwidth = 2;
				c.fill = GridBagConstraints.NONE;
				panel.add( settingComponent = GuiUtils.createSettingCheckBox( Utils.< SettingKey< Boolean > >cast( settingKey ), null, tempSettings ), c );
				c.gridwidth = 1;
				c.fill = GridBagConstraints.HORIZONTAL;
				
			} else if ( settingKey.type == String.class ) {
				
				panel.add( settingComponent = GuiUtils.createSettingTextField( settingKey, null, tempSettings ), c );
				
			} else if ( settingKey.type == TypeList.class ) {
				
				final Class< ? extends JComponent > compType = viewHints.getSingleValue( ViewHints.COMPONENT_TYPE );
				if ( compType == JTextField.class ) {
					panel.add( settingComponent = GuiUtils.createSettingTextField( settingKey, null, tempSettings ), c );
				}
				else
					throw new RuntimeException( "Unhandled designated component type: " + compType );
				
			} else {
				
				throw new RuntimeException( "Unhandled setting type: " + settingKey.name );
				
			}
			
			// Process tool tip providers
			for ( final SimpleProvider< String > toolTipProvider : viewHints.getValueList( ViewHints.TOOL_TIP_TEXT_PROVIDER ) )
				settingComponent.setToolTipText( toolTipProvider.provide() );
			
			// Process tool tip texts
			for ( final TextKey textKey : viewHints.getValueList( ViewHints.TOOL_TIP_TEXT ) )
				settingComponent.setToolTipText( get( textKey ) );
			
			// Process subsequent text hints
			final JPanel subsequentTextsPanel = new JPanel( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
			for ( final TextKey textKey : viewHints.getValueList( ViewHints.SUBSEQUENT_TEXT ) )
				subsequentTextsPanel.add( new JLabel( get( textKey ) ) );
			panel.add( subsequentTextsPanel, c );
			
			c.insets.left += 5;
			// Restore default value button
			final JXButton defaultButton = new JXButton( get( TextKey.SETTINGS$DEFAULT_BTN ) );
			defaultButton.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					if ( settingComponent instanceof JCheckBox )
						( (JCheckBox) settingComponent ).setSelected( (Boolean) settingKey.defaultValue );
					else if ( settingComponent instanceof JComboBox )
						( (JComboBox< ? >) settingComponent ).setSelectedItem( settingKey.defaultValue );
					else if ( settingComponent instanceof JSpinner )
						( (JSpinner) settingComponent ).setValue( settingKey.defaultValue );
					else if ( settingComponent instanceof JTextField ) {
						if ( settingKey.type == TypeList.class )
							( (JTextField) settingComponent ).setText( ( ( (TypeList< ? >) settingKey.defaultValue ).convertToString( ',' ) ) );
						else
							( (JTextField) settingComponent ).setText( Utils.simpleToString( settingKey.defaultValue ) );
					}
					else
						throw new RuntimeException( "Unhandled setting component: " + settingComponent.getClass() );
					// Setting the value of the setting component does not trigger events (so reset setting value manually):
					tempSettings.reset( settingKey );
				}
			} );
			panel.add( defaultButton, c );
			c.insets.left -= 5;
		}
		
		return GuiUtils.wrapInPanel( panel );
	}
	
}
