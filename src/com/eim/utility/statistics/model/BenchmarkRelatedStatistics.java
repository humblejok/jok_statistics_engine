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
 * POJO representing the <B>benchmark</B> related statistics.<BR/><BR/>
 * It is annotated with hibernate annotations for persistence purpose.<BR/>
 * These statistics are persisted in the table <I>STATS_BENCHMARK</I> and identified using a sequence called <I>SEQ_STATS_BENCHMARK</I>.
 * That POJO possesses 7 fields (other than the <code>id</code> field) that are mandatory:<BR/>
 * <ul>
 * <li>targetEntityType</li>
 * <li>targetEntityId</li>
 * <li>relatedEntityType</li>
 * <li>relatedEntityId</li>
 * <li>applicationDate</li>
 * <li>computationDate</li>
 * <li>status</li>
 * </ul>
 * @author sdj
 *
 */
@Entity
@Table(name="STATS_BENCHMARK")
@SequenceGenerator (name="SEQ_STATS_BENCHMARK",sequenceName="SEQ_STATS_BENCHMARK",allocationSize=1)
public class BenchmarkRelatedStatistics implements Serializable, IStatistics {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -5461773036612608389L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_STATS_BENCHMARK")
	private Long id;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private EntityType targetEntityType;
	@Column(nullable=false)
	private String targetEntityId;

	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private EntityType relatedEntityType;
	
	@Column(nullable=false)
	private String relatedEntityId;
	
	@Column(nullable=false)
	private boolean riskFreeBenchmark;
	
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
	
	private Double alpha;
	
	private Double annualizedAlpha;
	
	private Double beta;
	private Double downCapture;
	private Double downCaptureRatio;
	private Double downNumberRatio;
	
	private Double downPercentageRatio;
	private Double informationRatio;
	private Double jensenAlpha;

	private Double percentageGainRatio;
	
	private Double r;
	private Double r2;
	private Double trackingError;
	private Double treynorRatio;
	private Double upCapture;
	private Double upPercentageRatio;
	private Double upNumberRatio;
	private Double annualizedReturn;
	private Double totalReturn;
	private Double averageMonthlyROR;
	private Double averageMonthlyLoss;
	private Double averageMonthlyGain;
	
	private Double positiveMonthRatio;
	private Double activePremium;
	private Double annualizedStandardDeviation;
	private Double annualizedVolatility;
	private Double annualizedDownsideDeviationRFR;
	private Double sharpRatioRFR;
	private Double sortinoRatioRFR;
	
	private Double maximumDrawdown;
	private Integer maximumLengthOfDrawdown = 0;
	private String drawdownOngoing = "false";
	
