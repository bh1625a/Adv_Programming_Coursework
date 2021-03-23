package main;

public class Server {
    public static void main(String[] args) {
        System.out.println("Server is running...");
        ServerConnectionHandler serverConnectionHandler = new ServerConnectionHandler("localhost", 59001);
    }

}
