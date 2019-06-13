package pt.isel.ls.view.html;

import pt.isel.ls.mappers.html.MovieHtmlMapper;
import pt.isel.ls.mappers.html.TheaterHtmlMapper;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.mappers.html.CinemaHtmlMapper;
import pt.isel.ls.model.Movie;
import pt.isel.ls.result.CinemaResult;
import pt.isel.ls.result.Result;
import pt.isel.ls.view.html.util.Element;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static java.util.Arrays.*;


public class GetCinemasByIdHtmlView extends AbstractHtmlView {

    static final String PAGE_TITLE = "CINEMA ";


    private Element build(Result res) {
        CinemaResult result = (CinemaResult)res;
        Cinema cin = result.getCinema();

        Element page  = initPage(PAGE_TITLE + cin.name);
        Element body = new Element("body");


        CinemaHtmlMapper map = new CinemaHtmlMapper();
        Element cinema = map.single(cin);

        TheaterHtmlMapper tMap = new TheaterHtmlMapper();
        Element theaters = tMap.mapTheaterList(result.getTheaters());
        Element thtrs = new Element("p");
        thtrs.setContent("Theaters");
        thtrs.addElement(theaters);

        MovieHtmlMapper movMap = new MovieHtmlMapper();
        List<Movie> movList = result.getMovies();

        Element movs = new Element("p");

        if(movList.size() == 0)
        {
            movs.setContent("No Movies to show");
        }
        else {
            Element movies = movMap.mapList(movList);

            movs.setContent("Showing Movies");
            movs.addElement(movies);
        }
        //link element for all cinemas list page
        Element cinAll = new Element("p");
        Element coll = new Element("a", asList("href='/cinemas'"));
        coll.setContent("CINEMAS");
        cinAll.addElement(coll);

        String link = String.format("href='/cinemas/%d/sessions/today'", cin.cid);
        Element sLink = new Element("a", asList(link)); //today sessions link
        sLink.setContent("Today sessions");

        body.addElement(cinAll);   //add link for list of cinemas to body
        body.addElement(cinema);  //Add cinema to body
        body.addElement(thtrs);  //theaters table
        body.addElement(movs);    //movies table
        body.addElement(sLink);     //today sessions
        page.addElement(body);  //Add body to page

        return page;

    }

    @Override
    public void show(Result res, Writer writer) throws CommandIOException {
        try
        {
            build(res).writeTo(writer);
        }catch (IOException e)
        {
            throw new CommandIOException(
                    String.format("Error writing HTML page using write %s",writer)
            );
        }
    }

    @Override
    public HttpContent getContent(Result target) {
        return build(target);
    }
}
