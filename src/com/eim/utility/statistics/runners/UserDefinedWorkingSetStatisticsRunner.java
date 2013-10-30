package com.eim.utility.statistics.runners;

import java.util.ArrayList;

import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.EntityType;

/**
 * Implementation of <code>AbstractStatisticsRunner</code> for a user defined entity type and a user defined entity ids list
 * @author sdj
 *
 */
public class UserDefinedWorkingSetStatisticsRunner extends AbstractStatisticsRunner {

	private ArrayList<String> entityIds = new ArrayList<String>();
	
	public UserDefinedWorkingSetStatisticsRunner(EntityType entityType, ComputationFrequency frequency, ArrayList<String> entityIds) {
		super();
		this.entityType = entityType;
		this.executeBenchmarksRelatedStatistics = true;
		if (entityType==EntityType.INDEX) {
			this.executeBenchmarksRelatedStatistics = false;
		}
		this.executeNonBenchmarksRelatedStatistics = true;
		this.frequency = frequency;
		this.entityIds = entityIds;
		this.allowNonEOPEstimated = true;
	}

	@Override
	public ArrayList<String> getEntitiesWorkingSet() {
		return entityIds;
	}

}
