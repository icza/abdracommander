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

import java.util.Date;
import java.util.Set;

import javax.swing.ImageIcon;

import com.abdracmd.service.icon.Icons;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.Language;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.language.TextWithMnemonic;
import com.abdracmd.service.log.LogLevel;
import com.abdracmd.service.log.Logging;
import com.abdracmd.service.settings.SettingKey;
import com.abdracmd.service.settings.SettingSetChangeListener;
import com.abdracmd.service.settings.Settings;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.main.MainModel;
import com.abdracmd.util.bean.person.Person;

/**
 * Contains references and methods to easily access common objects.
 * 
 * @author Andras Belicza
 */
public class CommonAccessHelper {
	
	/** Reference to the main model. */
	// On startup when MainShell.INSTANCE is being assigned, MainShell.INSTANCE is null so we have to check for it...
	protected MainModel mainModel = MainShell.INSTANCE == null ? null : MainShell.INSTANCE.getModel();
	
	/**
	 * Returns the application language.
	 * @return the application language
	 */
	public Language getLanguage() {
		return mainModel.getLanguage();
	}
	
	/**
	 * Returns the application settings.
	 * @return the application settings
	 */
	public Settings getSettings() {
		return mainModel.getSettings();
	}
	
	/**
	 * Returns the application logging.
	 * @return the application logging
	 */
	public Logging getLogging() {
		return mainModel.getLogging();
	}
	
	/**
	 * Returns the application icons.
	 * @return the application icons
	 */
	public Icons getIcons() {
		return mainModel.getIcons();
	}
	
	
	
