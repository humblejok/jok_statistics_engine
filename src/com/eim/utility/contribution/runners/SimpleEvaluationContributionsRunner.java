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
 * @deprecated Use SimpleContributionsRunner instead
 *
 */
public class SimpleEvaluationContributionsRunner {
	
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
//	public EntityType entityType;
	
//	public boolean executeTopLevelReturnsComputation = false;
	
//	public boolean executeUnderlyingContributionsComputation = false;
	
	/**
	 * Default empty constructor
	 */
	public SimpleEvaluationContributionsRunner() {
	}
	
	/**
	 * Execute the computations according to the properties
	 * @return <code>true</code> if everything is ok (not yet implemented, always <code>true</code>)
	 */
	public boolean compute() {
		// Initialize returned value
		boolean result = true;
		
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
		SimpleContributionsWorker simpleContributionsWorker = new SimpleContributionsWorker(null, EntityType.EVALUATION, EIMCompanies.EIM_SWITZERLAND, null, null);
//		simpleContributionsWorker.setEntityType(EntityType.EVALUATION);
		threads.add(simpleContributionsWorker);
		
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
