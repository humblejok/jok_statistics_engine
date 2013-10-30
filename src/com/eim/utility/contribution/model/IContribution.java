package com.eim.utility.contribution.model;

import java.util.Date;

import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.QualityStatus;

public interface IContribution {

	public void setId(Long id);
	public Long getId();

//	public String getRelatedEntityId();
//	public void setRelatedEntityId(String id);
//	
//	public String getTargetEntityId();
//	public void setTargetEntityId(String id);
//	
	public void setApplicationDate(Date applicationDate);
	public Date getApplicationDate();
	
	public void setComputationDate(Date computationDate);
	public Date getComputationDate();
//	
//	public ComputationFrequency getFrequency();
//	public void setFrequency(ComputationFrequency frequency);
//	
//	public QualityStatus getStatus();
//	public void setStatus(QualityStatus status);
	
}
