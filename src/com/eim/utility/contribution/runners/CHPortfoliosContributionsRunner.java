package com.eim.utility.contribution.runners;

import java.util.Date;

import com.eim.utility.common.model.EIMCompanies;

public class CHPortfoliosContributionsRunner extends PortfoliosContributionsRunner {

	/* -------------------------------------------------
	 * ATTRIBUTES
	 ------------------------------------------------- */
	
	/* -------------------------------------------------
	 * CONSTRUCTORS
	 ------------------------------------------------- */
	
	public CHPortfoliosContributionsRunner(Date startDate, Date endDate) {
		super(startDate, endDate, EIMCompanies.EIM_SWITZERLAND, "source");
	}

	/* -------------------------------------------------
	 * GETTERS/SETTERS
	 ------------------------------------------------- */
	
	/* -------------------------------------------------
	 * METHODS
	 ------------------------------------------------- */
	
}
