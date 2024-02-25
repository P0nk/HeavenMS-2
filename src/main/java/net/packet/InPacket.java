package net.packet;

import java.awt.*;

public interface InPacket extends Packet {
    byte readByte();
    short readUnsignedByte();
    short readShort();
    int readInt();
    long readLong();
    Point readPos();
    String readString();
    byte[] readBytes(int numberOfBytes);
    void skip(int numberOfBytes);
    int available();
    void seek(int byteOffset);
    int getPosition();
}
