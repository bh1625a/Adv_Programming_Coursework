package main;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class DisconnectedStateTest {
    private ServerConnectionHandler serverConnectionHandler;
    private Socket testSocket;
    private ServerClientHandler serverClientHandler;
    ConnectionState state;

    @BeforeEach
    public void initialize() throws IOException {
        serverConnectionHandler = new ServerConnectionHandler("localhost", 65000);
        testSocket = new Socket("localhost", 65000);
        serverClientHandler = new ServerClientHandler(serverConnectionHandler, testSocket);
        state = new DisconnectedState(serverClientHandler);
    }

    @AfterEach
    public void tearDown() throws IOException {
        testSocket.close();
    }

    @After
    public void closeConnection() throws IOException{
        serverConnectionHandler.getConnection().close();
    }

    @Test
    public void onJoin(){
        String status = this.state.onJoin();
        assertEquals("Connected", status);
        assertTrue(serverClientHandler.getConnectionState() instanceof ConnectedState);
    }

    @Test
    void onQuit() {
        String status = this.state.onQuit();
        assertEquals("Disconnected", status);
    }

    @Test
    void testToString() {
        assertTrue(this.state.toString().equals("Disconnected"));
    }
}