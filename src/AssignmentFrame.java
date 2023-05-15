package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AssignmentFrame extends JFrame {
    JLabel title1;

    public AssignmentFrame(List<Assignment> bestAssignment){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 650);
        this.getContentPane().setBackground(Color.decode("#F2D5F8"));
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        title1 = new JLabel("Current assignments:");
        title1.setBounds(260, 45, 400, 100);
        title1.setFont(new Font("Serif", Font.BOLD, 30));
        this.add(title1);

        String[] columnNames = {"Student", "Destination"};

        if(bestAssignment != null) {
            Object[][] data = new Object[bestAssignment.size()][2];
            for (int i = 0; i < bestAssignment.size(); i++) {
                data[i][0] = bestAssignment.get(i).getStudent().getName();
                data[i][1] = bestAssignment.get(i).getDestination().getName();
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable table = new JTable(model);
            table.setBounds(100, 150, 600, 350);
            table.setFont(new Font("Arial", Font.PLAIN, 16));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
            table.setRowHeight(25);
            table.setFillsViewportHeight(true);
            table.setBackground(Color.decode("#E6C0E9"));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(100, 150, 600, 350);

            this.add(scrollPane);
        }
        this.setVisible(true);
    }
}