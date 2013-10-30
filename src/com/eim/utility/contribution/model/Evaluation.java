package com.eim.utility.contribution.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Class for Webfolio evaluations
 * @author abaguet
 */
public class Evaluation {
	
	/*------------------------------------------------------------
	 * Attributes
	 ------------------------------------------------------------*/
	
	/**
	 * The evaluation ID
	 */
	private Long id;
	
	/**
	 * The portfolio
	 */
	private Portfolio portfolio;
	
	/**
	 * The reference evaluation
	 */
	private Evaluation referenceEvaluation;
	
	/**
	 * The evaluation date
	 */
	private Date date;
	
	/**
	 * The evaluation status
	 */
	private String status;
	
	/**
	 * The evaluation calculation date
	 */
	private Date calculationDate;
	
	/**
	 * The local amount
	 */
	private Double localAmount;
	
	/**
	 * The spot rate
	 */
	private Double spotRate;
	
	/**
	 * The contribution from last month
	 */
	private Double buyShares;
	
	/**
	 * The withdrawal from last month
	 */
	private Double sellShares;
	
	/**
	 * The net Asset Under Management
	 */
	private Double netAUM;
	
	/*------------------------------------------------------------
	 * Constructors
	 ------------------------------------------------------------*/
	
	/**
	 * Basic constructor
	 */
	public Evaluation() {
	}
	
	/**
	 * Constructor with evaluation ID
	 * @param id The evaluation ID
	 */
	public Evaluation(Long id) {
		this.id = id;
	}
	
	/*------------------------------------------------------------
	 * Getters / setters
	 ------------------------------------------------------------*/
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
	
	public Portfolio getPortfolio() {
		return portfolio;
	}
	
	public void setReferenceEvaluation(Evaluation referenceEvaluation) {
		this.referenceEvaluation = referenceEvaluation;
	}
	
	public Evaluation getReferenceEvaluation() {
		return referenceEvaluation;
	}
	
	public void setLocalAmount(Double localAmount) {
		this.localAmount = localAmount;
	}
	
	public Double getLocalAmount() {
		return localAmount;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setSpotRate(Double spotRate) {
		this.spotRate = spotRate;
	}
	
	public Double getSpotRate() {
		return spotRate;
	}
	
	public void setBuyShares(Double buyShares) {
		this.buyShares = buyShares;
	}
	
	public Double getBuyShares() {
		return buyShares;
	}
	
	public void setSellShares(Double sellShares) {
		this.sellShares = sellShares;
	}
	
	public Double getSellShares() {
		return sellShares;
	}
	
	public void setNetAUM(Double netAUM) {
		this.netAUM = netAUM;
	}
	
	public Double getNetAUM() {
		return netAUM;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDate(Timestamp timestamp) {
		Date date = new Date(timestamp.getTime());
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setCalculationDate(Date calculationDate) {
		this.calculationDate = calculationDate;
	}
	
	public void setCalculationDate(Timestamp timestamp) {
		Date date = new Date(timestamp.getTime());
		this.calculationDate = date;
	}
	
	public Date getCalculationDate() {
		return calculationDate;
	}
	
	/*------------------------------------------------------------
	 * Methods
	 ------------------------------------------------------------*/
	
}
