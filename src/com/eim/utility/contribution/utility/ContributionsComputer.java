package com.eim.utility.contribution.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.eim.utility.common.model.EIMCompanies;
import com.eim.utility.common.model.QualityStatus;
import com.eim.utility.contribution.impl.WebfolioContentExtractor;
import com.eim.utility.contribution.impl.WebfolioContentExtractorFactory;
import com.eim.utility.contribution.model.Contribution;
import com.eim.utility.contribution.model.Evaluation;
import com.eim.utility.contribution.model.Operation;
import com.eim.utility.contribution.model.Portfolio;
import com.eim.utility.contribution.model.Position;
import com.eim.utility.contribution.model.Switch;

public class ContributionsComputer {

	/**
	 * Logging utility
	 */
	private static Logger log = Logger.getLogger(ContributionsComputer.class);
	
	/**
	 * Compute contributions for portfolio (id = entityId) between fromDate and toDate.
	 * @param fromDate
	 * @param toDate
	 * @param entityId
	 * @return
	 */
	public static List<Contribution> computePortfolioContributionsWithinPeriod(Date fromDate, Date toDate, Long entityId, EIMCompanies eimCompany) {
		//
		log.info("Simple contribution computation - Working on portfolio with id [" + entityId.toString() + "]");
		long fullStart = System.currentTimeMillis();
//		long start = System.currentTimeMillis();
		
		// Initialize value to be returned
		ArrayList<Contribution> results = new ArrayList<Contribution>();
		
		// Interface to query DB
//		ValuationContentExtractor valuationContentExtractor = ValuationContentExtractorFactory.getValuationContentExtractor(EntityType.PORTFOLIO);
		WebfolioContentExtractor webfolioContentExtractor = WebfolioContentExtractorFactory.getWebfolioContentExtractor(eimCompany);
		
		// Get portfolio details
		Portfolio portfolio = new Portfolio(entityId);
		webfolioContentExtractor.getPortfolioDetails(portfolio);
//		portfolio.setCurrency(currency);
		
		// Get all valuations
		Collection<Evaluation> evaluations = new ArrayList<Evaluation>();
		// If period is defined
		if (fromDate != null && toDate != null) {
			// Get all valuations in period
			evaluations = webfolioContentExtractor.getPortfolioEvaluationsWithinPeriod(entityId, fromDate, toDate);
		}
		// Else, if only "from date" is defined
		else if (fromDate != null) {
			// Get all valuations in period
			evaluations = webfolioContentExtractor.getPortfolioEvaluationsAfterDate(entityId, fromDate);
		}
		// Else, if only "to date" is defined
		else if (toDate != null) {
			// Get all valuations in period
			evaluations = webfolioContentExtractor.getPortfolioEvaluationsBeforeDate(entityId, toDate);
		}
		// Else, none is defined
		else {
			// Get all valuations in period
			evaluations = webfolioContentExtractor.getPortfolioEvaluations(entityId);
		}
		
		// For each evaluation
		for (Evaluation evaluation : evaluations) {
			// Get reference evaluation
			Evaluation refEvaluation = evaluation.getReferenceEvaluation();
			
			// Get t and t-1
			Date refEvaluationDate = refEvaluation.getDate(); // t-1
			Date evaluationDate = evaluation.getDate(); // t
			
			// Get all securities concerned by these evaluations
			Collection<Position> securities = webfolioContentExtractor.getEvaluationSecurities(evaluation.getId(), refEvaluation.getId());
			
			// Check if there is any switch
			Collection<Switch> switches = webfolioContentExtractor.getPortfolioSwitches(entityId, refEvaluationDate, evaluationDate, evaluation.getCalculationDate(), refEvaluation.getCalculationDate());
			
			// Check if there is any operation (not switch)
			Collection<Operation> operations = webfolioContentExtractor.getPortfolioOperations(entityId, refEvaluationDate, evaluationDate, evaluation.getCalculationDate(), refEvaluation.getCalculationDate());
			
			// Check if there is any penalty
			webfolioContentExtractor.getPortfolioPenalties(entityId, refEvaluationDate, evaluationDate, evaluation.getCalculationDate(), refEvaluation.getCalculationDate(), securities);
			
			// For each security
			for (Position security : securities) {
				
				computeSecurityContribution(portfolio, evaluation, security, switches, operations, results, eimCompany);
				
//				Long securityID = security.getId();
//				
//				// Make contribution
//				Contribution contrib = new Contribution();
//				
//				///// 2012-10-30: Before changing switch handling
//				
////				// If position in switches
////				Boolean hasSwitch = false;
////				for (Switch thisSwitch : switches) {
////					// If position has switch redemption
////					if (securityID.equals(thisSwitch.getRedemptionSecurityID())) {
//////						if (securityID == 19289) {
//////							log.info("has red switch");
//////						}
////						hasSwitch = true;
////						// Set position amount
////						security.setCalculatedAmount(security.getLocalAmount() - security.getReferencePosition().getLocalAmount() + thisSwitch.getRedemptionAmount());
////						break;
////					}
////					
////					// If position has switch subscription
////					if (securityID.equals(thisSwitch.getSubscriptionSecurityID())) {
//////						if (securityID == 19289) {
//////							log.info("has sub switch");
//////						}
////						hasSwitch = true;
////						// Set position amount
////						security.setCalculatedAmount(thisSwitch.getSubscriptionQuantity() * thisSwitch.getSubscriptionNAV() - thisSwitch.getRedemptionQuantity() * thisSwitch.getRedemptionNAV());
////						break;
////					}
////				}
////				
////				// Else
////				if (!hasSwitch) {
//////					if (securityID == 19289) {
//////						log.info("not has switch");
//////					}
////					// Set position amount
////					security.setCalculatedAmount(security.getLocalAmount() - security.getReferencePosition().getLocalAmount());
////					// Check if there is any operation
////					for (Operation operation : operations) {
////						// If position has operations
////						if (securityID.equals(operation.getId())) {
////							// Set position amount
////							security.setCalculatedAmount(security.getCalculatedAmount() + operation.getAmount());
////							break;
////						}
////					}
////				}
//				
//				///// 2012-10-30: After changing switch handling
//				
//				// If position in switches
//				Boolean hasSwitch = false;
//				for (Switch thisSwitch : switches) {
//					// If position has switch redemption
//					if (securityID.equals(thisSwitch.getRedemptionSecurityID())) {
////						if (securityID.equals(debugSecurityID)) {
////							log.info("has red switch : " + thisSwitch.getRedemptionAmount().toString());
////						}
//						hasSwitch = true;
//						// Set position amount
//						security.setCalculatedAmount(security.getCalculatedAmount() + security.getLocalAmount() - security.getReferencePosition().getLocalAmount() + thisSwitch.getRedemptionAmount());
//						continue;
//					}
//					
//					// If position has switch subscription
//					if (securityID.equals(thisSwitch.getSubscriptionSecurityID())) {
////						if (securityID.equals(debugSecurityID)) {
////							log.info("has sub switch : subquantity = " + thisSwitch.getSubscriptionQuantity().toString() + " / subnav = " +  thisSwitch.getSubscriptionNAV().toString() + " / redquantity = " + thisSwitch.getRedemptionQuantity().toString() + " / rednav = " + thisSwitch.getRedemptionNAV().toString());
////						}
//						hasSwitch = true;
//						// Set position amount
////						security.setCalculatedAmount(security.getCalculatedAmount() + thisSwitch.getSubscriptionQuantity() * thisSwitch.getSubscriptionNAV() - thisSwitch.getRedemptionQuantity() * thisSwitch.getRedemptionNAV());
//						security.setCalculatedAmount(security.getCalculatedAmount() + thisSwitch.getSubscriptionQuantity() * security.getNAV() - thisSwitch.getRedemptionQuantity() * thisSwitch.getRedemptionNAV());
////						if (securityID.equals(debugSecurityID)) {
////							log.info("after sub switch added : security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
////						}
//						
//						// If switch on existing series, remove switch quantity x security NAV
//						if (security.getReferencePosition().getExists()) {
//							hasSwitch = false;
////							if (securityID.equals(debugSecurityID)) {
////								log.info("sub switch on existing series: remove switch quantity = " + thisSwitch.getSubscriptionQuantity().toString() + " * security NAV = " + security.getNAV().toString());
////							}
//							security.setCalculatedAmount(security.getCalculatedAmount() - thisSwitch.getSubscriptionQuantity() * security.getNAV());
////							if (securityID.equals(debugSecurityID)) {
////								log.info("after sub switch on existing series: security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
////							}
//						}
////						break;
//					}
//				}
//				
////				// If position in switches
////				Boolean hasSwitch = false;
////				for (Switch thisSwitch : switches) {
////					// If position has switch redemption
////					if (securityID.equals(thisSwitch.getRedemptionSecurityID())) {
//////						if (securityID == 19289) {
//////							log.info("has red switch");
//////						}
////						hasSwitch = true;
////						// Set position amount
////						security.setCalculatedAmount(security.getCalculatedAmount() + security.getLocalAmount() - security.getReferencePosition().getLocalAmount() + thisSwitch.getRedemptionAmount());
////						continue;
////					}
////					
////					// If position has switch subscription
////					if (securityID.equals(thisSwitch.getSubscriptionSecurityID())) {
//////						if (securityID == 19289) {
//////							log.info("has sub switch");
//////						}
////						hasSwitch = true;
////						// Set position amount
////						security.setCalculatedAmount(security.getCalculatedAmount() + thisSwitch.getSubscriptionQuantity() * thisSwitch.getSubscriptionNAV() - thisSwitch.getRedemptionQuantity() * thisSwitch.getRedemptionNAV());
////						
////						// If switch on existing series, remove switch quantity x security NAV
////						if (security.getReferencePosition().getExists()) {
////							security.setCalculatedAmount(security.getCalculatedAmount() - thisSwitch.getSubscriptionQuantity() * security.getNAV());
////						}
//////						break;
////					}
////				}
//				
//				// Else
//				if (!hasSwitch) {
////					if (securityID.equals(debugSecurityID)) {
////						log.info("not has switch");
////					}
//				
//				// DEBUG
////				log.info("security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
////				log.info("security.getLocalAmount() = " + security.getLocalAmount().toString());
////				log.info("security.getReferencePosition().getLocalAmount() = " + security.getReferencePosition().getLocalAmount().toString());
//					// Set position amount
//					security.setCalculatedAmount(security.getCalculatedAmount() + security.getLocalAmount() - security.getReferencePosition().getLocalAmount());
//				}
//				
////				if (securityID.equals(debugSecurityID)) {
////					log.info("Going to check operation. security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
////				}
//				
//				// Check if there is any operation
//				for (Operation operation : operations) {
//					// If position has operations
//					if (securityID.equals(operation.getId())) {
////						if (securityID.equals(debugSecurityID)) {
////							log.info("position has operation : " + operation.getAmount().toString());
////						}
//						// Set position amount
//						security.setCalculatedAmount(security.getCalculatedAmount() + operation.getAmount());
////						break;
//					}
//				}
//				
////				// Else
////				if (!hasSwitch) {
//////					if (securityID == 19289) {
//////						log.info("not has switch");
//////					}
////					// Set position amount
////					security.setCalculatedAmount(security.getCalculatedAmount() + security.getLocalAmount() - security.getReferencePosition().getLocalAmount());
////				}
////				
////				// Check if there is any operation
////				for (Operation operation : operations) {
////					// If position has operations
////					if (securityID.equals(operation.getId())) {
////						// Set position amount
////						security.setCalculatedAmount(security.getCalculatedAmount() + operation.getAmount());
//////						break;
////					}
////				}
//				
//				///// 2012-10-30: Switch handling - END
//				
//				// If penalty fees
//				security.setCalculatedAmount(security.getCalculatedAmount() - security.getPenaltyFees());
////				if (security.getPenaltyFees() != 0) {
////				}
//				
////				if (securityID.equals(debugSecurityID)) {
////					log.info("Add penalty fees : security.getPenaltyFees() = " + security.getPenaltyFees().toString());
////				}
////				
////				if (securityID.equals(debugSecurityID)) {
////					log.info("Remove EF at t : security.getEqualizationFactorAmount() = " + security.getEqualizationFactorAmount().toString());
////					log.info("Add EF at t-1 : security.getReferencePosition().getEqualizationFactorAmount() = " + security.getReferencePosition().getEqualizationFactorAmount().toString());
////				}
//				
//				// If equalization factor
//				Double equalizationFactor = security.getEqualizationFactorAmount() - security.getReferencePosition().getEqualizationFactorAmount();
//				security.setCalculatedAmount(security.getCalculatedAmount() - Math.abs(equalizationFactor));
//				
////				if (securityID == 19289) {
////					log.info("Add EF: calc amount = " + security.getCalculatedAmount().toString());
////				}
//				
//				// If position currency != portfolio currency
//				if (!security.getCurrency().equals(portfolio.getCurrency())) {
////					if (securityID.equals(debugSecurityID)) {
////						log.info("Change currency (spot): " + security.getReferencePosition().getSpotRate().toString());
////					}
//			
//					if (security.getReferencePosition().getHasSpotRate()) {
//						security.setCalculatedAmount(security.getCalculatedAmount() * security.getReferencePosition().getSpotRate());
//					}
//				}
//				
//				// Calculate divider
//				Double divider = evaluation.getBuyShares() + evaluation.getSellShares() + evaluation.getReferenceEvaluation().getNetAUM();
////				if (securityID.equals(debugSecurityID)) {
////					log.info("divider = " + divider.toString());
////				}
//				if (divider == 0) {
//					log.error("Cannot calculate contribution: divider is equal to 0. (evaluation ID = " + evaluation.getId().toString() + ")");
//					continue;
//				}
//				
//////				if (securityID == 19289) {
//////					log.info("calc amount = " + security.getCalculatedAmount().toString());
//////				}
////				
////				// If penalty fees
////				security.setCalculatedAmount(security.getCalculatedAmount() - security.getPenaltyFees());
//////				if (security.getPenaltyFees() != 0) {
//////				}
////				
//////				if (securityID == 19289) {
//////					log.info("Add penalty fees: calc amount = " + security.getCalculatedAmount().toString());
//////				}
////				
////				// If equalization factor
////				security.setCalculatedAmount(security.getCalculatedAmount()  - security.getEqualizationFactorAmount() + security.getReferencePosition().getEqualizationFactorAmount());
////				
//////				if (securityID == 19289) {
//////					log.info("Add EF: calc amount = " + security.getCalculatedAmount().toString());
//////				}
////				
////				// If position currency != portfolio currency
////				if (!security.getCurrency().equals(portfolio.getCurrency())) {
////					if (security.getReferencePosition().getHasSpotRate()) {
////						security.setCalculatedAmount(security.getCalculatedAmount() * security.getReferencePosition().getSpotRate());
////					}
////				}
////				
//////				if (securityID == 19289) {
//////					log.info("Change currency (spot): calc amount = " + security.getCalculatedAmount().toString());
//////				}
////				
////				// Calculate divider
////				Double divider = evaluation.getBuyShares() + evaluation.getSellShares() + evaluation.getReferenceEvaluation().getNetAUM();
////				if (divider == 0) {
////					log.error("Cannot calculate contribution: divider is equal to 0. (evaluation ID = " + evaluation.getId().toString() + ")");
////					continue;
////				}
//				
////				if (securityID == 19289) {
////					log.info("divider = " + divider.toString());
////				}
//				
//				// Contribution
//				contrib.setApplicationDate(evaluation.getDate());
//				contrib.setComputationDate(new Date());
//				if (evaluation.getStatus().equals("EOM")) {
//					contrib.setStatus(QualityStatus.FINAL);
//				}
//				else {
//					contrib.setStatus(QualityStatus.ESTIMATED);
//				}
//				contrib.setValue(security.getCalculatedAmount() / divider);
//				contrib.setEvaluationID(evaluation.getId());
//				contrib.setEvaluationDate(evaluation.getCalculationDate());
//				contrib.setPortfolioID(entityId);
//				contrib.setPortfolioCode(portfolio.getCode());
//				contrib.setCompany("EIM SWITZERLAND");
//				contrib.setSecurityID(securityID);
//				contrib.setIntranetID(security.getIntranetID());
////				contrib.setFundName(security.getName());
//				
////				if (securityID == 19289) {
////					log.info("contrib = " + contrib.getValue().toString());
////				}
//				
//				// Add this contribution to be returned
////				if (contrib.getValue().isNaN()) {
////					continue;
////				}
//				results.add(contrib);
			}
			
		}
		
		log.info("Simple contribution computation - Portfolio with id [" + entityId.toString() + "] computed in " + (System.currentTimeMillis() - fullStart) + "ms.");
		return results;
	}
	
