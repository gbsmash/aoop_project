import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Server server;
    public ClientHandler(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting for student...");
            Student student = (Student) in.readObject(); // get student from client
            System.out.println("Received student: " + student.getName());

            server.addStudent(student); // add to students array
            server.genetic(); // implement the algorithm

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close the connection
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}