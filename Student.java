

import java.util.List;

public class Student {
    private String name;
    private String surname;
    private List<Destination> preferences;
    private Destination assignedDest;

    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Destination> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Destination> preferences) {
        this.preferences = preferences;
    }

    public Destination getAssignedDest() {
        return assignedDest;
    }

    public void setAssignedDest(Destination assignedDest) {
        this.assignedDest = assignedDest;
    }
}