	/**
	 * Compute contributions for evaluation (id = evaluationID).
	 * @param evaluationID
	 * @return
	 */
	public static List<Contribution> computeEvaluationContributions(Long evaluationID, EIMCompanies eimCompany) {
		//
		
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! DEBUG - Log info for this security
//		Long debugSecurityID = new Long(17704);
		
		log.info("Simple contribution computation - Working on evaluation with id [" + evaluationID.toString() + "]");
		long fullStart = System.currentTimeMillis();
//		long start = System.currentTimeMillis();
		
		// Initialize value to be returned
		ArrayList<Contribution> results = new ArrayList<Contribution>();
		
		// Interface to query DB
//		ValuationContentExtractor valuationContentExtractor = ValuationContentExtractorFactory.getValuationContentExtractor(EntityType.PORTFOLIO);
		WebfolioContentExtractor webfolioContentExtractor = WebfolioContentExtractorFactory.getWebfolioContentExtractor(eimCompany);
		
		// Get evaluation details
		Evaluation evaluation = webfolioContentExtractor.getEvaluation(evaluationID);
		Portfolio portfolio = evaluation.getPortfolio();
		Long portfolioID = portfolio.getId();
		
		// Get reference evaluation
		Evaluation refEvaluation = evaluation.getReferenceEvaluation();
		
		// Get t and t-1
		Date refEvaluationDate = refEvaluation.getDate(); // t-1
		Date evaluationDate = evaluation.getDate(); // t
		
		// Get all securities concerned by these evaluations
		Collection<Position> securities = webfolioContentExtractor.getEvaluationSecurities(evaluation.getId(), refEvaluation.getId());
		
		// Check if there is any switch
		Collection<Switch> switches = webfolioContentExtractor.getPortfolioSwitches(portfolioID, refEvaluationDate, evaluationDate, evaluation.getCalculationDate(), refEvaluation.getCalculationDate());
		
		// Check if there is any operation (not switch)
		String str2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(refEvaluation.getCalculationDate());
		log.info("Going to get operations... refEvaluation.getCalculationDate() = " + str2);
		Collection<Operation> operations = webfolioContentExtractor.getPortfolioOperations(portfolioID, refEvaluationDate, evaluationDate, evaluation.getCalculationDate(), refEvaluation.getCalculationDate());
		
		// Check if there is any penalty
		webfolioContentExtractor.getPortfolioPenalties(portfolioID, refEvaluationDate, evaluationDate, evaluation.getCalculationDate(), refEvaluation.getCalculationDate(), securities);
		
		// For each security
		for (Position security : securities) {
			
			computeSecurityContribution(portfolio, evaluation, security, switches, operations, results, eimCompany);
			
//			Long securityID = security.getId();
//			
//			if (securityID.equals(debugSecurityID)) {
//				log.info("!!!!!!!!!!!!!!!!!!!!! Treating security ID = " + debugSecurityID.toString() + " !!!!!!!!!!!!!!!!!!!!!");
//				log.info("security.getLocalAmount() = " + security.getLocalAmount().toString());
//				log.info("security.getReferencePosition().getLocalAmount() = " + security.getReferencePosition().getLocalAmount().toString());
//			}
//			
//			// Make contribution
//			Contribution contrib = new Contribution();
//			
//			///// 2012-10-30: Before changing switch handling
//			
////			// If position in switches
////			Boolean hasSwitch = false;
////			for (Switch thisSwitch : switches) {
////				// If position has switch redemption
////				if (securityID.equals(thisSwitch.getRedemptionSecurityID())) {
//////					if (securityID == 19289) {
//////						log.info("has red switch");
//////					}
////					hasSwitch = true;
////					// Set position amount
////					security.setCalculatedAmount(security.getLocalAmount() - security.getReferencePosition().getLocalAmount() + thisSwitch.getRedemptionAmount());
////					break;
////				}
////				
////				// If position has switch subscription
////				if (securityID.equals(thisSwitch.getSubscriptionSecurityID())) {
//////					if (securityID == 19289) {
//////						log.info("has sub switch");
//////					}
////					hasSwitch = true;
////					// Set position amount
////					security.setCalculatedAmount(thisSwitch.getSubscriptionQuantity() * thisSwitch.getSubscriptionNAV() - thisSwitch.getRedemptionQuantity() * thisSwitch.getRedemptionNAV());
////					break;
////				}
////			}
////			
////			// Else
////			if (!hasSwitch) {
//////				if (securityID == 19289) {
//////					log.info("not has switch");
//////				}
////				// Set position amount
////				security.setCalculatedAmount(security.getLocalAmount() - security.getReferencePosition().getLocalAmount());
////			}
////			
////			// Check if there is any operation
////			for (Operation operation : operations) {
////				// If position has operations
////				if (securityID.equals(operation.getId())) {
////					// Set position amount
////					security.setCalculatedAmount(security.getCalculatedAmount() + operation.getAmount());
////					break;
////				}
////			}
//			
//			if (securityID.equals(debugSecurityID)) {
//				log.info("Going to check switch. security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
//			}
//			
//			///// 2012-10-30: After changing switch handling
//			
//			// If position in switches
//			Boolean hasSwitch = false;
//			for (Switch thisSwitch : switches) {
//				// If position has switch redemption
//				if (securityID.equals(thisSwitch.getRedemptionSecurityID())) {
//					if (securityID.equals(debugSecurityID)) {
//						log.info("has red switch : " + thisSwitch.getRedemptionAmount().toString());
//					}
//					hasSwitch = true;
//					// Set position amount
//					security.setCalculatedAmount(security.getCalculatedAmount() + security.getLocalAmount() - security.getReferencePosition().getLocalAmount() + thisSwitch.getRedemptionAmount());
//					continue;
//				}
//				
//				// If position has switch subscription
//				if (securityID.equals(thisSwitch.getSubscriptionSecurityID())) {
//					if (securityID.equals(debugSecurityID)) {
//						log.info("has sub switch : subquantity = " + thisSwitch.getSubscriptionQuantity().toString() + " / subnav = " +  thisSwitch.getSubscriptionNAV().toString() + " / redquantity = " + thisSwitch.getRedemptionQuantity().toString() + " / rednav = " + thisSwitch.getRedemptionNAV().toString());
//					}
//					hasSwitch = true;
//					// Set position amount
////					security.setCalculatedAmount(security.getCalculatedAmount() + thisSwitch.getSubscriptionQuantity() * thisSwitch.getSubscriptionNAV() - thisSwitch.getRedemptionQuantity() * thisSwitch.getRedemptionNAV());
//					security.setCalculatedAmount(security.getCalculatedAmount() + thisSwitch.getSubscriptionQuantity() * security.getNAV() - thisSwitch.getRedemptionQuantity() * thisSwitch.getRedemptionNAV());
//					if (securityID.equals(debugSecurityID)) {
//						log.info("after sub switch added : security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
//					}
//					
//					// If switch on existing series, remove switch quantity x security NAV
//					if (security.getReferencePosition().getExists()) {
//						hasSwitch = false;
//						if (securityID.equals(debugSecurityID)) {
//							log.info("sub switch on existing series: remove switch quantity = " + thisSwitch.getSubscriptionQuantity().toString() + " * security NAV = " + security.getNAV().toString());
//						}
//						security.setCalculatedAmount(security.getCalculatedAmount() - thisSwitch.getSubscriptionQuantity() * security.getNAV());
//						if (securityID.equals(debugSecurityID)) {
//							log.info("after sub switch on existing series: security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
//						}
//					}
////					break;
//				}
//			}
//			
//			if (securityID.equals(debugSecurityID)) {
//				log.info("After check switch. security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
//			}
//			
//			// Else
//			if (!hasSwitch) {
//				if (securityID.equals(debugSecurityID)) {
//					log.info("not has switch");
//				}
//			
//			// DEBUG
////			log.info("security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
////			log.info("security.getLocalAmount() = " + security.getLocalAmount().toString());
////			log.info("security.getReferencePosition().getLocalAmount() = " + security.getReferencePosition().getLocalAmount().toString());
//				// Set position amount
//				security.setCalculatedAmount(security.getCalculatedAmount() + security.getLocalAmount() - security.getReferencePosition().getLocalAmount());
//			}
//			
//			if (securityID.equals(debugSecurityID)) {
//				log.info("Going to check operation. security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
//			}
//			
//			// Check if there is any operation
//			for (Operation operation : operations) {
//				// If position has operations
//				if (securityID.equals(operation.getId())) {
//					if (securityID.equals(debugSecurityID)) {
//						log.info("position has operation : " + operation.getAmount().toString());
//					}
//					// Set position amount
//					security.setCalculatedAmount(security.getCalculatedAmount() + operation.getAmount());
////					break;
//				}
//			}
//			
//			if (securityID.equals(debugSecurityID)) {
//				log.info("After check operation. security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
//			}
//			
//			///// 2012-10-30: Switch handling - END
//			
////			if (securityID == 19289) {
////				log.info("calc amount = " + security.getCalculatedAmount().toString());
////			}
//			
//			// If penalty fees
//			security.setCalculatedAmount(security.getCalculatedAmount() - security.getPenaltyFees());
////			if (security.getPenaltyFees() != 0) {
////			}
//			
//			if (securityID.equals(debugSecurityID)) {
//				log.info("Add penalty fees : security.getPenaltyFees() = " + security.getPenaltyFees().toString());
//			}
//			
//			if (securityID.equals(debugSecurityID)) {
//				log.info("Remove EF at t : security.getEqualizationFactorAmount() = " + security.getEqualizationFactorAmount().toString());
//				log.info("Add EF at t-1 : security.getReferencePosition().getEqualizationFactorAmount() = " + security.getReferencePosition().getEqualizationFactorAmount().toString());
//			}
//			
//			// If equalization factor
//			Double equalizationFactor = security.getEqualizationFactorAmount() - security.getReferencePosition().getEqualizationFactorAmount();
//			security.setCalculatedAmount(security.getCalculatedAmount() - Math.abs(equalizationFactor));
//			
////			if (securityID == 19289) {
////				log.info("Add EF: calc amount = " + security.getCalculatedAmount().toString());
////			}
//			
//			// If position currency != portfolio currency
//			if (!security.getCurrency().equals(portfolio.getCurrency())) {
//				if (securityID.equals(debugSecurityID)) {
//					log.info("Change currency (spot): " + security.getReferencePosition().getSpotRate().toString());
//				}
//		
//				if (security.getReferencePosition().getHasSpotRate()) {
//					security.setCalculatedAmount(security.getCalculatedAmount() * security.getReferencePosition().getSpotRate());
//				}
//			}
//			
//			// Calculate divider
//			Double divider = evaluation.getBuyShares() + evaluation.getSellShares() + evaluation.getReferenceEvaluation().getNetAUM();
//			if (securityID.equals(debugSecurityID)) {
//				log.info("divider = " + divider.toString());
//			}
//			if (divider == 0) {
//				log.error("Cannot calculate contribution: divider is equal to 0. (evaluation ID = " + evaluation.getId().toString() + ")");
//				continue;
//			}
//			
////			if (securityID == 19289) {
////				log.info("divider = " + divider.toString());
////			}
//			
//			// Contribution
//			contrib.setApplicationDate(evaluation.getDate());
//			contrib.setComputationDate(new Date());
//			if (evaluation.getStatus().equals("EOM")) {
//				contrib.setStatus(QualityStatus.FINAL);
//			}
//			else {
//				contrib.setStatus(QualityStatus.ESTIMATED);
//			}
//			contrib.setValue(security.getCalculatedAmount() / divider);
//			
//			if (securityID.equals(debugSecurityID)) {
//				log.info("contrib.value = " + contrib.getValue().toString());
//			}
//			
//			contrib.setEvaluationID(evaluation.getId());
//			contrib.setEvaluationDate(evaluation.getCalculationDate());
//			contrib.setPortfolioID(portfolioID);
//			contrib.setPortfolioCode(portfolio.getCode());
//			contrib.setCompany("EIM SWITZERLAND");
//			contrib.setSecurityID(securityID);
//			contrib.setIntranetID(security.getIntranetID());
////			contrib.setFundName(security.getName()); // Do not set it if you don't want to save it in DB
//			
////			if (securityID == 19289) {
////				log.info("contrib = " + contrib.getValue().toString());
////			}
//			
//			// Add this contribution to be returned
////			if (contrib.getValue().isNaN()) {
////				continue;
////			}
//			results.add(contrib);
		}
		
		log.info("Simple contribution computation - Evaluation with id [" + evaluationID.toString() + "] computed in " + (System.currentTimeMillis() - fullStart) + "ms.");
		return results;
	}
	
