import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DestinationFrame extends JFrame {

    JLabel title1, title2;
    JTextArea t1;
    JScrollPane scrollPane;
    JButton submitBtn;
    private Client client;

    private JComboBox<String> destinationComboBox;
    private ButtonGroup destinationButtonGroup;
    private List<JComboBox<String>> preferenceComboBoxes;
    private JTextArea preferencesTextArea;
    private JLabel inputErrorLabel;
    public DestinationFrame(Client client, Student student) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        title1 = new JLabel("Enter your desired destinations");
        title1.setBounds(70, 70, 400, 100);
        title1.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(title1);

        title2 = new JLabel("<html>(only corresponding numbers, in descending order, comma separated)<br>Ex: 3, 6, 2, 5, 9</html>");
        title2.setBounds(70, 130, 900, 100);
        title2.setFont(new Font("Arial", Font.PLAIN, 20));
        this.add(title2);

        List<Destination> destinations = Destination.getDefaultDestinations();

        String destinationsText = "";
        for (int i = 0; i < destinations.size(); i++) {
            destinationsText += destinations.get(i).getIndex() + ". " + destinations.get(i).getName() + "\n";
        }

        JTextArea destinationsTextArea = new JTextArea(destinationsText);
        destinationsTextArea.setEditable(false);
        destinationsTextArea.setBounds(70, 250, 400, 200);
        this.add(destinationsTextArea);

        preferencesTextArea = new JTextArea();
        preferencesTextArea.setBounds(150, 470, 200, 25);
        this.add(preferencesTextArea);

        JLabel inputLabel = new JLabel("Preferences:");
        inputLabel.setBounds(70, 470, 300, 25);
        this.add(inputLabel);

        inputErrorLabel = new JLabel();
        inputErrorLabel.setForeground(Color.RED);
        inputErrorLabel.setBounds(200, 500, 300, 25);
        this.add(inputErrorLabel);

        submitBtn = new JButton("Submit");
        submitBtn.setBounds(260, 550, 80, 30);
        this.add(submitBtn);

        submitBtn.addActionListener(new ActionListener() {
            @Override    public void actionPerformed(ActionEvent e) {
                String input = preferencesTextArea.getText();        String[] preferencesInput = input.split("[,\\s]+");
                if (preferencesInput.length != 5) {
                    inputErrorLabel.setText("Please enter exactly 5 preferences.");
                }
                else {
                    // check if input is correct(1-10) and unique
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
                        try {
                            List<Assignment> assignments = client.sendPreferencesAndGetResults(student);
                            ResultsFrame resultsFrame = new ResultsFrame(assignments);
                            resultsFrame.setVisible(true);
                            dispose();
                        } catch (IOException | ClassNotFoundException ex) {
                            ex.printStackTrace();
                            inputErrorLabel.setText("Error sending preferences: " + ex.getMessage());
                        }
                    }
                }
            }
        });

        this.setVisible(true);
    }
}
