package com.eim.utility.statistics.impl;

import com.eim.utility.common.model.EntityType;
import com.eim.utility.statistics.api.TrackContentExtractor;

/**
 * Factory class that handles the creation of <code>TrackContentExtractor</code> using the <code>type</code> of the entity.  
 * @author sdj
 *
 */
public class TrackContentExtractorFactory {

	/**
	 * The factory method 
	 * @param type The entity type
	 * @return An instance of the entity type dedicated extractor
	 */
	public static TrackContentExtractor getTrackContentExtractor(EntityType type) {
		switch (type) {
		case PORTFOLIO:
			return new SequoiaPortfolioTrackContentExtractor();
		case OTHER_FUND:
			return new SequoiaSecurityRepositoryTrackContentExtractor();
		default:
			return new SequoiaSecurityTrackContentExtractor();
		}
	}
	
}
