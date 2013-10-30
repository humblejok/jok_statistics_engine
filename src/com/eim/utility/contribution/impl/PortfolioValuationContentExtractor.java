package com.eim.utility.contribution.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.dst.timeserie.TimeSerieValue;
import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.DatesComputer;
import com.eim.utility.contribution.api.ValuationContentExtractor;
import com.eim.utility.contribution.model.AUMToken;
import com.eim.utility.contribution.model.CashflowsToken;
import com.eim.utility.contribution.model.Evaluation;

public class PortfolioValuationContentExtractor extends ValuationContentExtractor {

	public PortfolioValuationContentExtractor() {
	}

	@Override
	public String getAllAUMSQueryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllAUMSWithinRangeQueryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllCashflowsQueryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllCashflowsWithinRangeQueryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllUnderlyingAUMSQueryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllUnderlyingAUMSWithinRangeQueryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllUnderlyingCashflowsQueryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllUnderlyingCashflowsWithinRangeQueryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HashMap<Long, ArrayList<CashflowsToken>> readDatabaseUnderlyingCashflowsData(
			HashMap<Long, ArrayList<CashflowsToken>> cashflows,
			PreparedStatement statement, ComputationFrequency frequency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<CashflowsToken> readDatabaseCashflowsData(
			ArrayList<CashflowsToken> cashflows, PreparedStatement statement,
			ComputationFrequency frequency) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<AUMToken> readDatabaseAUMSData(
			ArrayList<AUMToken> aums, PreparedStatement statement,
			ComputationFrequency frequency) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected HashMap<Long, ArrayList<AUMToken>> readDatabaseUnderlyingAUMSData(
			HashMap<Long, ArrayList<AUMToken>> aums,
			PreparedStatement statement, ComputationFrequency frequency)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
