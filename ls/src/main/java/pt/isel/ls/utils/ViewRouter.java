package pt.isel.ls.utils;

import pt.isel.ls.commands.Command;
import pt.isel.ls.commands.get.*;
import pt.isel.ls.commands.get.GetTicketsFromSessionsByIdFromTheatersByIdFromCinemasById;
import pt.isel.ls.view.View;
import pt.isel.ls.view.html.*;
import pt.isel.ls.view.json.JsonView;
import pt.isel.ls.view.html.GetTicketsHtmlView;
import pt.isel.ls.view.plain.PlainView;

import java.util.HashMap;
import java.util.Map;

public class ViewRouter {

    private static class ViewPair
    {
        Pair<String, Class> pair;

         ViewPair(String name , Class c)
        {
            pair = new Pair<>(name ,c);
        }

        @Override
        public String toString() {
            return pair.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) return false;
            if(!(obj instanceof ViewPair)) return false;
            ViewPair p = (ViewPair) obj;
            return pair.key.equals(p.pair.key) && pair.value.equals(p.pair.value);
        }

        @Override
        public int hashCode() {
            return pair.hashCode();
        }
    }

    static Map<ViewPair , View> viewMap = new HashMap<>();

    static final String PLAIN_VIEW_TEMPLATE = "text/plain";
    static final String JSON_VIEW_TEMPLATE = "application/json";
    static final String DEFAULT_VIEW_TEMPLATE = "text/html";

    static {
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE,GetTicketsFromSessionsByIdFromTheatersByIdFromCinemasById.class), new GetTicketsHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE,GetCinemas.class), new GetCinemasHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE,GetCinemas.class), new GetCinemasHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE,GetCinemasById.class), new GetCinemasByIdHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE,GetMovies.class), new GetMoviesHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE,GetMoviesById.class), new GetMoviesByIdHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE, GetTheatersByIdFromCinemasById.class), new GetTheatersByIdHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE, GetSessionsByIdFromCinemasById.class), new GetSessionsByIdHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE, GetSessionsFromCinemasByIdFromDay.class), new GetSessionsHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE, GetSessionsFromCinemasById.class), new GetSessionsHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE, GetSessionsFromMoviesByIdFromDay.class), new GetSessionsHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE, GetSessionsFromTheaterByIdFromCinemaById.class), new GetSessionsHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE, GetTodaySessionsFromTheaterByIdFromCinemaById.class), new GetSessionsHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE, GetTodaySessionsFromCinemaById.class), new GetSessionsHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE, GetTicketsByIdFromSessionsByIdFromTheatersByIdFromCinemasById.class), new GetTicketsByIdHtmlView());
        viewMap.put(
                new ViewPair(DEFAULT_VIEW_TEMPLATE, GetHomePage.class), new ServerHomePageHtmlView());



    }


    public ViewRouter() {

    }

    //get view instance
    public View getView(Command cmd, String view){
        if(view.equals(JSON_VIEW_TEMPLATE)) return new JsonView();
        if(view.equals(PLAIN_VIEW_TEMPLATE))return new PlainView();
        return viewMap.get(new ViewPair(view,cmd.getClass()));

    }




}
