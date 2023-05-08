import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainFrame extends JFrame {
    JLabel name, surname;
    JTextArea t1, t2;

    JButton submitBtn;

    public MainFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,400);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        name = new JLabel("Enter your name: ");
        name.setBounds(70,70,300,100);
        name.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(name);

        t1 = new JTextArea();
        t1.setBounds(350, 110, 100, 25);
        this.add(t1);

        surname = new JLabel("Enter your surname: ");
        surname.setBounds(70,100,300,100);
        surname.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(surname);

        t2 = new JTextArea();
        t2.setBounds(350, 140, 100, 25);
        this.add(t2);

        submitBtn = new JButton("Next");
        submitBtn.setBounds(250, 180, 80, 30);
        this.add(submitBtn);
        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitBtn.setEnabled(false);
                Student student = new Student();
                // Get the values from the JTextFields
                String name = t1.getText();
                String surname = t2.getText();

                // Check if all fields are filled
                if (name.isEmpty() || surname.isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Please fill in all the fields.");
                } else {
                    student.setName(name);
                    student.setSurname(surname);
                    // Create a DestinationFrame and pass the student object
                    DestinationFrame destinationFrame = new DestinationFrame(student);
                    destinationFrame.setVisible(true);
                    dispose();
                }
            }


        });




    }


}