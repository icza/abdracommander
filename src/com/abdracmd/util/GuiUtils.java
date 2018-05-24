/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.util;

import hu.belicza.andras.util.TypeList;
import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.iface.HasDisplayName;
import hu.belicza.andras.util.iface.Provider;
import hu.belicza.andras.util.iface.Receiver;
import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableModel;

import com.abdracmd.Consts;
import com.abdracmd.action.NewEmailAction;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.language.TextWithMnemonic;
import com.abdracmd.service.settings.FixedValuesSettingKey;
import com.abdracmd.service.settings.RangeSettingKey;
import com.abdracmd.service.settings.SettingKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.service.settings.SettingText;
import com.abdracmd.service.settings.Settings;
import com.abdracmd.service.settings.viewhint.ViewHints;
import com.abdracmd.uicomponent.smarttable.SmartTable;
import com.abdracmd.util.bean.person.Contact;
import com.abdracmd.util.bean.person.Contact.ContactType;

/**
 * GUI utilities.
 * 
 * @author Andras Belicza
 */
public class GuiUtils {
	
	/** Constant identifying the left button of the mouse.   */
	public static final int MOUSE_BTN_LEFT   = MouseEvent.BUTTON1;
	/** Constant identifying the middle button of the mouse. */
	public static final int MOUSE_BTN_MIDDLE = MouseEvent.BUTTON2;
	/** Constant identifying the right button of the mouse.  */
	public static final int MOUSE_BTN_RIGHT  = MouseEvent.BUTTON3;
	
	/** A common assess helper instance used locally. */
	private static final CommonAccessHelper CAH = new CommonAccessHelper();
	
	
	/**
	 * Wraps the specified component in a {@link JPanel} with {@link FlowLayout}.
	 * @param component component to be wrapped
	 * @return a panel wrapping the specified component
	 */
	public static JPanel wrapInPanel( final JComponent component ) {
		final JPanel panel = new JPanel();
		panel.add( component );
		return panel;
	}
	
	/**
	 * Wraps the specified component in a {@link JPanel} with {@link BorderLayout}.
	 * @param component component to be wrapped
	 * @return a panel wrapping the specified component
	 */
	public static JPanel wrapInBorderPanel( final JComponent component ) {
		final JPanel panel = new JPanel( new BorderLayout() );
		panel.add( component, BorderLayout.CENTER );
		return panel;
	}
	
	/**
	 * Returns a nice string representation of the specified key stroke.
	 * @param keyStroke key stroke whose nice string representation to return
	 * @return a nice string representation of the specified key stroke
	 */
	public static String keyStrokeToString( final KeyStroke keyStroke ) {
		final StringBuilder sb = new StringBuilder( Utils.SPACE_PARENTHESIS_STRING );
		
		if ( keyStroke.getModifiers() != 0 )
			sb.append( KeyEvent.getKeyModifiersText( keyStroke.getModifiers() ) ).append( '+' );
		
		sb.append( KeyEvent.getKeyText( keyStroke.getKeyCode() ) ).append( ')' );
		
		return sb.toString();
	}
	
	/**
	 * Initializes an action.
	 * 
	 * @param action  action to be initialized
	 * @param xicon   optional xicon of the action
	 * @param textKey text key of the action's text
	 * @param params  parameters of the action's text
	 */
	public static void initAction( final AbstractAction action, final XIcon xicon, final TextKey textKey, final Object...params ) {
		final TextWithMnemonic textWithMnemonic = CAH.getWithMnemonic( textKey, params );
		
		action.putValue( AbstractAction.NAME, textWithMnemonic.text );
		// Set text for tool tip, include key stroke
		final KeyStroke keyStroke = (KeyStroke) action.getValue( AbstractAction.ACCELERATOR_KEY );
		action.putValue( AbstractAction.SHORT_DESCRIPTION, keyStroke == null ? textWithMnemonic.text : textWithMnemonic.text + keyStrokeToString( keyStroke ) );
		
		if ( textWithMnemonic.mnemonic != null )
			action.putValue( AbstractAction.MNEMONIC_KEY, (int) textWithMnemonic.mnemonic );
		
		if ( xicon != null )
			action.putValue( AbstractAction.SMALL_ICON, CAH.get( xicon ) );
	}
	
