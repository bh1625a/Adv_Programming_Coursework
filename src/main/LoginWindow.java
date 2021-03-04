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

    public LoginWindow() {
    }

    public LoginWindow(Client client) {
        this.client = client;


        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.setServerPort(serverPortInput.getText());

            }
        });
    }

    public void openWindow(){
        JFrame frame = new JFrame("Login");
        frame.setContentPane(getPanel());
        frame.pack();
        frame.setVisible(true);

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
