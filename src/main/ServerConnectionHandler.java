package main;

import java.io.IOException;
import java.io.PrintWriter;
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


    public ServerConnectionHandler(String serverIP, int serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.clientList = new HashMap<>();
        super.start();
    }
    
    public void addToClientList(String ID, ServerClientHandler clientConnection){
        this.clientList.put(ID,clientConnection);
        notifyClients();
    }


    @Override
    public void run(){
        while(true){
            try {
                serverSocket = new ServerSocket(this.serverPort);
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

    public HashMap<String, ServerClientHandler> listOfUsers(){
        return clientList;
    }

    public void sendMessageToUser(String message, String recipient){
        if (listOfUsers().containsKey(recipient)){
            listOfUsers().get(recipient).sendMessage(message);
        }
    }

    public void notifyClients(){
        ArrayList<String> listOfCurrentUsers = new ArrayList<>(clientList.keySet());
        Collections.sort(listOfCurrentUsers);
        for(String id : clientList.keySet()){
            // Create a new thread to handle the client
            ServerClientHandler ch = clientList.get(id);
            // Pass the list of current users to each client
            ch.notifyUsers(listOfCurrentUsers);
        }
    }

//    public void notifyUsers(HashMap<String, ServerClientHandler> userList){
//        out.println("/ALLUSERS");
//        for(Map.Entry<String, ServerClientHandler> entry : userList.entrySet()){
//            String id = entry.getKey();
//            out.println(id);
//        }
//        out.println("/END");
//    }

}
