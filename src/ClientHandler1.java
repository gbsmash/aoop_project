package src;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ClientHandler1 implements Runnable {
//    private final ServerSocket serverSocket;
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int nbStudent;
    private List<Student> students;
    private Server server;
    public ClientHandler1(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
//        this.socket = serverSocket.accept();
//        this.server = server;
        this.nbStudent = 0;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting for student...");
            Student student = (Student) in.readObject();
            System.out.println("Received student: " + student.getName());
            // Add student to the server's student list
            server.addStudent(student);
            // Update the Genetic Algorithm with the new student
            server.initializeGeneticAlgorithm();
            server.allocateStudents();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}