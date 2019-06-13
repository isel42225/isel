package pt.isel.ls.app.servlet;

import pt.isel.ls.app.Commander;
import pt.isel.ls.commands.Command;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.exceptions.CommandNotFoundException;
import pt.isel.ls.http.HttpResponse;
import pt.isel.ls.http.HttpStatusCode;
import pt.isel.ls.result.Result;
import pt.isel.ls.utils.CommandPair;
import pt.isel.ls.utils.Pair;
import pt.isel.ls.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

public class ComandServlet extends HttpServlet {

    private final static String ACCEPT_HEADER = "Accept";
    private final Commander commander = new Commander();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            //resp.setContentType(String.format("%s; charset=%s",header, _charset));
            HttpResponse hResp = resolveRequest(req);
            hResp.send(resp);
    }

    private HttpResponse resolveRequest(HttpServletRequest req) {
        String method = req.getMethod();
        String path = req.getRequestURI();
        String view = req.getHeader(ACCEPT_HEADER).split(",")[0]; //FIRST header

        if(!method.equalsIgnoreCase("GET"))
            return new HttpResponse(HttpStatusCode.BadRequest);

        CommandPair pair;
        try {
            pair = commander.commandGet(method, path);
        }catch (CommandNotFoundException e)
        {
            return new HttpResponse(HttpStatusCode.NotFound);
        }
        Result res;
        try{
            res = commander.executeCommand(pair);
        }catch (CommandException  e)
        {
            return new HttpResponse(HttpStatusCode.InternalServerError);
        }

        View v = commander.getView(pair, view);
        return new HttpResponse(HttpStatusCode.Ok , v.getContent(res)) ;

    }


}
