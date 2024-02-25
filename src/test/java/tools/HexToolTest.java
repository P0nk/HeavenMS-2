package tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HexToolTest {

    @Test
    void bytesToHexString() {
        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 127, -1};
        String expectedHexString = "01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10 11 7F FF";
        assertEquals(expectedHexString, HexTool.toHexString(bytes));
    }

    @Test
    void bytesToCompactHexString() {
        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 127, -1};
        String expectedHexString = "0102030405060708090A0B0C0D0E0F10117FFF";
        assertEquals(expectedHexString, HexTool.toCompactHexString(bytes));
    }

    @Test
    void hexStringWithSpacesToBytes() {
        String hexString = "01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10 11 7F FF";
        byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 127, -1};
        assertArrayEquals(expectedBytes, HexTool.toBytes(hexString));
    }

    @Test
    void compactHexStringToBytes() {
        String hexString = "0102030405060708090A0B0C0D0E0F10117FFF";
        byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 127, -1};
        assertArrayEquals(expectedBytes, HexTool.toBytes(hexString));
    }

    @Test
    void lowerCaseHexStringToBytes() {
        String lowerCaseHexString = "01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 7f ff";
        byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 127, -1};
        assertArrayEquals(expectedBytes, HexTool.toBytes(lowerCaseHexString));
    }

    @Test
    void lowerCaseCompactHexStringToBytes() {
        String hexString = "0102030405060708090a0b0c0d0e0f10117fff";
        byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 127, -1};
        assertArrayEquals(expectedBytes, HexTool.toBytes(hexString));
    }

    @Test
    void toStringFromAscii() {
        byte[] asciiBytes = new byte[]{1, 10, 20, 30, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 0, 31};
        String expectedString = "....0123456789..";
        assertEquals(expectedString, HexTool.toStringFromAscii(asciiBytes));
    }
}