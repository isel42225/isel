package pt.isel.ls.view.html.util;

import pt.isel.ls.utils.Pair;

public class Attribute<K,V> {

    private Pair<K,V> pair;

    public Attribute(Pair<K, V> pair) {
        this.pair = pair;
    }

    @Override
    public String toString() {
        return String.format("%s = %s", pair.key, pair.value);
    }
}
