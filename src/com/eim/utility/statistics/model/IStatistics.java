package com.eim.utility.statistics.model;

import java.util.Date;

import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.QualityStatus;

/**
 * Interface that defines the common getters and setters for a statistic item.
 * @author sdj
 *
 */
public interface IStatistics {

	public void setId(Long id);
	public Long getId();

	public void setApplicationDate(Date applicationDate);
	public Date getApplicationDate();
	
	public ComputationFrequency getFrequency();
	public void setFrequency(ComputationFrequency frequency);
	
	public QualityStatus getStatus();
	public void setStatus(QualityStatus status);
	
}
