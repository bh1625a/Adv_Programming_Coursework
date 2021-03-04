package main;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
public class LoginWindow {
    private JPanel loginPanel;
    private JTextField idInput;
    private JTextField clientPortInput;
    private JTextField serverPortInput;
    private JTextField serverIPInput;
    private JTextField clientIPInput;
    private JButton connectButton;
    private JPanel OuterPanel;
    private Client client;
    private JFrame frame = new JFrame("Login");

    public LoginWindow() {
    }

    public LoginWindow(Client client) {
        this.client = client;


        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.setServerPort(serverPortInput.getText());
                    client.setServerAddress(serverIPInput.getText());
                    client.setClientPort(clientPortInput.getText());
                    client.setClientAddress(clientIPInput.getText());
                    client.connectToServer();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
    }

    public void openWindow(){
        frame.setContentPane(getPanel());
        frame.pack();
        frame.setVisible(true);

    }

    public void closeWindow(){
        frame.setVisible(false);
        frame.dispose();
    }

    public JPanel getPanel() {
        return loginPanel;
    }
}
// public static void main(String[] args) {
// JFrame frame = new JFrame("App");
// frame.setContentPane(new LoginGUI().getPanel());
// frame.pack();
// frame.setVisible(true);
// }
