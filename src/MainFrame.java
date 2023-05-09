package src;

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
    JLabel title, name, surname;
    JTextArea t1, t2;
    JButton submitBtn;

    public MainFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700,570);
        this.getContentPane().setBackground(Color.decode("#E6C0E9"));
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        title = new JLabel("Enter your name and surname");
        title.setBounds(170, 50, 500, 100);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        this.add(title);

        name = new JLabel("Name");
        name.setBounds(270,130,300,100);
        name.setFont(new Font("Serif", Font.BOLD, 17));
        this.add(name);

        t1 = new JTextArea();
        t1.setBounds(270, 195, 170, 25);
        t1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        t1.setFont(new Font("Serif", Font.PLAIN, 18));
        this.add(t1);

        surname = new JLabel("Surname ");
        surname.setBounds(270,210,300,100);
        surname.setFont(new Font("Serif", Font.BOLD, 17));
        this.add(surname);

        t2 = new JTextArea();
        t2.setBounds(270, 275, 170, 25);
        t2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        t2.setFont(new Font("Serif", Font.PLAIN, 18));
        this.add(t2);

        submitBtn = new JButton("Next");
        submitBtn.setBounds(305, 360, 100, 40);
        submitBtn.setBackground(Color.decode("#8D89A6"));
        submitBtn.setFont(new Font("Arial", Font.BOLD, 17));
        submitBtn.setForeground(Color.white);
        this.add(submitBtn);

        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Student student = new Student();

                String name = t1.getText();
                String surname = t2.getText();

                if (name.isEmpty() || surname.isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Please fill in all the fields.");
                } else {
                    student.setName(name);
                    student.setSurname(surname);
                    try {
                        Client client = new Client("localhost", 1234, student);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    dispose();
                    DestinationFrame destinationFrame = new DestinationFrame(student);
                }
            }
        });
        this.setVisible(true);
    }
}