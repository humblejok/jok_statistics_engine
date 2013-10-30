package com.eim.utility.statistics.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.special.Gamma;
import org.apache.log4j.Logger;

import com.dst.timeserie.TimeSerie;
import com.dst.timeserie.TimeSerieIndicator;
import com.dst.timeserie.TimeSerieValue;
import com.dst.timeserie.TimeSeries;
import com.dst.timeserie.interpolator.InterpolatorFactory;
import com.dst.timeserie.window.LastValueFunction;
import com.dst.timeserie.window.WindowAnalysis;
import com.dst.timeserie.window.WindowAnalysisFunctionFactory;
import com.eim.util.date.DateUtil;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.DatesComputer;
import com.eim.utility.common.utility.DoublesArrayLoader;
import com.eim.utility.statistics.model.AdditionalEntityInformation;
import com.eim.utility.statistics.model.BenchmarkRelatedStatistics;
import com.eim.utility.statistics.model.ComputedStatistics;

/**
 * Utility class that handles the assignments of statistics to the statistics objects but also provides a set computation methods.
 * @author sdj
 *
 */
public class StatisticsValueComputer {
	
	/**
	 * Logging utility
	 */
	private static Logger log = Logger.getLogger(StatisticsValueComputer.class);
	
	/**
	 * Array that defines the list of annualized returns that need to be computed.
	 */
	public static Integer [] ANNUALIZED_RETURNS_MONTHS = new Integer [] {18,24,36,60};
	
	/**
	 * The pre-computation start range for STUDENT T DISTRIBUTION
	 */
	public static int PRECOMPUTATION_START_RANGE = 3;
	/**
	 * The pre-computation end range for STUDENT T DISTRIBUTION
	 */
	public static int PRECOMPUTATION_END_RANGE = 360;
	
	/**
	 * Pre-computed value of 12^1/2
	 */
	public static final double SQRT12 = 3.4641016151377545870548926830117d;
	
	/**
	 * Initial value for temporary NAV computations 
	 */
	public static final double START_VALUE = 100.0d;
	
	/**
	 * Number of months per year (double format)
	 */
	public static final double MONTHS_PER_YEAR = 12.0d;
	
	/**
	 * The ratio representing a month compared to a year
	 */
	public static final double YEAR_MONTHS_RATIO = 1.0d/MONTHS_PER_YEAR;
	
	/**
	 * Array that contain the precomputed gamma function results (gamma function is an extension of factorial function: http://en.wikipedia.org/wiki/Gamma_function )  
	 */
	public static final double [] PRECOMPUTED_GAMMAS = new double[PRECOMPUTATION_END_RANGE * 2];
	
	/**
	 * Array that contain the precomputed logGamma function results (gamma function is an extension of factorial function: http://en.wikipedia.org/wiki/Gamma_function )  
	 */
	public static final double [] PRECOMPUTED_LNGAMMAS = new double[PRECOMPUTATION_END_RANGE * 2];
	
	/**
	 * Array of precomputed constants for VaR and CVaR computation (used in the application of the Student T distribution)
	 */
	public static final double [] PRECOMPUTED_SKEWED_C = new double[PRECOMPUTATION_END_RANGE];
	
	/**
	 * Array of precomputed constants for VaR and CVaR computation (used in the application of the Student T distribution)
	 */
	public static final double [] PRECOMPUTED_SKEWED_LNC = new double[PRECOMPUTATION_END_RANGE];
	
	static {
		/*
		 * Initializing the gammas and constants 
		 */
		for (int i=0;i<PRECOMPUTATION_END_RANGE * 2;i++) {
			PRECOMPUTED_LNGAMMAS[i] = Gamma.logGamma(new Integer(i).doubleValue()/2.0d);
			PRECOMPUTED_GAMMAS[i] = i%2==0?Math.rint(Math.exp(PRECOMPUTED_LNGAMMAS[i])):Math.exp(PRECOMPUTED_LNGAMMAS[i]);
		}
		for (int i=PRECOMPUTATION_START_RANGE;i<PRECOMPUTATION_END_RANGE;i++) {
			double SQRT_PI = Math.PI * new Integer(i-2).doubleValue();
			PRECOMPUTED_SKEWED_C[i] = PRECOMPUTED_GAMMAS[i+1] / (Math.sqrt(SQRT_PI) * PRECOMPUTED_GAMMAS[i]);
			PRECOMPUTED_SKEWED_LNC[i] = PRECOMPUTED_LNGAMMAS[i+1] - PRECOMPUTED_LNGAMMAS[i] - 0.5d * Math.log(SQRT_PI);
		}
	}
	
