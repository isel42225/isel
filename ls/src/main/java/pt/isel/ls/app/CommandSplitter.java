package pt.isel.ls.app;

import java.util.HashMap;

public class CommandSplitter {
    String method;
    String path;
    String parameters;
    String headers;

    public CommandSplitter(String [] args ) {

           method = args[0];
           path = args[1];
           if (args.length == 4) {
               parameters = args[2];
               headers = args[3];
           } else if (args.length > 2) chooseHeaderOrParameter(args[2]);

    }

    private void chooseHeaderOrParameter(String arg) {

        if (arg.contains("=")) {
            parameters = arg;
        }
        else if (arg.contains(":"))
            headers = arg;


    }

    //Ideia de splitter Geral
    void splitAndPut(String params,
                     HashMap<String, String> map) {
        for (String s : params.split("&")) {
            String[] arr = s.split("=");
            map.put(arr[0], arr[1].replaceAll("\\+", " "));

        }
    }
}