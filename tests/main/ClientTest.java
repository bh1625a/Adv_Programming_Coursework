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
    private Socket socket;
    private PrintWriter out;
    private OutputStream outputStream;
    private Scanner in;
    private ServerSocket serverSocket;



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
    }

    @Test
    void sendClientPing() {
    }

    @Test
    void sendClientPong() throws IOException {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            client.setId("Ben");
            out.println("/PONG" + ":" + client.getUserId());
            in = new Scanner(socket.getInputStream());
            String inputStream = in.nextLine();
            String pong = "";
            if (inputStream.contains("/PONG")) {
                pong = inputStream;
            }
            assertEquals("/PONG:Ben", pong);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    void userQuit() {
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
        client.setServerAddress("localhost");
        assertTrue(client.getServerAddress() == "localhost");
    }

    @Test
    void getServerAddress() {
        client.setServerAddress("localhost");
        assertTrue(client.getServerAddress() == "localhost");
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
        client.setClientAddress("localhost");
        assertTrue(client.getClientAddress() == "localhost");
    }

    @Test
    void getClientAddress() {
        client.setClientAddress("localhost");
        assertTrue(client.getClientAddress() == "localhost");
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