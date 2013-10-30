package com.eim.utility.statistics.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.dst.timeserie.TimeSerie;
import com.dst.timeserie.TimeSerieIndicator;
import com.dst.timeserie.TimeSerieValue;
import com.dst.timeserie.TimeSeries;
import com.dst.util.log.Logger;
import com.eim.util.date.DateUtil;
import com.eim.util.format.DateFormatter;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.DatesComputer;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.statistics.database.QueriesProvider;
import com.eim.utility.statistics.impl.TrackContentExtractorFactory;
import com.eim.utility.statistics.model.AdditionalEntityInformation;
import com.eim.utility.statistics.model.BenchmarkDescriptor;

/**
 * Abstract class that defines the minimum methods that need to be implemented when creating a Track Content Extractor.
 * @author sdj
 *
 */
public abstract class TrackContentExtractor {
	
	/**
	 * The logging utility
	 */
	protected Logger logger = Logger.getLogger(TrackContentExtractor.class);
	
	/**
	 * Returns the filename of the benchmark query
	 * @return The filename
	 */
	public abstract String getBenchmarkQueryName();
	/**
	 * Returns the filename of the query that extracts all the performances.
	 * @return The filename
	 */
	public abstract String getAllPerformancesQueryName();
	/**
	 * Returns the filename of the query that extracts all the performances between a range of dates.
	 * @return The filename
	 */
	public abstract String getRangePerformancesQueryName();

	/**
	 * Returns the filename of the query that extracts the total position of the product or portfolio.
	 * @return The filename
	 */
	public abstract String getTotalPositionQueryName();
	
	/**
	 * Returns the filename of the query that extracts all the long exposures of the product or portfolio.
	 * @return The filename
	 */
	public abstract String getLongExposureQueryName();
	
	/**
	 * Returns the filename of the query that extracts all the short exposures of the product or portfolio.
	 * @return The filename
	 */
	public abstract String getShortExposureQueryName();
	
	/**
	 * Returns the exposure of the given entity from the DB
	 * @param entityId The entity id
	 * @param frequency The frequency
	 * @param date The application date
	 * @return The set of exposures
	 */
	public AdditionalEntityInformation getAdditionalInformation(String entityId, ComputationFrequency frequency, Date date) {
		AdditionalEntityInformation info = new AdditionalEntityInformation();
		
		if (getTotalPositionQueryName()!=null) {
			info.setTotalInvestedUSDAmount(getTotalPosition(entityId,date));
		}
		if (getShortExposureQueryName()!=null) {
			info.setShortExposures(getShortExposures(entityId,date));
		}
		if (getLongExposureQueryName()!=null) {
			info.setLongExposures(getLongExposures(entityId,date));
		}
		
		return info;
	}
	
