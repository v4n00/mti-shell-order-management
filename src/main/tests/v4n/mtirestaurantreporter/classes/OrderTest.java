package v4n.mtirestaurantreporter.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import v4n.mtirestaurantreporter.exceptions.InvalidFileFormat;
import v4n.mtirestaurantreporter.exceptions.MenuItemNotFound;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;
    private String[] items;

    @BeforeEach
    void setUp() throws InvalidFileFormat {
        items = new String[]{"Pizza", "Burger"};
        order = new Order();
        Restaurant _ = new Restaurant("menu.txt", "orders.txt");
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