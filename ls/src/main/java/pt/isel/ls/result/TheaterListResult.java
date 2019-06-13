package pt.isel.ls.result;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;

import java.util.List;

public class TheaterListResult implements Result {
    private final Cinema cin;
    private final List<Theater> theaters;

    public TheaterListResult(Cinema cin, List<Theater> theaters) {
        this.cin = cin;
        this.theaters = theaters;
    }

    public Cinema getCin()
    {
        return cin;
    }

    public List<Theater> getTheaters()
    {
        return theaters;
    }
}
