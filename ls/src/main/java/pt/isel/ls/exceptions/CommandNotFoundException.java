package pt.isel.ls.exceptions;

public class CommandNotFoundException extends CommandException {
    public CommandNotFoundException() {
        super("Command was not found");
    }
}
