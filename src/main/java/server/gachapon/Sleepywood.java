package server.gachapon;

/**
 * @author Alan (SharpAceX) - gachapon source classes stub & pirate equipment
 * @author Ronan - parsed MapleSEA loots
 * <p>
 * MapleSEA-like loots thanks to AyumiLove - src: https://ayumilovemaple.wordpress.com/maplestory-gachapon-guide/
 */

public class Sleepywood extends GachaponItems {

    @Override
    public int[] getCommonItems() {
        return new int[]{

                /* Scroll */
                2048003, 2048000, 2040601, 2044501, 2041019, 2041016, 2041022, 2041010, 2041013, 2043301, 2040301, 2040801, 2040001,
                2040004, 2043101, 2043201, 2043001, 2040504, 2040501, 2048004, 2048001, 2044401, 2040901, 2040701, 2040704, 2040707,
                2044301, 2043801, 2044101, 2044201, 2044001, 2040602, 2044502, 2041020, 2041017, 2041023, 2041014, 2041005, 2044702,
                2044602, 2043302, 2040302, 2040802, 2040005, 2043202, 2043002, 2040505, 2040502, 2048005, 2048002, 2044402, 2040902,
                2040702, 2040705, 2040708, 2044302, 2043802, 2044202, 2044002, 2044801, 2044903, 2044814,
                2070005,
                /* Common equipment */
                1032003, 1432009, 1102014, 1102018, 1002392, 1322026, 1032022, 1312012, 1332020, 1092030, 1032016, 1032015, 1032014,
                1322024, 1032013, 1322022, 1102016, 1032012, 1032023, 1402014, 1032000, 1102017,

        };
    }

    @Override
    public int[] getUncommonItems() {
        return new int[]{
        		3010069
        };
    }

    @Override
    public int[] getRareItems() {
        return new int[]{1082149, 1372040, 1372041};
    }

}
