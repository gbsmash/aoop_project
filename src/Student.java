import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student implements Serializable {
    private String name;
    private String surname;
    private List<Destination> preferences;
    private Destination assignedDest;

    public Student() {
        preferences = new ArrayList<>();
    }

    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
        preferences = new ArrayList<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return Objects.equals(getName(), student.getName()) && Objects.equals(getSurname(), student.getSurname()) && Objects.equals(getPreferences(), student.getPreferences()) && Objects.equals(getAssignedDest(), student.getAssignedDest());
    }
}
