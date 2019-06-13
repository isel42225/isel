package pt.isel.ls.view.html;

import pt.isel.ls.model.Movie;
import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.mappers.html.MovieHtmlMapper;
import pt.isel.ls.result.MovieListResult;
import pt.isel.ls.result.Result;
import pt.isel.ls.view.html.util.Element;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static java.util.Arrays.asList;

public class GetMoviesHtmlView extends AbstractHtmlView {

    private final String PAGE_TITLE = "LIST OF MOVIES";


    private Element build(Result res) {
        MovieListResult result = (MovieListResult)res;

        Element page = initPage(PAGE_TITLE);
        Element body = new Element("body");

        Element homePage = new Element("p");
        Element ref = new Element("a" , asList("href='/'"));
        ref.setContent("HOME PAGE");
        homePage.addElement(ref);
        body.addElement(homePage);

        MovieHtmlMapper mapper = new MovieHtmlMapper();
        List<Movie> movs = result.getMovies();

        Element movie = mapper.mapList(movs);
        body.addElement(movie);
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
