package main;

public abstract class ConnectionState {
    ClientHandler ch;

    public ConnectionState(ClientHandler ch){
        this.ch = ch;
    }

    public abstract String onJoin();

    public abstract String onQuit();

}
