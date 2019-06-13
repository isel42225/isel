package pt.isel.ls.view.html.util;

import java.util.ArrayList;
import java.util.List;

public class  Tag {
    public final String start;
    public final String end;


    public List<String> attributes = new ArrayList<>();


    public Tag(String start) {
        this.start = String.format("<%s>",start);
        end = String.format("</%s>",start);
    }

    public Tag(String start, List<String> attributes) {
        this.start = String.format("<%s %s>",start, attributes)
                            .replaceAll("\\[","").replaceAll("]","");
        this.attributes = attributes;
        end = String.format("</%s>",start);
    }


}
