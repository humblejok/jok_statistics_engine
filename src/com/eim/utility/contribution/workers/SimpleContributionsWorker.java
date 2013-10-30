package com.eim.utility.contribution.workers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.eim.utility.common.model.EIMCompanies;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.common.utility.HibernateUtil;
import com.eim.utility.contribution.model.Contribution;
import com.eim.utility.contribution.utility.ContributionsComputer;

public class SimpleContributionsWorker extends AbstractContributionsWorker {

	/* -------------------------------------------------
	 * ATTRIBUTES
	 ------------------------------------------------- */
	
	/* -------------------------------------------------
	 * CONSTRUCTORS
	 ------------------------------------------------- */
	
	public SimpleContributionsWorker(ArrayList<Long> workingSet, EntityType entityType, EIMCompanies eimCompany, Date startDate, Date endDate) {
		super(workingSet, entityType, eimCompany, startDate, endDate);
	}

	/* -------------------------------------------------
	 * GETTERS/SETTERS
	 ------------------------------------------------- */
	
	/* -------------------------------------------------
	 * METHODS
	 ------------------------------------------------- */
	
	public void run() {
		log.info("Simple contributions - Computing on working set");
		long start = System.currentTimeMillis();
		
		// For each entity
		for (Long id : workingSet) {
			
			// Get start date (?)
//			Date startDate = getExistingUnderlyingContributionsStartDate(id);
			
//			cleanExistingTopLevelReturns(id, startDate);
			
			// Get existing contributions
			List<Contribution> existingContributions = null;
			if (this.entityType == EntityType.PORTFOLIO) {
				existingContributions = getPortfolioExistingContributions(id, startDate, endDate);
			}
			else if (this.entityType == EntityType.EVALUATION) {
				existingContributions = getExistingEvaluationContributions(id);
			}
			
			// Set end date (?)
//			Date endDate = null;
//			if (startDate!=null) {
//				endDate = DatesComputer.getPreviousValidDate(new Date(), frequency);
//			}
			
			List<Contribution> computedContributions = null;
			
			if (this.entityType == EntityType.PORTFOLIO) {
				// Compute contributions
				computedContributions = ContributionsComputer.computePortfolioContributionsWithinPeriod(startDate, endDate, id, this.eimCompany);
			}
			else if (this.entityType == EntityType.EVALUATION) {
				// Compute contributions
				computedContributions = ContributionsComputer.computeEvaluationContributions(id, this.eimCompany);
			}
			
//			// DEBUG - Start
//			for (Contribution contribution : computedContributions) {
//				System.out.println(contribution.toString());
//			}
//			// DEBUG - End
			
			// Store contributions in DB
			HibernateUtil.storeContributions(computedContributions, existingContributions, Contribution.class);
		}
		log.info("Simple contributions - Finished on working set. Treated " + workingSet.size() + " entities in " + (System.currentTimeMillis()-start) + "ms.");
	}
	
	public List<Contribution> getPortfolioExistingContributions(Long portfolioID, Date fromDate, Date toDate) {
		// Get existing contributions
		List<Contribution> existingContributions = new ArrayList<Contribution>();
		// If period is defined
		if (fromDate != null && toDate != null) {
			// Get all contributions in period
			existingContributions = getExistingPortfolioContributionsWithinPeriod(portfolioID, fromDate, toDate);
		}
		// Else, if only "from date" is defined
		else if (fromDate != null) {
			// Get all contributions in period
			existingContributions = getExistingPortfolioContributionsAfterDate(portfolioID, fromDate);
		}
		// Else, if only "to date" is defined
		else if (toDate != null) {
			// Get all contributions in period
			existingContributions = getExistingPortfolioContributionsBeforeDate(portfolioID, toDate);
		}
		// Else, none is defined
		else {
			// Get all contributions in period
			existingContributions = getExistingPortfolioContributions(portfolioID);
		}
		
		return existingContributions;
	}
	
	/**
	 * Lists all the non-benchmark statistics that already exists at a given date 
	 * @param id The entity id
	 * @param frequency The frequency
	 * @param date The date
	 * @return The list of statistics
	 */
	@SuppressWarnings("unchecked")
	public List<Contribution> getExistingPortfolioContributionsWithinPeriod(Long id, Date startDate, Date endDate) {
		log.info("Contributions - Looking for contributions for portfolio with id [" + id.toString() + "] between " + startDate.toString() + " and " + endDate.toString());

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT c " +
				"FROM Contribution c " +
				"WHERE c.portfolioID = ? " +
				"AND c.applicationDate > ? " +
				"AND c.applicationDate <= ? " +
				"AND c.company = ? ");
		
