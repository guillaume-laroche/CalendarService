package fr.axin.gservices;

import fr.axin.gservices.auth.GoogleAuth;
import fr.axin.gservices.database.oracle.OracleConnection;
import fr.axin.gservices.database.oracle.OracleException;
import fr.axin.gservices.database.oracle.OracleJob;
import fr.axin.gservices.gcal.Gcal;
import fr.axin.gservices.gcal.Gevent;
import fr.axin.gservices.servlet.html.HTMLPageFactory;
import fr.axin.gservices.util.DateUtils;
import fr.axin.gservices.util.LogUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/*
 * 0.4.1 17/02/2015 GL : gestion de la valeur NULL sur la description de l'évenement
 * 0.4   14/05/2014 GL : migration vers API v3 + authentification OAuth2
 */

public class GCalendarService {
    private final static String version = "version 0.4.1, 2015/02/17";
    final static String gName = "CalendarService";
    final static String applicationName = "Google Calendar Service";
    static String gUsername = "";
    static String gCode = "";
    static String gID = "";
    static boolean service = false;
    private static final int MAX_CANCEL_ATTEMPTS = 2;

    public static boolean isService() {
		return service;
	}

	public static void setService(boolean service) {
		GCalendarService.service = service;
	}

	public static String getVersion() {
        return version;
    }    

    public static String getApplicationName() {
        return applicationName;
    }

    public static void synchronizeOracle(String host, String port, String sid, String oraUsername, String oraPassword,
                                         String syncType, String sD, String eD, String id, String username, String code,
                                         String gid, String view, HTMLPageFactory out,
                                         PrintWriter log) throws SQLException, ClassNotFoundException,
                                                                 MalformedURLException,
                                                                 IOException, java.text.ParseException, InterruptedException,
                                                                 OracleException, Exception {  
        String gStartDateString = null;
        String gEndDateString = null;
        String identifier = null;

        String dateFormat = "dd/MM/yyyy";
        String startDateString = null;
        String endDateString = null;
        java.util.Date startDate = null;
        java.util.Date endDate = null;
        boolean cancel = false;
        int c = 1;
        int nEvents = 0;
        
        Gevent[] events;                
        OracleConnection conn;        

        if (syncType.toUpperCase().compareTo("SYSDATE") == 0) {
            startDate = DateUtils.addDaysToDate(new java.util.Date(), Integer.parseInt(sD) * (-1));
            endDate = DateUtils.addDaysToDate(new java.util.Date(), Integer.parseInt(eD));            
            startDateString = DateUtils.dateToString(startDate, new SimpleDateFormat(dateFormat));
            endDateString = DateUtils.dateToString(endDate, new SimpleDateFormat(dateFormat));
            identifier = id.toUpperCase();
        } else if (syncType.toUpperCase().compareTo("RANGE") == 0) {
            startDateString = sD;
            endDateString = eD;
            startDate = DateUtils.toDate(startDateString, new SimpleDateFormat(dateFormat));
            endDate = DateUtils.toDate(endDateString, new SimpleDateFormat(dateFormat));            
            identifier = id.toUpperCase();
        }

        gUsername = username;        
        gID = gid;
        gCode = code;
        
        gStartDateString = DateUtils.toGoogleDateString(startDateString, "00:00:00");
        gEndDateString = DateUtils.toGoogleDateString(endDateString, "23:59:00");   
                        
        try {
            LogUtils.log("Connexion base Oracle " + sid + " sur " + host + "...", out, log);
            conn = new OracleConnection(host, port, sid, oraUsername, oraPassword);
            conn.connect();
            LogUtils.log("Connexion OK", out, log);
            nEvents = conn.getEventsCount(view, identifier, startDateString, endDateString);
            events = new Gevent[nEvents];
            events = conn.getEvents(view, identifier, startDateString, endDateString, nEvents, out, log);
            LogUtils.log(conn.getNbEvents() + " evenements extraits de la base.", out, log);
            conn.disconnect();

            if(isService()) {
            	LogUtils.log("Mode service requis, connexion Google avec le certificat P12...", out, log);            	            	
            	GoogleAuth.setCredentialService();            	
            	GoogleAuth.createCalendarInstance();            	
            }
            
            LogUtils.log("Instanciation calendrier...", out, log);
            Gcal cal = new Gcal(gName, gUsername, gCode, gID, out, log);
            LogUtils.log("Instance OK pour agenda : " + gID, out, log);            
                        
            
            do {            	
            	LogUtils.log("Debut annulation evenements entre le " + startDateString + " et le " + endDateString + " (" + c + ")", out, log);            	
            	cancel = cal.cancelEvents(startDate, endDate);
            	c++;
            }
            while(!cancel && c <= MAX_CANCEL_ATTEMPTS);

            if (events != null) {
                LogUtils.log("\nDebut traitement evenements...", out, log);                
                for (int i = 0; i < conn.getNbEvents(); i++) {                	                	                	
                	if(!cal.eventExists(events[i]))
                		cal.addEvent(events[i]);
                	else
                		cal.updateEvent(events[i]);
                }                                                
            }
        } catch (OracleException oraE) {
            throw new Exception(oraE.getMessage());
        }

        System.gc();
    }

