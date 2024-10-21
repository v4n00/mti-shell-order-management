package v4n.mtirestaurantreporter.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Restaurant {
    private ArrayList<Order> orders;
    private Set<String> menuItems;

    public Restaurant() {
        orders = new ArrayList<>();
        menuItems = MenuItems.get().keySet();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
