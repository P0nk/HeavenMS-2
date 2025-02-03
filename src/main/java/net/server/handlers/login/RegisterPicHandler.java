package net.server.handlers.login;

import client.Client;
import lombok.extern.slf4j.Slf4j;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import net.server.Server;
import net.server.coordinator.session.Hwid;
import net.server.coordinator.session.SessionCoordinator;
import net.server.coordinator.session.SessionCoordinator.AntiMulticlientResult;
import net.server.world.World;
import service.AccountService;
import service.BanService;
import service.TransitionService;
import tools.PacketCreator;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public final class RegisterPicHandler extends AbstractPacketHandler {
    private final BanService banService;
    private final TransitionService transitionService;
    private final AccountService accountService;

    public RegisterPicHandler(BanService banService, AccountService accountService, TransitionService transitionService) {
        this.banService = banService;
        this.accountService = accountService;
        this.transitionService = transitionService;
    }

    @Override
    public final void handlePacket(InPacket p, Client c) {
        p.readByte();
        int charId = p.readInt();

        String macs = p.readString();
        String hostString = p.readString();

        final Hwid hwid;
        try {
            hwid = Hwid.fromHostString(hostString);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid host string: {}", hostString, e);
            c.sendPacket(PacketCreator.getAfterLoginError(17));
            return;
        }

        c.setHwid(hwid);
        c.setMacs(macs);
        accountService.setIpAndMacsAndHwidAsync(c.getAccID(), c.getRemoteAddress(), macs, hwid);

        AntiMulticlientResult res = SessionCoordinator.getInstance().attemptGameSession(c, c.getAccID(), hwid);
        if (res != AntiMulticlientResult.SUCCESS) {
            c.sendPacket(PacketCreator.getAfterLoginError(parseAntiMulticlientError(res)));
            return;
        }

        if (banService.isBanned(c)) {
            SessionCoordinator.getInstance().closeSession(c, true);
            return;
        }

        Server server = Server.getInstance();
        if (!server.haveCharacterEntry(c.getAccID(), charId)) {
            SessionCoordinator.getInstance().closeSession(c, true);
            return;
        }

        String pic = p.readString();
        if (c.getPic() == null || c.getPic().equals("")) {
            accountService.setPic(c.getAccID(), pic);
            c.setPic(pic);

            c.setWorld(server.getCharacterWorld(charId));
            World wserv = c.getWorldServer();
            if (wserv == null || wserv.isWorldCapacityFull()) {
                c.sendPacket(PacketCreator.getAfterLoginError(10));
                return;
            }

            String[] socket = server.getInetSocket(c, c.getWorld(), c.getChannel());
            if (socket == null) {
                c.sendPacket(PacketCreator.getAfterLoginError(10));
                return;
            }

            server.unregisterLoginState(c);
            transitionService.setInTransition(c, charId);

            try {
                c.sendPacket(PacketCreator.getServerIP(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1]), charId));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            SessionCoordinator.getInstance().closeSession(c, true);
        }
    }

    private static int parseAntiMulticlientError(AntiMulticlientResult res) {
        return switch (res) {
            case REMOTE_PROCESSING -> 10;
            case REMOTE_LOGGEDIN -> 7;
            case REMOTE_NO_MATCH -> 17;
            case COORDINATOR_ERROR -> 8;
            default -> 9;
        };
    }
}
