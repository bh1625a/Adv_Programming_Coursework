package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private String id;

    private Scanner in;
    private PrintWriter out;




    public Client(ClientHelper clientHelper) {
        super.start();
        this.clientHelper = clientHelper;
        listOfLocalAddresses = new ArrayList<>();

    }

    public void run(){
        openLoginWindow();
        openChatWindow();

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
        chatWindow = new ChatWindow(this);
        chatWindow.openWindow();

    }

    public void connectToServer() throws IOException {
        createListOfLocalAddresses();
        try {
            if (checkClientIpAvailable(this.getClientAddress())) {
                socket = new Socket(this.getServerAddress(), this.getServerPort(), InetAddress.getByName(this.getClientAddress()), this.getClientPort());
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(),true);
                out.println(this.id);
                if (in.nextLine().equals("IDACCEPTED")) {
                    this.isConnected = true;
                    clientHelper.setConnected(true);
                    clientHelper.setInputStream(in);
                    this.chatWindow.makeMessageAvailable();
                    closeLoginWindow();
                } else {
                    loginWindow.userNameTakenWarning();
                    socket.close();
                    return;
                }
            } else {
                loginWindow.clientAddressWarning();
            }

        } catch (ConnectException cx){
            loginWindow.incorrectServerPortWarning();
        } catch (SocketException se){
            loginWindow.debuggingWarning();
        } catch (UnknownHostException uhe){
            loginWindow.serverIPNotFoundWarning();
        }
    }

    public void sendMessage(String message, String recipient){
        this.out.println("/SENDMESSAGE");
        this.out.println(recipient);
        this.out.println(message);
    }

    public ArrayList<String> getListOfLocalAddresses(){
        return this.listOfLocalAddresses;
    }

    public synchronized void setIsConnected(boolean value){
        this.isConnected = value;
    }

    public synchronized boolean isConnected(){
        return isConnected;
    }

    public void connectionStatusChange(ConnectionState status){

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

    public void UpdateChatWindow(String id, String message){
        // Tells GUI to display message from id
        String updatedMessage = id + ": " + message;
        chatWindow.updateDisplay(updatedMessage);
    }

    public void UpdateGUIUsers(ArrayList<String> userList) throws IOException {
        chatWindow.displayMembers(userList);

    }

    public synchronized void setId(String id){
        this.id = id;
    }

    public String getUserId(){
        return this.id;
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

    public void setCoordinator(boolean value){
        this.isCoordinator = value;
    }


}