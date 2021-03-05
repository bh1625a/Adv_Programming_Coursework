package main;

public class ClientHelper extends Thread {
    private Client client;

    public ClientHelper(){
        super.start();
    }

    public void run(){
        client = new Client(this);
    }

}
