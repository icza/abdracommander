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

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.util.AcUtils;

/**
 * Texts and icons of settings.
 * 
 * Enum names are the same as the associated setting key, listed in {@link SettingKeys}.
 * Setting documentation (javadoc) can be found there.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings("javadoc")
public enum SettingText {
	
	APP( XIcon.MY_APP_ICON_SMALL ),
	APP$LANGUAGE,
	APP$LOOK_AND_FEEL,
	APP$LOG_LEVEL,
	APP$LOG_FILE_LIFETIME,
	APP$BEEP_ON_ERRORS,
	APP$CONFIRM_EXIT,
	APP$SAVE_SETTINGS_ON_EXIT,
	
	MAIN_FRAME( XIcon.F_APPLICATION_BLUE ),
	MAIN_FRAME$REFRESH_ON_ACTIVATE,
	MAIN_FRAME$SPLIT_CONTINOUS_LAYOUT,
	
	FOLDER_TABS( XIcon.F_UI_TAB ),
	FOLDER_TABS$REFRESH_ON_SELECT,
	
	FOLDER_TABLE( XIcon.F_TABLE ),
	FOLDER_TABLE$THEME,
	FOLDER_TABLE$GO_INTO_FILES_MODE,
	FOLDER_TABLE$CUSTOM_TYPES_TO_GO_INTO,
	FOLDER_TABLE$MOVE_FOCUS_WHEN_USING_SPACE,
	
	FILE_LISTING( XIcon.F_DOCUMENTS_STACK ),
	FILE_LISTING$SHOW_HIDDEN_SYSTEM_FILES,
	FILE_LISTING$SIZE_COLUMN_FORMAT,
	FILE_LISTING$SIZE_COLUMN_FRACTION_DIGITS,
	FILE_LISTING$SIZE_SUMMARY_FORMAT,
	FILE_LISTING$SIZE_SUMMARY_FRACTION_DIGITS,
	
	NEW_FOLDER$CREATE_NONEXISTENT_PARENTS,
	
	ROOTS_POPUP( XIcon.F_DRIVE ),
	ROOTS_POPUP$REFRESH_ON_CMD_BAR_REFRESH,
	
	FILE_OP( XIcon.FILE_OP ),
	FILE_OP$SHOW_COMPLETION_PERCENT_IN_TITLE,
	FILE_OP$CLOSE_DIALOG_WHEN_COMPLETED,
	FILE_OP$BEEP_ON_COMPLETION,
	FILE_OP$BUFFER_SIZE,
	FILE_OP$ACTION_WHEN_FILE_EXISTS,
	FILE_OP$ACTION_ON_ERROR,
	FILE_OP$COPY( XIcon.COPY ),
	FILE_OP$DELETE( XIcon.DELETE ),
	FILE_OP$DELETE$WIPE_DELETE,
	
	UI( XIcon.F_UI_LAYOUT_PANEL ),
	UI$TOOL_TIP_INITIAL_DELAY,
	UI$TOOL_TIP_DISMISS_DELAY,
	UI$DISPLAY_LINK_TOOL_TIPS,
	;
	
	/** Xicon of the setting or setting node. */
	public final XIcon xicon;
	
	/**
	 * Creates a new SettingText, without icon.
	 */
	private SettingText() {
		this( null );
	}
	
	/**
	 * Creates a new SettingText with icon.
	 * @param xicon xicon of the associated setting or setting node
	 */
	private SettingText( final XIcon xicon ) {
		this.xicon = xicon;
	}
	
	@Override
	public String toString() {
		return AcUtils.getEnumText( this );
	}
	
}