	/**
	 * Initializes a button.
	 * 
	 * @param button  button to be initialized
	 * @param xicon   optional xicon of the button
	 * @param textKey text key of the button's text
	 * @param params  parameters of the button's text
	 */
	public static void initButton( final AbstractButton button, final XIcon xicon, final TextKey textKey, final Object...params ) {
		final TextWithMnemonic textWithMnemonic = CAH.getWithMnemonic( textKey, params );
		
		button.setText( textWithMnemonic.text );
		
		if ( textWithMnemonic.mnemonic != null )
			button.setMnemonic( textWithMnemonic.mnemonic );
		
		if ( xicon != null )
			button.setIcon( CAH.get( xicon ) );
	}
	
	/**
	 * Initializes a tool bar button.
	 * @param button tool bar button to be initialized
	 */
	public static void initToolBarButton( final JButton button ) {
		// Mnemonics are for menu items, not for tool bar buttons
		button.setMnemonic( '\0' );
	}
	
    /**
	 * Initializes a menu item.
	 * @param menuItem menu item to be initialized
     */
    public static void initMenuItem( final JMenuItem menuItem ) {
    	// Tool tips are for tool bar buttons not for menu items
    	menuItem.setToolTipText( null );
    }
    
	/**
	 * Initializes a command bar button.
	 * @param button command bar button to be initialized
	 * @return the button
	 */
	public static JXButton initCmdBarButton( final JXButton button ) {
		// On command bar buttons always display the hotkey!
		final Action action = button.getAction();
		
		// No mnemonics for cmd bar buttons (they are for the Command bar menu).
		button.setMnemonic( 0 );
		
		if ( action != null ) {
        	final KeyStroke keyStroke = (KeyStroke) action.getValue( AbstractAction.ACCELERATOR_KEY );
        	
        	if ( keyStroke != null ) {
        		button.setText( button.getText() + keyStrokeToString( keyStroke ) );
        		
        		// Register the keystroke "globally"
        		final Object actionKey = new Object();
        		button.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( keyStroke, actionKey );
        		button.getActionMap().put( actionKey, action );
        	}
		}
		
		// Tool tips on command bar buttons are disturbing
		button.setToolTipText( null );
		
		return button;
	}
	
	/**
	 * Creates a check box whose value is bound to the specified settingKey.<br>
	 * The initial value of the check box will be taken from the settings of the main model,
	 * and an action listener will be added to the check box to register changes at the settings of the main model.<br>
	 * The text of the returned check box is specified by the enum text of {@link SettingText},
	 * having the same name as the name of the specified setting key.
	 * 
	 * <p><i>Warning!</i> Updating the setting is done via a registered {@link ActionListener}.
	 * In Swing the execution order of registered listeners is not specified.
	 * Custom listeners added to the returned check box might be called before the setting is updated!
	 * If it is required that the new setting be updated when a custom listener is called,
	 * pass the custom listener as the <code>customListener</code> argument here.</p>
	 * 
	 * @param settingKey key of the settings its value is bound to
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the setting bound the check box is bound to has been updated
	 * @return the created check box
	 */
	public static JCheckBox createSettingCheckBox( final SettingKey< Boolean > settingKey, final ActionListener customListener ) {
		return createSettingCheckBox( settingKey, customListener, CAH.getSettings() );
	}
	
