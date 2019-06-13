package pt.isel.ls.view.json;

import java.util.ArrayList;
import java.util.List;

public class JsonArray {
    private List<JsonValue> values;

    public JsonArray() {
        this.values = new ArrayList<>();
    }

    public void addValue(JsonValue val)
    {
        values.add(val);
    }

    public List<JsonValue> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return String.format("%s",values);
    }
}
