package com.eim.utility.common.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.EIMCompanies;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.contribution.model.Contribution;
import com.eim.utility.contribution.runners.AbstractContributionsRunner;
import com.eim.utility.contribution.runners.CHPortfoliosContributionsRunner;
import com.eim.utility.contribution.runners.PTPortfoliosContributionsRunner;
import com.eim.utility.contribution.runners.SimpleContributionsRunner;
import com.eim.utility.contribution.runners.UKPortfoliosContributionsRunner;
import com.eim.utility.statistics.database.QueriesProvider;
import com.eim.utility.statistics.model.BenchmarkRelatedStatistics;
import com.eim.utility.statistics.model.ComputedStatistics;
import com.eim.utility.statistics.model.IStatistics;
import com.eim.utility.statistics.runners.UserDefinedWorkingSetStatisticsRunner;

/**
 * Hibernate utility class that initializes the Hibernate engine with all configuration information (such as database parameters, classes to persist, ...).<BR/>
 * It also provides a set of methods that allows the manipulation of data (statistics) on the database using Hibernate.
 * @author sdj
 *
 */
public class HibernateUtil {

	/**
	 * Logging utility
	 */
	private static Logger logger = Logger.getLogger(HibernateUtil.class);
	
	/**
	 * Session utility
	 */
	private static SessionFactory sessionFactory;
	
	/**
	 * Static initialization
	 */
	static {
		Properties properties = PropertiesProvider.getProperties("databases.properties");
		try {
			sessionFactory = new Configuration()
				.addAnnotatedClass(Contribution.class)
				.addAnnotatedClass(BenchmarkRelatedStatistics.class)
				.addAnnotatedClass(ComputedStatistics.class)
				.setProperty("hibernate.connection.driver_class", properties.getProperty("target.driver"))
				.setProperty("hibernate.connection.password", properties.getProperty("target.password"))
				.setProperty("hibernate.connection.url", properties.getProperty("target.url"))
				.setProperty("hibernate.connection.username", properties.getProperty("target.user"))
				.setProperty("hibernate.dialect", properties.getProperty("target.hibernate.dialect"))
				.setProperty("hibernate.hbm2ddl.auto", "update")
				.setProperty("hibernate.validator.apply_to_ddl", "false")
				.setProperty("hibernate.validator.autoregister_listeners", "false")
				.buildSessionFactory();
		} catch (Throwable ex) {
			logger.error("Unable to prepare Hibernate session!",ex);
		}

	}
	
	/**
	 * Get the session factory created during the static initialization
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * Returns all the non-benchmark statistics of the given entity
	 * @param entityId The entity id
	 * @return The list of statistics
	 */
	@SuppressWarnings("unchecked")
	public static Collection<ComputedStatistics> getComputedStatistics(String entityId) {
		ArrayList<ComputedStatistics> results = new ArrayList<ComputedStatistics>();
		
		logger.info("getComputedStatistics - Looking for statistics of " + entityId);

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT cs FROM ComputedStatistics cs WHERE cs.targetEntityId  = ? ORDER BY cs.applicationDate DESC");
		
		query.setString(0, entityId);
		
		results.addAll(query.list());

		transaction.commit();
		session.close();
		logger.info("getComputedStatistics - Finished looking for statistics of " + entityId);
		
		return results;
	}
	
	/**
	 * Delete all statistics (benchmark and non-benchmark related) of the given entity
	 * @param entityId The entity id
	 * @return The result string (JSON)
	 */
	public static String dropAllStatistics(String entityId) {
		logger.info("dropAllStatistics - Deleting statistics of " + entityId);

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("DELETE FROM ComputedStatistics cs WHERE cs.targetEntityId  = ?");
		
		query.setString(0, entityId);
		
		int executeUpdate = query.executeUpdate();

		logger.info("Delete " + executeUpdate + " standard statistics for " + entityId);

		query = session.createQuery("DELETE FROM BenchmarkRelatedStatistics brs WHERE brs.relatedEntityId  = ?");
		
		query.setString(0, entityId);
		
		int executeUpdateBench = query.executeUpdate();
		
		logger.info("Delete " + executeUpdateBench + " benchmark related statistics for " + entityId);
		
		transaction.commit();
		session.close();
		logger.info("dropAllStatistics - Deleting statistics of " + entityId);
		
		return "{ result: \"" + (executeUpdateBench+executeUpdate>=0) + "\"}";
	}
	
