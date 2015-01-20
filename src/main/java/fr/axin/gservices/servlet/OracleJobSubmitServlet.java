package fr.axin.gservices.servlet;

import fr.axin.gservices.GCalendarService;
import fr.axin.gservices.servlet.html.HTMLPageFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class OracleJobSubmitServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    private static final long serialVersionUID = 726525363319982152L;

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
            String gUser = "";            
            String gID = "";
            String interval = "";
            PrintWriter logFile;
            boolean parametersException = false;
            String logDirectory = request.getSession().getServletContext().getInitParameter("serverLogDirectory");
            String ipAddress = request.getSession().getServletContext().getInitParameter("serverIpAddress");

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
                gUser = request.getParameter("user");                
                gID = request.getParameter("ID");
                interval = request.getParameter("syncInterval");
            } catch (Exception e) {
                parametersException = true;
            }

            if (!parametersException) {
                if (gUser.length() > 0) {
                    if (System.getProperty("os.name").toLowerCase().equals("linux")) {
                        logFile =
                                new PrintWriter(new BufferedWriter(new FileWriter(logDirectory + "/OracleJobSubmitServlet_" +
                                                                                  gUser.toLowerCase() + "_" +
                                                                                  gID.toLowerCase() + ".log")));
                    } else if (System.getProperty("os.name").substring(0, 7).toLowerCase().equals("windows")) {
                        logFile =
                                new PrintWriter(new BufferedWriter(new FileWriter(logDirectory + "\\OracleJobSubmitServlet_" +
                                                                                  gUser.toLowerCase() + "_" +
                                                                                  gID.toLowerCase() + ".log")));
                    } else {
                        logFile = new PrintWriter(System.out);
                    }
                } else {
                    logFile = new PrintWriter(System.out);
                }
                logFile.println("Date de creation du fichier journal : " + new java.util.Date());

                if (dbuser.length() == 0 || dbpwd.length() == 0 || host.length() == 0 || port.length() == 0 ||
                    sid.length() == 0 || syncType.length() == 0 || arg1.length() == 0 || arg2.length() == 0 ||
                    identifier.length() == 0 || gUser.length() == 0 || gID.length() == 0 ||
                    interval.length() == 0) {
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
                        GCalendarService.submitOracleJob(host, port, sid, dbuser, dbpwd, syncType, arg1, arg2,
                                                         identifier, gUser, gID, interval,
                                                         ipAddress, request.getServerPort(), webpage,
                                                         logFile);
                        webpage.writeSuccess();
                        webpage.write("</font>");
                        webpage.writeBodyAndPage();
                        webpage.close();

                        logFile.flush();
                        logFile.close();

                    } catch (Exception e) {
                        webpage.writeHeader();
                        webpage.writeService();
                        webpage.writeSyncError(e);
                        webpage.writeBodyAndPage();
                        webpage.close();

                        logFile.println("Erreur de soumission du travail");
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
