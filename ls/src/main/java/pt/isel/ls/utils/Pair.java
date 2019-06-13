package pt.isel.ls.utils;

public class Pair<K, V> {

    public final K key;
    public final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s:%s",key,value);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)return false;
        if(!(obj instanceof Pair))return false;
        Pair p = (Pair)obj;
        return p.key.equals(this.key);
    }

    @Override
    public int hashCode() {
        return (key.hashCode() + value.hashCode()) ^ key.hashCode();
    }
}
