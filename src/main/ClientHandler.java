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
    private boolean isServerRunning = true;
    private String id;
    private Scanner in;
    private PrintWriter out;

    public ClientHandler(ConnectionHandler connectionToHandler, Socket connectionSocket) throws IOException {
        this.connectionToHandler = connectionToHandler;
        this.connectionSocket = connectionSocket;
        super.start();
        in = new Scanner(connectionSocket.getInputStream());
        out = new PrintWriter(connectionSocket.getOutputStream());
    }

    public void sendMessage(){

    }

    public void setID(String id){
        this.id = id;
    }

    public String getID(){
        return this.id;
    }

    public void sendCurrentUserList(){

    }

    @Override
    public void run()  {
        while(isServerRunning){


        }

    }
}
