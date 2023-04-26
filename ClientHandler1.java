import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler1 implements Runnable {
    private final ServerSocket serverSocket;
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int nbStudent;

    public ClientHandler1(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.socket = serverSocket.accept();
        this.nbStudent = 0;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Object request = in.readObject();

                if (request instanceof Student) {
                    out.writeObject(nbStudent);
                    System.out.println("Student " + nbStudent + " connected!");
                    nbStudent++;
                }
                else { System.err.println("invalid request: " + request); }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}