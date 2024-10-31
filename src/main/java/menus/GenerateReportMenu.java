package menus;

import classes.Order;
import classes.Restaurant;
import org.jline.terminal.Terminal;

import java.util.List;

public class GenerateReportMenu extends Menu {
    protected GenerateReportMenu(Terminal terminal, Restaurant restaurant, SelectedOrderIndex selectedOrderIndex) {
        super(terminal, restaurant.getOrders(), selectedOrderIndex);
    }

    @Override
    public MenuType display() {
        return null;
    }

    @Override
    protected MenuType handleInput() {
        return null;
    }

    @Override
    protected void displayMenu() {

    }

    @Override
    protected void setMenuOptions() {

    }
}
