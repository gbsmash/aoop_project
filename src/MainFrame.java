import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    JLabel title, name, surname;
    JTextArea t1, t2;
    JLabel title1, title2;
    JButton submitBtn;
    private final JTextArea preferencesTextArea;
    private final JLabel inputErrorLabel;

    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(790,700);
        this.getContentPane().setBackground(Color.decode("#ede6ed"));
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        title = new JLabel("Enter your name and surname");
        title.setBounds(200, 10, 500, 100);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        this.add(title);

        name = new JLabel("Name");
        name.setBounds(290,50,300,100);
        name.setFont(new Font("Serif", Font.BOLD, 17));
        this.add(name);

        t1 = new JTextArea();
        t1.setBounds(290, 115, 170, 25);
        t1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        t1.setFont(new Font("Serif", Font.PLAIN, 18));
        this.add(t1);

        surname = new JLabel("Surname ");
        surname.setBounds(290,130,300,100);
        surname.setFont(new Font("Serif", Font.BOLD, 17));
        this.add(surname);

        t2 = new JTextArea();
        t2.setBounds(290, 195, 170, 25);
        t2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        t2.setFont(new Font("Serif", Font.PLAIN, 18));
        this.add(t2);


        title1 = new JLabel("Enter your desired destinations");
        title1.setBounds(190, 200, 400, 100);
        title1.setFont(new Font("Serif", Font.BOLD, 28));
        this.add(title1);

        title2 = new JLabel("<html>(only corresponding numbers, in descending order of preference, comma separated)<br>Example: 3, 6, 2, 5, 9</html>");
        title2.setBounds(110, 240, 900, 100);
        title2.setFont(new Font("Serif", Font.PLAIN, 17));
        this.add(title2);

        // displays the default destinations on screen
        List<Destination> destinations = Destination.getDefaultDestinations();
        String destinationsText = "";
        for (Destination destination : destinations) {
            destinationsText += " " + destination.getIndex() + ". " + destination.getName() + "\n";
        }

        JTextArea destinationsTextArea = new JTextArea(destinationsText);
        destinationsTextArea.setEditable(false);
        destinationsTextArea.setBounds(110, 330, 300, 270);
        destinationsTextArea.setFont(new Font("Serif", Font.PLAIN, 19));
        destinationsTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        destinationsTextArea.setBackground(Color.decode("#cac2cf"));
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
        inputErrorLabel.setBounds(490, 410, 300, 25);
        this.add(inputErrorLabel);


        submitBtn = new JButton("Submit");
        submitBtn.setBounds(540, 435, 100, 40);
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

                // check the correctness of the input
                if (name.isEmpty() || surname.isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Please fill in all the fields.");
                } else if (preferencesInput.length != 5) {
                    inputErrorLabel.setText("Please enter exactly 5 preferences.");
                } else {
                    List<Destination> preferences = new ArrayList<>();
                    boolean validInput = true;

                    // go over the preference input and check whether the input is valid, if it is valid add it to preferences
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
                        // set the student info
                        student.setName(name);
                        student.setSurname(surname);
                        student.setPreferences(preferences);
                        // create an instance of client with the setted student info
                        Client client = new Client("localhost", 1222, student);
                        inputErrorLabel.setText("");
                        MainFrame.this.setVisible(false);
                    }

                }
            }
        });
        this.setVisible(true);
    }
}