package pt.isel.ls.mappers.html;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.view.html.util.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.*;

public class CinemaHtmlMapper {


    public Element single(Cinema cin){

        Element res = new Element("p");
        res.setContent("Name : " + cin.name + " City : " + cin.city );

        return res;
    }

    public Element list(List<Cinema> cinemas){
        Element table = new Element("table", asList("border = '1'"));
        Element columns = new Element("tr");
        Element idCol =  new Element("th");

        idCol.setContent("ID");
        Element cityCol =  new Element("th");
        cityCol.setContent("CITY");
        Element nameCol =  new Element("th");
        nameCol.setContent("NAME");

        columns.addElement(idCol,
                            cityCol,
                            nameCol);

        //Primeira linha com nome das colunas
        table.addElement(columns);

        for(Cinema cin : cinemas){

            Element row = new Element("tr");
            Element id = new Element("td");
            Element link = new Element("a", asList("href='/cinemas/" + cin.cid +"'"));
            id.addElement(link);    //link to specific cinema html page
            Element city = new Element("td");
            Element name = new Element("td");

            link.setContent(cin.cid);
            city.setContent(cin.city);
            name.setContent(cin.name);

            row.addElement(id,
                             city,
                             name
            );

            table.addElement(row);
        }

        return table;
    }
}
