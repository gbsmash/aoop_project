package src;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler1> clients;
    private List<Destination> destinations;
    private List<Destination> preferences;
    private int port;
    private boolean isRunning;
    private List<Assignment> assignments;
    private List<Student> students;

    private GeneticAlgorithm geneticAlgorithm;
    private List<Assignment> bestAssignment;
    private int populationSize = 100;
    private int maxGenerations = 250;
    private double crossoverRate = 0.8;
    private double mutationRate = 0.08;

    private AssignmentFrame assignmentFrame;
    private Map<Student, List<Destination>> preferencesMap;
    public Server(int port) {
        this.port = port;
        clients = new ArrayList<>();
        destinations = new ArrayList<>();
        assignments = new ArrayList<>();
        students = new ArrayList<>();
        preferences = new ArrayList<>();
        preferencesMap = new HashMap<>();
//        assignmentFrame = new AssignmentFrame(this);
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
    public void initializeGeneticAlgorithm() {
        this.geneticAlgorithm = new GeneticAlgorithm(
                this.students,
                this.destinations,
                populationSize,
                maxGenerations,
                crossoverRate,
                mutationRate
        );
    }
    public void allocateStudents() {
        bestAssignment = geneticAlgorithm.run();
        System.out.println("Best assignment found:");
        for (Assignment assignment : bestAssignment) {
            System.out.println("Student: " + assignment.getStudent().getName()
                    + ", Destination: " + assignment.getDestination().getName());
        }
        System.out.println(this.students);

    }
    public void addStudent(Student student) {
        this.students.add(student);

//        this.students.add
    }
    public void addPreference(Student student, List<Destination> preferences) {
        try {
            Socket socket = new Socket("localhost", 1234);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject(student);
            out.writeObject(preferences);

            out.close();
            socket.close();
        } catch (IOException e) {
            // Handle any networking errors here
            e.printStackTrace();
        }
    }
    public void genetic() {
        if (assignmentFrame != null) {
            assignmentFrame.dispose();  // Close the previous AssignmentFrame
        }
        initializeGeneticAlgorithm();
        allocateStudents();
        assignmentFrame = new AssignmentFrame(bestAssignment);
    }

    public List<Assignment> getAssignments() {
        return this.bestAssignment;
    }


    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();

    }
}
