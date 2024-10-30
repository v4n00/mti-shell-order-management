package menus;

import classes.Restaurant;
import org.jline.terminal.Terminal;

public class MenuController {
    private MenuType currentMenu;
    private SelectedOrderIndex selectedOrderIndex;
    private final Menu mainMenu;
    private final Menu viewOrderMenu;
    private final Terminal terminal;

    public MenuController(Terminal terminal, Restaurant restaurant) {
        this.currentMenu = MenuType.MAIN;
        this.selectedOrderIndex = new SelectedOrderIndex(0);
        this.terminal = terminal;

        this.mainMenu = new MainMenu(terminal, restaurant, selectedOrderIndex);
        this.viewOrderMenu = new ViewOrderMenu(terminal, restaurant, selectedOrderIndex);
    }

    public void start() {
        // TODO:
        // add order
        // edit order
        // delete order
        // sort order
        // generate report
        while (true) {
            switch (currentMenu) {
                case MAIN:
                    currentMenu = mainMenu.display();
                    break;
                case VIEW_ORDER:
                    currentMenu = viewOrderMenu.display();
                    break;
                case EXIT:
                    terminal.writer().println("Exiting the application...");
                    terminal.writer().print("\033[?25h");
                    terminal.flush();
                    System.exit(0);
                default:
                    break;
            }
        }
    }
}
