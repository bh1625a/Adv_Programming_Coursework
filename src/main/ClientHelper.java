package main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHelper extends Thread {
    private Client client;
    private ClientHelper clientHelper;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String message = "";
    private String userList;
    private String clientPort;
    private ArrayList<String> memberList;
    private InputStreamReader isr;




    public ClientHelper(){
        super.start();
    }

    public void run(){
        client = new Client(this);
    }


    public void sendMemberDetails() throws IOException {
        try {
            socket = new Socket();

            while (client.isConnected()) {
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("SUBMITUSERLIST");
                userList = client.getClientAddress() + "," + client.getClientPort();
                out.println(userList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getAllMembers() throws IOException {
        socket = new Socket();
        isr = new InputStreamReader(System.in);
        in = new BufferedReader(isr);
        memberList = new ArrayList<>();
        try {
            while (client.isConnected())
                while (true) {
                    while (message == in.readLine()) {
                        if (message != null) {
                            if (message.equals("ALLUSERS")) {
                                String list = "";
                                while (list == in.readLine()) {
                                    memberList.add(list);
                                }
                                if (message.contains("END")) {
                                    break;
                                }
                            }
                        }
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memberList;
    }
}
