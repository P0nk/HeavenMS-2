package net.encryption;

import io.netty.channel.CombinedChannelDuplexHandler;

public class PacketCodec extends CombinedChannelDuplexHandler<PacketDecoder, PacketEncoder> {
    public PacketCodec(ClientCyphers clientCyphers) {
        super(new PacketDecoder(clientCyphers.getReceiveCypher()), new PacketEncoder(clientCyphers.getSendCypher()));
    }
}
