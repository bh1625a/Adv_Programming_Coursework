package main;

public class ConnectedState implements ConnectionState {


    @Override
    public void setState(Client client) {
        client.connectionStatusChange(this);
    }

    @Override
    public void getConnectionState() {

    }

    public void sendMessage(){

    }
}
