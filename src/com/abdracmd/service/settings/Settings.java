/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.service.settings;

import hu.belicza.andras.util.TypeList;
import hu.belicza.andras.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.abdracmd.Consts;
import com.abdracmd.service.log.Logging;
import com.abdracmd.smp.main.MainShell;

/**
 * Settings service.
 * 
 * @author Andras Belicza
 */
public class Settings {
	
	/** Path of the settings file to persist settings. */
	public static final Path SETTINGS_FILE_PATH        = Consts.PATH_USER_CONTENT.resolve( "settings.xml" );
	/** Path of the backup settings file.              */
	public static final Path BACKUP_SETTINGS_FILE_PATH = SETTINGS_FILE_PATH.resolveSibling( SETTINGS_FILE_PATH.getFileName() + ".bak" );
	
	/** Reference to the application logging. */
	private final Logging logging = MainShell.INSTANCE.getModel().getLogging();
	
	/**
	 * Map storing the settings.
	 * Only settings diverging from the default setting values are stored here.
	 */
	private final Map< SettingKey< ? >, Object > settingsMap = new HashMap<>();
	
	/**
	 * Map storing the setting set change listeners.
	 * Key is the listened setting key, value is a {@link WeakHashMap} storing the listeners (as weak-keys of the map) for the setting key.
	 */
	private final Map< SettingKey< ? >, Map< SettingSetChangeListener, Object > > settingKeyChangeListenerMapMap = new HashMap<>();
	/** A not-used value for listeners stored in the the weak hash map. */
	private static final Object LISTENER_PRESENT = new Object();
	
	/** Tells if change tracking is enabled. */
	private boolean                      changeTrackingEnabled;
	/** Set to store the tracked changes.    */
	private final Set< SettingKey< ? > > trackedChangedSettingKeySet = new HashSet<>();
	
	/**
	 * Creates a new Settings.
	 */
	public Settings() {
	}
	
	/**
	 * Returns the setting value specified by the key.<br>
	 * If the value of the specified setting is not yet set, its default value will be returned.
	 * 
	 * @param <T> setting value type
	 * @param key key of the setting whose value to return
	 * 
	 * @return the setting value specified by the key
	 */
    public < T > T get( final SettingKey< T > key ) {
    	final T value = Utils.cast( settingsMap.get( key ) );
		return value == null ? key.defaultValue : value;
	}
	
	/**
	 * Resets the specified setting, restores the default value of the specified setting.<br>
	 * A setting is reset by removing its value (in which case when queried, the default value will be returned).
	 * @param key key of the setting to reset
	 */
	public void reset( final SettingKey< ? > key ) {
		if ( changeTrackingEnabled )
			trackedChangedSettingKeySet.add( key );
		
		if ( settingsMap.remove( key ) != null )
			notifyListeners( Utils.< Set< SettingKey< ? > > >cast( key.SELF_CONTAINING_SET ) );
	}
	
	/**
	 * Sets the setting specified by the key.
	 * @param <T>   setting value type
	 * @param key   key of the setting to set
	 * @param value value of the setting to set
	 */
	public < T > void set( final SettingKey< T > key, final T value ) {
		if ( changeTrackingEnabled )
			trackedChangedSettingKeySet.add( key );
		
		// Do not store default value:
		if ( key.defaultValue.equals( value ) ) {
			if ( Utils.cast( settingsMap.remove( key ) ) != null )
				notifyListeners( Utils.< Set< SettingKey< ? > > >cast( key.SELF_CONTAINING_SET ) );
		}
		else {
			final T previousValue = Utils.cast( settingsMap.put( key, value ) );
			if ( !value.equals( previousValue ) ) // previousValue might be null, compare value to previousValue!
				notifyListeners( Utils.< Set< SettingKey< ? > > >cast( key.SELF_CONTAINING_SET ) );
		}
	}
	
	/**
	 * Adds a new setting set change listener.
	 * 
	 * <p><strong>Note:</strong> listeners are stored as {@link WeakReference}.
	 * This means that no need to remove registered listeners, but this also means that
	 * if an anonymous listener with no references is added, it will be removed shortly after!</p>
	 * 
	 * <p>Adding the same listener to the same setting key multiple times has no effect.</p>
	 * 
	 * @param settingKey setting key whose changes to be notified for
	 * @param listener listener to be added
	 */
	public void addChangeListener( final SettingKey< ? > settingKey, final SettingSetChangeListener listener ) {
		addChangeListener( Utils.< Set< SettingKey< ? > > >cast( settingKey.SELF_CONTAINING_SET ), listener );
	}
	
