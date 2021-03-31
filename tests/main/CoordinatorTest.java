package main;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatorTest {
    private Coordinator coordinator;
    private ArrayList<String> memberList;
    private ArrayList<String> ids;
    private HashMap <String, Integer> counterMap;
    private ServerSocket serverSocket;
    private Socket socket;
    private Socket connect;
    private Scanner in;
    private PrintWriter out;

    @BeforeClass
    public void initialise() throws IOException {
        serverSocket = new ServerSocket(58001);
        socket = new Socket("localhost", 58001);
        connect = serverSocket.accept();
        in = new Scanner(connect.getInputStream());
        out = new PrintWriter(connect.getOutputStream(), true);
    }
    @BeforeEach
    public void setup(){
        coordinator = new Coordinator();
    }

    @Test
    void getId() {
        coordinator.setId("Ben");
        assertEquals("Ben", coordinator.getId());
    }

    @Test
    void setId() {
        coordinator.setId("Ben");
        assertEquals("Ben", coordinator.getId());
    }

    @Test
    void getPort() {
        coordinator.setPort(59002);
        assertTrue(coordinator.getPort() == 59002);
    }

    @Test
    void setPort() {
        coordinator.setPort(59002);
        assertTrue(coordinator.getPort() == 59002);
    }

    @Test
    void getIp() {
        coordinator.setIp("localhost");
        assertEquals(coordinator.getIp(), "localhost");
    }

    @Test
    void setIp() {
        coordinator.setIp("localhost");
        assertEquals(coordinator.getIp(), "localhost");
    }

    @Test
    void buildHashMap() throws IOException {
        memberList = new ArrayList<>();
        ids = new ArrayList<>();
        memberList.add("Benjamin:59002:192.168.0.60");
        memberList.add("Robbie:59006:192.168.0.60");

        for(String clients: memberList){
            String[] parts = clients.split(":");
            String userID = parts[0];
            ids.add(userID);
        }
        assertEquals(2, ids.size());

        counterMap = new HashMap<>();

       for (String members: ids){
            counterMap.put(members, 0);
        }

        assertEquals(2 ,counterMap.size());
        assertEquals(true , counterMap.containsKey("Benjamin"));
    }


}