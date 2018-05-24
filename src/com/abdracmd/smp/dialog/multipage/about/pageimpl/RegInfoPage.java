/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage.about.pageimpl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.abdracmd.Consts;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.uicomponent.PersonCard;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;
import com.abdracmd.util.SizeFormat;
import com.abdracmd.util.bean.reginfo.RegistrationInfo;
import com.abdracmd.util.bean.reginfo.SystemInfo;

/**
 * Registration information about page.
 * 
 * @author Andras Belicza
 */
public class RegInfoPage extends BasePage {
	
    /**
     * Creates a new RegInfoPage.
     */
    public RegInfoPage() {
    	super( TextKey.ABOUT$PAGE$REG_INFO$NAME, XIcon.F_LICENSE_KEY );
    }
    
	@Override
	public JComponent createPage() {
		final Box box = Box.createVerticalBox();
		
    	final JPanel regPageLinkRow = new JPanel( new FlowLayout( FlowLayout.CENTER, 5, 0 ) );
    	regPageLinkRow.add( new JLabel( get( TextKey.REG_NOTE$REGISTRATION_DETAILS ) ) );
    	regPageLinkRow.add( GuiUtils.createLinkLabel( get( TextKey.REG_NOTE$REGISTRATION_PAGE ), Consts.URL_REGISTRATION_PAGE, null ) );
		
		final RegistrationInfo regInfo = mainModel.getRegistrationInfo();
		
		if ( regInfo == null ) {
	    	box.add( Box.createVerticalStrut( 20 ) );
			box.add( GuiUtils.changeFontToBold( new JLabel( get( TextKey.REG_NOTE$NOT_REGISTERED, Consts.APP_NAME ) ) ) );
	    	box.add( Box.createVerticalStrut( 20 ) );
			box.add( new JLabel( get( TextKey.ABOUT$PAGE$REG_INFO$PLEASE_REGISTER ) ) );
	    	box.add( Box.createVerticalStrut( 20 ) );
	    	box.add( regPageLinkRow );
	    	
	    	GuiUtils.alignBox( box, SwingConstants.CENTER );
			return box;
		}
		
    	box.add( Box.createVerticalStrut( 10 ) );
		box.add( regPageLinkRow );
		
		box.add( GuiUtils.wrapInPanel( new PersonCard( regInfo.getRegisteredPerson(), get( TextKey.ABOUT$PAGE$REG_INFO$REGISTERED_PERSON ) ) ) );
		
		final Map< String, Object > map = new LinkedHashMap<>(); // LinkedHashMap to preserve order
		
		// Registration info details
		Box tableBox = Box.createVerticalBox();
		tableBox.setBorder( BorderFactory.createTitledBorder( get( TextKey.ABOUT$PAGE$REG_INFO$REGINFO_DETAILS ) ) );
		
		map.put( get( TextKey.ABOUT$PAGE$REG_INFO$REGISTRATION_DATE ), formatDate( regInfo.getRegistrationDate() ) );
		map.put( get( TextKey.ABOUT$PAGE$REG_INFO$ROAMING_ENABLED   ), regInfo.isRoamingEnabled() );
		
		JTable table = GuiUtils.createNameValueTable( map.entrySet(), false );
		tableBox.add( table.getTableHeader() );
		tableBox.add( table );
		box.add( tableBox );
		
		// Registered system info
		map.clear();
		
		tableBox = Box.createVerticalBox();
		tableBox.setBorder( BorderFactory.createTitledBorder( get( TextKey.ABOUT$PAGE$REG_INFO$REGISTERED_SYSTEM_INFO ) ) );
		
		final SystemInfo sysInfo = regInfo.getSystemInfo();
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$OS$OS_NAME    ), sysInfo.getOsName() );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$OS$OS_VERSION ), sysInfo.getOsVersion() );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$OS$OS_ARCH    ), sysInfo.getOsArchitecture() );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$OS$AVAIL_PROC ), sysInfo.getAvailableProcessors() );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$USER_NAME          ), sysInfo.getUserName() );
		map.put( get( TextKey.ABOUT$PAGE$SYSINFO$USER_TIME_ZONE     ), sysInfo.getUserTimeZone() );
		map.put( get( TextKey.ABOUT$PAGE$REG_INFO$MAIN_ROOT_SIZE    ), AcUtils.getFormattedSize( SizeFormat.AUTO, sysInfo.getMainRootSize(), 1 ) );
		
		table = GuiUtils.createNameValueTable( map.entrySet(), false );
		tableBox.add( table.getTableHeader() );
		tableBox.add( table );
		box.add( tableBox );
		
		box.add( new JPanel( new BorderLayout() ) );
		
		return box;
	}
	
}
