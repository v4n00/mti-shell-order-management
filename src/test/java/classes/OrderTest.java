package classes;

import exceptions.InvalidFileFormat;
import exceptions.MenuItemNotFound;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private static Order[] orders;
    private Order order;
    private String[] items;
    private static final String menuItemsFileName = "menu.txt";
    private static final String ordersFileName = "orders.txt";

    public static void modifyPrivateDateField(Object obj, String fieldName, Date newDate) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, newDate);
    }

    @BeforeAll
    static void setUpAll() throws Exception {
        Restaurant Restaurant = new Restaurant(menuItemsFileName, ordersFileName);

        Order order1 = new Order(new String[]{"Pizza"}, "Alice", 10.0f);
        order1.setStatus(OrderStatus.COMPLETED);
        modifyPrivateDateField(order1, "dateOrdered", new Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000));

        Order order2 = new Order(new String[]{"Burger"}, "Bob", 5.0f);
        order2.setStatus(OrderStatus.PENDING);
        modifyPrivateDateField(order2, "dateOrdered", new Date(System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000));

        Order order3 = new Order(new String[]{"Salad"}, "Charlie", 0.0f);
        order3.setStatus(OrderStatus.PREPARING);
        modifyPrivateDateField(order3, "dateOrdered", new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000));

        orders = new Order[]{order1, order2, order3};
    }

    @BeforeEach
    void setUp() throws InvalidFileFormat {
        items = new String[]{"Pizza", "Burger"};
        order = new Order();
        Restaurant _ = new Restaurant("menu.txt", "orders.txt");
    }

    @AfterAll
    static void tearDownAll() {
        new File(menuItemsFileName).delete();
        new File(ordersFileName).delete();
    }

    @Test
    void testDefaultComparator() {
        Order[] sortedArray = orders.clone();
        Arrays.sort(sortedArray, new Comparator<Order>() {
            @Override
            public int compare(Order order, Order t1) {
                return order.getDateOrdered().compareTo(t1.getDateOrdered());
            }
        });
        Arrays.sort(orders);
        assertArrayEquals(sortedArray, orders);
    }

    @Test
    void testStatusComparator() {
        Order[] sortedArray = orders.clone();
        Arrays.sort(sortedArray, new Comparator<Order>() {
            @Override
            public int compare(Order order, Order t1) {
                return order.getStatus().compareTo(t1.getStatus());
            }
        });
        Arrays.sort(orders, Order.STATUS_COMPARATOR);
        assertArrayEquals(sortedArray, orders);
    }

    @Test
    void testTotalComparator() {
        Order[] sortedArray = orders.clone();
        Arrays.sort(sortedArray, new Comparator<Order>() {
            @Override
            public int compare(Order order, Order t1) {
                return Float.compare(t1.getTotal(), order.getTotal());
            }
        });
        Arrays.sort(orders, Order.TOTAL_COMPARATOR.reversed());
        assertArrayEquals(sortedArray, orders);
    }

    @Test
    void testSetItems() throws MenuItemNotFound, InvalidFileFormat {
        order.setItems(items);
        assertArrayEquals(items, order.getItems());
    }

    @Test
    void testSetStatus() {
        order.setStatus(OrderStatus.PENDING);
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void testSetDiscount() throws MenuItemNotFound, InvalidFileFormat {
        order.setItems(items);
        order.setDiscount(10.0f);
        assertEquals(10.0f, order.getDiscount());
    }

    @Test
    void testCalculateTotal() throws MenuItemNotFound, InvalidFileFormat {
        HashMap<String, Float> menuItems = MenuItems.get();
        float total = menuItems.get(items[0]) + menuItems.get(items[1]);
        total = total * (1 - 10.0f / 100);

        order.setItems(items);
        order.setDiscount(10.0f);
        assertEquals(total, order.getTotal(), 0.01f);
    }
}