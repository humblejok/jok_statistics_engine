package com.eim.utility.contribution.runners;

import java.util.Date;

import com.eim.utility.common.model.EIMCompanies;

public class PTPortfoliosContributionsRunner extends PortfoliosContributionsRunner {

	/* -------------------------------------------------
	 * ATTRIBUTES
	 ------------------------------------------------- */
	
	/* -------------------------------------------------
	 * CONSTRUCTORS
	 ------------------------------------------------- */
	
	public PTPortfoliosContributionsRunner(Date startDate, Date endDate) {
		super(startDate, endDate, EIMCompanies.EIM_PAPER_TRADED, "source_pt");
	}

	/* -------------------------------------------------
	 * GETTERS/SETTERS
	 ------------------------------------------------- */
	
	/* -------------------------------------------------
	 * METHODS
	 ------------------------------------------------- */
	
}
