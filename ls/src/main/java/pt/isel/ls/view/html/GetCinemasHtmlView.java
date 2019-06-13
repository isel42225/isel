package pt.isel.ls.view.html;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.mappers.html.CinemaHtmlMapper;
import pt.isel.ls.result.CinemaListResult;
import pt.isel.ls.result.Result;
import pt.isel.ls.view.html.util.Element;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static java.util.Arrays.asList;

public class GetCinemasHtmlView  extends AbstractHtmlView{

    private static final String PAGE_TITLE = "LIST OF CINEMAS";

    private Element build(Result res){
        CinemaListResult result = (CinemaListResult)res;

        Element homePage = new Element("p");
        Element ref = new Element("a" , asList("href='/'"));
        ref.setContent("HOME PAGE");
        homePage.addElement(ref);

        Element page  = initPage(PAGE_TITLE);
        Element body = new Element("body");

        CinemaHtmlMapper map = new CinemaHtmlMapper();
        List<Cinema> list = result.getCinemas();
        Element cinemaList = map.list(list);


        body.addElement(homePage);
        body.addElement(cinemaList);  //Add cinema list to body
        page.addElement(body);  //Add body to page

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
                    String.format("Error writing HTML page using writer %s",writer)
            );
        }
    }

    @Override
    public HttpContent getContent(Result target) {
        return build(target);
    }
}
