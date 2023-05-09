import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class WaitingFrame extends JFrame {
    private Client client;

    public WaitingFrame(Client client) {
        this.client = client;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel waitingLabel = new JLabel("Please wait. Waiting for results from the server...");
        waitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(waitingLabel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }


}
