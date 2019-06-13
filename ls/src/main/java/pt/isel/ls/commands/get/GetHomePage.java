package pt.isel.ls.commands.get;

import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.result.StringResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.util.HashMap;

public class GetHomePage extends Get {
    @Override
    public StringResult execute(HashMap parameters, SqlConnectionManager manager) throws CommandException {
        return new StringResult("Movies \n Cinemas");
    }

    @Override
    public String getDescription() {
        return null;
    }
}
