package main;

public class DisconnectedState implements ConnectionState{
    @Override
    public void setState(Client client) {
        client.connectionStatusChange(this);

    }




    @Override
    public void getConnectionState() {

    }
}
