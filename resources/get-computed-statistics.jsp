<%@page import="com.eim.utility.statistics.model.ComputedStatistics"%>
<%@page import="com.eim.utility.statistics.model.IStatistics"%>
<%@page import="java.util.Collection"%>
<%@page import="com.eim.utility.statistics.utility.HibernateUtil"%>
<%@page import="com.eim.utility.statistics.model.EntityType"%>

<%
	Collection<ComputedStatistics> results = HibernateUtil.getComputedStatistics(request.getParameter("entityId"));
	
	if (results==null) {
		out.print("[]");
	} else {
		boolean first = true;
		StringBuffer outBuffer = new StringBuffer();
		outBuffer.append('[');
		for (ComputedStatistics stat : results) {
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