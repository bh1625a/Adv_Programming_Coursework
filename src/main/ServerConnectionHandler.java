package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerConnectionHandler extends Thread {
    private int serverPort;
    private String serverIP;
    private ServerSocket serverSocket;
    private Socket connection;
    private Scanner in;
    private PrintWriter out;
    private HashMap<String, ServerClientHandler> clientList;
    private ArrayList<String> idList = new ArrayList<>();
    private Coordinator coordinator;


    public ServerConnectionHandler(String serverIP, int serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.clientList = new HashMap<>();
        coordinator = new Coordinator();
        super.start();
    }


    @Override
    public void run(){
        while(true){
            try {
                serverSocket = new ServerSocket(this.serverPort);
                System.out.println(Inet4Address.getLocalHost().getHostAddress());


                connection = serverSocket.accept(); // Get connection from client
                String clientIP = connection.getInetAddress().getHostName();
                int clientPort = connection.getPort();

                System.out.println("New Client IP: " + clientIP + ", Client Port: " + clientPort);

                // Deals with a new connection by creating a new thread
                ServerClientHandler initiateConnection = new ServerClientHandler(this, connection);


            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public synchronized void addToClientList(String ID, ServerClientHandler clientConnection){
        /**
         * Adds a client to the clientList hashmap.
         * @param ID The id of the client / user
         * @param clientConnection A ServerClientHandler containing information about the client
         */
        this.clientList.put(ID,clientConnection);
        notifyClients();
    }

    public synchronized void removeFromClientList(String ID){
        /**
         * Removes the specified ID from the clientList hashmap
         * @param ID The id of the client / user to be removed
         */
        this.clientList.remove(ID);
        notifyClients();
    }

    public HashMap<String, ServerClientHandler> listOfUsers(){
        /**
         * Returns the hashmap list of users, with their ID and connection information
         * @return The clientList hashmap
         */
        return clientList;
    }

    public void sendMessageToUser(String message, String recipient, String sender){
        /**
         * Sends a message received from a client to a recipient
         * @param message The contents of the message to be sent
         * @param recipient The intended recipient of the message (id)
         * @param sender The id that has sent the message
         */
        String parts[] = recipient.split(":");
        recipient = parts[0];

        if (clientList.containsKey(recipient)){
            clientList.get(recipient).sendMessage(sender + ": " + message);
        }
    }

    public void pingAllClients(){
        /**
         * Goes through the list of clients in the clientList hashmap.
         * Uses the sendPingToClient method from ServerClientHandler to send a "/PING" message to all users in the list
         */
        for (String id : clientList.keySet()){
            clientList.get(id).sendPingToClient();
        }
    }

    public void pongCoordinator(String messageStream){
        /**
         * Goes through all connected users and checks if they are the coordinator.
         * Utilizes the ServerClientHandlers sendPongToCoordinator method to send a "/PONG" to the coordinator
         * @param messageStream The message stream contains "/PONG" and the id of the client that has returned the pong
         */
        for (String id : clientList.keySet()){
            if (clientList.get(id).getCoordinator()) {
                ServerClientHandler clientHandler = clientList.get(id);
                clientHandler.sendPongToCoordinator(messageStream);
            }
        }
    }

    public void notifyClients() {
        /**
         * Builds and sorts an idList that can be sent to each client via the ServerClientHandler
         * Creates a new user list with each call
         * Goes through each id in the clientList hashmap using the information provided to notify users of all connections
         */
        createUserList();
        Collections.sort(idList);
        for (String id: clientList.keySet()){
            ServerClientHandler clientHandler = clientList.get(id);
            clientHandler.notifyUsers(idList);
        }
    }


    public void createUserList(){
        /**
         * Creates an idList containing the id, port and ip of each currently connected client.
         * Clears the list to prevent duplicates.
         */
        idList.clear();
        for (String id : clientList.keySet()){
            ServerClientHandler clientHandler = clientList.get(id);
            idList.add(clientHandler.getID() + ":" + clientHandler.getCurrentClientPort() + ":" + clientHandler.getCurrentClientIP());
        }
    }

    public Socket getConnection(){
        /**
         * Returns the sockets connection information
         * @return this.connection The socket connection information
         */
        return this.connection;
    }

    public Coordinator getCoordinatorObject(){
        /**
         * Returns the coordinator object
         * @return this.coordinator A coordinator object containing information about the current coordinator.
         */
        return this.coordinator;
    }

}
