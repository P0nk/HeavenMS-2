package net.packet.out;

import net.opcodes.SendOpcode;
import net.packet.ByteBufOutPacket;

public final class SendNoteSuccessPacket extends ByteBufOutPacket {

    public SendNoteSuccessPacket() {
        super(SendOpcode.MEMO_RESULT);

        writeByte(4);
    }
}
