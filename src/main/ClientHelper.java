package main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
            while (true){
                if(getConnected()) {
                    while (in.hasNextLine()) {
                        String inputStream = in.nextLine();
                        System.out.println(inputStream);
                        System.out.println("-----------------------------------------------------------");
                        if (inputStream.equals("/COORDINATOR")){
                            String coordinatorInfo = in.nextLine();
                            String coordValue = in.nextLine();
                            if (coordValue.equals("/COORDINATORTRUE")){
                                client.setCoordinator(true);
                                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                            }
                        } else if (inputStream.equals("/ALLUSERS")) {
                            String incomingID = in.nextLine();
                            System.out.println("IncomingID: " + incomingID);
                            memberList = createIDListFromInput(incomingID);
                            client.UpdateOnlineUsers(memberList);
                            if (incomingID.contains("/END")){
                                break;
                            }
                        } else if (inputStream.equals("/SENDMESSAGE")){
                            String message = in.nextLine();
                            System.out.println("message is " + message);
                        }
                    }
                    System.out.println("GOT OUTSIDE LOOP");
                    System.out.println();

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
