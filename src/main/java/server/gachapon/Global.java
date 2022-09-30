package server.gachapon;

import constants.id.ItemId;

/**
 * @author Alan (SharpAceX)
 * @author Ronan - added ores and reworked global loots
 */

public class Global extends GachaponItems {

    @Override
    public int[] getCommonItems() {
        return new int[]{
                /* Potions */
                2000004, 2000005, 2001002, 2001001, 2020012, 2020013, 2020014, 2020015
        };
    }

    @Override
    public int[] getUncommonItems() {
        return new int[]{
                /* Buff Potions */
                2022179, 2022273, 2022182, 2022285,

                /* Scrolls */
                
                
                /* Chairs */
                3010063, 3010064, 3012011
        };
    }

    @Override
    public int[] getRareItems() {
        return new int[]{
        		2049003
                
        };
    }

}
