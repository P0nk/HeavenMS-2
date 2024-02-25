package client;

import java.time.LocalDate;
import java.time.LocalDateTime;

final public class DefaultDates {
    // May 11 2005 is the date MapleGlobal released, so it's a symbolic default value

    private DefaultDates() {
    }

    public static LocalDate getBirthday() {
        return LocalDate.parse("2005-05-11");
    }

    public static LocalDateTime getTempban() {
        return LocalDateTime.parse("2005-05-11T00:00:00");
    }
}
