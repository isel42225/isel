package pt.isel.ls.app;

import pt.isel.ls.commands.*;
import pt.isel.ls.commands.get.*;
import pt.isel.ls.exceptions.CommandNotFoundException;
import pt.isel.ls.utils.*;
import pt.isel.ls.utils.Node;
import pt.isel.ls.utils.NodeParameter;

import java.util.HashMap;
import java.util.List;

public class CommandContainer {

    public  Node cmdtree;

    public CommandContainer() {
        cmdtree = new NodeLiteral("Root");
        populate();

    }

    public CommandContainer(List<Pair<String, Command>> commands){
        cmdtree = new NodeLiteral("Root");
        populateFrom(commands);
    }

    private void populateFrom(List<Pair<String, Command>> commands) {
        commands.forEach(this::add);
    }

    public CommandPair get (String method , String path) throws CommandNotFoundException{

        String [] s = (method + path).split("/");

        Node curr = cmdtree;
        HashMap<String , String> parameters = new HashMap<>();

        boolean isCommand = false;
        for(String str : s){
            isCommand = false;
            int count = 0;
            for(Node n : curr.children){

                if( n.compareTemplate(str)){
                    curr = curr.children.get(count);
                    isCommand = true;
                    break;
                }
                ++count;
            }

            //only if is parameter
            if(curr instanceof NodeParameter)
                parameters.put(curr.template , str);

        }

        //Lança excepção not found
        if( !isCommand ) throw new CommandNotFoundException();
        return new CommandPair(parameters, curr.cmd);
    }

    public void add (Pair<String , Command> pair ) {
        Node curr = cmdtree;

        for (String s : pair.key.split("/")) {
            boolean b = s.startsWith("{");
            Node n = b ? new NodeParameter(s) : new NodeLiteral(s);
            if(!curr.children.contains(n)) curr.children.add(n);    //Add new Node to trie
            curr = curr.children.get(curr.children.indexOf(n));     //Go to added or existing node
        }
        curr.cmd = pair.value;

    }

    public void printAll() {

        Node.print(cmdtree);
    }
    //23
    private void populate() {
        add(new Pair<>("GET/" ,
                new GetHomePage()));
        add(new Pair<>("GET/movies" ,
                new GetMovies()));
        add(new Pair<>("GET/movies/{mid}" ,
                new GetMoviesById()));
        add(new Pair<>("GET/movies/{mid}/sessions/date/{dmy}",
                new GetSessionsFromMoviesByIdFromDay()));
        add(new Pair<>("GET/cinemas" ,
                new GetCinemas()));
        add(new Pair<>("GET/cinemas/{cid}" ,
                new GetCinemasById()));
        add(new Pair<>("GET/cinemas/{cid}/theaters" ,
                new GetTheatersFromCinemasById()));
        add(new Pair<>("GET/cinemas/{cid}/theaters/{tid}" ,
                new GetTheatersByIdFromCinemasById()));
        add(new Pair<>("GET/cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets",
                new GetTicketsFromSessionsByIdFromTheatersByIdFromCinemasById()));
        add(new Pair<>("GET/cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/{tkid}",
                new GetTicketsByIdFromSessionsByIdFromTheatersByIdFromCinemasById()));
        add(new Pair<>("GET/cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/available",
                new GetTicketsAvailableFromSessionsByIdFromTheatersByIdFromCinemasById()));
        add(new Pair<>("GET/cinemas/{cid}/sessions" ,
                new GetSessionsFromCinemasById()));
        add(new Pair<>("GET/cinemas/{cid}/sessions/{sid}" ,
                new GetSessionsByIdFromCinemasById()));
        add(new Pair<>("GET/cinemas/{cid}/sessions/today" ,
                new GetTodaySessionsFromCinemaById()));
        add(new Pair<>("GET/cinemas/{cid}/theaters/{tid}/sessions" ,
                new GetSessionsFromTheaterByIdFromCinemaById()));
        add(new Pair<>("GET/cinemas/{cid}/theaters/{tid}/sessions/today" ,
                new GetTodaySessionsFromTheaterByIdFromCinemaById()));
        add(new Pair<>("GET/cinemas/{cid}/sessions/date/{dmy}",
                new GetSessionsFromCinemasByIdFromDay()));
        add(new Pair<>("POST/movies",
                new PostMovies()));
        add(new Pair<>("POST/cinemas",
                new PostCinemas()));
        add(new Pair<>("POST/cinemas/{cid}/theaters",
                new PostTheaters()));
        add(new Pair<>("POST/cinemas/{cid}/theaters/{tid}/sessions",
                new PostSessions()));
        add(new Pair<>("POST/cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets",
                new PostTickets()));
        add(new Pair<>("DELETE/cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets",
                new DeleteTickets()));



    }
}
