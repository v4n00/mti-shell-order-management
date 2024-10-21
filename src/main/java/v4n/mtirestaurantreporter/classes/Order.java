package v4n.mtirestaurantreporter.classes;

import v4n.mtirestaurantreporter.exceptions.InvalidFileFormat;
import v4n.mtirestaurantreporter.exceptions.MenuItemNotFound;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Order implements Comparable<Order>, Serializable {
    private String[] items;
    private OrderStatus status;
    private Date dateOrdered;
    private String customerName;
    private float discount;
    private float total;

    /**
     * Compares this Order object with another Order object based on {@link Date#compareTo(Date)}.
     *
     * @param order the Order object to compare with
     * @return a negative integer, zero, or a positive integer as this Order object is less than, equal to, or greater than the specified Order object.
     */
    @Override
    public int compareTo(Order order) {
        return dateOrdered.compareTo(order.dateOrdered);
    }

    /**
     * Calculates the total price of the order.
     *
     * @throws MenuItemNotFound if an item in the order is not found in the menu
     * @throws InvalidFileFormat if the menu file is not in the correct format
     */
    private void calculateTotal() throws MenuItemNotFound, InvalidFileFormat {
        float total = 0;
        HashMap<String, Float> menuItems = MenuItems.get();
        for (String item : items) {
            if (menuItems.containsKey(item)) {
                total += menuItems.get(item);
            } else {
                this.total = 0;
                throw new MenuItemNotFound("Item not found in the menu: " + item);
            }
        }
        this.total = total * (1 - discount / 100);
    }

    Order() {
        this.setStatus(OrderStatus.PENDING);
        this.setDateOrdered(new Date());
        this.discount = 0.0f;
        this.total = 0;
    }

    /**
     * Creates an Order object with the specified items and customer name.
     * The order status is set to {@link OrderStatus#PENDING}.
     * The date ordered is set to the {@link Date#Date()}.
     *
     * @param items        the items in the order
     * @param customerName the name of the customer
     * @throws MenuItemNotFound if an item in the order is not found in the menu
     * @throws InvalidFileFormat if the menu file is not in the correct format
     */
    public Order(String[] items, String customerName, float discount) throws MenuItemNotFound, InvalidFileFormat {
        this();
        this.setItems(items);
        this.setDiscount(discount);
        this.setCustomerName(customerName);
    }

    private void setTotal(float total) {
        if(total < 0) {
            throw new IllegalArgumentException("Total cannot be negative.");
        }
        this.total = total;
    }

    private void setDateOrdered(Date dateOrdered) {
        if(dateOrdered == null) {
            throw new IllegalArgumentException("Date ordered cannot be null.");
        }
        this.dateOrdered = dateOrdered;
    }

    private void setCustomerName(String customerName) {
        if(customerName == null || customerName.isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty.");
        }
        this.customerName = customerName;
    }

    /**
     * Sets the items in the order and calculates the total price.
     *
     * @see #calculateTotal()
     * @param items the items in the order
     * @throws MenuItemNotFound if an item in the order is not found in the menu
     * @throws InvalidFileFormat if the menu file is not in the correct format
     */
    public void setItems(String[] items) throws MenuItemNotFound, InvalidFileFormat {
        if(items == null) {
            throw new IllegalArgumentException("Items cannot be null.");
        }
        this.items = items.clone();
        calculateTotal();
    }

    /**
     * Sets the status of the order.
     *
     * @param status the status of the order
     */
    public void setStatus(OrderStatus status) {
        if(status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    /**
     * Sets the discount of the order.
     *
     * @param discount the discount of the order
     * @throws MenuItemNotFound if an item in the order is not found in the menu
     * @throws InvalidFileFormat if the menu file is not in the correct format
     */
    public void setDiscount(float discount) throws MenuItemNotFound, InvalidFileFormat {
        if(discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount cannot be negative or above 100.");
        }
        this.discount = discount;
        calculateTotal();
    }

    @Override
    public String toString() {
        return "Order{" +
                "items=" + Arrays.toString(items) +
                ", status=" + status +
                ", dateOrdered=" + dateOrdered +
                ", customerName='" + customerName + '\'' +
                ", discount=" + discount +
                ", total=" + total +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Float.compare(discount, order.discount) == 0 && Float.compare(total, order.total) == 0 && Objects.deepEquals(items, order.items) && status == order.status && Objects.equals(dateOrdered, order.dateOrdered) && Objects.equals(customerName, order.customerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(items), status, customerName, discount, dateOrdered, total);
    }

    public float getTotal() {
        return total;
    }

    public float getDiscount() {
        return discount;
    }

    public String[] getItems() {
        return items;
    }

    public Date getDateOrdered() {
        return dateOrdered;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getCustomerName() {
        return customerName;
    }
}
