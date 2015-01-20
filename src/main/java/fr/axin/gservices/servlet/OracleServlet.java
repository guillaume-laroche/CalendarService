package fr.axin.gservices.servlet;

import fr.axin.gservices.GCalendarService;
import fr.axin.gservices.auth.GoogleAuth;
import fr.axin.gservices.servlet.html.HTMLPageFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class OracleServlet extends HttpServlet {
    private final static long serialVersionUID = 1;
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";    

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                         IOException {
        if (request.getMethod().equals("GET") || request.getMethod().equals("POST")) {
            String dbuser = "";
            String dbpwd = "";
            String host = "";
            String port = "1521";
            String sid = "";
            String syncType = "";
            String arg1 = "";
            String arg2 = "";
            String identifier = "";            
            String gID = "";
            String gUser = "";
            String gCode = "";     
            String view = "";
            boolean service = false;
            PrintWriter logFile;            
            boolean parametersException = false;
            String logDirectory = request.getSession().getServletContext().getInitParameter("serverLogDirectory");            

            response.setContentType(CONTENT_TYPE);
            HTMLPageFactory webpage = new HTMLPageFactory(response.getWriter(), HTMLPageFactory.ORACLE_SERVICE);

            try {
                dbuser = request.getParameter("dbuser");
                dbpwd = request.getParameter("dbpwd");
                host = request.getParameter("host");
                port = request.getParameter("port");
                sid = request.getParameter("sid");
                syncType = request.getParameter("syncType");
                arg1 = request.getParameter("arg1");
                arg2 = request.getParameter("arg2");
                identifier = request.getParameter("identifier");
                gID = request.getParameter("ID");
                view = request.getSession().getServletContext().getInitParameter("exportViewName");
                if(request.getParameter("service").toLowerCase().equals("true"))
                	service = true;
                else
                	service = false;
                
                if(!service) {                	
                	gCode = GoogleAuth.getCode();
                	gUser = GoogleAuth.getEmail();
                }
                else {
                	String accountId = request.getSession().getServletContext().getInitParameter("serviceAccountEmail");
                	String p12File = request.getSession().getServletContext().getInitParameter("serviceAccountKeyFile");
                	p12File = getServletContext().getRealPath("/auth/"+ p12File);
                	GoogleAuth auth = new GoogleAuth(accountId, p12File);
                	gCode = null;
                	gUser = request.getParameter("user");
                	
                }

            } catch (Exception e) {            	
            	parametersException = true;                
            }

            if (!parametersException) {
                if (gUser.length() > 0) {
                    if (System.getProperty("os.name").toLowerCase().equals("linux")) {
                        logFile =
                                new PrintWriter(new BufferedWriter(new FileWriter(logDirectory + "/OracleServlet_" + gUser.toLowerCase() + "_" + gID.toLowerCase() + ".log")));
                    } else if (System.getProperty("os.name").substring(0, 7).toLowerCase().equals("windows")) {
                        logFile =
                                new PrintWriter(new BufferedWriter(new FileWriter(logDirectory + "\\OracleServlet_" + gUser.toLowerCase() + "_" + gID.toLowerCase() + ".log")));
                    } else {
                        logFile = new PrintWriter(System.out);
                    }
                } else {
                    logFile = new PrintWriter(System.out);
                }
                logFile.println("Date de creation du fichier journal : " + new java.util.Date());

                if (dbuser.length() == 0 || dbpwd.length() == 0 || host.length() == 0 || port.length() == 0 ||
                    sid.length() == 0 || syncType.length() == 0 || arg1.length() == 0 || arg2.length() == 0 ||
                    identifier.length() == 0 || gID.length() == 0) {
                    webpage.writeHeader();
                    webpage.writeService();                    
                    webpage.writeParametersError();
                    webpage.writeBodyAndPage();
                    webpage.close();
                    logFile.println("Erreur de parametres");
                    logFile.flush();
                    logFile.close();
                } else {
                    try {
                        webpage.writeHeader();
                        webpage.writeService();
                        webpage.write("<font face=\"Arial\" size=\"2\">");
                        GCalendarService.setService(service);
                        GCalendarService.synchronizeOracle(host, port, sid, dbuser, dbpwd, syncType, arg1, arg2,
                                                           identifier, gUser, gCode, gID, view, webpage, logFile);
                        webpage.writeSuccess();
                        webpage.write("</font>");
                        webpage.writeBodyAndPage();
                        webpage.close();

                        logFile.println("Synchronisation terminee avec succes");
                        logFile.println("Date : " + new java.util.Date());
                        logFile.flush();
                        logFile.close();

                    } 
                    catch (Exception e) {
                        webpage.writeHeader();
                        webpage.writeService();
                        webpage.writeSyncError(e);
                        webpage.writeBodyAndPage();
                        webpage.close();

                        logFile.println("Erreur de synchronisation");
                        logFile.println("Date : " + new java.util.Date());
                        logFile.println(e.getMessage());
                        logFile.flush();
                        logFile.close();
                    }
                }
            } else {            	
                webpage.writeHeader();
                webpage.writeService();                
                webpage.writeParametersError();
                webpage.writeBodyAndPage();                
                webpage.close();
            }
        }
    }
}
