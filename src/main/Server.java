package main;

public class Server {
    public static void main(String[] args) {
        System.out.println("Server is running...");
        ConnectionHandler connectionHandler = new ConnectionHandler("localhost", 59001);
    }

}
