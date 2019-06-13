package pt.isel.ls.view.html.util;

import pt.isel.ls.http.HttpContent;
import pt.isel.ls.view.Viewable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Element implements Viewable {

    public final Tag tag;
    public List<Element> children;

    public Object content;


    public Element(String tag) {
        this.tag = new Tag(tag);
        children = new ArrayList<>();
    }
    public Element(String tag, List<String> attrs) {
        this.tag = new Tag(tag, attrs);
        children = new ArrayList<>();
    }


    //Add content to html element
    public void addElement(Element... elements){
        for(Element e : elements){
            children.add(e);
        }
    }

    public void setContent(Object content) {
        this.content = content;
    }

    //método que sabe escrever, recebe um writer é recursivo (!!!!)
    public void writeTo(Writer out) throws IOException{

        out.write(tag.start);

        //write html element content
        if(content != null) out.write(content.toString());

        for(Element e : children){
            e.writeTo(out);
        }

        out.write(tag.end + "\n");
        out.flush();
    }

    @Override
    public String getMediaType() {
        return "text/html";
    }
}
