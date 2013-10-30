package com.eim.utility.contribution.model;

/**
 * Class for Webfolio securities
 * @author abaguet
 */
public class Switch {
	
	/*------------------------------------------------------------
	 * Attributes
	 ------------------------------------------------------------*/
	
	/**
	 * The redemption security ID
	 */
	private Long redemptionSecurityID;
	
	/**
	 * The redemption amount
	 */
	private Double redemptionAmount;
	
	/**
	 * The redemption quantity
	 */
	private Double redemptionQuantity;
	
	/**
	 * The redemption NAV
	 */
	private Double redemptionNAV;
	
	/**
	 * The subscription security ID
	 */
	private Long subscriptionSecurityID;
	
	/**
	 * The subscription quantity
	 */
	private Double subscriptionQuantity;
	
	/**
	 * The redemption NAV
	 */
	private Double subscriptionNAV;
	
	/*------------------------------------------------------------
	 * Constructors
	 ------------------------------------------------------------*/
	
	/**
	 * Basic constructor
	 */
	public Switch() {
	}
	
	/*------------------------------------------------------------
	 * Getters / setters
	 ------------------------------------------------------------*/
	
	public void setRedemptionSecurityID(Long redemptionSecurityID) {
		this.redemptionSecurityID = redemptionSecurityID;
	}
	
	public Long getRedemptionSecurityID() {
		return redemptionSecurityID;
	}
	
	public void setSubscriptionSecurityID(Long subscriptionSecurityID) {
		this.subscriptionSecurityID = subscriptionSecurityID;
	}
	
	public Long getSubscriptionSecurityID() {
		return subscriptionSecurityID;
	}
	
	public void setRedemptionAmount(Double redemptionAmount) {
		this.redemptionAmount = redemptionAmount;
	}
	
	public Double getRedemptionAmount() {
		return redemptionAmount;
	}
	
	public void setRedemptionQuantity(Double redemptionQuantity) {
		this.redemptionQuantity = redemptionQuantity;
	}
	
	public Double getRedemptionQuantity() {
		return redemptionQuantity;
	}
	
	public void setRedemptionNAV(Double redemptionNAV) {
		this.redemptionNAV = redemptionNAV;
	}
	
	public Double getRedemptionNAV() {
		return redemptionNAV;
	}
	
	public void setSubscriptionQuantity(Double subscriptionQuantity) {
		this.subscriptionQuantity = subscriptionQuantity;
	}
	
	public Double getSubscriptionQuantity() {
		return subscriptionQuantity;
	}
	
	public void setSubscriptionNAV(Double subscriptionNAV) {
		this.subscriptionNAV = subscriptionNAV;
	}
	
	public Double getSubscriptionNAV() {
		return subscriptionNAV;
	}
	
	/*------------------------------------------------------------
	 * Methods
	 ------------------------------------------------------------*/
	
}
