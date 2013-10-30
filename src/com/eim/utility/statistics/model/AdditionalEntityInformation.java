package com.eim.utility.statistics.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * POJO defining additional information regarding an entity. It can be extended to accept useful information for statistics computations.
 * @author sdj
 *
 */
public class AdditionalEntityInformation implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -2995142603353835577L;

	/**
	 * Investment amount in USD
	 */
	private Double totalInvestedUSDAmount;
	
	/**
	 * Long exposures
	 */
	private ArrayList<Double> longExposures = new ArrayList<Double>();
	
	/**
	 * Short exposures
	 */
	private ArrayList<Double> shortExposures = new ArrayList<Double>();
	
	public AdditionalEntityInformation() {
	}

	public Double getTotalInvestedUSDAmount() {
		return totalInvestedUSDAmount;
	}

	public void setTotalInvestedUSDAmount(Double totalInvestedUSDAmount) {
		this.totalInvestedUSDAmount = totalInvestedUSDAmount;
	}

	public ArrayList<Double> getLongExposures() {
		return longExposures;
	}

	public void setLongExposures(ArrayList<Double> longExposures) {
		this.longExposures = longExposures;
	}

	public ArrayList<Double> getShortExposures() {
		return shortExposures;
	}

	public void setShortExposures(ArrayList<Double> shortExposures) {
		this.shortExposures = shortExposures;
	}
	
}
