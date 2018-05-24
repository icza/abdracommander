/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.uicomponent.smarttable;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.bean.Email;

import java.awt.Component;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.Language;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.util.AcUtils;

/**
 * A smart table cell renderer which handles the following values:
 * <ul>
 *     <li>{@link Icon}: renders the icon
 *     <li>{@link Date}: formats it using {@link Language#formatDateTime(Date)}
 *     <li>{@link Number}: formats it using {@link Language#formatNumber(long)}
 *     <li>{@link Email}: renders as link
 *     <li>{@link Boolean}: renders a tick icon for <code>true</code> and a cross icon for <code>false</code>
 *     <li>URL texts (defined by {@link Utils#isUrlText(String)}): renders as link
 * </ul>
 * 
 * @author Andras Belicza
 */
class SmartTableCellRenderer extends DefaultTableCellRenderer {
	
	/** Default serial version ID. */
	private static final long serialVersionUID = 1L;
	
	@Override
	public Component getTableCellRendererComponent( final JTable table, Object value, final boolean isSelected, final boolean hasFocus, final int row, int column ) {
		super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
		
		setToolTipText( null );
		
		if ( value instanceof Boolean )
			value = AcUtils.CAH.get( (Boolean) value ? XIcon.F_TICK : XIcon.F_CROSS );
		
		if ( value instanceof Icon ) {
			setIcon( (Icon) value );
			setText( null );
		}
		else {
			setIcon( null );
			if ( value instanceof Number )
				// TODO [OPTIMIZE]
				setText( AcUtils.CAH.formatNumber( ( (Number) value ).longValue() ) + "    " ); // Leave space at the end since it is right aligned
			else if ( value instanceof Date )
				setText( AcUtils.CAH.formatDateTime( (Date) value ) );
			else if ( value instanceof String ) {
				if ( Utils.isUrlText( (String) value ) ) {
					setText( Utils.createHtmlLink( (String) value ) );
					if ( AcUtils.CAH.get( SettingKeys.UI$DISPLAY_LINK_TOOL_TIPS ) )
						setToolTipText( AcUtils.CAH.get( TextKey.GENERAL$OPEN_LINK, value ) );
				}
			} else if ( value instanceof Email ) {
				setText( Utils.createHtmlLink( value.toString() ) );
				if ( AcUtils.CAH.get( SettingKeys.UI$DISPLAY_LINK_TOOL_TIPS ) )
					setToolTipText( AcUtils.CAH.get( TextKey.GENERAL$WRITE_EMAIL, value ) );
			}
		}
		
		return this;
	}
	
}
