import javax.swing.*;
import java.awt.*;
import java.net.ServerSocket;

public class DestinationFrame extends JFrame {

    ServerSocket serverSocket;
    JLabel title1, title2;
    JTextArea t1, t2;

    JButton submitBtn;

    public DestinationFrame(){
//        this.serverSocket=serverSocket;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        title1 = new JLabel("Enter your desired destinations");
        title1.setBounds(70,70,300,100);
        title1.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(title1);

        title2 = new JLabel("<html>(only corresponding numbers, in descending order, comma separated)<br>Ex: 3, 6, 2, 5, 9</html>");
        title2.setBounds(70,130,900,100);
        title2.setFont(new Font("Arial", Font.PLAIN, 20));
        this.add(title2);












//        this.setVisible(true);

    }

//    public static void main(String[] args) {
//        DestinationFrame destinationFrame = new DestinationFrame();
//    }
}
