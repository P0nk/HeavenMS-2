package net.encryption;

import constants.net.ServerConstants;

public class ClientCyphers {
    private final MapleAESOFB send;
    private final MapleAESOFB receive;

    private ClientCyphers(MapleAESOFB send, MapleAESOFB receive) {
        this.send = send;
        this.receive = receive;
    }

    public static ClientCyphers of(InitializationVector sendIv, InitializationVector receiveIv) {
        MapleAESOFB send = new MapleAESOFB(sendIv, (short) (0xFFFF - ServerConstants.VERSION));
        MapleAESOFB receive = new MapleAESOFB(receiveIv, ServerConstants.VERSION);
        return new ClientCyphers(send, receive);
    }

    public MapleAESOFB getSendCypher() {
        return send;
    }

    public MapleAESOFB getReceiveCypher() {
        return receive;
    }
}
