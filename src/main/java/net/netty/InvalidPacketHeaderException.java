package net.netty;

public class InvalidPacketHeaderException extends RuntimeException {
    private final int header;

    public InvalidPacketHeaderException(String message, int header) {
        super(message);
        this.header = header;
    }

    public int getHeader() {
        return header;
    }
}
