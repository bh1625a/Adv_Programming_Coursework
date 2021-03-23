package main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ServerClientHandlerTest {

    private ServerConnectionHandler serverConnectionHandler;
    private Socket socket;
    private ServerClientHandler ch;

void  initialise() throws IOException {
    serverConnectionHandler = new ServerConnectionHandler("localhost", 59009);
    socket = new Socket("localhost", 59009);
    ch = new ServerClientHandler(serverConnectionHandler, socket);
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