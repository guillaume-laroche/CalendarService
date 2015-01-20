package fr.axin.gservices.database.oracle;

import fr.axin.gservices.gcal.Gevent;
import fr.axin.gservices.servlet.html.HTMLPageFactory;

import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleDriver;


public class OracleConnection {

    final static int COMMIT = 1;
    final static int NO_COMMIT = 0;
    String host;
    String port;
    String sid;
    String user;
    String password;
    private static Connection conn;
    private static boolean connected;
    static int nbEvents = 0;

    public OracleConnection(String h, String p, String s, String u, String pwd) {
        this.host = h;
        this.port = p;
        this.sid = s;
        this.user = u;
        this.password = pwd;
        this.connected = false;
    }

    public void connect() throws OracleException {
        if (!connected) {
            try {
                DriverManager.registerDriver(new OracleDriver());
                conn =
DriverManager.getConnection("jdbc:oracle:thin:@" + this.host + ":" + this.port + ":" + this.sid, this.user,
                            this.password);
                connected = true;
            } catch (SQLException e) {
                throw new OracleException("Erreur de connexion à la base Oracle : " + e.getMessage());
            }
        } else {
            throw new OracleException("Déjà connecté");
        }
    }

    public void disconnect() throws OracleException {
        if (connected) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new OracleException("Erreur de déconnexion à la base Oracle : " + e.getMessage());
            }
        } else {
            throw new OracleException("Non connecté à Oracle");
        }
    }

    public void disconnect(int commitMode) throws OracleException {
        if (connected) {
            try {
                switch (commitMode) {
                case COMMIT:
                    conn.commit();
                    break;
                case NO_COMMIT:
                    conn.rollback();
                    break;
                }
                conn.close();
            } catch (SQLException e) {
                throw new OracleException("Erreur de déconnexion à la base Oracle : " + e.getMessage());
            }
        } else {
            throw new OracleException("Non connecté à Oracle");
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void execute(String sql) throws OracleException {
        if (connected) {
            try {
                conn.prepareCall(sql).execute();
            } catch (SQLException e) {
                throw new OracleException("Erreur lors de l'exécution de la requête : " + e.getMessage());
            }
        } else {
            throw new OracleException("Non connecté à Oracle");
        }
    }

    public static Gevent[] getEvents(String view, String octID, String startD, String endD, int nDays, HTMLPageFactory out,
                                     PrintWriter log) throws OracleException {
        String eventQuery;
        Gevent[] tabEvents = new Gevent[nDays];
        boolean allDay;
        int i = 0;        

        if (connected) {
            try {
                eventQuery =
                        "SELECT STARTDATE, ENDDATE, TITLE, DESCRIPTION, LOCATION, GUID, ALLDAY, COLOR, REMINDER_METHOD, REMINDER_MINUTES, TYPE, KEY, DDATE, READ_ONLY " 
                      + "FROM " + view + " " +
                        "WHERE EDATE BETWEEN TO_DATE('" + startD + "','DD/MM/YYYY') AND TO_DATE('" + endD +
                        "','DD/MM/YYYY') " + "AND IDENTIFIER = '" + octID + "'" + " ORDER BY DDATE";                
                PreparedStatement queryStatement = conn.prepareStatement(eventQuery);
                ResultSet events = queryStatement.executeQuery();
                while (events.next()) {
                	if(events.getString("ALLDAY").equals("O"))
                		allDay = true;
                	else
                		allDay = false;
                			
                    tabEvents[i] =
                            new Gevent(events.getString("TITLE"), events.getString("DESCRIPTION"), events.getString("STARTDATE"),
                                       events.getString("ENDDATE"), events.getString("LOCATION"),
                                       events.getString("GUID"), allDay, events.getString("COLOR"),
                                       events.getString("REMINDER_METHOD"), events.getInt("REMINDER_MINUTES"),
                                       events.getString("TYPE"), events.getString("KEY"), events.getString("DDATE"));
                    
                    if(events.getString("READ_ONLY").equals("O"))
                    	tabEvents[i].setReadOnly(true);
                    else
                    	tabEvents[i].setReadOnly(false);
                    	
                    i++;
                }
                nbEvents = i;
            } catch (Exception e) {
                throw new OracleException("Erreur lors de l'extraction des évènements : " + e.toString());
            }
        } else {
            throw new OracleException("Non connecté à Oracle");
        }
        return tabEvents;
    }

    public static int getNbEvents() {
        return nbEvents;
    }

    public List<OracleJob> getJobs() throws OracleException {
        List<OracleJob> jobs = new ArrayList<OracleJob>();
        ResultSet result = null;
        String sql = "SELECT JOB, LAST_DATE, NEXT_DATE, INTERVAL, WHAT FROM USER_JOBS WHERE INSTR(WHAT, 'CalendarService') > 0 ORDER BY JOB";
        int i = 0;
        if (connected) {
            try {
                result = conn.prepareStatement(sql).executeQuery();
                while (result.next()) {
                    jobs.add(i,
                             new OracleJob(this, result.getInt("JOB"), result.getString("LAST_DATE"), result.getString("NEXT_DATE"),
                                           result.getString("INTERVAL"), result.getString("WHAT"), true));
                }
            } catch (SQLException e) {
                throw new OracleException("Erreur lors de l'exécution de la requête : " + e.getMessage());
            }
        } else {
            throw new OracleException("Non connecté à Oracle");
        }
        return jobs;
    }

    public OracleJob getJob(int jobId) throws OracleException {
        OracleJob job = new OracleJob();
        ResultSet result = null;
        String sql =
            "SELECT JOB, LAST_DATE, NEXT_DATE, INTERVAL, WHAT FROM USER_JOBS WHERE JOB=" + jobId + "ORDER BY JOB";
        int i = 0;
        if (connected) {
            try {
                result = conn.prepareStatement(sql).executeQuery();
                while (result.next()) {
                    job.setJobId(result.getInt("JOB"));
                    job.setConn(this);
                    job.setInterval(result.getString("INTERVAL"));
                    job.setLastDate(result.getString("LAST_DATE"));
                    job.setNextDate(result.getString("NEXT_DATE"));
                    job.setWhat(result.getString("WHAT"));
                    job.setSubmitted(true);
                }
            } catch (SQLException e) {
                throw new OracleException("Erreur lors de l'exécution de la requête : " + e.getMessage());
            }
        } else {
            throw new OracleException("Non connecté à Oracle");
        }
        return job;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public static void setNbEvents(int nbEvents) {
        OracleConnection.nbEvents = nbEvents;
    }

    public static Connection getConn() {
        return conn;
    }

    public int getEventsCount(String view, String octID, String startD, String endD) throws OracleException {
        String countQuery = null;
        int n = 0;
        
    	if (connected) {
            try {                
                countQuery =
                        "SELECT COUNT(1) " 
                      + "FROM " + view + " " +
                        "WHERE EDATE BETWEEN TO_DATE('" + startD + "','DD/MM/YYYY') AND TO_DATE('" + endD +
                        "','DD/MM/YYYY') " + "AND IDENTIFIER = '" + octID + "'" + " ORDER BY DDATE";                
                PreparedStatement queryStatement = conn.prepareStatement(countQuery);
                ResultSet result = queryStatement.executeQuery();
                while (result.next()) {            	
                	n = result.getInt(1);
                }
            } catch (SQLException e) {
                throw new OracleException("Erreur lors de l'exécution de la requête : " + e.getMessage());
            }
        } else {
            throw new OracleException("Non connecté à Oracle");
        }
        return n;
    }
    
}
