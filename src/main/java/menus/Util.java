package menus;

import classes.OrderStatus;
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

    public static String promptForName(Terminal terminal, LineReader reader, String lastName) {
        String name;
        while (true) {
            try {
                name = reader.readLine("\uF4FF Enter customer name (a-z, A-Z only)" + (lastName != null ? " (previous: " + lastName + ")" : "") + ": ").trim();
                if (name.matches("^[a-zA-Z]+$")) { // Only letters a-z, A-Z
                    return name;
                } else if(lastName != null && name.equals("")) {
                    return lastName;
                } else {
                    terminal.writer().println("\uEBFB Invalid input. Name must contain only letters.");
                }
            } catch (Exception e) {
                terminal.writer().println("\uEBFB Error reading input: " + e.getMessage());
            }
        }
    }

    public static int promptForDiscount(Terminal terminal, LineReader reader, float lastDiscount) {
        String discountStr;
        while (true) {
            try {
                discountStr = reader.readLine("\uF02B Enter discount (0-100)" + (lastDiscount != 0.0F ? " (previous: " + lastDiscount + ")" : "") + ": ").trim();
                if (discountStr.equals("") && lastDiscount != 0.0F) {
                    return (int) lastDiscount;
                }
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
            } else if(input.isEmpty()) {
                return true;
            }
        }
    }

    public static OrderStatus promptForStatus(Terminal terminal, LineReader reader, OrderStatus lastStatus) {
        terminal.writer().print("\033[?25h");
        while (true) {
            try {
                String statusStr = reader.readLine("\uF0E0 Enter status (Pending, Preparing, Completed)" + (lastStatus != null ? " (previous: " + lastStatus + ")" : "") + ": ").trim().toUpperCase();
                if(statusStr.equals("") && lastStatus != null) {
                    return lastStatus;
                }
                OrderStatus status = OrderStatus.valueOf(statusStr.toUpperCase());
                return status;
            } catch (IllegalArgumentException e) {
                terminal.writer().println("\uEBFB Invalid input. Please enter a valid status.");
            } catch (Exception e) {
                terminal.writer().println("\uEBFB Error reading input: " + e.getMessage());
            }
        }
    }

    public static String[] promptForItems(Terminal terminal, String[] menuItems, Map<String, Integer> basketOld) {
        Map<String, Integer> basket = basketOld == null ? new HashMap<>() : basketOld;
        int currentIndex = 0;

        try {
            while (true) {
                terminal.writer().println("\033[H\033[2J"); // Clear console
                terminal.writer().println(basketOld == null ? "\uF457 Add order: " : "\uF044 Edit order: ");
                printDelimitator(terminal);
                terminal.writer().println("\uDB80\uDC76 Select items to add to order ('p' to add, 'r' to remove, 'Enter' to confirm):");
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
                        currentIndex = switch (next2) {
                            case 65 -> // Up arrow
                                    (currentIndex - 1 + menuItems.length) % menuItems.length;
                            case 66 -> // Down arrow
                                    (currentIndex + 1) % menuItems.length;
                            default -> currentIndex;
                        };
                    }
                } else if (ch == 13) { // Enter
                    break;
                } else if (ch == 'r' || ch == 'R') {
                    String selectedItem = menuItems[currentIndex];
                    if (basket.getOrDefault(selectedItem, 0) > 0) {
                        basket.put(selectedItem, basket.get(selectedItem) - 1);
                    }
                } else if (ch == 'p' || ch == 'P') {
                    String selectedItem = menuItems[currentIndex];
                    basket.put(selectedItem, basket.getOrDefault(selectedItem, 0) + 1);
                }
            }
        } catch (Exception e) {
            terminal.writer().println("\uEBFB Error reading input: " + e.getMessage());
        }

        return generateBasketArray(basket);
    }

    public static Map<String, Integer> createBasketFromArray(String[] basket) {
        Map<String, Integer> basketMap = new HashMap<>();
        for (String item : basket) {
            basketMap.put(item, basketMap.getOrDefault(item, 0) + 1);
        }
        return basketMap;
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
