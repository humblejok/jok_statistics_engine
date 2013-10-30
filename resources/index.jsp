<!DOCTYPE html>
<html lang="en">
    <head>
		<meta charset="utf-8">    	
		<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		  
		<title>Statistics Engine Front End</title>
		<link rel="icon" href="./static/favicon.png" type="image/png" /> 
		<link rel="shortcut icon" href="./static/favicon.png" type="image/png" /> 
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
		<meta http-equiv="Content-Language" value="en-US" /> 
		      
		<link rel="stylesheet" href="./static/css/main.css" />
		<link rel="stylesheet" href="./static/css/jquery.ui.all.css">
		      
		<script language="JavaScript" type="text/javascript" src="./static/js/jquery-1.6.4.min.js"></script>
		<script language="JavaScript" type="text/javascript" src="./static/js/jquery-ui.js"></script>                      	
		<script language="JavaScript" type="text/javascript" src="./static/js/jquery.form.js"></script>
		<title>Statistics Viewer</title>
    </head>
	<body>
		<center><h1>Statistics Viewer</h1></center>
		<div id="statisticsTabs">
			<ul>
				<li><a href="#fundsStats">Funds Statistics</a></li>
				<li><a href="#indicesStats">Indices Statistics</a></li>
				<li><a href="#portfoliosStats">Portfolios Statistics</a></li>
			</ul>
			<div id="fundsStats">
				Fund id:&nbsp;
				<input type="text" id="fundId" value="50000000000000"/>&nbsp;
				<button id="searchButton" title="Get statistics" value="Get statistics" onclick="getStatistics(fundId,'funds')">Get statistics</button>&nbsp;
				<button id="computeButton" title="Compute statistics" value="Compute statistics" onclick="computeStatistics(fundId,'funds')">Compute statistics</button>&nbsp;
				<button id="clearButton" title="Clear statistics" value="Clear statistics" onclick="clearStatistics(fundId,'funds')">Clear statistics</button>&nbsp;
				<div id="searchFormFundResults" style="width:100%; height:800px;">
					<ul>
						<li><a href="#fundsStdStats">Standard statistics</a></li>
						<li><a href="#fundsBenStats">Benchmark statistics</a></li>
					</ul>
					<div id="fundsStdStats" style="width:97%; height:92%; overflow: scroll;">
					</div>
					<div id="fundsBenStats" style="width:97%; height:92%; overflow: scroll;">
					</div>
				</div>
			</div>
			<div id="indicesStats">
				Index id:&nbsp;
				<input type="text" id="indexId" value="370000000000000"/>&nbsp;
				<button id="searchButton" title="Get statistics" value="Get statistics" onclick="getStatistics(indexId,'indices')">Get statistics</button>&nbsp;
				<button id="computeButton" title="Compute statistics" value="Compute statistics" onclick="computeStatistics(indexId,'indices')">Compute statistics</button>&nbsp;
				<button id="clearButton" title="Clear statistics" value="Clear statistics" onclick="clearStatistics(indexId,'indices')">Clear statistics</button>&nbsp;
				<div id="searchFormIndexResults" style="width:100%; height:800px;">
					<ul>
						<li><a href="#indicesStdStats">Standard statistics</a></li>
						<li><a href="#indicesBenStats">Benchmark statistics</a></li>
					</ul>
					<div id="indicesStdStats" style="width:97%; height:92%; overflow: scroll;">
					</div>
					<div id="indicesBenStats" style="width:97%; height:92%; overflow: scroll;">
					</div>
				</div>
			</div>
			<div id="portfoliosStats">
				Portfolio id:&nbsp;
				<input type="text" id="portfolioId" value="00.00.00.0000"/>&nbsp;
				<button id="searchButton" title="Get statistics" value="Get statistics" onclick="getStatistics(portfolioId,'portfolios')">Get statistics</button>&nbsp;
				<button id="computeButton" title="Compute statistics" value="Compute statistics" onclick="computeStatistics(portfolioId,'portfolios')">Compute statistics</button>&nbsp;
				<button id="clearButton" title="Clear statistics" value="Clear statistics" onclick="clearStatistics(portfolioId,'portfolios')">Clear statistics</button>&nbsp;
				<div id="searchFormPortfolioResults" style="width:100%; height:800px;">
					<ul>
						<li><a href="#portfoliosStdStats">Standard statistics</a></li>
						<li><a href="#portfoliosBenStats">Benchmark statistics</a></li>
					</ul>
					<div id="portfoliosStdStats" style="width:97%; height:92%; overflow: scroll;">
					</div>
					<div id="portfoliosBenStats" style="width:97%; height:92%; overflow: scroll;">
					</div>
				</div>
			</div>
		</div>
		<script>
			$(function() {
				$( "#searchFormFundResults" ).tabs();
				$( "#searchFormIndexResults" ).tabs();
				$( "#searchFormPortfolioResults" ).tabs();
				$( "#statisticsTabs" ).tabs();
			});

			function getStatistics(field,tgt) {
				var jsonXHR = $.getJSON("./get-computed-statistics.jsp?entityId=" + field.value,{},
						 function(data) {
					 		var items = [];
					 		items.push('<table id="csResultsTable" style="border-style: solid; border-color: black; border:1px; width:100%; height:100%; overflow: scroll;">');
					 		items.push('<tr>');
					 		items.push('<th>Date</th>');
					 		items.push('<th>Quality</th>');
					 		items.push('<th>Last Return</th>');
					 		items.push('<th>Year to date</th>');
					 		items.push('<th>Last 3 months total return</th>');
					 		items.push('<th>Last 6 months total return</th>');
					 		items.push('<th>Last 12 months total return</th>');
					 		items.push('<th>Last 12 months standard deviation (Annualized)</th>');
					 		items.push('<th>Last 24 months standard deviation (Annualized)</th>');
					 		items.push('<th>Last 36 months standard deviation (Annualized)</th>');
					 		items.push('<th>Last 60 months standard deviation (Annualized)</th>');
					 		items.push('<th>Last 18 months maximum drawdown</th>');
					 		items.push('<th>Maximum drawdown</th>');
					 		items.push('<th>Current drawdown</th>');
					 		items.push('<th>Maximum drawdown duration</th>');
					 		items.push('<th>Current maximum drawdown sequence</th>');
					 		items.push('<th>Annualized return</th>');
					 		items.push('<th>Total return</th>');
					 		items.push('<th>Average rate of return</th>');
					 		items.push('<th>Average gain</th>');
					 		items.push('<th>Average loss</th>');
					 		items.push('<th>Percentage of positive months</th>');
					 		items.push('<th>Annualized standard deviation</th>');

					 		items.push('<th>Annualized loss deviation</th>');
					 		items.push('<th>Skewness</th>');
					 		items.push('<th>Kurtosis</th>');
					 		items.push('<th>Number of months</th>');
					 		items.push('<th>CVaR 11/12</th>');
					 		items.push('<th>Simulated CVaR 11/12</th>');
					 		items.push('<th>Simulated VaR 11/12</th>');
					 		items.push('<th>VaR 95</th>');
					 		items.push('<th>VaR 99</th>');
					 		items.push('<th>Omega since inception (10)</th>');
					 		items.push('<th>Omega over 36 months (10)</th>');
					 		items.push('</tr>');
					 		$.each(data, function (key, val) {
						 				items.push('<tr>')
						 				items.push('<td>' + val.applicationDate.substring(0,10) + '</td>');
						 				items.push('<td>' + val.status + '</td>');
						 				items.push('<td>' + val.lastReturn + '</td>');
						 				items.push('<td>' + val.yearToDate + '</td>');
						 				if (val.last3MonthsTotalReturn || val.last3MonthsTotalReturn==0) {
						 					items.push('<td>' + val.last3MonthsTotalReturn + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.last6MonthsTotalReturn || val.last6MonthsTotalReturn==0) {
						 					items.push('<td>' + val.last6MonthsTotalReturn + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.last12MonthsTotalReturn || val.last12MonthsTotalReturn==0) {
						 					items.push('<td>' + val.last12MonthsTotalReturn + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.last12MonthsAnnualizedStandardDeviation || val.last12MonthsAnnualizedStandardDeviation==0) {
						 					items.push('<td>' + val.last12MonthsAnnualizedStandardDeviation + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.last24MonthsAnnualizedStandardDeviation || val.last24MonthsAnnualizedStandardDeviation==0) {
						 					items.push('<td>' + val.last24MonthsAnnualizedStandardDeviation + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.last36MonthsAnnualizedStandardDeviation || val.last36MonthsAnnualizedStandardDeviation==0) {
						 					items.push('<td>' + val.last36MonthsAnnualizedStandardDeviation + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.last60MonthsAnnualizedReturn || val.last60MonthsAnnualizedReturn==0) {
						 					items.push('<td>' + val.last60MonthsAnnualizedReturn + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.last18MonthsMaximumDrawdown || val.last18MonthsMaximumDrawdown==0) {
						 					items.push('<td>' + val.last18MonthsMaximumDrawdown + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.maximumDrawdown || val.maximumDrawdown==0) {
						 					items.push('<td>' + val.maximumDrawdown + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				
						 				if (val.currentDrawdown || val.currentDrawdown==0) {
						 					items.push('<td>' + val.currentDrawdown + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.maximumLengthOfDrawdown || val.maximumLengthOfDrawdown==0) {
						 					items.push('<td>' + val.maximumLengthOfDrawdown + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.drawdownOngoing) {
						 					items.push('<td>' + val.drawdownOngoing + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.annualizedReturn || val.annualizedReturn==0) {
						 					items.push('<td>' + val.annualizedReturn + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.totalReturn || val.totalReturn==0) {
						 					items.push('<td>' + val.totalReturn + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.averageRateOfReturn || val.averageRateOfReturn==0) {
						 					items.push('<td>' + val.averageRateOfReturn + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.averageGain || val.averageGain==0) {
						 					items.push('<td>' + val.averageGain + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.averageLoss || val.averageLoss==0) {
						 					items.push('<td>' + val.averageLoss + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.percentageOfPositiveMonths || val.percentageOfPositiveMonths==0) {
						 					items.push('<td>' + val.percentageOfPositiveMonths + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.annualizedStandardDeviation || val.annualizedStandardDeviation==0) {
						 					items.push('<td>' + val.annualizedStandardDeviation + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.annualizedLossDeviation || val.annualizedLossDeviation==0) {
						 					items.push('<td>' + val.annualizedLossDeviation + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.skewness || val.skewness==0) {
						 					items.push('<td>' + val.skewness + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.kurtosis || val.kurtosis==0) {
						 					items.push('<td>' + val.kurtosis + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.numberOfMonths || val.numberOfMonths==0) {
						 					items.push('<td>' + val.numberOfMonths + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.cvar_11_12 || val.cvar_11_12==0) {
						 					items.push('<td>' + val.cvar_11_12 + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.cvar_11_12_simulated || val.cvar_11_12_simulated==0) {
						 					items.push('<td>' + val.cvar_11_12_simulated + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.var_11_12_simulated || val.var_11_12_simulated==0) {
						 					items.push('<td>' + val.var_11_12_simulated + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.var_95_simulated || val.var_95_simulated==0) {
						 					items.push('<td>' + val.var_95_simulated + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.var_99_simulated || val.var_99_simulated==0) {
						 					items.push('<td>' + val.var_99_simulated + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.omega10SinceInception || val.omega10SinceInception==0) {
						 					items.push('<td>' + val.omega10SinceInception + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.omega10Over36Months || val.omega10Over36Months==0) {
						 					items.push('<td>' + val.omega10Over36Months + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				
						 				
					 					}
							 		);
					 		items.push('</table>');
					 		$('#' + tgt + 'StdStats').html(items.join(''));
					 		getBenchmarkStatistics(field,tgt);
							}
				).error(function() { alert('Could not load standard statistics, please contact your administrator!');});
			}

			function getBenchmarkStatistics(field,tgt) {
				var jsonXHR = $.getJSON("./get-benchmark-related-statistics.jsp?entityId=" + field.value,{},
						 function(data) {
					 		var items = [];

					 		items.push('<table id="brsResultsTable" style="border-style: solid; border-color: black; border:1px; width:100%; height:100%; overflow: scroll;">');
					 		items.push('<tr>');
					 		items.push('<th>Date</th>');
					 		items.push('<th>Quality</th>');
					 		items.push('<th>Benchmark id</th>');
					 		items.push('<th>RFR</th>');
					 		items.push('<th>Alpha</th>');
					 		items.push('<th>Annualized alpha</th>');
					 		items.push('<th>Beta</th>');
					 		items.push('<th>Down capture</th>');
					 		items.push('<th>Down capture ratio</th>');
					 		items.push('<th>Down number ratio</th>');
					 		items.push('<th>Down percentage ratio</th>');
					 		items.push('<th>Information ratio</th>');
					 		items.push('<th>Jensen alpa</th>');
					 		items.push('<th>Percentage gain ratio</th>');
					 		items.push('<th>R</th>');
					 		items.push('<th>R2</th>');
					 		items.push('<th>Tracking error</th>');
					 		items.push('<th>Treynor ratio</th>');
					 		items.push('<th>Up capture</th>');
					 		items.push('<th>Up percentage ratio</th>');
					 		items.push('<th>Up number ratio</th>');
					 		items.push('<th>Annualized return</th>');
					 		items.push('<th>Total return</th>');
					 		items.push('<th>Average monthly ROR</th>');
					 		items.push('<th>Average monthly loss</th>');
					 		items.push('<th>Average monthly gain</th>');
					 		items.push('<th>Positive months ratio</th>');
					 		items.push('<th>Active premium</th>');
					 		items.push('<th>Annualized standard deviation</th>');
					 		items.push('<th>Annualized downside deviation (RFR)</th>');
					 		items.push('<th>Sharpe ratio (RFR)</th>');
					 		items.push('<th>Sortino ratio (RFR)</th>');
					 		items.push('</tr>');
					 		$.each(data, function (key, val) {
						 				items.push('<tr>')
						 				items.push('<td>' + val.applicationDate.substring(0,10) + '</td>');
						 				items.push('<td>' + val.status + '</td>');
						 				items.push('<td>' + val.targetEntityId + '</td>');
						 				items.push('<td>' + val.riskFreeBenchmark + '</td>');
						 				if (val.alpha || val.alpha==0) {
						 					items.push('<td>' + val.alpha + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.annualizedAlpha || val.annualizedAlpha==0) {
						 					items.push('<td>' + val.annualizedAlpha + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.beta || val.beta==0) {
						 					items.push('<td>' + val.beta + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.downCapture || val.downCapture==0) {
						 					items.push('<td>' + val.downCapture + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.downCaptureRatio || val.downCaptureRatio==0) {
						 					items.push('<td>' + val.downCaptureRatio + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.downNumberRatio || val.downNumberRatio==0) {
						 					items.push('<td>' + val.downNumberRatio + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.downPercentageRatio || val.downPercentageRatio==0) {
						 					items.push('<td>' + val.downPercentageRatio + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.informationRatio || val.informationRatio==0) {
						 					items.push('<td>' + val.informationRatio + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.jensenAlpha || val.jensenAlpha==0) {
						 					items.push('<td>' + val.jensenAlpha + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.percentageGainRatio || val.percentageGainRatio==0) {
						 					items.push('<td>' + val.percentageGainRatio + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.r || val.r==0) {
						 					items.push('<td>' + val.r + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.r2 || val.r2==0) {
						 					items.push('<td>' + val.r2 + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.trackingError || val.trackingError==0) {
						 					items.push('<td>' + val.trackingError + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.treynorRatio || val.treynorRatio==0) {
						 					items.push('<td>' + val.treynorRatio + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.upCapture || val.upCapture==0) {
						 					items.push('<td>' + val.upCapture + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.upPercentageRatio || val.upPercentageRatio==0) {
						 					items.push('<td>' + val.upPercentageRatio + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.upNumberRatio || val.upNumberRatio==0) {
						 					items.push('<td>' + val.upNumberRatio + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.annualizedReturn || val.annualizedReturn==0) {
						 					items.push('<td>' + val.annualizedReturn + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.totalReturn || val.totalReturn==0) {
						 					items.push('<td>' + val.totalReturn + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.averageMonthlyROR || val.averageMonthlyROR==0) {
						 					items.push('<td>' + val.averageMonthlyROR + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.averageMonthlyLoss || val.averageMonthlyLoss==0) {
						 					items.push('<td>' + val.averageMonthlyLoss + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.averageMonthlyGain || val.averageMonthlyGain==0) {
						 					items.push('<td>' + val.averageMonthlyGain + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.positiveMonthRatio || val.positiveMonthRatio==0) {
						 					items.push('<td>' + val.positiveMonthRatio + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.activePremium || val.activePremium==0) {
						 					items.push('<td>' + val.activePremium + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.annualizedStandardDeviation || val.annualizedStandardDeviation==0) {
						 					items.push('<td>' + val.annualizedStandardDeviation + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.annualizedDownsideDeviationRFR || val.annualizedDownsideDeviationRFR==0) {
						 					items.push('<td>' + val.annualizedDownsideDeviationRFR + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}

						 				if (val.sharpRatioRFR || val.sharpRatioRFR==0) {
						 					items.push('<td>' + val.sharpRatioRFR + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				if (val.sortinoRatioRFR || val.sortinoRatioRFR==0) {
						 					items.push('<td>' + val.sortinoRatioRFR + '</td>');
						 				} else {
						 					items.push('<td>N/A</td>');
						 				}
						 				
					 					}
							 		);
					 		items.push('</table>');
					 		$('#' + tgt + 'BenStats').html(items.join(''));

					 		
						}).error(function() { alert('Could not load benchmark statistics, please contact your administrator!');
				});
			}

			function computeStatistics(field,tgt) {
				var jsonXHR = $.getJSON("./compute-statistics.jsp?entityId=" + field.value + "&entityType=" + tgt,{},
						 function(data) {
					 		if (data.result=="true") {
					 			getStatistics(field,tgt);
					 		} else {
						 		alert("Statistics could not be computed, please contact your administrator!")
					 		}
						});
			}

			function clearStatistics(field,tgt) {
				var jsonXHR = $.getJSON("./clear-statistics.jsp?entityId=" + field.value,{},
						 function(data) {
					 		if (data.result=="true") {
					 			getStatistics(field,tgt);
					 		} else {
						 		alert("Statistics could not be cleared, please contact your administrator!")
					 		}
						});
			}
			
		</script>
	</body>
</html>