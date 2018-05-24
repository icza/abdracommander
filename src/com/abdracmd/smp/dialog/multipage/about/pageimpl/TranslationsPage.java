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

import hu.belicza.andras.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.Language;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.uicomponent.PersonCard;
import com.abdracmd.uicomponent.smarttable.SmartTable;
import com.abdracmd.util.AcUtils;
import com.abdracmd.util.GuiUtils;
import com.abdracmd.util.bean.person.Person;

/**
 * Translations page.
 * 
 * @author Andras Belicza
 */
public class TranslationsPage extends BasePage {
	
    /**
     * Creates a new TranslationsPage.
     */
    public TranslationsPage() {
    	super( TextKey.ABOUT$PAGE$TRANSLATIONS$NAME, XIcon.F_LANGUAGE );
    }
    
	@Override
	public JComponent createPage() {
		final Box box = Box.createVerticalBox();
		
		final JTable table = new SmartTable();
		
		final List< Language > languageList = new ArrayList<>();
		
		final Vector< Vector< Object > > dataVector = new Vector<>();
		for ( final String languageName : AcUtils.getLanguageNameList() ) {
			final Vector< Object > row = new Vector<>();
			
			row.add( getIcons().getLanguageIcon( languageName ) );
			row.add( languageName );
			
			final Language language;
			if ( languageName.equals( mainModel.getLanguage().name ) )
				language = mainModel.getLanguage();
			else {
				language = new Language( languageName );
				// Load only language meta data (texts not needed)
				if ( !language.load( false ) )
					continue;
			}
			
			languageList.add( language );
			
			row.add( language.getLocalName() );
			row.add( AcUtils.getTranslatorListString( language ) );
			row.add( language.getTarget() );
			row.add( language.getVersion() );
			
			dataVector.add( row );
		}
		
		final Vector< Object > columnIdnetifiers = new Vector<>();
		columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$TRANSLATIONS$COL_FLAG           ) );
		columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$TRANSLATIONS$COL_LANGUAGE       ) );
		columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$TRANSLATIONS$COL_LOCAL_NAME     ) );
		columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$TRANSLATIONS$COL_TRANSLATORS    ) );
		columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$TRANSLATIONS$COL_TARGET_VERSION ) );
		columnIdnetifiers.add( get( TextKey.ABOUT$PAGE$TRANSLATIONS$COL_VERSION        ) );
		( (DefaultTableModel) table.getModel() ).setDataVector( dataVector, columnIdnetifiers );
		
		GuiUtils.packTable( table );
		
		// Sort by language name 
		table.getRowSorter().setSortKeys( Arrays.asList( new RowSorter.SortKey( 1, SortOrder.ASCENDING ) ) );
		
		final Box tableBox = Box.createVerticalBox();
		tableBox.add( table.getTableHeader() );
		tableBox.add( table );
		box.add( tableBox );
		
		// Show translators' cards when selected
		
		final Box translatorsBox = Box.createVerticalBox();
		final TitledBorder translatorsTitledBorder = BorderFactory.createTitledBorder( Utils.EMPTY_STRING );
		translatorsBox.setBorder( translatorsTitledBorder );
		box.add( GuiUtils.wrapInPanel( translatorsBox ) );
		
		table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
			{ valueChanged( null ); } // Initialize
			@Override
			public void valueChanged( final ListSelectionEvent event ) {
				translatorsBox.removeAll();
				translatorsBox.revalidate();
				
				// VIEW index is returned as selection, we need MODEL index 
				int selectedRow = table.getSelectedRow();
				if ( selectedRow != -1 ) {
					selectedRow = table.convertRowIndexToModel( selectedRow );
					final Language language = languageList.get( selectedRow );
					translatorsTitledBorder.setTitle( get( TextKey.ABOUT$PAGE$TRANSLATIONS$TRANSLATORS_CARDS, '(' + language.name + ')' ) );
					for ( final Person translator : language.getTranslatorList() )
						translatorsBox.add( new PersonCard( translator ) );
				}
				else {
					translatorsBox.add( GuiUtils.changeFontToItalic( new JLabel( get( TextKey.ABOUT$PAGE$TRANSLATIONS$TRANSLATORS_CARDS_INFO ) ) ) );
					translatorsTitledBorder.setTitle( get( TextKey.ABOUT$PAGE$TRANSLATIONS$TRANSLATORS_CARDS, Utils.EMPTY_STRING ) );
				}
				
				translatorsBox.repaint();
			}
		} );
		
		return box;
	}
	
}
