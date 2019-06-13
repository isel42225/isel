package pt.isel.ls.view.plain;

import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.result.Result;
import pt.isel.ls.view.View;
import pt.isel.ls.view.Viewable;

import java.io.IOException;
import java.io.Writer;

public class PlainView extends View {


    private Viewable build(Result target) {
        Viewable v = new PlainViewable(target);
        return v;
    }

    @Override
    public void show(Result target, Writer writer) throws CommandIOException {
        try
        {
            build(target).writeTo(writer);
        }catch (IOException e)
        {
            throw new CommandIOException(
                    String.format("Error writing HTML page using write %s",writer)
            );
        }
    }

    @Override
    public HttpContent getContent(Result target) {
        return build(target);
    }
}
