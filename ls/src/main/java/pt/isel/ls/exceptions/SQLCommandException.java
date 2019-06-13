package pt.isel.ls.exceptions;

public class SQLCommandException extends CommandException {
    public SQLCommandException(Throwable cause) {
        this("Error acessing database",cause);
    }

    public SQLCommandException(String message , Throwable cause){
        super(message , cause);
    }

    public SQLCommandException(String msg){
        super(msg);
    }
}
