package com.example.syshealthfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class HelloController {
    @FXML
    private Button ingresar;

    @FXML
    protected void onHelloButtonClick() throws IOException {

        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("index-admin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("ADMINISTRADOR");
        stage.show();

        Stage old = (Stage) ingresar.getScene().getWindow();

        old.close();
        SQLClass bd = new SQLClass("root", "", "sys_health_prueba");
        bd.connect();
        try {
           ResultSet rs = bd.executeQuery("SELECT * FROM paciente");
           ResultSetMetaData rsmt = rs.getMetaData();
           int columns = rsmt.getColumnCount();
            System.out.println("COLUMNAS" + columns);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        bd.disconnect();
    }
}