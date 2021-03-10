package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatWindow implements UIWindow {
    private JPanel mainPanel;
    private JTextField userInputField;
    private JButton sendMessageButton;
    private JPanel displayPanel;
    private JPanel userListPanel;
    private JList userListField;
    private JFrame frame = new JFrame("Chat");


    private Client client;

    public ChatWindow(Client client){
        this.client = client;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userQuits();

        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

    public void userQuits(){
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Call code to remove from list of active users
                // ADD CODE HERE
                System.out.println("Closing window");
                e.getWindow().dispose();

            }
        });
    }

    public void updateDisplay(){

    }

    public void displayMembers(){

    }


}
