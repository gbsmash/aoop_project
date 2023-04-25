import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int nbClient;


    public ClientHandler(ServerSocket serverSocket, Socket socket) throws IOException {
        this.serverSocket = serverSocket;
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }


    @Override
    public void run() {
        try {
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Student № " + nbClient + " is connected!");
                nbClient++;
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
