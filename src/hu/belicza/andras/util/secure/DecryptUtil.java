/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.secure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * RSA decryption utility.
 * 
 * @author Andras Belicza
 */
public class DecryptUtil {
	
	/** Key size in bits.                             */
	private static final int       KEY_SIZE;
	/** RSA public key used to decrypt encrypted data.*/
	private static final PublicKey PUBLIC_KEY;
	static {
		try {
			final KeyFactory kf = KeyFactory.getInstance( "RSA" );
			
			final Object[] sizeModExp;
			try ( final InputStream keyInput = DecryptUtil.class.getResourceAsStream( "pubkey.rsa" ) ) {
				sizeModExp = loadKey( keyInput );
			}
			
			KEY_SIZE   = (Integer) sizeModExp[ 0 ];
			PUBLIC_KEY = kf.generatePublic( new RSAPublicKeySpec( (BigInteger) sizeModExp[ 1 ], (BigInteger) sizeModExp[ 2 ] ) );
		} catch ( final Exception e ) {
			throw new RuntimeException( "Failed to initialize decryption utility!", e );
		}
	}
	
	/**
	 * Loads a key from the specified file.
	 * @param keyInput input stream to read the key from
	 * @return the loaded key, array of { (Integer) keySize, (BigInteger) modulus, (BigInteger) exponent }
	 * @throws Exception if some error occurs
	 */
	private static Object[] loadKey( final InputStream keyInput ) throws Exception {
		final Object[] sizeModExp = new Object[ 3 ];
		
		try ( final DataInputStream in = new DataInputStream( keyInput ) ) {
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
	
	/**
	 * Decrypts the specified encrypted data.
	 * 
	 * @param data data to be decrypted.
	 * @return the decrypted data or <code>null</code> if the encrypted data is invalid or corrupt
	 */
	public static byte[] decrypt( final byte[] data ) {
		// cipher.getBlockSize() always returns 0, calculate it ourself:
		final int blockSize = KEY_SIZE / 8;
		
		try ( final ByteArrayInputStream in = new ByteArrayInputStream( data ); final ByteArrayOutputStream out = new ByteArrayOutputStream( data.length ) ) {
			final Cipher cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding" );
			cipher.init( Cipher.DECRYPT_MODE, PUBLIC_KEY );
			
			final byte[] buffer = new byte[ blockSize ];
			
			int bytesRead;
			byte[] result;
			while ( ( bytesRead = in.read( buffer ) ) > 0 ) {
				result = cipher.doFinal( buffer, 0, bytesRead );
				if ( result != null )
					out.write( result );
			}
			
			return out.toByteArray();
		} catch ( final Exception e ) {
			return null;
		}
	}
	
}