	/**
	 * Computes and adds to the <B>non-benchmark</B> related statistic the following values:<BR/>
	 * <ul>
	 * <li>Current year to date performance</li>
	 * <li>Current total return</li>
	 * <li>Last 3 months total return</li>
	 * <li>Last 6 months total return</li>
	 * <li>Last 12 months total return</li>
	 * <li>12 months rolling average</li>
	 * <li>12 months rolling deviation</li>
	 * <li>Month to date</li>
	 * <li>Quarter to date</li>
	 * <li>Semester to date</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param workingMonthlySeries The monthly DST TimeSeries 
	 */
	public static void computeTotalReturns(ComputedStatistics statistic, TimeSeries workingSeries, TimeSeries workingMonthlySeries) {
		if (workingMonthlySeries.getDates().size()>0) {
			Double[] computeTotalReturn = computeTotalReturn(workingMonthlySeries.getTimeSerie(TimeSerieIndicator.RELATIVERETURN));
			if (computeTotalReturn.length>0) {
				statistic.setTotalReturn(computeTotalReturn[computeTotalReturn.length-1]);
			}
			statistic.setYearToDate(workingMonthlySeries.getYearToDate().getLastValue().getValue());
			statistic.setMonthToDate(workingMonthlySeries.getMonthlyReturn().getLastValue().getValue());
			statistic.setQuarterToDate(workingMonthlySeries.getQuarterReturn().getLastValue().getValue());
			Date lastDate = (Date) workingMonthlySeries.getDates().get(workingMonthlySeries.getDates().size()-1);
			Calendar cal = Calendar.getInstance();
			cal.setTime(lastDate);
			int currentMonth = cal.get(Calendar.MONTH);
			statistic.setSemesterToDate(workingMonthlySeries.getLastnMonthReturn((currentMonth%6) + 1));
			// TODO: Could be refactored and optimized using a loop
			if (workingMonthlySeries.getDates().size()>=3) {
				statistic.setLast3MonthsTotalReturn(workingMonthlySeries.getLastnMonthReturn(3));
				
				if (workingMonthlySeries.getDates().size()>=6) {
					statistic.setLast6MonthsTotalReturn(workingMonthlySeries.getLastnMonthReturn(6));
				
					if (workingMonthlySeries.getDates().size()>=12) {
						statistic.setLast12MonthsTotalReturn(workingMonthlySeries.getLastnMonthReturn(12));
						
						// Compute 12 months rolling average
						statistic.setRollingAverage12(workingMonthlySeries.getRollingAverage12().getLastValue().getValue());
						// Compute 12 months rolling deviation
						statistic.setRollingDeviation12(workingMonthlySeries.getRollingDeviation12().getLastValue().getValue());
						
						if (workingMonthlySeries.getDates().size()>=18) {
							statistic.setLast18MonthsTotalReturn(workingMonthlySeries.getLastnMonthReturn(18));
							if (workingMonthlySeries.getDates().size()>=24) {
								statistic.setLast24MonthsTotalReturn(workingMonthlySeries.getLastnMonthReturn(24));
								if (workingMonthlySeries.getDates().size()>=36) {
									statistic.setLast36MonthsTotalReturn(workingMonthlySeries.getLastnMonthReturn(36));
									
									statistic.setRollingExcessKurtosis36(computeRolling(workingMonthlySeries, 36, TimeSerieIndicator.KURTOSIS).getLastValue().getValue());
									statistic.setRollingSkewness36(computeRolling(workingMonthlySeries, 36, TimeSerieIndicator.SKEWNESS).getLastValue().getValue());
									if (workingMonthlySeries.getDates().size()>=60) {
										statistic.setLast60MonthsTotalReturn(workingMonthlySeries.getLastnMonthReturn(60));
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Compute Rolling thanks to DST WindowAnalysis API
	 * @param tss the given time series from which to compute
	 * @param nbMonths the number of month of the window
	 * @param tsIndicator the type of wanted indicator {@link TimeSerieIndicator}
	 * @return the result time serie
	 */
	private static TimeSerie computeRolling(TimeSeries tss, int nbMonths, int tsIndicator) {
	    TimeSerie ts = tss.getRelativeReturn().getEndOfMonthValues();
	    return WindowAnalysis.Calculate(ts, nbMonths, tsIndicator, 
		    -1, // no indicator is currently expected
		    false,
		    WindowAnalysisFunctionFactory.getSingleton().getFunction( LastValueFunction.KEY ),
		    WindowAnalysisFunctionFactory.getSingleton().getFunction( LastValueFunction.KEY ) ); // function to process the serie on each step (last value for our case)
	}
	
	/**
	 * Computes and adds to the <B>non-benchmark</B> related statistic the following values:<BR/>
	 * <ul>
	 * <li>Year-1 year to date</li>
	 * <li>Year-2 year to date</li>
	 * <li>Year-3 year to date</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param workingMonthlySeries The monthly DST TimeSeries 
	 */
	public static void computePreviousYearToDate(ComputedStatistics statistic, TimeSeries workingSeries, TimeSeries workingMonthlySeries) {
		if (workingMonthlySeries.getDates().size()>=12) {
			Date applicationDate = statistic.getApplicationDate();
			Double [] year_n = new Double[] {Double.NaN,Double.NaN,Double.NaN};
			for (int i=1;i<=3;i++) {
				Date yearStart = null;
				Date yearEnd = applicationDate;
				for (int j=0;j<i;j++) {
					yearEnd = DateUtil.addOrRemoveDays(DateUtil.getBeginOfYear(yearEnd),-1);
					yearStart = DateUtil.getBeginOfYear(yearEnd);
				}
				TimeSerie previousYear = workingMonthlySeries.getMonthlyReturn().truncateBefore(yearStart).truncateAfter(yearEnd);
				if (previousYear.getDatesAsArray().length>=12) {
					TimeSeries yearSeries = new TimeSeries();
					yearSeries.addTimeSerie(previousYear);
					year_n[i-1] = yearSeries.getYearToDate().getLastValue().getValue();
				}
			}
			statistic.setYearToDateY_1(year_n[0]);
			statistic.setYearToDateY_2(year_n[1]);
			statistic.setYearToDateY_3(year_n[2]);
		}
	}
	
	/**
	 * Computes and adds to the <B>non-benchmark</B> related statistic the following values:<BR/>
	 * <ul>
	 * <li>Current annualized standard deviation</li>
	 * <li>Current annualized loss deviation</li>
	 * <li>Last 12 months annualized standard deviation</li>
	 * <li>Last 24 months annualized standard deviation</li>
	 * <li>Last 36 months annualized standard deviation</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param workingMonthlySeries The monthly DST TimeSeries 
	 */
	public static void computeStandardDeviations(ComputedStatistics statistic, TimeSeries workingSeries, TimeSeries workingMonthlySeries) {
		if (workingMonthlySeries.getDates().size()>0) {
			
			//statistic.setAnnualizedStandardDeviation(workingMonthlySeries.getAnnualizedStandardDeviation().getLastValue().getValue());
			statistic.setAnnualizedStandardDeviation(workingSeries.getStandardDeviation().getLastValue().getValue() * SQRT12);
			statistic.setAnnualizedLossDeviation(StatisticsValueComputer.annualizeValue(workingMonthlySeries.getLossStandardDeviation().getLastValue().getValue()));
			// TODO: Could be refactored and optimized using a loop
			if (workingMonthlySeries.getDates().size()>=12) {
				statistic.setLast12MonthsAnnualizedStandardDeviation(StatisticsValueComputer.getAnnualizedStandardDeviation(12,workingMonthlySeries));
				if (workingMonthlySeries.getDates().size()>=24) {
					statistic.setLast24MonthsAnnualizedStandardDeviation(StatisticsValueComputer.getAnnualizedStandardDeviation(24,workingMonthlySeries));
					if (workingMonthlySeries.getDates().size()>=36) {
						statistic.setLast36MonthsAnnualizedStandardDeviation(StatisticsValueComputer.getAnnualizedStandardDeviation(36,workingMonthlySeries));
					}
				}
			}
		}
	}
	
	/**
	 * Computes and adds to the <B>non-benchmark</B> related statistic the following values:<BR/>
	 * <ul>
	 * <li>Current annualized return</li>
	 * <li>Last 18 months annualized return</li>
	 * <li>Last 24 months annualized return</li>
	 * <li>Last 36 months annualized return</li>
	 * <li>Last 60 months annualized return</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param workingMonthlySeries The monthly DST TimeSeries 
	 */
	public static void computeAnnualizedReturns(ComputedStatistics statistic, TimeSeries workingSeries, TimeSeries workingMonthlySeries, boolean fastMode) {
		if (fastMode) {
			HashMap<Integer, Double> computedAnnualizedReturns = StatisticsValueComputer.computeAnnualizedReturns(workingMonthlySeries.getTimeSerie(TimeSerieIndicator.RELATIVERETURN), ANNUALIZED_RETURNS_MONTHS);
			statistic.setAnnualizedReturn(computedAnnualizedReturns.get(0));
			statistic.setLast18MonthsAnnualizedReturn(computedAnnualizedReturns.get(18));
			statistic.setLast24MonthsAnnualizedReturn(computedAnnualizedReturns.get(24));
			statistic.setLast36MonthsAnnualizedReturn(computedAnnualizedReturns.get(36));
			statistic.setLast60MonthsAnnualizedReturn(computedAnnualizedReturns.get(60));
		} else {
			// TODO: Could be refactored and optimized using a loop
			if (workingMonthlySeries.getDates().size()>0) {
				statistic.setAnnualizedReturn(workingMonthlySeries.getAnnualizedReturn().getLastValue().getValue());
				if (workingMonthlySeries.getDates().size()>=18) {
					statistic.setLast18MonthsAnnualizedReturn(StatisticsValueComputer.getAnnualizedReturn(18, workingMonthlySeries));
					if (workingMonthlySeries.getDates().size()>=24) {
						statistic.setLast24MonthsAnnualizedReturn(StatisticsValueComputer.getAnnualizedReturn(24, workingMonthlySeries));
						if (workingMonthlySeries.getDates().size()>=36) {
							statistic.setLast36MonthsAnnualizedReturn(StatisticsValueComputer.getAnnualizedReturn(36, workingMonthlySeries));
							if (workingMonthlySeries.getDates().size()>=60) {		
								statistic.setLast60MonthsAnnualizedReturn(StatisticsValueComputer.getAnnualizedReturn(60, workingMonthlySeries));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Computes and adds to the <B>non-benchmark</B> related statistic the following values:<BR/>
	 * <ul>
	 * <li>Current drawdown</li>
	 * <li>Last 18 months maximum drawdown</li>
	 * <li>Maximum drawdown</li>
	 * <li>Length of maximum drawdown</li>
	 * <li>Is drawdown ongoing</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param workingMonthlySeries The monthly DST TimeSeries 
	 */
	public static void computeDrawdowns(ComputedStatistics statistic, TimeSeries workingSeries, TimeSeries workingMonthlySeries) {
		if (workingMonthlySeries.getDates().size()>0) {
			DrawdownDescriptor dd = getMaximumDrawdownLength(workingMonthlySeries.calcTimeSerie(TimeSerieIndicator.DRAWDOWN),workingMonthlySeries.getMaximumDrawDown());
			statistic.setCurrentDrawdown(dd.lastValue);
			statistic.setMaximumLengthOfDrawdown(dd.maximumLength);
			statistic.setDrawdownOngoing(dd.maximumOngoing?"true":"false");
			statistic.setMaximumDrawdown(-workingMonthlySeries.getMaximumDrawDown());
			if (workingMonthlySeries.getDates().size()>=18) {
				statistic.setLast18MonthsMaximumDrawdown(StatisticsValueComputer.getLargestDrawdown(18, workingMonthlySeries));
			}
		}
	}
	
	/**
	 * Computes and adds to the <B>non-benchmark</B> related statistic the following values:<BR/>
	 * <ul>
	 * <li>Current drawdown</li>
	 * <li>Last 18 months maximum drawdown</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param workingMonthlySeries The monthly DST TimeSeries 
	 */
	public static void computeDrawdowns(BenchmarkRelatedStatistics statistic, TimeSeries workingMonthlySeries) {
		if (workingMonthlySeries.getDates().size()>0) {
			DrawdownDescriptor dd = getMaximumDrawdownLength(workingMonthlySeries.calcTimeSerie(TimeSerieIndicator.DRAWDOWN),workingMonthlySeries.getMaximumDrawDown());
			statistic.setMaximumLengthOfDrawdown(dd.maximumLength);
			statistic.setDrawdownOngoing(dd.maximumOngoing?"true":"false");
			statistic.setMaximumDrawdown(-workingMonthlySeries.getMaximumDrawDown());
		}
	}
	
	/**
	 * Given list of values, it computes drawdown values list
	 * @param NAVs The list of NAVs
	 * @return The drawdown track
	 */
	public static List<Double> computeDrawdownTrack(List<Double> NAVs) {
		ArrayList<Double> drawdowns = new ArrayList<Double>();
		double peak = -99999.99;
		for (Double nav : NAVs) {
			if (nav>peak) {
				peak = nav;
				drawdowns.add(new Double(0.0d));
			} else {
				double dd = 100.0 * (nav-peak) / peak;
				drawdowns.add(dd);
			}
		}
		return drawdowns;
	}

	/**
	 * Computes a DrawdownDescriptor given a drawdown values <code>TimeSerie</code>.<BR/>
	 * Drawdown descriptor contains the following information:<BR/>
	 * <ul>
	 * <li>Maximum drawdown length</li>
	 * <li>Is maximum drawdown ongoing</li>
	 * <li>Last drawdown value</li>
	 * </ul>
	 * @param drawdownSerie The drawdown <code>TimeSerie</code>
	 * @return
	 */
	public static DrawdownDescriptor getMaximumDrawdownLength(TimeSerie drawdownSerie, double largestDrawdown) {
		DrawdownDescriptor dd = new DrawdownDescriptor();
		dd.lastValue = Double.NaN;
		dd.maximumLength = 0;
		dd.maximumOngoing = false;
		
		int currentDDLength = 0;
		boolean maximumFound = false;
		
		for (Date date : drawdownSerie.getDatesAsArray()) {
			dd.lastValue = drawdownSerie.getValue(date).getValue();
			if (dd.lastValue<0) {
				currentDDLength++;
				maximumFound = maximumFound || largestDrawdown==Math.abs(dd.lastValue);
				if (date.getTime()==drawdownSerie.getLastValue().getDate().getTime()) {
					if (currentDDLength>dd.maximumLength) {
						dd.maximumLength = currentDDLength;
						dd.maximumOngoing = true;
					}
				}
			} else {
				if (currentDDLength>dd.maximumLength && maximumFound) {
					dd.maximumLength = currentDDLength;
					dd.maximumOngoing = false;
				}
				maximumFound = false;
				currentDDLength = 0;
			}
		}
		return dd;
	}
	
	/**
	 * Object class that defines a drawdown descriptor
	 * @author sdj
	 *
	 */
	public static class DrawdownDescriptor {
		public Double lastValue;
		public Integer longestDrawdown;
		public Integer maximumLength;
		public boolean maximumOngoing;
	}
	
	/**
	 * Computes and adds to the <B>non-benchmark</B> related statistic the following values:<BR/>
	 * <ul>
	 * <li>Current average gain</li>
	 * <li>Current average loss</li>
	 * <li>Current average rate of return</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param workingMonthlySeries The monthly DST TimeSeries 
	 */
	public static void computeAverageValues(ComputedStatistics statistic, TimeSeries workingSeries, TimeSeries workingMonthlySeries) {
		
		if (workingMonthlySeries.getDates().size()>0) {
			statistic.setAverageGain(workingMonthlySeries.getAverageGain().getLastValue().getValue());
		
			statistic.setAverageLoss(workingMonthlySeries.getAverageLoss().getLastValue().getValue());
		
			statistic.setAverageRateOfReturn(workingMonthlySeries.getAverageReturn().getLastValue().getValue());
			
		}
	}
	
	/**
	 * Computes and adds to the <B>non-benchmark</B> related statistic the following values:<BR/>
	 * <ul>
	 * <li>Current percentage of positive months</li>
	 * <li>Current skewness ratio</li>
	 * <li>Current kurtosis ratio</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param workingMonthlySeries The monthly DST TimeSeries 
	 */
	public static void computeRatioValues(ComputedStatistics statistic, TimeSeries workingSeries, TimeSeries workingMonthlySeries) {
		if (workingMonthlySeries.getDates().size()>0 && workingMonthlySeries.getDates().size()>0) {
			statistic.setPercentageOfPositiveMonths(StatisticsValueComputer.getPercentageOfPositiveMonths(workingMonthlySeries));
		
			statistic.setSkewness(workingMonthlySeries.getSkewness().getLastValue().getValue());
		
			statistic.setKurtosis(workingMonthlySeries.getKurtosis().getLastValue().getValue());
		}
	}
	
	/**
	 * Computes and adds to the <B>non-benchmark</B> related statistics the following values:<BR/>
	 * <ul>
	 * <li>CVaR 11/12</li>
	 * <li>CVaR 11/12 simulated</li>
	 * <li>VaR 11/12 simulated</li>
	 * <li>VaR 95 simulated</li>
	 * <li>VaR 99 simulated</li>
	 * <li>Omega 10 since inception</li>
	 * <li>Omega 10 over 36 months</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param workingMonthlySeries The monthly DST TimeSeries 
	 */
	public static void computeRiskValues(ComputedStatistics statistic, TimeSeries workingSeries, TimeSeries workingMonthlySeries) {
		TimeSerie timeSerie = workingMonthlySeries.getTimeSerie(TimeSerieIndicator.RELATIVERETURN);
		statistic.setOmega10SinceInception(StatisticsValueComputer.computeOmega(timeSerie, 0, 0.10d));
		statistic.setOmega10Over36Months(StatisticsValueComputer.computeOmega(timeSerie, 36, 0.10d));
		if (workingMonthlySeries.getDates().size()>12) {
			double[] vector = timeSerie.getValuesAsdoubles(true);
			double[] valuesAtRisk = StatisticsValueComputer.computeValuesAtRisk(vector, 0, vector.length);
			
			statistic.setCvar_11_12(valuesAtRisk[0]);
			statistic.setCvar_11_12_simulated(valuesAtRisk[1]);
			statistic.setVar_11_12_simulated(valuesAtRisk[2]);
			statistic.setVar_95_simulated(valuesAtRisk[3]);
			statistic.setVar_99_simulated(valuesAtRisk[4]);
		}
	}
	
	/**
	 * Computes and adds to the <B>benchmark</B> related statistics the following values:<BR/>
	 * <ul>
	 * <li>Down capture</li>
	 * <li>Down percentage ratio</li>
	 * <li>Average monthly loss</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param benchmarkSeries The complete DST TimeSeries of the benchmark
	 * @param rfrSeries The complete DST TimeSeries of the RFR benchmark
	 * @param betaTS The complete DST TimeSerie of betas
	 * @param correlationTS The complete DST TimeSerie of correlations
	 */
	public static void computeDownValues(BenchmarkRelatedStatistics statistic, TimeSeries workingSeries, TimeSeries benchmarkSeries, TimeSeries rfrSeries, TimeSerie betaTS, TimeSerie correlationTS) {
		TimeSerie workTS = workingSeries.getDownCapture(benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setDownCapture(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [DOWN CAPTURE] on entity");
		}
		
		workTS = workingSeries.getDownPercentageRatio(benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setDownPercentageRatio(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [DOWN PERCENTAGE RATIO] on entity");
		}
		
		workTS = benchmarkSeries.getAverageLoss();
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setAverageMonthlyLoss(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [AVERAGE MONTHLY LOSS] on entity");
		}
	}
	
	/**
	 * Computes and adds to the <B>benchmark</B> related statistics the following values:<BR/>
	 * <ul>
	 * <li>Up capture</li>
	 * <li>Up percentage ratio</li>
	 * <li>Average monthly gain</li>
	 * <li>Percentage gain ratio</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param benchmarkSeries The complete DST TimeSeries of the benchmark
	 * @param rfrSeries The complete DST TimeSeries of the RFR benchmark
	 * @param betaTS The complete DST TimeSerie of betas
	 * @param correlationTS The complete DST TimeSerie of correlations
	 */
	public static void computeUpValues(BenchmarkRelatedStatistics statistic, TimeSeries workingSeries, TimeSeries benchmarkSeries, TimeSeries rfrSeries, TimeSerie betaTS, TimeSerie correlationTS) {
		TimeSerie workTS = workingSeries.getPercentageGainRatio(benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setPercentageGainRatio(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [PERCENTAGE GAIN RATIO]");
		}
		workTS = workingSeries.getUpCapture(benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setUpCapture(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [UP CAPTURE]");
		}
		workTS = workingSeries.getUpPercentageRatio(benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setUpPercentageRatio(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [UP PERCENTAGE RATIO]");
		}
		workTS = benchmarkSeries.getAverageGain();
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setAverageMonthlyGain(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [AVERAGE MONTHLY GAIN] on entity");
		}
	}
	
	/**
	 * Computes and adds to the <B>benchmark</B> related statistics the following values:<BR/>
	 * <ul>
	 * <li>Information ratio</li>
	 * <li>Tracking error</li>
	 * <li>Active premium</li>
	 * <li>Annualized standard deviation</li>
	 * <li>Annualized volatility</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param benchmarkSeries The complete DST TimeSeries of the benchmark
	 * @param rfrSeries The complete DST TimeSeries of the RFR benchmark
	 * @param betaTS The complete DST TimeSerie of betas
	 * @param correlationTS The complete DST TimeSerie of correlations
	 */
	public static void computeVariousValues(BenchmarkRelatedStatistics statistic, TimeSeries workingSeries, TimeSeries benchmarkSeries, TimeSeries rfrSeries, TimeSerie betaTS, TimeSerie correlationTS) {
		TimeSerie workTS =  workingSeries.getInformationRatio(benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setInformationRatio(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [INFORMATION RATIO] on entity");
		}
		workTS = workingSeries.getTrackingError(benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setTrackingError(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [TRACKING ERROR] on entity");
		}
		workTS = workingSeries.getActivePremium(benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setActPremium(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [ACTIVE PREMIUM] on entity");
		}
		workTS = benchmarkSeries.getAnnualizedStandardDeviation();
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setAnnualizedStandardDeviation(workTS.getLastValue().getValue());
			// The volatility is the annualized standard deviation, this may need to be changed.
			statistic.setAnnualizedVolatility(statistic.getAnnualizedStandardDeviation());
		} else {
			log.debug("\t\tNot enought data to compute [ANNUALIZED STANDARD DEVIATION] and [ANNUALIZED VOLATILITY] on entity");
		}
	}
	
	/**
	 * Computes and adds to the <B>benchmark</B> related statistics the following values:<BR/>
	 * <ul>
	 * <li>Treynor ratio</li>
	 * <li>Positive months ratio</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param benchmarkSeries The complete DST TimeSeries of the benchmark
	 * @param rfrSeries The complete DST TimeSeries of the RFR benchmark
	 * @param betaTS The complete DST TimeSerie of betas
	 * @param correlationTS The complete DST TimeSerie of correlations
	 */
	public static void computeRatios(BenchmarkRelatedStatistics statistic, TimeSeries workingSeries, TimeSeries benchmarkSeries, TimeSeries rfrSeries, TimeSerie betaTS, TimeSerie correlationTS) {
		TimeSerie workTS =  workingSeries.getTreynorRatio(benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setTreynorRatio(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [TREYNOR RATIO] on entity");
		}
		if (benchmarkSeries!=null && benchmarkSeries.getDates().size()>0) {
			statistic.setPositiveMonthRatio(StatisticsValueComputer.getPercentageOfPositiveMonths(benchmarkSeries));
		}
	}

	/**
	 * Computes and adds to the <B>benchmark</B> related statistics the following values:<BR/>
	 * <ul>
	 * <li>Annualized return since related entity inception</li>
	 * <li>Relative return since related entity inception</li>
	 * <li>Average monthly rate of return</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param benchmarkSeries The complete DST TimeSeries of the benchmark
	 * @param rfrSeries The complete DST TimeSeries of the RFR benchmark
	 * @param betaTS The complete DST TimeSerie of betas
	 * @param correlationTS The complete DST TimeSerie of correlations
	 */
	public static void computeReturns(BenchmarkRelatedStatistics statistic, TimeSeries workingSeries, TimeSeries benchmarkSeries, TimeSeries rfrSeries, TimeSerie betaTS, TimeSerie correlationTS) {
		TimeSerie workTS =  benchmarkSeries.getAnnualizedReturn();
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setAnnualizedReturn(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [ANNUALIZED RETURN SINCE RELATED ENTITY INCEPTION] on entity");
		}
		workTS =  benchmarkSeries.getRelativeReturn();
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setTotalReturn(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [RELATIVE RETURN SINCE RELATED ENTITY INCEPTION] on entity");
		}
		workTS =  benchmarkSeries.getAverageReturn();
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setAverageMonthlyROR(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [AVERAGE MONTHLY ROR] on entity");
		}
	}
	
	/**
	 * Computes and adds to the <B>benchmark</B> related statistics the following values:<BR/>
	 * <ul>
	 * <li>Alpha</li>
	 * <li>Beta</li>
	 * <li>Annualized alpha</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param benchmarkSeries The complete DST TimeSeries of the benchmark
	 * @param rfrSeries The complete DST TimeSeries of the RFR benchmark
	 * @param betaTS The complete DST TimeSerie of betas
	 * @param correlationTS The complete DST TimeSerie of correlations
	 */
	public static void computeAlphaAndBetaValues(BenchmarkRelatedStatistics statistic, TimeSeries workingSeries, TimeSeries benchmarkSeries, TimeSeries rfrSeries, TimeSerie betaTS, TimeSerie correlationTS) {
		TimeSerie workTS = null;
		if (betaTS!=null && betaTS.getDatesAsList().size()>0) {
			workTS = workingSeries.getAlpha(betaTS, benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
			if (workTS!=null && workTS.getDatesAsList().size()>0) {
				statistic.setAlpha(workTS.getLastValue().getValue());
			} else {
				log.debug("\t\tNot enought BENCHMARK data to compute [ALPHA] on entity");
			}
		} else {
			log.debug("\t\tNot enought BETA data to compute [ALPHA] on entity");
		}
						
		workTS = workingSeries.getAnnualizedAlpha(benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setAnnualizedAlpha(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [ANNUALIZED ALPHA] on entity");
		}
		
		workTS = betaTS;
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setBeta(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [BETA] on entity");
		}
		
		workTS = workingSeries.RollingBench(TimeSerieIndicator.GetBenchTimeSerie(TimeSerieIndicator.ALPHA), 18, benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setLast18MonthsRollingAlpha(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [18 MONTHS ROLLING ALPHA] on entity");
		}
		
		workTS = workingSeries.RollingBench(TimeSerieIndicator.GetBenchTimeSerie(TimeSerieIndicator.BETA), 18, benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setLast18MonthsRollingBeta(workTS.getLastValue().getValue());
		} else {
			log.debug("\t\tNot enought data to compute [18 MONTHS ROLLING BETA] on entity");
		}
	}
	
	/**
	 * Computes and adds to the <B>benchmark</B> related statistics the following values (RFR based statistics):<BR/>
	 * <ul>
	 * <li>Annualized downside deviation</li>
	 * <li>Sortino ratio</li>
	 * <li>Sharpe ratio</li>
	 * <li>Jensen Alpha</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param benchmarkSeries The complete DST TimeSeries of the benchmark
	 * @param rfrSeries The complete DST TimeSeries of the RFR benchmark
	 * @param betaTS The complete DST TimeSerie of betas
	 * @param correlationTS The complete DST TimeSerie of correlations
	 * @param fast If <code>true</code> the computation engine is switched to the EIM one (faster)
	 */
	public static void computeRFRValues(BenchmarkRelatedStatistics statistic, TimeSeries workingSeries, TimeSeries benchmarkSeries, TimeSeries rfrSeries, TimeSerie betaTS, TimeSerie correlationTS, boolean fast) {
		if (rfrSeries!=null && rfrSeries.getTrackLength().getLastValue()!=null && rfrSeries.getTrackLength().getLastValue().getDate().equals(benchmarkSeries.getTrackLength().getLastValue().getDate())) {
			// This buffer is used for benchmarking purpose, Webfolio libraries is too slow...
			StringBuffer str = new StringBuffer();
			long start = System.currentTimeMillis();
			TimeSerie workTS = benchmarkSeries.getDownsideDeviation(rfrSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
			if (workTS!=null && workTS.getDatesAsList().size()>0) {
				statistic.setAnnualizedDownsideDeviationRFR(workTS.getLastValue().getValue() * StatisticsValueComputer.SQRT12);
			} else {
				log.debug("\t\tNot enought data to compute [ANNUALIZED DOWNSIDE DEVIATION] on entity");
			}
			str.append("ANNUALIZED DOWNSIDE DEVIATION: ").append(System.currentTimeMillis()-start).append("ms...");
			start = System.currentTimeMillis();
			workTS = benchmarkSeries.getSortinoRatio(rfrSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
			if (workTS!=null && workTS.getDatesAsList().size()>0) {
				statistic.setSortinoRatioRFR(workTS.getLastValue().getValue());
			} else {
				log.debug("\t\tNot enought data to compute [SORTINO RATIO] on entity");
			}
			str.append("SORTINO RATIO: ").append(System.currentTimeMillis()-start).append("ms...");
			start = System.currentTimeMillis();
			workTS = benchmarkSeries.getSharpeRatio(rfrSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
			if (workTS!=null && workTS.getDatesAsList().size()>0) {
				statistic.setSharpRatioRFR(workTS.getLastValue().getValue());
			} else {
				log.debug("\t\tNot enought data to compute [SHARPE RATIO] on entity]");
			}
			str.append("SHARPE RATIO: ").append(System.currentTimeMillis()-start).append("ms...");
			if (fast) {
				start = System.currentTimeMillis();
				Double myJensenAlpha = StatisticsValueComputer.getJensenAlpha(workingSeries, benchmarkSeries, rfrSeries, betaTS,true);
				statistic.setJensenAlpha(myJensenAlpha);
				str.append("JENSEN ALPHA: ").append(System.currentTimeMillis()-start).append("ms...");
			} else {
				start = System.currentTimeMillis();
				workTS = workingSeries.getJensenAlpha(benchmarkSeries, rfrSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
				if (workTS!=null && workTS.getDatesAsList().size()>0) {
					statistic.setJensenAlpha(workTS.getLastValue().getValue());
				} else {
					log.debug("\t\tNot enought data to compute [JENSEN ALPHA] on entity");
				}
				str.append("JENSEN ALPHA: ").append(System.currentTimeMillis()-start).append("ms...");
			}
		}
	}
	
	/**
	 * Computes and adds to the <B>benchmark</B> related statistics the following values:<BR/>
	 * <ul>
	 * <li>R</li>
	 * <li>R2</li>
	 * </ul>
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param benchmarkSeries The complete DST TimeSeries of the benchmark
	 * @param rfrSeries The complete DST TimeSeries of the RFR benchmark
	 * @param betaTS The complete DST TimeSerie of betas
	 * @param correlationTS The complete DST TimeSerie of correlations
	 */
	public static void computeRValues(BenchmarkRelatedStatistics statistic, TimeSeries workingSeries, TimeSeries benchmarkSeries, TimeSeries rfrSeries, TimeSerie betaTS, TimeSerie correlationTS) {
		TimeSerie workTS = correlationTS;
		if (workTS!=null && workTS.getDatesAsList().size()>0) {
			statistic.setR(workTS.getLastValue().getValue());
			workTS = workingSeries.getCoefficientOfDetermination(correlationTS, benchmarkSeries, InterpolatorFactory.SAMEMONTH_INTERPOLATOR);
			if (workTS!=null && workTS.getDatesAsList().size()>0) {
				statistic.setR2(workTS.getLastValue().getValue());
			} else {
				log.debug("\t\tNot enought data to compute [R2] on entity");
			}
		} else {
			log.debug("\t\tNot enought data to compute [R] and [R2] on entity");
		}
	}
	
	/**
	 * A method that computes the Jensen's alpha. It is called by the <code>computeRFRValues</code> method.<BR/>
	 * That method handles several computation mode. That mode is selected by the <code>webfolio</code> flag.<BR/>
	 * If <code>true</code>, the Jensen's Alpha is computed as in Webfolio (average gains).<BR/>
	 * If <code>false</code>, the Jensen's Alpha is computed as stated in financial handbooks (effective gains).
	 * It doesn't have any impact on the computation speed.<BR/>
	 * 
	 * @param statistic The statistic to update
	 * @param workingSeries The complete DST TimeSeries
	 * @param benchmarkSeries The complete DST TimeSeries of the benchmark
	 * @param rfrSeries The complete DST TimeSeries of the RFR benchmark
	 * @param betaTS The complete DST TimeSerie of betas
	 * @param webfolio Indicates which computation mode has to be used
	 * @return The Jensen's alpha value
	 */
	public static Double getJensenAlpha(TimeSeries workingSeries, TimeSeries benchmarkSeries, TimeSeries rfrSeries, TimeSerie betaTS, boolean webfolio) {
		Double jensenAlpha = Double.NaN;
		if (workingSeries!=null && benchmarkSeries!=null && rfrSeries!=null && betaTS!=null && workingSeries.getDates().size()>0 && benchmarkSeries.getDates().size()>0 && rfrSeries.getDates().size()>0 && betaTS.getTrackLength()>0) {
			ArrayList<TimeSerie> timeseries = new ArrayList<TimeSerie>();
			timeseries.add(workingSeries.getTimeSerie(TimeSerieIndicator.RELATIVERETURN));
			timeseries.add(benchmarkSeries.getTimeSerie(TimeSerieIndicator.RELATIVERETURN));
			timeseries.add(rfrSeries.getTimeSerie(TimeSerieIndicator.RELATIVERETURN));
			List<Double> returnsFromPerformances = null;
			if (webfolio) {
				returnsFromPerformances = getAverageGainFromPerformances(timeseries);
			} else {
				returnsFromPerformances = getReturnsFromPerformances(timeseries);
			}
			jensenAlpha = returnsFromPerformances.get(0) - (returnsFromPerformances.get(2) + betaTS.getLastValue().getValue() * (returnsFromPerformances.get(1) - returnsFromPerformances.get(2)));
		} else {
			log.debug("\t\tNot enought data to compute [MY JENSEN ALPHA] on entity");
		}
		return jensenAlpha;
	}
	
	/**
	 * Method used by the Jensen's alpha computation method when called in Webfolio mode.
	 * @param timeseries The list of timeseries to use
	 * @return The list of average gains from performances.
	 */
	public static List<Double> getAverageGainFromPerformances(List<TimeSerie> timeseries) {
		ArrayList<Double> results = new ArrayList<Double>();
		
		for (int i = 0;i<timeseries.size();i++) {
			results.add(0.0d);
		}
		TimeSerie referenceTS = timeseries.get(0);
		for (Date date : referenceTS.getDatesAsArray()) {
			for (int i = 0;i<timeseries.size();i++) {
				if (timeseries.get(i).getValue(date)!=null) {
					results.set(i, results.get(i)+timeseries.get(i).getValue(date).getValue());
				} else {
					results.set(i, Double.NaN);
				}
			}
		}
		for (int i = 0;i<timeseries.size();i++) {
			results.set(i,results.get(i) / referenceTS.getDatesAsArray().length);
		}
		return results;
	}
	
	/**
	 * Method used by the Jensen's alpha computation method when not called in Webfolio mode.
	 * @param timeseries The list of timeseries to use
	 * @return The list of effective returns from performances.
	 */
	public static List<Double> getReturnsFromPerformances(List<TimeSerie> timeseries) {
		ArrayList<Double> results = new ArrayList<Double>();
		
		for (int i = 0;i<timeseries.size();i++) {
			results.add(START_VALUE);
		}
		TimeSerie referenceTS = timeseries.get(0);
		for (Date date : referenceTS.getDatesAsArray()) {
			for (int i = 0;i<timeseries.size();i++) {
				results.set(i, (results.get(i)*timeseries.get(i).getValue(date).getValue()) + results.get(i));
			}
		}
		for (int i = 0;i<timeseries.size();i++) {
			results.set(i,(results.get(i) - START_VALUE) / START_VALUE);
		}
		return results;
	}
	
	/**
	 * Computes the standard deviation over the last <code>months</code> months.
	 * @param months Number of months
	 * @param monthlySeries The source time series
	 * @return The standard deviation
	 */
	public static Double getAnnualizedStandardDeviation(int months,TimeSeries monthlySeries) {
		if (monthlySeries.getDates().size()>=months) {
			TimeSeries relativeReturnsSeries = new TimeSeries();
			relativeReturnsSeries.addTimeSerie(monthlySeries.getRelativeReturn().getLastnValues(months));
			return relativeReturnsSeries.getAnnualizedStandardDeviation().getLastValue().getValue();
		}
		return Double.NaN;
	}
	
	/**
	 * Computes the annualized return over the last <code>months</code> months.
	 * @param months Number of months
	 * @param monthlySeries The source time series
	 * @return
	 */
	public static Double getAnnualizedReturn(int months,TimeSeries monthlySeries) {
		if (monthlySeries.getDates().size()>=months) {
			TimeSerie windowSerie = WindowAnalysis.Calculate( monthlySeries.getRelativeReturn(),months, TimeSerieIndicator.ANNUALIZED_RETURN, 0, false, new LastValueFunction(), new LastValueFunction());
			if (windowSerie.getLastValue()!=null) {
				return windowSerie.getLastValue().getValue();
			}
		}
		return Double.NaN;
	}
	
	/**
	 * Computes the largest drawdown over the last <code>months</code> months.
	 * @param months Number of months
	 * @param monthlySeries The source time series
	 */
	public static Double getLargestDrawdown(int months, TimeSeries monthlySeries) {
		if (monthlySeries.getDates().size()>=months) {
			TimeSerie windowSerie = monthlySeries.getDrawdown().getLastnValues(months);
			return windowSerie.getMin().getValue();
		}
		return Double.NaN;
	}
	
	/**
	 * Computes the percentage of positive months.
	 * @param monthlySeries The source time series
	 * @return
	 */
	public static Double getPercentageOfPositiveMonths(TimeSeries monthlySeries) {
		Date lastValidMonthlyDate = monthlySeries.getRelativeReturn().getLastValue().getDate();
		return monthlySeries.getPositiveMonths().getValue(lastValidMonthlyDate).getValue() / monthlySeries.getTrackLength().getValue(lastValidMonthlyDate).getValue();
		
	}
	
	/**
	 * Annualizes the given value.
	 * @param value The value
	 * @return The annualized value
	 */
	public static Double annualizeValue(Double value) {
		return value * StatisticsValueComputer.SQRT12;
	}
	
	public static List<Double> computeVirtualNAVs(double [] vector, int startRange, int endRange) {
		ArrayList<Double> navs = new ArrayList<Double>();
		navs.add(START_VALUE);
		
		for (int i = startRange;i<endRange;i++) {
			navs.add((navs.get(i) * vector[i]) + navs.get(i));
		}
		
		return navs;
	}

	public static Double [] computeTotalReturn(TimeSerie workingMonthlySerie) {
		ArrayList<Double> returns = new ArrayList<Double>();
		
		double value = START_VALUE;
		
		returns.add(Double.NaN);
		
		for (Date dt : workingMonthlySerie.getDatesAsArray()) {
			value = value + value * workingMonthlySerie.getValue(dt).getValue();
			returns.add((value - START_VALUE)/START_VALUE);
		}		
		
		return returns.toArray(new Double[]{});
	}
	
	/**
	 * Computes a set of annualized return. The set content is defined by the <code>monthsList</code> argument. It contains the months to use for each entry.<BR/>
	 * @param workingMonthlySerie The source time serie
	 * @param monthsList The list of months
	 */
	public static HashMap<Integer,Double> computeAnnualizedReturns(TimeSerie workingMonthlySerie, Integer [] monthsList) {
		HashMap<Integer,Double> values = new HashMap<Integer, Double>();
		HashMap<Integer,Date> dates = new HashMap<Integer, Date>();
		values.put(0, START_VALUE);
		for (Integer m : monthsList) {
			values.put(m, Double.NaN);
			dates.put(m, DateUtil.addOrRemoveMonthsEOM(workingMonthlySerie.getStopDate(),-m));
		}
		
		for (Date date : workingMonthlySerie.getDatesAsArray()) {
			Double value = workingMonthlySerie.getValue(date).getValue();
			values.put(0,values.get(0) + (values.get(0) * value));
			
			for (Integer m : monthsList) {
				if (!values.get(m).isNaN()) {
					values.put(m,values.get(m) + (values.get(m) * value));
				}
				if (dates.get(m).equals(date)) {
					values.put(m,START_VALUE);
				}
			}
		}
		
		values.put(0, values.get(0)/START_VALUE);
		values.put(0, Math.pow(values.get(0), MONTHS_PER_YEAR / workingMonthlySerie.getDatesAsArray().length) - 1);
		
		for (Integer m : monthsList) {
			values.put(m, values.get(m)/START_VALUE);
			values.put(m, Math.pow(values.get(m), MONTHS_PER_YEAR / m.doubleValue()) - 1);
		}
		return values;
	}
	
	/**
	 * Computes the Omega value
	 * @param ts The working time series
	 * @param overPeriod The period used to compute
	 * @param thresholdReturn The computation threshold
	 * @return The Omega value
	 */
	public static Double computeOmega(TimeSerie ts, int overPeriod, double thresholdReturn) {
		double thresholdEffective = Math.pow(1 + thresholdReturn,YEAR_MONTHS_RATIO)-1;
	
		double [] valuesAsdoubles = getSortedDoubleValues(ts, overPeriod);

		if (thresholdEffective<=valuesAsdoubles[0]) {
			return Double.POSITIVE_INFINITY;
		} else if (thresholdEffective>=valuesAsdoubles[valuesAsdoubles.length-1]) {
			return 0.0d;
		}
		double lengthAsDouble = new Integer(valuesAsdoubles.length).doubleValue();
		double gain = 0;
		double loss = 0;
		int i = 0;
		while (i<valuesAsdoubles.length-1 && thresholdEffective>=valuesAsdoubles[i+1]) {
				loss = loss + (valuesAsdoubles[i+1] - valuesAsdoubles[i]) * new Integer(i+1).doubleValue() / lengthAsDouble;
				i++;
		}
		loss = loss + (thresholdEffective - valuesAsdoubles[i]) * new Integer(i+1).doubleValue() / lengthAsDouble;
		gain = (valuesAsdoubles[i+1] - thresholdEffective) * (1.0d - new Integer(i+1).doubleValue() / lengthAsDouble);
		i = i + 1;
		for (int j = i;j<valuesAsdoubles.length-1;j++) {
			gain = gain + (valuesAsdoubles[j+1] - valuesAsdoubles[j]) * (1.0d - new Integer(j+1).doubleValue() / lengthAsDouble);
		}
		return gain/loss;
	}
	
	/**
	 * Return a subtract of the given time serie and sort their content.
	 * @param ts The time serie
	 * @param overPeriod The period to extract
	 * @return The sorted values array
	 */
	public static double [] getSortedDoubleValues(TimeSerie ts,int overPeriod) {
		TimeSerie lastnValues = null;
		if (overPeriod>0) {
			lastnValues = ts.getLastnValues(overPeriod);
		} else {
			lastnValues = ts;
		}
		double[] valuesAsdoubles = lastnValues.getValuesAsdoubles(true);
		Arrays.sort(valuesAsdoubles);
		return valuesAsdoubles;
	}
	
	/**
	 * Computes a vector containing the sums of values contained in the source vector according to the given range
	 * @param vector The source vector
	 * @param startRange The start index
	 * @param endRange The end index (included)
	 * @return The vector of sums
	 */
	public static double [] getSumsFromVector(double [] vector, int startRange, int endRange) {
		double result [] = null;
		if (vector.length>=startRange && startRange>=0 && vector.length>=endRange && endRange>=0) {
			result = new double[endRange - startRange];
			result[0] = vector[startRange];
			for (int i = startRange + 1;i<endRange;i++) {
				result[i - startRange] = result[i - 1 - startRange] + vector[i];
			}
		}
		return result;
	}
	
	/**
	 * Computes a vector containing the pow-ed of values contained in the source vector according to the given range and the powe factor
	 * @param vector The source vector
	 * @param startRange The start index
	 * @param endRange The end index (included)
	 * @param pow The pow factor
	 * @return The vector of powed values
	 */
	public static double [] computePowedVector(double [] vector, int startRange, int endRange, double pow) {
		double result [] = null;
		if (vector.length>=startRange && startRange>=0 && vector.length>=endRange && endRange>=0) {
			result = new double[endRange - startRange];
			for (int i = startRange;i<endRange;i++) {
				result[i - startRange] = Math.pow(vector[i],pow);
			}
		}
		return result;
	}
	
	/**
	 * Computes a vector containing the means of values contained in the source vector according to the given range
	 * @param vector The source vector
	 * @param startRange The start index
	 * @param endRange The end index (included)
	 * @return The vector of means
	 */
	public static double [] getMeansFromVector(double [] vector, int startRange, int endRange) {
		double [] meansVector = null;
		double currentSum = 0.0d;
		if (vector.length>=startRange && startRange>=0 && vector.length>=endRange && endRange>=0) {
			meansVector = new double[endRange - startRange];
			for (int i = startRange;i<endRange;i++) {
				int workingIndex = i - startRange;
				currentSum += vector[i];
				meansVector[workingIndex] = currentSum / (workingIndex + 1);
			}
		}
		return meansVector;
	}
	
	/**
	 * Computes a vector containing the standard deviation of values contained in the source vector according to the given range
	 * 
	 * http://en.wikipedia.org/wiki/Variance
	 * 
	 * @param vector The source vector
	 * @param startRange The start index
	 * @param endRange The end index (included)
	 * @param variance The array of variance
	 * @return The vector of means
	 */
	public static double [] getStandardDeviationsFromVector(double [] vector, int startRange, int endRange, double [] variances) {
		double [] stdVector = null;
		if (variances==null || variances.length==0) {
			variances = getMeansFromVector(vector,startRange,endRange);
		}
		if (vector.length>=startRange && startRange>=0 && vector.length>=endRange && endRange>=0) {
			stdVector = new double[endRange - startRange];
			for (int i = 0;i<variances.length;i++) {
				stdVector[i] = Math.pow(variances[i],0.5d);
			}
		}
		return stdVector;
	}
	
	/**
	 * 
	 * Computes a vector containing the variances of values contained in the source vector according to the given range
	 * 
	 * http://en.wikipedia.org/wiki/Variance
	 * 
	 * @param vector The source vector
	 * @param startRange The start index
	 * @param endRange The end index (included)
	 * @param means The vector of means
	 * @param nBased Is the variances computation n-based
	 * @return The vector of variances
	 */
	public static double [] getVariancesFromVector(double [] vector, int startRange, int endRange, double [] means, boolean nBased) {
		double [] varVector = null;
		
		if (vector.length>=startRange && startRange>=0 && vector.length>=endRange && endRange>=0) {
			if (means==null || means.length==0) {
				means = getMeansFromVector(vector,startRange,endRange);
			}
			varVector = new double[endRange - startRange];
			for (int i = startRange;i<endRange;i++) {
				int workingIndex = i - startRange;
				double sum = 0.0d;
				for (int j = startRange;j<=i;j++) {
					sum += Math.pow(vector[j] - means[workingIndex],2);
				}
				if (i!=0 || nBased) {
					varVector[workingIndex] = sum / (i + (nBased?1:0));
				} else {
					varVector[workingIndex] = Double.NaN;
				}
			}
		}
		return varVector;
	}
	
	/**
	 * 
	 * Computes a vector containing the estimated skewed T of values contained in the source vector according to the given range
	 * <B>The method was copied from the Matlab source, there were no comments. Purpose and accuracy of the computations are not known.</B>
	 * @param vector The source vector
	 * @param startRange The start index
	 * @param endRange The end index (included)
	 * @param means The vector of means
	 * @param stds The vector of standard deviation
	 * @return The vector of skewed T values
	 */
	public static double [] getEstimatedSkewedStudentTForSimulationLight(double [] vector, int startRange, int endRange, double [] means, double [] stds) {
		double [] nuAndLambda = new double [2];
		if (vector.length>=startRange && startRange>=0 && vector.length>=endRange && endRange>=0) {
			// Initialize if necessary
			if (means==null || means.length==0) {
				means = getMeansFromVector(vector,startRange,endRange);
			}
			if (stds==null || stds.length==0) {
				stds = getStandardDeviationsFromVector(vector,startRange,endRange,getVariancesFromVector(vector, startRange, endRange, means, false));
			}
			// Prepare working variables
			double [] workingVector = Arrays.copyOfRange(vector, startRange, endRange);
			
			// Effective job
			if (workingVector.length>=12) {
				for (int k = 0;k<workingVector.length;k++) {
					workingVector[k] = (workingVector[k] - means[workingVector.length-1]) / stds[workingVector.length-1];
				}
				nuAndLambda[0] = 10.0d;
				nuAndLambda[1] = 0.0d;
				double maximum = Double.POSITIVE_INFINITY;
				double skewedLL = Double.NaN;
				for (int i=3;i<36;i++) {
					double iAsDouble = new Integer(i).doubleValue();
					for (double lambda = -0.95d;lambda<=0.96d;lambda+=0.05) {
						skewedLL = computeSkewedT_LL(iAsDouble,lambda,PRECOMPUTED_SKEWED_C[i],PRECOMPUTED_SKEWED_LNC[i],workingVector);
						if (skewedLL<maximum) {
							maximum = skewedLL;
							nuAndLambda[0] = iAsDouble;
							nuAndLambda[1] = lambda;
						}
					}
				}
				
			} else {
				nuAndLambda[0] = 300.0d;
				nuAndLambda[1] = 0.0d;
			}
		}
		return nuAndLambda;
	}
	
	/**
	 * Method used by <code>getEstimatedSkewedStudentTForSimulationLight</code> during its treatment, it computes the sum of skewed factors
	 * <B>The method was copied from the Matlab source, there were no comments. Purpose and accuracy of the computations are not known.</B>
	 * @param nu The nu value
	 * @param lambda The lambda value
	 * @param fduC The constant
	 * @param fduLnC The log constant
	 * @param vector The working vector
	 * @return The sum of skewed factor
	 */
	public static double computeSkewedT_LL(double nu, double lambda,double fduC, double fduLnC,double[] vector) {
		double a = 4.0d * lambda * fduC * ((nu-2)/(nu-1));
		double a2 = Math.pow(a,2.0d);
		double lambda_a = 1.0d + 3.0d * Math.pow(lambda,2.0d) - a2;
		double b = Math.pow(lambda_a,0.5d);
		double a_b = -a / b;
		double logb = 0.5d * Math.log(lambda_a);
		double skewedLLCurrent;
		double skewedLLSum = 0.0d;
		for (int i=0;i<vector.length;i++) {
			if (vector[i]<a_b) {
				skewedLLCurrent = logb + fduLnC - ((nu+1.0d) / 2.0d) * Math.log(1.0d + 1.0d/(nu-2.0d) * Math.pow((b*vector[i] + a)/(1-lambda),2.0d));
			} else {
				skewedLLCurrent = logb + fduLnC - ((nu+1.0d) / 2.0d) * Math.log(1.0d + 1.0d/(nu-2.0d) * Math.pow((b*vector[i] + a)/(1+lambda),2.0d));
			}
			skewedLLSum += skewedLLCurrent;
		}
		return -skewedLLSum;
	}
	
	/**
	 * Sub-method used the V@R computation<BR/>
	 * <B>The method was copied from the Matlab source, there were no comments. Purpose and accuracy of the computations are not known.</B>
	 * 
	 * @param vector The working vector
	 * @param nu The nu value
	 * @param lambda The lambda value
	 * @return The skewed T inverse values
	 */
	public static double [] computeSkewedTInverseCDF(double [] vector, double nu, double lambda) {
		double[] results = new double[vector.length];
		double a = 4.0d * lambda * PRECOMPUTED_SKEWED_C[new Double(nu + 1.0d).intValue()] * ((nu-2)/(nu-1));
		double a2 = Math.pow(a,2.0d);
		double lambda_a = 1.0d + 3.0d * Math.pow(lambda,2.0d) - a2;
		double b = Math.pow(lambda_a,0.5d);
		double a_b = a / b;
		double threshold = (1.0d-lambda)/2.0d;
		double nuRatio = Math.pow((nu-2.0d) / nu, 0.5d);
		double lowerLambdaFactor = (1.0d - lambda)/b;
		double higherLambdaFactor = (1.0d + lambda)/b;
		try {
			TDistributionImpl impl = new TDistributionImpl(nu);
			for (int i=0;i<vector.length;i++) {
				double probability = 0;
				double lambdaFactor = 0.0d;
				if (vector[i]<threshold) {
					probability = vector[i] / (1.0d - lambda);
					lambdaFactor = lowerLambdaFactor;
				} else {
					probability = 0.5d + (1.0d / (1.0d + lambda)) * (vector[i] - threshold);
					lambdaFactor = higherLambdaFactor;
				}
				results[i] = lambdaFactor * nuRatio * impl.inverseCumulativeProbability(probability) - a_b;
			}
		} catch (MathException e) {
			log.error("An error occured during computations! Final values may not be accurate!",e);
		}
		return results;
	}
	
	/**
	 * Get the last mean according to the alpha value.
	 * <B>The method was copied from the Matlab source, there were no comments. Purpose and accuracy of the computations are not known.</B>
	 * @param vector The source vector
	 * @param alpha The alpha
	 * @param sorted Is the vector sorted 
	 * @return The mean
	 */
	public static double computeMean_ETL(double [] vector, double alpha, boolean sorted) {
		double[] workingVector = Arrays.copyOfRange(vector, 0, vector.length);
		if (!sorted) {
			Arrays.sort(workingVector);
		}
		int workingLimit = new Double(new Integer(vector.length).doubleValue() * alpha).intValue();
		return StatisticsValueComputer.getMeansFromVector(workingVector,0,workingLimit)[workingLimit-1];
	}
	
	/**
	 * Computes the value at risk of a given vector using its alpha
	 * <B>The method was copied from the Matlab source, there were no comments. Purpose and accuracy of the computations are not known.</B>
	 * @param vector The source vector
	 * @param alpha The alpha
	 * @param sorted Is the vector sorted 
	 * @return The V@R
	 */
	public static double valueAtRisk(double [] vector, double alpha, boolean sorted) {
		double[] workingVector = Arrays.copyOfRange(vector, 0, vector.length);
		if (!sorted) {
			Arrays.sort(workingVector);
		}
		int workingLimit = new Double(new Integer(vector.length).doubleValue() * alpha).intValue();
		return vector[workingLimit];
	}
	
	@SuppressWarnings("unchecked")
	public static TimeSerie computePerformancesFromValues(TimeSerie ts, ComputationFrequency frequency) {
		TimeSerie pts = new TimeSerie(TimeSerieIndicator.RELATIVERETURN);
		TimeSerieValue previous = null;
		for (TimeSerieValue tsv : (ArrayList<TimeSerieValue>)ts.getValuesAsList()) {
			if (DatesComputer.isValidDate(tsv._date, frequency)) {
				if (previous!=null) {
					TimeSerieValue perf = new TimeSerieValue();
					perf._date = tsv._date;
					perf._isFinalStatus = tsv._isFinalStatus;
					perf._value = (previous._value / tsv._value) - 1;
					pts.addValueWithoutReindex(perf);
				}
				previous = tsv;
			}
		}
		return pts;
	}
	
	/**
	 * Computes all V@R.<BR/>
	 * V@Rs are stored in an array with indices representing values as follow:
	 * <ul>
	 * <li>0 - Historical CVaR</li>
	 * <li>1 - Simulated CVaR</li>
	 * <li>2 - VaR_1_12</li>
	 * <li>3 - VaR 95</li>
	 * <li>4 - VaR 99</li>
	 * </ul>
	 * @param vector The source vector
	 * @param startRange The start range
	 * @param endRange The end range
	 * @return The set of V@R
	 */
	public static double [] computeValuesAtRisk(double [] vector, int startRange, int endRange) {
		double [] vars = new double[5];
		// Preparing data
		double [] workingSet = Arrays.copyOfRange(vector, startRange, endRange);
		double [] meansFromVector = StatisticsValueComputer.getMeansFromVector(workingSet, 0, workingSet.length);
		double [] variancesFromVector = StatisticsValueComputer.getVariancesFromVector(workingSet, 0, workingSet.length, meansFromVector,false);
		double [] stdFromVector = StatisticsValueComputer.getStandardDeviationsFromVector(workingSet, 0, workingSet.length, variancesFromVector);
		
		double[] estimatedSkewedStudentTForSimulationLight = StatisticsValueComputer.getEstimatedSkewedStudentTForSimulationLight(vector,startRange,endRange,meansFromVector,stdFromVector);
		double[] computeSkewedTInverseCDF = StatisticsValueComputer.computeSkewedTInverseCDF(DoublesArrayLoader.getArray("STUDENT_T_5_DEGREES_CUMULATIVE_PROBABILITY_1000"),estimatedSkewedStudentTForSimulationLight[0],estimatedSkewedStudentTForSimulationLight[1]);
		for (int i = 0;i<computeSkewedTInverseCDF.length;i++) {
			computeSkewedTInverseCDF[i] = meansFromVector[vector.length - 1] + computeSkewedTInverseCDF[i] * stdFromVector[vector.length - 1] ;
		}
		Arrays.sort(computeSkewedTInverseCDF, 0, computeSkewedTInverseCDF.length);
		
		vars[0] = StatisticsValueComputer.computeMean_ETL(vector, YEAR_MONTHS_RATIO ,false);
		vars[1] = StatisticsValueComputer.computeMean_ETL(computeSkewedTInverseCDF, YEAR_MONTHS_RATIO,true);
		
		vars[2] = StatisticsValueComputer.valueAtRisk(computeSkewedTInverseCDF, YEAR_MONTHS_RATIO ,true);
		vars[3] = StatisticsValueComputer.valueAtRisk(computeSkewedTInverseCDF, 0.05,true);
		vars[4] = StatisticsValueComputer.valueAtRisk(computeSkewedTInverseCDF, 0.01,true);
		
		return vars;
	}
	/**
	 * Computes statistics information using the additional information available for the current entity:
	 * <ul>
	 * <li>Total investments in USD</li>
	 * <li>Gross exposure</li>
	 * <li>Long exposure</li>
	 * </ul>
	 * @param statistic The statistics set to update
	 * @param additionalInfo The additional information to use
	 */
	public static void computeWithAdditionalInformation(ComputedStatistics statistic,AdditionalEntityInformation additionalInfo) {
		// Working on additional info				
		if (additionalInfo!=null) {
			if (additionalInfo.getTotalInvestedUSDAmount()!=null 
				&& !Double.isNaN(additionalInfo.getTotalInvestedUSDAmount())
				&& additionalInfo.getTotalInvestedUSDAmount()>0.0d
				&& additionalInfo.getShortExposures()!=null 
				&& additionalInfo.getShortExposures().size()>0
				&& additionalInfo.getLongExposures()!=null 
				&& additionalInfo.getLongExposures().size()>0
				) {
				double longV = 0.0d;
				double shortV = 0.0d;
				for (Double longValue :  additionalInfo.getLongExposures()) {
					longV += longValue;
				}
				longV = longV / additionalInfo.getTotalInvestedUSDAmount();
				for (Double shortValue :  additionalInfo.getShortExposures()) {
					shortV += shortValue;
				}
				shortV = shortV / additionalInfo.getTotalInvestedUSDAmount();
				
				statistic.setTotalAmountUSD(additionalInfo.getTotalInvestedUSDAmount());
				
				statistic.setGrossExposure(longV + shortV);
				
				statistic.setNetExposure(longV - shortV);
			}
		}
	}
	
}
