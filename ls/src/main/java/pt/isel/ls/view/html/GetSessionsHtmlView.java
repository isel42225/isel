package pt.isel.ls.view.html;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Session;
import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.mappers.html.SessionHtmlMapper;
import pt.isel.ls.result.Result;
import pt.isel.ls.result.SessionListFromCinemaResult;
import pt.isel.ls.result.SessionListResult;
import pt.isel.ls.view.html.util.Element;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;

public class GetSessionsHtmlView extends AbstractHtmlView {

    private static final String PAGE_TITLE = "List of Sessions ";

    private Element build(Result target)
    {
        SessionListFromCinemaResult res = (SessionListFromCinemaResult)target;
        Element page  = initPage(PAGE_TITLE);
        Element body = new Element("body");

        SessionHtmlMapper map = new SessionHtmlMapper();
        List<Session> list = res.getSessions();
        Cinema cinema = res.getCinema();

        //no sessions this day
        if(list.size() == 0)
        {
            Element msg = new Element("p");
            msg.setContent("No sessions today");

            Element home = new Element("p");
            Element homeLink = new Element("a",
                    asList(("href='/'")));
            homeLink.setContent("HOME PAGE");
            home.addElement(homeLink);

            body.addElement(home);
            body.addElement(msg);
            page.addElement(body);
            return page;
        }

        Session first = list.get(0);
        int cid = first.cid;
        LocalDate tmrw = first.localDate.plusDays(1);
        LocalDate yst = first.localDate.minusDays(1);
        Element sessList = map.list(list);

        Element nextDay = new Element("p");
        Element nextLink = new Element("a",
                asList(String.format("href='/cinemas/%d/sessions/date/%s'",cid,tmrw))); //link for tomorrow sessions
        nextLink.setContent("Tommorrow Sessions");
        nextDay.addElement(nextLink);

        Element prevDay = new Element("p");
        Element prevLink = new Element("a",
                asList(String.format("href='/cinemas/%d/sessions/date/%s'",cid,yst))); //link to yesterday sessions
        prevLink.setContent("Yesterday Sessions");
        prevDay.addElement(prevLink);

        Element cin = new Element("p");
        Element cinLink = new Element("a" , asList(String.format("href='/cinemas/%d/'", first.cid)));
        cinLink.setContent("Cinema : " + cinema.name);
        cin.addElement(cinLink);

        body.addElement(cin);
        body.addElement(nextDay, prevDay);
        body.addElement(sessList);  //Add cinema list to body
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
