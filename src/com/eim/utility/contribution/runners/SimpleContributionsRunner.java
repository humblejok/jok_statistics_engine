package com.eim.utility.contribution.runners;

import java.util.ArrayList;
import java.util.Date;

import com.eim.utility.common.model.EIMCompanies;
import com.eim.utility.common.model.EntityType;

public class SimpleContributionsRunner extends AbstractContributionsRunner {

	public ArrayList<Long> workingSet;
	
	public SimpleContributionsRunner(EntityType entityType, EIMCompanies eimCompany, ArrayList<Long> workingSet, Date startDate, Date endDate) {
		super();
		this.workingSet = workingSet;
		this.entityType = entityType;
		this.eimCompany = eimCompany;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public ArrayList<Long> getEntitiesWorkingSet() {
		return this.workingSet;
	}

}
