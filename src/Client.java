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

    public Client(String serverHost, int serverPort, Student student) throws IOException {
        this.student=student;
        connect(serverHost, serverPort);
//        this.out = new ObjectOutputStream(socket.getOutputStream());
//        this.in = new ObjectInputStream(socket.getInputStream());
    }


    public List<Destination> requestDestinations(Student student) throws IOException, ClassNotFoundException {
        out.writeObject(student);
        out.flush();
        return getDestinations();
    }

    void sendMessage(){             // EXAMPLE CODE
        try {
            // first, send the name of client
            out.writeChars(student.getName() + ' '+student.getSurname() +'\n');
            out.flush();

            Scanner sc = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = sc.nextLine();
                out.writeChars(student.getName() + ' '+student.getSurname() + ": " + message+'\n');
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void receiveMessage(){                // EXAMPLE CODE
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromOthers;
                while (socket.isConnected()) {
                    try {
                        messageFromOthers = in.readLine();
                        System.out.println(messageFromOthers);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    public List<Destination> getDestinations() throws IOException, ClassNotFoundException {
        // Read the destinations list from the server
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
