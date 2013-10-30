package com.eim.utility.contribution.workers;

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
import com.eim.utility.common.model.EIMCompanies;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.common.utility.HibernateUtil;
import com.eim.utility.contribution.model.Contribution;
import com.eim.utility.contribution.model.IContribution;

/**
 * Abstract thread implementation that provides the basic features (start dates extraction, existing values, ...) for contributions and returns computations.
 * 
 * @author sdj
 * @author abaguet
 *
 */
public abstract class AbstractContributionsWorker extends Thread {

	/* -------------------------------------------------
	 * ATTRIBUTES
	 ------------------------------------------------- */
	
	/**
	 * The logging utility
	 */
	protected Logger log = Logger.getLogger(AbstractContributionsWorker.class);
	
	/**
	 * Current list of entity ids to work with
	 */
	protected ArrayList<Long> workingSet;
	
	/**
	 * The frequency
	 */
	protected ComputationFrequency frequency;
	
	/**
	 * The entity type
	 */
	protected EntityType entityType;
	
	/**
	 * The EIM company related to this set of entities
	 */
	protected EIMCompanies eimCompany;
	
	/**
	 * The start date
	 */
	protected Date startDate;

	/**
	 * The end date
	 */
	protected Date endDate;

	/* -------------------------------------------------
	 * CONSTRUCTORS
	 ------------------------------------------------- */
	
//	/**
//	 * The constructor
//	 * @param workingSet The list of entity ids
//	 * @param frequency The frequency
//	 * @param entityType The entity type
//	 */
//	public AbstractContributionsWorker(ArrayList<Long> workingSet,ComputationFrequency frequency,EntityType entityType) {
//		super();
//		this.workingSet = workingSet;
//		this.frequency = frequency;
//		this.entityType = entityType;
//	}

	/**
	 * The main constructor with minimum information required
	 * @param workingSet The list of entity ids
	 * @param entityType The entity type
	 * @param eimCompany The EIM company
	 */
	public AbstractContributionsWorker(ArrayList<Long> workingSet, EntityType entityType, EIMCompanies eimCompany) {
		super();
		this.workingSet = workingSet;
		this.entityType = entityType;
		this.eimCompany = eimCompany;
	}

	/**
	 * The constructor with maximum information
	 * @param workingSet The list of entity ids
	 * @param startDate The start date
	 * @param endDate The end date
	 */
	public AbstractContributionsWorker(ArrayList<Long> workingSet, EntityType entityType, EIMCompanies eimCompany, Date startDate, Date endDate) {
		super();
		this.workingSet = workingSet;
		this.entityType = entityType;
		this.eimCompany = eimCompany;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
//	/**
//	 * The constructor
//	 * @param workingSet The list of entity ids
//	 */
//	public AbstractContributionsWorker(ArrayList<Long> workingSet) {
//		super();
//		this.workingSet = workingSet;
//	}

//	/**
//	 * The constructor
//	 * @param workingSet The list of entity ids
//	 * @param startDate The start date
//	 * @param endDate The end date
//	 */
//	public AbstractContributionsWorker(ArrayList<Long> workingSet, Date startDate, Date endDate) {
//		super();
//		this.workingSet = workingSet;
//		this.startDate = startDate;
//		this.endDate = endDate;
//	}
	
	/* -------------------------------------------------
	 * GETTERS/SETTERS
	 ------------------------------------------------- */
	
	public EntityType getEntityType() {
		return this.entityType;
	}
	
	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}
	
	public EIMCompanies getEIMCompany() {
		return this.eimCompany;
	}
	
	public void setEIMCompany(EIMCompanies eimCompany) {
		this.eimCompany = eimCompany;
	}
	
	/* -------------------------------------------------
	 * METHODS
	 ------------------------------------------------- */
	
	@SuppressWarnings("unchecked")
	public List<IContribution> getExistingTopLevelReturns(String id, Date date) {
		log.info("Top Level Returns - Looking for computation after " + DateFormatter.toInputString(date) + " for " + id + " with frequency " + frequency);
		
		ArrayList<IContribution> effectiveList = new ArrayList<IContribution>();
		
		// TODO Implement Hibernate query
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery(
			"SELECT sc" +
			"FROM stats_contributions sc" +
			"WHERE sc.targetEntityId  = ?" +
			"AND sc.applicationDate >= ?" +
			"AND sc.frequency = ?" +
			"ORDER BY sc.applicationDate DESC"
		);
		
		query.setString(0, id);
		query.setDate(1, date);
		query.setString(2, frequency.name());
		
		List<Contribution> results = query.list();
		
		for (Contribution stat : results) {
			if (DatesComputer.isValidDate(stat.getApplicationDate(), frequency)) {
				effectiveList.add(stat);
			}
		}

		transaction.commit();
		session.close();
		// TODO - end
		
		log.info("Top Level Returns - Get all already existing top level returns for " + id + " with frequency " + frequency + " after " + DateFormatter.toInputString(date));
		return effectiveList;		
	}
	
	public List<IContribution> getExistingUnderlyingContributions(String id, Date date) {
		log.info("Underlying Contributions - Looking for computation after " + DateFormatter.toInputString(date) + " for " + id + " with frequency " + frequency);
		
		ArrayList<IContribution> effectiveList = new ArrayList<IContribution>();
		
		// TODO Implement Hibernate query
		
		log.info("Underlying Contributions - Get all already existing underlying contributions for " + id + " with frequency " + frequency + " after " + DateFormatter.toInputString(date));
		
		return effectiveList;		
	}
	
	public void cleanExistingTopLevelReturns(String id, Date date) {
		log.info("Top Level Returns - Cleaning computations after " + DateFormatter.toInputString(date) + " for " + id + " with frequency " + frequency);
		
		// TODO Implement Hibernate query
		
		log.info("Top Level Returns - Finished cleaning");
	}
	
	public void cleanExistingUnderlyingContributions(String id, Date date) {
		log.info("Underlying Contributions - Cleaning computations after " + DateFormatter.toInputString(date) + " for " + id + " with frequency " + frequency);
		
		// TODO Implement Hibernate query
		
		log.info("Underlying Contributions - Finished cleaning");
	}
	
	public Date getExistingTopLevelReturnsStartDate(String id) {
		log.info("Top Level Returns - Looking for computation start date of " + id + " with frequency " + frequency);
		Date date = null;
		
		// TODO Implement Hibernate query
		
		log.info("Top Level Returns - Computation start date of " + id + " with frequency " + frequency + ":" + DateFormatter.toInputString(date));
		return date;		
	}
	
	public Date getExistingUnderlyingContributionsStartDate(String id) {
		log.info("Underlying Contributions - Looking for computation start date of " + id + " with frequency " + frequency);
		Date date = null;
		
		// TODO Implement Hibernate query
		
		log.info("Underlying Contributions - Computation start date of " + id + " with frequency " + frequency + ":" + DateFormatter.toInputString(date));
		return date;		
	}
}