	/**
	 * Computes all statistics (benchmark and non-benchmark related) of the given entity
	 * @param entityId The entity id
	 * @param entityType The entity type
	 * @return The result string (JSON)
	 */
	public static String computeAllStatistics(String entityId, String entityType) {
		logger.info("computeAllStatistics - Computing statistics of " + entityId);
		ArrayList<String> entityIds = new ArrayList<String>();
		entityIds.add(entityId);
		EntityType entityTypeEnum = null;
		if (entityType.equals("indices")) {
			entityTypeEnum = EntityType.INDEX;
		} else if (entityType.equals("portfolios")) {
			entityTypeEnum = EntityType.PORTFOLIO;
		} else if (entityType.equals("products")) {
			entityTypeEnum = EntityType.PRODUCT;
		} else {
			String query = QueriesProvider.getQuery("FUND_IS_TOP_LEVEL.SQL");
			Connection connection = QueriesProvider.getConnection("source");
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement(query);
				statement.setString(1, entityId);
				ResultSet executeQuery = statement.executeQuery();
				boolean topLevel = false;
				while (executeQuery.next()) {
					String topLevelFlag = executeQuery.getString(1);
					topLevel = topLevelFlag.equals("Y"); 
				}
				statement.close();
				connection.close();
				entityTypeEnum = EntityType.CHILD_FUND;
				if (topLevel) {
					entityTypeEnum = EntityType.TOP_LEVEL_FUND;
				}
				
			} catch (SQLException e) {
				return "{ result: \"" + (false) + "\"}";
			}
		}
		UserDefinedWorkingSetStatisticsRunner runner = new UserDefinedWorkingSetStatisticsRunner(entityTypeEnum, ComputationFrequency.MONTHLY , entityIds);
		runner.compute();
		logger.info("computeAllStatistics - Computing statistics of " + entityId);
		
