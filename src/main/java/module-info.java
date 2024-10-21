module v4n.mtirestaurantreporter {
    requires javafx.controls;
    requires javafx.fxml;


    opens v4n.mtirestaurantreporter to javafx.fxml;
    exports v4n.mtirestaurantreporter;
}