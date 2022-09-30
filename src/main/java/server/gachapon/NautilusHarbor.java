package server.gachapon;

/**
 * @author Alan (SharpAceX) - gachapon source classes stub & pirate items
 * @author Ronan - parsed MapleSEA loots
 * <p>
 * MapleSEA-like loots thanks to AyumiLove - src: https://ayumilovemaple.wordpress.com/maplestory-gachapon-guide/
 */

public class NautilusHarbor extends GachaponItems {

    @Override
    public int[] getCommonItems() {
        return new int[]{
                /* Scroll */
        		2044901, 2044902, 2044801, 2044802, 2040406, 2040407, 2040531, 2040533, 2041034, 2041035, 2041312, 2041313,
        		2040028, 2040030, 2040306, 2040307, 2040508, 2040509, 2040610, 2040611, 2040808, 2040809, 2041038, 2041039,
        		2041316, 2041317, 2040714, 2040715,
        		2330003, // vital bullet
                /* Pirate equipment */
                1492011, 1492009, 1492007, 1492010, 1492008, 1482011, 1482010, 1482009, 1482008, 1052107, 1052110, 1052113,
                1052116, 1052119, 1052122, 1052125, 1052128, 1052134, 1072300, 1072303, 1072306, 1072309, 1072312, 1072315,
                1072318, 1072321, 1082216, 1082210, 1082207, 1082204, 1082201, 1082198, 1082195
        };
    }

    @Override
    public int[] getUncommonItems() {
        return new int[]{
        		/*Glove Scrolls */
        		2040804, 2040805, 2040803,
        		/* weapon scrolls */
        		2044903, 2044904, 2044803, 2044804,
        		2330004 // shiny bullet
        		
        };
    }

    @Override
    public int[] getRareItems() {
        return new int[]{
        		1482013, 1492013, 1102084, 2330005
        		
        };
    }

}
