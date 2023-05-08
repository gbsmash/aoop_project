import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

        // Display destinations
        String destinationsText = "";
        for (int i = 0; i < destinations.size(); i++) {
            destinationsText += destinations.get(i).getIndex() + ". " + destinations.get(i).getName() + "\n";
        }

        JTextArea destinationsTextArea = new JTextArea(destinationsText);
        destinationsTextArea.setEditable(false);
        destinationsTextArea.setBounds(70, 250, 400, 200);
        this.add(destinationsTextArea);

        // JTextArea for client preferences input
        preferencesTextArea = new JTextArea();
        preferencesTextArea.setBounds(150, 470, 200, 25);
        this.add(preferencesTextArea);

        JLabel inputLabel = new JLabel("Preferences:");
        inputLabel.setBounds(70, 470, 300, 25);
        this.add(inputLabel);

        // Error label
        inputErrorLabel = new JLabel();
        inputErrorLabel.setForeground(Color.RED);
        inputErrorLabel.setBounds(200, 500, 300, 25);
        this.add(inputErrorLabel);

        // Create a "Submit" button
        submitBtn = new JButton("Submit");
        submitBtn.setBounds(260, 550, 80, 30);
        this.add(submitBtn);

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = preferencesTextArea.getText();
                String[] preferencesInput = input.split("\\s+");

                if (preferencesInput.length != 5) {
                    inputErrorLabel.setText("Please enter exactly 5 preferences.");
                } else {
                    List<Destination> preferences = new ArrayList<>();
                    boolean validInput = true;

                    for (String preference : preferencesInput) {
                        try {
                            int index = Integer.parseInt(preference) - 1;
                            if (index < 0 || index >= destinations.size()) {
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
                        inputErrorLabel.setText("Enter valid numbers, 1-10");
                    } else {
                        inputErrorLabel.setText("");
                        student.setPreferences(preferences);

                    }
                }
            }
        });
//        t1 = new JTextArea();
//        t1.setEditable(false);
//        t1.setFont(new Font("Arial", Font.PLAIN, 18));
//        for (int i = 0; i < destinations.size(); i++) {
//            t1.append((i + 1) + ". " + destinations.get(i).getName() + "\n");
//        }
//        scrollPane = new JScrollPane(t1);
//        scrollPane.setBounds(70, 250, 600, 300);
//        this.add(scrollPane);

        // Add submit button or other UI components as needed

        this.setVisible(true);
    }
}
