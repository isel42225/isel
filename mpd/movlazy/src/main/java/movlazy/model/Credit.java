package movlazy.model;

import java.util.function.Supplier;

/**
 * @author Miguel Gamboa
 *         created on 04-08-2017
 */
public class Credit {
    private final int id;
    private final String character;
    private final String name;
    private final String department;
    private final String job;
    private final Supplier<Person> actor;



    public Credit(int id, String character, String name, String department, String job, Supplier<Person> actor) {
        this.id = id;
        this.character = character;
        this.name = name;
        this.department = department;
        this.job = job;
        this.actor = actor;
    }

    public int getId() {
        return id;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public Person getActor() {
        return actor.get();
    }

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
    }

    @Override
    public String toString() {
        return "Credit{" +
                " id =" + id +
                ", character ='" + character + '\'' +
                ", name ='" + name + '\'' +
                ", job = " + job + '\'' +
                ", department = " + department + '\'' +
                ", getPersonCreditsCast =" + actor +
                '}';
    }
}
