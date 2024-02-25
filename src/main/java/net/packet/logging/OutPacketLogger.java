package net.packet.logging;

import constants.net.OpcodeConstants;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import net.packet.OutPacket;
import net.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.HexTool;

@Sharable
public class OutPacketLogger extends ChannelOutboundHandlerAdapter implements PacketLogger {
    private static final Logger log = LoggerFactory.getLogger(OutPacketLogger.class);
    private static final int LOG_CONTENT_THRESHOLD = 50_000;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (msg instanceof OutPacket packet) {
            log(packet);
        }

        ctx.write(msg);
    }

    @Override
    public void log(Packet packet) {
        final byte[] content = packet.getBytes();
        final int packetLength = content.length;

        if (packetLength <= LOG_CONTENT_THRESHOLD) {
            final short opcode = LoggingUtil.readFirstShort(content);
            String opcodeHex = Integer.toHexString(opcode).toUpperCase();
            String opcodeName = getSendOpcodeName(opcode);
            String prefix = opcodeName == null ? "<UnknownPacket> " : "";
            log.debug("{}ServerSend:{} [{}] ({}) <HEX> {} <TEXT> {}", prefix, opcodeName, opcodeHex, packetLength,
                    HexTool.toHexString(content), HexTool.toStringFromAscii(content));
        } else {
            log.debug(HexTool.toHexString(new byte[]{content[0], content[1]}) + " ...");
        }
    }

    private String getSendOpcodeName(short opcode) {
        return OpcodeConstants.sendOpcodeNames.get((int) opcode);
    }
}
