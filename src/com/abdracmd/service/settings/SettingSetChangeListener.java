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

import java.util.Set;

/**
 * A setting change listener.
 * 
 * @author Andras Belicza
 */
public interface SettingSetChangeListener {
	
	/**
	 * Called when the values of the specified setting keys have changed.
	 * @param settingKeySet set of setting keys whose values have changed<br>
	 * 	<i>Note:</i> this set might be larger than the set this listener was registered for
	 * 	(but it is guaranteed that at least 1 registered setting key is contained in this set)
	 * 	<i>Also note</i> that there is no guarantee that the settings contained in this set really changed
	 */
	void valuesChanged( Set< SettingKey< ? > > settingKeySet );
	
}
