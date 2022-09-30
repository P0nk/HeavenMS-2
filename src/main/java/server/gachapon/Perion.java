package server.gachapon;

/**
 * @author Alan (SharpAceX) - gachapon source classes stub & pirate equipment
 * @author Ronan - parsed MapleSEA loots
 * <p>
 * MapleSEA-like loots thanks to AyumiLove - src: https://ayumilovemaple.wordpress.com/maplestory-gachapon-guide/
 */

public class Perion extends GachaponItems {

    @Override
    public int[] getCommonItems() {
        return new int[]{

                /* Scrolls */
                2040417, 2040418, 2040419, 2040530, 2040532, 2040534, 2041012, 2041013, 2041014, 2048010, 2041300, 2041301,
                2041302, 2044301, 2044302, 2044401, 2044402, 2043001, 2043002, 2044001, 2044002, 2043201, 2043202,

                /* Warrior equipment */
                1050000, 1051078, 1051000, 1050083, 1052075, 1082036, 1082010, 1082061, 1082105, 1082117, 1082130, 1082168,
                1072002, 1072132, 1072148, 1072156, 1072211, 1072198, 1072220, 1072273, 1092007, 1092014, 1092010, 1092017,
                1092025, 1092026, 1092038, 1092060, 1432022, 1432023, 1432011, 1432030, 1442025, 1442031, 1442032, 1442033,
                1442019, 1442020, 1442044, 1322017, 1322018, 1322019, 1322020, 1322028, 1322029, 1322045, 1302010, 1302012,
                1302023, 1402013, 1402022, 1402005, 1402035, 1402036
        };
    }

    @Override
    public int[] getUncommonItems() {
        return new int[]{
                /*Glove Scrolls and shield and spear and polearm and swords and 1h bw */
                2040804, 2040805, 2040803, 2040914, 2040915, 2044304, 2044305, 2044404, 2044405, 2043004, 2043005, 2044004,
                2044005, 2043204, 2043205,

                3010012, // chair
                1432013, 1442018, 1302080, 1302063, 1402044
        };
    }

    @Override
    public int[] getRareItems() {
        return new int[]{
                1082223, 1432038, 1442057, 1322062, 1302059

        };
    }

}
