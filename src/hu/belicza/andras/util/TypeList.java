/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util;


import hu.belicza.andras.util.iface.HasType;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * A list which stores the dynamic type of the elements.
 * 
 * @param < T > type of the elements
 * 
 * @author Andras Belicza
 */
public class TypeList< T > extends ArrayList< T > implements HasType< T > {
	
	/** Default serial version ID. */
	private static final long serialVersionUID = 1L;
	
	/** Type of the elements. */
	public final Class< T > type;
	
	/**
	 * Creates a new TypeList with initial size of 10.
	 * @param type type of the elements
	 */
	public TypeList( final Class< T > type ) {
		this( type, 10 );
	}
	
	/**
	 * Creates a new TypeList.
	 * @param type        type of the elements
	 * @param initialSize initial list size
	 */
	public TypeList( final Class< T > type, final int initialSize ) {
		super( initialSize );
		
		this.type = type;
	}
	
	/**
	 * Creates a new TypeList initialized with the specified values.
	 * @param type     type of the elements
	 * @param elements elements to initialize the list with
	 */
	@SafeVarargs
    public TypeList( final Class< T > type, final T... elements ) {
		this( type, elements.length );
		
		for ( final T element : elements )
			add( element );
	}
	
	/**
	 * Creates a new TypeList initialized with the specified values.
	 * @param type     type of the elements
	 * @param elements elements to initialize the list with
	 */
    public TypeList( final Class< T > type, final List< T > elements ) {
		this( type, elements.size() );
		
		addAll( elements );
	}
	
	@Override
	public Class< T > getType() {
		return type;
	}
	
	/**
	 * Converts the list to a string separating the elements with the specified separator character.<br>
	 * Elements will be converted to string using the {@link Utils#simpleToString(Object)} method.
	 * 
	 * @param separator separator character to be used to separate the elements in the string
	 * @return a string where elements are separated with the specified separator character
	 */
	public String convertToString( final char separator ) {
		final StringBuilder sb = new StringBuilder();
		
		boolean first = true;
		for ( final T element : this ) {
			if ( first )
				first = false;
			else
				sb.append( separator ).append( ' ' );
			sb.append( Utils.simpleToString( element ) );
		}
		
		return sb.toString();
	}
	
	/**
	 * Parses a type list from its string representation (created by {@link #convertToString(char)} for example).<br>
	 * 
	 * <p>Elements will be parsed using the {@link Utils#simpleValueOf(Class, String)} method.
	 * Tokens of the source string will be trimmed. Empty tokens will be discarded.</p> 
	 * 
	 * @param type      type of the elements
	 * @param source    source string to parse the list from
	 * @param separator separator character that was used to separate the elements in the string
	 * @return the parsed type list
	 */
	public static < T > TypeList< T > parseFromString( final Class< T > type, final String source, final char separator ) {
		final StringTokenizer tokenizer = new StringTokenizer( source, Character.toString( separator ) );
		
		final TypeList< T > list = new TypeList< T >( type );
		
		while ( tokenizer.hasMoreTokens() ) {
			final String stringElement = tokenizer.nextToken().trim();
			
			if ( stringElement.isEmpty() )
				continue; // Discard empty elements
			
			list.add( Utils.simpleValueOf( type, stringElement ) );
		}
		
		return list;
	}
	
}
