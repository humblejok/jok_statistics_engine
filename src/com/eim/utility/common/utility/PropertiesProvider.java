package com.eim.utility.common.utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Utility class that load properties file from the file system. It possesses a cache mechanism.
 * @author sdj
 *
 */
public class PropertiesProvider {
	
	/**
	 * Logging utility
	 */
	private static Logger log = Logger.getLogger(PropertiesProvider.class);
	
	/**
	 * Cache
	 */
	private static HashMap<String,Properties> properties = new HashMap<String, Properties>();
	
	/**
	 * Loads the given properties file
	 * @param propertiesFile The file name (must not contain the path)
	 * @return The properties
	 */
	public static Properties getProperties(String propertiesFile) {
		if(properties.containsKey( propertiesFile )) {
			return properties.get( propertiesFile );
		}
		Properties loadedProperties = new Properties();
		try {
			loadedProperties.load(PropertiesProvider.class.getResourceAsStream(propertiesFile));
		} catch (IOException e) {
			log.error("Could not load properties file with name [" + propertiesFile + "]",e);
		}
		properties.put(propertiesFile, loadedProperties);
		return loadedProperties;
	}
}
