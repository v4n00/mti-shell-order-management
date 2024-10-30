import classes.Order;
import classes.Restaurant;
import menus.MenuController;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        terminal.enterRawMode();

        Restaurant restaurant = new Restaurant("menu.txt", "orders.txt");
        restaurant.addOrder(new Order(new String[]{"Pizza", "Pasta"}, "Alice", 10));
        restaurant.addOrder(new Order(new String[]{"Burger", "Burger"}, "Bob", 20));
        restaurant.addOrder(new Order(new String[]{"Salad", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda", "Soda"}, "Charlie", 30));
        restaurant.addOrder(new Order(new String[]{"Salad", "Soda"}, "Charlie", 30));
        restaurant.addOrder(new Order(new String[]{"Salad", "Soda"}, "Charlie", 30));
        restaurant.addOrder(new Order(new String[]{"Salad", "Soda"}, "Charlie", 30));
        restaurant.addOrder(new Order(new String[]{"Salad", "Soda"}, "Charlie", 30));


        new MenuController(terminal, restaurant).start();
    }
}
