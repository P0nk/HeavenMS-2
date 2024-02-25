package net.netty;

public abstract class AbstractServer {
    final int port;

    AbstractServer(int port) {
        this.port = port;
    }

    public abstract void start();
    public abstract void stop();
}
