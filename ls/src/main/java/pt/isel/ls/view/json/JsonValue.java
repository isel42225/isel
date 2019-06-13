package pt.isel.ls.view.json;

import java.time.LocalDate;
import java.time.LocalTime;

public  class JsonValue {
    private Object value;
    public JsonValue(Object v) {
        value = v;
    }

    @Override
    public String toString() {
        if(value == null) return "null";
        if(value instanceof String || value instanceof LocalDate || value instanceof LocalTime) return String.format("\"%s\"",value);
        return value.toString();
    }
}
