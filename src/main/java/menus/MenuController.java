package menus;

import classes.Restaurant;
import org.jline.terminal.Terminal;

public class MenuController {
    private MenuType currentMenu;
    private SelectedOrderIndex selectedOrderIndex;
    private final Terminal terminal;
    private final Restaurant restaurant;

    private final Menu mainMenu;
    private final Menu viewOrderMenu;
    private final Menu addOrderMenu;
    private final Menu generateReportMenu;

    public MenuController(Terminal terminal, Restaurant restaurant) {
        this.currentMenu = MenuType.MAIN;
        this.selectedOrderIndex = new SelectedOrderIndex(0);
        this.terminal = terminal;
        this.restaurant = restaurant;

        this.mainMenu = new MainMenu(terminal, restaurant, selectedOrderIndex);
        this.viewOrderMenu = new ViewOrderMenu(terminal, restaurant, selectedOrderIndex);
        this.addOrderMenu = new AddOrderMenu(terminal, restaurant, selectedOrderIndex);
        this.generateReportMenu = new GenerateReportMenu(terminal, restaurant, selectedOrderIndex);
    }

    public void start() {
        while (true) {
            switch (currentMenu) {
                case MAIN:
                    currentMenu = mainMenu.display();
                    break;
                case VIEW_ORDER:
                    currentMenu = viewOrderMenu.display();
                    break;
                case ADD_ORDER:
                    currentMenu = addOrderMenu.display();
                    break;
                case GENERATE_REPORT:
                    currentMenu = generateReportMenu.display();
                    break;
                case EXIT:
                    terminal.writer().println("Exiting and saving...");
                    terminal.writer().print("\033[?25h");
                    terminal.flush();
                    restaurant.saveOrders();
                    System.exit(0);
                default:
                    break;
            }
        }
    }
}
