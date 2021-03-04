package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionHandler extends Thread {
    private int serverPort;
    private String serverIP;
    private ServerSocket serverSocket;
    private Socket connection;
    private Scanner in;
    private PrintWriter out;

    public ConnectionHandler(String serverIP, int serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        super.start();
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


    public void parseMessage(){}
    public void notifyUsers(){}


}