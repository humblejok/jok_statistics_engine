package com.eim.utility.statistics.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.dst.timeserie.TimeSerie;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.statistics.api.TrackContentExtractor;

public class ExternalTrackContentExtractor extends TrackContentExtractor {

	@Override
	public String getBenchmarkQueryName() {
		return null;
	}

	@Override
	public String getAllPerformancesQueryName() {
		return null;
	}

	@Override
	public String getRangePerformancesQueryName() {
		return null;
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

	@Override
	protected TimeSerie readDatabaseData(TimeSerie ts,
			PreparedStatement statement, ComputationFrequency frequency,
			boolean allowNonEOPEstimated) throws SQLException {
		return null;
	}

	@Override
	public TimeSerie getTrackContent(String id, ComputationFrequency frequency,boolean allowNonEOPEstimated) {
		return null;
	}
	
	@Override
	public TimeSerie getTrackContent(String id, Date fromDate, Date toDate, ComputationFrequency frequency, boolean allowNonEOPEstimated) {
		return null;
	}
	
}
