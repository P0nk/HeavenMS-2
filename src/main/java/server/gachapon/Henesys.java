package server.gachapon;

/**
 * @author Alan (SharpAceX) - gachapon source classes stub
 * @author Ronan - parsed MapleSEA loots
 * <p>
 * MapleSEA-like loots thanks to AyumiLove - src: https://ayumilovemaple.wordpress.com/maplestory-gachapon-guide/
 */

public class Henesys extends GachaponItems {

    @Override
    public int[] getCommonItems() {
        return new int[]{

                /* Scroll */
        		2040031, 2040318, 2040627, 2041308, 2040502, 2040702, 2040802, 2041020, 2040029, 2040317, 2040625, 2048012, 2041307,
        		2040501, 2040701, 2040801, 2041019, 2044501, 2044502, 2044601, 2044602,         		        	                    

                /* Bowman equipment*/
                1452017, 1462018, 1462009, 1462007, 1462004, 1452009, 1452008, 1452007, 1002270, 1002405, 1002547, 1050059, 1051042,
                1050077, 1051068, 1050088, 1051082, 1052071, 1072164, 1072203, 1082125, 1082108
        };
    }

    @Override
    public int[] getUncommonItems() { // bow, xbow scrolls and dead mine map
        return new int[]{
        		2044505, 2044605, 2044504, 2044604, 2030007,
        		/*Glove Scrolls */
        		2040804, 2040805, 2040803,
        		/* palm tree beach chair */
        		3010018
        		
        };
    }

    @Override
    public int[] getRareItems() {
        return new int[]{1082149, 1452044, 1462039, 2049100};
    }

}
