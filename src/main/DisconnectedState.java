package main;

public class DisconnectedState extends ConnectionState {
    public DisconnectedState(ClientHandler ch) {
        super(ch);
    }

    public String onJoin(){
        this.ch.changeState(new ConnectedState(ch));
        return "Connected";
    }

    @Override
    public String onQuit() {
        return "Disconnected";
    }

    public String toString(){
        return "Disconnected";
    }
}
