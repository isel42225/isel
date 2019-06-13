package pt.isel.ls.app;

import pt.isel.ls.exceptions.CommandException;

public class App {

    public static void main(String[] args)  {

        try {
            Commander commander = new Commander();
            commander.run(args);
        }catch (CommandException e){
            System.out.println(e.getMessage());
        }

    }






}
