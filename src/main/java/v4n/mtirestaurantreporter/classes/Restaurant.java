package v4n.mtirestaurantreporter.classes;

import v4n.mtirestaurantreporter.exceptions.InvalidFileFormat;
import v4n.mtirestaurantreporter.exceptions.MenuItemNotFound;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private static String MENU_ITEMS_FILE_NAME;
    private static String ORDERS_FILE_NAME;
    private final String[] menuItems;
    private final List<Order> orders;

    /**
     * Creates a new restaurant.
     * The menu items and orders are loaded using {@link OrderLoader#getOrders(String)} and {@link MenuItems#get()}.
     *
     * @param menuItemsFileName the file name of the menu items
     * @param ordersFileName the file name of the orders
     */
    public Restaurant(String menuItemsFileName, String ordersFileName) throws InvalidFileFormat {
        Restaurant.MENU_ITEMS_FILE_NAME = menuItemsFileName;
        Restaurant.ORDERS_FILE_NAME = ordersFileName;

        orders = OrderLoader.getOrders(ordersFileName);
        menuItems = MenuItems.get().keySet().toArray(new String[0]);
    }

    /**
     * Adds an order to the restaurant.
     *
     * @param order the order to add
     */
    public void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * Removes an order from the restaurant.
     * The order to remove is identified by the {@link Order#equals(Object)} method.
     *
     * @param order the order to remove
     */
    public void removeOrder(Order order) {
        orders.remove(order);
    }

    /**
     * Gets the orders in the restaurant.
     *
     * @return the orders in the restaurant
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Edits an order in the restaurant.
     * Editing an order means changing the status, discount, and items.
     * The order to edit is identified by the order date.
     *
     * @param order the order to edit
     */
    public void editOrder(Order order) throws MenuItemNotFound, InvalidFileFormat {
        for (Order or : orders) {
            if (or.compareTo(order) == 0) {
                or.setStatus(order.getStatus());
                or.setDiscount(order.getDiscount());
                or.setItems(order.getItems());
            }
        }
    }

    /**
     * Saves the orders in the restaurant using `MENU_ITEMS_FILE_NAME`.
     */
    public void saveOrders() {
        OrderLoader.saveOrders(orders, ORDERS_FILE_NAME);
    }

    /**
     * Gets the menu items in the restaurant.
     *
     * @return the menu items in the restaurant
     */
    public String[] getMenuItems() {
        return menuItems;
    }

    public static String getMenuItemsFileName() {
        return MENU_ITEMS_FILE_NAME;
    }

    public static String getOrdersFileName() {
        return ORDERS_FILE_NAME;
    }
}
