package pt.isel.ls.mappers.html;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.view.html.util.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class MovieHtmlMapper {

    public Element map(Movie mov){
        Element res = new Element("p");
        res.setContent("Name : " + mov.title + " Duration (min) :" + mov.getDuration() );

        return res;
    }

    public Element mapList(List<Movie> movies){
        Element table = new Element("table", asList("border = '1'"));
        Element collumns = new Element("tr");

        Element idCol =  new Element("th");
        idCol.setContent("ID");
        Element titleCol =  new Element("th");
        titleCol.setContent("TITLE");
        Element durationCol =  new Element("th");
        durationCol.setContent("DURATION");
        collumns.addElement(idCol,
                titleCol,
                durationCol);

        //Primeira linha com nome das colunas
        table.addElement(collumns);

        for(Movie mov : movies){

            Element row = new Element("tr");
            Element id = new Element("td");
            Element title = new Element("td");
            Element duration = new Element("td");
            Element link = new Element("a", asList("href='/movies/" + mov.mid +"'"));
            id.addElement(link);


            link.setContent(mov.mid);
            title.setContent(mov.title);
            duration.setContent(mov.duration);
            row.addElement(id,
                           title,
                           duration);

            table.addElement(row);
        }

        return table;
    }
}
