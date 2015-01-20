package fr.axin.gservices.servlet.html;

import fr.axin.gservices.GCalendarService;

import java.io.PrintWriter;


public class HTMLPageFactory {

    public final static int NO_SERVICE = 0;
    public final static int ICAL_SERVICE = 1;
    public final static int ORACLE_SERVICE = 2;

    PrintWriter out;
    private int service;
    private boolean header;
    private boolean serv;

    public HTMLPageFactory(PrintWriter pw, int s) {
        out = pw;
        service = s;
        header = false;
        serv = false;
    }

    public void writeHeader() {
        if (!this.isHeader()) {
            out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
            out.write("<html>");
            out.write("<head>");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\"></meta>");
            out.write("</head>");
            out.write("<link rel=\"icon\" href=\"img/GoogleServices.ico\" type=\"image/x-icon\"/>");
            out.write("<body>");
            out.write("<p>");
            out.write("<font color=\"#000000\" size=\"7\" face=\"Arial\">");
            out.write("<font size=\"6\">");
            out.write("<a href=\"index.jsp\">");
            out.write("<img src=\"img/GoogleServices.jpg\" height=\"80\" width=\"80\"/>");
            out.write("</a>");
            out.write(GCalendarService.getApplicationName());
            out.write("</font>");
            out.write("</font>");
            out.write("</p>");
            out.write("<font size=\"2\" face=\"Arial\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                      GCalendarService.getVersion() + "&nbsp;&nbsp;&nbsp;</font>");
            out.write("<a href=\"logs\"><img src=\"img/logs.png\" height=\"20\" width=\"20\" title=\"View Google Services log files\"/></a> ");
            out.write("<hr></hr>");
            this.setHeader(true);
        }
    }

    public void writeService() {
        if (!this.isServ()) {
            if (service == ICAL_SERVICE) {
                out.write("<p>");
                out.write("<img src=\"img/ical.jpg\" height=\"20\" width=\"20\" alt=\"ical.jpg\"/>");
                out.write("&nbsp;<font face=\"Arial\">iCalendar Service</font>");
                out.write("</p>");
            } else if (service == ORACLE_SERVICE) {
                out.write("<p>");
                out.write("<img src=\"img/oracle.jpg\" height=\"20\" width=\"50\" alt=\"oracle.jpg\"/>");
                out.write("&nbsp;<font face=\"Arial\">Oracle Database Service</font>");
                out.write("</p>");
            } else if (service == NO_SERVICE) {
                out.write("<p>");
                out.write("<img src=\"img/GoogleCalendar.png\" height=\"20\" width=\"50\" alt=\"GoogleCalendar.png\"/>");
                out.write("&nbsp;<font face=\"Arial\">Google Calendar</font>");
                out.write("</p>");
            }
            this.setServ(true);
        }
    }

    public void writeParametersError() {
        if (service == ICAL_SERVICE) {
            out.write("<font face=\"Arial\" size=\"2\">");
            out.write("<p>" + "<img src=\"img/error.gif\" width=\"20\" height=\"20\"> &nbsp&nbsp" +
                      "Invalid parameters, following parameters are requested : " + "</p>");
            out.write("<p>file : URL of the iCalendar file</p>");
            out.write("<p>user : Google username</p>");
            out.write("<p>pwd : Google password</p>");
            out.write("<p>ID : Google Agenda ID</p>");
            out.write("<p>replace : Should I replace existing events? (yes | no)</p>");
            out.write("</font>");
        } else if (service == ORACLE_SERVICE) {
            out.write("<font face=\"Arial\" size=\"2\">");
            out.write("<p>" + "<img src=\"img/error.gif\" width=\"20\" height=\"20\"> &nbsp&nbsp" +
                      "Invalid parameters, following parameters are requested : </p>");
            out.write("<p>dbuser : Oracle database username</p>");
            out.write("<p>dbpwd : Oracle database password</p>");
            out.write("<p>host : Oracle host (name or IP address)</p>");
            out.write("<p>port : Oracle listener port</p>");
            out.write("<p>SID : Oracle SID</p>");
            out.write("<p>syncType : Synchronization type (SYSDATE or RANGE)</p>");
            out.write("<p>arg1 : 1st argument</p>");
            out.write("<p>arg2 : 2nd argument</p>");
            out.write("<p>identifier : Your initials</p>");
            out.write("<p>user : Google username</p>");            
            out.write("<p>ID : Google Agenda ID</p>");
            out.write("</font>");
        }
    }

    public void writeSyncError(Exception e) {
        out.write("<font face=\"Arial\" size=\"2\">");
        out.write("<p>" + "<img src=\"img/error.gif\" width=\"20\" height=\"20\"> &nbsp&nbsp" +
                  "Erreur de synchronisation</p>");
        out.write("<p>" + e.getMessage() + "</p>");
        out.write("</font>");
    }

    public void writeSuccess() {
        out.write("<p>" + "<img src=\"img/ok.png\" width=\"20\" height=\"20\"> &nbsp&nbsp" +
                  "Opération terminée avec succès</p>");
    }

    public void write(String line) {
        out.write(line);
    }

    public void write(int i) {
        out.write(i);
    }

    public void writeBodyAndPage() {
        out.write("</body>");
        out.write("</html>");
    }

    public void clear() {
        out.flush();
    }

    public void close() {
        out.flush();
        out.close();
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getService() {
        return service;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public boolean isHeader() {
        return header;
    }

    public void setServ(boolean serv) {
        this.serv = serv;
    }

    public boolean isServ() {
        return serv;
    }
}
