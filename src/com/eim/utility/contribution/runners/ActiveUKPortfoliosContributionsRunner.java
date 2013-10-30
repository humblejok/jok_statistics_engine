package com.eim.utility.contribution.runners;

import java.util.Date;

import com.eim.utility.common.model.EIMCompanies;

public class ActiveUKPortfoliosContributionsRunner extends ActivePortfoliosContributionsRunner {

	public ActiveUKPortfoliosContributionsRunner(Date startDate, Date endDate) {
		super(startDate, endDate, EIMCompanies.EIM_UK, "source_uk");
	}

}
