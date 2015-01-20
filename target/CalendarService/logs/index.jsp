<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="fr.axin.gservices.servlet.html.LogFileList"%>
<%@ page import="fr.axin.gservices.GCalendarService"%>
<%@ page import="java.lang.Integer"%>
<%@ page import="java.io.File"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Google Calendar Service - Journal</title>
    </head>
    <link rel="icon" href="../img/GoogleServices.ico" type="image/x-icon"/>
    <body>
        <%! 
            LogFileList log;             
            String delete;
            String filename;
            int index;
            String version;
        %>
        <p>
            <font color="#000000" size="7" face="Arial">
                <font size="6">
                    <a href="../index.jsp">
                        <img src="../img/GoogleServices.jpg" height="80" width="80"/></a>
                    Google Calendar Service
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
         
        <img src="../img/logs.png" height="20" width="20" title="View Google Services log files"/>
        <hr></hr>
        <p>
            <img src="../img/logs.png" type="image/x-icon" height="20" width="20"/>
             &nbsp;<font face="Arial">Liste des fichiers journaux
                                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
        </p>
        <%
            try {
                delete = request.getParameter("delete");
                index = Integer.parseInt(request.getParameter("fileIndex"));
            }
            catch(Exception e) {
                delete = "no";
                index = -1;
            }
            
            if(delete.equals("yes") && index >= 0) {
                filename = LogFileList.getFileList()[index].getName();
                if(LogFileList.getFileList()[index].delete()) {
                        out.write("<font face=\"Arial\" size=\"2\" color=\"green\">");
                        out.write("Le fichier " + filename + " a été supprimé");
                        out.write("</font>");
                }                        
                else {
                        out.write("<font face=\"Arial\" size=\"2\" color=\"red\">");
                        out.write("Le fichier " + filename + " n'a pas été supprimé");
                        out.write("</font>");
                }
            }
        %>
         
        <font size="2" face="Arial">
            <% 
            log = null;            
            log = new LogFileList(request.getSession().getServletContext().getInitParameter("serverLogDirectory"));            
            for (int i = 0; i < log.getCount(); i++) {            
                if(log.getFileAt(i).getName().toLowerCase().endsWith(".log")) {
                    out.write("<p>");                    
                    out.write(log.getFileAt(i).getName() + "   " + 
                              "<a href=\"" + request.getSession().getServletContext().getInitParameter("virtualLogDirectory") + "/" + log.getFileAt(i).getName() + "\" target=\"_blank\">" +
                              "<img src=\"../img/view.gif\" widht=\"20\" height=\"20\" title=\"Cliquer pour affiche le fichier\"/></a>   " +
                              "<a href=\"confirm.jsp?fileIndex=" + i + "\">" +
                              "<img src=\"../img/delete.gif\" widht=\"20\" height=\"20\" title=\"Cliquer pour supprimer le fichier\"/></a>");                              
                    out.write("</p>");
                }
            } %>
        </font>
    </body>
</html>