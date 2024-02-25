package model;

import java.util.Objects;

public record Note(int id, String message, String from, String to, long timestamp, int fame) {
    private static final int PLACEHOLDER_ID = -1;

    public Note {
        Objects.requireNonNull(message);
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
    }

    public static Note createNormal(String message, String from, String to, long timestamp) {
        return new Note(PLACEHOLDER_ID, message, from, to, timestamp, 0);
    }

    public static Note createGift(String message, String from, String to, long timestamp) {
        return new Note(PLACEHOLDER_ID, message, from, to, timestamp, 1);
    }
}
