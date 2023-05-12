package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler1> clients;
    private List<Destination> destinations;
    private int port;
    private boolean isRunning;
    private List<Assignment> assignments;
    private List<Student> students;

    private GeneticAlgorithm geneticAlgorithm;
    private List<Assignment> bestAssignment;
    private int populationSize = 200;
    private int maxGenerations = 1000;
    private double crossoverRate = 0.8;
    private double mutationRate = 0.02;

    public Server(int port) {
        this.port = port;
        clients = new ArrayList<>();
        destinations = new ArrayList<>();
        assignments = new ArrayList<>();
        students = new ArrayList<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            isRunning = true;
            System.out.println("Server started on port " + port);

            // Add code to populate your students list here.
            // this.students = ...;

            this.destinations = Destination.getDefaultDestinations();
            initializeGeneticAlgorithm();

            while (isRunning) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected " + socket.getInetAddress());
                ClientHandler1 clientHandler = new ClientHandler1(socket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Error start " + e.getMessage());
        }
        System.out.println("Best assignment found:");

    }

//    public void broadcastMessage(String message) {
//        for (ClientHandler client : clients) {
//        }
//    }
    public synchronized void initializeGeneticAlgorithm() {
        this.geneticAlgorithm = new GeneticAlgorithm(
                this.students,
                this.destinations,
                populationSize,
                maxGenerations,
                crossoverRate,
                mutationRate
        );
    }
    public synchronized void allocateStudents() {
        bestAssignment = geneticAlgorithm.run();
        System.out.println("Best assignment found:");
        for (Assignment assignment : bestAssignment) {
            System.out.println("Student: " + assignment.getStudent().getName()
                    + ", Destination: " + assignment.getDestination().getName());
        }
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }
    public List<Assignment> getAssignments() {
        return this.bestAssignment;
    }
    public synchronized void removeStudent(Student student) {
        assignments.removeIf(assignment -> assignment.getStudent().equals(student));
        for (Destination destination : destinations) {
            if (destination.removeStudent(student)) {
//                broadcastMessage("remove student " + student.getName() + " " + student.getSurname() + " " + destination.getName());
                break;
            }
        }
    }

    public synchronized void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
        assignment.getDestination().removeStudent(assignment.getStudent());
//        broadcastMessage("remove assignment " + assignment.getStudent().getName() + " " + assignment.getStudent().getSurname() + " " + assignment.getDestination().getName());
    }

//    public synchronized void addDestination(Destination destination) {
//        destinations.add(destination);
//    }

//    public synchronized int getMaxStudentCount(Destination destination) {
//        int count = 0;
//        for (Assignment assignment : assignments) {
//            if (assignment.getDestination().equals(destination)) {
//                count++;
//            }
//        }
//        return count;
//    }

    public synchronized int getAssignmentCost(Student student, Destination destination) {
        int cost = 0;
//        for (Destination pref : student.getPreferences()) {
//            if (pref.equals(destination)) {
//                cost = student.getPreferences().indexOf(pref) + 1;
//                break;
//            }
//        }
        return cost;
    }

    public synchronized List<Destination> getDestinations() { return destinations; }

    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();

    }
}