	/**
	 * Compute contributions for evaluation (id = evaluationID).
	 * @param evaluationID
	 * @return
	 */
	public static void computeSecurityContribution(Portfolio portfolio, Evaluation evaluation, Position security, Collection<Switch> switches, Collection<Operation> operations, ArrayList<Contribution> results, EIMCompanies eimCompany) {
		//
		
		// DEBUG - Log info for this security
		Long debugSecurityID = new Long(13181);
		
//		log.info("Security contribution computation - Working on security with id [" + security.getId().toString() + "]");
//		long fullStart = System.currentTimeMillis();
//		long start = System.currentTimeMillis();
		
		Long securityID = security.getId();
		
		if (securityID.equals(debugSecurityID)) {
			log.info("!!!!!!!!!!!!!!!!!!!!! Treating security ID = " + debugSecurityID.toString() + " !!!!!!!!!!!!!!!!!!!!!");
			log.info("security.getLocalAmount() = " + security.getLocalAmount().toString());
			log.info("security.getReferencePosition().getLocalAmount() = " + security.getReferencePosition().getLocalAmount().toString());
		}
		
		// Make contribution
		Contribution contrib = new Contribution();
		
		if (securityID.equals(debugSecurityID)) {
			log.info("Going to check switch. security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
		}
		
		// If position in switches
		Boolean hasSwitch = false;
		for (Switch thisSwitch : switches) {
			// If position has switch redemption
			if (securityID.equals(thisSwitch.getRedemptionSecurityID())) {
				if (securityID.equals(debugSecurityID)) {
					log.info("has red switch : " + thisSwitch.getRedemptionAmount().toString());
				}
				hasSwitch = true;
				// Set position amount
				security.setCalculatedAmount(security.getCalculatedAmount() + security.getLocalAmount() - security.getReferencePosition().getLocalAmount() + thisSwitch.getRedemptionAmount());
				continue;
			}
			
			// If position has switch subscription
			if (securityID.equals(thisSwitch.getSubscriptionSecurityID())) {
				if (securityID.equals(debugSecurityID)) {
					log.info("has sub switch : subquantity = " + thisSwitch.getSubscriptionQuantity().toString() + " / subnav = " +  thisSwitch.getSubscriptionNAV().toString() + " / redquantity = " + thisSwitch.getRedemptionQuantity().toString() + " / rednav = " + thisSwitch.getRedemptionNAV().toString());
				}
				hasSwitch = true;
				// Set position amount
//					security.setCalculatedAmount(security.getCalculatedAmount() + thisSwitch.getSubscriptionQuantity() * thisSwitch.getSubscriptionNAV() - thisSwitch.getRedemptionQuantity() * thisSwitch.getRedemptionNAV());
				security.setCalculatedAmount(security.getCalculatedAmount() + thisSwitch.getSubscriptionQuantity() * security.getNAV() - thisSwitch.getRedemptionQuantity() * thisSwitch.getRedemptionNAV());
				if (securityID.equals(debugSecurityID)) {
					log.info("after sub switch added : security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
				}
				
				// If switch on existing series, remove switch quantity x security NAV
				if (security.getReferencePosition().getExists()) {
					hasSwitch = false;
					if (securityID.equals(debugSecurityID)) {
						log.info("sub switch on existing series: remove switch quantity = " + thisSwitch.getSubscriptionQuantity().toString() + " * security NAV = " + security.getNAV().toString());
					}
					security.setCalculatedAmount(security.getCalculatedAmount() - thisSwitch.getSubscriptionQuantity() * security.getNAV());
					if (securityID.equals(debugSecurityID)) {
						log.info("after sub switch on existing series: security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
					}
				}
//					break;
			}
		}
		
		if (securityID.equals(debugSecurityID)) {
			log.info("After check switch. security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
		}
		
		// Else
		if (!hasSwitch) {
			if (securityID.equals(debugSecurityID)) {
				log.info("No switch - Calculate amount according to simple formula (1)");
			}
		
		// DEBUG
//			log.info("security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
//			log.info("security.getLocalAmount() = " + security.getLocalAmount().toString());
//			log.info("security.getReferencePosition().getLocalAmount() = " + security.getReferencePosition().getLocalAmount().toString());
			// Set position amount
			security.setCalculatedAmount(security.getCalculatedAmount() + security.getLocalAmount() - security.getReferencePosition().getLocalAmount());
		}
		
		if (securityID.equals(debugSecurityID)) {
			log.info("Going to check operation. security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
		}
		
		// Check if there is any operation
		for (Operation operation : operations) {
			// If position has operations
			if (securityID.equals(operation.getId())) {
				if (securityID.equals(debugSecurityID)) {
					log.info("Position has operation : " + operation.getAmount().toString());
				}
				// Set position amount
				security.setCalculatedAmount(security.getCalculatedAmount() + operation.getAmount());
//					break;
			}
		}
		
		if (securityID.equals(debugSecurityID)) {
			log.info("After check operation. security.getCalculatedAmount() = " + security.getCalculatedAmount().toString());
		}
		
		///// 2012-10-30: Switch handling - END
		
//			if (securityID == 19289) {
//				log.info("calc amount = " + security.getCalculatedAmount().toString());
//			}
		
		// If penalty fees
		security.setCalculatedAmount(security.getCalculatedAmount() - security.getPenaltyFees());
//			if (security.getPenaltyFees() != 0) {
//			}
		
		if (securityID.equals(debugSecurityID)) {
			log.info("Add penalty fees : security.getPenaltyFees() = " + security.getPenaltyFees().toString());
		}
		
		if (securityID.equals(debugSecurityID)) {
			log.info("Remove EF at t : security.getEqualizationFactorAmount() = " + security.getEqualizationFactorAmount().toString());
			log.info("Add EF at t-1 : security.getReferencePosition().getEqualizationFactorAmount() = " + security.getReferencePosition().getEqualizationFactorAmount().toString());
		}
		
		// If equalization factor
		Double equalizationFactor = security.getEqualizationFactorAmount() - security.getReferencePosition().getEqualizationFactorAmount();
		security.setCalculatedAmount(security.getCalculatedAmount() - Math.abs(equalizationFactor));
		
//			if (securityID == 19289) {
//				log.info("Add EF: calc amount = " + security.getCalculatedAmount().toString());
//			}
		
		// If position currency != portfolio currency
		if (!security.getCurrency().equals(portfolio.getCurrency())) {
			if (securityID.equals(debugSecurityID)) {
				log.info("Change currency (spot): " + security.getReferencePosition().getSpotRate().toString());
			}
	
			if (security.getReferencePosition().getHasSpotRate()) {
				security.setCalculatedAmount(security.getCalculatedAmount() * security.getReferencePosition().getSpotRate());
			}
		}
		
		// Calculate divider
		Double divider = evaluation.getBuyShares() + evaluation.getSellShares() + evaluation.getReferenceEvaluation().getNetAUM();
		if (securityID.equals(debugSecurityID)) {
			log.info("evaluation.getBuyShares() = " + evaluation.getBuyShares().toString());
			log.info("evaluation.getSellShares() = " + evaluation.getSellShares().toString());
			log.info("evaluation.getReferenceEvaluation().getNetAUM() = " + evaluation.getReferenceEvaluation().getNetAUM().toString());
			log.info("divider = " + divider.toString());
		}
		if (divider == 0) {
			log.error("Cannot calculate contribution: divider is equal to 0. (evaluation ID = " + evaluation.getId().toString() + ")");
			return;
		}
		
//			if (securityID == 19289) {
//				log.info("divider = " + divider.toString());
//			}
		
		// Contribution
		contrib.setApplicationDate(evaluation.getDate());
		contrib.setComputationDate(new Date());
		if (evaluation.getStatus().equals("EOM")) {
			contrib.setStatus(QualityStatus.FINAL);
		}
		else {
			contrib.setStatus(QualityStatus.ESTIMATED);
		}
		contrib.setValue(security.getCalculatedAmount() / divider);
		
		if (securityID.equals(debugSecurityID)) {
			log.info("contrib.value = " + contrib.getValue().toString());
		}
		
		contrib.setEvaluationID(evaluation.getId());
		contrib.setEvaluationDate(evaluation.getCalculationDate());
		contrib.setPortfolioID(portfolio.getId());
		contrib.setPortfolioCode(portfolio.getCode());
		contrib.setCompany(eimCompany.toString());
		contrib.setSecurityID(securityID);
		contrib.setIntranetID(security.getIntranetID());
		contrib.setFundName(security.getName()); // Do not set it if you don't want to save it in DB
		
//			if (securityID == 19289) {
//				log.info("contrib = " + contrib.getValue().toString());
//			}
		
		// Add this contribution to be returned
//			if (contrib.getValue().isNaN()) {
//				continue;
//			}
		results.add(contrib);
		
//		log.info("Security with id [" + security.getId().toString() + "] contribution computed in " + (System.currentTimeMillis() - fullStart) + "ms.");
		return;
	}
	
//	public static List<IContribution> computePortfolioUnderlyingReturns(Date fromDate, Date toDate, ComputationFrequency frequency, String entityId) {
//		log.info("Portfolio underlying returns computation - Working on portfolio with id [" + entityId + "]");
//		ArrayList<IContribution> results = new ArrayList<IContribution>();
//		
//		long fullStart = System.currentTimeMillis();
//		long start = System.currentTimeMillis();
//		
//		ValuationContentExtractor extractor = ValuationContentExtractorFactory.getValuationContentExtractor(EntityType.PORTFOLIO);
//		Collection<AUMToken> fullTrackContent = null;
//		if (fromDate==null && toDate==null) {
//			fullTrackContent = extractor.getPortfolioAUMS(entityId, frequency);
//			Date startDate = AUMToken.getStartDate(fullTrackContent);
//			Date stopDate = AUMToken.getEndDate(fullTrackContent);
//			if (startDate!=null) {
//				fromDate = startDate;
//				toDate = stopDate;
//			} else {
//				log.error("The extracted aums list has no entries!");
//				return results;
//			}
//		} else if (fromDate==null || toDate==null) {
//			log.error("Computation arguments are invalid!");
//			return results;
//		} else {
//			fullTrackContent = extractor.getPortfolioAUMS(entityId, DatesComputer.getPreviousValidDate(fromDate,frequency), toDate, frequency);
//		}
//
//		fromDate = DateUtil.roundToDay(fromDate);
//		toDate = DateUtil.roundToDay(toDate);
//		
//		Date workingDate = fromDate;
//		log.debug("Portfolio underlying returns computation - Initialization done in " + (System.currentTimeMillis()-start) + "ms.");
//		AUMToken previous = null;
//		while (workingDate.before(toDate) || workingDate.equals(toDate)) {
//			
//			workingDate = DatesComputer.getNextValidDate(workingDate, frequency);
//		}
//		log.info("Portfolio underlying returns computation - Portfolio with id [" + entityId + "] computed in " + (System.currentTimeMillis()-fullStart) + "ms.");
//		return results;
//	}
//	
////	public static List<IContribution> computePortfolioReturns(Date fromDate, Date toDate, ComputationFrequency frequency, String entityId) {
////		log.info("Portfolio returns computation - Working on portfolio with id [" + entityId + "]");
////		
////		// Prepare array of results (to be returned)
////		ArrayList<IContribution> results = new ArrayList<IContribution>();
////		
////		// Timers
////		long fullStart = System.currentTimeMillis();
////		long start = System.currentTimeMillis();
////		
////		// Make valuations extractor for this portfolio
////		ValuationContentExtractor extractor = ValuationContentExtractorFactory.getValuationContentExtractor(EntityType.PORTFOLIO);
////		
////		// Get all AUMs
////		Collection<AUMToken> fullTrackContent = null;
////		// If no limit date are set
////		if (fromDate == null && toDate == null) {
////			fullTrackContent = extractor.getPortfolioAUMS(entityId, frequency);
////			// Get track start date and end date
////			Date startDate = AUMToken.getStartDate(fullTrackContent);
////			Date stopDate = AUMToken.getEndDate(fullTrackContent);
////			
////			if (startDate != null) {
////				fromDate = startDate;
////				toDate = stopDate;
////			}
////			else {
////				log.error("The extracted aums list has no entries!");
////				return results;
////			}
////		}
////		// Else, if one of the 2 date is not defined
////		else if (fromDate==null || toDate==null) {
////			// This is not allowed
////			log.error("Computation arguments are invalid!");
////			return results;
////		}
////		// Else, both of them are defined
////		else {
////			// Get all AUMs between the 2 defined dates
////			fullTrackContent = extractor.getPortfolioAUMS(entityId, DatesComputer.getPreviousValidDate(fromDate,frequency), toDate, frequency);
////		}
////		
////		// Round dates to day
////		fromDate = DateUtil.roundToDay(fromDate);
////		toDate = DateUtil.roundToDay(toDate);
////		
////		// For each date (from fromDate to toDate)
////		Date workingDate = fromDate;
////		log.debug("Portfolio returns computation - Initialization done in " + (System.currentTimeMillis()-start) + "ms.");
////		AUMToken previous = null;
////		while (workingDate.before(toDate) || workingDate.equals(toDate)) {
////			// Preparing data
////			AUMToken current = null;
////			// If a previous AUM token was defined
////			if (previous != null) {
////				start = System.currentTimeMillis();
////				// Get AUM at this date
////				current = AUMToken.getTokenAt(fullTrackContent,workingDate);
////				log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Current token read in " + (System.currentTimeMillis()-start) + "ms.");
////				// If an AUM exists
////				if (current != null) {
////					// Make contribution
////					Contribution contrib = new Contribution();
////					contrib.setApplicationDate(workingDate);
////					contrib.setComputationDate(new Date());
////					contrib.setFrequency(frequency);
////					contrib.setStatus(current.getStatus());
////					contrib.setRelatedEntityId(entityId);
////					contrib.setTargetEntityId(entityId);
////					start = System.currentTimeMillis();
////					// Get portfolio cashflows
////					Collection<CashflowsToken> portfolioCF = extractor.getPortfolioCashflows(entityId, previous.getAumDate(), workingDate, frequency);
////					log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Cashflows between " + DateFormatter.toInputString(previous.getAumDate()) + " and current date extracted in" + (System.currentTimeMillis()-start) + "ms.");
////					// Set modified dietz (??)
////					contrib.setModifiedDietz(getModifiedDietzValue(frequency,previous.getAumNet(), current.getAumNet(), portfolioCF, true));
////					log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Modified Dietz value computed in " + (System.currentTimeMillis()-start) + "ms.");
////					// Set webfolio modified dietz (??)
////					contrib.setWebfolioModifiedDietz(getModifiedDietzValue(frequency,previous.getAumNet(), current.getAumNet(), portfolioCF,false));
////					log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Webfolio Modified Dietz value computed in " + (System.currentTimeMillis()-start) + "ms.");
////					
////					contrib.setSimpleDietz(getSimpleDietzValue(frequency,previous.getAumNet(), current.getAumNet(), portfolioCF,true));
////					log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Simple Dietz value computed in " + (System.currentTimeMillis()-start) + "ms.");
////					
////					contrib.setWebfolioSimpleDietz(getSimpleDietzValue(frequency,previous.getAumNet(), current.getAumNet(), portfolioCF,false));
////					log.debug("\t" + DateFormatter.toInputString(workingDate) + " - Webfolio Simple Dietz value computed in " + (System.currentTimeMillis()-start) + "ms.");
////					
////					// Add this contribution to be returned
////					results.add(contrib);
////				}
////				// Else, no AUM exists
////				else {
////					log.warn("\t" + DateFormatter.toInputString(workingDate) + " - AUM is missing");
////				}
////			}
////			
////			// If current is not defined, either this AUM does not exist or the previous AUM was not defined -> get the current AUM and save it in previous
////			previous = current==null?AUMToken.getTokenAt(fullTrackContent,workingDate):current;
////			// Prepare to reloop
////			workingDate = DatesComputer.getNextValidDate(workingDate, frequency);
////		}
////		log.info("Portfolio returns computation - Portfolio with id [" + entityId + "] computed in " + (System.currentTimeMillis()-fullStart) + "ms.");
////		return results;
////	}
//	
//	public static double getModifiedDietzValue(ComputationFrequency frequency, Double previousAum, Double currentAUM, Collection<CashflowsToken> cashflows, boolean includeNetInflows) {
//		//
//		Double upValue = currentAUM - previousAum;
//		Double downValue = previousAum;
//		
//		// For each cashflow
//		for (CashflowsToken cashflow : cashflows) {
//			// For each contribution
//			for (Double contribution : cashflow.getContributions()) {
//				// If ...
//				if (includeNetInflows) {
//					upValue -= contribution;
//				}
//				downValue += (contribution * DatesComputer.getFrequencyPeriodRatio(frequency,cashflow.getTokenDate()));
//			}
//			for (Double withdraw : cashflow.getWithdrawals()) {
//				if (includeNetInflows) {
//					upValue += withdraw;
//				}
//				downValue -= (withdraw * DatesComputer.getFrequencyPeriodRatio(frequency,cashflow.getTokenDate()));
//			}
//		}
//		return upValue/downValue;
//	}
//
//	public static double getSimpleDietzValue(ComputationFrequency frequency, Double previousAum, Double currentAUM, Collection<CashflowsToken> cashflows, boolean includeNetInflows) {
//		Double upValue = currentAUM - previousAum;
//		Double downValue = previousAum;
//		Double cashflowsSum = new Double(0.0d);
//		for (CashflowsToken cashflow : cashflows) {
//			for (Double contribution : cashflow.getContributions()) {
//				cashflowsSum += contribution;
//			}
//			for (Double withdraw : cashflow.getWithdrawals()) {
//				cashflowsSum -= withdraw;
//			}
//		}
//		if (includeNetInflows) {
//			upValue -= cashflowsSum;
//		}
//		downValue += cashflowsSum/2;
//		return upValue/downValue;
//	}
	
}
