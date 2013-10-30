package com.eim.utility.statistics.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dst.timeserie.TimeSerie;
import com.dst.timeserie.TimeSerieIndicator;
import com.dst.timeserie.TimeSerieValue;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.DatesComputer;
import com.eim.utility.statistics.api.TrackContentExtractor;
import com.eim.utility.statistics.database.QueriesProvider;

/**
 * Implementation of the <code>TrackContentExtractor</code> for indices.
 * @author sdj
 *
 */
public class IndexTrackContentExtractor extends TrackContentExtractor {

	protected TimeSerie readDatabaseData(TimeSerie ts,PreparedStatement statement, ComputationFrequency frequency,boolean allowNonEOPEstimated) throws SQLException {
		ResultSet rs = statement.executeQuery();
		TimeSerieValue lastValue = null;
		while (rs.next()) {
			TimeSerieValue tsv = new TimeSerieValue(rs.getDouble(4),rs.getDate(1));
			tsv._isFinalStatus = rs.getString(3)==null || rs.getString(3).equals("Final");
			if (DatesComputer.isValidDate(rs.getDate(1), frequency) && rs.getString(5).equalsIgnoreCase(frequency.toString())) {
				ts.addValueWithoutReindex(tsv);
			}
			lastValue = tsv;
		}
		if (allowNonEOPEstimated && lastValue!=null && !DatesComputer.isValidDate(lastValue.getDate(), frequency)) {
			ts.addValueWithoutReindex(lastValue);
		}
		rs.close();
		ts.reIndex();
		return ts;
	}
	
	/**
	 * Return the content of a track as DST TimeSerie, it only needs the entity <code>id</code>.
	 * @param id The entity id
	 * @return The content as DST TimeSerie
	 */
	@Override
	public TimeSerie getTrackContent(String id, ComputationFrequency frequency,boolean allowNonEOPEstimated) {
		logger.info("Track content of index with id [" + id + "] and frequency " + frequency + " using " + getAllPerformancesQueryName());
		TimeSerie ts = new TimeSerie(TimeSerieIndicator.RELATIVERETURN);
		Connection intranetConnection = QueriesProvider.getConnection("intranet");
		try {
			PreparedStatement prepareStatement = intranetConnection.prepareStatement(QueriesProvider.getQuery(getAllPerformancesQueryName()));
			prepareStatement.setString(1, id);
			readDatabaseData(ts,prepareStatement,frequency,allowNonEOPEstimated);
			prepareStatement.close();
			intranetConnection.close();
		} catch (SQLException e) {
			logger.error("A SQL error occured during track content extraction!",e);
		}
		fillHoles(id,ts,frequency,true);
		return ts;
	}

	
	@Override
	public String getBenchmarkQueryName() {
		// No use
		return null;
	}

	@Override
	public String getAllPerformancesQueryName() {
		return "INDEX_PERFORMANCES.SQL";
	}

	@Override
	public String getRangePerformancesQueryName() {
		return "INDEX_PERFORMANCES_WITHIN_RANGE.SQL";
	}

	@Override
	public String getTotalPositionQueryName() {
		return null;
	}

	@Override
	public String getLongExposureQueryName() {
		return null;
	}

	@Override
	public String getShortExposureQueryName() {
		return null;
	}

}