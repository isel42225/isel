package pt.isel.ls.view.json;

import pt.isel.ls.utils.Pair;
import pt.isel.ls.view.Viewable;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonObject implements Viewable {

    private List<JsonPair> members;

    public JsonObject(List<JsonPair> members) {
        this.members = members;
    }

    public JsonObject()
    {
        members = new ArrayList<>();
    }

    public void addMember(JsonPair pair)
    {
        members.add(pair);
    }

    public List<JsonPair> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("{");

        for(JsonPair p : members)
        {
            ret.append(p).append(",");
        }
        //delete last ',' and add final '}'
        ret.deleteCharAt(ret.lastIndexOf(",")).append("}");

        return ret.toString();
    }

    @Override
    public void writeTo(Writer out) throws IOException {
        out.write(toString());
        out.flush();
    }

    @Override
    public String getMediaType() {
        return "application/json";
    }
}
