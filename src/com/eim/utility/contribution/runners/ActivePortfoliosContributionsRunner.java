package com.eim.utility.contribution.runners;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.eim.utility.common.model.EIMCompanies;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.contribution.database.QueriesProvider;

public class ActivePortfoliosContributionsRunner extends AbstractContributionsRunner {

	/* -------------------------------------------------
	 * ATTRIBUTES
	 ------------------------------------------------- */
	
	public ArrayList<Long> workingSet;
	
	/**
	 * The (webfolio) connection source
	 */
	public String connectionSource;
	
	/* -------------------------------------------------
	 * CONSTRUCTORS
	 ------------------------------------------------- */
	
	public ActivePortfoliosContributionsRunner(Date startDate, Date endDate, EIMCompanies eimCompany, String connectionSource) {
		super();
		this.entityType = EntityType.PORTFOLIO;
		this.eimCompany = eimCompany;
		this.connectionSource = connectionSource;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/* -------------------------------------------------
	 * GETTERS/SETTERS
	 ------------------------------------------------- */
	
	/* -------------------------------------------------
	 * METHODS
	 ------------------------------------------------- */
	
	@Override
	public ArrayList<Long> getEntitiesWorkingSet() {
		log.info("Getting Active Portfolios");
		
		ArrayList<Long> workingSet = new ArrayList<Long>();
		
		Connection connection = QueriesProvider.getConnection(connectionSource);
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(QueriesProvider.getQuery("ACTIVE_PORTFOLIOS_LIST.SQL"));
			ResultSet resultset = prepareStatement.executeQuery();
			while (resultset.next()) {
				workingSet.add(resultset.getLong("PORTFOLIOSEEDID"));
			}
			prepareStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			log.error("A SQL error occured during Active Portfolio workset creation!",e);
		}
		return workingSet;
	}

}
