package main;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private ClientHelper ch = new ClientHelper();
    private Client client = new Client(ch);

    @Test
    void run() {
    }

    @Test
    void openLoginWindow() {
    }

    @Test
    void closeLoginWindow() {
    }

    @Test
    void openChatWindow() {
    }

    @Test
    void connectToServer() {
    }

    @Test
    void sendMessage() {
        String messageText = "Hello mate";
        String recipient = "Steve";
        client.setId("Bob");
        String sender = client.getUserId();
        assertEquals("Hello mate", messageText);
        assertEquals("Steve", recipient);
        assertEquals("Bob", sender);
    }

    @Test
    void sendClientPing() {
    }

    @Test
    void sendClientPong() throws IOException {
        client.setId("Steve");
        String pong = "/PONG:" + client.getUserId();
        assertEquals("/PONG:Steve", pong);
    }

    @Test
    void userQuit() {
        client.setId("John");
        String userQuitting = "This users id is: " + this.client.getUserId();
        assertEquals("This users id is: John", userQuitting);
    }

    @Test
    void getListOfLocalAddresses() {
    }

    @Test
    void setIsConnected() {
    }

    @Test
    void isConnected() {
    }

    @Test
    void connectionStatusChange() {
    }

    @Test
    void sendGUIExit() {
    }

    @Test
    void createListOfLocalAddresses() {
    }

    @Test
    void updateChatWindow() {
    }

    @Test
    void updateOnlineUsers() {
    }

    @Test
    void chatWindowFirstMember() {
    }

    @Test
    void updateCoordinatorDetails() {
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
    void getSocketInformation() throws IOException {

    }
}