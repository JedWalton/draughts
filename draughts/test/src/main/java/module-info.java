module assignment1.test {
    requires javafx.controls;
    requires javafx.fxml;

    opens assignment1.test to javafx.fxml;
    exports assignment1;
}
