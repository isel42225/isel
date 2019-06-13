package pt.isel.ls.view.html;

import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.result.Result;
import pt.isel.ls.view.html.util.Element;

import java.io.IOException;
import java.io.Writer;

import static java.util.Arrays.*;

public class ServerHomePageHtmlView extends AbstractHtmlView {
    private static final String PAGE_TITLE = "HOME";


    private Element build(){
        Element page  = initPage(PAGE_TITLE);
        Element body = new Element("body");
        Element movies = new Element("a", asList("href='/movies'"));
        movies.setContent("MOVIES");

        Element cinemas = new Element("a", asList("href='/cinemas'"));
        cinemas.setContent("CINEMAS");
        body.addElement(movies, cinemas);
        page.addElement(body);
        return page;
    }

    @Override
    public void show(Result target, Writer writer) throws CommandIOException
    {
        try
        {
            build().writeTo(writer);
        }catch (IOException e)
        {
            throw new CommandIOException(
                    String.format("Error writing HTML page using write %s",writer)
            );
        }
    }

    @Override
    public HttpContent getContent(Result target) {
        return build();
    }

}