	/**
	 * Adds a new setting set change listener.
	 * 
	 * <p><strong>Note:</strong> listeners are stored as {@link WeakReference}.
	 * This means that no need to remove registered listeners, but this also means that
	 * if an anonymous listener with no references is added, it will be removed shortly after!</p>
	 * 
	 * <p>Adding the same listener to the same setting key multiple times has no effect.</p>
	 * 
	 * @param settingKeySet set of setting keys whose changes to be notified for
	 * @param listener listener to be added
	 */
	public void addChangeListener( final Set< SettingKey< ? > > settingKeySet, final SettingSetChangeListener listener ) {
		for ( final SettingKey< ? > settingKey : settingKeySet ) {
			Map< SettingSetChangeListener, Object > listenerList = settingKeyChangeListenerMapMap.get( settingKey );
			
			if ( listenerList == null )
				settingKeyChangeListenerMapMap.put( settingKey, listenerList = new WeakHashMap<>() );
			
			listenerList.put( listener, LISTENER_PRESENT);
		}
	}
	
	/**
	 * Notifies the listeners registered to the specified setting keys.
	 * @param settingKeySet set of setting keys whose values have changed 
	 */
	private void notifyListeners( final Set< SettingKey< ? > > settingKeySet ) {
		// Only call once each listener that is registered to any of the specified setting keys!
		// For this, we first collect the listeners to be called:
		final Set< SettingSetChangeListener > listenerSet = new HashSet<>();
		
		for ( final SettingKey< ? > settingKey : settingKeySet ) {
			final Map< SettingSetChangeListener, Object > listenerList = settingKeyChangeListenerMapMap.get( settingKey );
			if ( listenerList != null )
				listenerSet.addAll( listenerList.keySet() );
		}
		
		for ( final SettingSetChangeListener listener : listenerSet )
			listener.valuesChanged( settingKeySet );
	}
	
	/**
	 * Saves the settings.
	 * @return true if settings were saved successfully; false otherwise
	 */
	public boolean saveSettings() {
		// First backup old settings
		if ( Files.exists( SETTINGS_FILE_PATH ) )
			try {
				Files.move( SETTINGS_FILE_PATH, BACKUP_SETTINGS_FILE_PATH, StandardCopyOption.REPLACE_EXISTING );
			} catch ( final IOException ie ) {
				logging.warning( "Failed to backup old settings!", ie );
			}
		
		// Set meta data
		set( SettingKeys.META$SAVED_WITH_VERSION, Consts.APP_VERSION_STRING  );
		set( SettingKeys.META$SAVED_AT          , System.currentTimeMillis() );
		
		Document document;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			final Element docElement = document.createElement( "settings" );
			docElement.setAttribute( "docVer", "1.0" );
			
			docElement.appendChild( document.createTextNode( "\n\n" ) );
			docElement.appendChild( document.createComment( "\n   This settings file is managed by " + Consts.APP_NAME + " automatically. Do not edit it unless you know what you're doing!\n" ) );
			docElement.appendChild( document.createTextNode( "\n\n" ) );
			
			for ( final Entry< SettingKey< ? >, Object > entry : settingsMap.entrySet() ) {
				final SettingKey< ? > settingKey = entry.getKey  ();
				final Object          value      = entry.getValue();
				
				final Element settingElement = document.createElement( "s" );
				settingElement.setAttribute( "k", settingKey.name );
				
				if ( settingKey.type == TypeList.class ) {
					for ( final Object item : (TypeList< ? >) value ) {
						final Element itemElement = document.createElement( "i" );
						itemElement.setTextContent( Utils.simpleToString( item ) );
						settingElement.appendChild( itemElement );
					}
				}
				else
					settingElement.setTextContent( Utils.simpleToString( value ) );
				
				docElement.appendChild( settingElement );
			}
			
			document.appendChild( docElement );
		} catch ( final Exception e ) {
			logging.error( "Failed to save settings!", e );
			return false;
		}
		
		try ( final OutputStream out = Files.newOutputStream( SETTINGS_FILE_PATH ) ) {
			final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty( OutputKeys.INDENT  , "yes"           );
			transformer.setOutputProperty( OutputKeys.ENCODING, Consts.ENCODING );
			transformer.transform( new DOMSource( document ), new StreamResult( out ) );
		} catch ( final Exception e) {
			logging.error( "Failed to save settings!", e );
			return false;
		}
		
