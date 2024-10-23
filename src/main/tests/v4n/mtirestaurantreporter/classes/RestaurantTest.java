package v4n.mtirestaurantreporter.classes;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import v4n.mtirestaurantreporter.exceptions.InvalidFileFormat;
import v4n.mtirestaurantreporter.exceptions.MenuItemNotFound;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    private static final String menuItemsFileName = "menu.txt";
    private static final String ordersFileName = "orders.txt";
    private static final String weeklyEarningsFileName = "weekly-earnings.txt";
    private static final String topFoodsFileName = "top-foods.txt";
    private Restaurant restaurant;
    private Order order1;
    private Order order2;
    private Order order3;

    public static void modifyPrivateDateField(Object obj, String fieldName, Date newDate) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, newDate);
    }

    @BeforeEach
    void setUp() throws InvalidFileFormat, MenuItemNotFound {
        new File(menuItemsFileName).delete();
        new File(ordersFileName).delete();

        restaurant = new Restaurant(menuItemsFileName, ordersFileName);
        order1 = new Order();
        order1.setItems(new String[]{"Pizza", "Burger"});
        order1.setStatus(OrderStatus.COMPLETED);
        order1.setDiscount(10.0f);

        order2 = new Order();
        order2.setItems(new String[]{"Pasta", "Pizza"});
        order2.setStatus(OrderStatus.PREPARING);
        order2.setDiscount(5.0f);

        order3 = new Order();
        order3.setItems(new String[]{"Burger", "Pizza"});
        order3.setStatus(OrderStatus.COMPLETED);
        order3.setDiscount(5.0f);
    }

    @AfterAll
    static void tearDown() {
        new File(menuItemsFileName).delete();
        new File(ordersFileName).delete();
        new File(weeklyEarningsFileName).delete();
        new File(topFoodsFileName).delete();
    }

    @Test
    void testGenerateReportEarnings() throws MenuItemNotFound, InvalidFileFormat {
        Order order4 = new Order();
        order4.setItems(new String[]{"Burger", "Pizza"});
        order4.setStatus(OrderStatus.COMPLETED);
        order4.setDiscount(5.0f);

        try {
            modifyPrivateDateField(order1, "dateOrdered", new Date(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000));
            modifyPrivateDateField(order2, "dateOrdered", new Date(System.currentTimeMillis() - 1 * 12 * 60 * 60 * 1000));
            modifyPrivateDateField(order3, "dateOrdered", new Date(System.currentTimeMillis() - 17 * 24 * 60 * 60 * 1000));
            modifyPrivateDateField(order4, "dateOrdered", new Date(System.currentTimeMillis() - 19 * 24 * 60 * 60 * 1000));
        } catch (Exception e) {
            fail(e);
        }

        restaurant.addOrder(order1);
        restaurant.addOrder(order2);
        restaurant.addOrder(order3);
        restaurant.addOrder(order4);
        restaurant.generateReportEarnings(weeklyEarningsFileName);

        File file = new File(weeklyEarningsFileName);
        assertTrue(file.exists());

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line = br.readLine();
            float totalDaily = 0.0F;
            assertEquals("Total earnings for the past day: " + String.format("%.2f", totalDaily), line);

            line = br.readLine();
            float totalWeek = order1.getTotal();
            assertEquals("Total earnings for the past 7 days: " + String.format("%.2f", totalWeek), line);

            line = br.readLine();
            float totalMonth = order3.getTotal() + order4.getTotal();
            assertEquals("Total earnings for the past 30 days: " + String.format("%.2f", totalMonth), line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGenerateReportTopFoods() {
        restaurant.addOrder(order1);
        restaurant.addOrder(order2);
        restaurant.addOrder(order3);
        restaurant.generateReportTopFoods(topFoodsFileName);

        File file = new File(topFoodsFileName);
        assertTrue(file.exists());

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line = br.readLine();
            assertEquals("Pizza: 3", line);

            line = br.readLine();
            assertEquals("Burger: 2", line);

            line = br.readLine();
            assertEquals("Pasta: 1", line);
        } catch (Exception e) {
            fail(e);
        }
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