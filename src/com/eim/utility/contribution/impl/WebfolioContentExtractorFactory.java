package com.eim.utility.contribution.impl;

import com.eim.utility.common.model.EIMCompanies;

/**
 * Factory class that handles the creation of <code>ValuationContentExtractor</code> using the <code>type</code> of the entity.  
 * @author sdj
 * @author abaguet
 *
 */
public class WebfolioContentExtractorFactory {
	/**
	 * The factory method 
	 * @param type The entity type
	 * @return An instance of the entity type dedicated extractor
	 */
	public static WebfolioContentExtractor getWebfolioContentExtractor(EIMCompanies eimCompany) {
		// Depending on EIM company
		switch (eimCompany) {
			case EIM_SWITZERLAND:
				return new WebfolioContentExtractor();
			case EIM_UK:
				return new WebfolioUKContentExtractor();
			case EIM_PAPER_TRADED:
				return new WebfolioPTContentExtractor();
			default:
				return null;
		}
	}
}
