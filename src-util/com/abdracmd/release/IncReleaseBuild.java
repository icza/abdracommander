/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.release;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.abdracmd.Consts;

/**
 * Writes the release build to a file.
 * 
 * @author Andras Belicza
 */
public class IncReleaseBuild {
	
	/** Main build path.    */
	private static final Path BUILD_PATH         = Paths.get( "src/com/abdracmd/build" );
	/** Build history path. */
	private static final Path BUILD_HISTORY_PATH = Paths.get( "docs/auto-generated/build-history.txt" );
	
	/** Alternate build paths to copy the main build path to. */
	private static final Path[] ALTERNATE_BUILD_PATHS = new Path[] { Paths.get( "bin/com/abdracmd/build" ) };
	
	/**
	 * Program entry point.
	 * @param arguments not used
	 * @throws IOException when it fails to write release build
	 */
	public static void main( final String[] arguments ) throws IOException {
		// Read current build
		int versionBuild;
		try ( final BufferedReader in = new BufferedReader( new InputStreamReader( Consts.class.getResourceAsStream( "build" ), Consts.ENCODING ) ) ) {
			versionBuild = Integer.parseInt( in.readLine() );
        }
		
		// Increment build
		versionBuild++;
		
		// Release date
		final SimpleDateFormat sdf = new SimpleDateFormat( Consts.APP_RELEASE_DATE_PATTERN );
		final String releaseDate = sdf.format( new Date() );
		
		System.out.println( "Version build: " + versionBuild );
		System.out.println( "Release date : " + releaseDate );
		
		final Charset charset = Charset.forName( Consts.ENCODING );
		try ( final PrintWriter out = new PrintWriter( Files.newBufferedWriter( BUILD_PATH, charset ) ) ) {
			out.println( versionBuild );
			out.println( releaseDate );
		}
		
		for ( final Path alternateBuildPath : ALTERNATE_BUILD_PATHS )
			Files.copy( BUILD_PATH, alternateBuildPath, StandardCopyOption.REPLACE_EXISTING );
		
		// Update build history
		final List< String > buildLineList = Files.readAllLines( BUILD_PATH, charset );
		
		try ( final PrintWriter out = new PrintWriter( Files.newBufferedWriter( BUILD_HISTORY_PATH, charset, StandardOpenOption.APPEND ) ) ) {
			out.println();
			out.println( "--" );
			for ( final String line : buildLineList )
				out.println( line );
        }
		
	}
	
}
