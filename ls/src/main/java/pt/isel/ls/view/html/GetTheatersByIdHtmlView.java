package pt.isel.ls.view.html;

import pt.isel.ls.mappers.html.SessionHtmlMapper;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.mappers.html.TheaterHtmlMapper;
import pt.isel.ls.result.Result;
import pt.isel.ls.result.TheaterResult;
import pt.isel.ls.view.html.util.Element;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static java.util.Arrays.asList;

public class GetTheatersByIdHtmlView extends AbstractHtmlView {
    static final String PAGE_TITLE = "THEATER ";

    private Element build(Result target) {
        TheaterResult res = (TheaterResult)target;
        Theater th = res.getTheater();
        Cinema cinema = res.getCinema();

        Element page  = initPage(PAGE_TITLE + th.name);
        Element body = new Element("body");

        TheaterHtmlMapper tmap = new TheaterHtmlMapper();
        Element theater = tmap.mapTheater(th);

        SessionHtmlMapper sMap = new SessionHtmlMapper();
        List<Session> sessList = res.getSessions();
        Element sess = new Element("p");

        if(sessList.size() == 0)
        {
            sess.setContent("No sessions in this theater");
        }
        else {
            Element sessions = sMap.list(sessList);

            sess.setContent("Sessions");
            sess.addElement(sessions);
        }
        Element cin = new Element("p");
        String link = String.format("href='/cinemas/%s/'",cinema.cid);
        Element cinLink = new Element("a", asList(link));   //link to "owner" cinema
        cinLink.setContent("Cinema :" + cinema.name);
        cin.addElement(cinLink);

        body.addElement(cin);
        body.addElement(theater);  //Add theater to body
        body.addElement(sess);
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
                    String.format("Error writing HTML page using write %s",writer)
            );
        }
    }

    @Override
    public HttpContent getContent(Result target) {
        return build(target);
    }
}
