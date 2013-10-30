package com.eim.utility.contribution.model;

/**
 * Class for Webfolio securities
 * @author abaguet
 */
public class Security {
	
	/*------------------------------------------------------------
	 * Attributes
	 ------------------------------------------------------------*/
	
	/**
	 * The security ID
	 */
	private Long id;
	
	/**
	 * The security name
	 */
	private String name;
	
	/**
	 * The local amount
	 */
	private Double localAmount;
	
	/**
	 * Whether security has spot rate
	 */
	private Boolean hasSpotRate;
	
	/**
	 * The spot rate
	 */
	private Double spotRate;
	
	/*------------------------------------------------------------
	 * Constructors
	 ------------------------------------------------------------*/
	
	/**
	 * Basic constructor
	 */
	public Security() {
	}
	
	/**
	 * Constructor with security ID
	 * @param id The security ID
	 */
	public Security(Long id) {
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
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
	
	public void setSpotRate(Double spotRate) {
		this.hasSpotRate = true;
		this.spotRate = spotRate;
	}
	
	public Double getSpotRate() {
		return spotRate;
	}
	
	/*------------------------------------------------------------
	 * Methods
	 ------------------------------------------------------------*/
	
}
