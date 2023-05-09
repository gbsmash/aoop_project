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

            // No need to initialize the GeneticAlgorithm instance here
            // It will be initialized within the handleStudent method

            while (isRunning) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected " + socket.getInetAddress());
                handleStudent(socket);
                ClientHandler1 clientHandler = new ClientHandler1(serverSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            System.err.println("Error start " + e.getMessage());
        }
    }

//    public void broadcastMessage(String message) {
//        for (ClientHandler client : clients) {
//        }
//    }

    public synchronized void handleStudent(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            Student student = (Student) in.readObject();
            int maxCost = calculateCost();

            out.writeObject(destinations);
            out.flush();

            students.add(student);

            int populationSize = 50;
            int maxGenerations = 100;
            double crossoverRate = 0.8;
            double mutationRate = 0.1;
            this.geneticAlgorithm = new GeneticAlgorithm(students, destinations, populationSize, maxGenerations, crossoverRate, mutationRate);

            List<Assignment> optimalAssignments = geneticAlgorithm.run();
            assignments = optimalAssignments;

            while (true) {
                String preferenceName = (String) in.readObject();
                Destination preference = null;

                for (Destination dest : destinations) {
                    if (dest.getName().equals(preferenceName)) {
                        preference = dest;
                        break;
                    }
                }

                if (preference != null) {
                    int preferenceCost = getAssignmentCost(student, preference);
                    if (preferenceCost <= maxCost && !preference.isFull()) {
                        Assignment assignment = new Assignment(student, preference);
                        assignment.setCost(preferenceCost);
                        assignments.add(assignment);
                        preference.addStudent(student);
                        student.setAssignedDest(preference);
                        break;
                    } else {
                        out.writeObject("rejected");
                        out.flush();
                    }
                } else {
                    out.writeObject("invalid");
                    out.flush();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("error handling studen " + e.getMessage());
        }
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

    public synchronized void addDestination(Destination destination) {
        destinations.add(destination);
    }

    public synchronized int getMaxStudentCount(Destination destination) {
        int count = 0;
        for (Assignment assignment : assignments) {
            if (assignment.getDestination().equals(destination)) {
                count++;
            }
        }
        return count;
    }

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

    public synchronized List<Assignment> getAssignments() { return assignments; }

    public synchronized int calculateCost() {
        int totalCost = 0;
        return totalCost;
    }

//    public synchronized void calculateAssignment() {
//        List<Student> unassignedStudents = new ArrayList<>();
//        for (ClientHandler client : clients) {
//            unassignedStudents.add(client.getStudent());
//        }
//    }

    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();

    }
}
