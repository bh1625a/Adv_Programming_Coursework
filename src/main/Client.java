package main;

import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {
    private LoginWindow loginWindow;
    private ChatWindow chatWindow;
    private Socket socket;

    public static void main(String[] args) throws IOException {
        ClientHelper clientHelper = new ClientHelper();

    }
}
