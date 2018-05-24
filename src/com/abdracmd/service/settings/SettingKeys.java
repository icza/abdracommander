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

import static com.abdracmd.service.settings.SettingLevel.ADVANCED;
import static com.abdracmd.service.settings.SettingLevel.BASIC;
import static com.abdracmd.service.settings.SettingLevel.HIDDEN;

import hu.belicza.andras.util.TypeList;
import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.iface.Provider;
import hu.belicza.andras.util.iface.SimpleProvider;

import java.util.EnumSet;

import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.SortOrder;

import com.abdracmd.Consts;
import com.abdracmd.fileop.impl.ActionOnError;
import com.abdracmd.fileop.impl.ActionWhenFileExists;
import com.abdracmd.service.language.Language;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.log.LogLevel;
import com.abdracmd.service.settings.viewhint.ViewHints;
import com.abdracmd.smp.dialog.fileop.BeepOnCompletion;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.folderstabbed.TabLayoutPolicy;
import com.abdracmd.smp.mainframe.folderstabbed.TabPlacement;
import com.abdracmd.smp.mainframe.folderstabbed.folder.AutoResizeMode;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderPresenter;
import com.abdracmd.smp.mainframe.folderstabbed.folder.GoIntoFilesMode;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.AttrColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.DateColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.ExtColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.IconColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.NameColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.impl.SizeColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.impl.DefaultFolderPresenterTheme;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.SizeFormat;

/**
 * Setting keys.
 * 
 * @author Andras Belicza
 */
