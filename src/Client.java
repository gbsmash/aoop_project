package src;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    private Student student;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    public boolean isConnected;

    public Client(String serverHost, int serverPort, Student student) {
        this.student = student;
        try {
            connect(serverHost, serverPort);
            send(student);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Student student) throws IOException {
        System.out.println("Sending student: " + student.getName());
        this.out.writeObject(student);
        this.out.flush();
        System.out.println("Student sent: " + student.getName());
    }
    public List<Destination> requestDestinations(Student student) throws IOException, ClassNotFoundException {
        out.writeObject(student);
        out.flush();
        return getDestinations();
    }

    public List<Destination> getDestinations() throws IOException, ClassNotFoundException {
        return (List<Destination>) in.readObject();
    }
    public void connect(String serverHost, int serverPort) throws IOException {
        socket = new Socket(serverHost, serverPort);
        isConnected=true;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected to server at " + serverHost + ":" + serverPort);
    }

    void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            isConnected=false;
            System.out.println("Disconnected from server");
        }
    }

    public static void main(String[] args) throws IOException {
        MainFrame f = new MainFrame();
    }
}
