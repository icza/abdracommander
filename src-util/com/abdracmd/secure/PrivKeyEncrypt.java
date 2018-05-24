/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.secure;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;

import javax.crypto.Cipher;

/**
 * Private key encryption utility.
 * 
 * @author Andras Belicza
 */
public class PrivKeyEncrypt {
	
	/** Input path to encrypt.                         */
	private static final Path INPUT_PATH       =  Paths.get( "w:/toencrypt.dat" );
	/** Output path to write the encrypted results to. */
	private static final Path OUTPUT_PATH      =  Paths.get( "w:/encrypted.dat" );
	/** Private key path to load from.                 */
	private static final Path PRIVATE_KEY_PATH =  Paths.get( "w:/privkey.rsa" );
	
	/**
	 * Loads a private key from a file and encrypts an input file to another file.
	 * @param args not used
	 * @throws Exception if some error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		final KeyFactory kf = KeyFactory.getInstance( "RSA" );
		
		final Object[] sizeModExp = loadKey( PRIVATE_KEY_PATH );
		final int keySize = (Integer) sizeModExp[ 0 ];
		final PrivateKey privKey = kf.generatePrivate( new RSAPrivateKeySpec( (BigInteger) sizeModExp[ 1 ], (BigInteger) sizeModExp[ 2 ] ) );
		
		// cipher.getBlockSize() always returns 0, calculate it ourself:
		final int blockSize = keySize / 8 - 11; // 11 bytes header in blocks
		
		final Cipher cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding" );
		cipher.init( Cipher.ENCRYPT_MODE, privKey );
		
		System.out.println( "Encrypting..." );
		final long start = System.nanoTime();
		
		try ( final InputStream in = Files.newInputStream( INPUT_PATH ); final OutputStream out = Files.newOutputStream( OUTPUT_PATH ) ) {
			final byte[] buffer = new byte[ blockSize ];
			
			int bytesRead;
			byte[] result;
			while ( ( bytesRead = in.read( buffer ) ) > 0 ) {
				result = cipher.doFinal( buffer, 0, bytesRead );
				if ( result != null )
					out.write( result );
			}
		}
		
		final long end = System.nanoTime();
		System.out.println( "Done. (" + ( ( end - start ) / 1_000_000 ) + " ms)" );
	}
	
	/**
	 * Loads a key from the specified file.
	 * @param path path to load the key from
	 * @return the loaded key, array of { (Integer) keySize, (BigInteger) modulus, (BigInteger) exponent }
	 * @throws Exception if some error occurs
	 */
	public static Object[] loadKey( final Path path ) throws Exception {
		final Object[] sizeModExp = new Object[ 3 ];
		
		try ( final DataInputStream in = new DataInputStream( Files.newInputStream( path ) ) ) {
			// Key size
			sizeModExp[ 0 ] = in.readInt();
			
			byte[] buff = new byte[ in.readInt() ];
			in.read( buff );
			sizeModExp[ 1 ] = new BigInteger( buff );
			
			buff = new byte[ in.readInt() ];
			in.read( buff );
			sizeModExp[ 2 ] = new BigInteger( buff );
		}
		
		return sizeModExp;
	}
	
}
