package pt.isel.ls.view.html;

import pt.isel.ls.model.Ticket;
import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.mappers.html.TicketHtmlMapper;
import pt.isel.ls.result.Result;
import pt.isel.ls.result.TicketListResult;
import pt.isel.ls.view.html.util.Element;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class GetTicketsHtmlView extends AbstractHtmlView {

    private final String PAGE_TITLE = "LIST OF TICKETS";


    public Element build(Result target) {
        TicketListResult res = (TicketListResult)target;
        Element page = initPage(PAGE_TITLE);
        Element body = new Element("body");

        TicketHtmlMapper mapper = new TicketHtmlMapper();
        List<Ticket> tk = res.getTickets();
        Element ticket = mapper.mapTicketsList(tk,res.getCinema().cid, res.getTheater().tid);
        body.addElement(ticket);
        page.addElement(body);
        return page;
    }

    @Override
    public void show(Result target, Writer writer) throws CommandIOException {
        try {
            build(target).writeTo(writer);
        } catch (IOException e) {
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
