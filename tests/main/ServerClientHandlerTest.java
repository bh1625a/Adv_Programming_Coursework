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
    private Coordinator coordinator = new Coordinator();

void  initialise() throws IOException {
    serverConnectionHandler = new ServerConnectionHandler("localhost", 59009);
    socket = new Socket("localhost", 59009);
    ch = new ServerClientHandler(serverConnectionHandler, socket);
}
    @AfterEach
    void tearDown(){

    }

    @Test
    void setID() throws IOException {
//    initialise();
//        ch.setID("mani");
//        assertEquals("mani", ch.getID());
    }

    @Test
    void getID() throws IOException {
    initialise();
        ch.setID("mani");
        assertEquals("mani", ch.getID());
    }

    @Test
    void sendCurrentUserList() {
    }


    @Test
    void sendCoordinatorDetails() {
        coordinator.setId("max");
        coordinator.setIp("localhost");
        coordinator.setPort(59002);
        String sendCoordDetails = "/SENDCOORDINATORDETAILS:" + coordinator.getId() + ":" + coordinator.getIp() +
                ":" + coordinator.getPort();
        assertEquals("/SENDCOORDINATORDETAILS:max:localhost:59002", sendCoordDetails);
    }

    @Test
    void notifyUsers() {
    }

    @Test
    void setTheCoordinator() {
    }

    @Test
    void getCoordinator() {
    coordinator.setId("max");
    assertEquals("max", coordinator.getId());
    }

    @Test
    void getCurrentClientPort() {

    }

    @Test
    void getCurrentClientIP() {

    }

    @Test
    void sendPingToClient() {
    //    initialise();
//    ch.setID("max");
//    String ping = "/PING:" + ch.getID();
//    assertEquals("/PING:max", ping);

    }

    @Test
    void sendPongToCoordinator() {
        coordinator.setId("ben");
        String pong = "/PONG:" + coordinator.getId();
        assertEquals("/PONG:ben", pong);
    }

}