		return true;
	}
	
	/**
	 * Loads the settings.
	 * @return true if settings were loaded successfully; false otherwise
	 */
	public boolean loadSettings() {
		if ( !Files.exists( SETTINGS_FILE_PATH ) ) {
			logging.warning( "Settings file does not exist: " + SETTINGS_FILE_PATH );
			return false;
		}
		
		try ( final InputStream xmlInput = Files.newInputStream( SETTINGS_FILE_PATH ) ) {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments( true );
			final Document document = factory.newDocumentBuilder().parse( xmlInput );
			
			final Element docElement = document.getDocumentElement();
			
			settingsMap.clear();
			
			final NodeList settingNodeList = docElement.getElementsByTagName( "s" );
			for ( int i = 0; i < settingNodeList.getLength(); i++ ) {
				final Element settingElement = (Element) settingNodeList.item( i );
				
				final SettingKey< ? > settingKey = SettingKey.getByName( settingElement.getAttribute( "k" ) );
				
				Object value;
				try {
					if ( settingKey.type == TypeList.class ) {
						final Class< ? >         listItemType     = ( (TypeList< ? >) settingKey.defaultValue ).type;
						final NodeList           listItemNodeList = settingElement.getElementsByTagName( "i" );
						final TypeList< Object > itemList         = Utils.cast( new TypeList<>( listItemType, listItemNodeList.getLength() ) );
						for ( int j = 0; j < listItemNodeList.getLength(); j++ )
							itemList.add( Utils.simpleValueOf( listItemType, listItemNodeList.item( j ).getTextContent() ) );
						value = itemList;
					}
					else
						value = Utils.simpleValueOf( settingKey.type, settingElement.getTextContent() );
				} catch ( final Exception e ) {
					// Invalid value (manually manipulated stored settings?)
					// Ignore it so default value will be used
					value = null;
				}
				
				if ( value == null )
					logging.warning( "Invalid setting value: " + settingKey + "=" + settingElement.getTextContent() );
				else
					settingsMap.put( settingKey, value );
			}
			
			return true;
		} catch ( final Exception e ) {
			logging.warning( "Failed to load settings!", e );
			return false;
		}
	}
	
	/**
	 * Packs and returns all settings as one string.
	 * @return all settings as one string
	 */
	public String packAllSettings() {
		return settingsMap.toString();
	}
	
	/**
	 * Sets the state of change tracking.
	 * @param changeTrackingEnabled state of change tracking to be set
	 */
	public void setChangeTrackingEnabled( final boolean changeTrackingEnabled ) {
		this.changeTrackingEnabled = changeTrackingEnabled;
	}
	
	/**
	 * Tells if change tracking is enabled.
	 * @return true if change tracking is enabled; false otherwise
	 */
	public boolean isChangeTrackingEnabled() {
		return changeTrackingEnabled;
	}
	
	/**
	 * Clears the previously tracked changes.
	 */
	public void clearTrackedChanges() {
		trackedChangedSettingKeySet.clear();
	}
	
	/**
	 * Clones this settings.
	 * @return a new settings instance having the same settings
	 */
	public Settings cloneSettings() {
		final Settings clonedSettings = new Settings();
		
		clonedSettings.settingsMap.putAll( settingsMap );
		
		return clonedSettings;
	}
	
	/**
	 * Copies the changed settings to the specified target settings.
	 * @param targetSettings target settings to copy the changed settings to
	 */
	public void copyChangedSettingsTo( final Settings targetSettings ) {
		// Copy all changed settings and notify listeners in one step for optimization reasons
		// (listeners might perform lengthy operations when a setting changes). 
		
		// Simply copying the properties is not enough, because if a setting previously was not the default value
		// and was changed to the default value, it is removed from the properties (and it is not copied).
		// Remove those by ourselves:
		for ( final SettingKey< ? > settingKey : trackedChangedSettingKeySet ) {
			if ( settingsMap.containsKey( settingKey ) )
				targetSettings.settingsMap.put( settingKey, settingsMap.get( settingKey ) );
			else
				targetSettings.settingsMap.remove( settingKey );
		}
			
		// Notify listeners using the whole changed set
		targetSettings.notifyListeners( trackedChangedSettingKeySet );
	}
	
}
