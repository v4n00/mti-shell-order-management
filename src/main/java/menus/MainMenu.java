package menus;

import classes.Order;
import classes.Restaurant;
import org.jline.terminal.Terminal;

import static menus.Util.*;

public class MainMenu extends Menu {

    private static final int ITEMS_PER_PAGE = 10;
    private static final int PAGE_SIZE = ITEMS_PER_PAGE + 4;

    private int currentPage;
    private boolean isOnMenuSide;

    public MainMenu(Terminal terminal, Restaurant restaurant, SelectedOrderIndex selectedOrderIndex) {
        super(terminal, restaurant.getOrders(), selectedOrderIndex);

        this.currentPage = 0;
        this.isOnMenuSide = true;
    }

    @Override
    public MenuType display() {
        while(true) {
            displayOrders();
            displayMenu();
            MenuType menuType = handleInput();
            if(menuType != null) {
                return menuType;
            }
        }
    }

    protected MenuType handleInput() {
        try {
            int ch = terminal.reader().read();
            if (ch == 27) { // "ESC" key
                int next1 = terminal.reader().read();
                int next2 = terminal.reader().read();

                if (next1 == 91) { // "[" character in escape sequence
                    switch (next2) {
                        case 67:  // Right Arrow
                            if (hasMoreOrders()) {
                                currentPage++;
                            }
                            break;
                        case 68:  // Left Arrow
                            if (currentPage > 0) {
                                currentPage--;
                            }
                            break;
                        case 65:  // Up Arrow
                            if(isOnMenuSide) {
                                selectedOption = Math.max(0, selectedOption - 1);
                            }
                            else {
                                selectedOrderIndex.value = Math.max(0, selectedOrderIndex.value - 1);
                            }
                            break;
                        case 66:  // Down Arrow
                            if(isOnMenuSide) {
                                selectedOption = Math.min(MenuOption.count() - 1, selectedOption + 1);
                            }
                            else {
                                selectedOrderIndex.value = Math.min(orders.size() - 1, selectedOrderIndex.value + 1);
                            }
                            break;
                    }
                }
            } else if (ch == 9) { // "Tab" key
                isOnMenuSide = !isOnMenuSide;
            } else if (ch == 13) { // "Enter" key
                if(isOnMenuSide) {
                    return executeOption();
                }
                else {
                    return MenuType.VIEW_ORDER;
                }
            }
        } catch (Exception e) {
            terminal.writer().print("\033[?25h");
            System.exit(1);
        }
        return null;
    }

    @Override
    protected void setMenuOptions() {
        menuOptions = new MenuOption[] { MenuOption.VIEW_ORDER, MenuOption.ADD_ORDER, MenuOption.EDIT_ORDER,
                MenuOption.DELETE_ORDER, MenuOption.SORT, MenuOption.GENERATE_REPORT, MenuOption.EXIT };
    }

    private boolean hasMoreOrders() {
        return (currentPage + 1) * ITEMS_PER_PAGE < orders.size();
    }

    protected void displayMenu() {
        terminal.writer().print("\033[" + PAGE_SIZE + ";0H");
        printDelimitator(terminal);

        for (int i = 0; i < menuOptions.length; i++) {
            MenuOption option = menuOptions[i];
            if (i == selectedOption) {
                if(isOnMenuSide) {
                    terminal.writer().println("\033[1;42m" + option.getLabel() + "\033[0m");
                }
                else {
                    terminal.writer().println("\033[1;47m" + option.getLabel() + "\033[0m");
                }
            } else {
                terminal.writer().println(option.getLabel());
            }
        }

        terminal.writer().print("\033[?25l");
        terminal.flush();
    }

    private void displayOrders() {
        terminal.writer().print("\033[H\033[2J"); // Clear the screen
        terminal.writer().println("\uDB80\uDE5A Restaurant Orders (Page " + (currentPage + 1) + "):");
        printDelimitator(terminal);

        if(orders.isEmpty()) {
            terminal.writer().println("No orders available.");
            terminal.flush();
            return;
        }

        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, orders.size());

        terminal.writer().println(String.format("  %-14s %-12s %-17s %-11s %-10s", "\uDB80\uDCED Date", "\uDB85\uDDAB Status", "\uF4FF Customer", "\uEFC7 Total", "\uDB80\uDC76 Items"));

        for (int i = start; i < end; i++) {
            Order currentOrder = orders.get(i);
            if(i == selectedOrderIndex.value) {
                terminal.writer().print(isOnMenuSide ? "\033[1;47m" : "\033[1;42m");
            }

            terminal.writer().print('\uF061');
            terminal.writer().print(' ');
            terminal.writer().print(SHORT_DATE_FORMAT.format(currentOrder.getDateOrdered()));
            terminal.writer().print(" - ");
            terminal.writer().print(fixedLengthString(currentOrder.getStatus().toString(), 9));
            terminal.writer().print(" - ");
            terminal.writer().print(fixedLengthString(currentOrder.getCustomerName(), 15));
            terminal.writer().print(" - ");
            terminal.writer().print(fixedLengthString(String.format("%.2f", currentOrder.getTotal()), 9));
            terminal.writer().print(" - ");

            terminal.writer().print("Items: ");
            int length = currentOrder.getItems().length;
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < length; j++) {
                sb.append(currentOrder.getItems()[j]);
                if (j < length - 1) {
                    sb.append(", ");
                }
            }
            terminal.writer().print(fixedLengthString(sb.toString(), 30));

            if(i == selectedOrderIndex.value) {
                terminal.writer().print("\033[0m");
            }
            terminal.writer().println();
        }

        terminal.flush();
    }
}
