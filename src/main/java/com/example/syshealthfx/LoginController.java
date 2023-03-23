package com.example.syshealthfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
*   CONTROLADOR DEL LOGIN, EN ESTA CLASE SE VERIFICARÁ QUE EL USUARIO
*   Y LA CONTRASEÑA PROPORCIONADA SEAN VALIDAS A TRAVES DE CONSULTAS DE SQL
*   EN CUANTO A FUNCIONAMIENTO ESTA LISTO, SOLO FALTA AGREGAR PAQUETES DE SEGURIDAD
*   Y MÁS VALIDACIONES
*   ATTE. SAMUEL AMAYA
*
*
* */
public class LoginController {
    private String usuario;
    private String password;

    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button acceder;
    private ButtonType result;

    public void initialize(){
        acceder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                usuario = txtUsuario.getText();
                password = txtPassword.getText();
                SQLClass conexion = new SQLClass("root", "", "sys_health");
                conexion.connect();
                try {
                    ResultSet rs = conexion.executeQuery("SELECT usuario, pass FROM usuarios WHERE usuario='" + usuario + "' AND pass=PASSWORD('" + password + "');");
                    if (rs.next()) {
                        // El usuario y la contraseña son válidos
                        System.out.println("SI!");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("!!");
                        alert.setHeaderText(null);
                        alert.setContentText("ACCESO CONCEDIDO");
                        alert.showAndWait();
                        result = alert.getResult();
                        if(result == ButtonType.OK){
                            Stage stage = new Stage();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("index-admin.fxml"));
                                Scene scene = new Scene(root);
                                stage.setMaximized(true);
                                stage.setScene(scene);
                                stage.show();
                                // Ocultar la ventana actual
                                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                                conexion.disconnect();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else{
                            Stage stage = new Stage();
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("index-admin.fxml"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Scene scene = new Scene(root);
                            stage.setMaximized(true);
                            stage.setScene(scene);
                            stage.show();
                            // Ocultar la ventana actual
                            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                            conexion.disconnect();
                        }

                    } else {
                        // El usuario y/o la contraseña son incorrectos
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("!!");
                        alert.setHeaderText(null);
                        alert.setContentText("USUARIO NO ENCONTRADO \nVERIFICA TU USUARIO O CONTRASEÑA");
                        alert.showAndWait();
                        result = alert.getResult();
                        if(result == ButtonType.OK){
                            txtUsuario.setText(null);
                            txtPassword.setText(null);
                        } else{
                            txtUsuario.setText(null);
                            txtPassword.setText(null);
                        }
                    }
                } catch (SQLException e) {
                    // Error al ejecutar la consulta SQL
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("!!");
                    alert.setHeaderText(null);
                    alert.setContentText("ERROR AL EJECUTAR LA CONSULTA SQL");
                    throw new RuntimeException(e);
                }

            }
        });
    }


}