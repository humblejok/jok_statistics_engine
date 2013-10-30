package com.eim.utility.statistics.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dst.timeserie.TimeSerie;
import com.dst.timeserie.TimeSerieValue;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.DatesComputer;
import com.eim.utility.statistics.api.TrackContentExtractor;
/**
 * Implementation of the <code>TrackContentExtractor</code> for non-top-level funds.
 * @author sdj
 *
 */
public class ChildFundTrackContentExtractor extends TrackContentExtractor {

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

	@Override
	public String getBenchmarkQueryName() {
		return "FUND_BENCHMARKS.SQL";
	}

	@Override
	public String getAllPerformancesQueryName() {
		return "CHILD_FUND_PERFORMANCES.SQL";
	}

	@Override
	public String getRangePerformancesQueryName() {
		return "CHILD_FUND_PERFORMANCES_WITHIN_RANGE.SQL";
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