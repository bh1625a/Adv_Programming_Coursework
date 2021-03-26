package main;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class ChatWindow extends Thread implements UIWindow{
    private JPanel mainPanel;
    private JTextField userInputField;
    private JButton sendMessageButton;
    private JPanel displayPanel;
    private JPanel userListPanel;
    private JList userListField;
    private JTextArea textArea;
    private JFrame frame = new JFrame("Chat");
    private DefaultListModel listModel;
    private String msg = "";
    private Client client;
    private ClientHelper clientHelper;
    private String disconnectedUser = "";



    public ChatWindow(Client client){
        this.client = client;
        listModel = new DefaultListModel();
        userListField.setModel(listModel);
        userInputField.setEditable(false);
        sendMessageButton.setEnabled(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userQuits();


        // on close window the close method is called
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                closeWindow();
            }
        });


        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_DOWN_MASK,false),"EXIT");
        frame.getRootPane().getActionMap().put("EXIT",new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
            //Ensure to add the function to close everything for the user.
                frame.dispose();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                userQuits();
            }
        });


        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String recipient = (String) userListField.getSelectedValue();
                    String messageContents = userInputField.getText();

                    if (!(userListField.isSelectionEmpty())) {
                        userInputField.setText("");
                        textArea.append("Me: " + messageContents + "\n");
                    } else {
                        noIDSelectedWarning();
                    }


                    client.sendMessage(messageContents, recipient);
                } catch (NullPointerException ne) {
                    ne.getMessage();
                }

            }
        });

        userInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = userInputField.getText();
                userInputField.setText("");
                textArea.append(input);
                textArea.append("\n");
            }
        });


    }



    @Override
    public void openWindow(){
        frame.setContentPane(getPanel());
        frame.pack();
        frame.setSize(500,500);
        frame.setVisible(true);
    }

    @Override
    public void closeWindow(){
        frame.setVisible(false);
        frame.dispose();
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    public void makeMessageAvailable(){
        // This makes it possible to send messages after a user has logged in.
        // Input fields are disabled prior to this
        this.userInputField.setEditable(true);
        this.sendMessageButton.setEnabled(true);
    }

    public void userQuits(){
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Call code to remove from list of active users
                // ADD CODE HERE
                client.userQuit();
                System.out.println("Closing window");
                e.getWindow().dispose();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            }
        });
    }

    public void updateMessageDisplay(String message){
        new Thread(() -> SwingUtilities.invokeLater(() -> {
//            String msg = "";
            msg += message + "\n";
            textArea.append(msg);
        })).start();



    }


    public void displayMembers(ArrayList<String> users) throws IOException {
        new Thread(() -> {
            listModel.removeAllElements();
            SwingUtilities.invokeLater(() -> {
                for (String id : users){
                    listModel.addElement(id);
                }
            });
        }).start();
    }

    public void displayFirstMember(){
        textArea.setText("You are the first client to join (and have been assigned as coordinator)\n");
    }

    public void displayCoordinatorInfo(String id, int port, String ip){
        textArea.append("Coordinator is : " + id + " port: " + port + " IP address: " + ip + "\n");
    }

    public void noIDSelectedWarning(){
        JOptionPane.showMessageDialog(frame,
                "You have to select an ID from the list",
                "No user selected",
                JOptionPane.ERROR_MESSAGE);
    }

}
