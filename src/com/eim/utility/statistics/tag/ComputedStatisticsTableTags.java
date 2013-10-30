package com.eim.utility.statistics.tag;

public interface ComputedStatisticsTableTags extends StatisticsTableTags {
	
	String TABLE_NAME 							= "STATS_COMPUTED";
	
	String LASTRETURN							= "lastReturn";
	String YEARTODATE							= "yearToDate";
	
	String LAST3MONTHSTOTALRETURN				= "last3MonthsTotalReturn";
	String LAST6MONTHSTOTALRETURN				= "last6MonthsTotalReturn";
	String LAST12MONTHSTOTALRETURN				= "last12MonthsTotalReturn";
	String LAST18MONTHSTOTALRETURN				= "last18MonthsTotalReturn";
	String LAST24MONTHSTOTALRETURN				= "last24MonthsTotalReturn";
	String LAST36MONTHSTOTALRETURN				= "last36MonthsTotalReturn";
	String LAST60MONTHSTOTALRETURN				= "last60MonthsTotalReturn";
	
	String LAST12ANNUALSTDDEV					= "last12MonthsAnnualizedStandardDeviation";
	String LAST24ANNUALSTDDEV					= "last24MonthsAnnualizedStandardDeviation";
	String LAST36ANNUALSTDDEV					= "last36MonthsAnnualizedStandardDeviation";
	
	String LAST18MONTHSANNUALIZEDRETURN			= "last18MonthsAnnualizedReturn";
	String LAST24MONTHSANNUALIZEDRETURN			= "last24MonthsAnnualizedReturn";
	String LAST36MONTHSANNUALIZEDRETURN			= "last36MonthsAnnualizedReturn";
	String LAST60MONTHSANNUALIZEDRETURN			= "last60MonthsAnnualizedReturn";

	String LAST18MONTHSMAXIMUMDRAWDOWN			= "last18MonthsMaximumDrawdown";
	String CURRENTDRAWDOWN						= "currentDrawdown";
	
	String ANNUALIZEDRETURN						= "annualizedReturn";
	String TOTALRETURN							= "totalReturn";
	String AVERAGERATEOFRETURN					= "averageRateOfReturn";
	String AVERAGEGAIN							= "averageGain";
	String AVERAGELOSS							= "averageLoss";
	String PERCENTAGEOFPOSITIVEMONTHS			= "percentageOfPositiveMonths";
	String ANNUALIZEDSTANDARDDEVIATION			= "annualizedStandardDeviation";
	String ANNUALIZEDLOSSDEVIATION				= "annualizedLossDeviation";
	String SKEWNESS								= "skewness";
	String KURTOSIS								= "kurtosis";
	String NUMBEROFMONTHS						= "numberOfMonths";
	
	String CVAR_11_12							= "cvar_11_12";
	String CVAR_11_12_SIMULATED					= "cvar_11_12_simulated";
	String VAR_11_12_SIMULATED					= "var_11_12_simulated";
	String VAR_95_SIMULATED						= "var_95_simulated";
	String VAR_99_SIMULATED						= "var_99_simulated";
	String OMEGA10SINCEINCEPTION				= "omega10SinceInception";
	String OMEGA10OVER36MONTHS					= "omega10Over36Months";

	String MAXIMUMDRAWDOWN						= "maximumDrawdown";
	String MAXIMUMLENGTHOFDRAWDOWN				= "maximumLengthOfDrawdown";
	String DRAWDOWNONGOING						= "drawdownOngoing";
	
	String TOTALAMOUNTUSD						= "totalAmountUSD";
	
	String GROSSEXPOSURE						= "grossExposure";
	String NETEXPOSURE							= "netExposure";
	String LASTRETURNDATE						= "lastReturnDate";
	
	
	String YEARTODATEY_1 						= "yearToDateY_1";
	String YEARTODATEY_2 						= "yearToDateY_2";
	String YEARTODATEY_3 						= "yearToDateY_3";
	
	String MONTHTODATE							= "monthToDate";
	String QUARTERTODATE						= "quarterToDate";
	String SEMESTERTODATE						= "semesterToDate";
}
