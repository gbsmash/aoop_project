import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultsFrame extends JFrame {
    public ResultsFrame(List<Assignment> assignments) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        JTextArea resultsTextArea = new JTextArea();
        resultsTextArea.setEditable(false);

        String resultsText = "Current Assignments:\n";
        for (Assignment assignment : assignments) {
            resultsText += assignment.getStudent().getName() + " " + assignment.getStudent().getSurname() + "-----" + assignment.getDestination().getName() + "\n";
        }

        resultsTextArea.setText(resultsText);
        this.add(new JScrollPane(resultsTextArea), BorderLayout.CENTER);
    }
}
