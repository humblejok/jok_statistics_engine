<%@page import="com.eim.utility.statistics.utility.HibernateUtil"%>

<%
	out.print(HibernateUtil.computeAllStatistics(request.getParameter("entityId"),request.getParameter("entityType")));
%>