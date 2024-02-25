/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.packets;

import client.Character;
import client.inventory.Item;
import constants.id.ItemId;
import constants.id.MapId;
import net.opcodes.SendOpcode;
import net.packet.OutPacket;
import net.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.PacketCreator;
import tools.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CField_Wedding, CField_WeddingPhoto, CWeddingMan, OnMarriageResult, and all Wedding/Marriage enum/structs.
 *
 * @author Eric
 * <p>
 * Wishlists edited by Drago (Dragohe4rt)
 */
public class WeddingPackets extends PacketCreator {
    private static final Logger log = LoggerFactory.getLogger(WeddingPackets.class);

    /*
        00000000 CWeddingMan     struc ; (sizeof=0x104)
        00000000 vfptr           dd ?                    ; offset
        00000004 ___u1           $01CBC6800BD386B8A8FD818EAD990BEC ?
        0000000C m_mCharIDToMarriageNo ZMap<unsigned long,unsigned long,unsigned long> ?
        00000024 m_mReservationPending ZMap<unsigned long,ZRef<GW_WeddingReservation>,unsigned long> ?
        0000003C m_mReservationPendingGroom ZMap<unsigned long,ZRef<CUser>,unsigned long> ?
        00000054 m_mReservationPendingBride ZMap<unsigned long,ZRef<CUser>,unsigned long> ?
        0000006C m_mReservationStartUser ZMap<unsigned long,unsigned long,unsigned long> ?
        00000084 m_mReservationCompleted ZMap<unsigned long,ZRef<GW_WeddingReservation>,unsigned long> ?
        0000009C m_mGroomWishList ZMap<unsigned long,ZRef<ZArray<ZXString<char> > >,unsigned long> ?
        000000B4 m_mBrideWishList ZMap<unsigned long,ZRef<ZArray<ZXString<char> > >,unsigned long> ?
        000000CC m_mEngagementPending ZMap<unsigned long,ZRef<GW_MarriageRecord>,unsigned long> ?
        000000E4 m_nCurrentWeddingState dd ?
        000000E8 m_dwCurrentWeddingNo dd ?
        000000EC m_dwCurrentWeddingMap dd ?
        000000F0 m_bIsReservationLoaded dd ?
        000000F4 m_dwNumGuestBless dd ?
        000000F8 m_bPhotoSuccess dd ?
        000000FC m_tLastUpdate   dd ?
        00000100 m_bStartWeddingCeremony dd ?
        00000104 CWeddingMan     ends
    */

    public class Field_Wedding {
        public int m_nNoticeCount;
        public int m_nCurrentStep;
        public int m_nBlessStartTime;
    }

    public class Field_WeddingPhoto {
        public boolean m_bPictureTook;
    }

    public class GW_WeddingReservation {
        public int dwReservationNo;
        public int dwGroom, dwBride;
        public String sGroomName, sBrideName;
        public int usWeddingType;
    }

    public class WeddingWishList {
        public Character pUser;
        public int dwMarriageNo;
        public int nGender;
        public int nWLType;
        public int nSlotCount;
        public List<String> asWishList = new ArrayList<>();
        public int usModifiedFlag; // dword
        public boolean bLoaded;
    }

    public class GW_WeddingWishList {
        public final int WEDDINGWL_MAX = 0xA; // enum WEDDINGWL
        public int dwReservationNo;
        public byte nGender;
        public String sItemName;
    }

    public enum MarriageStatus {
        SINGLE(0x0),
        ENGAGED(0x1),
        RESERVED(0x2),
        MARRIED(0x3);
        private final int ms;

        MarriageStatus(int ms) {
            this.ms = ms;
        }

        public int getMarriageStatus() {
            return ms;
        }
    }

    public enum MarriageRequest {
        AddMarriageRecord(0x0),
        SetMarriageRecord(0x1),
        DeleteMarriageRecord(0x2),
        LoadReservation(0x3),
        AddReservation(0x4),
        DeleteReservation(0x5),
        GetReservation(0x6);
        private final int req;

        MarriageRequest(int req) {
            this.req = req;
        }

        public int getMarriageRequest() {
            return req;
        }
    }

    public enum WeddingType {
        CATHEDRAL(0x1),
        VEGAS(0x2),
        CATHEDRAL_PREMIUM(0xA),
        CATHEDRAL_NORMAL(0xB),
        VEGAS_PREMIUM(0x14),
        VEGAS_NORMAL(0x15);
        private final int wt;

        WeddingType(int wt) {
            this.wt = wt;
        }

        public int getType() {
            return wt;
        }
    }

