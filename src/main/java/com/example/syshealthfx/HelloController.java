package com.example.syshealthfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

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
    }
}