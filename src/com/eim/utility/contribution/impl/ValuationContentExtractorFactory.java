package com.eim.utility.contribution.impl;

import com.eim.utility.common.model.EntityType;
import com.eim.utility.contribution.api.ValuationContentExtractor;

/**
 * Factory class that handles the creation of <code>ValuationContentExtractor</code> using the <code>type</code> of the entity.  
 * @author sdj
 * @author abaguet
 *
 */
public class ValuationContentExtractorFactory {
	/**
	 * The factory method 
	 * @param type The entity type
	 * @return An instance of the entity type dedicated extractor
	 */
	public static ValuationContentExtractor getValuationContentExtractor(EntityType type) {
		switch (type) {
		case PORTFOLIO:
			return new PortfolioValuationContentExtractor(); 
		default:
			return null;
		}
	}
}
