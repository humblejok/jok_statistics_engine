package com.eim.utility.statistics.runners;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.eim.util.date.DateUtil;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.DatesComputer;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.common.utility.HibernateUtil;
import com.eim.utility.statistics.model.ComputedStatistics;
import com.eim.utility.statistics.model.IStatistics;
import com.eim.utility.statistics.utility.StatisticsSetComputer;

/**
 * Implementation of <code>AbstractStatisticsWorker</code> for non-benchmark related statistics
 * @author sdj
 *
 */
public class ComputedStatisticsWorker extends AbstractStatisticsWorker {

	Logger log = Logger.getLogger(ComputedStatisticsWorker.class);
	
	public ComputedStatisticsWorker(ArrayList<String> workingSet,ComputationFrequency frequency,EntityType entityType,boolean allowNonEOPEstimated) {
		super(workingSet,frequency,entityType,allowNonEOPEstimated);
	}
	
	public void run() {
		log.info("ComputedStatistics - Computing on working set");
		long start = System.currentTimeMillis();
		for (String id : workingSet) {
			Date startDate = getNonBenchmarksComputationStartDate(id);
			cleanEstimatedNonBenchmarksComputations(id,startDate);
			List<IStatistics> estimatedStatistics = getExistingNonBenchmarkStatistics(id,startDate);
			Date endDate = null;
			if (startDate!=null && !allowNonEOPEstimated) {
				endDate = DateUtil.roundToDay(DatesComputer.getPreviousValidDate(new Date(), frequency));
			}
			List<IStatistics> computeStatiticsSet = StatisticsSetComputer.computeStatiticsSet(startDate, endDate, frequency, id, entityType,allowNonEOPEstimated);
			HibernateUtil.storeStatistics(computeStatiticsSet,estimatedStatistics,ComputedStatistics.class);
		}
		log.info("ComputedStatistics - Finished on working set. Treated " + workingSet.size() + " entities in " + (System.currentTimeMillis()-start) + "ms.");
	}
}
