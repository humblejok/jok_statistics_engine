package com.eim.utility.contribution.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import com.eim.utility.common.model.QualityStatus;

public class AUMToken implements Serializable, Comparable<AUMToken> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6489891819127117379L;
	private Date aumDate;
	private QualityStatus status;
	private Double aumNet;
	private Double aumGross;
	private Double aumNetUSD;
	private Double aumGrossUSD;
	
	public AUMToken() {
	}

	public Date getAumDate() {
		return aumDate;
	}

	public void setAumDate(Date aumDate) {
		this.aumDate = aumDate;
	}

	public Double getAumNet() {
		return aumNet;
	}

	public void setAumNet(Double aumNet) {
		this.aumNet = aumNet;
	}

	public Double getAumGross() {
		return aumGross;
	}

	public void setAumGross(Double aumGross) {
		this.aumGross = aumGross;
	}

	public Double getAumNetUSD() {
		return aumNetUSD;
	}

	public void setAumNetUSD(Double aumNetUSD) {
		this.aumNetUSD = aumNetUSD;
	}

	public Double getAumGrossUSD() {
		return aumGrossUSD;
	}

	public void setAumGrossUSD(Double aumGrossUSD) {
		this.aumGrossUSD = aumGrossUSD;
	}
	
	@Override
	public int compareTo(AUMToken o) {
		if (o!=null && o.getAumDate()!=null && this.getAumDate()!=null) {
			return this.getAumDate().compareTo(this.getAumDate());
		} else if (this.getAumDate()!=null) {
			return 1;
		} else {
			return -1;
		}
	}
	
	public static AUMToken getTokenAt(Collection<AUMToken> tokens, Date date) {
		if (tokens==null) {
			return null;
		}
		for (AUMToken token : tokens) {
			if (token.getAumDate().equals(date)) {
				return token;
			}
		}
		return null;
	}
	
	public static Date getStartDate(Collection<AUMToken> tokens) {
		if (tokens==null) {
			return null;
		}
		Date first = null;
		for (AUMToken token : tokens) {
			if (first==null) {
				first = token.getAumDate();
			} else if (first.after(token.getAumDate()) ) {
				first = token.getAumDate();
			}
		}
		return first;
	}
	
	public static Date getEndDate(Collection<AUMToken> tokens) {
		if (tokens==null) {
			return null;
		}
		Date last = null;
		for (AUMToken token : tokens) {
			if (last==null) {
				last = token.getAumDate();
			} else if (last.before(token.getAumDate()) ) {
				last = token.getAumDate();
			}
		}
		return last;
	}

	public QualityStatus getStatus() {
		return status;
	}

	public void setStatus(QualityStatus status) {
		this.status = status;
	}
}
