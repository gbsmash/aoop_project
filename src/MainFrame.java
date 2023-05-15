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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainFrame extends JFrame {
    JLabel title, name, surname;
    JTextArea t1, t2;
    JLabel title1, title2;
    JButton submitBtn;
    private Client client;
    private JTextArea preferencesTextArea;
    private JLabel inputErrorLabel;

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

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 650);
        this.getContentPane().setBackground(Color.decode("#F8CACD"));
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        title1 = new JLabel("Enter your desired destinations");
        title1.setBounds(210, 280, 400, 100);
        title1.setFont(new Font("Serif", Font.BOLD, 30));
        this.add(title1);

        title2 = new JLabel("<html>(only corresponding numbers, in descending order, comma separated)<br>Example: 3, 6, 2, 5, 9</html>");
        title2.setBounds(100, 290, 900, 100);
        title2.setFont(new Font("Serif", Font.PLAIN, 21));
        this.add(title2);

        List<Destination> destinations = Destination.getDefaultDestinations();

        String destinationsText = "";
        for (int i = 0; i < destinations.size(); i++) {
            destinationsText += " " + destinations.get(i).getIndex() + ". " + destinations.get(i).getName() + "\n";
        }

        JTextArea destinationsTextArea = new JTextArea(destinationsText);
        destinationsTextArea.setEditable(false);
        destinationsTextArea.setBounds(110, 310, 300, 270);
        destinationsTextArea.setFont(new Font("Serif", Font.PLAIN, 19));
        destinationsTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        destinationsTextArea.setBackground(Color.decode("#BFABCB"));
        this.add(destinationsTextArea);

        JLabel inputLabel = new JLabel("Preferences");
        inputLabel.setFont(new Font("Serif", Font.BOLD, 18));
        inputLabel.setBounds(490, 350, 300, 25);
        this.add(inputLabel);

        preferencesTextArea = new JTextArea();
        preferencesTextArea.setBounds(490, 380, 200, 30);
        preferencesTextArea.setFont(new Font("Serif", Font.PLAIN, 20));
        preferencesTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(preferencesTextArea);


        inputErrorLabel = new JLabel();
        inputErrorLabel.setForeground(Color.decode("#ba0606"));
        inputErrorLabel.setBounds(490, 390, 300, 25);
        this.add(inputErrorLabel);


        submitBtn = new JButton("Submit");
        submitBtn.setBounds(540, 400, 100, 40);
        submitBtn.setBackground(Color.decode("#8D89A6"));
        submitBtn.setFont(new Font("Arial", Font.BOLD, 17));
        submitBtn.setForeground(Color.white);
        this.add(submitBtn);

        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Student student = new Student();

                String name = t1.getText();
                String surname = t2.getText();
                String input = preferencesTextArea.getText();
                String[] preferencesInput = input.split("[,\\s]+");

                if (name.isEmpty() || surname.isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Please fill in all the fields.");
                } else if (preferencesInput.length != 5) {
                    inputErrorLabel.setText("Please enter exactly 5 preferences.");
                } else {
                    List<Destination> preferences = new ArrayList<>();
                    boolean validInput = true;

                    for (String preference: preferencesInput) {
                        try {
                            int index = Integer.parseInt(preference) - 1;
                            if (index < 0 || index >= destinations.size() || preferences.contains(destinations.get(index))) {
                                validInput = false;
                                break;
                            }
                            preferences.add(destinations.get(index));
                        } catch (NumberFormatException ex) {
                            validInput = false;
                            break;
                        }
                    }
                    if (!validInput) {
                        inputErrorLabel.setText("Enter unique numbers in range 1-10");
                    } else {
                        student.setName(name);
                        student.setSurname(surname);
                        student.setPreferences(preferences);
                        client = new Client("localhost", 1234, student);
                        inputErrorLabel.setText("");
                        MainFrame.this.setVisible(false);
                    }

                }
            }
        });
        this.setVisible(true);
    }
}