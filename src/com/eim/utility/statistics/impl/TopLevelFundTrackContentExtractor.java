package com.eim.utility.statistics.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.dst.timeserie.TimeSerie;
import com.dst.timeserie.TimeSerieIndicator;
import com.dst.timeserie.TimeSerieValue;
import com.dst.util.log.Logger;
import com.eim.util.date.DateUtil;
import com.eim.util.format.DateFormatter;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.DatesComputer;
import com.eim.utility.statistics.api.TrackContentExtractor;
import com.eim.utility.statistics.database.QueriesProvider;

/**
 * Implementation of the <code>TrackContentExtractor</code> for top-level funds.
 * @author sdj
 *
 */
public class TopLevelFundTrackContentExtractor extends TrackContentExtractor {

	private Logger logger = Logger.getLogger(TopLevelFundTrackContentExtractor.class);
	
	private Connection masterConnection = null;
	
	/**
	 * Return the content of a track as DST TimeSerie, it only needs the entity <code>id</code>.
	 * @param id The entity id
	 * @return The content as DST TimeSerie
	 */
	@Override
	public TimeSerie getTrackContent(String id, ComputationFrequency frequency,boolean allowNonEOPEstimated) {
		logger.info("Track content of top level fund with id [" + id + "] and frequency " + frequency + " using " + getAllPerformancesQueryName());
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
	
	/**
	 * Return the content of a track as DST TimeSerie from the <code>fromDate</code> (included) to the <code>toDate</code> (included) according to the given <code>frequency</code>, it only needs the entity <code>id</code>.
	 * @param id The entity id
	 * @param fromDate The start date (included)
	 * @param toDate The end date (included)
	 * @param frequency The asked frequency
	 * @return The content as DST TimeSerie
	 */
	@Override
	public TimeSerie getTrackContent(String id, Date fromDate, Date toDate, ComputationFrequency frequency,boolean allowNonEOPEstimated) {
		logger.info("Track content of product with id [" + id + "] and frequency " + frequency  + " between " + DateFormatter.toInputString(fromDate) + " to "  + DateFormatter.toInputString(toDate) + " using " + getRangePerformancesQueryName());
		TimeSerie ts = new TimeSerie(TimeSerieIndicator.RELATIVERETURN);
		Connection intranetConnection = QueriesProvider.getConnection("intranet");
		try {
			PreparedStatement prepareStatement = null;
			if (masterConnection==null) {
				prepareStatement = intranetConnection.prepareStatement(QueriesProvider.getQuery(getRangePerformancesQueryName()));
			} else {
				prepareStatement = masterConnection.prepareStatement(QueriesProvider.getQuery(getRangePerformancesQueryName()));
			}
			prepareStatement.setString(1, id);
			prepareStatement.setDate(2, new java.sql.Date(fromDate.getTime()));
			prepareStatement.setDate(3, new java.sql.Date(toDate.getTime()));
			readDatabaseData(ts,prepareStatement,frequency, allowNonEOPEstimated);
			prepareStatement.close();
			if (masterConnection==null) {
				intranetConnection.close();
			}
		} catch (SQLException e) {
			logger.error("A SQL error occured during track content extraction!",e);
		}
		fillHoles(id,ts,frequency,true);
		return ts;
	}
	
	protected TimeSerie readDatabaseData(TimeSerie ts,PreparedStatement statement, ComputationFrequency frequency,boolean allowNonEOPEstimated) throws SQLException {
		ResultSet rs = statement.executeQuery();
		TimeSerieValue lastValue = null;
		ArrayList<Date> datesWithFinal = new ArrayList<Date>();
		while (rs.next()) {
			TimeSerieValue tsv = new TimeSerieValue(rs.getDouble(4),rs.getDate(1));
			tsv._isFinalStatus = rs.getString(3)==null || rs.getString(3).equals("Final");
			if (DatesComputer.isValidDate(rs.getDate(1), frequency) && !datesWithFinal.contains(tsv.getDate()) && rs.getString(5).equalsIgnoreCase(frequency.toString())) {
				ts.addValueWithoutReindex(tsv);
			}
			if (tsv._isFinalStatus && rs.getString(5).equalsIgnoreCase(frequency.toString())) {
				datesWithFinal.add(tsv.getDate());
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
	 * Return a list that contains all missing dates in a <code>TimeSerie</code> according to its frequency.<BR/>
	 * If the <code>insert</code> mode is activated the method with try to fill the holes using the push to end of period method.<BR/>
	 * 
	 * @param id The id of the entity
	 * @param ts The TimeSerie
	 * @param frequency The expected frequency
	 * @param insert When activated, fill the holes in the <code>TimeSerie</code> 
	 * @return The list of missing dates
	 */
	public Collection<Date> fillHoles(String id, TimeSerie ts, ComputationFrequency frequency, boolean insert) {
		if (frequency==ComputationFrequency.DAILY) {
			return new ArrayList<Date>();
		}
		Collection<Date> allDates = DatesComputer.getAllDatesWithinRange(ts.getStartDate(), ts.getStopDate(), frequency);
		ArrayList<Date> missingDates = new ArrayList<Date>();
		
		for (Date date : allDates) {
			if (ts.getValue(date)==null) {
				missingDates.add(date);
				logger.debug("Missing date:" + DateFormatter.toInputString(date));
			}
		}
		if (insert) {
			masterConnection = QueriesProvider.getConnection("intranet");
			for (Date date : missingDates) {
				Date startDate = DatesComputer.getPreviousValidDate(date,frequency);
				startDate = DateUtil.addOneDay(startDate);
				TimeSerie trackContent = this.getTrackContent(id,startDate,date,ComputationFrequency.DAILY,false);
				if (trackContent.getDatesAsList().size()>0) {
					TimeSerieValue lastValue = trackContent.getLastValue();
					lastValue._date = DatesComputer.getNextValidDate(lastValue.getDate(), frequency);
					ts.addValue(lastValue);
				} else {
					logger.error(DateFormatter.toInputString(date) + " - No replacement values could be found!");
				}
			}
			try {
				masterConnection.close();
			} catch (SQLException e) {
				logger.error("Could not properly close database connection!",e);
			}
			ts.reIndex();
		}
		return missingDates;
	}
	
	@Override
	public String getBenchmarkQueryName() {
		return "FUND_BENCHMARKS.SQL";
	}

	@Override
	public String getAllPerformancesQueryName() {
		return "TOPLEVEL_FUND_PERFORMANCES.SQL";
	}

	@Override
	public String getRangePerformancesQueryName() {
		return "TOPLEVEL_FUND_PERFORMANCES_WITHIN_RANGE.SQL";
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