
import java.io.*;
import java.net.Socket;
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
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
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

    public void connect(String serverHost, int serverPort) throws IOException {
        socket = new Socket(serverHost, serverPort);
        isConnected=true;
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
        Student student = new Student();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name: ");
        student.setName(sc.nextLine());
        System.out.println("Enter your surname: ");
        student.setSurname(sc.nextLine());
        Client client = new Client("localhost", 1234, student);
    }
}
