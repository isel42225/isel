package pt.isel.ls.exceptions;

public class SQLConnectionException extends SQLCommandException {
    public SQLConnectionException(String msg) {
        super(msg);
    }
}
