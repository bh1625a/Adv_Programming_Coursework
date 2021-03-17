package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ConnectionHandler extends Thread {
    private int serverPort;
    private String serverIP;
    private ServerSocket serverSocket;
    private Socket connection;
    private Scanner in;
    private PrintWriter out;
    private HashMap<String, ClientHandler> clientList;


    public ConnectionHandler(String serverIP, int serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.clientList = new HashMap<String,ClientHandler>();
        super.start();
    }
    
    public void addToClientList(String ID, ClientHandler clientConnection){
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
                ClientHandler initiateConnection = new ClientHandler(this, connection);

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

    public HashMap<String, ClientHandler> listOfUsers(){
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
            ClientHandler ch = clientList.get(id);
            ch.notifyUsers(listOfCurrentUsers);
        }


    }

//    public void notifyUsers(HashMap<String, ClientHandler> userList){
//        out.println("/ALLUSERS");
//        for(Map.Entry<String, ClientHandler> entry : userList.entrySet()){
//            String id = entry.getKey();
//            out.println(id);
//        }
//        out.println("/END");
//    }

}
