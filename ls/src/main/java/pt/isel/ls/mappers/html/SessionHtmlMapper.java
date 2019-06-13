package pt.isel.ls.mappers.html;

import pt.isel.ls.model.Session;
import pt.isel.ls.view.html.util.Element;

import java.util.List;

import static java.util.Arrays.asList;

public class SessionHtmlMapper {
    public Element single(Session session){
        Element res = new Element("p");
        res.setContent("Movie starting at : " + session.localTime);
        return res;
    }


    public Element list(List<Session> sessions){
        Element table = new Element("table", asList("border = '1'"));
        Element collumns = new Element("tr");

        Element idCol =  new Element("th");
        idCol.setContent("ID");
        Element dateCol = new Element("th");
        dateCol.setContent("Date");
        Element timeCol = new Element("th");
        timeCol.setContent("Time");


        collumns.addElement(idCol,
                            dateCol,
                            timeCol);
        table.addElement(collumns);

        for(Session s : sessions){
            Element row = new Element("tr");
            Element id = new Element("td");
            Element link = new Element("a", asList(String.format("href='/cinemas/%d/sessions/%d'",s.cid,s.sid)));
            id.addElement(link);
            Element date = new Element("td");
            Element time = new Element("td");

            link.setContent(s.sid);
            date.setContent(s.localDate);
            time.setContent(s.localTime);

            row.addElement(id,
                           date,
                           time);

            table.addElement(row);
        }

        return table;
    }
}
