package com.eim.utility.statistics.model;

import java.io.Serializable;
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

import org.json.JSONObject;

import com.eim.utility.common.model.ComputationFrequency;
import com.eim.utility.common.model.EntityType;
import com.eim.utility.common.model.QualityStatus;

/**
 * POJO representing the <B>non-benchmark</B> related statistics.<BR/><BR/>
 * It is annotated with hibernate annotations for persistence purpose.<BR/>
 * These statistics are persisted in the table <I>STATS_COMPUTED</I> and identified using a sequence called <I>SEQ_STATS_COMPUTED</I>.
 * That POJO possesses 5 fields (other than the <code>id</code> field) that are mandatory:<BR/>
 * <ul>
 * <li>targetEntityType</li>
 * <li>targetEntityId</li>
 * <li>applicationDate</li>
 * <li>computationDate</li>
 * <li>status</li>
 * </ul>
 * @author sdj
 *
 */
@Entity
@Table(name="STATS_COMPUTED")
@SequenceGenerator (name="SEQ_STATS_COMPUTED",sequenceName="SEQ_STATS_COMPUTED",allocationSize=1)
public class ComputedStatistics implements Serializable, IStatistics {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5204523622561692238L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_STATS_COMPUTED")
	private Long id;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private EntityType targetEntityType;
	
	@Column(nullable=false)
	private String targetEntityId;
	
	@Column(nullable=false)
	private Date applicationDate;
	
	@Column(nullable=false)
	private Date computationDate;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private QualityStatus status;
	
	@Column
	@Enumerated(EnumType.STRING)
	private ComputationFrequency frequency;
	
	private Double lastReturn;
	private Double yearToDate;
	private Double last3MonthsTotalReturn;
	private Double last6MonthsTotalReturn;
	private Double last12MonthsTotalReturn;
	@Column(name="LAST12ANNUALSTDDEV")
	private Double last12MonthsAnnualizedStandardDeviation;
	@Column(name="LAST24ANNUALSTDDEV")
	private Double last24MonthsAnnualizedStandardDeviation;
	@Column(name="LAST36ANNUALSTDDEV")
	private Double last36MonthsAnnualizedStandardDeviation;
	private Double last18MonthsAnnualizedReturn;
	private Double last24MonthsAnnualizedReturn;
	private Double last36MonthsAnnualizedReturn;
	private Double last60MonthsAnnualizedReturn;
	private Double last18MonthsMaximumDrawdown;
	private Double currentDrawdown;
	private Double annualizedReturn;
	private Double totalReturn;
	private Double averageRateOfReturn;
	private Double averageGain;
	private Double averageLoss;
	
	// 12 months rolling average
	private Double rollingAverage12;
	// 12 months rolling deviation
	private Double rollingDeviation12;
	// 36 months rolling excess kurtosis
	private Double rollingExcessKurtosis36;
	// 36 months rolling skewness
	private Double rollingSkewness36;
	
	private Double percentageOfPositiveMonths;
	private Double annualizedStandardDeviation;
	
	private Double annualizedLossDeviation;
	private Double skewness;
	private Double kurtosis;
	private Integer numberOfMonths;
	
	private Double cvar_11_12;
	private Double cvar_11_12_simulated;
	private Double var_11_12_simulated;
	private Double var_95_simulated;
	private Double var_99_simulated;
	private Double omega10SinceInception;
	private Double omega10Over36Months;
	
	private Double maximumDrawdown;
	
	private Integer maximumLengthOfDrawdown = 0;
	private String drawdownOngoing = "false";
	
	private Double last18MonthsTotalReturn;
	
	private Double last24MonthsTotalReturn;
	
	private Double last36MonthsTotalReturn;
	
	private Double last60MonthsTotalReturn;
	
	private Double totalAmountUSD;
	
	private Double grossExposure;
	
	private Double netExposure;
	
	private Date lastReturnDate;
	
	private Double yearToDateY_1;
	private Double yearToDateY_2;
	private Double yearToDateY_3;
	private Double yearToDateY_4;
	private Double yearToDateY_5;
	private Double yearToDateY_6;
	private Double yearToDateY_7;
	private Double yearToDateY_8;
	private Double yearToDateY_9;
	
