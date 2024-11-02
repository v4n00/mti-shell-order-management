package menus;

import classes.Order;
import classes.Restaurant;
import org.jline.terminal.Terminal;

import java.util.List;

public class GenerateReportMenu extends Menu {
    private final Restaurant restaurant;

    protected GenerateReportMenu(Terminal terminal, Restaurant restaurant, SelectedOrderIndex selectedOrderIndex) {
        super(terminal, restaurant.getOrders(), selectedOrderIndex);
        this.restaurant = restaurant;
    }

    @Override
    public MenuType display() {
        if(orders.isEmpty()) {
            return MenuType.MAIN;
        }
        while(true) {
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
                            selectedOption = Math.min(menuOptions.length - 1, selectedOption + 1);
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
    protected MenuType executeOption() {
        if(menuOptions[selectedOption] == MenuOption.MAIN) {
            return menuOptions[selectedOption].toMenuType();
}
        if(menuOptions[selectedOption] == MenuOption.TOP_FOODS) {
            restaurant.generateReportTopFoods("top_foods.txt");
            System.out.println("\uDB80\uDD93 Report generated and saved to top_foods.txt");
        } else if(menuOptions[selectedOption] == MenuOption.EARNINGS) {
            restaurant.generateReportEarnings("earnings.txt");
            System.out.println("\uDB80\uDD93 Report generated and saved to earnings.txt");
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return menuOptions[selectedOption].toMenuType();
    }

    @Override
    protected void displayMenu() {
        terminal.writer().print("\033[H\033[2J"); // Clear the screen
        terminal.writer().println("\uDB85\uDD17 Generate Report Menu");
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
                MenuOption.TOP_FOODS,
                MenuOption.EARNINGS,
                MenuOption.MAIN
        };
    }
}