    public enum WeddingMap {
        WEDDINGTOWN(MapId.AMORIA),
        CHAPEL_STARTMAP(MapId.CHAPEL_WEDDING_ALTAR),
        CATHEDRAL_STARTMAP(MapId.CATHEDRAL_WEDDING_ALTAR),
        PHOTOMAP(MapId.WEDDING_PHOTO),
        EXITMAP(MapId.WEDDING_EXIT);
        private final int wm;

        WeddingMap(int wm) {
            this.wm = wm;
        }

        public int getMap() {
            return wm;
        }
    }

    public enum WeddingItem {
        WR_MOONSTONE(ItemId.WEDDING_RING_MOONSTONE), // Wedding Ring
        WR_STARGEM(ItemId.WEDDING_RING_STAR),
        WR_GOLDENHEART(ItemId.WEDDING_RING_GOLDEN),
        WR_SILVERSWAN(ItemId.WEDDING_RING_SILVER),
        ERB_MOONSTONE(ItemId.ENGAGEMENT_BOX_MOONSTONE), // Engagement Ring Box
        ERB_STARGEM(ItemId.ENGAGEMENT_BOX_STAR),
        ERB_GOLDENHEART(ItemId.ENGAGEMENT_BOX_GOLDEN),
        ERB_SILVERSWAN(ItemId.ENGAGEMENT_BOX_SILVER),
        ERBE_MOONSTONE(ItemId.EMPTY_ENGAGEMENT_BOX_MOONSTONE), // Engagement Ring Box (Empty)
        ER_MOONSTONE(ItemId.ENGAGEMENT_RING_MOONSTONE), // Engagement Ring
        ERBE_STARGEM(ItemId.EMPTY_ENGAGEMENT_BOX_STAR),
        ER_STARGEM(ItemId.ENGAGEMENT_RING_STAR),
        ERBE_GOLDENHEART(ItemId.EMPTY_ENGAGEMENT_BOX_GOLDEN),
        ER_GOLDENHEART(ItemId.ENGAGEMENT_RING_GOLDEN),
        ERBE_SILVERSWAN(ItemId.EMPTY_ENGAGEMENT_BOX_SILVER),
        ER_SILVERSWAN(ItemId.ENGAGEMENT_RING_SILVER),
        PARENTS_BLESSING(ItemId.PARENTS_BLESSING), // Parents Blessing
        OFFICIATORS_PERMISSION(ItemId.OFFICIATORS_PERMISSION), // Officiator's Permission
        WR_CATHEDRAL_PREMIUM(ItemId.PREMIUM_CATHEDRAL_RESERVATION_RECEIPT), // Wedding Ring?
        WR_VEGAS_PREMIUM(ItemId.PREMIUM_CHAPEL_RESERVATION_RECEIPT),
        IB_VEGAS(ItemId.INVITATION_CHAPEL),      // toSend invitation
        IB_CATHEDRAL(ItemId.INVITATION_CATHEDRAL),  // toSend invitation
        IG_VEGAS(ItemId.RECEIVED_INVITATION_CHAPEL),      // rcvd invitation
        IG_CATHEDRAL(ItemId.RECEIVED_INVITATION_CATHEDRAL),  // rcvd invitation
        OB_FORCOUPLE(ItemId.ONYX_CHEST_FOR_COUPLE), // Onyx Box? For Couple
        WR_CATHEDRAL_NORMAL(ItemId.NORMAL_CATHEDRAL_RESERVATION_RECEIPT), // Wedding Ring?
        WR_VEGAS_NORMAL(ItemId.NORMAL_CHAPEL_RESERVATION_RECEIPT),
        WT_CATHEDRAL_NORMAL(ItemId.NORMAL_WEDDING_TICKET_CATHEDRAL), // Wedding Ticket
        WT_VEGAS_NORMAL(ItemId.NORMAL_WEDDING_TICKET_CHAPEL),
        WT_VEGAS_PREMIUM(ItemId.PREMIUM_WEDDING_TICKET_CHAPEL),
        WT_CATHEDRAL_PREMIUM(ItemId.PREMIUM_WEDDING_TICKET_CATHEDRAL);
        private final int wi;

        WeddingItem(int wi) {
            this.wi = wi;
        }

        public int getItem() {
            return wi;
        }
    }

    /**
     * <name> has requested engagement. Will you accept this proposal?
     *
     * @param name
     * @param playerid
     * @return mplew
     */
    public static Packet onMarriageRequest(String name, int playerid) {
        OutPacket p = OutPacket.create(SendOpcode.MARRIAGE_REQUEST);
        p.writeByte(0); //mode, 0 = engage, 1 = cancel, 2 = answer.. etc
        p.writeString(name); // name
        p.writeInt(playerid); // playerid
        return p;
    }

