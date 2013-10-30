package com.eim.utility.contribution.runners;

import java.util.Date;

import com.eim.utility.common.model.EIMCompanies;

public class ActiveCHPortfoliosContributionsRunner extends ActivePortfoliosContributionsRunner {

	public ActiveCHPortfoliosContributionsRunner(Date startDate, Date endDate) {
		super(startDate, endDate, EIMCompanies.EIM_SWITZERLAND, "source");
	}

}
