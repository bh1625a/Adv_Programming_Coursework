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


    public Coordinator(){

    }

    public Coordinator(ClientHelper clientHelper, Client client){
        this.clientHelper = clientHelper;
        this.client = client;
        this.counterMap = new HashMap<>();
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
        this.socket = client.getSocketInformation();
        System.out.println("CHECK CONNECTION STATUS REACHED");
        out = new PrintWriter(socket.getOutputStream(),true);

        java.util.Timer t = new java.util.Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                out.println("/PING");

            }
        }, 5000, 5000);

    }

    public void buildHashMap(){
        ArrayList<String> ids = new ArrayList<>();


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

        String[] parts = inputStream.split(":");
        String userid = parts[1];

        for (String i : counterMap.keySet()){
            if (i.equals(userid)){
                System.out.println("I is equal to userid");
                counterMap.replace(i,0);
                System.out.println("Countermap value after replace " + counterMap.get(i));
            } else {
                counterMap.put(i, counterMap.get(i) + 1);
                System.out.println("Countermap value in else " + counterMap.get(i));
            }

        }

    }




}