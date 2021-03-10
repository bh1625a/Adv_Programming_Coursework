package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread{
    private ConnectionHandler connectionToHandler;
    private Socket connectionSocket;
    private boolean isClientConnected = true;
    private String id;
    private Scanner in;
    private PrintWriter out;
    private ConnectionState state;

    public ClientHandler(ConnectionHandler connectionToHandler, Socket connectionSocket) throws IOException {
        this.connectionToHandler = connectionToHandler;
        this.connectionSocket = connectionSocket;
        this.state = new ConnectedState(this);
        super.start();
        in = new Scanner(connectionSocket.getInputStream());
        out = new PrintWriter(connectionSocket.getOutputStream());
    }

    public void sendMessage(String message){
        out.println("MESSAGE");
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
        if(state.toString().equals("Connected")){
            System.out.println("Client is connected");
        } else {
            System.out.println("Client is not connected.");
        }

    }
}
