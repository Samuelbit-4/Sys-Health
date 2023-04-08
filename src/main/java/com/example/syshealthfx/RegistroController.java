package com.example.syshealthfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {
    @FXML
    private ComboBox cmbDepartamentos;
    @FXML
    private Button generarUsuario;
    @FXML
    private Button generarPassword;
    @FXML
    private Button vaciar;
    @FXML
    private AnchorPane frame;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidoA;
    @FXML
    private TextField txtApellidoP;

    @FXML
    private TextField txtPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        ToggleGroup genero = new ToggleGroup();
        RadioButton rb1 = new RadioButton("MASCULINO");
        rb1.setSelected(true);
        RadioButton rb2 = new RadioButton("FEMENINO");
        rb1.setToggleGroup(genero);
        rb2.setToggleGroup(genero);
        HBox rbtnGenero = (HBox) frame.lookup("#rbtnGenero");
        rbtnGenero.getChildren().addAll(rb1, rb2);

        ToggleGroup rol = new ToggleGroup();
        RadioButton rol1 = new RadioButton("MÉDICO");
        RadioButton rol2 = new RadioButton("RECEPCION");
        RadioButton rol3 = new RadioButton("ADMINISTRACIÓN");

        rol1.setToggleGroup(rol);
        rol2.setToggleGroup(rol);
        rol3.setToggleGroup(rol);
        rol1.setSelected(true);

        HBox rbtnRol = (HBox) frame.lookup("#rbtnRol");
        rbtnRol.setStyle("-fx-alignment: center");
        rbtnRol.setSpacing(5);
        rbtnRol.getChildren().addAll(rol1, rol2, rol3);

       txtUsuario.textProperty().addListener((observable, oldValue, newValue) ->{
           if(!newValue.trim().isEmpty()){
               generarUsuario.setDisable(true);
           } else{
               generarUsuario.setDisable(false);
           }
       });
       txtPassword.textProperty().addListener((observable, oldValue, newValue) ->{
            if(!newValue.trim().isEmpty()){
                generarPassword.setDisable(true);
            } else{
                generarPassword.setDisable(false);
            }
        });

    }
    public void vaciarFormulario(){
        frame.getChildren().forEach((item) ->{
            item.lookupAll("TextField").forEach((items) ->{
                TextField textField = (TextField) items;
                if (textField.getText() != "") {
                    textField.setText("");
                }
            });
        });
    }
    public void cerrarVentana(){
        Alert cerrar = new Alert(Alert.AlertType.CONFIRMATION);
        cerrar.setTitle("CONFIRMACIÓN");
        cerrar.setHeaderText("¿DESEA CANCELAR LA CAPTURA?");
        cerrar.setContentText("Se perderan todos los datos...");
        cerrar.showAndWait();
        ButtonType result = cerrar.getResult();
        if(result == ButtonType.OK){
            frame.getScene().getWindow().hide();
        }
    }
    public void generarUsuario(){
        if(txtNombre.getText() != "" && txtApellidoA.getText() != "" && txtApellidoP.getText() != ""){
           String val1 = txtNombre.getText().trim().substring(0,3);
           String val2 = txtApellidoA.getText().trim().substring(0,2).toUpperCase();
            char[] symbols = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'};
            String val3 = txtApellidoP.getText().trim().substring(0,1).toLowerCase();
           int val4 = (int) ((Math.random() / 1000) * 1000);
           Random rand = new Random();
           int numeroA = rand.nextInt(12);
            System.out.println(val1 + " " + val2 + " " + val3 + " " + val4 + " " + symbols[numeroA]);
            txtUsuario.setText(val1+val2+symbols[numeroA]+val3+val4);
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("UPS!");
            alert.setHeaderText("ERROR");
            alert.setContentText("Error: No es posible generar el usuario, revisa que " +
                    "\nlos datos estén completos");
            alert.showAndWait();
        }
    }
    public void generarPass(){
        char[] letras = {'a', 'b', 'c', 'd', 'e'};
        char[] symbols = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'};
        Random rand = new Random();
        String acumulador;
        String pass = "";
        int contador = 0;
        while(contador < 4){
            int numeroSimbolos = rand.nextInt(12);
            int numeroLetras = rand.nextInt(5);
            acumulador = String.valueOf(letras[numeroLetras])+ String.valueOf(symbols[numeroSimbolos]);
            pass += acumulador;
            contador++;
        }
        txtPassword.setText(pass);
    }
}
