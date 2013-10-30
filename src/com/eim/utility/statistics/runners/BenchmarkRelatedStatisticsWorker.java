package com.eim.utility.statistics.runners;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.DatesComputer;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.common.utility.HibernateUtil;
import com.eim.utility.statistics.model.BenchmarkRelatedStatistics;
import com.eim.utility.statistics.model.IStatistics;
import com.eim.utility.statistics.utility.StatisticsSetComputer;

/**
 * Implementation of <code>AbstractStatisticsWorker</code> for benchmark related statistics
 * @author sdj
 *
 */
public class BenchmarkRelatedStatisticsWorker extends AbstractStatisticsWorker {

	Logger log = Logger.getLogger(BenchmarkRelatedStatisticsWorker.class);
	
	public BenchmarkRelatedStatisticsWorker(ArrayList<String> workingSet,ComputationFrequency frequency,EntityType entityType,boolean allowNonEOPEstimated) {
		super(workingSet,frequency,entityType,allowNonEOPEstimated);
	}
	
	public void run() {
		log.info("BenchmarkRelatedStatistics - Computing on working set");
		long start = System.currentTimeMillis();
		for (String id : workingSet) {
			Date startDate = getBenchmarksComputationStartDate(id);
			cleanBenchmarksComputations(id,startDate);
			List<IStatistics> estimatedStatistics = getExistingBenchmarkStatistics(id,startDate);
			Date endDate = null;
			if (startDate!=null && !allowNonEOPEstimated) {
				endDate = DatesComputer.getPreviousValidDate(new Date(), frequency);
			}
			List<IStatistics> computeBenchmarkStatisticsSet = StatisticsSetComputer.computeBenchmarkStatisticsSet(startDate, endDate, frequency, id, entityType,allowNonEOPEstimated);
			HibernateUtil.storeStatistics(computeBenchmarkStatisticsSet,estimatedStatistics,BenchmarkRelatedStatistics.class);
		}
		log.info("BenchmarkRelatedStatistics - Finished on working set. Treated " + workingSet.size() + " entities in " + (System.currentTimeMillis()-start) + "ms.");
	}
}
