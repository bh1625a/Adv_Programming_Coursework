package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    private LoginWindow loginWindow;
    private ChatWindow chatWindow;
    private Socket socket;
    private ClientHelper clientHelper;
    private boolean isConnected = false;
    private boolean isCoordinator = false;

    private int serverPort;
    private String serverAddress;
    private int clientPort;
    private String clientAddress;


    private Scanner in;
    private PrintWriter out;


    public Client(ClientHelper clientHelper){
        super.start();
        this.clientHelper = clientHelper;
    }

    public void run(){
        openLoginWindow();

    }

    public void openLoginWindow(){
        /* This will open a Login Window GUI*/
        loginWindow = new LoginWindow(this);
        loginWindow.openWindow();
    }

    public void closeLoginWindow(){
        loginWindow.closeWindow();
    }

    public void openChatWindow(){
        /* This will open the Chat Window GUI*/

    }

    public void connectToServer() throws IOException {
        socket = new Socket(this.getServerAddress(), this.getServerPort(), InetAddress.getByName(this.getClientAddress()), this.getClientPort());
        out = new PrintWriter(socket.getOutputStream());
        in = new Scanner(socket.getInputStream());
        setIsConnected(true);
        closeLoginWindow();
    }

    public void setIsConnected(boolean value){
        isConnected = value;
    }

    public boolean isConnected(){
        return isConnected;
    }

    public void connectionStatusChange(){

    }

    public void sendGUIExit(){

    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = Integer.valueOf(serverPort);
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setClientAddress(String address){
        this.clientAddress = address;
    }

    public String getClientAddress(){
        return this.clientAddress;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = Integer.valueOf(clientPort);
    }

    public int getClientPort() {
        return clientPort;
    }
}
