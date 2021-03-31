package main;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
public class LoginWindow implements UIWindow {
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

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_DOWN_MASK,false),"EXIT");
        frame.getRootPane().getActionMap().put("EXIT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
                frame.dispose();
            }
        });


        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean fieldsCompleted = validate();
                if(fieldsCompleted) {
                    try {

                        client.setId(idInput.getText());

                        if (portInRange(serverPortInput.getText()) && portInRange(clientPortInput.getText())){
                            client.setServerPort(serverPortInput.getText());
                            client.setClientPort(clientPortInput.getText());
                        } else {
                            portOutOfRangeWarning();
                        }
                        client.setServerAddress(serverIPInput.getText());
                        client.setClientAddress(clientIPInput.getText());
                        // Attempt to connect to the server using the supplied information
                        client.connectToServer();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    fieldEmptyWarning();
                }


            }
        });
    }
    public void openWindow(){
            frame.setContentPane(getPanel());
            frame.pack();
            frame.setLocationRelativeTo(null); // centres the window
            frame.setResizable(false);
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

    public boolean portInRange(String portNumber){
        try {
            int port = Integer.valueOf(portNumber);
            return port >= 1024 && port <= 65535;
        } catch (NumberFormatException nfe){
            System.out.println("You must enter a number");
            return false;
        }
    }

    public boolean validate(){
        int errorCount = 0;
        if (idInput.getText().length() == 0 || clientPortInput.getText().length() == 0
        || clientIPInput.getText().length() == 0 || serverIPInput.getText().length() == 0 ||
                serverPortInput.getText().length() == 0) {
            errorCount++;
        }

        return errorCount == 0;
    }

    public void portOutOfRangeWarning(){
        JOptionPane.showMessageDialog(frame,
                "You have chosen an invalid port number.\nMust be within range 1024-65535",
                "Port is not in valid range",
                JOptionPane.WARNING_MESSAGE);
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

    public void serverIPNotFoundWarning(){
        JOptionPane.showMessageDialog(frame,
                "Incorrect Server IP",
                "Check server IP is correct",
                JOptionPane.ERROR_MESSAGE);
    }

    public void incorrectServerPortWarning(){
        JOptionPane.showMessageDialog(frame,
                "Incorrect server port",
                "Check server port",
                JOptionPane.ERROR_MESSAGE);
    }

    public void userNameTakenWarning(){
        JOptionPane.showMessageDialog(frame,
                "Username already in use",
                "Username taken",
                JOptionPane.ERROR_MESSAGE);
    }

    public void serverIssueWarning(){
        JOptionPane.showMessageDialog(frame,
                "Server not running or incorrect server address supplied",
                "Check Server address",
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
