package pt.isel.ls.view;

import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.result.Result;

import java.io.Writer;

public abstract class View  {

    /**
     * show a view representation of target
     * @param res object to show
     * @param writer
     */
    public abstract  void show(Result res, Writer writer) throws CommandIOException;

    public abstract HttpContent getContent(Result content);
}
