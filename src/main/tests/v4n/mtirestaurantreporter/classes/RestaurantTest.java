package v4n.mtirestaurantreporter.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import v4n.mtirestaurantreporter.exceptions.InvalidFileFormat;
import v4n.mtirestaurantreporter.exceptions.MenuItemNotFound;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    private Restaurant restaurant;
    private Order order1;
    private Order order2;
    private String menuItemsFileName = "menu.txt";
    private String ordersFileName = "orders.txt";

    @BeforeEach
    void setUp() throws InvalidFileFormat, MenuItemNotFound {
        new File(menuItemsFileName).delete();
        new File(ordersFileName).delete();

        restaurant = new Restaurant(menuItemsFileName, ordersFileName);
        order1 = new Order();
        order1.setItems(new String[]{"Pizza", "Burger"});
        order1.setStatus(OrderStatus.PENDING);
        order1.setDiscount(10.0f);

        order2 = new Order();
        order2.setItems(new String[]{"Pasta", "Salad"});
        order2.setStatus(OrderStatus.COMPLETED);
        order2.setDiscount(5.0f);
    }

    @Test
    void testAddOrder() {
        restaurant.addOrder(order1);
        assertTrue(restaurant.getOrders().contains(order1));
    }

    @Test
    void testRemoveOrder() {
        restaurant.addOrder(order1);
        restaurant.removeOrder(order1);
        assertFalse(restaurant.getOrders().contains(order1));
    }

    @Test
    void testEditOrder() throws MenuItemNotFound, InvalidFileFormat {
        restaurant.addOrder(order1);
        restaurant.addOrder(order2);
        Order editedOrder = restaurant.getOrders().stream().filter(order -> order.equals(order1)).findFirst().orElse(null);

        assertNotNull(editedOrder);
        editedOrder.setStatus(OrderStatus.COMPLETED);
        restaurant.editOrder(editedOrder);

        Order updatedOrder = restaurant.getOrders().stream().filter(order -> order.equals(order1)).findFirst().orElse(null);
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus());
        assertEquals(10.0f, updatedOrder.getDiscount());
        assertArrayEquals(new String[]{"Pizza", "Burger"}, updatedOrder.getItems());
    }

    @Test
    void testSaveAndLoadOrders() throws InvalidFileFormat {
        restaurant.addOrder(order1);
        restaurant.saveOrders();

        Restaurant restaurant2 = new Restaurant(menuItemsFileName, ordersFileName);
        Order order = restaurant2.getOrders().getFirst();
        assertEquals(order1, order);
    }

    @Test
    void testGetOrders() {
        restaurant.addOrder(order1);
        restaurant.addOrder(order2);

        List<Order> orders = restaurant.getOrders();
        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    @Test
    void testGetMenuItems() {
        String[] menuItems = restaurant.getMenuItems();
        assertNotNull(menuItems);
        assertTrue(menuItems.length > 0);
    }
}