	private Double monthToDate;
	private Double quarterToDate;
	private Double semesterToDate;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public EntityType getTargetEntityType() {
		return targetEntityType;
	}
	public void setTargetEntityType(EntityType targetEntityType) {
		this.targetEntityType = targetEntityType;
	}
	public String getTargetEntityId() {
		return targetEntityId;
	}
	public void setTargetEntityId(String targetEntityId) {
		this.targetEntityId = targetEntityId;
	}
	public Date getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}
	public Date getComputationDate() {
		return computationDate;
	}
	public void setComputationDate(Date computationDate) {
		this.computationDate = computationDate;
	}
	public QualityStatus getStatus() {
		return status;
	}
	public void setStatus(QualityStatus status) {
		this.status = status;
	}
	public ComputationFrequency getFrequency() {
		return frequency;
	}
	public void setFrequency(ComputationFrequency frequency) {
		this.frequency = frequency;
	}
	public Double getLastReturn() {
		return lastReturn;
	}
	public void setLastReturn(Double lastReturn) {
		if (lastReturn!=null && lastReturn.isNaN()) {
			return ;
		}
		this.lastReturn = lastReturn;
	}
	public Double getYearToDate() {
		return yearToDate;
	}
	public void setYearToDate(Double yearToDate) {
		if (yearToDate!=null && yearToDate.isNaN()) {
			return ;
		}
		this.yearToDate = yearToDate;
	}
	public Double getLast3MonthsTotalReturn() {
		return last3MonthsTotalReturn;
	}
	public void setLast3MonthsTotalReturn(Double last3MonthsTotalReturn) {
		if (last3MonthsTotalReturn!=null && last3MonthsTotalReturn.isNaN()) {
			return ;
		}
		this.last3MonthsTotalReturn = last3MonthsTotalReturn;
	}
	public Double getLast6MonthsTotalReturn() {
		return last6MonthsTotalReturn;
	}
	public void setLast6MonthsTotalReturn(Double last6MonthsTotalReturn) {
		if (last6MonthsTotalReturn!=null && last6MonthsTotalReturn.isNaN()) {
			return ;
		}
		this.last6MonthsTotalReturn = last6MonthsTotalReturn;
	}
	public Double getLast12MonthsTotalReturn() {
		return last12MonthsTotalReturn;
	}
	public void setLast12MonthsTotalReturn(Double last12MonthsTotalReturn) {
		if (last12MonthsTotalReturn!=null && last12MonthsTotalReturn.isNaN()) {
			return ;
		}
		this.last12MonthsTotalReturn = last12MonthsTotalReturn;
	}
	public Double getLast12MonthsAnnualizedStandardDeviation() {
		return last12MonthsAnnualizedStandardDeviation;
	}
	public void setLast12MonthsAnnualizedStandardDeviation(Double last12MonthsAnnualizedStandardDeviation) {
		if (last12MonthsAnnualizedStandardDeviation!=null && last12MonthsAnnualizedStandardDeviation.isNaN()) {
			return ;
		}
		this.last12MonthsAnnualizedStandardDeviation = last12MonthsAnnualizedStandardDeviation;
	}
	public Double getLast24MonthsAnnualizedStandardDeviation() {
		return last24MonthsAnnualizedStandardDeviation;
	}
	public void setLast24MonthsAnnualizedStandardDeviation(
			Double last24MonthsAnnualizedStandardDeviation) {
		if (last24MonthsAnnualizedStandardDeviation!=null && last24MonthsAnnualizedStandardDeviation.isNaN()) {
			return ;
		}
		this.last24MonthsAnnualizedStandardDeviation = last24MonthsAnnualizedStandardDeviation;
	}
	public Double getLast36MonthsAnnualizedStandardDeviation() {
		return last36MonthsAnnualizedStandardDeviation;
	}
	public void setLast36MonthsAnnualizedStandardDeviation(
			Double last36MonthsAnnualizedStandardDeviation) {
		if (last36MonthsAnnualizedStandardDeviation!=null && last36MonthsAnnualizedStandardDeviation.isNaN()) {
			return ;
		}
		this.last36MonthsAnnualizedStandardDeviation = last36MonthsAnnualizedStandardDeviation;
	}
	public Double getLast18MonthsAnnualizedReturn() {
		return last18MonthsAnnualizedReturn;
	}
	public void setLast18MonthsAnnualizedReturn(Double last18MonthsAnnualizedReturn) {
		if (last18MonthsAnnualizedReturn!=null && last18MonthsAnnualizedReturn.isNaN()) {
			return ;
		}
		this.last18MonthsAnnualizedReturn = last18MonthsAnnualizedReturn;
	}
	public Double getLast24MonthsAnnualizedReturn() {
		return last24MonthsAnnualizedReturn;
	}
	public void setLast24MonthsAnnualizedReturn(Double last24MonthsAnnualizedReturn) {
		if (last24MonthsAnnualizedReturn!=null && last24MonthsAnnualizedReturn.isNaN()) {
			return ;
		}
		this.last24MonthsAnnualizedReturn = last24MonthsAnnualizedReturn;
	}
	public Double getLast36MonthsAnnualizedReturn() {
		return last36MonthsAnnualizedReturn;
	}
	public void setLast36MonthsAnnualizedReturn(Double last36MonthsAnnualizedReturn) {
		if (last36MonthsAnnualizedReturn!=null && last36MonthsAnnualizedReturn.isNaN()) {
			return ;
		}
		this.last36MonthsAnnualizedReturn = last36MonthsAnnualizedReturn;
	}
	public Double getLast60MonthsAnnualizedReturn() {
		return last60MonthsAnnualizedReturn;
	}
	public void setLast60MonthsAnnualizedReturn(Double last60MonthsAnnualizedReturn) {
		if (last60MonthsAnnualizedReturn!=null && last60MonthsAnnualizedReturn.isNaN()) {
			return ;
		}
		this.last60MonthsAnnualizedReturn = last60MonthsAnnualizedReturn;
	}
	public Double getLast18MonthsMaximumDrawdown() {
		return last18MonthsMaximumDrawdown;
	}
	public void setLast18MonthsMaximumDrawdown(Double last18MonthsMaximumDrawdown) {
		if (last18MonthsMaximumDrawdown!=null && last18MonthsMaximumDrawdown.isNaN()) {
			return ;
		}
		this.last18MonthsMaximumDrawdown = last18MonthsMaximumDrawdown;
	}
	public Double getCurrentDrawdown() {
		return currentDrawdown;
	}
	public void setCurrentDrawdown(Double currentDrawdown) {
		if (currentDrawdown!=null && currentDrawdown.isNaN()) {
			return ;
		}
		this.currentDrawdown = currentDrawdown;
	}
	public Double getAnnualizedReturn() {
		return annualizedReturn;
	}
	public void setAnnualizedReturn(Double annualizedReturn) {
		if (annualizedReturn!=null && annualizedReturn.isNaN()) {
			return ;
		}
		this.annualizedReturn = annualizedReturn;
	}
	public Double getTotalReturn() {
		return totalReturn;
	}
	public void setTotalReturn(Double totalReturn) {
		if (totalReturn!=null && totalReturn.isNaN()) {
			return ;
		}
		this.totalReturn = totalReturn;
	}
	public Double getAverageRateOfReturn() {
		return averageRateOfReturn;
	}
	public void setAverageRateOfReturn(Double averageRateOfReturn) {
		if (averageRateOfReturn!=null && averageRateOfReturn.isNaN()) {
			return ;
		}
		this.averageRateOfReturn = averageRateOfReturn;
	}
	public Double getAverageGain() {
		return averageGain;
	}
	public void setAverageGain(Double averageGain) {
		if (averageGain!=null && averageGain.isNaN()) {
			return ;
		}
		this.averageGain = averageGain;
	}
	
	// 12 months rolling average
	public Double getRollingAverage12() {
		return rollingAverage12;
	}
	public void setRollingAverage12(Double rollingAverage12) {
		if (rollingAverage12!=null && rollingAverage12.isNaN()) {
			return ;
		}
		this.rollingAverage12 = rollingAverage12;
	}
	
	// 12 months rolling deviation
	public Double getRollingDeviation12() {
		return rollingDeviation12;
	}
	public void setRollingDeviation12(Double rollingDeviation12) {
		if (rollingDeviation12!=null && rollingDeviation12.isNaN()) {
			return ;
		}
		this.rollingDeviation12 = rollingDeviation12;
	}
	
	public Double getAverageLoss() {
		return averageLoss;
	}
	public void setAverageLoss(Double averageLoss) {
		if (averageLoss!=null && averageLoss.isNaN()) {
			return ;
		}
		this.averageLoss = averageLoss;
	}
	public Double getPercentageOfPositiveMonths() {
		return percentageOfPositiveMonths;
	}
	public void setPercentageOfPositiveMonths(Double percentageOfPositiveMonths) {
		if (percentageOfPositiveMonths!=null && percentageOfPositiveMonths.isNaN()) {
			return ;
		}
		this.percentageOfPositiveMonths = percentageOfPositiveMonths;
	}
	public Double getAnnualizedStandardDeviation() {
		return annualizedStandardDeviation;
	}
	public void setAnnualizedStandardDeviation(Double annualizedStandardDeviation) {
		if (annualizedStandardDeviation!=null && annualizedStandardDeviation.isNaN()) {
			return ;
		}
		this.annualizedStandardDeviation = annualizedStandardDeviation;
	}
	public Double getAnnualizedLossDeviation() {
		return annualizedLossDeviation;
	}
	public void setAnnualizedLossDeviation(Double annualizedLossDeviation) {
		if (annualizedLossDeviation!=null && annualizedLossDeviation.isNaN()) {
			return ;
		}
		this.annualizedLossDeviation = annualizedLossDeviation;
	}
	public Double getSkewness() {
		return skewness;
	}
	public void setSkewness(Double skewness) {
		if (skewness!=null && skewness.isNaN()) {
			return ;
		}
		this.skewness = skewness;
	}
	public Double getKurtosis() {
		return kurtosis;
	}
	public void setKurtosis(Double kurtosis) {
		if (kurtosis!=null && kurtosis.isNaN()) {
			return ;
		}
		this.kurtosis = kurtosis;
	}
	public Integer getNumberOfMonths() {
		return numberOfMonths;
	}
	public void setNumberOfMonths(Integer numberOfMonths) {
		this.numberOfMonths = numberOfMonths;
	}
	public Double getCvar_11_12() {
		return cvar_11_12;
	}
	public void setCvar_11_12(Double cvar_11_12) {
		if (cvar_11_12!=null && cvar_11_12.isNaN()) {
			return ;
		}
		this.cvar_11_12 = cvar_11_12;
	}
	public Double getCvar_11_12_simulated() {
		return cvar_11_12_simulated;
	}
	public void setCvar_11_12_simulated(Double cvar_11_12_simulated) {
		if (cvar_11_12_simulated!=null && cvar_11_12_simulated.isNaN()) {
			return ;
		}
		this.cvar_11_12_simulated = cvar_11_12_simulated;
	}
	public Double getVar_11_12_simulated() {
		return var_11_12_simulated;
	}
	public void setVar_11_12_simulated(Double var_11_12_simulated) {
		if (var_11_12_simulated!=null && var_11_12_simulated.isNaN()) {
			return ;
		}
		this.var_11_12_simulated = var_11_12_simulated;
	}
	public Double getVar_95_simulated() {
		return var_95_simulated;
	}
	public void setVar_95_simulated(Double var_95_simulated) {
		if (var_95_simulated!=null && var_95_simulated.isNaN()) {
			return ;
		}
		this.var_95_simulated = var_95_simulated;
	}
	public Double getVar_99_simulated() {
		return var_99_simulated;
	}
	public void setVar_99_simulated(Double var_99_simulated) {
		if (var_99_simulated!=null && var_99_simulated.isNaN()) {
			return ;
		}
		this.var_99_simulated = var_99_simulated;
	}
	public Double getOmega10SinceInception() {
		return omega10SinceInception;
	}
	public void setOmega10SinceInception(Double omega10SinceInception) {
		if (omega10SinceInception!=null && omega10SinceInception.isNaN()) {
			return ;
		}
		this.omega10SinceInception = omega10SinceInception;
	}
	public Double getOmega10Over36Months() {
		return omega10Over36Months;
	}
	public void setOmega10Over36Months(Double omega10Over36Months) {
		if (omega10Over36Months!=null && omega10Over36Months.isNaN()) {
			return ;
		}
		this.omega10Over36Months = omega10Over36Months;
	}
	
	public void setMaximumDrawdown(Double maximumDrawdown) {
		if (maximumDrawdown!=null && maximumDrawdown.isNaN()) {
			return ;
		}
		this.maximumDrawdown = maximumDrawdown;
	}
	public Double getMaximumDrawdown() {
		return maximumDrawdown;
	}
	public void setDrawdownOngoing(String drawdownOngoing) {
		this.drawdownOngoing = drawdownOngoing;
	}
	public String getDrawdownOngoing() {
		return drawdownOngoing;
	}
	public void setMaximumLengthOfDrawdown(Integer maximumLengthOfDrawdown) {
		this.maximumLengthOfDrawdown = maximumLengthOfDrawdown;
	}
	public Integer getMaximumLengthOfDrawdown() {
		return maximumLengthOfDrawdown;
	}
	public Double getLast18MonthsTotalReturn() {
		return last18MonthsTotalReturn;
	}
	public void setLast18MonthsTotalReturn(Double last18MonthsTotalReturn) {
		if (last18MonthsTotalReturn!=null && last18MonthsTotalReturn.isNaN()) {
			return ;
		}
		this.last18MonthsTotalReturn = last18MonthsTotalReturn;
	}
	public Double getLast24MonthsTotalReturn() {
		return last24MonthsTotalReturn;
	}
	public void setLast24MonthsTotalReturn(Double last24MonthsTotalReturn) {
		if (last24MonthsTotalReturn!=null && last24MonthsTotalReturn.isNaN()) {
			return ;
		}
		this.last24MonthsTotalReturn = last24MonthsTotalReturn;
	}
	public Double getLast36MonthsTotalReturn() {
		return last36MonthsTotalReturn;
	}
	public void setLast36MonthsTotalReturn(Double last36MonthsTotalReturn) {
		if (last36MonthsTotalReturn!=null && last36MonthsTotalReturn.isNaN()) {
			return ;
		}
		this.last36MonthsTotalReturn = last36MonthsTotalReturn;
	}
	public Double getLast60MonthsTotalReturn() {
		return last60MonthsTotalReturn;
	}
	public void setLast60MonthsTotalReturn(Double last60MonthsTotalReturn) {
		if (last60MonthsTotalReturn!=null && last60MonthsTotalReturn.isNaN()) {
			return ;
		}
		this.last60MonthsTotalReturn = last60MonthsTotalReturn;
	}
	public Double getTotalAmountUSD() {
		return totalAmountUSD;
	}
	public void setTotalAmountUSD(Double totalAmountUSD) {
		this.totalAmountUSD = totalAmountUSD;
	}
	public Double getGrossExposure() {
		return grossExposure;
	}
	public void setGrossExposure(Double grossExposure) {
		this.grossExposure = grossExposure;
	}
	public Double getNetExposure() {
		return netExposure;
	}
	public void setNetExposure(Double netExposure) {
		this.netExposure = netExposure;
	}
	public void setLastReturnDate(Date lastReturnDate) {
		this.lastReturnDate = lastReturnDate;
	}
	public Date getLastReturnDate() {
		return lastReturnDate;
	}
	public Double getYearToDateY_1() {
		return yearToDateY_1;
	}
	public void setYearToDateY_1(Double yearToDateY_1) {
		if (yearToDateY_1!=null && yearToDateY_1.isNaN()) {
			return ;
		}
		this.yearToDateY_1 = yearToDateY_1;
	}
	public Double getYearToDateY_2() {
		return yearToDateY_2;
	}
	public void setYearToDateY_2(Double yearToDateY_2) {
		if (yearToDateY_2!=null && yearToDateY_2.isNaN()) {
			return ;
		}
		this.yearToDateY_2 = yearToDateY_2;
	}
	public Double getYearToDateY_3() {
		return yearToDateY_3;
	}
	public void setYearToDateY_3(Double yearToDateY_3) {
		if (yearToDateY_3!=null && yearToDateY_3.isNaN()) {
			return ;
		}
		this.yearToDateY_3 = yearToDateY_3;
	}
	public Double getYearToDateY_4() {
		return yearToDateY_4;
	}
	public void setYearToDateY_4(Double yearToDateY_4) {
		this.yearToDateY_4 = yearToDateY_4;
	}
	public Double getYearToDateY_5() {
		return yearToDateY_5;
	}
	public void setYearToDateY_5(Double yearToDateY_5) {
		this.yearToDateY_5 = yearToDateY_5;
	}
	public Double getYearToDateY_6() {
		return yearToDateY_6;
	}
	public void setYearToDateY_6(Double yearToDateY_6) {
		this.yearToDateY_6 = yearToDateY_6;
	}
	public Double getYearToDateY_7() {
		return yearToDateY_7;
	}
	public void setYearToDateY_7(Double yearToDateY_7) {
		this.yearToDateY_7 = yearToDateY_7;
	}
	public Double getYearToDateY_8() {
		return yearToDateY_8;
	}
	public void setYearToDateY_8(Double yearToDateY_8) {
		this.yearToDateY_8 = yearToDateY_8;
	}
	public Double getYearToDateY_9() {
		return yearToDateY_9;
	}
	public void setYearToDateY_9(Double yearToDateY_9) {
		this.yearToDateY_9 = yearToDateY_9;
	}
	public Double getMonthToDate() {
		return monthToDate;
	}
	public void setMonthToDate(Double monthToDate) {
		this.monthToDate = monthToDate;
	}
	public Double getQuarterToDate() {
		return quarterToDate;
	}
	public void setQuarterToDate(Double quarterToDate) {
		this.quarterToDate = quarterToDate;
	}
	public Double getSemesterToDate() {
		return semesterToDate;
	}
	public void setSemesterToDate(Double semesterToDate) {
		this.semesterToDate = semesterToDate;
	}
	public String toString() {
		JSONObject obj = new JSONObject(this);
		return obj.toString(); 
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationDate == null) ? 0 : applicationDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((targetEntityId == null) ? 0 : targetEntityId.hashCode());
		result = prime
				* result
				+ ((targetEntityType == null) ? 0 : targetEntityType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComputedStatistics other = (ComputedStatistics) obj;
		if (applicationDate == null) {
			if (other.applicationDate != null)
				return false;
		} else if (applicationDate.getTime()!=(other.applicationDate.getTime()))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (targetEntityId == null) {
			if (other.targetEntityId != null)
				return false;
		} else if (!targetEntityId.equals(other.targetEntityId))
			return false;
		if (targetEntityType == null) {
			if (other.targetEntityType != null)
				return false;
		} else if (!targetEntityType.equals(other.targetEntityType))
			return false;
		return true;
	}
	public Double getRollingSkewness36() {
	    return rollingSkewness36;
	}
	public void setRollingSkewness36(Double rollingSkewness36) {
	    if (rollingSkewness36 != null && rollingSkewness36.isNaN()) {
		return;
	    }
	    this.rollingSkewness36 = rollingSkewness36;
	}
	public Double getRollingExcessKurtosis36() {
	    return rollingExcessKurtosis36;
	}
	public void setRollingExcessKurtosis36(Double rollingExcessKurtosis36) {
	    if (rollingExcessKurtosis36 != null && rollingExcessKurtosis36.isNaN()) {
		return;
	    }
	    this.rollingExcessKurtosis36 = rollingExcessKurtosis36;
	}
	
}
