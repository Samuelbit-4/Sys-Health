module com.example.syshealthfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires pdfview;
    requires pdfa;
    requires pdf.renderer;

    requires layout;
    requires kernel;


    opens com.example.syshealthfx to javafx.fxml;
    exports com.example.syshealthfx;
}