    /**
     * A quick rundown of how (I think based off of enough BMS searching) WeddingPhoto_OnTakePhoto works:
     * - We send this packet with (first) the Groom / Bride IGNs
     * - We then send a fieldId (unsure about this part at the moment, 90% sure it's the id of the map)
     * - After this, we write an integer of the amount of characters within the current map (which is the Cake Map -- exclude users within Exit Map)
     * - Once we've retrieved the size of the characters, we begin to write information about them (Encode their name, guild, etc info)
     * - Now that we've Encoded our character data, we begin to Encode the ScreenShotPacket which requires a TemplateID, IGN, and their positioning
     * - Finally, after encoding all of our data, we send this packet out to a MapGen application server
     * - The MapGen server will then retrieve the packet byte array and convert the bytes into a ImageIO 2D JPG output
     * - The result after converting into a JPG will then be remotely uploaded to /weddings/ with ReservedGroomName_ReservedBrideName to be displayed on the web server.
     * <p>
     * - Will no longer continue Wedding Photos, needs a WvsMapGen :(
     *
     * @param ReservedGroomName The groom IGN of the wedding
     * @param ReservedBrideName The bride IGN of the wedding
     * @param m_dwField         The current field id (the id of the cake map, ex. 680000300)
     * @param m_uCount          The current user count (equal to m_dwUsers.size)
     * @param m_dwUsers         The List of all Character guests within the current cake map to be encoded
     * @return mplew (MaplePacket) Byte array to be converted and read for byte[]->ImageIO
     */
    public static Packet onTakePhoto(String ReservedGroomName, String ReservedBrideName, int m_dwField, List<Character> m_dwUsers) { // OnIFailedAtWeddingPhotos
        OutPacket p = OutPacket.create(SendOpcode.WEDDING_PHOTO);// v53 header, convert -> v83
        p.writeString(ReservedGroomName);
        p.writeString(ReservedBrideName);
        p.writeInt(m_dwField); // field id?
        p.writeInt(m_dwUsers.size());

        for (Character guest : m_dwUsers) {
            // Begin Avatar Encoding
            addCharLook(p, guest, false); // CUser::EncodeAvatar
            p.writeInt(30000); // v20 = *(_DWORD *)(v13 + 2192) -- new groom marriage ID??
            p.writeInt(30000); // v20 = *(_DWORD *)(v13 + 2192) -- new bride marriage ID??
            p.writeString(guest.getName());
            p.writeString(guest.getGuildId() > 0 && guest.getGuild() != null ? guest.getGuild().getName() : "");
            p.writeShort(guest.getGuildId() > 0 && guest.getGuild() != null ? guest.getGuild().getLogoBG() : 0);
            p.writeByte(guest.getGuildId() > 0 && guest.getGuild() != null ? guest.getGuild().getLogoBGColor() : 0);
            p.writeShort(guest.getGuildId() > 0 && guest.getGuild() != null ? guest.getGuild().getLogo() : 0);
            p.writeByte(guest.getGuildId() > 0 && guest.getGuild() != null ? guest.getGuild().getLogoColor() : 0);
            p.writeShort(guest.getPosition().x); // v18 = *(_DWORD *)(v13 + 3204);
            p.writeShort(guest.getPosition().y); // v20 = *(_DWORD *)(v13 + 3208);
            // Begin Screenshot Encoding
            p.writeByte(1); // // if ( *(_DWORD *)(v13 + 288) ) { COutPacket::Encode1(&thisa, v20);
            // CPet::EncodeScreenShotPacket(*(CPet **)(v13 + 288), &thisa);
            p.writeInt(1); // dwTemplateID
            p.writeString(guest.getName()); // m_sName
            p.writeShort(guest.getPosition().x); // m_ptCurPos.x
            p.writeShort(guest.getPosition().y); // m_ptCurPos.y
            p.writeByte(guest.getStance()); // guest.m_bMoveAction
        }

        return p;
    }

    /**
     * Enable spouse chat and their engagement ring without @relog
     *
     * @param marriageId
     * @param chr
     * @param wedding
     * @return mplew
     */
    public static Packet OnMarriageResult(int marriageId, Character chr, boolean wedding) {
        OutPacket p = OutPacket.create(SendOpcode.MARRIAGE_RESULT);
        p.writeByte(11);
        p.writeInt(marriageId);
        p.writeInt(chr.getGender() == 0 ? chr.getId() : chr.getPartnerId());
        p.writeInt(chr.getGender() == 0 ? chr.getPartnerId() : chr.getId());
        p.writeShort(wedding ? 3 : 1);
        if (wedding) {
            p.writeInt(chr.getMarriageItemId());
            p.writeInt(chr.getMarriageItemId());
        } else {
            p.writeInt(ItemId.WEDDING_RING_MOONSTONE); // Engagement Ring's Outcome (doesn't matter for engagement)
            p.writeInt(ItemId.WEDDING_RING_MOONSTONE); // Engagement Ring's Outcome (doesn't matter for engagement)
        }
        p.writeFixedString(StringUtil.getRightPaddedStr(chr.getGender() == 0 ? chr.getName() : Character.getNameById(chr.getPartnerId()), '\0', 13));
        p.writeFixedString(StringUtil.getRightPaddedStr(chr.getGender() == 0 ? Character.getNameById(chr.getPartnerId()) : chr.getName(), '\0', 13));

        return p;
    }

