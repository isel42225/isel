package pt.isel.ls.view.json;

import pt.isel.ls.utils.Pair;

public class JsonPair {
    public final Pair<String , JsonValue> pair;

    public JsonPair(String name , JsonValue val){
        this.pair = new Pair<>(name , val);
    }

    @Override
    public String toString() {
        return String.format("\"%s\": %s", pair.key, pair.value);
    }

    @Override
    public int hashCode() {
        return pair.hashCode();
    }
}
