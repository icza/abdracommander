/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.objectregistry;

import hu.belicza.andras.util.iface.Provider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A thread-safe object registry.
 * 
 * <p>Methods that return the set of keys or values define the iteration ordering, which is the object registration order.</p> 
 * 
 * @param <K> key type of the registered objects 
 * @param <V> value type of the registered objects
 *  
 * @author Andras Belicza
 */
public class ObjectRegistry< K, V > {
	
	/** Map to hold the registered objects. Use of {@link LinkedHashMap} ensures the iteration ordering. */
	private final Map< K, V > keyValueMap = new LinkedHashMap<>();
	
	/** Optional key provider. */
	private Provider< K, V > keyProvider;
	
	/**
	 * Creates a new ObjectRegistry.
	 */
	public ObjectRegistry() {
	}
	
	/**
	 * Creates a new ObjectRegistry.
	 * @param keyProvider key provider
	 */
	public ObjectRegistry( final Provider< K, V > keyProvider ) {
		this.keyProvider = keyProvider;
	}
	
	/**
	 * Factory method to create an object registry where the keys are class name strings of values.
	 * The object registry is initialized with a {@link ClassNameProvider}.
	 * @return a new object registry where the keys are the class name strings of values.
	 */
	public static < V > ObjectRegistry< String, V > newClassNameAsKeyObjectRegistry() {
		return new ObjectRegistry< String, V >( new ClassNameProvider< V >() );
	}
	
	/**
	 * Registers a new object.
	 * @param key   key of the new object to be registered
	 * @param value value of the new object to be registered
	 */
	public void register( final K key, final V value ) {
		synchronized ( keyValueMap ) {
			keyValueMap.put( key, value );
		}
	}
	
	/**
	 * Registers a new object.<br>
	 * The {@link Provider} will be used to determine the key.
	 * @param value value of the new object to be registered
	 * @throws NullPointerException if no key provider is set
	 */
	public void register( final V value ) {
		synchronized ( keyValueMap ) {
			register( keyProvider.provide( value ), value );
		}
	}
	
	/**
	 * Deregisters an object.
	 * @param key key of the object to be deregistered
	 */
	public void deregisterByKey( final K key ) {
		synchronized ( keyValueMap ) {
			keyValueMap.remove( key );
		}
	}
	
	/**
	 * Deregisters an object.
	 * @param value value of the object to be deregistered
	 */
	public void deregister( final V value ) {
		synchronized ( keyValueMap ) {
			deregisterByKey( keyProvider.provide( value) );
		}
	}
	
	/**
	 * Returns the registered object for the specified key.
	 * @param key key of the object to be returned
	 * @return the registered object for the specified key
	 */
	public V get( final K key ) {
		synchronized ( keyValueMap ) {
			return keyValueMap.get( key );
		}
	}
	
	/**
	 * Returns the list of the registered keys.
	 * The returned list contains keys in the registration order.
	 * @return the list of the registered keys
	 */
	public List< K > keyList() {
		synchronized ( keyValueMap ) {
			return new ArrayList<>( keyValueMap.keySet() );
		}
	}
	
	/**
	 * Returns the list of the registered objects.
	 * The returned list contains values in the registration order.
	 * @return the list of the registered objects
	 */
	public List< V > valueList() {
		synchronized ( keyValueMap ) {
			return new ArrayList<>( keyValueMap.values() );
		}
	}
	
	/**
	 * Returns the key provider.
	 * @return the key provider
	 */
	public Provider< K, V > getKeyProvider() {
		return keyProvider;
	}
	
	/**
	 * Sets the key provider
	 * @param keyProvider key provider to be set
	 */
	public void setKeyProvider( final Provider< K, V > keyProvider ) {
		this.keyProvider = keyProvider;
	}
	
}
