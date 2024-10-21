package v4n.mtirestaurantreporter.classes;

import v4n.mtirestaurantreporter.exceptions.MenuItemNotFound;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Order implements Cloneable, Comparable<Order>, Serializable {
    private String[] items;
    private OrderStatus status;
    private Date dateOrdered;
    private String customerName;
    private float discount;
    private float total;

    /**
     * Compares this Order object with another Order object based on the order date.
     * @param order the Order object to compare with
     * @return a negative integer, zero, or a positive integer as this Order object is less than, equal to, or greater than the specified Order object.
     */
    @Override
    public int compareTo(Order order) {
        return dateOrdered.compareTo(order.dateOrdered);
    }

    /*
     * Calculates the total price of the order.
     * @throws MenuItemNotFound if an item in the order is not found in the menu
     */
    private void calculateTotal() throws MenuItemNotFound {
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
        this.total = total;
    }

    private Order() {}

    public Order(String[] items, String customerName) {
        this.setItems(items);
        this.setCustomerName(customerName);
        this.setDateOrdered(new Date());
    }

    public Order(String[] items, OrderStatus status, String customerName, float discount, Date dateOrdered, float total) {
        this.setItems(items);
        this.setStatus(status);
        this.setCustomerName(customerName);
        this.setDiscount(discount);
        this.setDateOrdered(dateOrdered);
        this.setTotal(total);
    }

    @Override
    public Order clone() {
        Order order = new Order();
        order.setItems(this.items);
        order.setStatus(this.status);
        order.setCustomerName(this.customerName);
        order.setDiscount(this.discount);
        order.setDateOrdered(this.dateOrdered);
        order.setTotal(this.total);
        return order;
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

    public void setItems(String[] items) {
        if(items == null) {
            throw new IllegalArgumentException("Items cannot be null.");
        }
        this.items = items.clone();
    }

    public void setStatus(OrderStatus status) {
        if(status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    public void setCustomerName(String customerName) {
        if(customerName == null || customerName.isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty.");
        }
        this.customerName = customerName;
    }

    public void setDiscount(float discount) {
        if(discount < 0 || discount > 1) {
            throw new IllegalArgumentException("Discount cannot be negative or above 1.");
        }
        this.discount = discount;
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

    public float getTotal() throws MenuItemNotFound {
        calculateTotal();
        return total * (1 - discount / 100);
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
