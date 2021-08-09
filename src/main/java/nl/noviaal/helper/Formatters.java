package nl.noviaal.helper;

import java.time.format.DateTimeFormatter;

public final class Formatters {

    private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Formatters() {}

    public static DateTimeFormatter dateTimeFormatter() {
        return DTF;
    }
}
