package fr.axin.gservices.database.oracle;

public class OracleException extends Exception {
    private final static long serialVersionUID = 1;

    public OracleException(Throwable throwable) {
        super(throwable);
    }

    public OracleException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public OracleException(String string) {
        super(string);
    }

    public OracleException() {
        super();
    }
}
