package pt.isel.ls.view.html;

import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.view.View;
import pt.isel.ls.view.html.util.Element;

import java.io.Writer;

//Vista abstract de uma representação html
public abstract class AbstractHtmlView extends View {

    protected Element initPage(String pageTitle){
        Element page  = new Element("html");
        Element head = new Element("head");
        Element title = new Element("title");
        title.setContent(pageTitle);
        head.addElement(title);
        page.addElement(head);
        return page;
    }

}