    /**
     * To exit the Engagement Window (Waiting for her response...), we send a GMS-like pop-up.
     *
     * @param msg
     * @return mplew
     */
    public static Packet OnMarriageResult(final byte msg) {
        OutPacket p = OutPacket.create(SendOpcode.MARRIAGE_RESULT);
        p.writeByte(msg);
        if (msg == 36) {
            p.writeByte(1);
            p.writeString("You are now engaged.");
        }
        return p;
    }

    /**
     * The World Map includes 'loverPos' in which this packet controls
     *
     * @param partner
     * @param mapid
     * @return mplew
     */
    public static Packet OnNotifyWeddingPartnerTransfer(int partner, int mapid) {
        OutPacket p = OutPacket.create(SendOpcode.NOTIFY_MARRIED_PARTNER_MAP_TRANSFER);
        p.writeInt(mapid);
        p.writeInt(partner);
        return p;
    }

    /**
     * The wedding packet to display Pelvis Bebop and enable the Wedding Ceremony Effect between two characters
     * CField_Wedding::OnWeddingProgress - Stages
     * CField_Wedding::OnWeddingCeremonyEnd - Wedding Ceremony Effect
     *
     * @param setBlessEffect
     * @param groom
     * @param bride
     * @param step
     * @return mplew
     */
    public static Packet OnWeddingProgress(boolean setBlessEffect, int groom, int bride, byte step) {
        OutPacket p = OutPacket.create(setBlessEffect ? SendOpcode.WEDDING_CEREMONY_END : SendOpcode.WEDDING_PROGRESS);
        if (!setBlessEffect) { // in order for ceremony packet to send, byte step = 2 must be sent first
            p.writeByte(step);
        }
        p.writeInt(groom);
        p.writeInt(bride);
        return p;
    }

    /**
     * When we open a Wedding Invitation, we display the Bride & Groom
     *
     * @param groom
     * @param bride
     * @return mplew
     */
    public static Packet sendWeddingInvitation(String groom, String bride) {
        OutPacket p = OutPacket.create(SendOpcode.MARRIAGE_RESULT);
        p.writeByte(15);
        p.writeString(groom);
        p.writeString(bride);
        p.writeShort(1); // 0 = Cathedral Normal?, 1 = Cathedral Premium?, 2 = Chapel Normal?
        return p;
    }

    public static Packet sendWishList() { // fuck my life
        OutPacket p = OutPacket.create(SendOpcode.MARRIAGE_REQUEST);
        p.writeByte(9);
        return p;
    }

    /**
     * Handles all of WeddingWishlist packets
     *
     * @param mode
     * @param itemnames
     * @param items
     * @return mplew
     */
    public static Packet onWeddingGiftResult(byte mode, List<String> itemnames, List<Item> items) {
        OutPacket p = OutPacket.create(SendOpcode.WEDDING_GIFT_RESULT);
        p.writeByte(mode);
        switch (mode) {
            case 0xC: // 12 : You cannot give more than one present for each wishlist 
            case 0xE: // 14 : Failed to send the gift.
                break;

            case 0x09: { // Load Wedding Registry
                p.writeByte(itemnames.size());
                for (String names : itemnames) {
                    p.writeString(names);
                }
                break;
            }
            case 0xA: // Load Bride's Wishlist 
            case 0xF: // 10, 15, 16 = CWishListRecvDlg::OnPacket
            case 0xB: { // Add Item to Wedding Registry 
                // 11 : You have sent a gift | | 13 : Failed to send the gift. | 
                if (mode == 0xB) {
                    p.writeByte(itemnames.size());
                    for (String names : itemnames) {
                        p.writeString(names);
                    }
                }
                p.writeLong(32);
                p.writeByte(items.size());
                for (Item item : items) {
                    addItemInfo(p, item, true);
                }
                break;
            }
            default: {
                log.warn("Unknown Wishlist Mode: {}", mode);
                break;
            }
        }
        return p;
    }
} 