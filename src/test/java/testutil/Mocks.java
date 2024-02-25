package testutil;

import client.Character;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class Mocks {

    public static Character chr() {
        return Mockito.mock(Character.class);
    }

    public static Character chr(String name) {
        var chr = chr();
        when(chr.getName()).thenReturn(name);
        return chr;
    }
}
