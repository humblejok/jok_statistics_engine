package com.eim.utility.common.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Utility class that loads an array of <code>double</code> from the filesystem.<BR/>
 * It possesses a cache system. An already loaded file is cached and later calls won't have to access the filesystem.
 * @author sdj
 *
 */
public class DoublesArrayLoader {

	/**
	 * Logging utility
	 */
	private static Logger log = Logger.getLogger(DoublesArrayLoader.class);
	
	/**
	 * Cache
	 */
	private static HashMap<String,double []> doubleArrays = new HashMap<String, double []>();
	
	/**
	 * Loads, if not present in the cache, the given file and generates a <code>double</code> array.
	 * @param filename The file name (path are not allowed)
	 * @return The <code>double</code> array
	 */
	public static double [] getArray(String filename) {
		if(doubleArrays.containsKey( filename )) {
			return doubleArrays.get( filename );
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(DoublesArrayLoader.class.getResourceAsStream(filename)));
		ArrayList<Double> doubles = new ArrayList<Double>();
		try {
			String line = reader.readLine();
			while (line != null) {
				line = line.trim();
				Double dblValue = Double.NaN;
				try {
					dblValue = new Double(line);
				} catch (Exception e) {
					// Keep the NaN
				}
				doubles.add(dblValue);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			log.error("Could not load query:" + filename, e);
		}
		double [] array = new double[doubles.size()];
		for (int i = 0;i<doubles.size();i++) {
			array[i] = doubles.get(i);
		}
		doubleArrays.put( filename, array);
		return array;
	}
	
}
