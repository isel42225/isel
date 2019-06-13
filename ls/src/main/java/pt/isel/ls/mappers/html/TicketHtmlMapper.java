package pt.isel.ls.mappers.html;

import pt.isel.ls.model.Ticket;
import pt.isel.ls.view.html.util.Element;

import java.util.List;

import static java.util.Arrays.*;

public class TicketHtmlMapper {

    private static String LINKBYID = "href='/cinemas/%d/theaters/%d/sessions/%d/tickets/%d'";

    public Element mapTickets(Ticket ticket){
        Element res = new Element("p");
        res.setContent(ticket);
        return res;
    }



    public Element mapTicketsList(List<Ticket> tickets, int tid, int cid){
        Element table = new Element("table",  asList("border = 1"));
        Element columns = new Element("tr");

        Element idCol = new Element("th");
        idCol.setContent("ID");
        Element rowCol =  new Element("th");
        rowCol.setContent("ROW");
        Element seatCol = new Element("th");
        seatCol.setContent("SEAT");

        columns.addElement(idCol,rowCol,
                seatCol);

        table.addElement(columns);

        for(Ticket t : tickets){
            Element row = new Element("tr");
            Element id = new Element("td");

            Element link = new Element("a" ,
                    asList(String.format( LINKBYID, cid,tid,t.getSessionId(),t.getTkid())));


            Element rowLetter = new Element("td");
            Element seat = new Element("td");

            id.addElement(link);

            link.setContent(t.getTkid());
            rowLetter.setContent(t.rowLetter);
            seat.setContent(t.seatNumber);


            row.addElement(id, rowLetter,
                    seat);
            table.addElement(row);
        }

        return table;
    }
}
