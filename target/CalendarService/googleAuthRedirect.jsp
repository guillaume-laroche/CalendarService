<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="fr.axin.gservices.GCalendarService"%>
<%@ page import="fr.axin.gservices.auth.GoogleAuth"%>
<html>
<%
	GoogleAuth auth = new GoogleAuth(request.getParameter("redirect"),
			request.getSession()
			.getServletContext().getInitParameter("httpHost"), request
			.getSession().getServletContext()
			.getInitParameter("webappClientId"), request.getSession()
			.getServletContext().getInitParameter("webappClientSecret"));
	if (request.getParameter("code") == null
			|| request.getParameter("state") == null) {

		/*
		 * set the secure state token in session to be able to track what we sent to google
		 */
		session.setAttribute("state", auth.getStateToken());

	} else if (request.getParameter("code") != null
			&& request.getParameter("state") != null
			&& request.getParameter("state").equals(
					session.getAttribute("state"))) {

		session.removeAttribute("state");
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252"></meta>
<meta http-equiv="refresh" content="1; URL=<%=auth.buildLoginUrl()%>">
<title><%=GCalendarService.getApplicationName()%></title>
</head>
<link rel="icon" href="img/GoogleServices.ico" type="image/x-icon" />
<body>
	Redirection vers authentification Google OAuth2...
</body>
</html>


