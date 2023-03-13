module com.example.syshealthfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.syshealthfx to javafx.fxml;
    exports com.example.syshealthfx;
}