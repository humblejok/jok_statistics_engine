package com.eim.utility.statistics.runners;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.EntityType;
/**
 * Abstract utility that launchs the computations according to a set a properties.
 * @author sdj
 *
 */
public abstract class AbstractStatisticsRunner {
	
	/**
	 * The logging utility
	 */
	public static Logger log = Logger.getLogger(AbstractStatisticsRunner.class);

	/**
	 * Flag that indicates if the benchmark related computations have to be executed 
	 */
	public boolean executeBenchmarksRelatedStatistics = false;
	
	/**
	 * Flag that indicates if the general statistics computations have to be executed 
	 */
	public boolean executeNonBenchmarksRelatedStatistics = false;
	
	/**
	 * The frequency of the computations
	 */
	public ComputationFrequency frequency;
	
	/**
	 * The entity type
	 */
	public EntityType entityType;
	
	/**
	 * Allow estimated not matching the end-of-period for the latest stats
	 */
	public boolean allowNonEOPEstimated;
	
	/**
	 * Default empty constructor
	 */
	public AbstractStatisticsRunner() {
	}
	
	/**
	 * Gets the entity ids list to work with
	 * @return
	 */
	public abstract ArrayList<String> getEntitiesWorkingSet();

	/**
	 * Execute the computations according to the properties
	 * @return <code>true</code> if everything is ok (not yet implemented, always <code>true</code>
	 */
	public boolean compute() {
		boolean result = true;
		ArrayList<String> workingSet = getEntitiesWorkingSet();
		ArrayList<Thread> threads = new ArrayList<Thread>();
		if(executeBenchmarksRelatedStatistics) {
			threads.add(new BenchmarkRelatedStatisticsWorker(workingSet,frequency,entityType,allowNonEOPEstimated));
		}
		if(executeNonBenchmarksRelatedStatistics) {
			threads.add(new ComputedStatisticsWorker(workingSet,frequency,entityType,allowNonEOPEstimated));
		}
		for (Thread worker : threads) {
			worker.start();
		}
		
		for (Thread worker : threads) {
			try {
				worker.join();
			} catch (InterruptedException e) {
				log.error("Cannot wait for computation worker!",e);
			}
		}
		return result;
	}
}
