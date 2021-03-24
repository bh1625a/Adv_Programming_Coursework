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
    private ArrayList<String> idList = new ArrayList<>();


    public ServerConnectionHandler(String serverIP, int serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.clientList = new HashMap<>();
        super.start();
    }
    
    public synchronized void addToClientList(String ID, ServerClientHandler clientConnection){
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

    public void sendMessageToUser(String message, String recipient, String sender){
        if (clientList.containsKey(recipient)){
            clientList.get(recipient).sendMessage(sender + ": " + message);
        }
    }

    public void notifyClients() {
        createUserList();
        Collections.sort(idList);
        for (String id: clientList.keySet()){
            ServerClientHandler clientHandler = clientList.get(id);
            clientHandler.notifyUsers(idList);
        }
    }


    public void createUserList(){
        idList.clear();
        for (String id : clientList.keySet()){
            ServerClientHandler clientHandler = clientList.get(id);
            idList.add(clientHandler.getID());
        }
    }


}
