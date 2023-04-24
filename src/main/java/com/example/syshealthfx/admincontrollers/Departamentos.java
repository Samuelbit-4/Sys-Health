package com.example.syshealthfx.admincontrollers;

import com.example.syshealthfx.RegistroController;

import javafx.fxml.FXMLLoader;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;


public class Departamentos {
    private VBox vn;
    private AnchorPane frameAdmin;
    private HBox contenidoHBox;
    public Departamentos(VBox vn, AnchorPane frameAdmin, HBox contenidoHBox){
        this.vn = vn;
        this.frameAdmin = frameAdmin;
        this.contenidoHBox = contenidoHBox;
    }
    public void mostrarContenido(){
        System.out.println(vn.getChildren());
        System.out.println(frameAdmin.getLayoutX());
        System.out.println(contenidoHBox.getLayoutX());
    }
    public void eventosBotones(){
        TablaUsuarios tablaUsuarios = new TablaUsuarios();
        HBox hb = (HBox) this.vn.lookup("#btnListaEmpleados");
        HBox btnRegistro = (HBox) vn.lookup("#btnRegistrar");


    }
    public void removerElementos(){
        HBox hbox = (HBox) frameAdmin.lookup("#contenidoHBox");
        VBox vbox = (VBox) contenidoHBox.lookup("#contenidoInicio");
        hbox.getChildren().remove(vbox);
    }
    public boolean btnRegistro(){
        Button cancelar = (Button) vn.lookup("#cancelar");

        cancelar.setOnAction((e) ->{
            btnCancelar();
            removerElementos();
        });
        return true;

    }
    public boolean botonAccionado(){
        return true;
    }
    public void btnCancelar(){
        Alert cerrar = new Alert(Alert.AlertType.CONFIRMATION);
        cerrar.setTitle("CONFIRMACIÓN");
        cerrar.setHeaderText("¿DESEA CANCELAR LA CAPTURA?");
        cerrar.setContentText("Se perderán todos los datos...");
        cerrar.showAndWait();
        ButtonType result = cerrar.getResult();
        if(result == ButtonType.OK){
            contenidoHBox.getChildren().remove(vn);
            removerElementos();

        }
    }
    public void mostrarVentana(){
        FXMLLoader departamentos = new FXMLLoader(getClass().getResource("admin-views/departamentos/departamentos.fxml"));
        try {
            vn = (VBox) departamentos.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        vn.setId("contenidoInicio");
        contenidoHBox.getChildren().add(vn);

    }
}


