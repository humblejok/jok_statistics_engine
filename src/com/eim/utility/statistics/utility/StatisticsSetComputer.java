package com.eim.utility.statistics.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.dst.timeserie.TimeSerie;
import com.dst.timeserie.TimeSeries;
import com.dst.timeserie.interpolator.InterpolatorFactory;
import com.eim.util.date.DateUtil;
import com.eim.util.format.DateFormatter;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.DatesComputer;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.common.model.QualityStatus;
import com.eim.utility.statistics.api.TrackContentExtractor;
import com.eim.utility.statistics.impl.TrackContentExtractorFactory;
import com.eim.utility.statistics.model.AdditionalEntityInformation;
import com.eim.utility.statistics.model.BenchmarkDescriptor;
import com.eim.utility.statistics.model.BenchmarkRelatedStatistics;
import com.eim.utility.statistics.model.ComputedStatistics;
import com.eim.utility.statistics.model.IStatistics;

/**
 * Utility class that provides methods handling statistics set computations, would they be benchmark related or not.
 * 
 * @author sdj
 *
 */
public class StatisticsSetComputer {
	
	/**
	 * Logging utility
	 */
	private static Logger log = Logger.getLogger(StatisticsSetComputer.class);
	
	/**
	 * Computes and returns <B>non-benchmark</B> related statistics of the given entity (defined by its <code>entityId</code> and its <code>type</code>) for all dates between the <code>fromDate</code> and the <code>toDate</code> that match the given <code>frequency</code>.<BR/>
	 * 
	 * <B>WARNING: Working on ANY FREQUENCY, however the DST library has not been tested with other frequencies</B>
	 * 
	 * @param fromDate The start date (included)
	 * @param toDate The to date (included)
	 * @param frequency The computation frequency
	 * @param entityId The id of the target entity
	 * @param type The type of the target entity
	 * @return A collection of statistics
	 */
	public static List<IStatistics> computeStatiticsSet(Date fromDate, Date toDate, ComputationFrequency frequency, String entityId, EntityType type,boolean allowNonEOPEstimated) {
 		log.info("Non-benchmark related statistics - Working on a " + type + " with id [" + entityId + "]");
		long fullStart = System.currentTimeMillis();
		long start = System.currentTimeMillis();
		ArrayList<IStatistics> results = new ArrayList<IStatistics>();
		TrackContentExtractor extractor = TrackContentExtractorFactory.getTrackContentExtractor(type);
		
		// Get the full track content
		TimeSerie fullTrackContent = null;
		// If we don't have any restriction on the period to work with
		if (fromDate==null && toDate==null) {
			// Get the full track content
			fullTrackContent = extractor.getTrackContent(entityId, frequency, allowNonEOPEstimated);
			
			// If the full track start date is defined, save this value as well as the end date
			if (fullTrackContent.getStartDate() != null) {
				fromDate = fullTrackContent.getStartDate();
				toDate = fullTrackContent.getStopDate();
			}
			// We couldn't get the track start date, it's an error
			else {
				log.error("The extracted track has no entries!");
				return results;
			}
		}
		// Else, we have restrictions on the period to work with
		else {
			// Get everything?! How is that different with the previous approach???
			fullTrackContent = extractor.getTrackContent(entityId, frequency,allowNonEOPEstimated);
		}
		
		// Keep the last track date in memory
		if (toDate == null) {
			toDate = fullTrackContent.getStopDate();
		}
		
		// If track first and last dates are still undefined, it's an error
		 if (fromDate == null || toDate == null) {
			log.error("Computation arguments are invalid!");
			return results;
		}
		
		fromDate = DateUtil.roundToDay(fromDate);
		toDate = DateUtil.roundToDay(toDate);
		
		Date workingDate = fromDate;
		log.debug("Non-benchmark related statistics - Initialization done in " + (System.currentTimeMillis()-start) + "ms.");
		TimeSerie cachedMonthlyContent = extractor.getTrackContent(entityId, ComputationFrequency.DAILY,allowNonEOPEstimated);
		
		while (workingDate.before(toDate) || workingDate.equals(toDate)) {
			// Preparing data
			start = System.currentTimeMillis();
			TimeSerie workingTrackContent = fullTrackContent.truncateAfter(workingDate);
			TimeSerie workingTrackMonthlyContent = null;
			if (frequency!=ComputationFrequency.MONTHLY) {
				workingTrackMonthlyContent = extractor.getMonthlyTrackFromDaily(cachedMonthlyContent, workingDate);
			} else {
				workingTrackMonthlyContent = workingTrackContent;
			}
			TimeSeries workingSeries = new TimeSeries();
			AdditionalEntityInformation additionalInfo = extractor.getAdditionalInformation(entityId, frequency,workingDate);
			workingSeries.addTimeSerie(workingTrackContent);
			if (workingTrackContent.getDatesAsList().size()>2 && workingTrackMonthlyContent.getDatesAsList().size()>2) {
				TimeSeries workingMonthlySeries = new TimeSeries();
				workingMonthlySeries.addTimeSerie(workingTrackMonthlyContent);
				// Initialize statistic item
				ComputedStatistics statistic = new ComputedStatistics();
				statistic.setApplicationDate(workingDate);
				statistic.setTargetEntityId(entityId);
				statistic.setTargetEntityType(type);
				statistic.setComputationDate(new Date());
				statistic.setLastReturnDate(workingTrackContent.getStopDate());
				statistic.setFrequency(frequency);
				// Setting statistical values
				statistic.setNumberOfMonths(workingTrackContent.getDatesAsList().size());
				
				// Set last return (the last value of our track content)
				statistic.setLastReturn(workingTrackContent.getLastValue().getValue());
				
				if (workingTrackContent.getLastValue().isFinalStatus() && workingTrackContent.getLastValue().getDate().equals(workingDate)) {
					statistic.setStatus(QualityStatus.FINAL);
				} else {
					statistic.setStatus(QualityStatus.ESTIMATED);
				}
				log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Data prepared in " + (System.currentTimeMillis()-start) + "ms.");
				start = System.currentTimeMillis();
				// Total and yearly returns
				StatisticsValueComputer.computeTotalReturns(statistic, workingSeries, workingMonthlySeries);
				StatisticsValueComputer.computePreviousYearToDate(statistic, workingSeries, workingMonthlySeries);
				log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Total returns computed in " + (System.currentTimeMillis()-start) + "ms.");
				start = System.currentTimeMillis();
				// Standard Deviations
				StatisticsValueComputer.computeStandardDeviations(statistic, workingSeries, workingMonthlySeries);
				log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Deviations computed in " + (System.currentTimeMillis()-start) + "ms.");
				start = System.currentTimeMillis();
				// Annualized returns
				StatisticsValueComputer.computeAnnualizedReturns(statistic, workingSeries, workingMonthlySeries,true);
				log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Annualized returns computed in " + (System.currentTimeMillis()-start) + "ms.");
				start = System.currentTimeMillis();			
				// Drawdowns
				StatisticsValueComputer.computeDrawdowns(statistic, workingSeries, workingMonthlySeries);
				log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Drawdowns computed in " + (System.currentTimeMillis()-start) + "ms.");
				start = System.currentTimeMillis();
				// Average values
				StatisticsValueComputer.computeAverageValues(statistic, workingSeries, workingMonthlySeries);
				log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Average values computed in " + (System.currentTimeMillis()-start) + "ms.");
				start = System.currentTimeMillis();
				// Ratio values
				StatisticsValueComputer.computeRatioValues(statistic, workingSeries, workingMonthlySeries);
				log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Ratio values computed in " + (System.currentTimeMillis()-start) + "ms.");
				start = System.currentTimeMillis();			
				// CVaR*, VaR* and Omega values
				StatisticsValueComputer.computeRiskValues(statistic, workingSeries, workingMonthlySeries);
				log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Risk values computed in " + (System.currentTimeMillis()-start) + "ms.");

				// Computing statistics from additional information
				StatisticsValueComputer.computeWithAdditionalInformation(statistic,additionalInfo);
				
				// Add to collection
				results.add(statistic);
				
			}
			
			if (workingDate.equals(toDate)) {
				break;
			}
			
			workingDate = DatesComputer.getNextValidDate(workingDate, frequency);
			
			if (allowNonEOPEstimated && workingDate.after(toDate)) {
				workingDate = toDate;
			}
			
		}
		
		log.info("Non-benchmark related statistics - " + type + " with id [" + entityId + "] statistics computed in " + (System.currentTimeMillis()-fullStart) + "ms.");
		return results;
	}
	