		return "{ result: \"" + (true) + "\"}";
	}
	
	/**
	 * Computes all statistics (benchmark and non-benchmark related) of the given entity
	 * @param entityId The entity id
	 * @param entityType The entity type
	 * @return The result string (JSON)
	 */
	public static String computeAllContributions() {
		logger.info("computeAllContributions - Computing all contributions");
		
		// CH Portfolios
		logger.info("computeAllContributions - Computing CH contributions");
		CHPortfoliosContributionsRunner chRunner = new CHPortfoliosContributionsRunner(null, null);
		chRunner.compute();
		logger.info("computeAllContributions - Finished computing CH contributions");
		
		// UK Portfolios
		logger.info("computeAllContributions - Computing UK contributions");
		UKPortfoliosContributionsRunner ukRunner = new UKPortfoliosContributionsRunner(null, null);
		ukRunner.compute();
		logger.info("computeAllContributions - Finished computing UK contributions");
		
		logger.info("computeAllContributions - Finished computing all contributions");
		
		return "{ result: \"" + (true) + "\"}";
	}
	
	/**
	 * Computes all statistics (benchmark and non-benchmark related) of the given entity
	 * @param entityId The entity id
	 * @param entityType The entity type
	 * @return The result string (JSON)
	 */
	public static String computeLast12MonthsContributions() {
		logger.info("computeAllContributions - Computing last 12 months contributions");
		
		Calendar cal = Calendar.getInstance();
		// 12 months ago
		cal.add(Calendar.MONTH, -12);
		Date startDate = new Date(cal.getTime().getTime());
		
		// CH Portfolios
		logger.info("computeAllContributions - Computing last 12 months CH contributions");
		CHPortfoliosContributionsRunner chRunner = new CHPortfoliosContributionsRunner(startDate, null);
		chRunner.compute();
		logger.info("computeAllContributions - Finished computing last 12 months CH contributions");
		
		// UK Portfolios
		logger.info("computeAllContributions - Computing last 12 months UK contributions");
		UKPortfoliosContributionsRunner ukRunner = new UKPortfoliosContributionsRunner(startDate, null);
		ukRunner.compute();
		logger.info("computeAllContributions - Finished computing last 12 months UK contributions");
		
		// PT Portfolios
		logger.info("computeAllContributions - Computing last 12 months  PT contributions");
		PTPortfoliosContributionsRunner ptRunner = new PTPortfoliosContributionsRunner(startDate, null);
		ptRunner.compute();
		logger.info("computeAllContributions - Finished computing last 12 months PT contributions");
		
		logger.info("computeAllContributions - Finished computing last 12 months all contributions");
		
		return "{ result: \"" + (true) + "\"}";
	}
	
	/**
	 * Computes all statistics (benchmark and non-benchmark related) of the given entity
	 * @param entityId The entity id
	 * @param entityType The entity type
	 * @return The result string (JSON)
	 */
	public static String computeTuesdayPortfoliosContributions() {
		logger.info("computeAllContributions - Computing all Tuesday portfolios contributions");
		
		AbstractContributionsRunner runner = null;
		
		ArrayList<Long> entityIDs = new ArrayList<Long>();
		
		// --- CH Portfolios
		
		logger.info("computeAllContributions - Computing CH Tuesday portfolios contributions");
		
		// Read ch_tuesday_portfolios.txt
		BufferedReader br;
		String fileName = "ch_tuesday_portfolios.txt";
		String filePath = "./././././resources/";
		String line;
		try {
			br = new BufferedReader(new FileReader(filePath + fileName));
			while ((line = br.readLine()) != null) {
				if (line == "") {
					continue;
				}
				// Split line if there is any comment
				line = line.split("//")[0];
				// Add portfolio ID to list of entities to be treated
				entityIDs.add(Long.valueOf(line));
			}
			br.close();
		}
		catch (FileNotFoundException e1) {
			logger.error("Can't open file " + fileName + ": " + e1.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		catch (IOException e2) {
			logger.error("Can't read file " + fileName + ": " + e2.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		catch (Exception e) {
			logger.error("Exception occurred when reading " + fileName + ": " + e.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		
		Calendar cal = Calendar.getInstance();
		
		// Current date
		Date endDate = new Date(cal.getTime().getTime());
//		System.out.println("endDate = " + endDate.toString());
		
		// Last month
		cal.add(Calendar.MONTH, -1);
		Date startDate = new Date(cal.getTime().getTime());
//		System.out.println("startDate = " + startDate.toString());
		
		runner = new SimpleContributionsRunner(EntityType.PORTFOLIO, EIMCompanies.EIM_SWITZERLAND, entityIDs, startDate, endDate);
		runner.compute();
		
		logger.info("computeAllContributions - Finished computing CH Tuesday portfolios contributions");
		
		// --- UK Portfolios
		
		logger.info("computeAllContributions - Computing UK Tuesday portfolios contributions");
		
		entityIDs.clear();
		fileName = "uk_tuesday_portfolios.txt";
		
		try {
			br = new BufferedReader(new FileReader(filePath + fileName));
			while ((line = br.readLine()) != null) {
				if (line == "") {
					continue;
				}
				// Split line if there is any comment
				line = line.split("//")[0];
				// Add portfolio ID to list of entities to be treated
				entityIDs.add(Long.valueOf(line));
			}
			br.close();
		}
		catch (FileNotFoundException e1) {
			logger.error("Can't open file " + fileName + ": " + e1.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		catch (IOException e2) {
			logger.error("Can't read file " + fileName + ": " + e2.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		catch (Exception e) {
			logger.error("Exception occurred when reading " + fileName + ": " + e.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		
		runner = new SimpleContributionsRunner(EntityType.PORTFOLIO, EIMCompanies.EIM_UK, entityIDs, startDate, endDate);
		runner.compute();
		
		logger.info("computeAllContributions - Finished computing UK Tuesday portfolios contributions");
		
		logger.info("computeAllContributions - Finished computing all Tuesday portfolios contributions");
		
		return "{ result: \"" + (true) + "\"}";
	}
	
	/**
	 * Computes all statistics (benchmark and non-benchmark related) of the given entity
	 * @param entityId The entity id
	 * @param entityType The entity type
	 * @return The result string (JSON)
	 */
	public static String computeWednesdayPortfoliosContributions() {
		logger.info("computeAllContributions - Computing all Wednesday portfolios contributions");
		
		AbstractContributionsRunner runner = null;
		
		ArrayList<Long> entityIDs = new ArrayList<Long>();
		
		// --- CH Portfolios
		
		logger.info("computeAllContributions - Computing CH Wednesday portfolios contributions");
		
		// Read ch_tuesday_portfolios.txt
		BufferedReader br;
		String fileName = "ch_wednesday_portfolios.txt";
		String filePath = "./././././resources/";
		String line;
		try {
			br = new BufferedReader(new FileReader(filePath + fileName));
			while ((line = br.readLine()) != null) {
				if (line == "") {
					continue;
				}
				// Split line if there is any comment
				line = line.split("//")[0];
				// Add portfolio ID to list of entities to be treated
				entityIDs.add(Long.valueOf(line));
			}
			br.close();
		}
		catch (FileNotFoundException e1) {
			logger.error("Can't open file " + fileName + ": " + e1.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		catch (IOException e2) {
			logger.error("Can't read file " + fileName + ": " + e2.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		catch (Exception e) {
			logger.error("Exception occurred when reading " + fileName + ": " + e.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		
		Calendar cal = Calendar.getInstance();
		
		// Current date
		Date endDate = new Date(cal.getTime().getTime());
//		System.out.println("endDate = " + endDate.toString());
		
		// Last month
		cal.add(Calendar.MONTH, -1);
		Date startDate = new Date(cal.getTime().getTime());
//		System.out.println("startDate = " + startDate.toString());
		
		runner = new SimpleContributionsRunner(EntityType.PORTFOLIO, EIMCompanies.EIM_SWITZERLAND, entityIDs, startDate, endDate);
		runner.compute();
		
		logger.info("computeAllContributions - Finished computing CH Wednesday portfolios contributions");
		
		// --- UK Portfolios
		
		logger.info("computeAllContributions - Computing UK Wednesday portfolios contributions");
		
		entityIDs.clear();
		fileName = "uk_wednesday_portfolios.txt";
		
		try {
			br = new BufferedReader(new FileReader(filePath + fileName));
			while ((line = br.readLine()) != null) {
				if (line == "") {
					continue;
				}
				// Split line if there is any comment
				line = line.split("//")[0];
				// Add portfolio ID to list of entities to be treated
				entityIDs.add(Long.valueOf(line));
			}
			br.close();
		}
		catch (FileNotFoundException e1) {
			logger.error("Can't open file " + fileName + ": " + e1.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		catch (IOException e2) {
			logger.error("Can't read file " + fileName + ": " + e2.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		catch (Exception e) {
			logger.error("Exception occurred when reading " + fileName + ": " + e.getMessage());
			return "{ result: \"" + (false) + "\"}";
		}
		
		runner = new SimpleContributionsRunner(EntityType.PORTFOLIO, EIMCompanies.EIM_UK, entityIDs, startDate, endDate);
		runner.compute();
		
		logger.info("computeAllContributions - Finished computing UK Wednesday portfolios contributions");
		
		logger.info("computeAllContributions - Finished computing all Wednesday portfolios contributions");
		
		return "{ result: \"" + (true) + "\"}";
		
	}
	
	/**
	 * Returns all the benchmark statistics of the given entity
	 * @param entityId The entity id
	 * @return The list of statistics
	 */
	@SuppressWarnings("unchecked")
	public static Collection<BenchmarkRelatedStatistics> getBenchmarkRelatedStatistics(String entityId) {
		ArrayList<BenchmarkRelatedStatistics> results = new ArrayList<BenchmarkRelatedStatistics>();
		
		logger.info("getBenchamrkRelatedStatistics - Looking for statistics of " + entityId);

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT brs FROM BenchmarkRelatedStatistics brs WHERE brs.relatedEntityId  = ? ORDER BY brs.targetEntityId, brs.applicationDate DESC");
		
		query.setString(0, entityId);
	
		results.addAll(query.list());
		
		transaction.commit();
		session.close();
		logger.info("getBenchamrkRelatedStatistics - Finished looking for statistics of " + entityId);
		
		return results;
	}
	
	/**
	 * Store all the given statistics.<BR/>
	 * The method uses 2 lists the first one contains all the already existing statistics and the second one contains the newly computed statistics.<BR/>
	 * The method controls if a new statistics will replace an existing one. If so, the new statistics receives the former id (this will execute an update).
	 * 
	 * @param list The new statistics
	 * @param existing The existing statistics
	 * @param storedClass The class representing the statistics
	 */
	public synchronized static void storeStatistics(List<IStatistics> list, List<IStatistics> existing, Class<?> storedClass) {
		if (list.size()>0) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.setFlushMode(FlushMode.COMMIT);
			Transaction transaction = session.beginTransaction();
			int updated = 0;
			for (IStatistics s : list) {
				storedClass = s.getClass();
				if (existing.contains(s)) {
					IStatistics original = null;
					for (IStatistics e : existing) {
						if (e.equals(s)) {
							original = e;
							break;
						}
					}
					s.setId(original.getId());
					updated++;
				}
				session.saveOrUpdate(s);
			}
			session.flush();
			transaction.commit();
			session.close();
			logger.info("****************************************************************************************************");
			logger.info("Stored " + list.size() + " (updates " + updated + ") elements [" + storedClass.getSimpleName() + "]");
			logger.info("****************************************************************************************************");
		} else {
			logger.info("****************************************************************************************************");
			logger.info("Nothing to store or update [" + storedClass.getSimpleName() + "]");
			logger.info("****************************************************************************************************");
		}
	}

	/**
	 * Store all the given contribution.<BR/>
	 * The method uses 2 lists the first one contains all the already existing statistics and the second one contains the newly computed statistics.<BR/>
	 * The method controls if a new statistics will replace an existing one. If so, the new statistics receives the former id (this will execute an update).
	 * 
	 * @param list The new statistics
	 * @param existing The existing statistics
	 * @param storedClass The class representing the statistics
	 */
	public synchronized static void storeContributions(List<Contribution> list, List<Contribution> existing, Class<?> storedClass) {
//		logger.setLevel(Level.ERROR);
		logger.info("Treat " + list.size() + " elements [" + storedClass.getSimpleName() + "] before storing them in database ...");
		// If list is not empty
		if (list.size() > 0) {
			// Get hibernate session
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.setFlushMode(FlushMode.COMMIT);
			Transaction transaction = session.beginTransaction();
			// For each contribution
			int updated = 0;
			for (Contribution contribution : list) {
				// Get existing contribution
				for (Contribution existingContribution : existing) {
					// Compare porfolio ID, security ID, application date and status
//					if (existingContribution.getPortfolioID().equals(contribution.getPortfolioID()) &&
//							existingContribution.getSecurityID().equals(contribution.getSecurityID()) &&
//							existingContribution.getApplicationDate().getTime() == contribution.getApplicationDate().getTime() &&
//							existingContribution.getStatus().equals(contribution.getStatus())) {
					if (existingContribution.getEvaluationID().equals(contribution.getEvaluationID()) &&
							existingContribution.getSecurityID().equals(contribution.getSecurityID()) &&
							existingContribution.getCompany().equals(contribution.getCompany())) {
						contribution.setId(existingContribution.getId());
						updated++;
						break;
					}
				}
				try {
					session.saveOrUpdate(contribution);
				}
				catch (NonUniqueObjectException e) {
					logger.error("This contribution was found to not be unique: " + contribution.toString());
				}
				if (contribution.getValue().isNaN()) {
					logger.info("This contribution value is NaN: " + contribution.toString());
				}
			}
			session.flush();
			transaction.commit();
			session.close();
			logger.info("****************************************************************************************************");
			logger.info("Stored " + list.size() + " (updates " + updated + ") elements [" + storedClass.getSimpleName() + "]");
			logger.info("****************************************************************************************************");
		}
		// Else, list is empty
		else {
			logger.info("****************************************************************************************************");
			logger.info("Nothing to store or update [" + storedClass.getSimpleName() + "]");
			logger.info("****************************************************************************************************");
		}
	}
}
