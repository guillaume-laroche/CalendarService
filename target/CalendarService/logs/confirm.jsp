<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="fr.axin.gservices.servlet.html.LogFileList"%>
<%@ page import="fr.axin.gservices.GCalendarService"%>
<%@ page import="java.io.File"%>
<%@ page import="java.lang.Integer"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Google Calendar Service - Journal</title>
    </head>
    <link rel="icon" href="../img/GoogleServices.ico" type="image/x-icon"/>
    <body>
        <%! 
            File[] logs;
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
         
        <a href="logs">
            <img src="../img/logs.png" height="20" width="20" title="View Google Services log files"/></a>
        <hr></hr>
        <p>
            <font face="Arial" size="2">
                <%
                logs = LogFileList.getFileList();
                index = Integer.parseInt(request.getParameter("fileIndex"));
                out.write("Supprimer le fichier " + logs[index].getName() + "?");                
            %>
            </font>
        </p>
        <p>
            <input type="submit" name="submit_y" maxlength="50" size="50" value="Oui"
                   onclick="javascript:document.location.href='index.jsp?delete=yes&fileIndex=<%=index%>'"/>
             
            <input type="submit" name="submit_n" maxlength="50" size="50" value="Non"
                   onclick="javascript:document.location.href='index.jsp'"/>
        </p>
    </body>
</html>
