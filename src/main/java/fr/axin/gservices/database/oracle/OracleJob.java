package fr.axin.gservices.database.oracle;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class OracleJob {
    private int jobId;
    private String lastDate;
    private String nextDate;
    private String interval;
    private String what;
    private boolean submitted;
    private OracleConnection ora;

    public OracleJob() {
        this.jobId = 0;
        this.lastDate = null;
        this.nextDate = null;
        this.interval = null;
        this.what = null;
        this.ora = null;
        this.submitted = false;
    }

    public OracleJob(OracleConnection c, String nD, String i, String w) {
        this.nextDate = nD;
        this.interval = i;
        this.what = w;
        this.ora = c;
        this.submitted = false;
    }

    public OracleJob(OracleConnection c, int j, String lD, String nD, String i, String w, boolean s) {
        this.jobId = j;
        this.lastDate = lD;
        this.nextDate = nD;
        this.interval = i;
        this.what = w;
        this.ora = c;
        this.submitted = s;
    }

    public int getJobId() {
        return jobId;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getInterval() {
        return interval;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWhat() {
        return what;
    }

    public void setConn(OracleConnection conn) {
        this.ora = conn;
    }

    public OracleConnection getConn() {
        return ora;
    }

    public void submit() throws OracleException {
        String sql =
            "BEGIN DBMS_JOB.SUBMIT(job=>?,what=>'" + what + "',next_date => to_date('" + nextDate + "','dd/mm/yyyy hh24:mi:ss'),interval =>'sysdate + " +
            interval + "/1440',no_parse=>TRUE); END;";
        if (this.ora.isConnected()) {
            try {
                CallableStatement procStatement = this.ora.getConn().prepareCall(sql);
                procStatement.registerOutParameter(1, Types.NUMERIC);
                procStatement.execute();
                this.setJobId(procStatement.getInt(1));
                this.setSubmitted(true);
            } catch (SQLException e) {
                throw new OracleException("Error while executing statement : " + e.getMessage());
            }
        } else {
            throw new OracleException("Not connected to Oracle");
        }
    }

    public void drop() throws OracleException {
        String sql = "BEGIN DBMS_JOB.REMOVE(" + this.getJobId() + "); END;";

        if (this.ora.isConnected()) {
            try {
                CallableStatement procStatement = this.ora.getConn().prepareCall(sql);
                procStatement.execute();
                this.setJobId(0);
                this.setSubmitted(false);
            } catch (SQLException e) {
                throw new OracleException("Error while executing statement : " + e.getMessage());
            }
        } else {
            throw new OracleException("Not connected to Oracle");
        }
    }

    public void setJobId(int job) {
        this.jobId = job;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public boolean isSubmitted() {
        return submitted;
    }
}
