package pt.isel.ls.commands;

import pt.isel.ls.result.Result;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.utils.SqlConnectionManager;

import java.util.HashMap;


public interface Command  {

    Result execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException;

    String getDescription();
}
