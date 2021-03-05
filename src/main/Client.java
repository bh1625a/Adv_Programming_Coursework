package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

public class Client extends Thread {
    private LoginWindow loginWindow;
    private ChatWindow chatWindow;
    private Socket socket;
    private ClientHelper clientHelper;
    private boolean isConnected = false;
    private boolean isCoordinator = false;
    private ArrayList<String> listOfLocalAddresses;

    private int serverPort;
    private String serverAddress;
    private int clientPort;
    private String clientAddress;


    private Scanner in;
    private PrintWriter out;


    public Client(ClientHelper clientHelper) {
        super.start();
        this.clientHelper = clientHelper;
        listOfLocalAddresses = new ArrayList<>();

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
        createListOfLocalAddresses();
        try {
            if (checkClientIpAvailable(this.getClientAddress())) {
                socket = new Socket(this.getServerAddress(), this.getServerPort(), InetAddress.getByName(this.getClientAddress()), this.getClientPort());
                out = new PrintWriter(socket.getOutputStream());
                in = new Scanner(socket.getInputStream());
                setIsConnected(true);
                closeLoginWindow();
            } else {
                loginWindow.clientAddressWarning();
            }
        } catch (ConnectException cx){
            loginWindow.serverNotFoundWarning();
        } catch (SocketException se){
            loginWindow.serverNotFoundWarning();
        }
    }

    public ArrayList<String> getListOfLocalAddresses(){
        return this.listOfLocalAddresses;
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

    public void createListOfLocalAddresses() throws SocketException {
        /* Creates a list of local addresses found on the
        machine by adding to the listOfLocalAddresses arraylist.
         */
        this.listOfLocalAddresses.clear();
        this.listOfLocalAddresses.add("localhost");
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                String hostAddress = i.getHostAddress();
                if (hostAddress.startsWith("1")){
                    this.listOfLocalAddresses.add(hostAddress);
            }
            }
        }
    }


    public boolean checkClientIpAvailable(String ip){
        return this.listOfLocalAddresses.contains(ip);
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
