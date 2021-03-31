package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ServerClientHandler extends Thread {
    /**
     * Stores the connection information for each individual client connection
     * Including ip, port, user id and connection state.
     */
    private ServerConnectionHandler connectionToHandler;
    private Socket connectionSocket;
    private String id = null;
    private Scanner in;
    private PrintWriter out;
    private ConnectionState state;
    private String message;
    private String recipient;
    private String currentClientID;
    private Integer currentClientPort;
    private String currentClientIP = "";
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
                                invalidID();
                            } else {
                                validID();
                                break;
                            }
                        } // ID has been accepted
                    }

                    String messageInputStream = in.nextLine();


                    while(messageInputStream != null){
                        if(messageInputStream.equals("/SENDMESSAGE")){
                            this.recipient = in.nextLine();
                            this.message = in.nextLine();
                            String sender = in.nextLine();
                            connectionToHandler.sendMessageToUser(message, recipient, sender);
                            break;
                        } else if (messageInputStream.equals("/USERQUIT")){
                            String clientQuitting = in.nextLine();
                            this.state.onQuit();
                            // If client is the coordinator, set a new one
                            connectionToHandler.removeFromClientList(clientQuitting);

                            if (clientQuitting.equals(this.connectionToHandler.getCoordinatorObject().getId())){
                                this.connectionToHandler.changeCoordinator(clientQuitting);
                                break;
                            }

                        } else if (messageInputStream.equals("/PING")){
                            connectionToHandler.pingAllClients();
                            break;
                        } else if (messageInputStream.contains("/PONG")){
                            connectionToHandler.pongCoordinator(messageInputStream);
                            break;
                        } else if (messageInputStream.contains("/DISCONNECT")){
                            String parts[] = messageInputStream.split(":");
                            String pid = parts[1];
                            changeState(new DisconnectedState(this));
                            this.connectionToHandler.getConnection().close();
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

    public void invalidID() throws IOException {
        /**
         * Sends a message to the client informing them that the ID is taken
         * Closes the socket so that the port can be reused
         */
        out.println("INVALIDID");
        connectionSocket.close();
    }

    public void validID(){
        /**
         * Informs the client that the id has been accepted and adds the client to the list of current connections in the connection handler class.
         * This also informs all connected clients that this user has joined the chatroom.
         * Sets the user to be the coordinator if the chatroom is currently empty
         * Sends this coordinator information to the newly connected client.
         */
        out.println("/IDACCEPTED");
        System.out.println("ID " + this.id + " accepted");
        currentClientID = this.id;
        currentClientPort = this.connectionSocket.getPort();
        currentClientIP = this.connectionSocket.getInetAddress().getHostAddress();
        if(this.connectionToHandler.listOfUsers().size() == 0){
            setTheCoordinator(currentClientID, currentClientPort, currentClientIP);
        }

        connectionToHandler.addToClientList(this.id, this);
        sendCoordinatorDetails();
    }

    public void sendCoordinatorDetails(){
        /**
         * Sends a command instructing the client that coordinator details are being sent
         */
        out.println("/SENDCOORDINATORDETAILS");
        Coordinator coordDetails = connectionToHandler.getCoordinatorObject();
        out.println(coordDetails.getId() + ":" + coordDetails.getPort() + ":" + coordDetails.getIp());
    }

    public void notifyUsers(ArrayList<String> userList){
        /**
         * Sends a list of all current users over the network
         * @param userList a list of all currently connected users given by the ServerConnectionHandler
         */
        String idlist = "";
        out.println("/ALLUSERS");
        for (String id : userList){
            idlist += id + ",";
        }

        out.println(idlist + "/END");
    }
    public void setTheCoordinator(String id, int port, String ip){
        /**
         * Sends the coordinator information to client and informs the client to set themselves as the coordinator.
         */
        out.println("/COORDINATOR");
        connectionToHandler.getCoordinatorObject().setId(id);
        connectionToHandler.getCoordinatorObject().setPort(port);
        connectionToHandler.getCoordinatorObject().setIp(ip);

        out.println(id + ":" + port + ":" + ip);

        out.println("/COORDINATORTRUE");
        isCoordinator = true;
    }


    public boolean getCoordinator(){
        /**
         * Returns a boolean relating to whether this client is the coordinator
         * @return Whether the client being handled is the coordinator
         */
        return this.isCoordinator;
    }

    public int getCurrentClientPort() {
        /**
         * Returns the client port for the client being handled in this thread
         * @return The client port
         */
        return currentClientPort;
    }

    public String getCurrentClientIP() {
        /**
         * Returns the client ip for the client being handled in this thread
         * @return The client ip
         */
        return currentClientIP;
    }

    public void sendMessage(String message) {
        /**
         * Sends a command to a client instructing them that a message is being sent and the next inputStream will be the message.
         * @param message The message being sent to the client
         */
        out.println("/RECEIVEMESSAGE");
        out.println(message);
    }

    public void sendPingToClient(){
        /**
         * Sends a /PING message to clients
         * Connected clients are expected to reply to the server with a /PONG command
         */
        out.println("/PING");
    }

    public void sendPongToCoordinator(String inputStream){
        /**
         * Sends a command to the current coordinator, informing them of a clients reply to the servers PING
         * Takes an inputStream from the client with the command and users id to forward to the coordinator
         * @param inputStream An inputStream containing a PONG and the users id in the format /PONG:userid
         *
         */
        out.println(inputStream);
    }

    public void setID(String id) {
        /**
         * Sets the user id for the current client
         * @param id The user id of the client
         */
        this.id = id;
    }

    public String getID() {
        /**
         * Returns the user of the current client
         * @return The user id
         */
        return this.id;
    }


    public void changeState(ConnectionState state) {
        /**
         * Switches the connection state to the one defined
         * @param state The state to switch to, e.g connected or disconnected.
         */
        this.state = state;
    }

    public ConnectionState getConnectionState() {
        /**
         * Returns the current connection state for a client
         * @return The current connection state
         */
        return state;
    }

    public void sendRemoveCommand(String previousCoordinatorID){
        /**
         * Sends a message to the coordinator informing them to remove them from their list of users.
         */
        out.println("/REMOVEHASH" + ":" + previousCoordinatorID);
    }
}
