package main;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {

    private ConnectionHandler connectionHandler;
    private Socket socket;
    private ClientHandler ch;

void  initialise() throws IOException {
    connectionHandler = new ConnectionHandler("localhost", 59009);
    socket = new Socket("localhost", 59009);
    ch = new ClientHandler(connectionHandler, socket);
}
    @AfterEach
    void tearDown() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void setID(){
        ch.setID("mani");
        assertEquals("mani", ch.getID());
    }

    @Test
    void getID(){
        ch.setID("mani");
        assertEquals("mani", ch.getID());
    }

    @Test
    void sendCurrentUserList() {
    }

    @Test
    void run() {
    }
}