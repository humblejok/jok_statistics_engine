package com.eim.utility.contribution.model;

/**
 * Class for Webfolio portfolios
 * @author abaguet
 */
public class Portfolio {
	
	/*------------------------------------------------------------
	 * Attributes
	 ------------------------------------------------------------*/
	
	/**
	 * The portfolio ID
	 */
	private Long id;
	
	/**
	 * The portfolio code
	 */
	private String code;
	
	/**
	 * The portfolio currency
	 */
	private String currency;
	
	/*------------------------------------------------------------
	 * Constructors
	 ------------------------------------------------------------*/
	
	/**
	 * Basic constructor
	 */
	public Portfolio() {
	}
	
	/**
	 * Constructor with ID
	 * @param id The ID
	 */
	public Portfolio(Long id) {
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
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	/*------------------------------------------------------------
	 * Methods
	 ------------------------------------------------------------*/
	
}
