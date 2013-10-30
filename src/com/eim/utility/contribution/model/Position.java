package com.eim.utility.contribution.model;

/**
 * Class for Webfolio positions
 * @author abaguet
 */
public class Position {
	
	/*------------------------------------------------------------
	 * Attributes
	 ------------------------------------------------------------*/
	
	/**
	 * The security ID
	 */
	private Long id;
	
	/**
	 * The reference position
	 */
	private Position referencePosition;
	
	/**
	 * The security intranet ID
	 */
	private Long intranetID;
	
	/**
	 * The security name
	 */
	private String name;
	
	/**
	 * The security currency
	 */
	private String currency;
	
	/**
	 * The fund style
	 */
	private String style;
	
	/**
	 * The fund strategy
	 */
	private String strategy;
	
	/**
	 * The local amount
	 */
	private Double localAmount = 0.0;
	
	/**
	 * Whether position has spot rate
	 */
	private Boolean hasSpotRate;
	
	/**
	 * The spot rate
	 */
	private Double spotRate;
	
	/**
	 * The NAV
	 */
	private Double nav;
	
	/**
	 * The equalization factor amount
	 */
	private Double equalizationFactorAmount = 0.0;
	
	/**
	 * The calculated amount
	 */
	private Double calculatedAmount = 0.0;
	
	/**
	 * The penalty fees
	 */
	private Double penaltyFees = 0.0;
	
	/**
	 * Whether position is real (exists) i.e. not set to null (by default) everywhere
	 */
	private Boolean exists = true;
	
	/*------------------------------------------------------------
	 * Constructors
	 ------------------------------------------------------------*/
	
	/**
	 * Basic constructor
	 */
	public Position() {
	}
	
	/**
	 * Constructor with ID
	 * @param id The ID
	 */
	public Position(Long id) {
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
	
	public void setReferencePosition(Position referencePosition) {
		this.referencePosition = referencePosition;
	}
	
	public Position getReferencePosition() {
		return this.referencePosition;
	}
	
	public void setIntranetID(Long intranetID) {
		this.intranetID = intranetID;
	}
	
	public Long getIntranetID() {
		return intranetID;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setStyle(String style) {
		this.style = style;
	}
	
	public String getStyle() {
		return style;
	}
	
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	
	public String getStrategy() {
		return strategy;
	}
	
	public void setLocalAmount(Double localAmount) {
		this.localAmount = localAmount;
	}
	
	public Double getLocalAmount() {
		return localAmount;
	}
	
	public void setNoSpotRate() {
		this.hasSpotRate = false;
	}
	
	public Boolean getHasSpotRate() {
		return this.hasSpotRate;
	}
	
	public void setSpotRate(Double spotRate) {
		this.hasSpotRate = true;
		this.spotRate = spotRate;
	}
	
	public Double getSpotRate() {
		return spotRate;
	}
	
	public void setEqualizationFactorAmount(Double equalizationFactorAmount) {
		this.equalizationFactorAmount = equalizationFactorAmount;
	}
	
	public Double getEqualizationFactorAmount() {
		return equalizationFactorAmount;
	}
	
	public void setCalculatedAmount(Double calculatedAmount) {
		this.calculatedAmount = calculatedAmount;
	}
	
	public Double getCalculatedAmount() {
		return calculatedAmount;
	}
	
	public void setNAV(Double nav) {
		this.nav = nav;
	}
	
	public Double getNAV() {
		return nav;
	}
	
	public void setPenaltyFees(Double penaltyFees) {
		this.penaltyFees = penaltyFees;
	}
	
	public Double getPenaltyFees() {
		return penaltyFees;
	}
	
	public void setNotExists() {
		this.exists = false;
	}
	
	public Boolean getExists() {
		return this.exists;
	}
	
	/*------------------------------------------------------------
	 * Methods
	 ------------------------------------------------------------*/
	
}
