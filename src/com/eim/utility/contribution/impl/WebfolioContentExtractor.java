package com.eim.utility.contribution.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.dst.util.log.Logger;
import com.eim.utility.contribution.model.Evaluation;
import com.eim.utility.contribution.model.Operation;
import com.eim.utility.contribution.model.Portfolio;
import com.eim.utility.contribution.model.Position;
import com.eim.utility.contribution.model.Switch;
import com.eim.utility.contribution.database.QueriesProvider;

/**
 * Abstract class that defines the minimum methods that need to be implemented when creating a Portfolio/Valuation Content Extractor.
 * @author sdj
 * @author abaguet
 */
public class WebfolioContentExtractor {

	/**
	 * The logging utility
	 */
	private Logger logger = Logger.getLogger(WebfolioContentExtractor.class);
	
	protected String sourceConnectionName;
	
	public WebfolioContentExtractor() {
		sourceConnectionName = "source";
	}

	/**
	 * Returns the filename of the evaluation securities extraction query
	 * @return The filename
	 */
	public String getEvaluationSecuritiesQueryName() {
		return "WEBFOLIO_PORTFOLIO_EVALUATIONS_POSITIONS.SQL";
	}
	
	/**
	 * Returns the filename of the evaluations extraction query
	 * @return The filename
	 */
	public String getEvaluationsWithinPeriodQueryName() {
		return "WEBFOLIO_PORTFOLIO_EVALUATIONS_WITHIN_PERIOD.SQL";
	}
	
	/**
	 * Returns the filename of the evaluations extraction query
	 * @return The filename
	 */
	public String getEvaluationsAfterDateQueryName() {
		return "WEBFOLIO_PORTFOLIO_EVALUATIONS_AFTER_DATE.SQL";
	}
	
	/**
	 * Returns the filename of the evaluations extraction query
	 * @return The filename
	 */
	public String getEvaluationsBeforeDateQueryName() {
		return "WEBFOLIO_PORTFOLIO_EVALUATIONS_BEFORE_DATE.SQL";
	}
	
	/**
	 * Returns the filename of the evaluations extraction query
	 * @return The filename
	 */
	public String getEvaluationsQueryName() {
		return "WEBFOLIO_PORTFOLIO_EVALUATIONS.SQL";
	}
	
	/**
	 * Returns the filename of the evaluations extraction query
	 * @return The filename
	 */
	public String getEvaluationQueryName() {
		return "WEBFOLIO_EVALUATION.SQL";
	}
	
	/**
	 * Returns the filename of the evaluations extraction query
	 * @return The filename
	 */
	public String getPortfolioQueryName() {
		return "WEBFOLIO_PORTFOLIO.SQL";
	}
	
	/**
	 * Returns the filename of the evaluations extraction query
	 * @return The filename
	 */
	public String getPortfolioSwitchesQueryName() {
		return "WEBFOLIO_PORTFOLIO_SWITCHES_WITHIN_PERIOD.SQL";
	}

	/**
	 * Returns the filename of the evaluations extraction query
	 * @return The filename
	 */
	public String getPortfolioOperationsQueryName() {
		return "WEBFOLIO_PORTFOLIO_OPERATIONS_WITHIN_PERIOD.SQL";
	}

	/**
	 * Returns the filename of the evaluations extraction query
	 * @return The filename
	 */
	public String getPortfolioPenaltiesWithinPeriodQueryName() {
		return "WEBFOLIO_PORTFOLIO_PENALTIES_WITHIN_PERIOD.SQL";
	}

