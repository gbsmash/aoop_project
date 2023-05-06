import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class MyFrame extends JFrame {

    JLabel title, universities, l1, l2, l3, l4;
    JFormattedTextField t1, t2, t3, t4;

    JButton submitBtn;

    public MyFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        title = new JLabel("Please choose 5 destinations");
        title.setBounds(70,70,300,100);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(title);

        universities = new JLabel();
        universities.setText("<html>1. University of Melbourne <br>2. Heidelberg University<br>3. Michigan State University<br>4. Brandeis University<br>5. Lund University<br>6. Texas Tech University<br>7. Stanford University <br>8. Louisiana State University<br>9. The University of Manchester<br>10. Columbia University</html>");
        universities.setBounds(100,100,300,300);
        universities.setBorder(new EmptyBorder(0,10,0,0));
        universities.setFont(new Font("Arial", Font.PLAIN, 18));
        this.add(universities);

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        integerFormat.setGroupingUsed(false);
        integerFormat.setMaximumIntegerDigits(2);

        NumberFormatter formatter = new NumberFormatter(integerFormat);
        formatter.setMinimum(1);
        formatter.setMaximum(10);

        l1 = new JLabel("1st:");
        l1.setBounds(70, 350, 100, 100);
        l1.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(l1);

        t1 = new JFormattedTextField(formatter);
        t1.setBounds(110, 390, 40, 25);
        this.add(t1);

        l2 = new JLabel("2nd:");
        l2.setBounds(70, 400, 100, 100);
        l2.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(l2);

        t2 = new JFormattedTextField(formatter);
        t2.setBounds(115, 440, 40, 25);
        this.add(t2);

        l3 = new JLabel("3rd:");
        l3.setBounds(70, 450, 100, 100);
        l3.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(l3);

        t3 = new JFormattedTextField(formatter);
        t3.setBounds(115, 490, 40, 25);
        this.add(t3);


        l4 = new JLabel("4th:");
        l4.setBounds(70, 490, 100, 100);
        l4.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(l4);

        t4 = new JFormattedTextField(formatter);
        t4.setBounds(115, 530, 40, 25);
        this.add(t4);


        submitBtn = new JButton("Submit");
        submitBtn.setBounds(70, 570, 80, 30);
        this.add(submitBtn);

        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the values from the JTextFields
                String value1 = t1.getText();
                String value2 = t2.getText();
                String value3 = t3.getText();
                String value4 = t4.getText();

                // Check if all fields are filled
                if (value1.isEmpty() || value2.isEmpty() || value3.isEmpty() || value4.isEmpty()) {
                    JOptionPane.showMessageDialog(MyFrame.this, "Please fill in all the fields.");
                } else {
                    // Send the values to the server using a network connection
                    // ...
                    System.out.println(value1 + " " + value2 + " " + value3 + " " + value4);
                }
            }
        });



        this.setVisible(true);
    }


    public static void main(String[] args) {
        MyFrame f = new MyFrame();
    }
}