	// ******************************** CALL FORWARDING : LANGUAGE ******************************** 
	
	
	/**
	 * Forwards call to {@link Language#get(TextKey)} of {@link MainModel#getLanguage()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public String get( final TextKey key ) {
		return mainModel.getLanguage().get( key );
	}
	
	/**
	 * Forwards call to {@link Language#get(TextKey, Object...)} of {@link MainModel#getLanguage()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public String get( final TextKey key, final Object... params ) {
		return mainModel.getLanguage().get( key, params );
	}
	
	/**
	 * Forwards call to {@link Language#getWithMnemonic(TextKey)} of {@link MainModel#getLanguage()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public TextWithMnemonic getWithMnemonic( final TextKey key ) {
    	return mainModel.getLanguage().getWithMnemonic( key );
    }
    
	/**
	 * Forwards call to {@link Language#getWithMnemonic(TextKey, Object...)} of {@link MainModel#getLanguage()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public TextWithMnemonic getWithMnemonic( final TextKey key, final Object... params ) {
    	return mainModel.getLanguage().getWithMnemonic( key, params );
    }
	
	/**
	 * Forwards call to {@link Language#getPersonName(Person)} of {@link MainModel#getLanguage()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public String getPersonName( final Person person ) {
		return mainModel.getLanguage().getPersonName( person );
	}
	
	/**
	 * Forwards call to {@link Language#formatNumber(long)} of {@link MainModel#getLanguage()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public String formatNumber( final long n ) {
		return mainModel.getLanguage().formatNumber( n );
	}
	
	/**
	 * Forwards call to {@link Language#formatNumber(double, int)} of {@link MainModel#getLanguage()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public String formatNumber( final double n, final int fractionDigits ) {
		return mainModel.getLanguage().formatNumber( n, fractionDigits );
	}
	
	/**
	 * Forwards call to {@link Language#formatDate(Date)} of {@link MainModel#getLanguage()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public String formatDate( final Date date ) {
		return mainModel.getLanguage().formatDate( date );
	}
	
	/**
	 * Forwards call to {@link Language#formatDateTime(Date)} of {@link MainModel#getLanguage()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public String formatDateTime( final Date dateTime ) {
		return mainModel.getLanguage().formatDateTime( dateTime );
	}
	
	/**
	 * Forwards call to {@link Language#formatTime(Date)} of {@link MainModel#getLanguage()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public String formatTime( final Date date ) {
		return mainModel.getLanguage().formatTime( date );
	}
	
	
	// ******************************** CALL FORWARDING : SETTINGS ******************************** 
	
    
	/**
	 * Forwards call to {@link Settings#get(SettingKey)} of {@link MainModel#getSettings()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public < T > T get( final SettingKey< T > key ) {
    	return mainModel.getSettings().get( key );
    }
	
	/**
	 * Forwards call to {@link Settings#set(SettingKey, Object)} of {@link MainModel#getSettings()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public< T > void set( final SettingKey< T > key, final T value ) {
		mainModel.getSettings().set( key, value );
	}
	
	/**
	 * Forwards call to {@link Settings#reset(SettingKey)} of {@link MainModel#getSettings()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public void reset( final SettingKey< ? > key ) {
    	mainModel.getSettings().reset( key );
    }
	
	/**
	 * Forwards call to {@link Settings#addChangeListener(SettingKey, SettingSetChangeListener)} of {@link MainModel#getSettings()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public void addChangeListener( final SettingKey< ? > settingKey, final SettingSetChangeListener listener ) {
    	mainModel.getSettings().addChangeListener( settingKey, listener );
    }
	
    /**
	 * Forwards call to {@link Settings#addChangeListener(Set, SettingSetChangeListener)} of {@link MainModel#getSettings()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
    public void addChangeListener( final Set< SettingKey< ? > > settingKeySet, final SettingSetChangeListener listener ) {
    	mainModel.getSettings().addChangeListener( settingKeySet, listener );
    }
	
	
	// ******************************** CALL FORWARDING : LOGGING ******************************** 
	
    
	/**
	 * Forwards call to {@link Logging#testLevel(LogLevel)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public boolean testLevel( final LogLevel logLevel ) {
    	return mainModel.getLogging().testLevel( logLevel );
	}
	
	/**
	 * Forwards call to {@link Logging#error(String)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public void error( final String message ) {
    	mainModel.getLogging().error( message );
	}
	
	/**
	 * Forwards call to {@link Logging#error(String, Throwable)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public void error( final String message, final Throwable throwable ) {
    	mainModel.getLogging().error( message, throwable );
	}
	
	/**
	 * Forwards call to {@link Logging#warning(String)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public void warning( final String message ) {
    	mainModel.getLogging().warning( message );
	}
	
	/**
	 * Forwards call to {@link Logging#warning(String, Throwable)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public void warning( final String message, final Throwable throwable ) {
    	mainModel.getLogging().warning( message, throwable );
	}
	
	/**
	 * Forwards call to {@link Logging#info(String)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public void info( final String message ) {
    	mainModel.getLogging().info( message );
	}
	
	/**
	 * Forwards call to {@link Logging#info(String, Throwable)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public void info( final String message, final Throwable throwable ) {
    	mainModel.getLogging().info( message, throwable );
	}
	
	/**
	 * Forwards call to {@link Logging#debug(String)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public void debug( final String message ) {
    	mainModel.getLogging().debug( message );
	}
	
	/**
	 * Forwards call to {@link Logging#debug(String, Throwable)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public void debug( final String message, final Throwable throwable ) {
    	mainModel.getLogging().debug( message, throwable );
	}
	
	/**
	 * Forwards call to {@link Logging#trace(String)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public void trace( final String message ) {
    	mainModel.getLogging().trace( message );
	}
	
	/**
	 * Forwards call to {@link Logging#trace(String, Throwable)} of {@link MainModel#getLogging()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public void trace( final String message, final Throwable throwable ) {
    	mainModel.getLogging().trace( message, throwable );
	}
	
	
	// ******************************** CALL FORWARDING : ICONS ******************************** 
	
    
	/**
	 * Forwards call to {@link Icons#get(XIcon)} of {@link MainModel#getIcons()} of {@link #mainModel}.
	 */
    @SuppressWarnings( "javadoc" )
	public ImageIcon get( final XIcon xicon ) {
    	return mainModel.getIcons().get( xicon );
	}
	
}
