/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.service.icon;

import java.net.URL;

import com.abdracmd.Consts;
import com.abdracmd.smp.main.MainShell;

/**
 * Icons.
 * 
 * <p>Icon resource URLs are constructed runtime (startup), so we'll get notification of missing icons
 * at startup.</p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "javadoc" )
public enum XIcon {
	
    MISC_GOOGLE_PLUS( "misc/google_plus.png" ),
    MISC_FACEBOOK   ( "misc/facebook.gif"    ),
    MISC_JAVA       ( "misc/java.gif"        ),
    MISC_LINKEDIN   ( "misc/linkedin.gif"    ),
    MISC_LINUX      ( "misc/linux.png"       ),
    MISC_SOLARIS    ( "misc/solaris.png"     ),
    MISC_TWITTER    ( "misc/twitter.gif"     ),
    MISC_YOUTUBE    ( "misc/youtube.gif"     ),
    
    MY_APP_ICON_BIG  ( "my/app_icon_64x64.png"   ),
    MY_APP_ICON_SMALL( "my/app_icon_16x16.png"   ),
    MY_APP_LOGO      ( "my/app_logo_500x366.png" ),
    MY_EMPTY         ( "my/empty_16x16.png"      ),
    
    F_APPLICATION_PLUS        ( getFugueRes( "application--plus"        ) ),
    F_APPLICATION_BLUE        ( getFugueRes( "application-blue"         ) ),
    F_APPLICATION_BROWSER     ( getFugueRes( "application-browser"      ) ),
    F_APPLICATION_ICON_LARGE  ( getFugueRes( "application-icon-large"   ) ),
    F_ARROW_090_MEDIUM        ( getFugueRes( "arrow-090-medium"         ) ),
    F_ARROW_090_SMALL         ( getFugueRes( "arrow-090-small"          ) ),
    F_ARROW_180               ( getFugueRes( "arrow-180"                ) ),
    F_ARROW_270_MEDIUM        ( getFugueRes( "arrow-270-medium"         ) ),
    F_ARROW_270_SMALL         ( getFugueRes( "arrow-270-small"          ) ),
    F_ARROW_CIRCLE_315        ( getFugueRes( "arrow-circle-315"         ) ),
    F_ARROW_TURN_090          ( getFugueRes( "arrow-turn-090"           ) ),
    F_ARROW                   ( getFugueRes( "arrow"                    ) ),
    F_BALLOON                 ( getFugueRes( "balloon"                  ) ),
    F_CARD_ADDRESS            ( getFugueRes( "card-address"             ) ),
    F_CATEGORIES              ( getFugueRes( "categories"               ) ),
    F_COLOR_SWATCHES          ( getFugueRes( "color-swatches"           ) ),
    F_COMPUTER                ( getFugueRes( "computer"                 ) ),
    F_CROSS_BUTTON            ( getFugueRes( "cross-button"             ) ),
    F_CROSS_OCTAGON           ( getFugueRes( "cross-octagon"            ) ),
    F_CROSS_SMALL             ( getFugueRes( "cross-small"              ) ),
    F_CROSS                   ( getFugueRes( "cross"                    ) ),
    F_DISK_ARROW              ( getFugueRes( "disk--arrow"              ) ),
    F_DISK                    ( getFugueRes( "disk"                     ) ),
    F_DOCUMENT_CONVERT        ( getFugueRes( "document-convert"         ) ),
    F_DOCUMENT_COPY           ( getFugueRes( "document-copy"            ) ),
    F_DOCUMENT_EXPORT         ( getFugueRes( "document-export"          ) ),
    F_DOCUMENT_TEXT           ( getFugueRes( "document-text"            ) ),
    F_DOCUMENTS_STACK         ( getFugueRes( "documents-stack"          ) ),
    F_FOLDER_PLUS             ( getFugueRes( "folder--plus"             ) ),
    F_DOOR_OPEN_IN            ( getFugueRes( "door-open-in"             ) ),
    F_DRIVE                   ( getFugueRes( "drive"                    ) ),
    F_GEAR                    ( getFugueRes( "gear"                     ) ),
    F_HOURGLASS               ( getFugueRes( "hourglass"                ) ),
    F_INFORMATION             ( getFugueRes( "information"              ) ),
    F_LANGUAGE                ( getFugueRes( "language"                 ) ),
    F_LICENSE_KEY             ( getFugueRes( "license-key"              ) ),
    F_MAC_OS                  ( getFugueRes( "mac-os"                   ) ),
    F_MAIL_PENCIL             ( getFugueRes( "mail--pencil"             ) ),
    F_MAIL_AT_SIGN            ( getFugueRes( "mail-at-sign"             ) ),
    F_MAIL                    ( getFugueRes( "mail"                     ) ),
    F_MEMORY                  ( getFugueRes( "memory"                   ) ),
    F_NA                      ( getFugueRes( "na"                       ) ),
    F_NETWORK_ETHERNET        ( getFugueRes( "network-ethernet"         ) ),
    F_OCCLUDER                ( getFugueRes( "occluder"                 ) ),
    F_PENCIL                  ( getFugueRes( "pencil"                   ) ),
    F_PLUS_SMALL              ( getFugueRes( "plus-small"               ) ),
    F_QUESTION                ( getFugueRes( "question"                 ) ),
    F_SCRIPT_TEXT             ( getFugueRes( "script-text"              ) ),
    F_TABLE                   ( getFugueRes( "table"                    ) ),
    F_TICK                    ( getFugueRes( "tick"                     ) ),
    F_UI_FLOW                 ( getFugueRes( "ui-flow"                  ) ),
    F_UI_LAYOUT_PANEL         ( getFugueRes( "ui-layout-panel"          ) ),
    F_UI_SPLIT_PANEL          ( getFugueRes( "ui-split-panel"           ) ),
    F_UI_STATUS_BAR           ( getFugueRes( "ui-status-bar"            ) ),
    F_UI_TAB                  ( getFugueRes( "ui-tab"                   ) ),
    F_UI_TOOLBAR              ( getFugueRes( "ui-toolbar"               ) ),
    F_USER_SILHOUETTE_QUESTION( getFugueRes( "user-silhouette-question" ) ),
    F_WINDOWS                 ( getFugueRes( "windows"                  ) ),
	
    F_32_CROSS_CIRCLE         ( getFugueRes( "x32/cross-circle"         ) ),
    F_32_EXCLAMATION          ( getFugueRes( "x32/exclamation"          ) ),
    F_32_INFORMATION          ( getFugueRes( "x32/information"          ) ),
    F_32_QUESTION             ( getFugueRes( "x32/question"             ) );
    
    
    // ICON ALIASES
    public static final XIcon COPY       = F_DOCUMENT_COPY;
    public static final XIcon DELETE     = F_CROSS;
    public static final XIcon REFRESH    = F_ARROW_CIRCLE_315;
    public static final XIcon NEW_FOLDER = F_FOLDER_PLUS;
    public static final XIcon EXIT       = F_DOOR_OPEN_IN;
    public static final XIcon FILE_OP    = F_DOCUMENT_CONVERT;
    public static final XIcon WAITING    = F_HOURGLASS;
    
    
	/** Icon resource. */
	public final URL resource;
	
    /**
     * Creates a new XIcon.
     * @param resourceName icon resource name
     */
    private XIcon( final String resourceName ) {
    	this( XIcon.class.getResource( resourceName ) );
    }
    
    /**
     * Creates a new XIcon.
     * @param resource icon resource
     */
    private XIcon( final URL resource ) {
    	this.resource = resource;
    	if ( resource == null ) {
    		MainShell.INSTANCE.getModel().getLogging().error( "Icon not found: " + this );
    		if ( Consts.DEV_MODE )
    			throw new RuntimeException();
    	}
    }
    
    /**
     * Returns the resource for the specified fugue icon.
     * @param name name of the fugue icon to return the resource for
     * @return the resource for the specified fugue icon
     */
    private static URL getFugueRes( final String name ) {
    	return XIcon.class.getResource( "fugue/" + name + ".png" );
    }
    
}
