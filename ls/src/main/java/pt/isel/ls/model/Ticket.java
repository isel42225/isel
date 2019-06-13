package pt.isel.ls.model;



public class Ticket implements DataTable {

    private final String TABLE_NAME = "Ticket";

    public int tkid;
    private int sid;
    public int seatNumber;
    public String rowLetter;


    public Ticket(int tkid, int sid, int seatNumber, String rowLetter) {
        this.tkid = tkid;
        this.sid = sid;
        this.seatNumber = seatNumber;
        this.rowLetter = rowLetter;
    }

    public Ticket(int sid, int seatNumber, String rowLetter) {
        this.sid = sid;
        this.seatNumber = seatNumber;
        this.rowLetter = rowLetter;
    }

    public void setTkid(int tkid) {
        this.tkid = tkid;
    }

    public int getTkid() {
        return tkid;
    }

    public int getSessionId() {
        return sid;
    }

    public int getSeat() {
        return seatNumber;
    }

    public String getRow() {
        return rowLetter;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Ticket)) return false;
        Ticket tk = (Ticket) obj;
        return tk.tkid == tkid;
    }

    @Override
    public String toString() {
        return String.format("Tkid = %d, session id : %d, row: %s, seat: %s ",tkid,sid, rowLetter, seatNumber);
    }


}