	/**
	 * Returns the short exposure of the given entity
	 * @param id The entity id
	 * @param date The date
	 * @return The short exposure
	 */
	private ArrayList<Double> getShortExposures(String id, Date date) {
		Connection connection = QueriesProvider.getConnection("source");
		 ArrayList<Double> values = new ArrayList<Double>();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(QueriesProvider.getQuery(getShortExposureQueryName()));
			prepareStatement.setString(1, id);
			prepareStatement.setDate(2, new java.sql.Date(date.getTime()));
			
			ResultSet result = prepareStatement.executeQuery();
			
			while (result.next()) {
				values.add(new Double(result.getDouble(1)));
			}
			prepareStatement.close();
			connection.close();
		} catch (SQLException e) {
			logger.error("A SQL error occured during short exposure extraction!",e);
		}
		return values;
	}

	/**
	 * Returns the long exposure of the given entity
	 * @param id The entity id
	 * @param date The date
	 * @return The short exposure
	 */
	private ArrayList<Double> getLongExposures(String id, Date date) {
		Connection connection = QueriesProvider.getConnection("source");
		 ArrayList<Double> values = new ArrayList<Double>();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(QueriesProvider.getQuery(getLongExposureQueryName()));
			prepareStatement.setString(1, id);
			prepareStatement.setDate(2, new java.sql.Date(date.getTime()));
			
			ResultSet result = prepareStatement.executeQuery();
			
			while (result.next()) {
				values.add(new Double(result.getDouble(1)));
			}
			prepareStatement.close();
			connection.close();
		} catch (SQLException e) {
			logger.error("A SQL error occured during long exposure extraction!",e);
		}
		return values;
	}
	
	/**
	 * Returns the position of the given entity
	 * @param id The entity id
	 * @param date The date
	 * @return The short exposure
	 */
	private Double getTotalPosition(String id, Date date) {
		Connection connection = QueriesProvider.getConnection("source");
		Double value = Double.NaN;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(QueriesProvider.getQuery(getTotalPositionQueryName()));
			prepareStatement.setString(1, id);
			prepareStatement.setDate(2, new java.sql.Date(date.getTime()));
			
			ResultSet result = prepareStatement.executeQuery();
			
			if (result.next()) {
				value = new Double(result.getDouble(1));
			}
			prepareStatement.close();
			connection.close();
		} catch (SQLException e) {
			logger.error("A SQL error occured during total position extraction!",e);
		}
		return value;
	}
	
	/**
	 * Return the content of a track as DST TimeSerie, it only needs the entity <code>id</code>.
	 * @param id The entity id
	 * @param frequency The frequency
	 * @param allowNonEOPEstimated Accept non EOP for last value
	 * @return The content as DST TimeSerie
	 */
	public TimeSerie getTrackContent(String id, ComputationFrequency frequency,boolean allowNonEOPEstimated) {
		logger.info("Track content of child fund with id [" + id + "] and frequency " + frequency + " using " + getAllPerformancesQueryName());
		TimeSerie ts = new TimeSerie(TimeSerieIndicator.RELATIVERETURN);
		Connection connection = QueriesProvider.getConnection("source");
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(QueriesProvider.getQuery(getAllPerformancesQueryName()));
			prepareStatement.setString(1, id);
			readDatabaseData(ts,prepareStatement,frequency,allowNonEOPEstimated);
			prepareStatement.close();
			connection.close();
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
	public TimeSerie getTrackContent(String id, Date fromDate, Date toDate, ComputationFrequency frequency, boolean allowNonEOPEstimated) {
		logger.info("Track content with id [" + id + "] and frequency " + frequency  + " between " + DateFormatter.toInputString(fromDate) + " to "  + DateFormatter.toInputString(toDate) + " using " + getRangePerformancesQueryName());
		TimeSerie ts = new TimeSerie(TimeSerieIndicator.RELATIVERETURN);
		Connection connection = QueriesProvider.getConnection("source");
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(QueriesProvider.getQuery(getRangePerformancesQueryName()));
			prepareStatement.setString(1, id);
			prepareStatement.setDate(2, new java.sql.Date(fromDate.getTime()));
			prepareStatement.setDate(3, new java.sql.Date(toDate.getTime()));
			readDatabaseData(ts,prepareStatement,frequency,allowNonEOPEstimated);
			prepareStatement.close();
			connection.close();
		} catch (SQLException e) {
			logger.error("A SQL error occured during track content extraction!",e);
		}
		fillHoles(id,ts,frequency,true);
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
			ts.reIndex();
		}
		return missingDates;
	}
	
	/**
	 * Returns the list of benchmarks of the working entity and their associated tracks using dates delimiters and frequency.<BR/>
	 * @param id The entity id
	 * @param fromDate The start date for tracks extraction
	 * @param toDate The end date for tracks extraction
	 * @param frequency The expected frequency
	 * @return The list of benchmarks and their tracks
	 */
	public HashMap<BenchmarkDescriptor, TimeSerie> getBenchmarkTracks(String id, Date fromDate, Date toDate,ComputationFrequency frequency,boolean allowNonEOPEstimated) {
		HashMap<BenchmarkDescriptor, TimeSerie> results = new HashMap<BenchmarkDescriptor, TimeSerie>();
		if (fromDate!=null && toDate!=null) {
			Connection connection = QueriesProvider.getConnection("source");
			String query = QueriesProvider.getQuery(getBenchmarkQueryName());
			try {
				PreparedStatement prepareStatement = connection.prepareStatement(query);
				prepareStatement.setString(1, id);
				ResultSet executeQuery = prepareStatement.executeQuery();
				while (executeQuery.next()) {
					BenchmarkDescriptor descriptor = new BenchmarkDescriptor();
					descriptor.setId(executeQuery.getString(2));
					descriptor.setName(executeQuery.getString(1));
					descriptor.setRfr(executeQuery.getString(3).equals("true"));
					results.put(descriptor, TrackContentExtractorFactory.getTrackContentExtractor(EntityType.INDEX).getTrackContent(descriptor.getId(), fromDate, toDate, frequency,allowNonEOPEstimated));
				}
			} catch (SQLException e) {
				logger.error("A SQL error occured during benchmarks track content extraction!",e);
			}
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public TimeSerie getMonthlyTrackFromDaily(TimeSerie dailyContent, Date workingDate) {
		TimeSerie workingTrackMonthlyContent = new TimeSerie(TimeSerieIndicator.RELATIVERETURN);
		TimeSerie wrkTrackMonthlyContent = dailyContent;
		wrkTrackMonthlyContent = wrkTrackMonthlyContent.truncateAfter(workingDate);
		TimeSeries tss = new TimeSeries();
		tss.addTimeSerie(wrkTrackMonthlyContent);
		wrkTrackMonthlyContent = tss.getMonthlyReturn();
		for (Date dt : (ArrayList<Date>)wrkTrackMonthlyContent.getMonthlyDatesAsList()) {
			workingTrackMonthlyContent.addValueWithoutReindex(wrkTrackMonthlyContent.getValue(dt));
		}
		if (!DateUtil.isEOM(wrkTrackMonthlyContent.getLastValue().getDate())) {
			workingTrackMonthlyContent.addValueWithoutReindex(wrkTrackMonthlyContent.getLastValue());
		}
		workingTrackMonthlyContent.reIndex();
		return workingTrackMonthlyContent;
	}
	
	/**
	 * Extracts the data from the SQL statement and build a DST <code>TimeSerie</code> with these data.<BR/>
	 * Resulting <code>TimeSerie</code> content will be filled according to the given frequency.
	 * @param ts The <code>TimeSerie</code> to work with
	 * @param statement The SQL statement
	 * @param frequency The frequency
	 * @return The modified <code>TimeSerie</code>
	 * @throws SQLException
	 */
	protected abstract TimeSerie readDatabaseData(TimeSerie ts,PreparedStatement statement, ComputationFrequency frequency, boolean allowNonEOPEstimated) throws SQLException;
	
}
