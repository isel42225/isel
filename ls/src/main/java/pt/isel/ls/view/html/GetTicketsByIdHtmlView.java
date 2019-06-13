package pt.isel.ls.view.html;

import pt.isel.ls.model.Session;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.mappers.html.TicketHtmlMapper;
import pt.isel.ls.result.Result;
import pt.isel.ls.result.TicketResult;
import pt.isel.ls.view.html.util.Element;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import static java.util.Arrays.asList;

public class GetTicketsByIdHtmlView extends AbstractHtmlView {

    private final String PAGE_TITLE = "Ticket ";

    private Element build (Result target)
    {
        TicketResult res = (TicketResult)target;
        Ticket tk = res.getTicket();
        Session s = res.getSession();

        Element page = initPage(PAGE_TITLE + tk.rowLetter + tk.seatNumber);
        Element body = new Element("body");

        TicketHtmlMapper mapper = new TicketHtmlMapper();
        Element ticket = mapper.mapTickets(tk);


        Element session = new Element("p");
        Element sessLink = new Element("a",
                asList(String.format(
                        "href='/cinemas/%d/theater/%d/sessions/%d'",s.cid,s.tid , s.sid)));
        sessLink.setContent("Session :" + s.localTime);
        session.addElement(sessLink);

        body.addElement(session);
        body.addElement(ticket);
        page.addElement(body);

        return page;
    }


    @Override
    public void show(Result target, Writer writer) throws CommandIOException {
        try
        {
            build(target).writeTo(writer);
        }catch (IOException e) {
            throw new CommandIOException(
                    String.format("Error writing HTML page using write %s", writer)
            );
        }
    }

    @Override
    public HttpContent getContent(Result target) {
        return build(target);
    }
}
