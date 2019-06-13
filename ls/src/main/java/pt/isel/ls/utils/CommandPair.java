package pt.isel.ls.utils;

import pt.isel.ls.commands.Command;

import java.util.HashMap;

public class CommandPair {
    private final Pair<HashMap<String, String>, Command> pair;

    public CommandPair(HashMap <String, String> params, Command cmd) {
        this.pair = new Pair<>(params, cmd);
    }

    public HashMap<String,String> getParameters()
    {
        return pair.key;
    }

    public Command getCommand()
    {
        return pair.value;
    }
}