	/**
	 * Creates a check box whose value is bound to the specified settingKey.<br>
	 * The initial value of the check box will be taken from the specified settings,
	 * and an action listener will be added to the check box to register changes at the specified settings.<br>
	 * The text of the returned check box is specified by the enum text of {@link SettingText},
	 * having the same name as the name of the specified setting key.
	 * 
	 * <p><i>Warning!</i> Updating the setting is done via a registered {@link ActionListener}.
	 * In Swing the execution order of registered listeners is not specified.
	 * Custom listeners added to the returned check box might be called before the setting is updated!
	 * If it is required that the new setting be updated when a custom listener is called,
	 * pass the custom listener as the <code>customListener</code> argument here.</p>
	 * 
	 * @param settingKey key of the settings its value is bound to
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the setting bound the returned check box is bound to has been updated
	 * @param settings settings to take the initial value from and to set the changed value to 
	 * @return the created check box
	 */
	public static JCheckBox createSettingCheckBox( final SettingKey< Boolean > settingKey, final ActionListener customListener, final Settings settings ) {
		final JCheckBox checkBox = new JCheckBox( AcUtils.getEnumText( SettingText.valueOf( settingKey.name ) ), settings.get( settingKey ) );
		
		checkBox.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				settings.set( settingKey, checkBox.isSelected() );
				
				if ( customListener != null )
					customListener.actionPerformed( event );
			}
		} );
		
		return checkBox;
	}
	
	/**
	 * Creates a combo box whose value is bound to the specified settingKey.<br>
	 * The initial value of the combo box will be taken from the settings of the main model,
	 * and an action listener will be added to the combo box to register changes at the settings of the main model.
	 * 
	 * <p><i>Warning!</i> Updating the setting is done via a registered {@link ActionListener}.
	 * In Swing the execution order of registered listeners is not specified.
	 * Custom listeners added to the returned combo box might be called before the setting is updated!
	 * If it is required that the new setting be updated when a custom listener is called,
	 * pass the custom listener as the <code>customListener</code> argument here.</p>
	 * 
	 * @param settingKey key of the settings its value is bound to
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the setting bound the returned combo box is bound to has been updated
	 * @param <T> value type of the setting key
	 * @return the created combo box
	 */
	public static < T > JComboBox< T > createSettingComboBox( final FixedValuesSettingKey< T > settingKey, final ActionListener customListener ) {
		return createSettingComboBox( settingKey, customListener, CAH.getSettings() );
	}
	
	/**
	 * Creates a combo box whose value is bound to the specified settingKey.<br>
	 * The initial value of the combo box will be taken from the specified settings,
	 * and an action listener will be added to the combo box to register changes at the specified settings.
	 * 
	 * <p><i>Warning!</i> Updating the setting is done via a registered {@link ActionListener}.
	 * In Swing the execution order of registered listeners is not specified.
	 * Custom listeners added to the returned combo box might be called before the setting is updated!
	 * If it is required that the new setting be updated when a custom listener is called,
	 * pass the custom listener as the <code>customListener</code> argument here.</p>
	 * 
	 * @param settingKey key of the settings its value is bound to
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the setting bound the returned combo box is bound to has been updated
	 * @param settings settings to take the initial value from and to set the changed value to 
	 * @param <T> value type of the setting key
	 * @return the created combo box
	 */
	public static < T > JComboBox< T > createSettingComboBox( final FixedValuesSettingKey< T > settingKey, final ActionListener customListener, final Settings settings ) {
		final JComboBox< T > comboBox = new JComboBox< T >( settingKey.validValues ); 
		
		final List< Provider< Icon, Object > >   iconProviderList        = settingKey.viewHints.getValueList( ViewHints.VALUE_ICON_PROVIDER         );
		final List< Provider< String, Object > > displayNameProviderList = settingKey.viewHints.getValueList( ViewHints.VALUE_DISPLAY_NAME_PROVIDER );
		
		if ( !iconProviderList.isEmpty() || !displayNameProviderList.isEmpty() ) {
			comboBox.setRenderer( new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;
				@Override
				public Component getListCellRendererComponent( final JList< ? > list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus ) {
					super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
					if ( !displayNameProviderList.isEmpty() ) {
						String displayName = null;
						for ( final Provider< String, Object > displayNameProvider : displayNameProviderList ) {
							if ( ( displayName = displayNameProvider.provide( value ) ) != null )
								break;
						}
						setText( displayName );
					}
					if ( !iconProviderList.isEmpty() ) {
						Icon icon = null;
						for ( final Provider< Icon, Object > iconProvider : iconProviderList ) {
							if ( ( icon = iconProvider.provide( value ) ) != null )
								break;
						}
						setIcon( icon );
					}
					return this;
				}
			} );
		}
		
		comboBox.setSelectedItem( settings.get( settingKey ) );
		
		comboBox.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				settings.set( settingKey, comboBox.getItemAt( comboBox.getSelectedIndex() ) );
				
				if ( customListener != null )
					customListener.actionPerformed( event );
			}
		} );
		
		return comboBox;
	}
	
	/**
	 * Creates a spinner whose value is bound to the specified settingKey.<br>
	 * The initial value of the spinner will be taken from the settings of the main model,
	 * and an action listener will be added to the spinner to register changes at the settings of the main model.<br>
	 * 
	 * <p><i>Warning!</i> Updating the setting is done via a registered {@link ActionListener}.
	 * In Swing the execution order of registered listeners is not specified.
	 * Custom listeners added to the returned spinner might be called before the setting is updated!
	 * If it is required that the new setting be updated when a custom listener is called,
	 * pass the custom listener as the <code>customListener</code> argument here.</p>
	 * 
	 * @param settingKey key of the settings its value is bound to
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the setting bound the spinner is bound to has been updated
	 * @param <T> value type of the setting key
	 * @return the created spinner
	 */
	public static < T extends Number & Comparable< T > > JSpinner createSettingSpinner( final RangeSettingKey< T > settingKey, final ActionListener customListener ) {
		return createSettingSpinner( settingKey, customListener, CAH.getSettings() );
	}
	
	/**
	 * Creates a spinner whose value is bound to the specified settingKey.<br>
	 * The initial value of the spinner will be taken from the specified settings,
	 * and an action listener will be added to the spinner to register changes at the specified settings.<br>
	 * 
	 * <p><i>Warning!</i> Updating the setting is done via a registered {@link ActionListener}.
	 * In Swing the execution order of registered listeners is not specified.
	 * Custom listeners added to the returned spinner might be called before the setting is updated!
	 * If it is required that the new setting be updated when a custom listener is called,
	 * pass the custom listener as the <code>customListener</code> argument here.
	 * The <code>actionPerformed()</code> method of the passed custom listener will be called with
	 * a <code>null</code> argument.</p>
	 * 
	 * @param settingKey key of the settings its value is bound to
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the setting bound the returned spinner is bound to has been updated
	 * @param settings settings to take the initial value from and to set the changed value to 
	 * @param <T> value type of the setting key
	 * @return the created spinner
	 */
	public static < T extends Number & Comparable< T > > JSpinner createSettingSpinner( final RangeSettingKey< T > settingKey, final ActionListener customListener, final Settings settings ) {
		final JSpinner spinner = new JSpinner( new SpinnerNumberModel( settings.get( settingKey ), settingKey.minValue, settingKey.maxValue, 1 ) );
		
		spinner.setToolTipText( CAH.get( TextKey.GENERAL$VALID_RANGE, CAH.formatNumber( settingKey.minValue.longValue() ), CAH.formatNumber( settingKey.maxValue.longValue() ), CAH.formatNumber( settingKey.defaultValue.longValue() ) ) );
		
		spinner.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged( final ChangeEvent event ) {
				settings.set( settingKey, Utils.< T >cast( spinner.getValue() ) );
				
				if ( customListener != null )
					customListener.actionPerformed( null );
			}
		} );
		
		return spinner;
	}
	
	/**
	 * Creates a text field whose value is bound to the specified settingKey.<br>
	 * The initial value of the text field will be taken from the settings of the main model,
	 * and a document listener will be added to the text field to register changes at the settings of the main model.<br>
	 * 
	 * <p><i>Warning!</i> Updating the setting is done via a registered {@link DocumentListener}.
	 * In Swing the execution order of registered listeners is not specified.
	 * Custom listeners added to the returned text field might be called before the setting is updated!
	 * If it is required that the new setting be updated when a custom listener is called,
	 * pass the custom listener as the <code>customListener</code> argument here.</p>
	 * 
	 * @param settingKey key of the settings its value is bound to
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the setting bound the text field is bound to has been updated
	 * @param <T> value type of the setting key
	 * @return the created text field
	 */
	public static < T > JTextField createSettingTextField( final SettingKey< T > settingKey, final ActionListener customListener ) {
		return createSettingTextField( settingKey, customListener, CAH.getSettings() );
	}
	
	/**
	 * Creates a text field whose value is bound to the specified settingKey.<br>
	 * The initial value of the text field will be taken from the specified settings,
	 * and a document listener will be added to the text field to register changes at the specified settings.<br>
	 * 
	 * <p><i>Warning!</i> Updating the setting is done via a registered {@link DocumentListener}.
	 * In Swing the execution order of registered listeners is not specified.
	 * Custom listeners added to the returned text field might be called before the setting is updated!
	 * If it is required that the new setting be updated when a custom listener is called,
	 * pass the custom listener as the <code>customListener</code> argument here.</p>
	 * 
	 * @param settingKey key of the settings its value is bound to
	 * @param customListener action listener which is guaranteed to be called <b>after</b> the setting bound the returned text field is bound to has been updated
	 * @param settings settings to take the initial value from and to set the changed value to 
	 * @param <T> value type of the setting key
	 * @return the created text field
	 */
	public static < T > JTextField createSettingTextField( final SettingKey< T > settingKey, final ActionListener customListener, final Settings settings ) {
		final JTextField textField = new JTextField( 10 );
		
		if ( settingKey.type == TypeList.class )
			textField.setText( ( (TypeList< ? >) settings.get( settingKey ) ).convertToString( ',' ) );
		else
			textField.setText( Utils.simpleToString( settings.get( settingKey ) ) );
		
		textField.getDocument().addDocumentListener( new DocumentListener() {
			@Override
			public void removeUpdate( final DocumentEvent event ) {
				handleChange();
			}
			@Override
			public void insertUpdate( final DocumentEvent event ) {
				handleChange();
			}
			@Override
			public void changedUpdate( final DocumentEvent event ) {
				handleChange();
			}
			private void handleChange() {
				if ( settingKey.type == TypeList.class )
					settings.set( settingKey, Utils.< T >cast( TypeList.parseFromString( ( (TypeList< ? >) settingKey.defaultValue ).type, textField.getText(), ',' ) ) );
				else
					settings.set( settingKey, Utils.simpleValueOf( settingKey.type, textField.getText() ) );
				
				if ( customListener != null )
					customListener.actionPerformed( null );
			}
		} );
		
		return textField;
	}
	
	/**
	 * Builds radio button menu items for the specified values, having <code>selected</code> initially selected.
	 * When one of the menu items is activated, the specified <code>receiver</code>
	 * will be called passing the associated value from the <code>values</code> array.<br>
	 * 
	 * Menus' text will be {@link HasDisplayName#getDisplayName()} if values implement {@link HasDisplayName},
	 * else {@link Object#toString()} will be used.
	 * 
	 * @param parent   parent menu to add the created radio button menu items
	 * @param values   values to create radio button menu items for
	 * @param selected the initially selected value, optional
	 * @param receiver receiver to be called when one of the created radio button menu items is activated
	 */
	public static < T > void createRadioButtonMenuItems( final JMenu parent, final T[] values, final T selected, final Receiver< T > receiver ) {
		final ButtonGroup buttonGroup = new ButtonGroup();
		
		for ( final T value : values ) {
			final JRadioButtonMenuItem radioButtonMenuItem = new JRadioButtonMenuItem(
					value instanceof HasDisplayName ? ( (HasDisplayName) value ).getDisplayName() : value.toString(), value.equals( selected ) );
			buttonGroup.add( radioButtonMenuItem );
			
			radioButtonMenuItem.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					receiver.receive( value );
				}
			} );
			
			parent.add( radioButtonMenuItem );
		}
	}
	
    /**
     * Changes the font of the specified component to bold.
     * @param component component whose font to be changed
     * @return the component
     */
    public static < T extends Component > T changeFontToBold( final T component ) {
    	component.setFont( component.getFont().deriveFont( Font.BOLD ) );
    	return component;
    }
    
    /**
     * Changes the font of the specified component to italic.
     * @param component component whose font to be changed
     * @return the component
     */
    public static < T extends Component > T changeFontToItalic( final T component ) {
    	component.setFont( component.getFont().deriveFont( Font.ITALIC ) );
    	return component;
    }
    
	/**
	 * Makes the box left, center or right aligned.
	 * 
	 * @param box       box to be aligned
	 * @param alignment one of {@link SwingConstants#LEFT}, {@link SwingConstants#CENTER} or {@link SwingConstants#RIGHT}
	 * 
	 * @throws IllegalArgumentException if alignment is neither {@link SwingConstants#LEFT}, {@link SwingConstants#CENTER} nor {@link SwingConstants#RIGHT}
	 */
	public static void alignBox( final Box box, final int alignment ) {
		final float alignmentX = alignment == SwingConstants.LEFT ? 0f : alignment == SwingConstants.CENTER ? 0.5f : alignment == SwingConstants.RIGHT ? 1.0f : -1;
		
		if ( alignmentX < 0 )
			throw new IllegalArgumentException();
		
		for ( int i = box.getComponentCount() - 1; i >= 0; i-- )
			( (JComponent) box.getComponent( i ) ).setAlignmentX( alignmentX );
	}
	
	/**
	 * Resizes a window by setting its bounds to maximum that fits inside the default screen having the specified margin around it,
	 * and centers the window on the screen.
	 * 
	 * <p>The implementation takes the screen insets (for example space occupied by task bar) into account.</p>
	 * 
	 * @param window  window to be resized
	 * @param margin  margin to leave around the window
	 * @param maxSize optional parameter defining a maximum size
	 */
	public static void maximizeWindowWithMargin( final Window window, final int margin, final Dimension maxSize ) {
		final GraphicsConfiguration gconfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		final Rectangle bounds = gconfig.getBounds();
		final Insets    insets = Toolkit.getDefaultToolkit().getScreenInsets( gconfig );
		
		final int width  = bounds.width  - insets.left - insets.right  - ( margin << 1 );
		final int height = bounds.height - insets.top  - insets.bottom - ( margin << 1 );
		
		if ( maxSize == null )
			window.setSize( width, height );
		else
			window.setSize( Math.min( width, maxSize.width ), Math.min( height, maxSize.height ) );
		
		// And finally center on screen
		window.setLocationRelativeTo( null );
	}
	
	/**
	 * Requests focus for the specified component <i>later</i>.<br>
	 * <i>Later</i> means doing so using {@link SwingUtilities#invokeLater(Runnable)}.
	 * @param component component to request focus later for
	 */
	public static void requestFocusInWindowLater( final JComponent component ) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				component.requestFocusInWindow();
			}
		} );
	}
	
	/**
	 * Runs the specified task in the EDT.<br>
	 * If the current thread is the EDT, simply <code>task.run()</code> will be called.
	 * Else {@link SwingUtilities#invokeAndWait(Runnable)} will be used.
	 * 
	 * @param task task to be run in the EDT
	 * @return the exception if thrown by {@link SwingUtilities#invokeAndWait(Runnable)}; <code>null</code> otherwise
	 */
	public static Exception runInEDT( final Runnable task ) {
		if ( SwingUtilities.isEventDispatchThread() )
			task.run();
		else {
			try {
				SwingUtilities.invokeAndWait( task );
			} catch ( final Exception e ) {
	        	CAH.error( "Exeption thrown while executing task in EDT!", e );
	        	return e;
			}
		}
		
		return null;
	}
	
	/**
	 * Creates a label which operates as a link.<br>
	 * This method forwards to {@link #createLinkLabel(String, String, XIcon)} with {@link XIcon#F_APPLICATION_BROWSER} icon.
	 * 
	 * @param text      text of the label (link)
	 * @param targetUrl URL to be opened when the user clicks on the label
	 * @return the label which operates as a link
	 */
	public static JLabel createLinkLabel( final String text, final String targetUrl ) {
		return createLinkLabel( text, targetUrl, XIcon.F_APPLICATION_BROWSER );
	}
	
	/**
	 * Creates a label which operates as a link.<br>
	 * The link will only be opened if the label is not disabled.<br>
	 * The target URL is set as the tool of the label.
	 * 
	 * @param text      text of the label (link)
	 * @param targetUrl URL to be opened when the user clicks on the label
	 * @param xicon     optional xicon to be set/used for the label
	 * @return the label which operates as a link
	 */
	public static JLabel createLinkLabel( final String text, final String targetUrl, final XIcon xicon ) {
		final JLabel label = createLinkStyledLabel( text );
		
		if ( xicon != null )
			label.setIcon( CAH.get( xicon ) );
		
		if ( AcUtils.CAH.get( SettingKeys.UI$DISPLAY_LINK_TOOL_TIPS ) )
			label.setToolTipText( CAH.get( TextKey.GENERAL$OPEN_LINK, targetUrl ) );
		
		label.addMouseListener( new MouseAdapter() {
			public void mouseClicked( final MouseEvent event ) {
				AcUtils.showURLInBrowser( targetUrl );
			};
		} );
		
		return label;
	}
	
	/**
	 * Creates a label which looks like a link and has a hand mouse cursor.
	 * @param text text of the label (link)
	 * @return a label which looks like a link
	 */
	public static JLabel createLinkStyledLabel( final String text ) {
		final JLabel label = new JLabel( Utils.createHtmlLink( text ) );
		label.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		return label;
	}
	
	/**
	 * Encodes special characters in the specified text for HTML rendering.
	 * @param text text to encode special characters in
	 * @return a string where special characters are encoded
	 */
	public static String safeHtmlString( final String text ) {
		return text.replace( Utils.SPACE_STRING, "&nbsp;" ).replace( "<", "&lt;" ).replace( ">", "&gt;" );
	}
	
	/**
	 * Packs a table.<br>
	 * Resizes all columns to the maximum width of the values in each column.
	 * @param table table to be packed
	 */
	public static void packTable( final JTable table ) {
		for ( int column = table.getColumnCount() - 1; column >= 0 ; column-- ) {
			// Let's start with a minimum width being the preferred width of the column header
			int maxWidth = table.getRowMargin() + table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent( table, table.getColumnModel().getColumn( column ).getIdentifier(), false, false, 0, column ).getPreferredSize().width;
			
			for ( int row = table.getRowCount() - 1; row >= 0 ; row-- )
				maxWidth = Math.max( maxWidth, table.getRowMargin() + table.getCellRenderer( row, column ).getTableCellRendererComponent( table, table.getValueAt( row, column ), false, false, row, column ).getPreferredSize().width );
			
			table.getColumnModel().getColumn( column ).setPreferredWidth( maxWidth );
		}
	}
	
	/**
	 * Creates a name-value table containing all the entries of the specified entry set.<br>
	 * Each entry will have a row in the table, entry's key value goes to the name column, entry's value goes to the value column.<br>
	 * 
	 * Initial sorting will be set.<br>
	 * {@link TextKey#NAME_VALUE_TABLE$PROPERTY_NAME} and {@link TextKey#NAME_VALUE_TABLE$PROPERTY_VALUE} will be used as column headers.
	 * 
	 * @param entrySet   entry set whose entries to add to the table
	 * @return a table to contain all the entries of the specified entry set
	 */
    public static < K, V > JTable createNameValueTable( final Set< ? extends Entry< K, V > > entrySet ) {
    	return createNameValueTable( entrySet, true );
    }
    
	/**
	 * Creates a name-value table containing all the entries of the specified entry set.<br>
	 * Each entry will have a row in the table, entry's key value goes to the name column, entry's value goes to the value column.<br>
	 * 
	 * {@link TextKey#NAME_VALUE_TABLE$PROPERTY_NAME} and {@link TextKey#NAME_VALUE_TABLE$PROPERTY_VALUE} will be used as column headers.
	 * 
	 * @param entrySet   entry set whose entries to add to the table
	 * @param setSorting tells if initial sorting has to be set (name primary, value secondary)
	 * @return a table to contain all the entries of the specified entry set
	 */
    public static < K, V > JTable createNameValueTable( final Set< ? extends Entry< K, V > > entrySet, final boolean setSorting ) {
    	return createNameValueTable( entrySet, setSorting, 
    			CAH.get( TextKey.NAME_VALUE_TABLE$PROPERTY_NAME ), CAH.get( TextKey.NAME_VALUE_TABLE$PROPERTY_VALUE ) );
    }
    
	/**
	 * Creates a name-value table containing all the entries of the specified entry set.<br>
	 * 
	 * Each entry will have a row in the table, entry's key value goes to the name column, entry's value goes to the value column. 
	 * 
	 * @param entrySet         entry set whose entries to add to the table
	 * @param setSorting       tells if initial sorting has to be set (name primary, value secondary)
     * @param nameColumnTitle  name column title
     * @param valueColumnTitle value column title
     * 
	 * @return a table to contain all the entries of the specified entry set
	 */
    public static < K, V > JTable createNameValueTable( final Set< ? extends Entry< K, V > > entrySet, final boolean setSorting, final String nameColumnTitle, final String valueColumnTitle ) {
		final JTable table = new SmartTable();
		
		final Vector< Vector< Object > > dataVector = new Vector<>( entrySet.size() );
		for ( final Entry< K, V > entry : entrySet ) {
			final Vector< Object > row = new Vector<>( 2 );
			row.add( entry.getKey() );
			row.add( entry.getValue() );
			dataVector.add( row );
		}
		
		final Vector< Object > columnIdnetifiers = new Vector<>();
		columnIdnetifiers.add( nameColumnTitle  );
		columnIdnetifiers.add( valueColumnTitle );
		( (DefaultTableModel) table.getModel() ).setDataVector( dataVector, columnIdnetifiers );
		
		if ( setSorting ) // Sort by name by default
			table.getRowSorter().setSortKeys( Arrays.asList( new RowSorter.SortKey( 0, SortOrder.ASCENDING ), new RowSorter.SortKey( 1, SortOrder.ASCENDING ) ) );
    	
		return table;
	}
	
	/**
	 * Creates an HTML viewer component.
	 * 
	 * <p>The returned component is a {@link JEditorPane} set to be non-editable,
	 * handles hyperlinks (opens them in the default browser, shows proper tool tip).</p>
	 * 
	 * @return the created HTML viewer
	 */
	public static JEditorPane createHtmlViewer() {
		final JEditorPane editorPane = new JEditorPane();
		
		editorPane.setEditable( false );
		
		editorPane.addHyperlinkListener( new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate( final HyperlinkEvent event ) {
				if ( event.getEventType() == HyperlinkEvent.EventType.ACTIVATED )
					AcUtils.showURLInBrowser( event.getURL().toExternalForm() );
				else if ( event.getEventType() == HyperlinkEvent.EventType.ENTERED ) {
					final String url = event.getURL().toExternalForm();
					if ( AcUtils.CAH.get( SettingKeys.UI$DISPLAY_LINK_TOOL_TIPS ) )
						editorPane.setToolTipText( CAH.get( url.startsWith( "mailto:" ) ? TextKey.GENERAL$WRITE_EMAIL : TextKey.GENERAL$OPEN_LINK, url ) );
				}
				else if ( event.getEventType() == HyperlinkEvent.EventType.EXITED )
					editorPane.setToolTipText( null );
			}
		} );
		
		return editorPane;
	}
	
	/**
	 * Creates and returns a copyright info component.
	 * @return a copyright info component
	 */
	public static JComponent getCopyrightInfoComponent() {
		final JPanel panel = new JPanel( new FlowLayout( FlowLayout.CENTER, 0, 5 ) );
		
		panel.add( changeFontToItalic( new JLabel( "Copyright © " + Consts.COPYRIGHT_YEAR_INFO 
				+ Utils.SPACE_STRING + CAH.getPersonName( Consts.APP_AUTHOR ) + " <" ) ) );
		
		String email_ = Utils.EMPTY_STRING;
		for ( final Contact c : Consts.APP_AUTHOR.getContactList() )
			if ( c.getType() == ContactType.EMAIL ) {
				email_ = c.getValue();
				break;
			}
		final String email = email_;
		final JLabel emailLinkLabel = changeFontToItalic( createLinkStyledLabel( email ) );
		if ( AcUtils.CAH.get( SettingKeys.UI$DISPLAY_LINK_TOOL_TIPS ) )
			emailLinkLabel.setToolTipText( CAH.get( TextKey.GENERAL$WRITE_EMAIL, email ) );
		emailLinkLabel.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( final MouseEvent event ) {
				NewEmailAction.INSTANCE.newMail( email, null, null );
			}
		} );
		panel.add( emailLinkLabel );
		
		panel.add( changeFontToItalic( new JLabel( ">" ) ) );
		
		return panel;
	}
	
}
