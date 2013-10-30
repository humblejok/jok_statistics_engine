/**
 * Title           : $Workfile: StatisticsTags.java $
 * Copyright       : EIM (c) 2004
 * Updates         : $Date: 2/17/05 9:04a $
 * By              : $Author: Jpf $
 * Version number  : $Revision: 2 $
 *
 * $History: StatisticsTags.java $
 * 
 * *****************  Version 2  *****************
 * User: Jpf          Date: 2/17/05    Time: 9:04a
 * Updated in $/Current/Projects/core/src/com/eim/business/statistic/xml
 * Mise ne place recherche avancée avec les stats.
 * Ajout nouveaux indicateurs pour les stats.
 */
package com.eim.utility.statistics.tag;

/**
 * Tags representing computed statistics
 * 
 * @author  sdj
 */
public interface ComputedStatisticsTags extends StatisticsTags {

	String LAST_RETURN								= "lastReturn";
	String YEAR_TO_DATE								= "yearToDate";
	
	String LAST_3_MONTHS_TOTAL_RETURN				= "last3MonthsTotalReturn";
	String LAST_6_MONTHS_TOTAL_RETURN				= "last6MonthsTotalReturn";
	String LAST_12_MONTHS_TOTAL_RETURN				= "last12MonthsTotalReturn";
	String LAST_18_MONTHS_TOTAL_RETURN				= "last18MonthsTotalReturn";
	String LAST_24_MONTHS_TOTAL_RETURN				= "last24MonthsTotalReturn";
	String LAST_36_MONTHS_TOTAL_RETURN				= "last36MonthsTotalReturn";
	String LAST_60_MONTHS_TOTAL_RETURN				= "last60MonthsTotalReturn";
	
	String LAST_12_MONTHS_ANNUALIZED_STD_DEVIATION	= "last12MonthsAnnualizedStandardDeviation";
	String LAST_24_MONTHS_ANNUALIZED_STD_DEVIATION	= "last24MonthsAnnualizedStandardDeviation";
	String LAST_36_MONTHS_ANNUALIZED_STD_DEVIATION	= "last36MonthsAnnualizedStandardDeviation";
	
	String LAST_18_MONTHS_ANNULIZED_RETURN			= "last18MonthsAnnualizedReturn";
	String LAST_24_MONTHS_ANNULIZED_RETURN			= "last24MonthsAnnualizedReturn";
	String LAST_36_MONTHS_ANNULIZED_RETURN			= "last36MonthsAnnualizedReturn";
	String LAST_60_MONTHS_ANNULIZED_RETURN			= "last60MonthsAnnualizedReturn";

	String LAST_18_MONTHS_MAX_DRAWDOWN				= "last18MonthsMaximumDrawdown";
	String CURRENT_DRAWDOWN							= "currentDrawdown";
	
	String ANNUALIZED_RETURN						= "annualizedReturn";
	String TOTAL_RETURN								= "totalReturn";
	String AVG_RATE_OF_RETURN						= "averageRateOfReturn";
	String AVG_GAIN									= "averageGain";
	String AVG_LOSS									= "averageLoss";
	String PERCENTAGE_OF_POSITIVE_MONTHS			= "percentageOfPositiveMonths";
	String ANNUALIZED_STD_DEVIATION					= "annualizedStandardDeviation";
	String ANNUALIZED_LOSS_DEVIATION				= "annualizedLossDeviation";
	String SKEWNESS									= "skewness";
	String KURTOSIS									= "kurtosis";
	String NUMBER_OF_MONTHS							= "numberOfMonths";
	
	String CVAR_11_12								= "cvar_11_12";
	String CVAR_11_12_SIMULATED						= "cvar_11_12_simulated";
	String VAR_11_12_SIMULATED						= "var_11_12_simulated";
	String VAR_95_SIMULATED							= "var_95_simulated";
	String VAR_99_SIMULATED							= "var_99_simulated";
	String OMEGA_SINCE_INCEPTION					= "omega10SinceInception";
	String OMEGA_OVER_36_MONTHS						= "omega10Over36Months";

	String MAXIMUM_DRAWDOWN							= "maximumDrawdown";
	String MAXIMUM_LENGTH_OF_DRAWDOWN				= "maximumLengthOfDrawdown";
	String DRAWDOWN_ONGOING							= "drawdownOngoing";
	
	String TOTAL_AMOUNT_USD							= "totalAmountUSD";
	
	String GROSS_EXPOSURE							= "grossExposure";
	String NET_EXPOSURE								= "netExposure";
	String LAST_RETURN_DATE							= "lastReturnDate";
	
	
	String YEAR_TO_DATE_1 							= "yearToDateY_1";
	String YEAR_TO_DATE_2 							= "yearToDateY_2";
	String YEAR_TO_DATE_3 							= "yearToDateY_3";
	
	String MONTH_TO_DATE							= "monthToDate";
	String QUARTER_TO_DATE							= "quarterToDate";
	String SEMESTER_TO_DATE							= "semesterToDate";
	
} // end interface StatisticsTags
