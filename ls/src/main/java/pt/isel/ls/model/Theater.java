package pt.isel.ls.model;


public class Theater implements DataTable {

    public int tid;
    public int cid;
    public String name;
    public int availableSeats;
    public int rows;
    public int seatsPerRow;


    public Theater()
    {
    }


    public Theater(int tid, int cid, String name, int availableSeats, int rows, int seatsPerRow) {
        this.tid = tid;
        this.cid = cid;
        this.name = name;
        this.availableSeats = availableSeats;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }

    public Theater(int cid, String name, int availableSeats, int rows, int seatsPerRow) {
        this.cid = cid;
        this.name = name;
        this.availableSeats = availableSeats;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }

    @Override
    public String toString() {
        return String.format("Name = %s , Capacity = %d , Free Seats = %d \n",
                name , rows * seatsPerRow,  availableSeats);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Theater)) return false;
        Theater t = (Theater) obj;
        return t.tid == tid;
    }

    @Override
    public String getTableName() {
        return "Theater";
    }
}
