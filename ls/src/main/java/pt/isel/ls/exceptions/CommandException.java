package pt.isel.ls.exceptions;

public class CommandException extends RuntimeException {

    public CommandException(String message , Throwable cause) {
        super(message, cause);
    }

    public CommandException(String msg){
        super(msg);
    }
}
