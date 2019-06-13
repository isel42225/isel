package pt.isel.ls.model;


import java.time.LocalDate;
import java.time.LocalTime;


public class Session implements DataTable {

    private final String TABLE_NAME = "Session";

    public int sid;
    public int cid;
    public int tid;
    public int mid;
    public LocalDate localDate;
    public LocalTime localTime;

    public Session(int sid, int cid, int tid, int mid, LocalDate localDate, LocalTime localTime) {
        this.sid = sid;
        this.cid = cid;
        this.tid = tid;
        this.mid = mid;
        this.localDate = localDate;
        this.localTime = localTime;
    }

    public Session(int cid, int tid, int mid, LocalDate localDate, LocalTime localTime) {
        this.cid = cid;
        this.tid = tid;
        this.mid = mid;
        this.localDate = localDate;
        this.localTime = localTime;
    }

    public Session(){

    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String toString() {
        return String.format("Session of cinema %d in theater %d , showing movie %d at %s starting at %s ",
                cid,
                tid,
                mid,
                localDate,
                localTime);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Session)) return false;
        Session s = (Session) obj;
        return s.sid == sid;
    }


}
