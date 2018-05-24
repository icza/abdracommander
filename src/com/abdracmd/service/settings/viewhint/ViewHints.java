/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.service.settings.viewhint;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.iface.Provider;
import hu.belicza.andras.util.iface.SimpleProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;

import com.abdracmd.service.language.TextKey;
import com.abdracmd.service.settings.FixedValuesSettingKey;

/**
 * Properties intended for the Settings view dialog to customize the setting's view/edit components.
 * 
 * @author Andras Belicza
 */
public class ViewHints {
	
	/** Specifies a text to be displayed after the setting view.
	 * Text is specified by its {@link TextKey}.                                                                  */
	public static final ViewHint< TextKey >                       SUBSEQUENT_TEXT             = new ViewHint<>( TextKey.class );
	
	/** Specifies a tool tip text for the setting component.
	 * Tool tip text is specified by its {@link TextKey}.                                                         */
	public static final ViewHint< TextKey >                       TOOL_TIP_TEXT               = new ViewHint<>( TextKey.class );
	
	/** Specifies a tool tip text for the setting component.
	 * Tool tip text is specified by a {@link SimpleProvider}.                                                    */
	public static final ViewHint< SimpleProvider< String > >      TOOL_TIP_TEXT_PROVIDER      = new ViewHint<>( Utils.< Class< SimpleProvider< String > > >cast( SimpleProvider.class ) );
	
	/** Specifies icons for the (possible) setting values. Used in case of {@link FixedValuesSettingKey}.         */
	public static final ViewHint< Provider< Icon, Object > >      VALUE_ICON_PROVIDER         = new ViewHint<>( Utils.< Class< Provider< Icon, Object > > >cast( Provider.class ) );
	
	/** Specifies display names for the (possible) setting values. Used in case of {@link FixedValuesSettingKey}. */
	public static final ViewHint< Provider< String, Object > >    VALUE_DISPLAY_NAME_PROVIDER = new ViewHint<>( Utils.< Class< Provider< String, Object > > >cast( Provider.class ) );
	
	/** Specifies the component to be used as the setting component.
	 * The component is specified by its class (e.g. <code>JTextField.class</code>).                                                                   */
	public static final ViewHint< Class< ? extends JComponent > > COMPONENT_TYPE              = Utils.< ViewHint< Class< ? extends JComponent > > >cast( new ViewHint<>( JComponent.class.getClass() ) );
	
	/** Map of hint value lists.
	 * Key is the view hint, value is the value list associated with that hint. */
	private final Map< ViewHint< ? >, List< ? > > hintValueListMap =  new HashMap<>();
	
	/**
	 * Adds a new view hint.
	 * @param hint  hint to be added
	 * @param value hint value to be added
	 * @return this so this call can be chained
	 */
	public < T > ViewHints add( final ViewHint< T > hint, final T value ) {
		List< T > valueList = Utils.cast( hintValueListMap.get( hint ) );
		
		if ( valueList == null )
			hintValueListMap.put( hint, valueList = new ArrayList<>( 1 ) );
		
		valueList.add( value );
		
		return this;
	}
	
	/**
	 * Returns the value list associated with the specified hint.
	 * @param hint hint whose value list to be returned
	 * @return the value list associated with the specified hint; if no value has been added to the specified hint, an empty list is returned
	 */
	public < T > List< T > getValueList( final ViewHint< T > hint ) {
		final List< ? > valueList = hintValueListMap.get( hint );
		return Utils.cast( valueList == null ? Utils.EMPTY_LIST : valueList );
	}
	
	/**
	 * Returns the single value associated with the specified hint.
	 * @param hint hint whose single value to be returned
	 * @return the single value associated with the specified hint
	 * @throws IllegalArgumentException if the specified hint does not have exactly 1 associated value (e.g. no associated value or more than 1) 
	 */
	public < T > T getSingleValue( final ViewHint< T > hint ) {
		final List< ? > valueList = hintValueListMap.get( hint );
		
		if ( valueList == null || valueList.size() > 1 )
			throw new IllegalArgumentException();
		
		return Utils.cast( valueList.get( 0 ) );
	}
	
}
