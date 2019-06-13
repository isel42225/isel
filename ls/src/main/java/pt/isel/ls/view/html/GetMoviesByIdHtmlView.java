package pt.isel.ls.view.html;

import pt.isel.ls.mappers.html.CinemaHtmlMapper;
import pt.isel.ls.model.Movie;
import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.mappers.html.MovieHtmlMapper;
import pt.isel.ls.result.MovieResult;
import pt.isel.ls.result.Result;
import pt.isel.ls.view.html.util.Element;

import java.io.IOException;
import java.io.Writer;

import static java.util.Arrays.asList;

public class GetMoviesByIdHtmlView extends AbstractHtmlView {

    static final String PAGE_TITLE = "MOVIE ";


    private Element build(Result res) {
        MovieResult result = (MovieResult)res;
        Movie mov = result.getMov();

        Element page = initPage(PAGE_TITLE + mov.title );
        Element body = new Element("body");

        MovieHtmlMapper map = new MovieHtmlMapper();
        Element movie = map.map(mov);

        CinemaHtmlMapper cinMap = new CinemaHtmlMapper();
        Element cinemas = cinMap.list(result.getCinemas());
        Element cins = new Element("p");
        cins.setContent("Cinemas Showing");
        cins.addElement(cinemas);

        Element movAll = new Element("p");
        String link = String.format("href='/movies'");
        Element sLink = new Element("a", asList(link)); //today sessions link
        sLink.setContent("MOVIES");
        movAll.addElement(sLink);


        body.addElement(movAll);

        body.addElement(movie);
        body.addElement(cins); //cinemas showing the movie
        page.addElement(body);

        return page;
    }

    @Override
    public void show(Result target, Writer writer) throws CommandIOException {
        try
        {
            build(target).writeTo(writer);
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
