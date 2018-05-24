/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage.about.pageimpl.systeminfo;

import hu.belicza.andras.util.Utils;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.uicomponent.smarttable.SmartTable;
import com.abdracmd.util.GuiUtils;

/**
 * Network interfaces about page.
 * 
 * @author Andras Belicza
 */
public class NetworkInterfacesPage extends BasePage {
	
    /**
     * Creates a new NetworkInterfacesPage.
     */
    public NetworkInterfacesPage() {
    	super( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$NAME, XIcon.F_NETWORK_ETHERNET );
    }
    
	@Override
	public JComponent createPage() {
		final Box box = Box.createVerticalBox();
		
		final JTable table = new SmartTable();
		
		try {
			final Vector< Vector< Object > > dataVector = new Vector<>();
			final Enumeration< NetworkInterface > networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while ( networkInterfaces.hasMoreElements() ) {
				final Vector< Object > row = new Vector<>();
				final NetworkInterface ni = networkInterfaces.nextElement();
				
				row.add( ni.getIndex() );
				row.add( ni.getDisplayName() );
				row.add( ni.getName() );
				
				// IP addresses
				final StringBuilder iab = new StringBuilder();
				final Enumeration< InetAddress > inetAddresses = ni.getInetAddresses();
				while ( inetAddresses.hasMoreElements() ) {
					if ( iab.length() > 0 )
						iab.append( ", " );
					iab.append( inetAddresses.nextElement().getHostAddress() );
				}
				row.add( iab.toString() );
				
				final byte[] mac = ni.getHardwareAddress();
				row.add( mac == null ? null : Utils.toHexString( mac, "-" ) );
				
				row.add( ni.isUp() );
				row.add( ni.isVirtual() );
				row.add( ni.isPointToPoint() );
				row.add( ni.supportsMulticast() );
				row.add( ni.getMTU() );
				
				// Interface addresses
				iab.setLength( 0 );
				for ( final InterfaceAddress ia : ni.getInterfaceAddresses() ) {
					if ( iab.length() > 0 )
						iab.append( ", " );
					iab.append( ia.toString() );
				}
				row.add( iab.toString() );
				
				dataVector.add( row );
			}
			
			final Vector< Object > columnIdnetifiers = new Vector<>();
			columnIdnetifiers.add( "#" );
			columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_DISPLAY_NAME        ) );
			columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_NAME                ) );
			columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_IP_ADDRESSES        ) );
			columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_MAC_ADDRESS         ) );
			columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_UP                  ) );
			columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_VIRTUAL             ) );
			columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_POINT_TO_POINT      ) );
			columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_MULTICAST           ) );
			columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_MTU                 ) );
			columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$COL_INTERFACE_ADDRESSES ) );
			( (DefaultTableModel) table.getModel() ).setDataVector( dataVector, columnIdnetifiers );
			
			GuiUtils.packTable( table );
			
			box.add( table.getTableHeader() );
			box.add( GuiUtils.wrapInBorderPanel( table ) );
		} catch ( final SocketException se ) {
			warning( "Failed to inspect network interfaces!", se );
			box.add( GuiUtils.changeFontToItalic( new JLabel( get( TextKey.ABOUT$PAGE$SYSINFO$PAGE$NET_IF$FAILED_TO_INSPECT ) ) ) );
			GuiUtils.alignBox( box, SwingConstants.CENTER );
		}
		
		return box;
	}
	
}
