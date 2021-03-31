package main;

public class DisconnectedState extends ConnectionState {
    public DisconnectedState(ServerClientHandler ch) {
        super(ch);
    }

    public String onJoin(){
        /**
         * Changes the state of a ConnectionState object within the ServerClientHandler to connected.
         */
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
