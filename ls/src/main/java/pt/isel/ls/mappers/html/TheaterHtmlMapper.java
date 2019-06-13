package pt.isel.ls.mappers.html;

import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.html.util.Element;

import java.text.MessageFormat;
import java.util.List;

import static java.util.Arrays.asList;

public class TheaterHtmlMapper {

    private String BORDER = "border = '1'";
    private static String LINKBYID = "href='/cinemas/%d/theaters/%d'";

    public Element mapTheater(Theater theater){
        Element res = new Element("p");
        Element info = new Element("p");
        info.setContent("Name : " + theater.name + " Available seats : " + theater.availableSeats);

       /* Element sessions = new Element("p");
        sessions.setContent("Sessions : ");
        for(Session s : theater.getSessions())
        {
            String link = String.format("href='/cinemas/%s/sessions/%s'",theater.cin.cid,s.sid);
            Element sLink = new Element("a", asList(link)); //session link
            sLink.setContent(s.localTime);
            sessions.addElement(sLink);
        }*/
        res.addElement(info);
        return res;
    }

    public Element mapTheaterList(List<Theater> theaters){
        Element table = new Element("table " + BORDER );
        Element collumns = new Element("tr");

        Element idCol =  new Element("th");
        idCol.setContent("ID");
        Element nameCol = new Element("th");
        nameCol.setContent("NAME");
        Element seatsCol = new Element("th");
        seatsCol.setContent("AVAILABLE SEATS");

        collumns.addElement(idCol,
                            nameCol,
                            seatsCol);

        table.addElement(collumns);

        for(Theater t : theaters){
            Element row = new Element("tr");
            Element id = new Element("td");
            Element link = new Element("a" , asList(String.format( LINKBYID, t.cid ,t.tid)));

            Element name = new Element("td");
            Element seats = new Element("td");
            id.addElement(link);

            link.setContent(t.tid);
            name.setContent(t.name);
            seats.setContent(t.availableSeats);

            row.addElement(id,
                           name,
                           seats);
            table.addElement(row);
        }

        return table;
    }
}
