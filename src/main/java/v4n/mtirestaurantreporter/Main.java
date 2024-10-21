package v4n.mtirestaurantreporter;

import v4n.mtirestaurantreporter.classes.Restaurant;
import v4n.mtirestaurantreporter.exceptions.InvalidFileFormat;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            Restaurant restaurant = new Restaurant("menuItems.txt", "orders.txt");
            System.out.println(Arrays.toString(restaurant.getMenuItems()));
        } catch (InvalidFileFormat e) {
            throw new RuntimeException(e);
        }
    }
}
