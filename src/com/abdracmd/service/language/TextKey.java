/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.service.language;

/**
 * Text keys.
 * 
 * <p>Text keys for enums are not listed here.</p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "javadoc" )
public enum TextKey {
	
	ABOUT$TITLE( Param.PARAMS_APP_NAME ),
	
	ABOUT$PAGE$COMPONENTS$NAME,
	ABOUT$PAGE$COMPONENTS$APP_FULL_VERSION,
	ABOUT$PAGE$COMPONENTS$APP_RELEASE_DATE,
	ABOUT$PAGE$COMPONENTS$CURRENT_LANGUAGE,
	ABOUT$PAGE$COMPONENTS$CURRENT_LANGUAGE_TRANSLATORS,
	ABOUT$PAGE$COMPONENTS$CURRENT_LANGUAGE_TARGET_VERSION,
	ABOUT$PAGE$COMPONENTS$CURRENT_LANGUAGE_VERSION,
	ABOUT$PAGE$COMPONENTS$UPDATER_VERSION,
	
	ABOUT$PAGE$GENERAL$NAME,
	ABOUT$PAGE$GENERAL$BUILD,
	ABOUT$PAGE$GENERAL$THANK_YOU( Param.PARAMS_APP_NAME ),
	ABOUT$PAGE$GENERAL$AUTHORS_CARD,
	
	ABOUT$PAGE$LICENSE$NAME,
	ABOUT$PAGE$LICENSE$FAILED_TO_LOAD,
	
	ABOUT$PAGE$REG_INFO$NAME,
	ABOUT$PAGE$REG_INFO$PLEASE_REGISTER,
	ABOUT$PAGE$REG_INFO$REGISTERED_PERSON,
	ABOUT$PAGE$REG_INFO$REGINFO_DETAILS,
	ABOUT$PAGE$REG_INFO$REGISTRATION_DATE,
	ABOUT$PAGE$REG_INFO$ROAMING_ENABLED,
	ABOUT$PAGE$REG_INFO$REGISTERED_SYSTEM_INFO,
	ABOUT$PAGE$REG_INFO$MAIN_ROOT_SIZE,
	
	ABOUT$PAGE$SYSINFO$NAME,
	ABOUT$PAGE$SYSINFO$OP_SYS,
	ABOUT$PAGE$SYSINFO$USER_NAME,
	ABOUT$PAGE$SYSINFO$USER_TIME_ZONE,
	ABOUT$PAGE$SYSINFO$PAGE$ENV_VARS$NAME,
	ABOUT$PAGE$SYSINFO$PAGE$ENV_VARS$VARIABLE_NAME,
	ABOUT$PAGE$SYSINFO$PAGE$ENV_VARS$VARIABLE_VALUE,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$NAME,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVENDOR,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVERSION,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JHOME,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JRT_NAME,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JRT_VERSION,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JSPEC_VENDOR,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JSPEC_NAME,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JSPEC_VERSION,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMSPEC_VENDOR,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMSPEC_NAME,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMSPEC_VERSION,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMIMPL_VENDOR,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMIMPL_NAME,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVMIMPL_VERSION,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JVM_INPUT_ARGS,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$BOOT_CLASS_PATH,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$LIBRARY_PATH,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$CLASS_PATH,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JSTART_TIME,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$JUPTIME,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$LOADED_CLASS_COUNT,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$TOTAL_LOADED_CLASS_COUNT,
	ABOUT$PAGE$SYSINFO$PAGE$JAVA$UNLOADED_CLASS_COUNT,
	ABOUT$PAGE$SYSINFO$PAGE$MEMORY$NAME,
	ABOUT$PAGE$SYSINFO$PAGE$MEMORY$MAX_HEAP,
	ABOUT$PAGE$SYSINFO$PAGE$MEMORY$COMMITTED_HEAP,
	ABOUT$PAGE$SYSINFO$PAGE$MEMORY$USED_HEAP,
	ABOUT$PAGE$SYSINFO$PAGE$MEMORY$MAX_NONHEAP,
	ABOUT$PAGE$SYSINFO$PAGE$MEMORY$COMMITTED_NONHEAP,
	ABOUT$PAGE$SYSINFO$PAGE$MEMORY$USED_NONHEAP,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$NAME,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_DISPLAY_NAME,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_NAME,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_IP_ADDRESSES,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_INTERFACE_ADDRESSES,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_MAC_ADDRESS,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_UP,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_VIRTUAL,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_POINT_TO_POINT,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_MULTICAST,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_MTU,
	ABOUT$PAGE$SYSINFO$PAGE$NET_IF$FAILED_TO_INSPECT,
	ABOUT$PAGE$SYSINFO$PAGE$OS$NAME,
	ABOUT$PAGE$SYSINFO$PAGE$OS$OS_NAME,
	ABOUT$PAGE$SYSINFO$PAGE$OS$OS_VERSION,
	ABOUT$PAGE$SYSINFO$PAGE$OS$OS_ARCH,
	ABOUT$PAGE$SYSINFO$PAGE$OS$AVAIL_PROC,
	ABOUT$PAGE$SYSINFO$PAGE$OS$AVG_SYS_LOAD,
	ABOUT$PAGE$SYSINFO$PAGE$SYS_PROPS$NAME,
	
	ABOUT$PAGE$TRANSLATIONS$NAME,
	ABOUT$PAGE$TRANSLATIONS$COL_FLAG,
	ABOUT$PAGE$TRANSLATIONS$COL_LANGUAGE,
	ABOUT$PAGE$TRANSLATIONS$COL_LOCAL_NAME,
	ABOUT$PAGE$TRANSLATIONS$COL_TRANSLATORS,
	ABOUT$PAGE$TRANSLATIONS$COL_TARGET_VERSION,
	ABOUT$PAGE$TRANSLATIONS$COL_VERSION,
	ABOUT$PAGE$TRANSLATIONS$TRANSLATORS_CARDS( new Param( "languageName", "Language name" ) ),
	ABOUT$PAGE$TRANSLATIONS$TRANSLATORS_CARDS_INFO,
	
	ACTION$ABOUT( Param.PARAMS_APP_NAME ),
	ACTION$EXIT,
	ACTION$HOME_PAGE,
	ACTION$OFFICIAL_FORUM,
	ACTION$NEW_EMAIL,
	ACTION$NEW_WINDOW,
	ACTION$REGISTRATION_INFO,
	ACTION$REGISTRATION_PAGE,
	ACTION$SAVE_SETTINGS_NOW,
	ACTION$SAVE_SETTINGS_ON_EXIT,
	ACTION$SETTINGS,
	ACTION$SHOW_COMMAND_BAR,
	ACTION$SHOW_TOOL_BAR,
	ACTION$SPLIT_VERTICALLY,
	ACTION$SYSTEM_INFO,
	
	ACTION$CMD_BAR$VIEW,
	ACTION$CMD_BAR$EDIT,
	ACTION$CMD_BAR$COPY,
	ACTION$CMD_BAR$MOVE,
	ACTION$CMD_BAR$NEW_FOLDER,
	ACTION$CMD_BAR$DELETE,
	ACTION$CMD_BAR$REFRESH,
	ACTION$CMD_BAR$CLOSE,
	
	FILE_OP$CURRENT,
	FILE_OP$DESTINATION,
	FILE_OP$CURRENT_SIZE,
	FILE_OP$TOTAL_SIZE,
	FILE_OP$TOTAL_COUNT,
	FILE_OP$COUNTING,
	FILE_OP$PAUSED,
	FILE_OP$ABORTED,
	FILE_OP$DONE,
	FILE_OP$FILE_EXISTS,
	FILE_OP$SKIPPED,
	FILE_OP$REVERT_EDIT,
	FILE_OP$BTN$PAUSE,
	FILE_OP$BTN$RESUME,
	FILE_OP$BTN$SKIP,
	FILE_OP$BTN$SKIP_ALL,
	FILE_OP$BTN$RETRY,
	FILE_OP$BTN$OVERWRITE,
	FILE_OP$BTN$OVERWRITE_ALL,
	FILE_OP$BTN$RENAME,
	FILE_OP$COPY$NAME,
	FILE_OP$COPY$CONFIRM_MSG,
	FILE_OP$COPY$CONFIRM_MSG2( new Param( "filesCount", "Input files and folders count" ) ),
	FILE_OP$COPY$MAIN_TASK,
	FILE_OP$COPY$ERR$COULD_NOT_COPY_FILE,
	FILE_OP$COPY$ERR$COULD_NOT_CREATE_FOLDER,
	FILE_OP$DELETE$NAME,
	FILE_OP$DELETE$CONFIRM_MSG,
	FILE_OP$DELETE$CONFIRM_MSG2( new Param( "filesCount", "Input files and folders count" ) ),
	FILE_OP$DELETE$MAIN_TASK,
	FILE_OP$DELETE$ERR$COULD_NOT_DEL_FILE,
	FILE_OP$DELETE$ERR$COULD_NOT_DEL_FOLDER,
	
	FOLDER_TAB$OPEN_NEW_TAB_TTIP,
	FOLDER_TAB$OPENING_NEW_TAB,
	FOLDER_TAB$POPULATING,
	
	FOLDER_TABLE$COLUMN$ACCESSED,
	FOLDER_TABLE$COLUMN$ACCESSED_DESC,
	FOLDER_TABLE$COLUMN$ATTR,
	FOLDER_TABLE$COLUMN$ATTR_DESC,
	FOLDER_TABLE$COLUMN$CREATED,
	FOLDER_TABLE$COLUMN$CREATED_DESC,
	FOLDER_TABLE$COLUMN$DATE,
	FOLDER_TABLE$COLUMN$DATE_DESC,
	FOLDER_TABLE$COLUMN$EXT,
	FOLDER_TABLE$COLUMN$EXT_DESC,
	FOLDER_TABLE$COLUMN$ICON,
	FOLDER_TABLE$COLUMN$ICON_DESC,
	FOLDER_TABLE$COLUMN$NAME,
	FOLDER_TABLE$COLUMN$NAME_DESC,
	FOLDER_TABLE$COLUMN$MIME_TYPE,
	FOLDER_TABLE$COLUMN$MIME_TYPE_DESC,
	FOLDER_TABLE$COLUMN$OWNER,
	FOLDER_TABLE$COLUMN$OWNER_DESC,
	FOLDER_TABLE$COLUMN$SIZE,
	FOLDER_TABLE$COLUMN$SIZE_DESC,
	FOLDER_TABLE$COLUMN$TYPE,
	FOLDER_TABLE$COLUMN$TYPE_DESC,
	
	FOLDER_TABLE$COLUMN_SETUP,
	FOLDER_TABLE$COLUMN_SETUP$AUTO_RESIZE_MODE,
	FOLDER_TABLE$COLUMN_SETUP$AUTO_RESIZE_MODE$INFO,
	FOLDER_TABLE$COLUMN_SETUP$REARRANGE_INFO,
	FOLDER_TABLE$COLUMN_SETUP$RESTORE_DEFAULT_SETUP,
	FOLDER_TABLE$COLUMN_SETUP$SORTING_INFO,
	
	FOLDER_TABLE$TAB_SETUP,
	FOLDER_TABLE$TAB_SETUP$TAB_LAYOUT_POLICY,
	FOLDER_TABLE$TAB_SETUP$TAB_LAYOUT_POLICY$INFO,
	FOLDER_TABLE$TAB_SETUP$TAB_PLACEMENT,
	FOLDER_TABLE$TAB_SETUP$RESTORE_DEFAULT_SETUP,
	
	FOLDER_TABLE$THEME$DEFAULT,
	FOLDER_TABLE$THEME$NATURE,
	FOLDER_TABLE$THEME$RETRO,
	FOLDER_TABLE$THEME$LAVA,
	FOLDER_TABLE$THEME$ACCESSIBILITY,
	
	FOLDER_TABLE$NO_SELECTED_FILES,
	
	GENERAL$BTN$APPLY,
	GENERAL$BTN$CANCEL,
	GENERAL$BTN$CLOSE,
	GENERAL$BTN$YES,
	GENERAL$BTN$NO,
	GENERAL$BTN$OK,
	GENERAL$CLOSE_TAB_TTIP,
	GENERAL$INFO,
	GENERAL$ERROR,
	GENERAL$FILES,
	GENERAL$FOLDERS,
	GENERAL$HOME_PAGE,
	GENERAL$YES,
	GENERAL$NO,
	GENERAL$OK,
	GENERAL$OPEN_LINK( new Param( "link", "Link" ) ),
	GENERAL$REQUIRES_RESTART,
	GENERAL$SIZE,
	GENERAL$UNIT$BYTES,
	GENERAL$UNIT$KB,
	GENERAL$UNIT$MB,
	GENERAL$UNIT$GB,
	GENERAL$UNIT$TB,
	GENERAL$UNIT$DAYS,
	GENERAL$UNIT$MS,
	GENERAL$VALID_RANGE( new Param( "minValue", "Min value" ), new Param( "maxValue", "Max value" ), new Param( "defaultValue", "Default value" ) ),
	GENERAL$WRITE_EMAIL( new Param( "to", "Addressee" ) ),
	
	MAIN_FRAME$NOT_REGISTERED,
	MAIN_FRAME$EXIT( Param.PARAMS_APP_NAME ),
	
	MENU$FILE,
	MENU$COMMAND_BAR,
	MENU$VIEW,
	MENU$VIEW$LOOK_AND_FEEL,
	MENU$VIEW$THEMES,
	MENU$HELP,
	
	MULTIPAGE_DIALOG$PREVIOUS_PAGE_BTN,
	MULTIPAGE_DIALOG$NEXT_PAGE_BTN,
	
	NAME_VALUE_TABLE$PROPERTY_NAME,
	NAME_VALUE_TABLE$PROPERTY_VALUE,
	
	NEW_FOLDER$TITLE,
	NEW_FOLDER$NEW_FOLDER_NAME,
	NEW_FOLDER$ERR$READ_ONLY_FS,
	NEW_FOLDER$ERR$INVALID_FOLDER_NAME,
	NEW_FOLDER$ERR$FILE_ALREADY_EXISTS,
	NEW_FOLDER$ERR$COULD_NOT_CREATE_FOLDER,
	
	PERSON_CARD$NAME,
	PERSON_CARD$HOME_PAGE,
	PERSON_CARD$COMMENT,
	
	REG_NOTE$TITLE,
	REG_NOTE$NOT_REGISTERED( Param.PARAMS_APP_NAME ),
	REG_NOTE$REGISTRATION_INFO,
	REG_NOTE$REGISTRATION_DETAILS,
	REG_NOTE$REGISTRATION_PAGE,
	REG_NOTE$PRESS_BUTTON_TO_HIDE( new Param( "buttonNumber", "Button number" ) ),
	
	ROOT_POPUP$REFRESH,
	
	SETTINGS$CLEAR_SEARCH_TTIP,
	SETTINGS$TITLE,
	SETTINGS$VIEW,
	SETTINGS$SEARCH,
	SETTINGS$DEFAULT_BTN,
	
	SETTINGS$COMMA_SEPARATED_EXT_LIST,
	SETTINGS$ALWAYS_NEVER_GO_INTO_TIP,
	SETTINGS$DEFAULT_TYPES_TO_GO_INTO( new Param( "defaultTypes", "Default types list to go into (when executed)" ) ),
	
	STARTUP$PHASE( new Param( "startupPhase", "Startup phase" ) ),
	STARTUP$INITIALIZING_MAIN_WINDOW,
	
	TOOLBAR$TITLE,
	
	;
	
	
	/** Optional parameters of the text. */
	public final Param[] params;
	
	/**
	 * Creates a new TextKey.
	 */
	private TextKey() {
		this( Param.EMPTY_PARAMS );
	}
	
	/**
	 * Creates a new TextKey.
	 * @param params optional parameters of the text
	 */
	private TextKey( final Param... params ) {
		this.params = params;
	}
	
	
	
	/**
	 * Class describing parameters of a text.
	 * 
	 * <p>Instances are immutable.</p>
	 * 
	 * @author Andras Belicza
	 */
	public static class Param {
		
		/* Frequently used pre-defined params: */
		
		/** Application name single parameter. */
		public static final Param[] EMPTY_PARAMS = new Param[ 0 ];
		
		/** Application name single parameter. */
		public static final Param[] PARAMS_APP_NAME = new Param[] { new Param( "appName", "Application name" ) };
		
		
		/**
		 * Template of the parameter (as it appears in texts).<br>
		 * The template is in the form of <code>"${name}"</code> (<code>name</code> is received as constructor argument).
		 */
		public final String template;
		/** Description of the parameter. */
		public final String description;
		
        /**
         * Creates a new TextKey.Param.
         * @param name        name of the parameter
         * @param description description of the parameter
         */
        public Param( final String name, final String description ) {
        	this.template    = "${" + name + "}";
        	this.description = description;
        }
        
	}
	
}
