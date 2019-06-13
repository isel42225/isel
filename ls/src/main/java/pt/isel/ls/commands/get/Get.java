package pt.isel.ls.commands.get;

import pt.isel.ls.commands.Command;
import pt.isel.ls.result.Result;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.utils.SqlConnectionManager;

import java.util.HashMap;

public abstract class Get implements Command {

    public abstract  Result execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException;

    public abstract String getDescription();
}
