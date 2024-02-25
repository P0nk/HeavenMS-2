package net.packet.logging;

import constants.net.OpcodeConstants;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.packet.InPacket;
import net.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.HexTool;

@Sharable
public class InPacketLogger extends ChannelInboundHandlerAdapter implements PacketLogger {
    private static final Logger log = LoggerFactory.getLogger(InPacketLogger.class);
    private static final int LOG_CONTENT_THRESHOLD = 3_000;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof InPacket packet) {
            log(packet);
        }

        ctx.fireChannelRead(msg);
    }

    @Override
    public void log(Packet packet) {
        final byte[] content = packet.getBytes();
        final int packetLength = content.length;

        if (packetLength <= LOG_CONTENT_THRESHOLD) {
            final short opcode = LoggingUtil.readFirstShort(content);
            final String opcodeHex = Integer.toHexString(opcode).toUpperCase();
            final String opcodeName = getRecvOpcodeName(opcode);
            final String prefix = opcodeName == null ? "<UnknownPacket> " : "";
            log.debug("{}ClientSend:{} [{}] ({}) <HEX> {} <TEXT> {}", prefix, opcodeName, opcodeHex, packetLength,
                    HexTool.toHexString(content), HexTool.toStringFromAscii(content));
        } else {
            log.debug(HexTool.toHexString(new byte[]{content[0], content[1]}) + "...");
        }
    }

    private String getRecvOpcodeName(short opcode) {
        return OpcodeConstants.recvOpcodeNames.get((int) opcode);
    }
}
