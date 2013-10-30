<%@page import="com.eim.utility.statistics.model.BenchmarkRelatedStatistics"%>
<%@page import="com.eim.utility.statistics.model.IStatistics"%>
<%@page import="java.util.Collection"%>
<%@page import="com.eim.utility.statistics.utility.HibernateUtil"%>
<%@page import="com.eim.utility.statistics.model.EntityType"%>

<%
	Collection<BenchmarkRelatedStatistics> results = HibernateUtil.getBenchmarkRelatedStatistics(request.getParameter("entityId"));
	
	if (results==null) {
		out.print("[]");
	} else {
		boolean first = true;
		StringBuffer outBuffer = new StringBuffer();
		outBuffer.append('[');
		for (BenchmarkRelatedStatistics stat : results) {
			if (!first) {
				outBuffer.append(",");
			}
			outBuffer.append(stat.toString());
			first = false;
		}
		outBuffer.append(']');
		out.print(outBuffer.toString());
	}
%>