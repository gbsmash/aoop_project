package src;

import java.io.*;
import java.net.Socket;

public class ClientHandler1 implements Runnable {
    private Socket socket;
    private Server server;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler1(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error initializing ClientHandler1: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            Student student = (Student) in.readObject(); // Read the student object from the input stream
            server.handleStudent(student, out);
            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            System.err.println("Error in ClientHandler1 run method: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
