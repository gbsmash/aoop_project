import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    public boolean isConnected;

    public Client(String serverHost, int serverPort, Student student) {
        try {
            connect(serverHost, serverPort); // connect to server
            send(student); // send student to server
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
    public void connect(String serverHost, int serverPort) throws IOException {
        socket = new Socket(serverHost, serverPort);
        isConnected=true;
        out = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Connected to server at " + serverHost + ":" + serverPort);
    }

    void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            isConnected=false;
            System.out.println("Disconnected from server");
        }
    }

    public static void main(String[] args) {
        MainFrame f = new MainFrame();
    }
}
