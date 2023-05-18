import java.io.IOException;
import java.io.ObjectOutputStream;
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
    // writes a Student object to the ObjectOutputStream, sending it to the server.
    public void send(Student student) throws IOException {
        System.out.println("Sending student: " + student.getName());
        this.out.writeObject(student);
        this.out.flush();
        System.out.println("Student sent: " + student.getName());
    }
    // creates a new Socket connected to the specified host and port, and sets up the ObjectOutputStream.
    public void connect(String serverHost, int serverPort) throws IOException {
        socket = new Socket(serverHost, serverPort);
        isConnected=true;
        out = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Connected to server at " + serverHost + ":" + serverPort);
    }
    // closes the Socket, effectively disconnecting from the server.
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
