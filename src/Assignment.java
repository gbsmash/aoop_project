public class Assignment {
    private Student student;
    private Destination destination;
    private int cost;

    public Assignment(Student student, Destination destination) {
        this.student = student;
        this.destination = destination;
    }
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
