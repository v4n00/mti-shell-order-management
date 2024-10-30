package menus;

import classes.Order;
import org.jline.terminal.Terminal;

import java.util.List;

public abstract class Menu {
    protected Menu(Terminal terminal, List<Order> orders, SelectedOrderIndex selectedOrderIndex) {
        this.terminal = terminal;
        this.orders = orders;
        this.selectedOrderIndex = selectedOrderIndex;
        setMenuOptions();
    }

    protected MenuType executeOption() {
        return menuOptions[selectedOption].toMenuType();
    }

    abstract public MenuType display();
    abstract protected MenuType handleInput();
    abstract protected void displayMenu();
    abstract protected void setMenuOptions();

    protected final Terminal terminal;
    protected final List<Order> orders;
    protected final SelectedOrderIndex selectedOrderIndex;
    protected MenuOption[] menuOptions;
    protected int selectedOption;
}
