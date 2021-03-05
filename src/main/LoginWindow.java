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


    public LoginWindow(Client client) {
        this.client = client;


        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean fieldsCompleted = validate();
                if(fieldsCompleted) {
                    try {
                        client.setServerPort(serverPortInput.getText());
                        client.setServerAddress(serverIPInput.getText());
                        client.setClientPort(clientPortInput.getText());
                        client.setClientAddress(clientIPInput.getText());
                        client.connectToServer();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    fieldEmptyWarning();
                }
frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

    /* Error checking and warning dialogs*/

    public boolean validate(){
        int errorCount = 0;
        if (idInput.getText().length() == 0 || clientPortInput.getText().length() == 0
        || clientIPInput.getText().length() == 0 || serverIPInput.getText().length() == 0 ||
                serverPortInput.getText().length() == 0) {
            errorCount++;
        }

        return errorCount == 0;
    }

    public void clientAddressWarning(){
        JOptionPane.showMessageDialog(frame,
                "Client Address not found on this machine. \nTry\n" + this.client.getListOfLocalAddresses(),
                "Please check your IP address",
                JOptionPane.WARNING_MESSAGE);
    }

    public void fieldEmptyWarning(){
        JOptionPane.showMessageDialog(frame,
                "All fields must be completed",
                "Field Empty",
                JOptionPane.ERROR_MESSAGE);
    }

    public void serverNotFoundWarning(){
        JOptionPane.showMessageDialog(frame,
                "Incorrect Server IP or Port",
                "Check server IP and port are correct",
                JOptionPane.ERROR_MESSAGE);
    }
}

// Former Test Code for LoginWindow
// public static void main(String[] args) {
// JFrame frame = new JFrame("App");
// frame.setContentPane(new LoginGUI().getPanel());
// frame.pack();
// frame.setVisible(true);
// }
