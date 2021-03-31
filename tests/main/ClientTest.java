package main;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private ClientHelper ch;
    private Client client;
    ServerSocket serverSocket;
    private Socket clientSocket;
    private Socket connection;
    private Scanner serverIn;
    private Scanner clientIn;
    private PrintWriter serverOut;
    private PrintWriter clientOut;

    @BeforeEach
    public void setup(){
        ch = new ClientHelper();
        client = new Client(ch);
    }



    @BeforeClass
    public void initialize() throws IOException {
//        ch = new ClientHelper();
//        client = new Client(ch);
        serverSocket = new ServerSocket(58001);
        clientSocket = new Socket("192.168.0.7", 58001);
        connection = serverSocket.accept();
        serverIn = new Scanner(connection.getInputStream());
        serverOut = new PrintWriter(connection.getOutputStream(), true);
        clientIn = new Scanner(clientSocket.getInputStream());
        clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @After
    public void tearDown() throws IOException {
        serverSocket.close();
        connection.close();
        clientSocket.close();
    }

    @Test
    void sendMessageTest() throws IOException {
        setup();
        initialize();
        clientOut.println("/SENDMESSAGE");
        String receiveSendMessage = serverIn.nextLine();
        clientOut.println("Hello mate");
        String message = serverIn.nextLine();
        clientOut.println("Steve");
        String recipient = serverIn.nextLine();
        client.setId("Bob");
        clientOut.println(client.getUserId());
        String sender = serverIn.nextLine();
        assertEquals("/SENDMESSAGE", receiveSendMessage);
        assertEquals("Hello mate", message);
        assertEquals("Steve", recipient);
        assertEquals("Bob", sender);
        tearDown();
    }

    @Test
    void sendClientPing() throws IOException {
        initialize();
        clientOut.println("/PING");
        String readPing = serverIn.nextLine();
        assertEquals("/PING", readPing);

    }

    @Test
    void sendClientPong() throws IOException {
        initialize();
        client.setId("Steve");
        clientOut.println("/PONG" + ":" + client.getUserId());
        String pong = serverIn.nextLine();
        assertEquals("/PONG:Steve", pong);
        tearDown();
    }

    @Test
    void userQuit() {
        client.setId("John");
        String userQuitting = "This users id is: " + this.client.getUserId();
        assertEquals("This users id is: John", userQuitting);
    }

    @Test
    void getListOfLocalAddresses() throws SocketException {
        client.getListOfLocalAddresses().clear();
        client.getListOfLocalAddresses().add("192.168.0.3");
        ArrayList<String> testAddresses = new ArrayList<>();
        testAddresses.add("192.168.0.3");
        assertEquals(testAddresses, client.getListOfLocalAddresses());

    }

    @Test
    public void setId() {
        client.setId("mani");
        assertTrue(client.getUserId() == "mani");
    }

    @Test
    void getUserId() {
        client.setId("ben");
        assertEquals("ben", client.getUserId());
    }

    @Test
    void checkClientIpAvailable() {
        client.getListOfLocalAddresses().clear();
        client.getListOfLocalAddresses().add("192.168.0.3");
        assertTrue(client.getListOfLocalAddresses().contains("192.168.0.3"));
    }

    @Test
    void setServerAddress() {
        client.setServerAddress("149.22.9.78");
        assertTrue(client.getServerAddress() == "149.22.9.78");
    }

    @Test
    void getServerAddress() {
        client.setServerAddress("149.22.9.78");
        assertTrue(client.getServerAddress() == "149.22.9.78");
    }

    @Test
    void setServerPort() {
        client.setServerPort("59001");
        assertTrue(client.getServerPort() == 59001);
    }

    @Test
    void getServerPort() {
        client.setServerPort("59001");
        assertTrue(client.getServerPort() == 59001);
    }

    @Test
    void setClientAddress() {
        client.setClientAddress("86.13.37.146");
        assertTrue(client.getClientAddress() == "86.13.37.146");
    }

    @Test
    void getClientAddress() {
        client.setClientAddress("86.13.37.146");
        assertTrue(client.getClientAddress() == "86.13.37.146");
    }

    @Test
    void setClientPort() {
        client.setClientPort("59002");
        assertTrue(client.getClientPort() == 59002);
    }

    @Test
    void getClientPort() {
        client.setClientPort("59003");
        assertEquals(59003, client.getClientPort());
    }

    @Test
    void setCoordinator() {
        this.client.setCoordinator(true);
        assertTrue(this.client.isTheCoordinator() == true);
    }

    @Test
    void isTheCoordinator() {
        this.client.setCoordinator(true);
        assertTrue(this.client.isTheCoordinator() == true);
    }


    @Test
    void updateCoordinatorDetails() {
    }

    @Test
    void getSocketInformation() {
        client.getSocketInformation();
    }
}