package com.eim.utility.contribution.runners;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.EIMCompanies;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.contribution.workers.SimpleContributionsWorker;
/**
 * Abstract utility that launchs the computations according to a set a properties.
 * @author sdj
 * @author abaguet
 *
 */
public abstract class AbstractContributionsRunner {
	
	/**
	 * The logging utility
	 */
	public static Logger log = Logger.getLogger(AbstractContributionsRunner.class);

	/**
	 * The frequency of the computations
	 */
//	public ComputationFrequency frequency;
	
	/**
	 * The entity type
	 */
	public EntityType entityType;
	
	/**
	 * The related EIM company
	 */
	public EIMCompanies eimCompany;
	
//	public boolean executeTopLevelReturnsComputation = false;
	
//	public boolean executeUnderlyingContributionsComputation = false;
	
	/**
	 * The start date and end date
	 */
	public Date startDate;
	public Date endDate;
	
	/**
	 * Default empty constructor
	 */
	public AbstractContributionsRunner() {
	}
	
	/**
	 * Gets the entity ids list to work with
	 * @return
	 */
	public abstract ArrayList<Long> getEntitiesWorkingSet();

	/**
	 * Execute the computations according to the properties
	 * @return <code>true</code> if everything is ok (not yet implemented, always <code>true</code>)
	 */
	public boolean compute() {
		// Initialize returned value
		boolean result = true;
		
		// Get entities
		ArrayList<Long> workingSet = getEntitiesWorkingSet();
		
		// Initialize array of threads
		ArrayList<Thread> threads = new ArrayList<Thread>();
		
		/*
		if(executeTopLevelReturnsComputation) {
			// TODO Implement
		}
		if(executeUnderlyingContributionsComputation) {
			// TODO Implement
		}
 		*/
		
		// Make a simple contribution worker
		SimpleContributionsWorker simpleContributionsWorker = new SimpleContributionsWorker(workingSet, this.entityType, this.eimCompany, startDate, endDate);
//		simpleContributionsWorker.setEntityType(this.entityType);
		//EntityType.EVALUATION;
		threads.add(simpleContributionsWorker);
//		threads.add(new SimpleContributionsWorker(workingSet, startDate, endDate));
		
		// Execute threads
		for (Thread worker : threads) {
			worker.start();
		}
		
		// ?
		for (Thread worker : threads) {
			try {
				worker.join();
			}
			catch (InterruptedException e) {
				log.error("Cannot wait for computation worker!", e);
			}
		}
		
		// Return boolean: true if everything worked well, false otherwise
		return result;
	}
}
