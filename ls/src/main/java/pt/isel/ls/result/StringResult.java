package pt.isel.ls.result;

public class StringResult implements Result {
    private final String res;

    public StringResult(String res) {
        this.res = res;
    }

    public String getRes() {
        return res;
    }
}
