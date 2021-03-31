package main;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TimerTask;

public class Coordinator {
    private String id;
    private int port;
    private String ip;
    private ClientHelper clientHelper;
    private Client client;
    private Socket socket;
    private PrintWriter out;
    HashMap<String, Integer> counterMap;
    ArrayList<String> ids;


    public Coordinator(){

    }

    public Coordinator(ClientHelper clientHelper, Client client){
        this.clientHelper = clientHelper;
        this.client = client;
        this.counterMap = new HashMap<>();
        this.ids = new ArrayList<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void checkConnectionStatus() throws IOException {
        /**
         * Periodically sends a "/PING" message to the server
         * This is then forwarded to all connected clients for them to respond to.
         * Sends the /PING every 10 seconds.
         */
        this.socket = client.getSocketInformation();
        out = new PrintWriter(socket.getOutputStream(),true);

        java.util.Timer t = new java.util.Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                out.println("/PING");

            }
        }, 5000, 10000);

    }

    public void buildHashMap(){
        /**
         * Creates a hashmap with a counter set to zero for each user that is connected.
         */
        ids.clear();

        for (String m : clientHelper.getMemberList()){
            String[] parts = m.split(":");
            String userID = parts[0];
            ids.add(userID);
        }

        for (String member : ids){
            counterMap.put(member, 0);
        }
    }

    public void checkPong(String inputStream) {
        /**
         * Reads in an inputStream of /PONG messages from each connected user.
         * If it does not receive a reply from a user it will begin incrementing a count.
         * If the count exceeds the set limit of the size of the built hashmap plus 3 attempts at communication
         * it will send a message to the server to disconnect that user
         *
         * @param inputStream An input stream received from the server in the format "/PONG:userid"
         */
        String[] parts = inputStream.split(":");
        String userid = parts[1];


        for (String i : counterMap.keySet()){
            // Reset the counter to 0 if the user has sent a /PONG reply
            if (i.equals(userid)){
                counterMap.replace(i,0);
            }
            counterMap.put(i, counterMap.get(i) + 1);

            // Send a message to the server to remove the user from the list of connected users.
            if (counterMap.get(i) > (counterMap.size() + 3)){
                out.println("/DISCONNECT" + ":" + i);
            }

            }
        // Removes the user from the coordinators hashmap
        counterMap.keySet().removeIf(k -> counterMap.get(k) > (counterMap.size() + 3));

        }

        public void remove(String id){
            /**
             * This method will remove the id supplied from the counterMap hashmap.
             * This method is called when a previous coordinator has left the chat.
             * @param id The id of the previous coordinator
             */
            counterMap.remove(id);
        }



}
