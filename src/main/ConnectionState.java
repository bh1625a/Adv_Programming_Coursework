package main;

public abstract class ConnectionState {
    ServerClientHandler ch;

    public ConnectionState(ServerClientHandler ch){
        this.ch = ch;
    }

    public abstract String onJoin();

    public abstract String onQuit();

}
