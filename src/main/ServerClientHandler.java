package main;

import java.io.IOException;
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
    private String currentClientID;
    private Integer currentClientPort;
    private String currentClientIP = "";
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


    @Override
    public void run() {
        while (true) {
            try {
                if (state.toString().equals("Connected")) {
                    if (this.id == null) {
                        this.id = in.nextLine();

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
                                currentClientID = this.id;
                                currentClientPort = this.connectionSocket.getPort();
                                currentClientIP = this.connectionSocket.getInetAddress().getHostAddress();

                                if(this.connectionToHandler.listOfUsers().size() == 0){
                                    setTheCoordinator();
                                }

                                connectionToHandler.addToClientList(this.id, this);
                                out.println("/SENDCOORDINATORDETAILS");
                                System.out.println(connectionToHandler.getCoordinatorObject().getId());
                                Coordinator coordDetails = connectionToHandler.getCoordinatorObject();
                                out.println(coordDetails.getId() + ":" + coordDetails.getPort() + ":" + coordDetails.getIp());

                                break;
                            }
                        } // ID has been accepted
                    }


                    String messageInputStream = in.nextLine();
                    System.out.println("INCOMING MESSAGE STREAM " + messageInputStream);

                    while(messageInputStream != null){
                        if(messageInputStream.equals("/SENDMESSAGE")){
                            this.recipient = in.nextLine();
                            this.message = in.nextLine();
                            String sender = in.nextLine();
                            connectionToHandler.sendMessageToUser(message, recipient, sender);
                            break;
                        } else if (messageInputStream.equals("/USERQUIT")){
                            String clientQuitting = in.nextLine();
                            System.out.println("Client quitting: " + clientQuitting);
                            connectionToHandler.removeFromClientList(clientQuitting);
                        } else if (messageInputStream.equals("/PING")){
                            connectionToHandler.pingAllClients();
                            break;
                        } else if (messageInputStream.contains("/PONG")){
                            connectionToHandler.pongCoordinator(messageInputStream);
                            break;
                        } else if (messageInputStream.contains("/DISCONNECT")){
                            String parts[] = messageInputStream.split(":");
                            String pid = parts[1];
                            System.out.println("ID in disconnect " + pid);
                            connectionToHandler.removeFromClientList(pid);
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
        connectionToHandler.getCoordinatorObject().setId(currentClientID);
        connectionToHandler.getCoordinatorObject().setPort(currentClientPort);
        connectionToHandler.getCoordinatorObject().setIp(currentClientIP);

        out.println(currentClientID + ":" + currentClientPort + ":" + currentClientIP);

        out.println("/COORDINATORTRUE");
        isCoordinator = true;
    }


    public boolean getCoordinator(){
        return this.isCoordinator;
    }

    public Integer getCurrentClientPort() {
        return currentClientPort;
    }

    public String getCurrentClientIP() {
        return currentClientIP;
    }

    public void sendMessage(String message) {
        System.out.println("Sending message: " + message);
        out.println("/RECEIVEMESSAGE");
        out.println(message);
    }

    public void sendPingToClient(){
        out.println("/PING");
    }

    public void sendPongToCoordinator(String inputStream){
        out.println(inputStream);
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
    }


    public void changeState(ConnectionState state) {
        this.state = state;
    }

    public ConnectionState getConnectionState() {
        return state;
    }
}
