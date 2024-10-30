package menus;

import org.jline.terminal.Terminal;

import java.text.SimpleDateFormat;

public class Util {
    public static String fixedLengthString(String string, int length) {
        if (string.length() > length) {
            return string.substring(0, length - 3) + "...";
        } else {
            return String.format("%1$-" + length + "s", string);
        }
    }

    public static void printDelimitator(Terminal terminal) {
        terminal.writer().println("━".repeat(100));
    }

    public static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("HH:mm dd/MM");
    public static final SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
}
