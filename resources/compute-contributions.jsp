<%@page import="com.eim.utility.common.utility.HibernateUtil"%>

<%
int computationType = Integer.parseInt(request.getParameter("computationType"));

switch (computationType) {
	case 1:
		out.print(HibernateUtil.computeAllContributions());
		break;
	case 2:
		out.print(HibernateUtil.computeLast12MonthsContributions());
		break;
	case 3:
		out.print(HibernateUtil.computeTuesdayPortfoliosContributions());
		break;
	case 4:
		out.print(HibernateUtil.computeWednesdayPortfoliosContributions());
		break;
	default:
		out.print("{ result: \"" + (false) + "\"}");
}

%>