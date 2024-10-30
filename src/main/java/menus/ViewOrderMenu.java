package menus;

import classes.Order;
import classes.Restaurant;
import org.jline.terminal.Terminal;

import java.util.List;

public class ViewOrderMenu extends Menu {
    private static final int PAGE_SIZE = 9;

    protected ViewOrderMenu(Terminal terminal, Restaurant restaurant, SelectedOrderIndex selectedOrderIndex) {
        super(terminal, restaurant.getOrders(), selectedOrderIndex);
    }

    @Override
    public MenuType display() {
        while(true) {
            displayOrder(orders.get(selectedOrderIndex.value));
            displayMenu();
            MenuType menuType = handleInput();
            if(menuType != null) {
                return menuType;
            }
        }
    }

    @Override
    protected MenuType handleInput() {
        try {
            int ch = terminal.reader().read();
            if (ch == 27) { // "ESC" key
                int next1 = terminal.reader().read();
                int next2 = terminal.reader().read();

                if (next1 == 91) { // "[" character in escape sequence
                    switch (next2) {
                        case 65:  // Up Arrow
                            selectedOption = Math.max(0, selectedOption - 1);
                            break;
                        case 66:  // Down Arrow
                            selectedOption = Math.min(MenuOption.count() - 1, selectedOption + 1);
                            break;
                    }
                }
            } else if (ch == 13) { // "Enter" key
                return executeOption();
            }
        } catch (Exception e) {
            terminal.writer().print("\033[?25h");
            System.exit(1);
        }
        return null;
    }

    @Override
    protected void displayMenu() {
        terminal.writer().print("\033[" + (PAGE_SIZE + orders.get(selectedOrderIndex.value).getItems().length) + ";0H");
        Util.printDelimitator(terminal);

        for (int i = 0; i < menuOptions.length; i++) {
            MenuOption option = menuOptions[i];
            if (i == selectedOption) {
                terminal.writer().println("\033[1;42m" + option.getLabel() + "\033[0m");
            } else {
                terminal.writer().println(option.getLabel());
            }
        }

        terminal.writer().print("\033[?25l");
        terminal.flush();
    }

    @Override
    protected void setMenuOptions() {
        menuOptions = new MenuOption[] {
                MenuOption.EDIT_ORDER,
                MenuOption.DELETE_ORDER,
                MenuOption.MAIN
        };
    }

    private void displayOrder(Order order) {
        terminal.writer().print("\033[H\033[2J"); // Clear the screen
        terminal.writer().println("\uF4FF Order for " + order.getCustomerName());
        Util.printDelimitator(terminal);

        terminal.writer().println("\uDB80\uDCED Time: " + Util.LONG_DATE_FORMAT.format(order.getDateOrdered()));
        terminal.writer().println("\uDB85\uDDAB Status: " + order.getStatus());
        terminal.writer().println("\uF02B Discount: " + order.getDiscount());
        terminal.writer().println("\uEFC7 Total: " + order.getTotal());
        terminal.writer().println("\uDB80\uDC76 Items:");
        for (String item : order.getItems()) {
            terminal.writer().println("\t\uF061 " + item);
        }
        terminal.writer().println("\uE279 Total: " + order.getTotal());
    }
}
