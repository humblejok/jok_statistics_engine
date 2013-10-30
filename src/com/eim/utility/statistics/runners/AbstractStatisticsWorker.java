package com.eim.utility.statistics.runners;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.eim.util.format.DateFormatter;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.DatesComputer;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.common.utility.HibernateUtil;
import com.eim.utility.statistics.model.BenchmarkRelatedStatistics;
import com.eim.utility.statistics.model.ComputedStatistics;
import com.eim.utility.statistics.model.IStatistics;

/**
 * Abstract thread implementation that provides the basic features (start dates extraction, existing values, ...) for statistics computations.
 * 
 * @author sdj
 *
 */
public abstract class AbstractStatisticsWorker extends Thread {

	/**
	 * The logging utility
	 */
	protected Logger log = Logger.getLogger(AbstractStatisticsWorker.class);
	
	/**
	 * Current list of entity ids to work with
	 */
	protected ArrayList<String> workingSet;
	/**
	 * The frequency
	 */
	protected ComputationFrequency frequency;
	/**
	 * The entity type
	 */
	protected EntityType entityType;
	
	/**
	 * 
	 */
	protected boolean allowNonEOPEstimated;
	/**
	 * The constructor
	 * @param workingSet The list of entity ids
	 * @param frequency The frequency
	 * @param entityType The entity type
	 */
	public AbstractStatisticsWorker(ArrayList<String> workingSet,ComputationFrequency frequency,EntityType entityType, boolean allowNonEOPEstimated) {
		super();
		this.workingSet = workingSet;
		this.frequency = frequency;
		this.entityType = entityType;
		this.allowNonEOPEstimated = allowNonEOPEstimated;
	}

	/**
	 * Lists all the non-benchmark statistics that already exists at a given date 
	 * @param id The entity id
	 * @param frequency The frequency
	 * @param date The date
	 * @return The list of statistics
	 */
	@SuppressWarnings("unchecked")
	public List<IStatistics> getExistingNonBenchmarkStatistics(String id, Date date) {
		log.info("ComputedStatistics - Looking for computation at " + DateFormatter.toInputString(date) + " for " + id + " with frequency " + frequency);

		ArrayList<IStatistics> effectiveList = new ArrayList<IStatistics>();
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT cs FROM ComputedStatistics cs WHERE cs.targetEntityId  = ? AND cs.applicationDate>= ? AND cs.frequency = ? ORDER BY cs.applicationDate DESC");
		
		query.setString(0, id);
		query.setDate(1, date);
		query.setString(2, frequency.name());
		
		List<ComputedStatistics> results = query.list();
		
		for (ComputedStatistics stat : results) {
			if (DatesComputer.isValidDate(stat.getApplicationDate(), frequency)) {
				effectiveList.add(stat);
			}
		}

		transaction.commit();
		session.close();
		log.info("ComputedStatistics - Get all already existing statistics for " + id + " with frequency " + frequency + " after " + DateFormatter.toInputString(date));
		return effectiveList;
	}
	
	/**
	 * Lists all the benchmark related statistics that already exists at a given date 
	 * @param id The entity id
	 * @param frequency The frequency
	 * @param date The date
	 * @return The list of statistics
	 */
	@SuppressWarnings("unchecked")
	public List<IStatistics> getExistingBenchmarkStatistics(String id, Date date) {
		log.info("ComputedStatistics - Looking for computation start date of " + id + " with frequency " + frequency);

		ArrayList<IStatistics> effectiveList = new ArrayList<IStatistics>();
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT brs FROM BenchmarkRelatedStatistics brs WHERE brs.relatedEntityId  = ? AND brs.applicationDate = ? AND brs.frequency = ? ORDER BY brs.applicationDate DESC");
		
		query.setString(0, id);
		query.setDate(1, date);
		query.setString(2, frequency.name());
		
		List<IStatistics> results = query.list();
		
		for (IStatistics stat : results) {
			if (DatesComputer.isValidDate(stat.getApplicationDate(), frequency)) {
				effectiveList.add(stat);
			}
		}

		transaction.commit();
		session.close();
		log.info("ComputedStatistics - Get all already existing statistics for " + id + " with frequency " + frequency + " after " + DateFormatter.toInputString(date));
		return effectiveList;
	}

