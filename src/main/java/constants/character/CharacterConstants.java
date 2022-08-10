package constants.character;

import client.Disease;
import client.Job;
import config.YamlConfig;
import constants.id.MapId;
import constants.skills.Aran;
import provider.*;
import provider.wz.WZFiles;
import server.maps.FieldLimit;
import server.maps.MapleMap;
import server.quest.Quest;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/*
 * @author kevintjuh93
 * @author Ronan
 */
public class CharacterConstants {
    
    //Level up HP/MP constants
    public static int BEGINNER_HP_GAIN_MINIMUM = 12;
    public static int BEGINNER_HP_GAIN_MAXIMUM = 16;
    public static int BEGINNER_MP_GAIN_MINIMUM = 10;
    public static int BEGINNER_MP_GAIN_MAXIMUM = 12;
    
    public static int WARRIOR_HP_GAIN_MINIMUM = 24;
    public static int WARRIOR_HP_GAIN_MAXIMUM = 28;
    public static int WARRIOR_MP_GAIN_MINIMUM = 4;
    public static int WARRIOR_MP_GAIN_MAXIMUM = 6;
    
    public static int MAGE_HP_GAIN_MINIMUM = 10;
    public static int MAGE_HP_GAIN_MAXIMUM = 14;
    public static int MAGE_MP_GAIN_MINIMUM = 22;
    public static int MAGE_MP_GAIN_MAXIMUM = 24;
    
    public static int ARCHER_HP_GAIN_MINIMUM = 20;
    public static int ARCHER_HP_GAIN_MAXIMUM = 24;
    public static int ARCHER_MP_GAIN_MINIMUM = 14;
    public static int ARCHER_MP_GAIN_MAXIMUM = 16;
    
    public static int THIEF_HP_GAIN_MINIMUM = 20;
    public static int THIEF_HP_GAIN_MAXIMUM = 24;
    public static int THIEF_MP_GAIN_MINIMUM = 14;
    public static int THIEF_MP_GAIN_MAXIMUM = 16;
    
    public static int PIRATE_HP_GAIN_MINIMUM = 22;
    public static int PIRATE_HP_GAIN_MAXIMUM = 28;
    public static int PIRATE_MP_GAIN_MINIMUM = 18;
    public static int PIRATE_MP_GAIN_MAXIMUM = 23;
    
    public static int AP_ASSIGNMENT_HP = 100;
    public static int AP_ASSIGNMENT_MP = 100;
    
    public static final String LEVEL_200 = "[Congrats] %s has reached Level %d! Congratulate %s on such an amazing achievement!";
    public static final String[] BLOCKED_NAMES = {"admin", "owner", "moderator", "intern", "donor", "administrator", "FREDRICK", "help", "helper", "alert", "notice", "maplestory", "fuck", "wizet", "fucking", "negro", "fuk", "fuc", "penis", "pussy", "asshole", "gay",
            "nigger", "homo", "suck", "cum", "shit", "shitty", "condom", "security", "official", "rape", "nigga", "sex", "tit", "boner", "orgy", "clit", "asshole", "fatass", "bitch", "support", "gamemaster", "cock", "gaay", "gm",
            "operate", "master", "sysop", "party", "GameMaster", "community", "message", "event", "test", "meso", "Scania", "yata", "AsiaSoft", "henesys"};  
}
