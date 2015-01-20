<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="fr.axin.gservices.GCalendarService"%>
<%@ page import="fr.axin.gservices.auth.GoogleAuth"%>
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252"></meta>
<title><%=GCalendarService.getApplicationName()%></title>
<style>
li {font-weight:bold; margin-top:5px;}
.explain {font-style:italic; font-size:9pt;}
</style>
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
				<a href="index.jsp" ><img src="img/GoogleServices.jpg" height="80" width="80" /></a>
		<%=GCalendarService.getApplicationName()%>
		</font>
		</font>
	</p>
	<font size="2" face="Arial">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%=version%> &nbsp;&nbsp;&nbsp;
	</font>

	<a href="logs"> <img src="img/logs.png" height="20" width="20"
		title="View Google Services log files" /></a>
	<hr></hr>
	<p>
		<img src="img/GoogleCalendar.png" type="image/x-icon" height="20"
			width="20" /> &nbsp;<font face="Arial">Google Calendar
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
	</p>	
	<div style="font-family:Arial; font-size:10pt">
	
		<p>
			Les donn�es export�es de la base Oracle et int�gr�es � Google Agenda sont extraites depuis une vue (ou une table) Oracle.<br/>
			La vue doit porter le nom V_PLANNING_EXPORT et doit suivre une structure bien pr�cise.<br/>
			Il est donc possible de centraliser des entit�s diff�rentes dans cette vue pour les exporter vers l'agenda (OF, dossiers support, actions client, etc.)<br/>
			Le descriptif des diff�rentes colonnes figure ci-dessous.
		</p>	
		<p>
			<ul>
				<li>STARTDATE</li>
					<span class="explain">
					&#8658; Date de d�but de l'�v�nement, suivant le RFC3339. Pour une date seule, le format est YYYY-MM-DD<br/>
					Pour une date et une heure, le format est YYYY-MM-DDTHH24:MI:SS+01:00 (le +01:00 indique le fuseau horaire).
					</span>
				<li>ENDDATE</li>
					<span class="explain">
					&#8658; Date de fin de l'�v�nement, suivant le RFC3339.
					</span>
				<li>DDATE</li>
					<span class="explain">
					&#8658; Date d�but de l'�v�nement au format DD/MM/YYYY, de type VARCHAR2
					</span>
				<li>EDATE</li>
					<span class="explain">
					&#8658; Date de d�but de l'�v�nement, de type DATE
					</span>					
				<li>IDENTIFIER</li>
					<span class="explain">
					&#8658; Identifiant de la personne (ou du groupe de personnes) concern� par l'�v�nement, utilis� pour filter suivant les param�tres utilisateurs.
					</span>
				<li>TITLE</li>
					<span class="explain">
					&#8658; Titre de l'�v�nement qui appara�tra sur Google Agenda.
					</span>
				<li>DESCRIPTION</li>
					<span class="explain">
					&#8658; Description de l'�v�nement qui appar�tra dans Google Agenda.
					</span>
				<li>LOCATION</li>
					<span class="explain">
					&#8658; Adresse de l'�v�nement qui appara�tra dans Google Agenda et servira � le localiser sur Google Maps.<br/>
					Le format doit �tre le suivant : AXIN 21 Rue Icare 67960 Entzheim, France
					</span>
				<li>GUID</li>
					<span class="explain">
					&#8658; Identifiant unique de l'�v�nement, au format cha�ne de caract�re. Bien s'assurer de l'unicit� de cette valeur dans l'agenda concern�.
					</span>
				<li>ALLDAY</li>
					<span class="explain">
					&#8658; Indique si l'�v�nement dure toute la journ�e, il sera alors affich� dans un cadre � part dans l'agenda. Indiquer la valeur O ou N.<br/>
					Si la valeur O est indiqu�e, il est alors inutile d'indiquer l'heure dans les champs STARTDATE et ENDDATE.
					</span>
				<li>COLOR</li>
					<span class="explain">
					&#8658; Code de la couleur d'affichage de l'�v�nement, si non indiqu�, l'�v�nement prendra la couleur par d�faut de l'agenda.<br/>
					Les valeurs num�riques possibles vont de 1 � 11, le descriptif complet est disponible <a href="img/google_calendar_events_color.JPG" target="_blank">ici</a>.
					</span>
				<li>REMINDER_METHOD</li>
					<span class="explain">
					&#8658; Renseigner cette valeur pour cr�er un rappel sur l'�v�nement, ce champ en indique alors la m�thode, 3 valeurs possibles (case sensitive) : email, popup, sms
					</span>
				<li>REMINDER_MINUTES</li>
					<span class="explain">
					&#8658; Si un rappel a �t� sp�cifi� dans REMINDER_METHOD, ce champ indique le nombre de minutes avant le d�but de l'�v�nement pour �mission du rappel.<br/>
					Si REMINDER_METHOD est vide, la valeur de ce champ n'est jamais lue.
					</span>
				<li>TYPE</li>
					<span class="explain">
					&#8658; Type de l'�v�nement, stock� dans les propri�t�s �tendues, indiquer ici l'alias de la table origine (ORFA, ACCO, etc.)
					</span>
				<li>KEY</li>
					<span class="explain">
					&#8658; En lien avec la colonne TYPE, cl� permettant d'identifier la source de l'�v�nement (num�ro d'OF, d'action, etc.)
					</span>
				<li>READ_ONLY</li>
					<span class="explain">
					&#8658; Indiquer O si �v�nement en lecture seule, N sinon, non fonctionnel pour l'instant sur l'interface Google Agenda.
					</span>										
			</ul>
		</p>
	</div>
</body>
</html>
