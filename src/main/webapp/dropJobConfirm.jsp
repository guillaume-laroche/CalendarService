<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="fr.axin.gservices.database.oracle.*"%>
<%@ page import="fr.axin.gservices.GCalendarService"%>
<%@ page import="java.lang.Integer"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title><%=GCalendarService.getApplicationName()%></title>
    </head>
    <link rel="icon" href="img/GoogleServices.ico" type="image/x-icon"/>
    <body>
        <%! 
            int job;    
            String host;
            String port;
            String dbuser;
            String dbpwd;
	    String sid;
            String version;
        %>
        <p>
            <font color="#000000" size="7" face="Arial">
                <font size="6">
                    <a href="index.jsp">
                        <img src="img/GoogleServices.jpg" height="80" width="80"/></a>
                    <%=GCalendarService.getApplicationName()%>
                </font>
            </font>
        </p>
        <%
            version = GCalendarService.getVersion();
        %>
         
        <font size="2" face="Arial">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <%=version%>
            &nbsp;&nbsp;&nbsp;
        </font>
         
	<a href="logs"> <img src="img/logs.png" style="width:18px; height:18px; margin-right:10px;"
		title="View Google Services log files" /></a>
	<a href="faq.jsp">
		<img src="img/help.png" alt="FAQ" style="width:18px; height:18px;" title="Show help"/>
	</a>
        <hr></hr>
        <p>
            <img src="img/oracle.jpg" height="20" width="50"/>&nbsp;<font face="Arial">Travaux de synchronisation actifs</font>
        </p>
        <hr/>
        <p>
            <font face="Arial" size="2">
                <%                
                out.write("Supprimer le travail " + request.getParameter("jobId") + "?");                
                job = Integer.parseInt(request.getParameter("jobId"));
                host = request.getParameter("host");
                port = request.getParameter("port");
				sid = request.getParameter("sid");
                dbuser = request.getParameter("dbuser");
                dbpwd = request.getParameter("dbpwd");
            %>
            </font>
        </p>
        <p>
            <input type="submit" name="submit_y" maxlength="50" size="50" value="Oui"
                   onclick="javascript:document.location.href='manageOracleJob.jsp?drop=yes&jobId=<%=job%>&host=<%=host%>&port=<%=port%>&sid=<%=sid%>&dbuser=<%=dbuser%>&dbpwd=<%=dbpwd%>'"/>
             
            <input type="submit" name="submit_n" maxlength="50" size="50" value="Non"
                   onclick="javascript:document.location.href='manageOracleJob.jsp?host=<%=host%>&port=<%=port%>&sid=<%=sid%>&dbuser=<%=dbuser%>&dbpwd=<%=dbpwd%>'"/>
        </p>
    </body>
</html>
