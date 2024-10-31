package menus;

import classes.MenuItems;
import classes.Order;
import classes.Restaurant;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;

import java.util.List;

import static menus.Util.*;

public class AddOrderMenu extends Menu {
    private LineReader reader;
    private String customerName;
    private String[] basket;
    private int discount;
    private Restaurant restaurant;

    protected AddOrderMenu(Terminal terminal, Restaurant restaurant, SelectedOrderIndex selectedOrderIndex) {
        super(terminal, restaurant.getOrders(), selectedOrderIndex);
        this.reader = LineReaderBuilder.builder().terminal(terminal).build();
        this.restaurant = restaurant;
    }

    @Override
    public MenuType display() {
        return handleInput();
    }

    @Override
    protected MenuType handleInput() {
        basket = promptForItems(terminal, MenuItems.get().keySet().toArray(new String[0]), null);
        terminal.writer().print("\033[?25h");
        customerName = promptForName(terminal, reader);
        discount = promptForDiscount(terminal, reader);
        printDelimitator(terminal);

        terminal.writer().println("\uF4FF Customer Name: " + customerName);
        terminal.writer().println("\uF02B Discount: " + discount);
        terminal.writer().print("\uDB80\uDC76 Items: ");
        for (String item : basket) {
            terminal.writer().print(item + " ");
        }
        terminal.writer().println();
        printDelimitator(terminal);

        boolean orderConfirmed = promptYesNo(terminal, reader, "\uDB85\uDC01 Would you like to save the order? (y/n):");
        terminal.writer().println();

        if (orderConfirmed) {
            restaurant.addOrder(new Order(basket, customerName, discount));
            System.out.println("\uF00C Order has been saved!");
        } else {
            System.out.println("\uF467 Order was not saved.");
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return MenuType.MAIN;
    }

    @Override
    protected void displayMenu() {}

    @Override
    protected void setMenuOptions() {}
}
