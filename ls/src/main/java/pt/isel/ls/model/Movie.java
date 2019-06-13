package pt.isel.ls.model;

import java.util.List;
import java.util.function.Supplier;

public class Movie implements DataTable {

    private final String TABLE_NAME = "Movie";

    public int mid;
    public String title;
    public int releaseYear;
    public int duration;


    public Movie(int mid , String title, int releaseYear, int duration) {
        this.mid = mid;
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public Movie(String title, int releaseYear, int duration) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public Movie(int mid) {
        this.mid = mid;
    }


    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public int getDuration() {
        return duration;
    }
    @Override
    public String toString() {
        return String.format("Title = %s , ReleaseYear = %d , Duration(min) = %d\n",
                title , releaseYear , duration);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Movie)  ) return false;
        Movie m = (Movie) obj;
        return m.mid == mid;
    }

    @Override
    public int hashCode() {
        return mid ^ Integer.MAX_VALUE;
    }


}
