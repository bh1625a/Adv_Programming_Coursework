package main;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatorTest {
    private Coordinator coordinator = new Coordinator();
    private ArrayList<String> ids;
    private ClientHelper ch;


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
    void checkConnectionStatus() {
    }

    @Test
    void buildHashMap() {
        
    }

    @Test
    void checkPong() {
    }
}