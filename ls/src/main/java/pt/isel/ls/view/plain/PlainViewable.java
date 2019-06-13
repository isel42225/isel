package pt.isel.ls.view.plain;

import pt.isel.ls.result.Result;
import pt.isel.ls.view.Viewable;

import java.io.IOException;
import java.io.Writer;

public class PlainViewable implements Viewable {
    private Result target;

    public PlainViewable(Result target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return target.toString();
    }

    @Override
    public void writeTo(Writer out) throws IOException {
        out.write(toString() +"\n");
        out.flush();
    }

    @Override
    public String getMediaType() {
        return "text/plain";
    }
}
