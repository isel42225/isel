package pt.isel.ls.result;

public class IntegerResult implements Result {
    private final int res ;

    public IntegerResult(int res) {
        this.res = res;
    }

    public int getRes() {
        return res;
    }
}
