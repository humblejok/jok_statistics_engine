package com.eim.utility.contribution.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.QualityStatus;

@Entity
@Table(name="STATS_CONTRIBUTIONS")
@SequenceGenerator (name="SEQ_DEFAULT_CONTRIB", sequenceName="SEQ_DEFAULT_CONTRIB", allocationSize=1)
public class Contribution implements IContribution {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_DEFAULT_CONTRIB")
	private Long id;
	
	@Column(nullable=false)
	private Long evaluationID;
	
	@Column(nullable=false)
	private Long portfolioID;
	
	@Column(nullable=false)
	private String portfolioCode;
	
	@Column(nullable=false)
	private String company;
	
	@Column(nullable=false)
	private Long securityID;
	
	@Column(nullable=true)
	private String fundName;
	
	@Column(nullable=true)
	private Long intranetID = null;
	
	@Column(nullable=false)
	private Double value;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private QualityStatus status;
	
	@Column(nullable=false)
	private Date applicationDate;
	
	@Column(nullable=false)
	private Date computationDate;
	
	@Column(nullable=true)
	private Date evaluationDate;
	
//	@Column(nullable=false)
//	private String targetEntityId;
//	
//	@Column(nullable=false)
//	private String relatedEntityId;
//	
//	
//	@Column(nullable=false)
//	@Enumerated(EnumType.STRING)
//	private QualityStatus status;
//	
//	@Column
//	@Enumerated(EnumType.STRING)
//	private ComputationFrequency frequency;
//
//	@Column
//	private Double webfolioModifiedDietz;
//	
//	@Column
//	private Double modifiedDietz;
//	
//	@Column
//	private Double webfolioSimpleDietz;
//	
//	@Column
//	private Double simpleDietz;
	
	public Contribution() {
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setEvaluationID(Long evaluationID) {
		this.evaluationID = evaluationID;
	}

	public Long getEvaluationID() {
		return evaluationID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioCode(String portfolioCode) {
		this.portfolioCode = portfolioCode;
	}

	public String getPortfolioCode() {
		return portfolioCode;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany() {
		return company;
	}

	public void setSecurityID(Long securityID) {
		this.securityID = securityID;
	}

	public Long getSecurityID() {
		return securityID;
	}

	public void setIntranetID(Long intranetID) {
		this.intranetID = intranetID;
	}

	public Long getIntranetID() {
		return intranetID;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getFundName() {
		return fundName;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getValue() {
		return value;
	}

	public void setStatus(QualityStatus status) {
		this.status = status;
	}

	public QualityStatus getStatus() {
		return status;
	}

//	@Override
//	public String getRelatedEntityId() {
//		return relatedEntityId;
//	}
//
//	@Override
//	public void setRelatedEntityId(String id) {
//		this.relatedEntityId = id;
//
//	}
//
//	@Override
//	public String getTargetEntityId() {
//		return targetEntityId;
//	}
//
//	@Override
//	public void setTargetEntityId(String id) {
//		this.targetEntityId = id;
//	}
//
	@Override
	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	@Override
	public Date getApplicationDate() {
		return applicationDate;
	}
//
//	@Override
//	public ComputationFrequency getFrequency() {
//		return frequency;
//	}
//
//	@Override
//	public void setFrequency(ComputationFrequency frequency) {
//		this.frequency = frequency;
//	}
//
//	@Override
//	public QualityStatus getStatus() {
//		return status;
//	}
//
//	@Override
//	public void setStatus(QualityStatus status) {
//		this.status = status;
//	}
//
	@Override
	public void setComputationDate(Date computationDate) {
		this.computationDate = computationDate;
	}

	@Override
	public Date getComputationDate() {
		return computationDate;
	}
	
	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate = evaluationDate;
	}

	public Date getEvaluationDate() {
		return evaluationDate;
	}
//
//	public Double getModifiedDietz() {
//		return modifiedDietz;
//	}
//
//	public void setModifiedDietz(Double modifiedDietz) {
//		this.modifiedDietz = modifiedDietz;
//	}
//
//	public Double getSimpleDietz() {
//		return simpleDietz;
//	}
//
//	public void setSimpleDietz(Double simpleDietz) {
//		this.simpleDietz = simpleDietz;
//	}
//
//	public void setWebfolioModifiedDietz(Double webfolioModifiedDietz) {
//		this.webfolioModifiedDietz = webfolioModifiedDietz;
//	}
//
//	public Double getWebfolioModifiedDietz() {
//		return webfolioModifiedDietz;
//	}
//
//	public void setWebfolioSimpleDietz(Double webfolioSimpleDietz) {
//		this.webfolioSimpleDietz = webfolioSimpleDietz;
//	}
//
//	public Double getWebfolioSimpleDietz() {
//		return webfolioSimpleDietz;
//	}
	
	@Override
	public String toString() {
//		return "Contribution: id = " + this.id.toString() +
		return "Contribution: evaluation id = " + this.evaluationID.toString() +
			", portfolio id = " + this.portfolioID.toString() +
			", portfolio code = " + this.portfolioCode.toString() +
			", security id = " + this.securityID.toString() +
			", intranet id = " + this.intranetID.toString() +
			", contribution value = " + this.value.toString() +
			", contribution status = " + this.status.toString() +
			", application date = " + this.applicationDate.toString() +
			", computation date = " + this.computationDate.toString() +
			", evaluation date = " + this.evaluationDate.toString();
	}

}