		query.setLong(0, id);
		query.setDate(1, startDate);
		query.setDate(2, endDate);
		query.setString(3, this.eimCompany.toString());
		
		List<Contribution> results = query.list();

		transaction.commit();
		session.close();
		log.info("Contributions - Got all contributions for portfolio with id [" + id.toString() + "] between " + startDate.toString() + " and " + endDate.toString());
		return results;
	}
	
	/**
	 * Lists all the non-benchmark statistics that already exists at a given date 
	 * @param id The entity id
	 * @param frequency The frequency
	 * @param date The date
	 * @return The list of statistics
	 */
	@SuppressWarnings("unchecked")
	public List<Contribution> getExistingPortfolioContributionsAfterDate(Long id, Date startDate) {
		log.info("Contributions - Looking for contributions for portfolio with id [" + id.toString() + "] after " + startDate.toString());

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT c " +
				"FROM Contribution c " +
				"WHERE c.portfolioID = ? " +
				"AND c.applicationDate > ? " +
				"AND c.company = ? ");
		
		query.setLong(0, id);
		query.setDate(1, startDate);
		query.setString(2, this.eimCompany.toString());
		
		List<Contribution> results = query.list();

		transaction.commit();
		session.close();
		log.info("Contributions - Got all contributions for portfolio with id [" + id.toString() + "] after " + startDate.toString());
		return results;
	}
	
	/**
	 * Lists all the non-benchmark statistics that already exists at a given date 
	 * @param id The entity id
	 * @param frequency The frequency
	 * @param date The date
	 * @return The list of statistics
	 */
	@SuppressWarnings("unchecked")
	public List<Contribution> getExistingPortfolioContributionsBeforeDate(Long id, Date endDate) {
		log.info("Contributions - Looking for contributions for portfolio with id [" + id.toString() + "] before " + endDate.toString());

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT c " +
				"FROM Contribution c " +
				"WHERE c.portfolioID = ? " +
				"AND c.applicationDate <= ? " +
				"AND c.company = ? ");
		
		query.setLong(0, id);
		query.setDate(1, endDate);
		query.setString(2, this.eimCompany.toString());
		
		List<Contribution> results = query.list();

		transaction.commit();
		session.close();
		log.info("Contributions - Got all contributions for portfolio with id [" + id.toString() + "] before " + endDate.toString());
		return results;
	}
	
	/**
	 * Lists all the non-benchmark statistics that already exists at a given date 
	 * @param id The entity id
	 * @param frequency The frequency
	 * @param date The date
	 * @return The list of statistics
	 */
	@SuppressWarnings("unchecked")
	public List<Contribution> getExistingPortfolioContributions(Long id) {
		log.info("Contributions - Looking for contributions for portfolio with id [" + id.toString() + "]");

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT c " +
				"FROM Contribution c " +
				"WHERE c.portfolioID = ? " +
				"AND c.company = ? ");
		
		query.setLong(0, id);
		query.setString(1, this.eimCompany.toString());
		
		List<Contribution> results = query.list();

		transaction.commit();
		session.close();
		log.info("Contributions - Got all contributions for portfolio with id [" + id.toString() + "]");
		return results;
	}
	
	/**
	 * Lists all the non-benchmark statistics that already exists at a given date 
	 * @param id The entity id
	 * @param frequency The frequency
	 * @param date The date
	 * @return The list of statistics
	 */
	@SuppressWarnings("unchecked")
	public List<Contribution> getExistingEvaluationContributions(Long id) {
		log.info("Contributions - Looking for contributions for evaluation with id [" + id.toString() + "]");

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("SELECT c " +
				"FROM Contribution c " +
				"WHERE c.evaluationID = ? " +
				"AND c.company = ? ");
		
		query.setLong(0, id);
		query.setString(1, this.eimCompany.toString());
		
		List<Contribution> results = query.list();

		transaction.commit();
		session.close();
		log.info("Contributions - Got all contributions for evaluation with id [" + id.toString() + "]");
		return results;
	}
	
}
