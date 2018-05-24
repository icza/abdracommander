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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.abdracmd.Consts;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.multipage.BasePage;
import com.abdracmd.util.GuiUtils;

/**
 * License information about page.
 * 
 * @author Andras Belicza
 */
public class LicensePage extends BasePage {
	
    /**
     * Creates a new LicensePage.
     */
    public LicensePage() {
    	super( TextKey.ABOUT$PAGE$LICENSE$NAME, XIcon.F_DOCUMENT_TEXT );
    }
    
	@Override
	public JComponent createPage() {
		final JEditorPane htmlViewer = GuiUtils.createHtmlViewer();
		htmlViewer.setContentType( "text/html" );
		
		try ( final BufferedReader in = new BufferedReader( new InputStreamReader( Consts.class.getResourceAsStream( "LICENSE.html" ), Consts.ENCODING ) ) ) {
			
			final StringBuilder contentBuilder = new StringBuilder();
			String line;
			while ( ( line = in.readLine() ) != null )
				contentBuilder.append( line );
			
			htmlViewer.setText( contentBuilder.toString() );
		} catch ( final Exception e ) {
			warning( "Failed to load LICENSE!", e );
			final Box box = Box.createVerticalBox();
			box.add( GuiUtils.changeFontToItalic( new JLabel( get( TextKey.ABOUT$PAGE$LICENSE$FAILED_TO_LOAD ) ) ) );
			GuiUtils.alignBox( box, SwingConstants.CENTER );
			return box;
        }
		
		return htmlViewer;
	}
	
}
