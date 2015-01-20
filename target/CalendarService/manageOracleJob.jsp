<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="fr.axin.gservices.database.oracle.*"%>
<%@ page import="fr.axin.gservices.GCalendarService"%>
<%@ page import="java.util.List"%>
<html>
    <head>
        <style type="text/css">
                table {
                    font-family: "Arial";
                    font-size: 14px;
                    border-style: solid;
                    border-width: 1px;
                }

                th {
                    background-color: rgb(158, 192, 255);
                }

                .pair {
                    background-color: rgb(210, 210, 210);
                }

                .impair {
                    background-color: rgb(240, 240, 240);
                }
            </style>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title><%=GCalendarService.getApplicationName()%> Logs</title>
    </head>
    <link rel="icon" href="img/GoogleServices.ico" type="image/x-icon"/>
    <body>
        <%! 
            OracleConnection conn;
            List<OracleJob> jobs; 
            String drop;
            int jobId;
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
        <%
            try {
                drop = request.getParameter("drop");
                jobId = Integer.parseInt(request.getParameter("jobId"));
            }
            catch(Exception e) {
                drop = "no";
                jobId = 0;
            }
            try {                
                conn = new OracleConnection(request.getParameter("host"), request.getParameter("port"),
                request.getParameter("sid"), request.getParameter("dbuser"), request.getParameter("dbpwd"));
                conn.connect();                
                if(drop.equals("yes") && jobId > 0) {
                    conn.getJob(jobId).drop();                                                           
                }
                
                jobs = conn.getJobs();
                if(!jobs.isEmpty()) {
                    out.write("<table>");
                    out.write("<tr>");
                    out.write("<th>Job</th>");
                    out.write("<th>Dernière exécution</th>");
                    out.write("<th>Intervalle</th>");
                    out.write("<th>Prochaine exécution</th>");
                    out.write("<th>Traitement</th>");
                    out.write("<th>Suppression</th>");
                    out.write("</tr>");
                    for(int i = 0; i < jobs.size(); i++) {
                        if(i%2 == 0) out.write("<tr class=pair>");
                        else out.write("<tr class=impair>");
                        out.write("<td>" + jobs.get(i).getJobId() + "</td>");    
                        out.write("<td>" + jobs.get(i).getLastDate() + "</td>");
                        out.write("<td>" + jobs.get(i).getInterval() + "</td>");
                        out.write("<td>" + jobs.get(i).getNextDate() + "</td>");
                        out.write("<td>" + jobs.get(i).getWhat() + "</td>");                        
                        out.write("<td align=\"center\"><a href=\"dropJobConfirm.jsp?host="+request.getParameter("host")+"&port="+request.getParameter("port")+"&sid="+request.getParameter("sid")+"&dbuser="+request.getParameter("dbuser")+"&dbpwd="+request.getParameter("dbpwd")+"&jobId=" + jobs.get(i).getJobId() + "\">" +
                              "<img src=\"img/delete.gif\" widht=\"15\" height=\"15\" title=\"Click to drop job\"/></a>");
                        out.write("</tr>");
                    }
                    out.write("</table>");
                }                                    
                else {
                   out.write("<font face=\"Arial\" size=\"2\">");
                   out.write("<p>Aucun travail actif pour l'utilisateur courant</p>");                   
                   out.write("</font>");
                }
                
                conn.disconnect();
            }
            catch(OracleException oraE) {
                if(conn.isConnected()) {
                    conn.disconnect();
                }
                out.write("<font face=\"Arial\" size=\"2\">");
                out.write("<p>" + "<img src=\"img/error.gif\" width=\"20\" height=\"20\"> &nbsp&nbsp" + "Erreur de connexion</p>");
                out.write("<p>" + oraE.getMessage() + "</p>");
                out.write("</font>");
            }
        %>
    </body>
</html>