	/**
	 * Computes and returns <B>benchmark</B> related statistics of the given entity (defined by its <code>entityId</code> and its <code>type</code>) for all dates between the <code>fromDate</code> and the <code>toDate</code> that match the given <code>frequency</code>.<BR/>
	 * 
	 * <B>WARNING: Working on ANY FREQUENCY, however the DST library has not been tested with other frequencies</B>
	 * 
	 * @param fromDate The start date (included)
	 * @param toDate The to date (included)
	 * @param frequency The computation frequency
	 * @param entityId The id of the target entity
	 * @param type The type of the target entity
	 * @return A collection of statistics
	 */
	public static List<IStatistics>  computeBenchmarkStatisticsSet(Date fromDate, Date toDate, ComputationFrequency frequency, String entityId, EntityType type, boolean allowNonEOPEstimated) {
		log.info("Benchmark related statistics - Working on a " + type + " with id [" + entityId + "] - From:" + DateFormatter.toInputString(fromDate) + " To:" + DateFormatter.toInputString(toDate));
		long fullStart = System.currentTimeMillis();
		long start = System.currentTimeMillis();
		ArrayList<IStatistics> results = new ArrayList<IStatistics>();
		
		if (fromDate!=null) {
			fromDate = DateUtil.roundToDay(fromDate);
		}
		if (toDate!=null) {
			toDate = DateUtil.roundToDay(toDate);
		}
		TrackContentExtractor extractor = TrackContentExtractorFactory.getTrackContentExtractor(type);
		
		TimeSerie fullTrackContent = null;
		if (fromDate==null && toDate==null) {
			fullTrackContent = extractor.getTrackContent(entityId, frequency,allowNonEOPEstimated);
			if (fullTrackContent.getStartDate()!=null) {
				fromDate = DateUtil.roundToDay(fullTrackContent.getStartDate());
				toDate = DateUtil.roundToDay(fullTrackContent.getStopDate());
			} else {
				log.error("The extracted track has no entries!");
				return results;
			}
		} else {
			fullTrackContent = extractor.getTrackContent(entityId, frequency,allowNonEOPEstimated);
		}
		
		if (toDate==null) {
			toDate = fullTrackContent.getStopDate();
		}
		
		if (fromDate==null || toDate==null) {
				log.error("Computation arguments are invalid!");
				return results;
		}
		
		HashMap<BenchmarkDescriptor, TimeSerie> benchmarkTracks = extractor.getBenchmarkTracks(entityId,fullTrackContent.getStartDate(),toDate,frequency,allowNonEOPEstimated);
		BenchmarkDescriptor entityDescriptor = new BenchmarkDescriptor();
		entityDescriptor.setId(entityId);
		entityDescriptor.setRfr(false);
		entityDescriptor.setName("Current entity");
		benchmarkTracks.put(entityDescriptor, fullTrackContent);
		BenchmarkDescriptor rfrBenchmark = null;
		for (BenchmarkDescriptor benchmark : benchmarkTracks.keySet()) {
			if (benchmark.getRfr()) {
				rfrBenchmark = benchmark;
			}
		}
		if (rfrBenchmark==null) {
			log.info("RFR benchmark is not available!");
		} else {
			log.info("Using " + rfrBenchmark.getName() + " as RFR benchmark!");
		}
		
		Date workingDate = fromDate;
		
		log.debug("Benchmark related statistics - Initialization done in " + (System.currentTimeMillis()-start) + "ms.");
		while (workingDate.before(toDate) || workingDate.equals(toDate)) {
			// Preparing data
			start = System.currentTimeMillis();
			TimeSerie relatedWorkingTrackContent = fullTrackContent.truncateAfter(workingDate);
			TimeSeries relatedWorkingSeries = new TimeSeries();
			relatedWorkingSeries.addTimeSerie(relatedWorkingTrackContent);
			QualityStatus overallStatus = QualityStatus.FINAL;
			if (relatedWorkingTrackContent.getDatesAsList().size()>2) {
				for (BenchmarkDescriptor benchmark : benchmarkTracks.keySet()) {
					start = System.currentTimeMillis();
					// Initialize statistic item
					BenchmarkRelatedStatistics brs = new BenchmarkRelatedStatistics();
					brs.setTargetEntityId(benchmark.getId());
					brs.setTargetEntityType(EntityType.INDEX);
					brs.setRiskFreeBenchmark(benchmark.getRfr());
					brs.setRelatedEntityType(type);
					brs.setRelatedEntityId(entityId);
					brs.setApplicationDate(workingDate);
					brs.setComputationDate(new Date());
	
					// Prepare benchmark data
					TimeSerie benchWorkingTrackContent = benchmarkTracks.get(benchmark).truncateAfter(workingDate).truncateBefore(relatedWorkingTrackContent.getStartDate());
					if (isInvalid(fullTrackContent, benchWorkingTrackContent) && benchWorkingTrackContent.getDatesAsList().size()>0) {
						relatedWorkingSeries = new TimeSeries();
						relatedWorkingSeries.addTimeSerie(relatedWorkingTrackContent.truncateBefore(benchWorkingTrackContent.getStartDate()));
					}

					if (benchWorkingTrackContent.getDatesAsList().size()>0 ) {
						TimeSeries benchWorkingSeries = new TimeSeries();
						benchWorkingSeries.addTimeSerie(benchWorkingTrackContent);
						
						// Prepare RFR data
						TimeSerie rfrWorkingTrackContent = null;
						TimeSeries rfrWorkingSeries = null;
						if (rfrBenchmark!=null) {
							rfrWorkingTrackContent = benchmarkTracks.get(rfrBenchmark).truncateAfter(workingDate).truncateBefore(relatedWorkingTrackContent.getStartDate());
							rfrWorkingSeries = new TimeSeries();
							rfrWorkingSeries.addTimeSerie(rfrWorkingTrackContent);
						}
		
						if (benchWorkingTrackContent.getLastValue().isFinalStatus() && benchWorkingTrackContent.getLastValue().getDate().equals(workingDate)) {
							brs.setStatus(QualityStatus.FINAL);
						} else {
							overallStatus = QualityStatus.ESTIMATED;
							brs.setStatus(QualityStatus.ESTIMATED);
						}
						
						brs.setFrequency(frequency);
						
						TimeSerie correlationTS = relatedWorkingSeries.getCorrelationCoefficient(benchWorkingSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
						TimeSerie betaTS = relatedWorkingSeries.getBeta(benchWorkingSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
						
						log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Data initialized in " + (System.currentTimeMillis()-start) + "ms.");
						
						
						start = System.currentTimeMillis();
						StatisticsValueComputer.computeAlphaAndBetaValues(brs, relatedWorkingSeries,benchWorkingSeries, rfrWorkingSeries, betaTS, correlationTS);
						log.debug("\t" + DateFormatter.toInputString(workingDate) + " - ALPHA & BETA values computed in " + (System.currentTimeMillis()-start) + "ms.");
						start = System.currentTimeMillis();
						StatisticsValueComputer.computeRatios(brs, relatedWorkingSeries,benchWorkingSeries, rfrWorkingSeries, betaTS, correlationTS);
						log.debug("\t" + DateFormatter.toInputString(workingDate) + " - RATIOS computed in " + (System.currentTimeMillis()-start) + "ms.");
						start = System.currentTimeMillis();
						StatisticsValueComputer.computeReturns(brs, relatedWorkingSeries,benchWorkingSeries, rfrWorkingSeries, betaTS, correlationTS);
						log.debug("\t" + DateFormatter.toInputString(workingDate) + " - RETURNS computed in " + (System.currentTimeMillis()-start) + "ms.");
						start = System.currentTimeMillis();
						StatisticsValueComputer.computeUpValues(brs, relatedWorkingSeries,benchWorkingSeries, rfrWorkingSeries, betaTS, correlationTS);
						log.debug("\t" + DateFormatter.toInputString(workingDate) + " - UP and GAIN values computed in " + (System.currentTimeMillis()-start) + "ms.");
						start = System.currentTimeMillis();
						StatisticsValueComputer.computeDownValues(brs, relatedWorkingSeries,benchWorkingSeries, rfrWorkingSeries, betaTS, correlationTS);
						log.debug("\t" + DateFormatter.toInputString(workingDate) + " - DOWN and LOSS values computed in " + (System.currentTimeMillis()-start) + "ms.");
						start = System.currentTimeMillis();
						StatisticsValueComputer.computeRValues(brs, relatedWorkingSeries,benchWorkingSeries, rfrWorkingSeries, betaTS, correlationTS);
						log.debug("\t" + DateFormatter.toInputString(workingDate) + " - R values computed in " + (System.currentTimeMillis()-start) + "ms.");
						start = System.currentTimeMillis();
						StatisticsValueComputer.computeRFRValues(brs, relatedWorkingSeries,benchWorkingSeries, rfrWorkingSeries, betaTS, correlationTS,true);
						log.debug("\t" + DateFormatter.toInputString(workingDate) + " - RFR values computed in " + (System.currentTimeMillis()-start) + "ms.");
						start = System.currentTimeMillis();
						StatisticsValueComputer.computeVariousValues(brs, relatedWorkingSeries,benchWorkingSeries, rfrWorkingSeries, betaTS, correlationTS);
						log.debug("\t" + DateFormatter.toInputString(workingDate) + " - VARIOUS values computed in " + (System.currentTimeMillis()-start) + "ms.");
						start = System.currentTimeMillis();
						StatisticsValueComputer.computeDrawdowns(brs, benchWorkingSeries);
						log.debug("\t" + DateFormatter.toInputString(workingDate) + " - DRAWDOWNS values computed in " + (System.currentTimeMillis()-start) + "ms.");
						results.add(brs);
					}
				}
			}
			if (overallStatus==QualityStatus.ESTIMATED) {
				for (IStatistics stat : results) {
					BenchmarkRelatedStatistics brs = (BenchmarkRelatedStatistics)stat;
					if (brs.getApplicationDate().equals(workingDate)) {
						brs.setStatus(overallStatus);
					}
				}
			}
			
			if (workingDate.equals(toDate)) {
				break;
			}
			
			workingDate = DatesComputer.getNextValidDate(workingDate, frequency);
			
			if (allowNonEOPEstimated && workingDate.after(toDate)) {
				workingDate = toDate;
			}
		}
		
		log.info("Benchmark related statistics - " + type + " with id [" + entityId + "] statistics computed in " + (System.currentTimeMillis()-fullStart) + "ms.");
		return results;
	}

	/**
	 * Controls the integrity of the benchmark regarding its associated entity.<BR/>
	 * It returns <code>false</code> if a date present in the entity track is not present in the benchmark track.
	 * @param fullTrackContent
	 * @param benchmark
	 * @return
	 */
	private static boolean isInvalid(TimeSerie fullTrackContent,TimeSerie benchmark) {
		for (Date date : fullTrackContent.getDatesAsArray()) {
			if (benchmark.getValue(date)==null) {
				return true;
			}
		}
		return false;
	}
	
}