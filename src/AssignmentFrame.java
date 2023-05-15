package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AssignmentFrame extends JFrame {
    JLabel title1;
    JTable table;
    DefaultTableModel model;
    Server server;

    public AssignmentFrame(Server server) {
        this.server = server;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 650);
        this.getContentPane().setBackground(Color.decode("#F2D5F8"));
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        title1 = new JLabel("Current assignments:");
        title1.setBounds(260, 45, 400, 100);
        title1.setFont(new Font("Serif", Font.BOLD, 30));
        this.add(title1);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(600, 45, 100, 30);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });
        this.add(updateButton);

        String[] columnNames = {"Student", "Destination"};
        model = new DefaultTableModel(columnNames, 0);

        table = new JTable(model);
        table.setBounds(100, 150, 600, 350);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setBackground(Color.decode("#E6C0E9"));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(100, 150, 600, 350);

        this.add(scrollPane);

        this.setVisible(true);
        updateTable();
    }

    public void updateTable() {
        List<Assignment> bestAssignment = server.getAssignments();
        if (bestAssignment != null) {
            model.setRowCount(0); // Clear existing rows
            for (Assignment assignment : bestAssignment) {
                Object[] row = new Object[2];
                row[0] = assignment.getStudent().getName();
                row[1] = assignment.getDestination().getName();
                model.addRow(row);
            }
        }
    }
}




