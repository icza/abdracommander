/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.mainframe.folderstabbed.folder.tableimpl;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.uicomponent.MatteBorder;

import java.awt.Component;
import java.awt.Graphics;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.abdracmd.service.language.Language;
import com.abdracmd.service.settings.SettingKey;
import com.abdracmd.service.settings.SettingKeys;
import com.abdracmd.service.settings.SettingSetChangeListener;
import com.abdracmd.smp.main.MainShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.FolderPresenter;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntity;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.ColorEntityFlag;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.IFolderPresenterTheme;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.SizeFormat;

/**
 * A {@link TableCellRenderer} responsible for rendering the table body cells of the folder presenter table.
 * 
 * @author Andras Belicza
 */
class FolderPresenterTableCellRenderer extends DefaultTableCellRenderer implements SettingSetChangeListener {
	
    /** Default serial version ID. */
	private static final long serialVersionUID = 1L;
	
	/** Reference to the folder presenter. */
	private final FolderPresenter folderPresenter;
	
	/** Theme. */
	private IFolderPresenterTheme theme;
	
	/** Size column view index.       */
	private int        sizeViewColIdx;
	/** Size column format.           */
	private SizeFormat sizeFormat;
	/** Size column fraction digits.  */
	private int        fractionDigits;
	
	/** Tells if the table is active.
	 * Not necessarily the "has focus" state (we might be the active even if <code>table.hasFocus()</code> returns false). */
	private boolean    active;
	
	/** Reference to the language. */
	private final Language language = MainShell.INSTANCE.getLanguage();
	
	/** Border renderer for cells. */
	private final MatteBorder border = new MatteBorder() {
		private static final long serialVersionUID = 1L;
		/**
	     * Overridden for performance reason, only calls super if a border color is set.
	     */
		@Override
	    public void paintBorder( final Component c, final Graphics g, final int x, final int y, final int width, final int height ) {
			if ( color != null )
				super.paintBorder( c, g, x, y, width, height );
	    }
	};
	
	/**
	 * Creates a new FolderPresenterTableCellRenderer.
     * @param folderPresenter reference to the folder presenter
	 */
	public FolderPresenterTableCellRenderer( final FolderPresenter folderPresenter ) {
		this.folderPresenter = folderPresenter;
		
		setBorder( border );
		
    	// Setting listening:
		// (Theme is listened by the owner table which notifies us.) 
		final Set< SettingKey< ? > > listenedSettingKeySet = Utils.< SettingKey< ? > >asNewSet(
				SettingKeys.FILE_LISTING$SIZE_COLUMN_FORMAT, SettingKeys.FILE_LISTING$SIZE_COLUMN_FRACTION_DIGITS );
		AcUtils.CAH.addChangeListener( listenedSettingKeySet, this );
		// Init setting dependent code:
		valuesChanged( listenedSettingKeySet );
	}
	
	@Override
	public void valuesChanged( final Set< SettingKey< ? > > settingKeySet ) {
    	this.sizeFormat     = AcUtils.CAH.get( SettingKeys.FILE_LISTING$SIZE_COLUMN_FORMAT );
    	this.fractionDigits = AcUtils.CAH.get( SettingKeys.FILE_LISTING$SIZE_COLUMN_FRACTION_DIGITS );
    	
    	if ( folderPresenter.getTable() != null )
    			folderPresenter.getTable().repaint();
	}
	
	@Override
	public void updateUI() {
		super.updateUI();
		
		if ( theme != null )
			refreshTheme();
	}
	
    /**
     * Refreshes the theme.
     */
    public void refreshTheme() {
		theme = AcUtils.getFolderPresenterTheme();
		
		setFont( theme.getFont() );
		
		border.setInsets( theme.getBorderWidth(), 0, theme.getBorderWidth(), 0 );
    }
    
    /**
     * Sets whether the table is active.
     * @param active true if the table is active
     */
    public void setActive( final boolean active ) {
    	this.active = active;
    }
    
    /**
     * Refreshes the view idx of the SIZE column.
     */
    public void refreshViewColIdx() {
    	this.sizeViewColIdx = folderPresenter.getSizeColumnViewIndex();
    }
    
	/** Flags set instance reused when rendering cells. */
    private final Set< ColorEntityFlag > flags = EnumSet.noneOf( ColorEntityFlag.class );
    
	@Override
	public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, int column ) {
		if ( value instanceof Icon ) {
			setIcon( (Icon) value );
			setText( null );
		}
		else {
			setIcon( null );
			
			if ( value instanceof Number ) {
				// TODO [OPTIMIZE]
				if ( column == sizeViewColIdx )
					setText( AcUtils.getFormattedSize( sizeFormat, ( (Number) value ).longValue(), fractionDigits ) + "    " );
				else
					setText( language.formatNumber( ( (Number) value ).longValue() ) + "    " ); // Leave space at the end since it is right aligned
				setHorizontalAlignment( RIGHT );
			} else if ( value instanceof Date ) {
				setText( language.formatDateTime( (Date) value ) );
				setHorizontalAlignment( LEFT );
			}
			else {
				setText( value == null ? null : value.toString() );
				setHorizontalAlignment( LEFT );
			}
		}
		
		flags.clear();
		if ( isSelected )
			flags.add( ColorEntityFlag.FOCUSED );
		if ( ( row & 0x01 ) != 0 )
			flags.add( ColorEntityFlag.ALTERNATE );
		// We are called with row<0 when only cell height is required but no real rendering...
		if ( row >= 0 && Boolean.TRUE == table.getModel().getValueAt( table.convertRowIndexToModel( row ), folderPresenter.getSelectedColumnModelIndex() ) )
			flags.add( ColorEntityFlag.SELECTED );
		if ( active )
			flags.add( ColorEntityFlag.ACTIVE );
		
		setForeground  ( theme.getColor( ColorEntity.TEXT      , flags ) );
		setBackground  ( theme.getColor( ColorEntity.BACKGROUND, flags ) );
		border.setColor( theme.getColor( ColorEntity.BORDER    , flags ) );
		
		return this;
	}
	
}
