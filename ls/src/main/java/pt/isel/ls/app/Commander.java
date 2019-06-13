package pt.isel.ls.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import pt.isel.ls.commands.Command;
import pt.isel.ls.exceptions.*;
import pt.isel.ls.app.servlet.ComandServlet;
import pt.isel.ls.result.Result;
import pt.isel.ls.result.VoidResult;
import pt.isel.ls.utils.*;
import pt.isel.ls.view.View;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Commander {

    private final List<Pair<String , Command>> internalCommands =
            Arrays.asList(
                    new Pair<>("OPTION", new Option()),
                    new Pair<>("EXIT", new Exit()),
                    new Pair<>("LISTEN", new Listen())
            );

    private final CommandContainer container = new CommandContainer();
    private final CommandContainer internalContainer = new CommandContainer(internalCommands);
    private CommandSplitter splitter ;
    private HeaderProcessor headerProcessor;
    private ViewRouter vr ;
    private boolean terminated = false;
    private SqlConnectionManager conManager ;
    private Server server = new Server(8080);

     private class Option implements Command {
        private final String description = "Prints Detailed information about each Command";

        @Override
        public VoidResult execute(HashMap parameters, SqlConnectionManager manager) throws CommandException {
            container.printAll();
            return VoidResult.empty();
        }


        public String getDescription() {
            return description;
        }
    }
     private class Exit implements Command{

        final String description = "Ends the Application ";
        @Override
        public VoidResult execute(HashMap parameters, SqlConnectionManager manager) throws CommandException {
            try {
                if(server != null) server.stop();
                terminated = true;
                manager.close();
            }catch ( Exception e){
                throw new CommandException(e.getMessage());
            }
            return VoidResult.empty() ;
        }

         @Override
         public String getDescription() {
             return description;
         }
     }

     private class Listen implements Command
     {
         @Override
         public VoidResult execute(HashMap parameters, SqlConnectionManager manager) throws CommandException {
             try {
                 ServletHandler handler = new ServletHandler();  //Atende os pedidos
                 server.setHandler(handler);
                 handler.addServletWithMapping(new ServletHolder(new ComandServlet()), "/*");
                 server.start();
                 System.out.println("Server started");
                 return VoidResult.empty();
             }catch (Exception e){
                 throw new RuntimeException(e);
             }
         }

         @Override
         public String getDescription() {
             return null;
         }
     }

    public Commander() {
        vr = new ViewRouter();
        conManager = new SqlConnectionManager();
    }



    public void run(String[] args) throws CommandException{

        if(args.length == 0)
            runLoop();

        else {
            runOnce(args);
        }
    }

    /**
     * Processes the command parameters and headers arguments
     */
    private void initCommandProcessing(String[] args) throws  CommandNotFoundException {
        if (args.length < 2) throw new CommandNotFoundException();
        splitter = new CommandSplitter(args);
        headerProcessor = new HeaderProcessor();

        if(splitter.headers != null) {
            headerProcessor.splitHeader(splitter.headers);
        }
    }

    /**
     * One time Run
     * @throws CommandException
     */
    private void runOnce(String[] args) throws CommandException {
        initCommandProcessing(args);


        CommandPair cmdPair =
                commandGet(splitter.method, splitter.path);
        Result result = executeCommand(cmdPair);

        if (splitter.method.equalsIgnoreCase("GET")) {
            String view = headerProcessor.getView();
            View v = getView(cmdPair, view);
            showCommand(v, result);
        }

        //Call Exit command for safe program termination
        internalExit();
    }

    private void internalExit() throws CommandException {
        Pair<String , Command > pair = internalCommands.get(1);
        pair.value.execute(null , null);
    }

    /**
     * Run in loop
     * @throws CommandException
     */
    private void runLoop(){
        Scanner scn = new Scanner(System.in);

        String line;

        while(!terminated){
           try {
               System.out.println("Write a command:");
               line = scn.nextLine();

               initCommandProcessing(line.split(" "));

               CommandPair cmdPair =
                       commandGet(splitter.method, splitter.path);
               Result result = executeCommand(cmdPair);

                if (splitter.method.equalsIgnoreCase("GET")) {
                    String view = headerProcessor.getView();
                    View v = getView(cmdPair, view);
                    showCommand(v, result);
               }

           }catch (CommandException e)
           {
               System.out.println(e.getMessage());
           }
        }
    }

    public View getView(CommandPair cmdPair, String view) {
        return vr.getView(cmdPair.getCommand(), view);
    }


    /**
     * Search over both containers and returns a command
     * @return The command Pair
     * @throws CommandNotFoundException
     */
    public CommandPair commandGet (String method , String path) throws CommandNotFoundException {
        if(isInternalCommand(method)) return internalContainer.get(method, path);
        return container.get(method, path);
    }

    /**
     * Executes Command
     * @return Command result
     * @throws CommandException
     */
    public Result executeCommand(CommandPair pair ) throws CommandException {

        HashMap<String, String> parameters = pair.getParameters();

        if (splitter != null && splitter.parameters != null)
            splitter.splitAndPut(splitter.parameters, parameters);


        return pair.getCommand().execute(parameters, conManager);
    }
    /**
     * Shows command execute result represented in <view>
     * @param view representation
     * @param res object to view
     * @throws CommandShowException
     */
    private void showCommand(View view , Result res) throws CommandShowException {
        String out = headerProcessor.getOut();
        boolean isFile = false;
        try {
            OutputStreamWriter writer = new OutputStreamWriter(System.out);
            if (out != null)
            {
                writer = new FileWriter(out);
                isFile = true;
            }

            view.show(res, writer);  //write to OutputStream

            //if output is a file close the fileWriter
            if(isFile) writer.close();

        }catch (CommandIOException | IOException e){
            throw new CommandShowException("Error in command output");
        }
    }

    /**
     * @return command is Internal (Option , Exit, Listen)
     */
    private boolean isInternalCommand(String method)  {
            for(Pair<String , Command> p : internalCommands)
                if(p.key.equalsIgnoreCase(method))
                    return true;

            return false;
    }


}
