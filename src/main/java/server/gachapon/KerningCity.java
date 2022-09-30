package server.gachapon;

/**
 * @author Alan (SharpAceX) - gachapon source classes stub & pirate equipment
 * @author Ronan - parsed MapleSEA loots
 * <p>
 * MapleSEA-like loots thanks to AyumiLove - src: https://ayumilovemaple.wordpress.com/maplestory-gachapon-guide/
 */

public class KerningCity extends GachaponItems {

    @Override
    public int[] getCommonItems() {
        return new int[]{

                /* Scroll */
        		2040319, 2040321, 2040323, 2040425, 2040427, 2040516, 2040517, 2040924, 2040925, 2041022, 2041023, 
        		2040331, 2041309, 2041310, 2041311, 2044701, 2044702, 2044702, 2043302,

                /* Thief equipment */
                1472010, 1472029, 1041048, 1041095, 1060031, 1061033, 1041049, 1472011, 1040096, 1472033, 1332026,
                1051006, 1082074, 1472025, 1061106, 1040084, 1332015, 1472000, 1332019, 1002183, 1002209, 1092020,
                1332029, 1092019, 1061099, 1060106, 1040032, 1040059, 1332003, 1040060, 1060046, 1472005, 1332027,

        };
    }

    @Override
    public int[] getUncommonItems() {
        return new int[]{
        		1092050, // khanjar
        		/*Glove Scrolls */
        		2040804, 2040805, 2040803,
        		/* weapon scrolls */
        		2044704, 2044705, 2043304, 2043305,
        		3010092 //chair
        		};
    }

    @Override
    public int[] getRareItems() {
        return new int[]{
        		1102041, 2070016, 1332050, 1332049, 1472052, 1472051
        };
    }

}
