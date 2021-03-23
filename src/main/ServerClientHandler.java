package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerClientHandler extends Thread{
    private ServerConnectionHandler connectionToHandler;
    private Socket connectionSocket;
    private boolean isClientConnected = true;
    private String id = "";
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

    public ServerClientHandler(ServerConnectionHandler connectionToHandler, Socket connectionSocket) throws IOException {
        this.connectionToHandler = connectionToHandler;
        this.connectionSocket = connectionSocket;
        this.state = new ConnectedState(this);
        super.start();

    }

    public void sendMessage(String message){
        out.println("/SENDMESSAGE");
        out.println(message);
    }

    public void setID(String id){
        this.id = id;
    }

    public String getID(){
        return this.id;
    }

    public void sendCurrentUserList(){
        out.println("SUBMITUSERLIST");

    }

    public void changeState(ConnectionState state){
        this.state = state;
    }

    public ConnectionState getConnectionState(){
        return state;
    }

    @Override
    public void run()  {

        try {
            in = new Scanner(connectionSocket.getInputStream());
            out = new PrintWriter(connectionSocket.getOutputStream(), true);
            while(true) {
                if (state.toString().equals("Connected")) {
                    this.id = in.nextLine();
                    if (id == null) {
                        System.out.println("Id was empty. Something has gone wrong.");
                    } else {
                        System.out.println("Checking ID " + this.id);
                        if (this.connectionToHandler.listOfUsers().containsKey(id)) {
                            // ID already exists
                            out.println("INVALIDID");
                            connectionSocket.close();
                            System.out.println("ID " + this.id + " is already taken.");
                        } else {
                            // ID is free for use. Add ID to current users
                            out.println("IDACCEPTED");
                            System.out.println("ID accepted");
                            if(this.connectionToHandler.listOfUsers().size() == 0){
                                coordinatorID = this.id;
                                coordinatorPort = this.connectionSocket.getPort();
                                coordinatorIP = this.connectionSocket.getInetAddress().getHostAddress();
                                setTheCoordinator();
                            }
                            this.connectionToHandler.addToClientList(this.id, this);
                            break;
                        }
                    }
                } else {
                    System.out.println("Client is not connected.");
                }
            } // Checking username loop ends here

            String userInputStream = in.nextLine();
            while(userInputStream != null){
                if(userInputStream.equals("/SENDMESSAGE")){
                    this.recipient = in.nextLine();
                    this.message = in.nextLine();
                    connectionToHandler.sendMessageToUser(message, recipient);
                    break;
                }

            }

        }


        catch (Exception e){
            e.getMessage();
        }

    }

    public void notifyUsers(ArrayList<String> userList){
        out.println("/ALLUSERS");
        for (String id : userList){
            out.println(id);
        }
        out.println("/END");
    }
    public void setTheCoordinator(){
        out.println("/COORDINATOR");
        out.println(coordinatorID + ":" + coordinatorPort + ":" + coordinatorIP);
        sendCoordinatorMessage();
        isCoordinator = true;
    }

    public void sendCoordinatorMessage(){
        out.println("/COORDINATORTRUE");
    }

    public boolean getCoordinator(){
        return this.isCoordinator;
    }

}
