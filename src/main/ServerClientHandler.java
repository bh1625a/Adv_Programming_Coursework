package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ServerClientHandler extends Thread {
    private ServerConnectionHandler connectionToHandler;
    private Socket connectionSocket;
    private boolean isClientConnected = true;
    private String id = null;
    private Scanner in;
    private PrintWriter out;
    private ConnectionState state;
    private Client client;
    private String message;
    private String recipient;
    private String coordinatorID;
    private Integer coordinatorPort;
    private String coordinatorIP = "";
    private ServerClientHandler serverClientHandler;
    private boolean isCoordinator = false;
    private String userListResponse = "";

    public ServerClientHandler(ServerConnectionHandler connectionToHandler, Socket connectionSocket) throws IOException {
        this.connectionToHandler = connectionToHandler;
        this.connectionSocket = connectionSocket;
        this.state = new ConnectedState(this);
        in = new Scanner(connectionSocket.getInputStream());
        out = new PrintWriter(connectionSocket.getOutputStream(), true);
        super.start();
    }

    public void sendMessage(String message) {
        System.out.println("Sending message: " + message);
        out.println("/SENDMESSAGE");
        out.println(message);
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
    }

    public void sendCurrentUserList() {
        out.println("SUBMITUSERLIST");

    }

    public void changeState(ConnectionState state) {
        this.state = state;
    }

    public ConnectionState getConnectionState() {
        return state;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (state.toString().equals("Connected")) {
                    if (this.id == null) {
                        System.out.println("ID empty");
                        this.id = in.nextLine();
                        System.out.println("this id = " + this.id);

                        while (id != null) {
                            System.out.println("Checking ID supplied: " + this.id);
                            if (this.connectionToHandler.listOfUsers().containsKey(id)) {
                                // ID already exists
                                out.println("INVALIDID");
                                connectionSocket.close();
                                System.out.println("ID " + this.id + " is already taken.");
                            } else {
                                out.println("/IDACCEPTED");
                                System.out.println("ID " + this.id + " accepted");
                                connectionToHandler.addToClientList(this.id, this);
                                break;
                            }
                        } // ID has been accepted
                    }

                    String messageInputStream = in.nextLine();

                    while(messageInputStream != null){
                        System.out.println("ServerSide messageInputStream: " + messageInputStream);
                        if(messageInputStream.equals("/SENDMESSAGE")){
                            this.recipient = in.nextLine();
                            this.message = in.nextLine();
                            String sender = in.nextLine();
                            connectionToHandler.sendMessageToUser(message, recipient, sender);
                            break;
                        }
                    }


                }
            } catch (Exception e) {
                e.getMessage();
            }

        } // while true ends here

    }

    public void notifyUsers(ArrayList<String> userList){
        String idlist = "";
        out.println("/ALLUSERS");
        System.out.println("Contents of userlist: " + userList);
        for (String id : userList){
            idlist += id + ",";
        }
        out.println(idlist + "/END");
    }
    public void setTheCoordinator(){
        out.println("/COORDINATOR");
        out.println(coordinatorID + ":" + coordinatorPort + ":" + coordinatorIP);
        out.println("/COORDINATORTRUE");
        isCoordinator = true;
    }

    public void sendCoordinatorMessage(){
        out.println("/COORDINATORTRUE");
    }

    public boolean getCoordinator(){
        return this.isCoordinator;
    }



}