    public static void submitOracleJob(String host, String port, String sid, String oraUsername, String oraPassword,
                                       String syncType, String sD, String eD, String id, String username, String gid, String interval, String wHost, int wPort,
                                       HTMLPageFactory out, PrintWriter log) throws SQLException,
                                                                                    ClassNotFoundException,                                                                                    
                                                                                    MalformedURLException, IOException,                                                                                    
                                                                                    java.text.ParseException,
                                                                                    InterruptedException,
                                                                                    OracleException, Exception {
        String identifier = null;
        String dateFormat = "dd/MM/yyyy";
        String startDateString = null;
        String endDateString = null;
        java.util.Date startDate = null;
        java.util.Date endDate = null;
        OracleConnection conn;
        OracleJob job;
        String jobProcedure = null;

        if (syncType.toUpperCase().compareTo("SYSDATE") == 0) {
            startDate = DateUtils.addDaysToDate(new java.util.Date(), Integer.parseInt(sD) * (-1));
            endDate = DateUtils.addDaysToDate(new java.util.Date(), Integer.parseInt(eD));
            startDateString = DateUtils.dateToString(startDate, new SimpleDateFormat(dateFormat));
            endDateString = DateUtils.dateToString(endDate, new SimpleDateFormat(dateFormat));
            identifier = id.toUpperCase();
        } else if (syncType.toUpperCase().compareTo("RANGE") == 0) {
            startDateString = sD;
            endDateString = eD;
            startDate = DateUtils.toDate(startDateString, new SimpleDateFormat(dateFormat));
            endDate = DateUtils.toDate(endDateString, new SimpleDateFormat(dateFormat));
            identifier = id.toUpperCase();
        }

        gUsername = username;        
        gID = gid;        

        try {
        	LogUtils.log("Connexion base Oracle " + sid + " sur " + host + "...", out, log);
            conn = new OracleConnection(host, port, sid, oraUsername, oraPassword);
            conn.connect();
            LogUtils.log("Connexion OK", out, log);

            jobProcedure =
                      "/* CalendarService : " + identifier +" */"
            		+ "declare\n "
                    + "v_url varchar2(4000) := null;\n "
                    + "v_ret LONG;\n "
                    + "req utl_http.req;\n "
                    + "resp utl_http.resp;\n " +
                    "begin\n "
	                    + "v_url := ''" + "http://" + wHost + ":" + wPort + "/CalendarService/OracleServlet?host=" +
	                    host + "&port=" + port + "&sid=" + URLEncoder.encode(sid, "UTF-8") + "&dbuser=" +
	                    URLEncoder.encode(oraUsername, "UTF-8") + "&dbpwd=" + URLEncoder.encode(oraPassword, "UTF-8") +
	                    "&syncType=" + URLEncoder.encode(syncType, "UTF-8") + "&arg1=" + sD + "&arg2=" + eD +
	                    "&identifier=" + URLEncoder.encode(identifier, "UTF-8") + "&user=" +
	                    URLEncoder.encode(username, "UTF-8") + "&service=true&ID=" +
	                    URLEncoder.encode(gid, "UTF-8") + "''" + ";\n "
                    + "utl_http.set_transfer_timeout(300);\n "
                    + "req := utl_http.begin_request(v_url, ''POST'');\n "	                    
	                + "resp := utl_http.get_response(req);\n "
	                + "loop\n "
	                + "utl_http.read_line(resp, v_ret, true);\n "
	                + "end loop;\n "
	                + "utl_http.end_response(resp);\n "
	                + "exception when utl_http.end_of_body then utl_http.end_response(resp);\n"
                    + "end;";            

            job = new OracleJob(conn, DateUtils.getNextJobDate(), interval, jobProcedure);
            job.submit();
            LogUtils.log("Travail cree avec le numero " + job.getJobId(), out, log);
            LogUtils.log("Prochaine execution : " + DateUtils.getNextJobDate(), out, log);
            conn.getConn().commit();
        } catch (OracleException oraE) {
            throw new Exception(oraE.getMessage());

        }
    }

}
