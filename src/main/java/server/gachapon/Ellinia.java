package server.gachapon;

/**
 * @author Alan (SharpAceX) - gachapon source classes stub & pirate equipment
 * @author Ronan - parsed MapleSEA loots
 * <p>
 * MapleSEA-like loots thanks to AyumiLove - src: https://ayumilovemaple.wordpress.com/maplestory-gachapon-guide/
 */

public class Ellinia extends GachaponItems {

    @Override
    public int[] getCommonItems() {
        return new int[]{
        		
                /* Scroll */
        		2040024, 2040025, 2040026, 2040205, 2040206, 2040207, 2040301, 2040302, 2040512, 2040513, 2040514, 2041015, 2041016,
                2041017, 2048011, 2041303, 2041304, 2041305, 2040918, 2040919, 2040920, 2043701, 2043702, 2043801,
                2043802,

                /* Magician equipment */
                1002579, 1002218, 1002272, 1051030, 1050048, 1050067, 1051054, 1051094, 1051094, 1052076, 1072137, 1072160, 1072209,
                1072268, 1082086, 1082123, 1082133, 1082154, 1082164, 1382041, 1382014, 1382010, 1382016, 1382036, 1372012, 1372017,
                1702032, 1372031, 1372009
        };
    }

    @Override
    public int[] getUncommonItems() {
        return new int[]{ // elemental wands and staves
        		1382045, 1382046, 1382047, 1382048, 1372035, 1372036, 1372037, 1372038, 1372032,
        		2043704, 2043705, 2043804, 2043805, 2040816, 2040817//30% scrolls and gloves
        		,3010017 // chair
        		
        };
    }

    @Override
    public int[] getRareItems() {
        return new int[]{ // elemental staves and wands higher lvl
        	1382049, 1382050, 1382051, 1382052, 1372042, 1102042}
        ;
    }

}
