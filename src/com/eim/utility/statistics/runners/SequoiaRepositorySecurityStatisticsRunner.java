package com.eim.utility.statistics.runners;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.statistics.database.QueriesProvider;

/**
 * Implementation of <code>AbstractStatisticsRunner</code> for invested top-level funds
 * @author sdj
 *
 */
public class SequoiaRepositorySecurityStatisticsRunner extends AbstractStatisticsRunner {

	public SequoiaRepositorySecurityStatisticsRunner(ComputationFrequency frequency) {
		this.entityType = EntityType.OTHER_FUND;
		this.executeBenchmarksRelatedStatistics = true;
		this.executeNonBenchmarksRelatedStatistics = true;
		this.frequency = frequency;
		this.allowNonEOPEstimated = true;
	}
	
	@Override
	public ArrayList<String> getEntitiesWorkingSet() {
		log.info("Getting Invested Top Level Funds");
		
		ArrayList<String> workingSet = new ArrayList<String>();
		
		Connection connection = QueriesProvider.getConnection("source");
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(QueriesProvider.getQuery("SEQUOIA_REPOSITORY_SECURITIES_LIST.SQL"));
			ResultSet resultset = prepareStatement.executeQuery();
			while (resultset.next()) {
				workingSet.add(resultset.getString(1));
			}
			prepareStatement.close();
			connection.close();
		} catch (SQLException e) {
			log.error("A SQL error occured during Invested Top Level Funds workset creation!",e);
		}
		return workingSet;
	}

}
