package v4n.mtirestaurantreporter.classes;

import v4n.mtirestaurantreporter.exceptions.InvalidFileFormat;
import v4n.mtirestaurantreporter.exceptions.MenuItemNotFound;

import java.io.*;
import java.util.*;

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
     * Sorts the orders in the restaurant using the given comparator.
     * The recommended comparators are {@link Order#compareTo(Order)} for doing the default sort (by date)
     *                                 {@link Order#STATUS_COMPARATOR} for sorting by status
     *                                 {@link Order#TOTAL_COMPARATOR} for sorting by total price
     *                                 or any other custom comparator.
     * Apply reverse order by using {@link Comparator#reverseOrder()} on any comparator.
     *
     * @param comparator the comparator to use for sorting
     */
    public void sortOrders(Comparator<Order> comparator) {
        orders.sort(comparator);
    }

    /**
     * Generates a report of the earnings in the restaurant.
     * The report includes the total earnings for the past day, week, and month.
     * Orders that don't have {@link OrderStatus#COMPLETED} will not be included in the report.
     *
     * @param fileName the file name where the report should be saved
     */
    public void generateReportEarnings(String fileName) {
        float dailyEarnings = 0.0f;
        float weeklyEarnings = 0.0f;
        float monthlyEarnings = 0.0f;

        for(Order order : orders) {
            if(order.getStatus() == OrderStatus.COMPLETED) {
                if (order.getDateOrdered().getTime() >= System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
                    dailyEarnings += order.getTotal();
                } else if (order.getDateOrdered().getTime() >= System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000) {
                    weeklyEarnings += order.getTotal();
                } else if (order.getDateOrdered().getTime() >= System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000) {
                    monthlyEarnings += order.getTotal();
                }
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))) {
            bw.write("Total earnings for the past day: " + String.format("%.2f", dailyEarnings));
            bw.newLine();

            bw.write("Total earnings for the past 7 days: " + String.format("%.2f", weeklyEarnings));
            bw.newLine();

            bw.write("Total earnings for the past 30 days: " + String.format("%.2f", monthlyEarnings));
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a report of the top foods in the restaurant.
     * The report includes the food items and the number of times they were ordered in {@link Comparator#reverseOrder()}.
     *
     * @param fileName the file name where the report should be saved
     */
    public void generateReportTopFoods(String fileName) {
        Map<String, Integer> foodCount = new HashMap<>();

        for (Order order : orders) {
            for (String item : order.getItems()) {
                foodCount.put(item, foodCount.getOrDefault(item, 0) + 1);
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))) {
            foodCount.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> {
                        try {
                            bw.write(entry.getKey() + ": " + entry.getValue());
                            bw.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
