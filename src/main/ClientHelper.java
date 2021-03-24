package main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHelper extends Thread {
    private Client client;
    private Socket socket;
    private PrintWriter out;
    private Scanner in;
    private String userInputStream = "";
    private String userList;
    private ArrayList<String> memberList;
    private boolean isConnected = false;


    public ClientHelper(){
        super.start();
    }

    public void run(){
        try {
            client = new Client(this);
            while(true) {
                if (getConnected()) {
                    userInputStream = in.nextLine();
                    System.out.println("Result of userInputStream = " + userInputStream);

                    while (userInputStream != null) {
                        if (userInputStream.equals("/COORDINATOR")){
                            String coordinatorInfo = in.nextLine();
                            String coordtrue = in.nextLine();
                            if (coordtrue.equals("/COORDINATORTRUE")){
                                client.setCoordinator(true);
                            }
                            break;
                        }
                        if (userInputStream.equals("/ALLUSERS")) {
                            String incomingListIDs = "";
                            incomingListIDs = in.nextLine();
                            System.out.println("incoming id = " + incomingListIDs);

                            while (incomingListIDs != null) {
                                memberList = createIDListFromInput(incomingListIDs);
                                client.UpdateGUIUsers(memberList);
                                System.out.println("The memberlist contents: " + memberList);
                                if (incomingListIDs.contains("/END")) {
                                    break;
                                } else {
                                    System.out.println("Somethings gone wrong");
                                    break;
                                }
                            }

                        } else if (userInputStream.equals("/SENDMESSAGE")) {
                            // Update ChatWindow textArea with message
                            String message = in.nextLine();
                            System.out.println("The received message from server is " + message);
                            client.UpdateChatWindow(client.getUserId(), message);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> createIDListFromInput(String receivedIDList){
        ArrayList<String> updatedList = new ArrayList<>();
        String[] parts = receivedIDList.split(",");
        for(String id : parts){
            if(!(id.equals("/END"))){
                updatedList.add(id);
            }
        }
        return updatedList;
    }


    public void sendMemberDetails() throws IOException {
        try {
            socket = new Socket();

            while (client.isConnected()) {
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("SUBMITUSERLIST");
                userList = client.getClientAddress() + "," + client.getClientPort();
                out.println(userList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> getAllMembers() throws IOException {
        in = new Scanner(socket.getInputStream());
        memberList = new ArrayList<>();
        return memberList;
    }

    public void receiveMessage(){
        String message = in.nextLine();
        client.UpdateChatWindow(client.getUserId(), message);
    }

    public synchronized boolean getConnected(){
        return this.isConnected;
    }

    public synchronized void setConnected(boolean value){
        this.isConnected = value;
    }

    public synchronized Scanner getInputStream(){
        return this.in;
    }

    public synchronized void setInputStream(Scanner inputStream){
        this.in = inputStream;
    }


}
