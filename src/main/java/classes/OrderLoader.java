package classes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderLoader {
    /**
     * Saves the orders to a file.
     *
     * @param orders the orders to save
     * @param ordersFileName the file to save the orders to
     */
    public static void saveOrders(List<Order> orders, String ordersFileName) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ordersFileName))) {
            for(Order order : orders) {
                oos.writeObject(order);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the orders from a file.
     *
     * @param ordersFileName the file to get the orders from
     * @return the orders
     */
    public static List<Order> getOrders(String ordersFileName) {
        List<Order> orders = new ArrayList<>();

        File file = new File(ordersFileName);
        if (file.exists()) {
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                while(true) {
                    try {
                        Order order = (Order) ois.readObject();
                        orders.add(order);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return orders;
    }
}
