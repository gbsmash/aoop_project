import java.util.List;

public class Destination {
    private String name;
    private int maxStudents;
    private List<Student> assignedStudents;

    public Destination(String name, int maxStudents) {
        this.name = name;
        this.maxStudents = maxStudents;
    }

    boolean removeStudent(Student student){
        for(Student s: assignedStudents){
            if(s==student){
                assignedStudents.remove(student);
                return true;
            }
        }
        return false;
    }

    boolean addStudent(Student student){
        if(assignedStudents.size()<maxStudents){
            assignedStudents.add(student);
            return true;
        }
        return false;
    }

    boolean isFull(){
        return assignedStudents.size() == maxStudents;
    }

    public void setAssignedStudents(List<Student> assignedStudents) {
        this.assignedStudents = assignedStudents;
    }

    public String getName() {
        return name;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public List<Student> getAssignedStudents() {
        return assignedStudents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }
}
