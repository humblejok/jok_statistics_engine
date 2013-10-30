package com.eim.utility.contribution.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class CashflowsToken implements Serializable, Comparable<CashflowsToken> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5294287951640836450L;
	
	private Date tokenDate;
	private Collection<Double> withdrawals = new ArrayList<Double>();
	private Collection<Double> contributions = new ArrayList<Double>();
	
	public Date getTokenDate() {
		return tokenDate;
	}

	public void setTokenDate(Date tokenDate) {
		this.tokenDate = tokenDate;
	}

	public Collection<Double> getWithdrawals() {
		return withdrawals;
	}

	public void setWithdrawals(Collection<Double> withdrawals) {
		this.withdrawals = withdrawals;
	}

	public Collection<Double> getContributions() {
		return contributions;
	}

	public void setContributions(Collection<Double> contributions) {
		this.contributions = contributions;
	}
	
	@Override
	public int compareTo(CashflowsToken o) {
		if (o!=null && o.getTokenDate()!=null && this.getTokenDate()!=null) {
			return this.getTokenDate().compareTo(this.getTokenDate());
		} else if (this.getTokenDate()!=null) {
			return 1;
		} else {
			return -1;
		}
	}
	
}
