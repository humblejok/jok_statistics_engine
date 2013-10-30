package com.eim.utility.contribution.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.eim.utility.common.utility.PropertiesProvider;

/**
 * Utility class that handles the reads of queries from the file system.<BR/>
 * It possesses a cache system that avoid re-reading the already loaded queries from the file system.
 * @author sdj
 * @author abaguet
 *
 */
public class QueriesProvider {

	/**
	 * The logging utility 
	 */
	private static Logger log = Logger.getLogger(QueriesProvider.class);
	
	/**
	 * The queries cache
	 */
	private static HashMap<String,String> queries = new HashMap<String, String>();
	
	/**
	 * Loads the content of the given <code>filename</code> as a SQL query
	 * @param filename The filename (no path allowed)
	 * @return The SQL query as <code>String</code>
	 */
	public static String getQuery(String filename) {
		if(queries.containsKey( filename )) {
			return queries.get( filename );
		}
		StringBuilder content = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(QueriesProvider.class.getResourceAsStream(filename)));
		
		try {
			String line = reader.readLine();
			while (line != null) {
				content.append(line).append('\n');
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			log.error("Could not load query:" + filename, e);
		}

		return content.toString();
	}

	/**
	 * Get a SQL <code>Connection</code> according to the databases.properies configuration file.
	 * @return The SQL <code>Connection</code>
	 */
	public static Connection getConnection(String key) {
		Properties properties = PropertiesProvider.getProperties("webfolio.databases.properties");
		String driver = properties.getProperty(key + ".driver");
		String url = properties.getProperty(key + ".url");
		String dbUsr = properties.getProperty(key + ".user");
		String dbPwd = properties.getProperty(key + ".password");
		try {
			Class.forName(driver);
			Connection connection = DriverManager.getConnection(url, dbUsr, dbPwd);
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
