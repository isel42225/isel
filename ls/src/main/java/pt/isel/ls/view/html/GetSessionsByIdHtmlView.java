package pt.isel.ls.view.html;

import pt.isel.ls.mappers.html.TicketHtmlMapper;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.mappers.html.SessionHtmlMapper;
import pt.isel.ls.result.Result;
import pt.isel.ls.result.SessionResult;
import pt.isel.ls.view.html.util.Element;

import java.io.IOException;
import java.io.Writer;

import static java.util.Arrays.asList;

public class GetSessionsByIdHtmlView extends AbstractHtmlView {

    private static final String PAGE_TITLE = "Session ";

    private Element build(Result target)
    {
        SessionResult res = (SessionResult)target;
        Session sess = res.getSession();
        Movie movie = res.getMov();
        Theater th = res.getTheater();
        Element page = initPage(PAGE_TITLE);
        Element body = new Element("body");

        SessionHtmlMapper mapper = new SessionHtmlMapper();

        Element session = mapper.single(sess);

        Element mov = new Element("p");
        mov.setContent("Showing Movie :");
        Element movLink = new Element("a",asList(String.format("href='/movies/%d'",sess.mid)));
        movLink.setContent( movie.title);
        mov.addElement(movLink);

        Element theater = new Element("p");
        theater.setContent("Theater : " );
        Element thLink = new Element("a", asList(String.format("href='/cinemas/%d/theaters/%d'",sess.cid, sess.tid)));
        thLink.setContent( th.name);
        theater.addElement(thLink);

        Element today = new Element("p");
        Element todayLink = new Element("a", asList(String.format("href='/cinemas/%d/sessions/today'",sess.cid)));
        todayLink.setContent("Other sessions today");
        today.addElement(todayLink);

        Element tk = new Element("p");
        tk.setContent("Tickets bought ");

        TicketHtmlMapper tkMap = new TicketHtmlMapper();
        Element tkList = tkMap.mapTicketsList(res.getTickets(),sess.tid,sess.cid);
        tk.addElement(tkList);

        body.addElement(session);
        body.addElement(mov);
        body.addElement(theater);
        body.addElement(tk);
        body.addElement(today);

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
