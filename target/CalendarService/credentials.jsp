<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="fr.axin.gservices.GCalendarService"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"></meta>
        <title><%=GCalendarService.getApplicationName()%></title>
    </head>
    <link rel="icon" href="img/GoogleServices.ico" type="image/x-icon"/>
    <body>
        <%!
            String version;
        %>
         
        <%
            version = GCalendarService.getVersion();
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
            <img src="img/oracle.jpg" height="20" width="50" alt="oracle.jpg"/>
             &nbsp;<font face="Arial">Oracle Database Service</font>
        </p>
        <form name="Credentials" action="manageOracleJob.jsp" method="post">
            <table>
			<tr>
				<td><label for="host"> <font face="Arial">Hôte Oracle</font>
				</label></td>
				<td><input type="text" name="host" id="host" maxlength="30"
					size="30" value=""
					title="Nom ou adresse IP du serveur Oracle" /></td>
			</tr>

			<tr>
				<td><label for="port"> <font face="Arial">Port</font>
				</label></td>
				<td><input type="text" name="port" id="port" maxlength="10"
					size="10" value="1521" title="Port d'écoute du listener, par défaut 1521" />
				</td>
			</tr>

			<tr>
				<td><label for="sid"> <font face="Arial">SID</font>
				</label></td>
				<td><input type="text" name="sid" id="sid" maxlength="30"
					size="30" title="SID base Oracle" value="" /></td>
			</tr>

			<tr>
				<td><label for="dbuser"> <font face="Arial">Utilisateur Oracle</font>
				</label></td>
				<td><input type="text" name="dbuser" id="dbuser" maxlength="30"
					size="30" title="Compte utilisateur Oracle" value="" /></td>
			</tr>

			<tr>
				<td><label for="dbpwd"> <font face="Arial">Mot de passe</font>
				</label></td>
				<td><input type="password" name="dbpwd" id="dbpwd"
					maxlength="30" size="30" title="Mot de passe Oracle"
					value="" /></td>
			</tr>
                 
                <tr>
                    <td align="center" colspan="2">
                        <font face="Arial">
                            <input type="submit" name="submit" maxlength="50" size="50" value="Connexion"/>
                        </font>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>