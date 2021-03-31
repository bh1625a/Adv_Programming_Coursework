package main;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ConnectedStateTest {
    private ServerConnectionHandler serverConnectionHandler;
    private Socket testSocket;
    private ServerClientHandler serverClientHandler;
    ConnectionState state;

    @BeforeEach
    public void initialize() throws IOException {
        serverConnectionHandler = new ServerConnectionHandler("localhost", 65000);
        testSocket = new Socket("localhost", 65000);
        serverClientHandler = new ServerClientHandler(serverConnectionHandler, testSocket);
        state = new ConnectedState(serverClientHandler);
    }

    @AfterEach
    public void tearDown() throws IOException {
        testSocket.close();
    }

    @Test
    void onJoin() {
        String status = this.state.onJoin();
        assertEquals("Connected", status);
    }

    @Test
    void onQuit() {
        String status = this.state.onQuit();
        assertEquals("Disconnected", status);
        assertTrue(serverClientHandler.getConnectionState() instanceof DisconnectedState);
    }

    @Test
    void testToString() {
        assertTrue(this.state.toString().equals("Connected"));
    }
}