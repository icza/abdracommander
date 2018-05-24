/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.selftest;

import hu.belicza.andras.util.Utils;

import java.util.HashMap;
import java.util.Map;

import com.abdracmd.DevConsts;
import com.abdracmd.fileop.impl.ActionOnError;
import com.abdracmd.fileop.impl.ActionWhenFileExists;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.log.LogLevel;
import com.abdracmd.service.settings.SettingKey;
import com.abdracmd.service.settings.SettingLevel;
import com.abdracmd.service.settings.SettingText;
import com.abdracmd.smp.dialog.fileop.BeepOnCompletion;
import com.abdracmd.smp.mainframe.folderstabbed.TabLayoutPolicy;
import com.abdracmd.smp.mainframe.folderstabbed.TabPlacement;
import com.abdracmd.smp.mainframe.folderstabbed.folder.AutoResizeMode;
import com.abdracmd.smp.mainframe.folderstabbed.folder.GoIntoFilesMode;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntity;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntityFlag;
import com.abdracmd.smp.shutdown.ShutdownPhase;
import com.abdracmd.smp.startup.StartupPhase;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.SizeFormat;
import com.abdracmd.util.bean.person.Contact.ContactType;
import com.abdracmd.util.bean.person.PersonNamePart.PersonNamePartType;

/**
 * Self diagnostics: diagnostic utilities to help reveal runtime errors.
 * 
 * @author Andras Belicza
 */
public class SelfDiagnostics {
	
	/**
	 * Runs all tests.
	 */
    public static void runAllTests() {
    	try {
    		
    		checkEnumTexts();
    		
    		checkTextKeys();
    		
    		checkSettingKeys();
    		
    	} catch ( final Exception e ) {
    		AcUtils.CAH.error( "SELF DIAGNOSTIC FAILED! Exiting now...", e );
    		System.exit( 0 );
    	}
    }
    
	/**
	 * Checks texts of enums.
	 */
    public static void checkEnumTexts() {
    	final Enum< ? >[][] enumValuess = {
    		LogLevel.values(),
    		StartupPhase.values(), ShutdownPhase.values(),
    		AutoResizeMode.values(), GoIntoFilesMode.values(),
    		PersonNamePartType.values(), ContactType.values(),
    		ColorEntity.values(), ColorEntityFlag.values(),
    		SettingLevel.values(), SettingText.values(),
    		SizeFormat.values(),
    		TabLayoutPolicy.values(), TabPlacement.values(),
    		BeepOnCompletion.values(),
    		ActionOnError.values(), ActionWhenFileExists.values(),
    	};
    	
		for ( final Enum< ? >[] enumValues : enumValuess ) {
			for ( final Enum< ? > value : enumValues )
				if ( AcUtils.getEnumText( value ) == null )
					throw new RuntimeException( "Missing enum text for: " + value.getClass() + "." + value.name() );
		}
	}
	
	/**
	 * Checks all text keys: a non-null string text must be associated.
	 */
    public static void checkTextKeys() {
    	final Map< Integer, Object[] > paramCountParamsMap = new HashMap<>();
    	
    	for ( final TextKey textKey : TextKey.values() ) {
    		Object[] params = paramCountParamsMap.get( textKey.params.length );
    		if ( params == null ) {
    			paramCountParamsMap.put( textKey.params.length, params = new Object[ textKey.params.length ] );
    			for ( int i = 0; i < params.length; i++ )
    				params[ i ] = Utils.EMPTY_STRING;
    		}
    		
    		try {
    			// Return value might be null or if params are required, it throws NPE when trying to substitute them...
    			AcUtils.CAH.get( textKey, params ).toString();
    		} catch ( final NullPointerException npe ) {
    			throw new RuntimeException( "Missing text for text key: " + textKey );
    		}
    	}
    }
    
	/**
	 * Checks all setting keys.<br>
	 * 
	 * Things to check:
	 * <ul>
	 * 	<li>their name must be unique
	 * 	<li>their name cannot be without a parent (must contain at least one parent node)
	 * 	<li>if the setting level is not {@link SettingLevel#HIDDEN}
	 * 		<ul>
	 * 			<li>a {@link SettingText} must be defined with the value of {@link SettingKey#name}
	 * 			<li>the parent node of the the setting key must have a setting text defined
	 * 			<li>the setting text of the setting key must have an xicon specified
	 * 		</ul>
	 * <ul>
	 */
    public static void checkSettingKeys() {
    	if ( SettingKey.getSettingKeyList().size() != SettingKey.getSettingMap().size() )
			throw new RuntimeException( "Setting key names are not unique!" );
    	
    	for ( final SettingKey< ? > settingKey : SettingKey.getSettingKeyList() ) {
    		final int lastNodeSeparatorIdx = settingKey.name.lastIndexOf( DevConsts.HIERARCHY_SEPARATOR );
    		
    		if ( lastNodeSeparatorIdx < 0 )
    			throw new RuntimeException( "Setting key without a parent node: " + settingKey.name );
    		
    		if ( settingKey.level != SettingLevel.HIDDEN ) {
    			try {
    				SettingText.valueOf( settingKey.name );
    			} catch ( final IllegalArgumentException iae ) {
    				throw new RuntimeException( "Setting key level is not HIDDEN and no setting text is defined with the same name: " + settingKey.name );
    			}
    			
    			try {
	    			final SettingText parentSettingText = SettingText.valueOf( settingKey.name.substring( 0, lastNodeSeparatorIdx ) );
	        		if ( parentSettingText.xicon == null )
	    				throw new RuntimeException( "Setting text has no xicon of the parent node of the setting key: " + settingKey.name );
    			} catch ( final IllegalArgumentException iae ) {
    				throw new RuntimeException( "Setting key level is not HIDDEN and no setting text is defined for the parent node of the setting key: " + settingKey.name );
    			}
    		}
    	}
    }
    
}
