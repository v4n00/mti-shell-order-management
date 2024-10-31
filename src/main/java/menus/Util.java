package menus;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;

import java.text.SimpleDateFormat;
import java.util.*;

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

    public static String promptForName(Terminal terminal, LineReader reader) {
        String name;
        while (true) {
            try {
                name = reader.readLine("\uF4FF Enter customer name (a-z, A-Z only): ").trim();
                if (name.matches("^[a-zA-Z]+$")) { // Only letters a-z, A-Z
                    return name;
                } else {
                    terminal.writer().println("\uEBFB Invalid input. Name must contain only letters.");
                }
            } catch (Exception e) {
                terminal.writer().println("\uEBFB Error reading input: " + e.getMessage());
            }
        }
    }

    public static int promptForDiscount(Terminal terminal, LineReader reader) {
        String discountStr;
        while (true) {
            try {
                discountStr = reader.readLine("\uF02B Enter discount (0-100): ").trim();
                int discountValue = Integer.parseInt(discountStr);

                if (discountValue >= 0 && discountValue <= 100) {
                    return discountValue;
                } else {
                    terminal.writer().println("\uEBFB Invalid input. Discount must be between 0 and 100.");
                }
            } catch (NumberFormatException e) {
                terminal.writer().println("\uEBFB Invalid input. Please enter a numeric value.");
            } catch (Exception e) {
                terminal.writer().println("\uEBFB Error reading input: " + e.getMessage());
            }
        }
    }

    public static boolean promptYesNo(Terminal terminal, LineReader reader, String message) {
        terminal.writer().print("\033[?25h");
        while (true) {
            String input = reader.readLine(message).trim().toLowerCase();
            if ("y".equals(input)) {
                return true;
            } else if ("n".equals(input)) {
                return false;
            } else {
                terminal.writer().print("\uEBFB Invalid input. Please enter 'y' or 'n':");
                terminal.flush();
            }
        }
    }

    public static String[] promptForItems(Terminal terminal, String[] menuItems, Map<String, Integer> basketOld) {
        Map<String, Integer> basket = basketOld == null ? new HashMap<>() : basketOld;
        int currentIndex = 0;

        try {
            while (true) {
                terminal.writer().println("\033[H\033[2J"); // Clear console
                terminal.writer().println("\uF457 Add order: ");
                printDelimitator(terminal);
                terminal.writer().println("\uDB80\uDC76 Select items to add to order (Press 'c' to confirm):");
                for (int i = 0; i < menuItems.length; i++) {
                    int noItems = basket.getOrDefault(menuItems[i], 0);
                    if (i == currentIndex) {
                        terminal.writer().println("\033[1;42m\uF061 [" + noItems + "] " + menuItems[i] + "\033[0m");
                    } else {
                        terminal.writer().println("\uF061 [" + noItems+ "] " + menuItems[i]);
                    }
                }
                terminal.flush();

                int ch = terminal.reader().read();

                if (ch == 27) {
                    int next1 = terminal.reader().read();
                    int next2 = terminal.reader().read();

                    if (next1 == 91) {
                        switch (next2) {
                            case 65: // Up arrow
                                currentIndex = (currentIndex - 1 + menuItems.length) % menuItems.length;
                                break;
                            case 66: // Down arrow
                                currentIndex = (currentIndex + 1) % menuItems.length;
                                break;
                        }
                    }
                } else if (ch == 13) { // Enter
                    String selectedItem = menuItems[currentIndex];
                    basket.put(selectedItem, basket.getOrDefault(selectedItem, 0) + 1);
                    terminal.writer().println("Added " + selectedItem + " to basket. Quantity: " + basket.get(selectedItem));
                } else if (ch == 'c' || ch == 'C') {
                    break;
                }
            }
        } catch (Exception e) {
            terminal.writer().println("\uEBFB Error reading input: " + e.getMessage());
        }

        return generateBasketArray(basket);
    }

    private static String[] generateBasketArray(Map<String, Integer> basket) {
        List<String> basketList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : basket.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                basketList.add(entry.getKey());
            }
        }
        return basketList.toArray(new String[0]);
    }
}
