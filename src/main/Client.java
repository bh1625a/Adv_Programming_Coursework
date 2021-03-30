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
    private String id;

    private Scanner in;
    private PrintWriter out;




    public Client(ClientHelper clientHelper) {
        super.start();
        this.clientHelper = clientHelper;
        listOfLocalAddresses = new ArrayList<>();

    }

    public void run(){
        /**
         * Creates a new thread opening a login and chat window for the current client
         */
        openLoginWindow();
        openChatWindow();

    }

    public void openLoginWindow(){
        /**
         *  Creates a new login window passing the current client object
         *  Opens the window so that it can be interacted with
         *  */
        loginWindow = new LoginWindow(this);
        loginWindow.openWindow();
    }

    public void closeLoginWindow(){
        /**
         * Closes the current login window
         */
        loginWindow.closeWindow();
    }

    public void openChatWindow(){
        /**
         *  Creates a new chat window passing the current client objects information
         *  */
        chatWindow = new ChatWindow(this);
        chatWindow.openWindow();

    }

    public void connectToServer() throws IOException {
        /**
         * Attempts to make a connection to the server.
         * Requires that the user provides correct connection information
         * Creates a new socket connection with the information provided by the user
         * Closes the socket if the user selects an id that is taken so that the port does not get used.
         * Provides varying error messages dependent on the information supplied.
         */
        createListOfLocalAddresses();
        try {
            if (checkClientIpAvailable(this.getClientAddress())) {
                socket = new Socket(this.getServerAddress(), this.getServerPort(), InetAddress.getByName(this.getClientAddress()), this.getClientPort());
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(),true);
                out.println(this.id);
                if (in.nextLine().equals("/IDACCEPTED")) {
                    this.isConnected = true;
                    clientHelper.setConnected(true);
                    clientHelper.setInputStream(in);
                    this.chatWindow.makeMessageAvailable();
                    this.chatWindow.setWindowTitle(this.id);
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
        /**
         * Sends a message to the server with the intended recipient and the id of the sender
         * @param message The message contents
         * @param recipient The intended recipient of the message
         */
        String sender = this.getUserId();
        this.out.println("/SENDMESSAGE");
        this.out.println(recipient);
        this.out.println(message);
        this.out.println(sender);
    }

    public void sendClientPing() throws IOException {
        /**
         * Calls on the Coordinator object from clientHelper to send pings to each client to determine connection status
         * Periodically send pings from the coordinator to the server to be forwarded to each connected user.
         */
        clientHelper.getCoordinator().checkConnectionStatus();
    }

    public void sendClientPong(){
        /**
         * Sends a "/PONG" with the current users ID.
         * Forwarded to the server which will then use this information to determine connectivity.
         */
        this.out.println("/PONG" + ":" + this.getUserId());
    }



    public void userQuit(){
        /**
         * Sends a "/USERQUIT" message from the client to the server to inform the server that the client is quitting.
         */
        System.out.println("This users id: " + this.getUserId());
        this.out.println("/USERQUIT");
        this.out.println(this.getUserId());
    }

    public ArrayList<String> getListOfLocalAddresses(){
        return this.listOfLocalAddresses;
    }


    public void createListOfLocalAddresses() throws SocketException {
        /**
         * Creates a list of local addresses found on the machine by adding to the listOfLocalAddresses arraylist.
         * Uses an Enumeration object so that each Network interface address can be accessed
         * Adds each found host address to a list of local addresses
         *
         */
        this.listOfLocalAddresses.clear();
        // Used for debugging
//        this.listOfLocalAddresses.add("localhost");
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

    public void UpdateChatWindow(String message){
        /**
         * Calls the chatWindows updateMessageDisplay method to update the chat window with the supplied message
         * @param message The message to be added to the chat window
         */
        chatWindow.updateMessageDisplay(message);
    }

    public void UpdateOnlineUsers(ArrayList<String> userList) throws IOException {
        /**
         * Supplies the chatWindow GUI interface with a list of currently connected users
         * @userList The list of currently connected users id's
         */
        chatWindow.displayMembers(userList);

    }

    public void chatWindowFirstMember(){
        /**
         * Calls the displayFirstMember method to display a message to the user that they are the first connected user.
         */
        chatWindow.displayFirstMember();
    }

    public void updateCoordinatorDetails(String id, int port, String ip){
        /**
         * Passes the coordinator information to the chat window GUI so that it can be displayed for each connected client
         * @param id The id of the coordinator
         * @param port The coordinator's port information
         * @param ip The ip address of the current coordinator
         */
        chatWindow.displayCoordinatorInfo(id, port, ip);
    }


    public synchronized void setId(String id){
        /**
         * Sets the id of the current client
         * @param id The id supplied by the user
         *
         */
        this.id = id;
    }

    public String getUserId(){
        /**
         * Returns the id of the current client
         * @return this.id The id of the client
         */
        return this.id;
    }

    public boolean checkClientIpAvailable(String ip){
        /**
         * Checks if the ip address supplied is a valid address for the local machine
         * Returns a boolean if the address is valid
         * @param ip The ip address supplied the user to be checked
         * @return Whether the listOfLocalAddresses on the machine contains the ip supplied
         */
        return this.listOfLocalAddresses.contains(ip);
    }

    public void setServerAddress(String serverAddress) {
        /**
         * Allows a user interface to set the server address to one supplied by a user
         * @param serverAddress The address of the server to be set
         */
        this.serverAddress = serverAddress;
    }

    public String getServerAddress() {
        /**
         * Returns the address of the server
         * @return The servers address supplied by the user
         */
        return serverAddress;
    }

    public void setServerPort(String serverPort) {
        /**
         * Allows a user to set the server port via their user interface
         * @param serverPort The intended port to be connected to.
         */
        this.serverPort = Integer.valueOf(serverPort);
    }

    public int getServerPort() {
        /**
         * Returns the port information supplied by the user
         * @return The port that the user wishes to connect to
         */
        return serverPort;
    }

    public void setClientAddress(String address){
        /**
         * Sets the client IP address
         * @param address The ip address supplied
         */
        this.clientAddress = address;
    }

    public String getClientAddress(){
        /**
         * Returns the address supplied by the user to be used as the client address
         * @return this.clientAddress The client IP address
         */
        return this.clientAddress;
    }

    public void setClientPort(String clientPort) {
        /**
         * Sets the client port
         * @param clientPort The client port
         */
        this.clientPort = Integer.valueOf(clientPort);
    }

    public int getClientPort() {
        /**
         * Returns the port supplied by the user to be used as the client port
         * @return An integer value of the client port
         */
        return clientPort;
    }

    public void setCoordinator(boolean value){
        /**
         * Allows the current client to be set as the coordinator
         * @param value A boolean value depending on whether this client should be set as the coordinator
         */
        this.isCoordinator = value;
    }

    public boolean isTheCoordinator(){
        return isCoordinator;
    }

    public Socket getSocketInformation(){
        /**
         * Returns a Socket object that has the current connection information
         * @return this.socket The socket connection
         */
        return this.socket;
    }


}