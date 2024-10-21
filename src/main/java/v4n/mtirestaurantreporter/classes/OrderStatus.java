package v4n.mtirestaurantreporter.classes;

public enum OrderStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    CANCELED("Canceled"),
    PREPARING("Preparing");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

