package com.eim.utility.statistics.tag;

/**
 * Tags representing benchmark statistics
 * @author sdj
 *
 */
public interface BenchmarkRelatedStatisticsTags extends StatisticsTags {
	
	String RELATED_ENTITY_TYPE = "relatedEntityType";
	
	String RELATED_ENTITY_ID = "relatedEntityId";
	
	String RISK_FREE_BENCHMARK = "riskFreeBenchmark";

	String ALPHA = "alpha";
	
	String ANNUALIZED_ALPHA = "annualizedAlpha";
	
	String BETA = "beta";
	String DOWN_CAPTURE = "downCapture";
	String DOWN_CAPTURE_RATIO = "downCaptureRatio";
	String DOWN_NUMBER_RATIO = "downNumberRatio";	
	String DOWN_PERCENTAGE_RATIO = "downPercentageRatio";
	String INFORMATION_RATIO = "informationRatio";
	String JENSEN_ALPHA = "jensenAlpha";

	String PERCENTAGE_GAIN_RATIO = "percentageGainRatio";
	
	String R = "r";
	String R2 = "r2";
	String TRACKING_ERROR = "trackingError";
	String TREYNOR_RATIO = "treynorRatio";
	String UP_CAPTURE = "upCapture";
	String UP_PERCENTAGE_RATIO = "upPercentageRatio";
	String UP_NUMBER_RATIO = "upNumberRatio";
	String ANNUALIZED_RETURN = "annualizedReturn";
	String TOTAL_RETURN = "totalReturn";
	String AVG_MONTHLY_ROR = "averageMonthlyROR";
	String AVG_MONTHLY_LOSS = "averageMonthlyLoss";
	String AVG_MONTHLY_GAIN = "averageMonthlyGain";
	
	String POSITIVE_MONTH_RATIO = "positiveMonthRatio";
	String ACTIVE_PREMIUM = "activePremium";
	String ANNUALIZED_STD_DEV = "annualizedStandardDeviation";
	String ANNUALIZED_VOLATILITY = "annualizedVolatility";
	String RFR_ANNUALIZED_DOWNSIDE_DEVIATION = "annualizedDownsideDeviationRFR";
	String RFR_SHARP_RATIO = "sharpRatioRFR";
	String RFR_SORTINO_RATIO = "sortinoRatioRFR";
	
	String MAXIMUM_DRAWDOWN = "maximumDrawdown";
	String MAXIMUM_LENGTH_OF_DRAWDOWN = "maximumLengthOfDrawdown";
	String IS_DRAWDOWN_ONGOING = "drawdownOngoing";

}
