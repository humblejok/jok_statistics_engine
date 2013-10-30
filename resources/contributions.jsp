<!DOCTYPE html>
<html lang="en">
    <head>
		<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		  
		<title>Contributions Engine Front End</title>
		<link rel="icon" href="./static/favicon.png" type="image/png" /> 
		<link rel="shortcut icon" href="./static/favicon.png" type="image/png" /> 
		<meta http-equiv="Content-Language" value="en-US" /> 
		      
		<link rel="stylesheet" href="./static/css/main.css" />
		<link rel="stylesheet" href="./static/css/jquery.ui.all.css">
		      
		<script language="JavaScript" type="text/javascript" src="./static/js/jquery-1.6.4.min.js"></script>
		<script language="JavaScript" type="text/javascript" src="./static/js/jquery-ui.js"></script>                      	
		<script language="JavaScript" type="text/javascript" src="./static/js/jquery.form.js"></script>
		<title>Contributions Viewer</title>
    </head>
	<body>
		<center><h1>Contributions Viewer</h1></center>
		<div>
			<button id="computeAllButton" title="Compute all contributions" value="Compute all contributions" onclick="computeContributions(1)">Compute all contributions</button>&nbsp;
			<button id="computeLast12MonthsButton" title="Compute all contributions for last 12 months" value="Compute all contributions for last 12 months" onclick="computeContributions(2)">Compute all contributions for last 12 months</button>&nbsp;
			<button id="computeTuesdayPortfoliosButton" title="Compute Tuesday portfolios contributions" value="Compute Tuesday portfolios contributions" onclick="computeContributions(3)">Compute Tuesday portfolios contributions</button>&nbsp;
			<button id="computeWednesdayPortfoliosButton" title="Compute Wednesday portfolios contributions" value="Compute Wednesday portfolios contributions" onclick="computeContributions(4)">Compute Wednesday portfolios contributions</button>&nbsp;
		</div>
		<script>
			function computeContributions(computationType) {
//				alert("computeContributions - Enter");
				
				// Start time
				var startTime = new Date();
//				alert("startTime gotten");

				var jsonXHR = $.getJSON("./compute-contributions.jsp?computationType=" + computationType, {} , function(data) {
						if (data.result == "true") {
							// End time
							var endTime = new Date();

							// time difference in ms
							var timeDiff = endTime - startTime;

							// strip the miliseconds
							timeDiff /= 1000;

							// get seconds
							var seconds = Math.round(timeDiff % 60);

							// remove seconds from the date
							timeDiff /= Math.round(60);

							// get minutes
							var minutes = Math.round(timeDiff % 60);

							// remove minutes from the date
							timeDiff /= Math.round(60);

							// get hours
							var hours = Math.round(timeDiff % 24);

							// remove hours from the date
							timeDiff /= Math.round(24);

							// the rest of timeDiff is number of days
							var days = timeDays;

							alert("Contributions have been computed successfully in " + days + " days, " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds");
						}
						else {
							alert("Contributions could not be computed, please contact your administrator!");
						}
					}
				);
				jsonXHR.error(function(jqXHR, textStatus, errorThrown) {
				    alert("Error: " + textStatus + " errorThrown: " + errorThrown);
				});
			}
		</script>
	</body>
</html>