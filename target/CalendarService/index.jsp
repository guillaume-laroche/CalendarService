<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="fr.axin.gservices.GCalendarService"%>
<%@ page import="fr.axin.gservices.auth.GoogleAuth"%>
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252"></meta>
<title><%=GCalendarService.getApplicationName()%></title>
</head>
<link rel="icon" href="img/GoogleServices.ico" type="image/x-icon" />
<body>
	<%!String version;%>

	<%
		version = GCalendarService.getVersion();
			request.getSession().getServletContext()
			.getInitParameter("virtualLogDirectory");
	%>
	<p>
		<font color="#000000" size="7" face="Arial"> <font size="6">
				<img src="img/GoogleServices.jpg" height="80" width="80" />
		<%=GCalendarService.getApplicationName()%>
		</font>
		</font>		
	</p>
	<font size="2" face="Arial">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%=version%> &nbsp;&nbsp;&nbsp;
	</font>

	<a href="logs"> <img src="img/logs.png" style="width:18px; height:18px; margin-right:10px;"
		title="View Google Services log files" /></a>
	<a href="faq.jsp">
		<img src="img/help.png" alt="FAQ" style="width:18px; height:18px;" title="Show help"/>
	</a>
	<hr></hr>
	<p>
		<img src="img/GoogleCalendar.png" type="image/x-icon" height="18"
			width="18" /> &nbsp;<font face="Arial">Google Calendar
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
	</p>
	<p>
		<font face="Arial" size="3"> <font size="2">
				<p>
					<a href="googleAuthRedirect.jsp?redirect=oracle">Synchronisation one-shot</a>
				</p>
				<p>
					<a href="googleAuthRedirect.jsp?redirect=submitOracleJob">Soumettre un travail de synchronisation</a>
				</p>
				<p>
					<a href="credentials.jsp">Gestion des travaux de synchronisation</a>
				</p>
		</font>
		</font>
	</p>
</body>
</html>
