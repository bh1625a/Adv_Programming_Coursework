package main;

public class ConnectedState extends ConnectionState {

    public ConnectedState(ServerClientHandler ch) {
        super(ch);
    }


    public String onJoin(){
        return "Connected";
    }


    public String onQuit(){
        ch.changeState(new DisconnectedState(ch));
        return "Disconnected";
    }


    public String toString(){
        return "Connected";
    }
}
