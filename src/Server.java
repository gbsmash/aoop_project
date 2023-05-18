import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private List<Destination> destinations;
    private int port;
    private boolean isRunning;
    private List<Student> students;

    private GeneticAlgorithm geneticAlgorithm;
    private List<Assignment> bestAssignment;
    private int populationSize = 100;
    private int maxGenerations = 250;
    private double crossoverRate = 0.8;
    private double mutationRate = 0.05;

    private AssignmentFrame assignmentFrame;
    public Server(int port) {
        this.port = port;
        clients = new ArrayList<>();
        destinations = Destination.getDefaultDestinations();
        students = new ArrayList<>();
    }

    public void start() {
        try {
            // creates a new ServerSocket
            serverSocket = new ServerSocket(port);
            isRunning = true;
            System.out.println("Server started on port " + port);
            while (isRunning) { // listen constantly to connected clients
                Socket socket = serverSocket.accept();
                System.out.println("Client connected " + socket.getInetAddress());
                // new ClientHandler is created to handle communication with that client
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start(); // start a thread for each student
            }
        } catch (IOException e) {
            System.err.println("Error start " + e.getMessage());
        }
    }
    // creates a new GeneticAlgorithm instance with the current students and destinations
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
    // adds a Student object to the students list
    public void addStudent(Student student) {
        this.students.add(student);
    }
    // runs the geneticAlgorithm and updates the bestAssignment and assignmentFrame
    public void genetic() {
        if (assignmentFrame != null) {
            assignmentFrame.dispose();  // Close the previous AssignmentFrame
        }
        initializeGeneticAlgorithm();
        bestAssignment = geneticAlgorithm.run();
        assignmentFrame = new AssignmentFrame(bestAssignment);
        long startTime = System.currentTimeMillis();
    }

    public static void main(String[] args) {
        Server server = new Server(1222);
        server.start();
    }
}