	@Column(name="LAST18MONTHSROLLINGALPHA")
	private Double last18MonthsRollingAlpha;
	@Column(name="LAST18MONTHSROLLINGBETA")
	private Double last18MonthsRollingBeta;
	
	
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
	public EntityType getRelatedEntityType() {
		return relatedEntityType;
	}
	public void setRelatedEntityType(EntityType relatedEntityType) {
		this.relatedEntityType = relatedEntityType;
	}
	public String getRelatedEntityId() {
		return relatedEntityId;
	}
	public void setRelatedEntityId(String relatedEntityId) {
		this.relatedEntityId = relatedEntityId;
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
	public Double getAlpha() {
		return alpha;
	}
	public void setAlpha(Double alpha) {
		if (alpha!=null && alpha.isNaN()) {
			return ;
		}
		this.alpha = alpha;
	}
	public Double getAnnualizedAlpha() {
		return annualizedAlpha;
	}
	public void setAnnualizedAlpha(Double annualizedAlpha) {
		if (annualizedAlpha!=null && annualizedAlpha.isNaN()) {
			return ;
		}
		this.annualizedAlpha = annualizedAlpha;
	}
	public Double getBeta() {
		return beta;
	}
	public void setBeta(Double beta) {
		if (beta!=null && beta.isNaN()) {
			return ;
		}
		this.beta = beta;
	}
	public Double getDownCapture() {
		return downCapture;
	}
	public void setDownCapture(Double downCapture) {
		if (downCapture!=null && downCapture.isNaN()) {
			return ;
		}
		this.downCapture = downCapture;
	}
	public Double getDownCaptureRatio() {
		return downCaptureRatio;
	}
	public void setDownCaptureRatio(Double downCaptureRatio) {
		if (downCaptureRatio!=null && downCaptureRatio.isNaN()) {
			return ;
		}
		this.downCaptureRatio = downCaptureRatio;
	}
	public Double getDownNumberRatio() {
		return downNumberRatio;
	}
	public void setDownNumberRatio(Double downNumberRatio) {
		if (downNumberRatio!=null && downNumberRatio.isNaN()) {
			return ;
		}
		this.downNumberRatio = downNumberRatio;
	}
	public Double getDownPercentageRatio() {
		return downPercentageRatio;
	}
	public void setDownPercentageRatio(Double downPercentageRatio) {
		if (downPercentageRatio!=null && downPercentageRatio.isNaN()) {
			return ;
		}
		this.downPercentageRatio = downPercentageRatio;
	}
	public Double getInformationRatio() {
		return informationRatio;
	}
	public void setInformationRatio(Double informationRatio) {
		if (informationRatio!=null && informationRatio.isNaN()) {
			return ;
		}
		this.informationRatio = informationRatio;
	}
	public Double getJensenAlpha() {
		return jensenAlpha;
	}
	public void setJensenAlpha(Double jensenAlpha) {
		if (jensenAlpha!=null && jensenAlpha.isNaN()) {
			return ;
		}
		this.jensenAlpha = jensenAlpha;
	}
	public Double getPerGainRatio() {
		return percentageGainRatio;
	}
	public void setPercentageGainRatio(Double perGainRatio) {
		if (perGainRatio!=null && perGainRatio.isNaN()) {
			return ;
		}
		this.percentageGainRatio = perGainRatio;
	}
	public Double getR() {
		return r;
	}
	public void setR(Double r) {
		if (r!=null && r.isNaN()) {
			return ;
		}
		this.r = r;
	}
	public Double getR2() {
		return r2;
	}
	public void setR2(Double r2) {
		if (r2!=null && r2.isNaN()) {
			return ;
		}
		this.r2 = r2;
	}
	public Double getTrackingError() {
		return trackingError;
	}
	public void setTrackingError(Double trackingError) {
		if (trackingError!=null && trackingError.isNaN()) {
			return ;
		}
		this.trackingError = trackingError;
	}
	public Double getTreynorRatio() {
		return treynorRatio;
	}
	public void setTreynorRatio(Double treynorRatio) {
		if (treynorRatio!=null && treynorRatio.isNaN()) {
			return ;
		}
		this.treynorRatio = treynorRatio;
	}
	public Double getUpCapture() {
		return upCapture;
	}
	public void setUpCapture(Double upCapture) {
		if (upCapture!=null && upCapture.isNaN()) {
			return ;
		}
		this.upCapture = upCapture;
	}
	public Double getUpPercentageRatio() {
		return upPercentageRatio;
	}
	public void setUpPercentageRatio(Double upPercentageRatio) {
		if (upPercentageRatio!=null && upPercentageRatio.isNaN()) {
			return ;
		}
		this.upPercentageRatio = upPercentageRatio;
	}
	public Double getUpNumberRatio() {
		return upNumberRatio;
	}
	public void setUpNumberRatio(Double upNumberRatio) {
		if (upNumberRatio!=null && upNumberRatio.isNaN()) {
			return ;
		}
		this.upNumberRatio = upNumberRatio;
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
	public Double getAverageMonthlyROR() {
		return averageMonthlyROR;
	}
	public void setAverageMonthlyROR(Double averageMonthlyROR) {
		if (averageMonthlyROR!=null && averageMonthlyROR.isNaN()) {
			return ;
		}
		this.averageMonthlyROR = averageMonthlyROR;
	}
	public Double getAverageMonthlyLoss() {
		return averageMonthlyLoss;
	}
	public void setAverageMonthlyLoss(Double averageMonthlyLoss) {
		if (averageMonthlyLoss!=null && averageMonthlyLoss.isNaN()) {
			return ;
		}
		this.averageMonthlyLoss = averageMonthlyLoss;
	}
	public Double getAverageMonthlyGain() {
		return averageMonthlyGain;
	}
	public void setAverageMonthlyGain(Double averageMonthlyGain) {
		if (averageMonthlyGain!=null && averageMonthlyGain.isNaN()) {
			return ;
		}
		this.averageMonthlyGain = averageMonthlyGain;
	}
	public Double getPositiveMonthRatio() {
		return positiveMonthRatio;
	}
	public void setPositiveMonthRatio(Double positiveMonthRatio) {
		if (positiveMonthRatio!=null && positiveMonthRatio.isNaN()) {
			return ;
		}
		this.positiveMonthRatio = positiveMonthRatio;
	}
	public Double getActivePremium() {
		return activePremium;
	}
	public void setActPremium(Double activePremium) {
		if (activePremium!=null && activePremium.isNaN()) {
			return ;
		}
		this.activePremium = activePremium;
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
	public Double getAnnualizedDownsideDeviationRFR() {
		return annualizedDownsideDeviationRFR;
	}
	public void setAnnualizedDownsideDeviationRFR(Double downSideDeviationRFR) {
		if (annualizedDownsideDeviationRFR!=null && annualizedDownsideDeviationRFR.isNaN()) {
			return ;
		}
		this.annualizedDownsideDeviationRFR = downSideDeviationRFR;
	}
	public Double getSharpRatioRFR() {
		return sharpRatioRFR;
	}
	public void setSharpRatioRFR(Double sharpRatioRFR) {
		if (sharpRatioRFR!=null && sharpRatioRFR.isNaN()) {
			return ;
		}
		this.sharpRatioRFR = sharpRatioRFR;
	}
	public Double getSortinoRatioRFR() {
		return sortinoRatioRFR;
	}
	public void setSortinoRatioRFR(Double sortinoRatioRFR) {
		if (sortinoRatioRFR!=null && sortinoRatioRFR.isNaN()) {
			return ;
		}
		this.sortinoRatioRFR = sortinoRatioRFR;
	}
	
	public void setRiskFreeBenchmark(boolean riskFreeBenchmark) {
		this.riskFreeBenchmark = riskFreeBenchmark;
	}
	public boolean isRiskFreeBenchmark() {
		return riskFreeBenchmark;
	}
	public void setAnnualizedVolatility(Double annualizedVolatility) {
		if (annualizedVolatility!=null && annualizedVolatility.isNaN()) {
			return ;
		}
		this.annualizedVolatility = annualizedVolatility;
	}
	public Double getAnnualizedVolatility() {
		return annualizedVolatility;
	}
	
	public Double getMaximumDrawdown() {
		return maximumDrawdown;
	}
	public void setMaximumDrawdown(Double maximumDrawdown) {
		this.maximumDrawdown = maximumDrawdown;
	}
	public Integer getMaximumLengthOfDrawdown() {
		return maximumLengthOfDrawdown;
	}
	public void setMaximumLengthOfDrawdown(Integer maximumLengthOfDrawdown) {
		this.maximumLengthOfDrawdown = maximumLengthOfDrawdown;
	}
	public String getDrawdownOngoing() {
		return drawdownOngoing;
	}
	public void setDrawdownOngoing(String drawdownOngoing) {
		this.drawdownOngoing = drawdownOngoing;
	}
	public Double getPercentageGainRatio() {
		return percentageGainRatio;
	}
	public void setActivePremium(Double activePremium) {
		this.activePremium = activePremium;
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
		BenchmarkRelatedStatistics other = (BenchmarkRelatedStatistics) obj;
		if (applicationDate == null) {
			if (other.applicationDate != null)
				return false;
		} else if (applicationDate.getTime()!=other.applicationDate.getTime())
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
	public Double getLast18MonthsRollingBeta() {
	    return last18MonthsRollingBeta;
	}
	public void setLast18MonthsRollingBeta(Double last18MonthsRollingBeta) {
	    if (last18MonthsRollingBeta != null && last18MonthsRollingBeta.isNaN()) {
		return;
	    }
	    this.last18MonthsRollingBeta = last18MonthsRollingBeta;
	}
	public Double getLast18MonthsRollingAlpha() {
	    return last18MonthsRollingAlpha;
	}
	public void setLast18MonthsRollingAlpha(Double last18MonthsRollingAlpha) {
	    if (last18MonthsRollingAlpha != null && last18MonthsRollingAlpha.isNaN()) {
		return;
	    }
	    this.last18MonthsRollingAlpha = last18MonthsRollingAlpha;
	}

}
