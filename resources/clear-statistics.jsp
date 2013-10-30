<%@page import="com.eim.utility.statistics.utility.HibernateUtil"%>

<%
	out.print(HibernateUtil.dropAllStatistics(request.getParameter("entityId")));
%>