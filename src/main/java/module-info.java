module com.example.syshealthfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires pdfview;
    requires pdfa;
    requires pdf.renderer;


    requires layout;
    requires kernel;
    requires html2pdf;


    requires com.google.protobuf;
    requires org.joda.time;
    requires com.calendarfx.view;
    requires com.calendarfx.recurrence;

    requires jfxtras.controls;
    requires javafx.web;
    requires svg;

    opens com.example.syshealthfx to javafx.fxml;
    exports com.example.syshealthfx;
    exports com.example.syshealthfx.admincontrollers;

}