	/**
	 * Default utility method that gets all evaluations in a given period.
	 * @param id Portfolio id
	 * @return The list of evaluations
	 */
	public Collection<Evaluation> getPortfolioEvaluationsWithinPeriod(Long id, Date fromDate, Date toDate) {
		// Get name of query to be used
		String queryName = getEvaluationsWithinPeriodQueryName();
		
		logger.info("Get evaluations for portfolio with id [" + id.toString() + "] from " + sourceConnectionName + " between " + fromDate.toString() + " and " + toDate.toString() + ", using " + queryName);
		
		// Initialize value to be returned
		ArrayList<Evaluation> results = null;
		
		// Get data
		Connection connection = null;
		// Try again (limited to 100 times) to get TNS connection (it sometimes fails when trying to connect to Paper Traded DB)
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, id);
			preparedStatement.setLong(2, id);
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(4, new java.sql.Timestamp(toDate.getTime()));
			// Get results
			results = readDatabaseEvaluationsData(new ArrayList<Evaluation>(), preparedStatement);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during evaluations content extraction!", e);
		}
		
		return results;
	}
	
	/**
	 * Default utility method that gets all evaluations in a given period.
	 * @param id Portfolio id
	 * @return The list of evaluations
	 */
	public Collection<Evaluation> getPortfolioEvaluationsAfterDate(Long id, Date fromDate) {
		// Get name of query to be used
		String queryName = getEvaluationsAfterDateQueryName();
		
		logger.info("Get evaluations for portfolio with id [" + id.toString() + "] after " + fromDate.toString() + ", using " + queryName);
		
		// Initialize value to be returned
		ArrayList<Evaluation> results = null;
		
		// Get data
		Connection connection = null;
		// Try again (limited to 100 times) to get TNS connection (it sometimes fails when trying to connect to Paper Traded DB)
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, id);
			preparedStatement.setLong(2, id);
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(fromDate.getTime()));
			// Get results
			results = readDatabaseEvaluationsData(new ArrayList<Evaluation>(), preparedStatement);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during evaluations content extraction!", e);
		}
		
		return results;
	}
	
	/**
	 * Default utility method that gets all evaluations in a given period.
	 * @param id Portfolio id
	 * @return The list of evaluations
	 */
	public Collection<Evaluation> getPortfolioEvaluationsBeforeDate(Long id, Date toDate) {
		// Get name of query to be used
		String queryName = getEvaluationsBeforeDateQueryName();
		
		logger.info("Get evaluations for portfolio with id [" + id.toString() + "] before " + toDate.toString() + ", using " + queryName);
		
		// Initialize value to be returned
		ArrayList<Evaluation> results = null;
		
		// Get data
		Connection connection = null;
		// Try again (limited to 100 times) to get TNS connection (it sometimes fails when trying to connect to Paper Traded DB)
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, id);
			preparedStatement.setLong(2, id);
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(toDate.getTime()));
			// Get results
			results = readDatabaseEvaluationsData(new ArrayList<Evaluation>(), preparedStatement);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during evaluations content extraction!", e);
		}
		
		return results;
	}
	
	/**
	 * Default utility method that gets all evaluations in a given period.
	 * @param id Portfolio id
	 * @return The list of evaluations
	 */
	public Collection<Evaluation> getPortfolioEvaluations(Long id) {
		// Get name of query to be used
		String queryName = getEvaluationsQueryName();
		
		logger.info("Get evaluations for portfolio with id [" + id.toString() + "], using " + queryName);
		
		// Initialize value to be returned
		ArrayList<Evaluation> results = null;
		
		// Get data
		Connection connection = null;
		// Try again (limited to 100 times) to get TNS connection (it sometimes fails when trying to connect to Paper Traded DB)
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, id);
			preparedStatement.setLong(2, id);
			// Get results
			results = readDatabaseEvaluationsData(new ArrayList<Evaluation>(), preparedStatement);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during evaluations content extraction!", e);
		}
		
		return results;
	}
	
	/**
	 * Method to get security positions for a given evaluation.
	 * @param id Evaluation id
	 * @return The list of securities
	 */
	public Collection<Position> getEvaluationSecurities(Long id, Long refID) {
		// Get query
		String queryName = getEvaluationSecuritiesQueryName();
		
		logger.info("Get securities for evaluation with id [" + id.toString() + "] and reference evaluation with id [" + refID.toString() + "], using " + queryName);
		
		// Prepare results to be returned
		ArrayList<Position> results = null;
		
		// Get data
		Connection connection = null;
		// Try again (limited to 100 times) to get TNS connection (it sometimes fails when trying to connect to Paper Traded DB)
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, id);
			preparedStatement.setLong(2, id);
			preparedStatement.setLong(3, refID);
			preparedStatement.setLong(4, refID);
			
			// Get results
			results = readDatabaseEvaluationSecuritiesData(preparedStatement);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during evaluation securities content extraction!",e);
		}
		
		return results;
	}
	
	/**
	 * Method to get security positions for a given evaluation.
	 * @param id Evaluation id
	 * @return The list of securities
	 */
	public String getPortfolio(Long portfolioID) {
		// Get name of query to be used
		String queryName = getPortfolioQueryName();
		
		logger.info("Get portfolio details with id [" + portfolioID.toString() + "], using " + queryName);
		
		// Prepare results to be returned
		String result = "";
		
		// Get data
		Connection connection = null;
		// Try again (limited to 100 times) to get TNS connection (it sometimes fails when trying to connect to Paper Traded DB)
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, portfolioID);
			
			// Get results
			result = readDatabasePortfolio(preparedStatement);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during portfolio switches content extraction!",e);
		}
		
		return result;
	}
	
	/**
	 * Method to get security positions for a given evaluation.
	 * @param id Evaluation id
	 * @return The list of securities
	 */
	public Evaluation getEvaluation(Long evaluationID) {
		// Get name of query to be used
		String queryName = getEvaluationQueryName();
		
		logger.info("Get evaluation details with id [" + evaluationID.toString() + "], using " + queryName);
		
		// Prepare results to be returned
		Evaluation result = new Evaluation(evaluationID);
		
		// Get data
		Connection connection = null;
		// Try again (limited to 100 times) to get TNS connection (it sometimes fails when trying to connect to Paper Traded DB)
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, evaluationID);
			preparedStatement.setLong(2, evaluationID);
			
			// Get results
			readDatabaseEvaluation(preparedStatement, result);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during portfolio switches content extraction!",e);
		}
		
		return result;
	}
	
	/**
	 * Method to get security positions for a given evaluation.
	 * @param id Evaluation id
	 * @return The list of securities
	 */
	public void getPortfolioDetails(Portfolio portfolio) {
		// Get name of query to be used
		String queryName = getPortfolioQueryName();
		Long portfolioID = portfolio.getId();
		
		logger.info("Get portfolio details with id [" + portfolioID.toString() + "], using " + queryName);
		
		// Get data
		Connection connection = null;
		// Try again (limited to 100 times) to get TNS connection (it sometimes fails when trying to connect to Paper Traded DB)
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, portfolioID);
			
			// Get results
			readDatabasePortfolioDetails(preparedStatement, portfolio);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during portfolio switches content extraction!",e);
		}
	}
	
	/**
	 * Method to get security positions for a given evaluation.
	 * @param id Evaluation id
	 * @return The list of securities
	 */
	public Collection<Switch> getPortfolioSwitches(Long portfolioID, Date fromDate, Date toDate, Date evalCalcDate, Date refEvalCalcDate) {
		// Get name of query to be used
		String queryName = getPortfolioSwitchesQueryName();
		
		logger.info("Get switches for portfolio with id [" + portfolioID.toString() + "] between " + fromDate.toString() + " and " + toDate.toString() + ", using " + queryName);
		
		// Prepare results to be returned
		ArrayList<Switch> results = null;
		
		// Get data
		Connection connection = null;
		// Try again (limited to 100 times) to get TNS connection (it sometimes fails when trying to connect to Paper Traded DB)
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, portfolioID);
//			preparedStatement.setTimestamp(2, new java.sql.Timestamp(fromDate.getTime()));
//			preparedStatement.setTimestamp(3, new java.sql.Timestamp(toDate.getTime()));
			preparedStatement.setTimestamp(2, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(toDate.getTime()));
			preparedStatement.setTimestamp(4, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(5, new java.sql.Timestamp(refEvalCalcDate.getTime()));
			preparedStatement.setTimestamp(6, new java.sql.Timestamp(evalCalcDate.getTime()));
			preparedStatement.setLong(7, portfolioID);
//			preparedStatement.setTimestamp(5, new java.sql.Timestamp(fromDate.getTime()));
//			preparedStatement.setTimestamp(6, new java.sql.Timestamp(toDate.getTime()));
			preparedStatement.setTimestamp(8, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(9, new java.sql.Timestamp(toDate.getTime()));
			preparedStatement.setTimestamp(10, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(11, new java.sql.Timestamp(refEvalCalcDate.getTime()));
			preparedStatement.setTimestamp(12, new java.sql.Timestamp(evalCalcDate.getTime()));
			
			// Get results
			results = readDatabasePortfolioSwitches(preparedStatement);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during portfolio switches content extraction!",e);
		}
		
		return results;
	}
	
	/**
	 * Method to get security positions for a given evaluation.
	 * @param id Evaluation id
	 * @return The list of securities
	 */
	public Collection<Operation> getPortfolioOperations(Long portfolioID, Date fromDate, Date toDate, Date evalCalcDate, Date refEvalCalcDate) {
		// Get name of query to be used
		String queryName = getPortfolioOperationsQueryName();
		
		logger.info("Get operations for portfolio with id [" + portfolioID.toString() + "] between " + fromDate.toString() + " and " + toDate.toString() + ", using " + queryName);
		
		// Prepare results to be returned
		ArrayList<Operation> results = null;
		
		// Get data
		Connection connection = null;
		// Try again (limited to 100 times) to get TNS connection (it sometimes fails when trying to connect to Paper Traded DB)
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, portfolioID);
			preparedStatement.setTimestamp(2, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(toDate.getTime()));
			preparedStatement.setTimestamp(4, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(5, new java.sql.Timestamp(refEvalCalcDate.getTime()));
			preparedStatement.setTimestamp(6, new java.sql.Timestamp(evalCalcDate.getTime()));
			preparedStatement.setLong(7, portfolioID);
			preparedStatement.setTimestamp(8, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(9, new java.sql.Timestamp(toDate.getTime()));
			preparedStatement.setTimestamp(10, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(11, new java.sql.Timestamp(refEvalCalcDate.getTime()));
			preparedStatement.setTimestamp(12, new java.sql.Timestamp(evalCalcDate.getTime()));
			
			// Get results
			results = readDatabasePortfolioOperations(preparedStatement);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during portfolio switches content extraction!",e);
		}
		
		return results;
	}
	
	/**
	 * Method to get security positions for a given evaluation.
	 * @param id Evaluation id
	 * @return The list of securities
	 */
	public void getPortfolioPenalties(Long portfolioID, Date fromDate, Date toDate, Date evalCalcDate, Date refEvalCalcDate, Collection<Position> securities) {
		// Get name of query to be used
		String queryName = getPortfolioPenaltiesWithinPeriodQueryName();
		
		logger.info("Get penalties for portfolio with id [" + portfolioID.toString() + "] between " + fromDate.toString() + " and " + toDate.toString() + ", using " + queryName);
		
		// Get data
		Connection connection = null;
		Integer i = 0;
		while (i <= 100) {
			i ++;
			connection = QueriesProvider.getConnection(sourceConnectionName);
			if (connection == null) {
				logger.info("Get connection failed - Try again (" + i.toString() + ")");
				continue;
			}
			break;
		}
		try {
			// Make SQL query
			PreparedStatement preparedStatement = connection.prepareStatement(QueriesProvider.getQuery(queryName));
			preparedStatement.setLong(1, portfolioID);
//			preparedStatement.setTimestamp(2, new java.sql.Timestamp(fromDate.getTime()));
//			preparedStatement.setTimestamp(3, new java.sql.Timestamp(toDate.getTime()));
			preparedStatement.setTimestamp(2, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(toDate.getTime()));
			preparedStatement.setTimestamp(4, new java.sql.Timestamp(fromDate.getTime()));
			preparedStatement.setTimestamp(5, new java.sql.Timestamp(refEvalCalcDate.getTime()));
			preparedStatement.setTimestamp(6, new java.sql.Timestamp(evalCalcDate.getTime()));
			
			// Get results
			readDatabasePortfolioPenalties(preparedStatement, securities);
			preparedStatement.close();
			connection.close();
		}
		catch (SQLException e) {
			logger.error("A SQL error occured during portfolio switches content extraction!",e);
		}
	}
	
	protected ArrayList<Evaluation> readDatabaseEvaluationsData(
			ArrayList<Evaluation> evaluations, PreparedStatement statement) throws SQLException {
		
		// Execute query
		ResultSet rs = statement.executeQuery();
		
		// Loop on results
		while (rs.next()) {
			// Get evaluation
			Evaluation evaluation = new Evaluation(rs.getLong("EVAL_ID"));
			
			// Set reference evaluation
			Evaluation refEvaluation = new Evaluation(rs.getLong("REF_EVAL_ID"));
			refEvaluation.setNetAUM(rs.getDouble("REF_EVAL_AUMNET"));
			refEvaluation.setDate(rs.getTimestamp("REF_EVAL_DATE"));
//			refEvaluation.setDate(rs.getDate("REF_EVAL_DATE"));
			refEvaluation.setCalculationDate(rs.getTimestamp("REF_EVAL_CALCULATION_DATE"));
//			refEvaluation.setCalculationDate(rs.getDate("REF_EVAL_CALCULATION_DATE"));
			evaluation.setReferenceEvaluation(refEvaluation);
			
			// Set other information
			evaluation.setDate(rs.getTimestamp("EVAL_DATE"));
//			evaluation.setDate(rs.getDate("EVAL_DATE"));
			evaluation.setStatus(rs.getString("EVAL_STATUS"));
			evaluation.setCalculationDate(rs.getTimestamp("EVAL_CALCULATION_DATE"));
//			evaluation.setCalculationDate(rs.getDate("EVAL_CALCULATION_DATE"));
			evaluation.setBuyShares(rs.getDouble("EVAL_CONTRIBUTIONS"));
			evaluation.setSellShares(rs.getDouble("EVAL_WITHDRAWALS"));
			
			// Add evaluation to array to be returned
			evaluations.add(evaluation);
		}
		rs.close();
		
		return evaluations;
	}

	protected void readDatabaseEvaluation(PreparedStatement statement, Evaluation evaluation) throws SQLException {
		
		// Execute query
		ResultSet rs = statement.executeQuery();
		
		// Loop on results
		while (rs.next()) {
			// Set portfolio
			Portfolio portfolio = new Portfolio(rs.getLong("PORTFOLIO_ID"));
			portfolio.setCode(rs.getString("PORTFOLIOCODE"));
			portfolio.setCurrency(rs.getString("ISOCURRENCY"));
			evaluation.setPortfolio(portfolio);
			
			// Set reference evaluation
			Evaluation refEvaluation = new Evaluation(rs.getLong("REF_EVAL_ID"));
			refEvaluation.setNetAUM(rs.getDouble("REF_EVAL_AUMNET"));
			refEvaluation.setDate(rs.getTimestamp("REF_EVAL_DATE"));
//			refEvaluation.setDate(rs.getDate("REF_EVAL_DATE"));
			refEvaluation.setCalculationDate(rs.getTimestamp("REF_EVAL_CALCULATION_DATE"));
//			refEvaluation.setCalculationDate(rs.getDate("REF_EVAL_CALCULATION_DATE"));
			evaluation.setReferenceEvaluation(refEvaluation);
			
			// Set other information
			evaluation.setDate(rs.getTimestamp("EVAL_DATE"));
//			evaluation.setDate(rs.getDate("EVAL_DATE"));
			evaluation.setStatus(rs.getString("EVAL_STATUS"));
			evaluation.setCalculationDate(rs.getTimestamp("EVAL_CALCULATION_DATE"));
//			evaluation.setCalculationDate(rs.getDate("EVAL_CALCULATION_DATE"));
			evaluation.setBuyShares(rs.getDouble("EVAL_CONTRIBUTIONS"));
			evaluation.setSellShares(rs.getDouble("EVAL_WITHDRAWALS"));
			
			break;
		}
		rs.close();
	}

	/**
	 * Get all security positions for a given evaluation
	 * @param statement
	 * @return The list of security positions for this evaluation
	 * @throws SQLException
	 */
	protected ArrayList<Position> readDatabaseEvaluationSecuritiesData(PreparedStatement statement) throws SQLException {
		// Prepare array to be returned
		ArrayList<Position> securities = new ArrayList<Position>();
		
		// Execute query
		ResultSet rs = statement.executeQuery();
		
		// Loop on results
		while (rs.next()) {
			// Get security
			Position security = new Position(rs.getLong("SECURITY_ID"));
			
			// Set reference evaluation
			Position refPosition = new Position();
			refPosition.setLocalAmount(rs.getDouble("REF_LOCAL_AMOUNT"));
			refPosition.setEqualizationFactorAmount(rs.getDouble("REF_EQUALIZATION_FACTOR_AMOUNT"));
			Double refSpotRate = rs.getDouble("REF_SPOT_RATE");
			if (refSpotRate == 0) {
				refPosition.setNoSpotRate();
			}
			else {
				refPosition.setSpotRate(refSpotRate);
			}
			refPosition.setNAV(rs.getDouble("REF_NAV_VALUE"));
			int refExists = rs.getInt("REF_POSITION_EXISTS");
			if (refExists != 1) {
				refPosition.setNotExists();
			}
			security.setReferencePosition(refPosition);
			
			// Set other information
			Long intranetID = rs.getLong("INTRANET_ID");
			if (intranetID == 0) {
				security.setIntranetID(null);
			}
			else {
				security.setIntranetID(intranetID);
			}
			security.setName(rs.getString("SECURITY_NAME"));
			security.setCurrency(rs.getString("SECURITY_CURRENCY"));
			security.setLocalAmount(rs.getDouble("LOCAL_AMOUNT"));
			security.setNAV(rs.getDouble("NAV_VALUE"));
			security.setEqualizationFactorAmount(rs.getDouble("EQUALIZATION_FACTOR_AMOUNT"));
			
			// Add evaluation to array to be returned
			securities.add(security);
		}
		rs.close();
		
		return securities;
	}

	/**
	 * Get all security positions for a given evaluation
	 * @param statement
	 * @return The list of security positions for this evaluation
	 * @throws SQLException
	 */
	protected ArrayList<Switch> readDatabasePortfolioSwitches(PreparedStatement statement) throws SQLException {
		// Prepare array to be returned
		ArrayList<Switch> switches = new ArrayList<Switch>();
		
		// Execute query
		ResultSet rs = statement.executeQuery();
		
		// Loop on results
		while (rs.next()) {
			// Get security
			Switch mySwitch = new Switch();
			
			// Set other information
			mySwitch.setRedemptionSecurityID(rs.getLong("REDEMPTION_SECURITY_ID"));
			mySwitch.setRedemptionAmount(rs.getDouble("REDEMPTION_AMOUNT"));
			mySwitch.setRedemptionQuantity(rs.getDouble("REDEMPTION_QUANTITY"));
			mySwitch.setRedemptionNAV(rs.getDouble("REDEMPTION_NAV"));
			mySwitch.setSubscriptionSecurityID(rs.getLong("SUBSCRIPTION_SECURITY_ID"));
			mySwitch.setSubscriptionQuantity(rs.getDouble("SUBSCRIPTION_QUANTITY"));
			mySwitch.setSubscriptionNAV(rs.getDouble("SUBSCRIPTION_NAV"));
			
			// Add evaluation to array to be returned
			switches.add(mySwitch);
		}
		rs.close();
		
		return switches;
	}

	/**
	 * Get all security positions for a given evaluation
	 * @param statement
	 * @return The list of security positions for this evaluation
	 * @throws SQLException
	 */
	protected ArrayList<Operation> readDatabasePortfolioOperations(PreparedStatement statement) throws SQLException {
		// Prepare array to be returned
		ArrayList<Operation> operations = new ArrayList<Operation>();
		
		// Execute query
		ResultSet rs = statement.executeQuery();
		
		// Loop on results
		while (rs.next()) {
			// Get security
			Operation operation = new Operation(rs.getLong("SECURITY_ID"));
			
			// Set other information
			operation.setAmount(rs.getDouble("TOTAL_OP_AMOUNT"));
			
			// Add evaluation to array to be returned
			operations.add(operation);
		}
		rs.close();
		
		return operations;
	}

	/**
	 * Get all security positions for a given evaluation
	 * @param statement
	 * @return The list of security positions for this evaluation
	 * @throws SQLException
	 */
	protected String readDatabasePortfolio(PreparedStatement statement) throws SQLException {
		// Prepare array to be returned
		String portfolioCurrency = "";
		
		// Execute query
		ResultSet rs = statement.executeQuery();
		
		// Loop on results
		while (rs.next()) {
			portfolioCurrency = rs.getString("ISOCURRENCY");
		}
		rs.close();
		
		return portfolioCurrency;
	}

	/**
	 * Get all security positions for a given evaluation
	 * @param statement
	 * @return The list of security positions for this evaluation
	 * @throws SQLException
	 */
	protected void readDatabasePortfolioDetails(PreparedStatement statement, Portfolio portfolio) throws SQLException {
		// Execute query
		ResultSet rs = statement.executeQuery();
		
		// Loop on results
		while (rs.next()) {
			portfolio.setCurrency(rs.getString("ISOCURRENCY"));
			portfolio.setCode(rs.getString("PORTFOLIOCODE"));
		}
		rs.close();
	}

	/**
	 * Get all security positions for a given evaluation
	 * @param statement
	 * @return The list of security positions for this evaluation
	 * @throws SQLException
	 */
	protected void readDatabasePortfolioPenalties(PreparedStatement statement, Collection<Position> securities) throws SQLException {
		// Execute query
		ResultSet rs = statement.executeQuery();
		
		// Loop on results
		while (rs.next()) {
			Long thisSecurityID = rs.getLong("SECURITY_ID");
			
			// Search this fund in list
			for (Position security : securities) {
				if (thisSecurityID.equals(security.getId())) {
					// Match found
					security.setPenaltyFees(rs.getDouble("TOTAL_PEN_AMOUNT"));
					continue;
				}
			}
		}
		rs.close();
		
	}

}
