package main;

public class ConnectedState extends ConnectionState {

    public ConnectedState(ClientHandler ch) {
        super(ch);
    }

    public String onJoin(){
        return "Connected";
    }


    public String onQuit(){
        ch.changeState(new DisconnectedState(ch));
        return "Disconnected";
    }

    public void sendMessage(){

    }

    public String toString(){
        return "Connected";
    }
}
