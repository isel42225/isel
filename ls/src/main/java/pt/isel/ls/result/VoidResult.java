package pt.isel.ls.result;

public class VoidResult implements Result {

    public static VoidResult empty()
    {
        return new VoidResult();
    }

}
