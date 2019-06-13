package pt.isel.ls.view;

import pt.isel.ls.http.HttpContent;

import java.io.IOException;
import java.io.Writer;

public interface Viewable extends HttpContent {


    /**
     * Write to a OutputStream
     * @param out writer in which to write
     * @throws IOException
     */
    void writeTo(Writer out) throws IOException;
}