	/**
	 * Removes all estimated values of <code>ComputedStatistics</code> of the given entity after the given date
	 * @param id The entity id
	 * @param date The date
	 */
	@SuppressWarnings("unchecked")
	public void cleanEstimatedNonBenchmarksComputations(String id, Date date) {
		log.info("ComputedStatistics - Cleaning ESTIMATED non-benchmark statistics for id " + id + " after " + date);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("SELECT cs FROM ComputedStatistics cs WHERE cs.targetEntityId  = ? AND cs.frequency = ?" + (date!=null?" AND cs.applicationDate >= ?":""));
		
		query.setString(0, id);
		query.setString(1, frequency.name());
		if (date!=null) {
			query.setDate(2, date);
		}
		
		List<ComputedStatistics> results = query.list();
		log.info("ComputedStatistics - Cleaning " + results.size() + " elements");
		for (ComputedStatistics stat : results) {
			session.delete(stat);
		}
		
		transaction.commit();
		session.close();
		log.info("ComputedStatistics - Finished cleaning ESTIMATED non-benchmark statistics");
	}
	
	/**
	 * Get the last date of non-benchmark related statistics
	 * @param id The entity id
	 * @param frequency The frequency
	 * @return The date
	 */
	@SuppressWarnings("unchecked")
	public Date getNonBenchmarksComputationStartDate(String id) {
		log.info("ComputedStatistics - Looking for computation start date of " + id + " with frequency " + frequency);
		Date date = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT cs FROM ComputedStatistics cs WHERE cs.targetEntityId  = ? AND cs.frequency = ? AND cs.status = 'FINAL' ORDER BY cs.applicationDate DESC");
		
		query.setString(0, id);
		query.setString(1, frequency.name());
		
		List<ComputedStatistics> results = query.list();
		
		for (ComputedStatistics stat : results) {
			date = stat.getApplicationDate();
			if (DatesComputer.isValidDate(stat.getApplicationDate(), frequency)) {
				break;
			}
		}

		transaction.commit();
		session.close();
		log.info("ComputedStatistics - Computation start date of " + id + " with frequency " + frequency + ":" + DateFormatter.toInputString(date));
		return date;
	}

	/**
	 * Removes all estimated values of <code>BenchmarkRelatedStatistics</code> of the given entity after the given date
	 * @param id The entity id
	 * @param date The date
	 */
	@SuppressWarnings("unchecked")
	public void cleanBenchmarksComputations(String id,Date date) {
		log.info("BenchmarkRelatedStatistics - Cleaning ESTIMATED benchmark statistics for id " + id + " after " + date);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("SELECT brs FROM BenchmarkRelatedStatistics brs WHERE brs.relatedEntityId = ? AND brs.frequency = ? " + (date!=null?" AND brs.applicationDate >= ?":""));
		
		query.setString(0, id);
		query.setString(1, frequency.name());
		if (date!=null) {
			query.setDate(2, date);
		}
		
		List<BenchmarkRelatedStatistics> results = query.list();
		
		log.info("BenchmarkRelatedStatistics - Cleaning " + results.size() + " elements");
		for (BenchmarkRelatedStatistics stat : results) {
			session.delete(stat);
		}
		
		transaction.commit();
		session.close();
		log.info("BenchmarkRelatedStatistics - Finished cleaning ESTIMATED benchmark statistics");
	}
	
	/**
	 * Get the last date of benchmark related statistics
	 * @param id The entity id
	 * @param frequency The frequency
	 * @return The date
	 */
	public Date getBenchmarksComputationStartDate(String id) {
		log.info("BenchmarkRelatedStatistics - Looking for computation start date of " + id + " with frequency " + frequency);
		Date date = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT MIN(brs.applicationDate) FROM BenchmarkRelatedStatistics brs WHERE brs.relatedEntityId  = ? AND brs.frequency = ? AND brs.status = 'ESTIMATED'");
		
		query.setString(0, id);
		query.setString(1, frequency.name());
		Timestamp minimalTS = (Timestamp)query.list().get(0);
		
		query = session.createQuery("SELECT MAX(brs.applicationDate) FROM BenchmarkRelatedStatistics brs WHERE brs.relatedEntityId  = ? AND brs.frequency = ? AND brs.status = 'FINAL'");
		
		query.setString(0, id);
		query.setString(1, frequency.name());
		
		Timestamp maximumTS = (Timestamp)query.list().get(0);
		date = null;
		if (minimalTS!=null) {
			log.info("BenchmarkRelatedStatistics - Using MINIMUM ESTIMATED VALUE as start date");
			date = new Date(minimalTS.getTime());
		} else if (maximumTS!=null) {
			log.info("BenchmarkRelatedStatistics - Using MAXIMUM FINAL VALUE as start date");
			date = new Date(maximumTS.getTime());
		}

		transaction.commit();
		session.close();
		
		log.info("BenchmarkRelatedStatistics - Computation start date of " + id + " with frequency " + frequency + ":" + DateFormatter.toInputString(date));
		return date;
	}
	
}