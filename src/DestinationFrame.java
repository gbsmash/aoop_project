package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DestinationFrame extends JFrame {

    JLabel title1, title2;
    JTextArea t1;
    JScrollPane scrollPane;
    JButton submitBtn;

    private JComboBox<String> destinationComboBox;
    private ButtonGroup destinationButtonGroup;
    private List<JComboBox<String>> preferenceComboBoxes;
    private JTextArea preferencesTextArea;
    private JLabel inputErrorLabel;
    public DestinationFrame(Student student) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 650);
        this.getContentPane().setBackground(Color.decode("#F8CACD"));
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        title1 = new JLabel("Enter your desired destinations");
        title1.setBounds(210, 45, 400, 100);
        title1.setFont(new Font("Serif", Font.BOLD, 30));
        this.add(title1);

        title2 = new JLabel("<html>(only corresponding numbers, in descending order, comma separated)<br>Example: 3, 6, 2, 5, 9</html>");
        title2.setBounds(100, 100, 900, 100);
        title2.setFont(new Font("Serif", Font.PLAIN, 21));
        this.add(title2);

        List<Destination> destinations = Destination.getDefaultDestinations();

        String destinationsText = "";
        for (int i = 0; i < destinations.size(); i++) {
            destinationsText += " " + destinations.get(i).getIndex() + ". " + destinations.get(i).getName() + "\n";
        }

        JTextArea destinationsTextArea = new JTextArea(destinationsText);
        destinationsTextArea.setEditable(false);
        destinationsTextArea.setBounds(110, 220, 300, 270);
        destinationsTextArea.setFont(new Font("Serif", Font.PLAIN, 19));
        destinationsTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        destinationsTextArea.setBackground(Color.decode("#BFABCB"));
        this.add(destinationsTextArea);

        JLabel inputLabel = new JLabel("Preferences");
        inputLabel.setFont(new Font("Serif", Font.BOLD, 18));
        inputLabel.setBounds(490, 250, 300, 25);
        this.add(inputLabel);

        preferencesTextArea = new JTextArea();
        preferencesTextArea.setBounds(490, 277, 200, 30);
        preferencesTextArea.setFont(new Font("Serif", Font.PLAIN, 20));
        preferencesTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(preferencesTextArea);


        inputErrorLabel = new JLabel();
        inputErrorLabel.setForeground(Color.decode("#ba0606"));
        inputErrorLabel.setBounds(490, 310, 300, 25);
        this.add(inputErrorLabel);

        submitBtn = new JButton("Submit");
        submitBtn.setBounds(540, 337, 100, 40);
        submitBtn.setBackground(Color.decode("#8D89A6"));
        submitBtn.setFont(new Font("Arial", Font.BOLD, 17));
        submitBtn.setForeground(Color.white);
        this.add(submitBtn);

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = preferencesTextArea.getText();
                String[] preferencesInput = input.split("[,\\s]+");

                if (preferencesInput.length != 5) {
                    inputErrorLabel.setText("Please enter exactly 5 preferences.");
                }
                else { // check if input is correct(1-10) and unique
                    List<Destination> preferences = new ArrayList<>();
                    boolean validInput = true;

                    for (String preference : preferencesInput) {
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
                        inputErrorLabel.setText("");
                        student.setPreferences(preferences);
                        dispose();
                        AssignmentFrame assignmentFrame = new AssignmentFrame();
                    }
                }
            }
        });

        this.setVisible(true);
    }
}
