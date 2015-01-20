<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="fr.axin.gservices.GCalendarService"%>
<%@ page import="fr.axin.gservices.auth.GoogleAuth"%>
<%@ page import="com.google.api.services.calendar.model.CalendarList"%>
<%@ page import="com.google.api.services.calendar.model.CalendarListEntry"%>
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
			GoogleAuth.setCode(request.getParameter("code"));
			GoogleAuth.setCredentialWeb();
			GoogleAuth.createCalendarInstance();        
        %>
        <p>
            <font color="#000000" size="7" face="Arial">
                <font size="6">
                    <a href="index.jsp">
                        <img src="img/GoogleServices.jpg" height="80" width="80"/></a>
                    <%=GCalendarService.getApplicationName()%>
                </font>
            </font>
            <span style="font-family:Arial;font-style:italic;">(<%=GoogleAuth.getEmail()%>)</span>
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
             &nbsp;<font face="Arial">Oracle Database Service - Job submit</font>
        </p>
        <form name="Parameters" action="OracleJobSubmitServlet" method="post">
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
				<td><label for="syncType"> <font face="Arial">Type de synchronisation</font>
				</label></td>
				<td><select size="1" name="syncType" id="syncType"
					title="SYSDATE : Date de référénce = SYSDATE. RANGE : synchronisation entre 2 dates données">
						<option value="SYSDATE">Sysdate</option>
						<option value="RANGE">Range</option>
				</select></td>
			</tr>

			<tr>
				<td><label for="arg1"> <font face="Arial">Paramètre n°1</font>
				</label></td>
				<td><input type="text" name="arg1" id="arg1" maxlength="30"
					size="30"					
					value="" /></td>
			</tr>

			<tr>
				<td><label for="arg2"> <font face="Arial">Paramètre n°2</font>
				</label></td>
				<td><input type="text" name="arg2" id="arg2" maxlength="30"
					size="30"					
					value="" /></td>
			</tr>

			<tr>
				<td><label for="identifier"> <font face="Arial">Identifiant</font>
				</label></td>
				<td><input type="text" name="identifier" id="identifier"
					maxlength="30" size="30"
					title="Clé permettant de sélectionner les événements dans la base de données"
					value="" /></td>
					<td><input type="hidden" name="service" id="service"									
					value="false" /></td>
			</tr>

			<tr>
				<td><label for="ID"> <font face="Arial">Agenda</font>
				</label></td>
				<td>
					<%
						CalendarList feed = GoogleAuth.getClient().calendarList().list().execute();
					%> 
					<select size="1" name="ID" id="ID" title="Google Agenda ID">
						<%
							if (feed.getItems() != null) {
								for (CalendarListEntry entry : feed.getItems()) {
									if(entry.getAccessRole().equals("owner") || entry.getAccessRole().equals("writer"))
										out.println("<option value=\"" + entry.getId() + "\">" + entry.getSummary() + "</option>");
								}
							}
						%>
						
				</select> 
				</td>
			</tr>
                 
                <tr>
                    <td>
                        <label for="syncInterval">
                            <font face="Arial">Intervalle</font>
                        </label>
                    </td>
                    <td>
                        <select size="1" name="syncInterval" id="syncInterval">
                            <option value="720">12 heures</option>
                            <option value="360">6 heures</option>
                            <option value="180">3 heures</option>
                            <option value="120">2 heures</option>
                            <option value="60">1 heure</option>
                            <option value="30">30 minutes</option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td>
                        <input type="hidden" name="user" id="user" value="<%=GoogleAuth.getEmail()%>"/>
                    </td>
                </tr>                 
                 
                <tr>
                    <td align="center" colspan="2">
                        <font face="Arial">
                            <input type="submit" name="submit" maxlength="50" size="50" value="Soumettre travail"/>
                        </font>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>