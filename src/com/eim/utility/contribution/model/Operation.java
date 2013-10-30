package com.eim.utility.contribution.model;

/**
 * Class for Webfolio operations
 * @author abaguet
 */
public class Operation {
	
	/*------------------------------------------------------------
	 * Attributes
	 ------------------------------------------------------------*/
	
	/**
	 * The operation ID
	 */
	private Long id;
	
	/**
	 * The operation type
	 */
	private String type;
	
	/**
	 * The amount
	 */
	private Double amount;
	
	/*------------------------------------------------------------
	 * Constructors
	 ------------------------------------------------------------*/
	
	/**
	 * Basic constructor
	 */
	public Operation() {
	}
	
	/**
	 * Constructor with ID
	 * @param id The ID
	 */
	public Operation(Long id) {
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
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Double getAmount() {
		return amount;
	}
	
	/*------------------------------------------------------------
	 * Methods
	 ------------------------------------------------------------*/
	
}