public class SettingKeys {
	
	
	// ========================================================
	// ======= APP ============================================
	// ========================================================
	
	
	/** Application language. */
	public static final FixedValuesSettingKey< String >
		APP$LANGUAGE = new FixedValuesSettingKey<>(
			BASIC, "APP$LANGUAGE", String.class, Consts.DEFAULT_LANGUAGE_NAME,
			new ViewHints().add( ViewHints.SUBSEQUENT_TEXT, TextKey.GENERAL$REQUIRES_RESTART ).add( ViewHints.VALUE_ICON_PROVIDER, new Provider< Icon, Object >() {
				/**
				 * Icon provider for language names. If the specified value is not {@link String}, returns <code>null</code>.
				 */
				@Override
				public Icon provide( final Object value ) {
					return value instanceof String ? MainShell.INSTANCE.getIcons().getLanguageIcon( (String) value ) : null;
				}
			} ),
			AcUtils.getLanguageNameList().toArray( new String[ 0 ] )
		);
	/** Application Look&Feel. */
	public static final FixedValuesSettingKey< String >
		APP$LOOK_AND_FEEL = new FixedValuesSettingKey<>(
			BASIC, "APP$LOOK_AND_FEEL", String.class, "Nimbus", AcUtils.getSortedInstalledLAFInfos()
		);
	/** Application wide Log level. */
	public static final EnumSettingKey< LogLevel >
		APP$LOG_LEVEL = new EnumSettingKey<>(
			BASIC, "APP$LOG_LEVEL" , LogLevel.class,
			Consts.DEV_MODE ? LogLevel.DEBUG : LogLevel.INFO
		);
	/** Log file lifetime in days. */
	public static final RangeSettingKey< Integer >
		APP$LOG_FILE_LIFETIME = new RangeSettingKey<>(
			BASIC, "APP$LOG_FILE_LIFETIME", Integer.class, Consts.DEV_MODE ? 10_000 : 14,
			new ViewHints().add( ViewHints.SUBSEQUENT_TEXT, TextKey.GENERAL$UNIT$DAYS ),
			0, 10_000
		);
	/** Tells if error beeps are enabled. */
	public static final SettingKey< Boolean >
		APP$BEEP_ON_ERRORS = new SettingKey<>(
			BASIC, "APP$BEEP_ON_ERRORS", Boolean.class, Boolean.TRUE
		);
	/** Get confirmation before exit. */
	public static final SettingKey< Boolean >
		APP$CONFIRM_EXIT = new SettingKey<>(
			BASIC, "APP$CONFIRM_EXIT", Boolean.class, Boolean.FALSE
		);
	/** Save settings on exit. */
	public static final SettingKey< Boolean >
		APP$SAVE_SETTINGS_ON_EXIT = new SettingKey<>(
			BASIC, "APP$SAVE_SETTINGS_ON_EXIT", Boolean.class, Boolean.TRUE
		);
	/** Setting level. */
	public static final FixedValuesSettingKey< SettingLevel >
		APP$SETTING_LEVEL = new FixedValuesSettingKey<>(
			HIDDEN, "APP$SETTING_LEVEL", SettingLevel.class,
			Consts.DEV_MODE ? SettingLevel.ADVANCED : SettingLevel.BASIC, EnumSet.complementOf( EnumSet.of( SettingLevel.HIDDEN ) ).toArray( new SettingLevel[ 0 ] )
		);
	
	
	// ========================================================
	// ======= MAIN FRAME =====================================
	// ========================================================
	
	
	/** Refresh main frame content when activated. */
	public static final SettingKey< Boolean >
		MAIN_FRAME$REFRESH_ON_ACTIVATE = new SettingKey<>(
			ADVANCED, "MAIN_FRAME$REFRESH_ON_ACTIVATE", Boolean.class, Boolean.TRUE
		);
	/** Main frame show command bar. */
	public static final SettingKey< Boolean >
		MAIN_FRAME$SHOW_COMMAND_BAR = new SettingKey<>(
			HIDDEN, "MAIN_FRAME$SHOW_COMMAND_BAR", Boolean.class, Boolean.TRUE
		);
	/** Main frame show tool bar. */
	public static final SettingKey< Boolean >
		MAIN_FRAME$SHOW_TOOL_BAR = new SettingKey<>(
			HIDDEN, "MAIN_FRAME$SHOW_TOOL_BAR", Boolean.class, Boolean.TRUE
		);
	/** Main frame split continuous layout. */
	public static final SettingKey< Boolean >
		MAIN_FRAME$SPLIT_CONTINOUS_LAYOUT = new SettingKey<>(
			ADVANCED, "MAIN_FRAME$SPLIT_CONTINOUS_LAYOUT", Boolean.class, Boolean.TRUE
		);
	/** Main frame split vertically. */
	public static final SettingKey< Boolean >
		MAIN_FRAME$SPLIT_VERTICALLY = new SettingKey<>(
			HIDDEN, "MAIN_FRAME$SPLIT_VERTICALLY", Boolean.class, Boolean.TRUE
		);
	
	
	// ========================================================
	// ======= FOLDER TABS ====================================
	// ========================================================
	
	
	/** Tab layout policy in the folders tabbed pane. */
	public static final EnumSettingKey< TabLayoutPolicy >
		FOLDER_TABS$LAYOUT_POLICY = new EnumSettingKey<>(
			HIDDEN, "FOLDER_TABS$LAYOUT_POLICY", TabLayoutPolicy.class, TabLayoutPolicy.MULTI_LINE
		);
	/** Tab placement in the folders tabbed pane. */
	public static final EnumSettingKey< TabPlacement >
		FOLDER_TABS$PLACEMENT = new EnumSettingKey<>(
			HIDDEN, "FOLDER_TABS$PLACEMENT", TabPlacement.class, TabPlacement.TOP
		);
	// TODO make another: refresh on focus
	/** Refresh tab content when selected. */
	public static final SettingKey< Boolean >
		FOLDER_TABS$REFRESH_ON_SELECT = new SettingKey<>(
			ADVANCED, "FOLDER_TABS$REFRESH_ON_SELECT", Boolean.class, Boolean.TRUE
		);
	
	
	// ========================================================
	// ======= FOLDER TABLE ===================================
	// ========================================================
	
	
	/** Folder presenter theme (class name). */
	public static final FixedValuesSettingKey< String >
		FOLDER_TABLE$THEME = new FixedValuesSettingKey<>(
			BASIC, "FOLDER_TABLE$THEME", String.class, DefaultFolderPresenterTheme.class.getName(),
			new ViewHints().add( ViewHints.VALUE_DISPLAY_NAME_PROVIDER, new Provider< String, Object >() {
				/**
				 * Display name provider for folder presenter theme classes. If the specified value is not {@link String}, returns <code>null</code>.
				 */
				@Override
				public String provide( final Object value ) {
					return value instanceof String ? MainShell.INSTANCE.getModel().getFolderPresenterThemeRegistry().get( (String) value ).getDisplayName() : null;
				}
			} ),
			DefaultFolderPresenterTheme.class.getName()
		);
	/** Go into files mode when executed. */
	public static final EnumSettingKey< GoIntoFilesMode >
		FOLDER_TABLE$GO_INTO_FILES_MODE = new EnumSettingKey<>(
			ADVANCED, "FOLDER_TABLE$GO_INTO_FILES_MODE", GoIntoFilesMode.class, GoIntoFilesMode.DEFAULT_PLUS_CUSTOM_TYPES,
			new ViewHints().add( ViewHints.TOOL_TIP_TEXT_PROVIDER, new SimpleProvider< String >() {
				@Override
				public String provide() {
					return AcUtils.CAH.get( TextKey.SETTINGS$DEFAULT_TYPES_TO_GO_INTO, FolderPresenter.DEFAULT_LOWERED_GO_INTO_TYPES.convertToString( ',' ) );
				}
			} ).add( ViewHints.SUBSEQUENT_TEXT, TextKey.SETTINGS$ALWAYS_NEVER_GO_INTO_TIP )
		);
	/** Custom types to go into. */
	public static final SettingKey< TypeList< String > >
		FOLDER_TABLE$CUSTOM_TYPES_TO_GO_INTO = new SettingKey<>(
			ADVANCED, "FOLDER_TABLE$CUSTOM_TYPES_TO_GO_INTO", Utils.< Class< TypeList< String > > >cast( TypeList.class ),
			new TypeList< String >( String.class, 0 ),
			new ViewHints().add( ViewHints.COMPONENT_TYPE, JTextField.class ).add( ViewHints.SUBSEQUENT_TEXT, TextKey.SETTINGS$COMMA_SEPARATED_EXT_LIST )
		);
	/** Move focus to the next row when using Space to select/deselect. */
	public static final SettingKey< Boolean >
		FOLDER_TABLE$MOVE_FOCUS_WHEN_USING_SPACE = new SettingKey<>(
			BASIC, "FOLDER_TABLE$MOVE_FOCUS_WHEN_USING_SPACE", Boolean.class, Boolean.FALSE
		);
	/** Auto resize mode of the folder presenter. */
	public static final EnumSettingKey< AutoResizeMode >
		FOLDER_TABLE$AUTO_RESIZE_MODE = new EnumSettingKey<>(
			HIDDEN, "FOLDER_TABLE$AUTO_RESIZE_MODE", AutoResizeMode.class, AutoResizeMode.SUBSEQUENT_COLUMNS
		);
	/** Column list of the folder presenter (excluding internal columns). */
	public static final SettingKey< TypeList< String > >
		FOLDER_TABLE$COLUMN_LIST = new SettingKey<>(
			HIDDEN, "FOLDER_TABLE$COLUMN_LIST", Utils.< Class< TypeList< String > > >cast( TypeList.class ),
			new TypeList<>( String.class, IconColumn.class.getName(), NameColumn.class.getName(), ExtColumn.class.getName(), SizeColumn.class.getName(), DateColumn.class.getName(), AttrColumn.class.getName() )
		);
	/** Sorting column of the folder presenter. */
	public static final SettingKey< String >
		FOLDER_TABLE$SORTING_COLUMN = new SettingKey<>(
			HIDDEN, "FOLDER_TABLE$SORTING_COLUMN", String.class, ExtColumn.class.getName()
		);
	/** Sort order column of the folder presenter. */
	public static final EnumSettingKey< SortOrder >
		FOLDER_TABLE$SORT_ORDER = new EnumSettingKey<>(
			HIDDEN, "FOLDER_TABLE$SORT_ORDER", SortOrder.class, SortOrder.ASCENDING
		);
	
	
	// ========================================================
	// ======= FILE LIST=======================================
	// ========================================================
	
	
	/** Show hidden and system files. */
	public static final SettingKey< Boolean >
		FILE_LISTING$SHOW_HIDDEN_SYSTEM_FILES = new SettingKey<>(
			ADVANCED, "FILE_LISTING$SHOW_HIDDEN_SYSTEM_FILES", Boolean.class, Consts.DEV_MODE ? Boolean.TRUE : Boolean.FALSE
		);
	/** Size column format in the folder presenter. */
	public static final EnumSettingKey< SizeFormat >
		FILE_LISTING$SIZE_COLUMN_FORMAT = new EnumSettingKey<>(
			BASIC, "FILE_LISTING$SIZE_COLUMN_FORMAT", SizeFormat.class, SizeFormat.AUTO
		);
	/** Size column fraction digits. */
	public static final RangeSettingKey< Integer >
		FILE_LISTING$SIZE_COLUMN_FRACTION_DIGITS = new RangeSettingKey<>(
			BASIC, "FILE_LISTING$SIZE_COLUMN_FRACTION_DIGITS", Integer.class, 1, 0, Language.MAX_FRACTION_DIGITS
		);
	/** Size summary format in the folder presenter. */
	public static final EnumSettingKey< SizeFormat >
		FILE_LISTING$SIZE_SUMMARY_FORMAT = new EnumSettingKey<>(
			BASIC, "FILE_LISTING$SIZE_SUMMARY_FORMAT", SizeFormat.class, SizeFormat.AUTO
		);
	/** Size summary fraction digits. */
	public static final RangeSettingKey< Integer >
		FILE_LISTING$SIZE_SUMMARY_FRACTION_DIGITS = new RangeSettingKey<>(
			BASIC, "FILE_LISTING$SIZE_SUMMARY_FRACTION_DIGITS", Integer.class, 1, 0, Language.MAX_FRACTION_DIGITS
		);
	
	
	// ========================================================
	// ======= FILE OPERATIONS ================================
	// ========================================================
	
	
	/** Show completion in window title. */
	public static final SettingKey< Boolean >
		FILE_OP$SHOW_COMPLETION_PERCENT_IN_TITLE = new SettingKey<>(
			BASIC, "FILE_OP$SHOW_COMPLETION_PERCENT_IN_TITLE", Boolean.class, Boolean.TRUE
		);
	/** Close file op dialog when operation completed. */
	public static final SettingKey< Boolean >
		FILE_OP$CLOSE_DIALOG_WHEN_COMPLETED = new SettingKey<>(
			BASIC, "FILE_OP$CLOSE_DIALOG_WHEN_COMPLETED", Boolean.class, Boolean.FALSE
		);
	/** Beep on completion modes. */
	public static final EnumSettingKey< BeepOnCompletion >
	FILE_OP$BEEP_ON_COMPLETION = new EnumSettingKey<>(
			BASIC, "FILE_OP$BEEP_ON_COMPLETION", BeepOnCompletion.class, BeepOnCompletion.WHEN_WINDOW_INACTIVE
		);
	/** Buffer size used for file operations, in KB. */
	public static final RangeSettingKey< Integer >
		FILE_OP$BUFFER_SIZE = new RangeSettingKey<>(
			ADVANCED, "FILE_OP$BUFFER_SIZE", Integer.class, 128,
			new ViewHints().add( ViewHints.SUBSEQUENT_TEXT, TextKey.GENERAL$UNIT$KB ),
			16, 1024
		);
	/** Default action when file exists. */
	public static final EnumSettingKey< ActionWhenFileExists >
		FILE_OP$ACTION_WHEN_FILE_EXISTS = new EnumSettingKey<>(
			BASIC, "FILE_OP$ACTION_WHEN_FILE_EXISTS", ActionWhenFileExists.class, ActionWhenFileExists.ASK
		);
	/** Default action on error. */
	public static final EnumSettingKey< ActionOnError >
		FILE_OP$ACTION_ON_ERROR = new EnumSettingKey<>(
			BASIC, "FILE_OP$ACTION_ON_ERROR", ActionOnError.class, ActionOnError.ASK
		);
	/** Wipe delete (destroy data before delete). */
	public static final SettingKey< Boolean >
		FILE_OP$DELETE$WIPE_DELETE = new SettingKey<>(
			BASIC, "FILE_OP$DELETE$WIPE_DELETE", Boolean.class, Boolean.FALSE
		);
	
	
	// ========================================================
	// ======= META ===========================================
	// ========================================================
	
	
	/** Saved at time. */
	public static final SettingKey< Long >
		META$SAVED_AT = new SettingKey<>(
			HIDDEN, "META$SAVED_AT", Long.class, 0l
		);
	/** Saved with version. */
	public static final SettingKey< String >
		META$SAVED_WITH_VERSION = new SettingKey<>(
			HIDDEN, "META$SAVED_WITH_VERSION", String.class, Utils.EMPTY_STRING
		);
	
	
	// ========================================================
	// ======= NEW FOLDER =====================================
	// ========================================================
	
	
	/** Create all nonexistent parent folders on the path. */
	public static final SettingKey< Boolean >
		NEW_FOLDER$CREATE_NONEXISTENT_PARENTS = new SettingKey<>(
			HIDDEN, "NEW_FOLDER$CREATE_NONEXISTENT_PARENTS", Boolean.class, Boolean.TRUE
		);
	
	
	// ========================================================
	// ======= ROOTS POPUP ====================================
	// ========================================================
	
	
	/** Refresh roots on command bar refresh action. */
	public static final SettingKey< Boolean >
		ROOTS_POPUP$REFRESH_ON_CMD_BAR_REFRESH = new SettingKey<>(
			ADVANCED, "ROOTS_POPUP$REFRESH_ON_CMD_BAR_REFRESH", Boolean.class, Boolean.TRUE
		);
	
	
	// ========================================================
	// ======= UI =============================================
	// ========================================================
	
	
	/** Tool tip initial delay in milliseconds. */
	public static final RangeSettingKey< Integer >
		UI$TOOL_TIP_INITIAL_DELAY = new RangeSettingKey<>(
			BASIC, "UI$TOOL_TIP_INITIAL_DELAY", Integer.class, 200,
			new ViewHints().add( ViewHints.SUBSEQUENT_TEXT, TextKey.GENERAL$UNIT$MS ),
			0, 10_000
		);
	/** Tool tip dismiss delay in milliseconds. */
	public static final RangeSettingKey< Integer >
		UI$TOOL_TIP_DISMISS_DELAY = new RangeSettingKey<>(
			BASIC, "UI$TOOL_TIP_DISMISS_DELAY", Integer.class, 10_000,
			new ViewHints().add( ViewHints.SUBSEQUENT_TEXT, TextKey.GENERAL$UNIT$MS ),
			0, 60_000
		);
	/** Display URLs of links in tool tips. */
	public static final SettingKey< Boolean >
		UI$DISPLAY_LINK_TOOL_TIPS = new SettingKey<>(
			BASIC, "UI$DISPLAY_LINK_TOOL_TIPS", Boolean.class, Boolean.TRUE
		);
	
	
	
	/**
	 * Creates a new SettingKeys.
	 * Private because no need to instantiate this class.
	 */
	private SettingKeys() {
	}
	
}
