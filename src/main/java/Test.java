import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class Test {
    private static String[] orders = {
            "Order #1: Burger and Fries", "Order #2: Pasta and Salad", "Order #3: Pizza and Soda",
            "Order #4: Steak and Mashed Potatoes", "Order #5: Fish and Chips", "Order #6: Soup and Sandwich",
            "Order #7: Salad and Smoothie", "Order #8: Tacos and Rice", "Order #9: Sushi and Miso Soup",
            "Order #10: Spaghetti and Meatballs", "Order #11: Pancakes and Sausage", "Order #12: Omelette and Toast",
            "Order #13: Burrito and Nachos", "Order #14: Fried Rice and Spring Rolls", "Order #15: Ramen and Tempura"
    };

    private static String[] menuOptions = {
            "View Order Details", "Mark as Completed", "Cancel Order", "Exit"
    };

    private static final int PAGE_SIZE = 10;  // Number of orders per page
    private static int currentPage = 0;       // Track current page
    private static int selectedOption = 0;    // Track selected menu item
    private static final int MENU_START_LINE = PAGE_SIZE + 2;  // Position for menu display

    public static void main(String[] args) throws IOException {
        // Open terminal in raw mode
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        terminal.enterRawMode();  // Enter raw mode after the terminal is built

        while (true) {
            displayOrders(terminal);  // Display orders for the current page
            displayMenu(terminal);    // Display menu

            try {
                int ch = terminal.reader().read();  // Read individual character
                if (ch == 27) {  // ESC key, start of an escape sequence
                    int next1 = terminal.reader().read();
                    int next2 = terminal.reader().read();
                    if (next1 == 91) {  // '[' character in escape sequence
                        switch (next2) {
                            case 65:  // Arrow up
                                if (selectedOption == 0 && currentPage > 0) {
                                    // Go to previous page if at the top of current page
                                    currentPage--;
                                    selectedOption = PAGE_SIZE - 1;
                                } else {
                                    selectedOption = Math.max(0, selectedOption - 1);
                                }
                                break;
                            case 66:  // Arrow down
                                if (selectedOption == PAGE_SIZE - 1 && hasMoreOrders()) {
                                    // Go to next page if at the bottom of current page and more orders
                                    currentPage++;
                                    selectedOption = 0;
                                } else {
                                    selectedOption = Math.min(PAGE_SIZE - 1, selectedOption + 1);
                                }
                                break;
                        }
                    }
                } else if (ch == 13) {  // Enter key
                    if (menuOptions[selectedOption].equals("Exit")) {
                        terminal.writer().println("Exiting the application...");
                        terminal.flush();
                        break;
                    }
                    executeOption(terminal);
                }
            } catch (UserInterruptException | EndOfFileException e) {
                break;  // Exit on Ctrl+C or EOF
            }
        }

        terminal.close();
    }

    // Display orders for the current page
    private static void displayOrders(Terminal terminal) {
        terminal.writer().print("\033[H\033[2J");  // Clear screen
        terminal.writer().println("Restaurant Orders (Page " + (currentPage + 1) + "):");

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, orders.length);

        for (int i = start; i < end; i++) {
            terminal.writer().println(orders[i]);
        }

        terminal.flush();
    }

    // Display menu with cursor repositioning and highlighting
    private static void displayMenu(Terminal terminal) {
        // Move the cursor to the start of the menu
        terminal.writer().print("\033[" + MENU_START_LINE + ";0H");  // Move cursor to menu start

        // Print each menu item, highlighting the selected one with color or bold
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedOption) {
                terminal.writer().println("\033[1;32m" + menuOptions[i] + "\033[0m");  // Bold green text for selected option
            } else {
                terminal.writer().println(menuOptions[i]);
            }
        }
        terminal.flush();
    }

    // Check if there are more orders for pagination
    private static boolean hasMoreOrders() {
        return (currentPage + 1) * PAGE_SIZE < orders.length;
    }

    // Execute the selected menu option
    private static void executeOption(Terminal terminal) {
        terminal.writer().print("\033[" + (MENU_START_LINE + menuOptions.length + 1) + ";0H");  // Move below menu
        switch (menuOptions[selectedOption]) {
            case "View Order Details":
                terminal.writer().println("Displaying details for selected order...");
                break;
            case "Mark as Completed":
                terminal.writer().println("Marking selected order as completed...");
                break;
            case "Cancel Order":
                terminal.writer().println("Canceling selected order...");
                break;
            case "Exit":
                // Exit is handled in the main loop
                break;
            default:
                terminal.writer().println("Invalid selection.");
        }
        terminal.flush();
    }
}
