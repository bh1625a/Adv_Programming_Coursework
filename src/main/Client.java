package main;

import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {
    private LoginWindow loginWindow;
    private ChatWindow chatWindow;
    private Socket socket;
    private ClientHelper clientHelper;
    private boolean isConnected = false;

    private int serverPort;
    private String serverAddress;


    public Client(ClientHelper clientHelper){
        super.start();
        this.clientHelper = clientHelper;
    }

    public void run(){

    }

    public void openLoginWindow(){

    }

    public void openChatWindow(){

    }

    public void connectToServer(){

    }

    public void setIsConnected(){

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

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }
}
