package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler1 implements Runnable {
    private Socket socket;
    private List<ClientHandler1> clients;
    private List<Student> students;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler1(Socket socket, List<ClientHandler1> clients) {
        this.socket = socket;
        this.clients = clients;
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
            clients.add(this);
            Student student = (Student) in.readObject(); // dunno if we need it
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
