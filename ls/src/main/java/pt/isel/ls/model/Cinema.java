package pt.isel.ls.model;

public class Cinema implements DataTable{

    private final String TABLE_NAME = "Cinema";

    public int cid;
    public String name;
    public String city;

    public Cinema(int cid, String name, String city) {
        this.cid = cid;
        this.name = name;
        this.city = city;
    }


    public Cinema(String name, String city) {
        this.name = name;
        this.city = city;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String toString() {

        return String.format("Name = %s , Location = %s  ",
                name , city  );
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Cinema)) return false;
        Cinema c = (Cinema) obj;
        return c.cid == cid;
    }
    @Override
    public int hashCode(){
        return cid ^ Integer.MAX_VALUE;
    }


}
