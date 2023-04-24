package com.example.syshealthfx;


import com.example.syshealthfx.admincontrollers.Departamentos;
import com.example.syshealthfx.admincontrollers.TablaUsuarios;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.input.*;


import java.io.IOException;
import java.net.URL;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;


public class UsersController implements Initializable {

    @FXML
    private AnchorPane frameAdmin;
    @FXML
    private Button usuario;
    @FXML
    private Button btnInicio;
    @FXML
    private Button btnPacientes;
    @FXML
    private Button btnLaboratorio;
    @FXML
    private Button btnCitas;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private HBox contenidoHBox;
    @FXML
    private VBox contenidoUsuarios;
    @FXML
    private VBox botonesAside;
    @FXML
    private Label nombreInicio;

    private SQLClass conexion;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VBox contenidoInicio = null;
        System.out.println(SesionUsuario.getUsuario());
        botonesAside.getChildren().forEach((boton) ->{
            boton.setOnMouseClicked((actionEvent) ->{
                switch (boton.getId()){
                    case "btnInicio" ->{
                        Stage stage = new Stage();
                        try{
                            Parent root = FXMLLoader.load(getClass().getResource("index-admin.fxml"));
                            Scene scene = new Scene(root);
                            Image imagenIcono = new Image(getClass().getResourceAsStream("assets/hospital-logo.png"));
                            stage.getIcons().add(imagenIcono);
                            stage.setScene(scene);
                            stage.setMaximized(true);
                            stage.show();
                            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

                        }catch (IOException e){
                            System.out.println("no");
                        }
                    }
                    case "btnDepartamentos" ->{
                        removerElementos();
                        mostrarVentana("departamentos");
                        Button botona = (Button) botonesAside.lookup("#"+boton.getId());
                        TablaUsuarios tabla = new TablaUsuarios();
                        VBox vn = (VBox) contenidoHBox.lookup("#contenidoInicio");
                        HBox hb = (HBox) vn.lookup("#btnListaEmpleados");
                        HBox btnRegistro = (HBox) vn.lookup("#btnRegistrar");
                        HBox btnRegistrarDepartamento = (HBox) vn.lookup("#btnRegistroDepartamento");
                        btnRegistro.setOnMouseClicked((actionEvents) ->{
                            removerElementos();
                            Button botonActual = (Button) actionEvent.getSource();
                            botonActual.setDisable(true);
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("registro-usuario.fxml"));
                            VBox vn1 = null;
                            try {
                                vn1 = (VBox) loader.load();
                                RegistroController registro = new RegistroController(vn1);
                                VBox vn2 = registro.getV();
                                contenidoHBox.getChildren().add(vn2);
                                Button cancelar = (Button) vn2.lookup("#cancelar");
                                cancelar.setOnAction((e) ->{
                                    Alert cerrar = new Alert(Alert.AlertType.CONFIRMATION);
                                    cerrar.setTitle("CONFIRMACIÓN");
                                    cerrar.setHeaderText("¿DESEA CANCELAR LA CAPTURA?");
                                    cerrar.setContentText("Se perderan todos los datos...");
                                    cerrar.showAndWait();
                                    ButtonType result = cerrar.getResult();
                                    if(result == ButtonType.OK){
                                        contenidoHBox.getChildren().remove(vn2);
                                        removerElementos();
                                        botonActual.setDisable(false);
                                        mostrarVentana("departamentos");
                                    }
                                });
                            } catch (IOException e){
                                e.printStackTrace();
                            }

                        });
                        hb.setOnMouseClicked((actionEvents) ->{
                            removerElementos();
                            FXMLLoader loader = new FXMLLoader (getClass().getResource("admin-views/departamentos/lista-empleados.fxml"));
                            VBox vn1 = null;
                            try {
                                vn1 = (VBox) loader.load();
                                vn1.getChildren().add(tabla.mostrarTabla());
                                Button btnVolver = (Button) vn1.lookup("#btnBack");
                                btnVolver.setOnAction((e) ->{
                                    removerElementos();
                                    mostrarVentana("departamentos");
                                });


                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            vn1.setId("contenidoInicio");
                            contenidoHBox.getChildren().add(vn1);

                        });
                        btnRegistrarDepartamento.setOnMouseClicked((registrar) ->{
                            removerElementos();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-views/departamentos/registrar-departamento.fxml"));
                            VBox vn1 = null;
                            try{
                                vn1 = (VBox) loader.load();
                                contenidoHBox.getChildren().add(vn1);
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        });

                    }
                    case "btnPacientes" ->{
                        System.out.println("PACIENTES");
                        removerElementos();
                        mostrarVentana("pacientes");
                        VBox vn = (VBox) contenidoHBox.lookup("#contenidoInicio");
                        HBox btnListaPacientes = (HBox) vn.lookup("#btnListaPacientes");
                        HBox btnRegistrarPacientes = (HBox) vn.lookup("#btnRegistrarPaciente");
                        btnListaPacientes.setOnMouseClicked((pacientes) ->{
                            removerElementos();
                            mostrarVentana("pacientes", "lista-pacientes");
                        });
                        btnRegistrarPacientes.setOnMouseClicked((registrarEvent) ->{
                            removerElementos();
                            mostrarVentana("pacientes", "registrar-paciente");
                        });



                    }
                    case "btnLaboratorio" ->{
                        System.out.println("LABORATORIO");
                        removerElementos();
                        mostrarVentana("laboratorio");
                        VBox vn = (VBox) contenidoHBox.lookup("#contenidoInicio");
                        HBox btnListaPruebas = (HBox) vn.lookup("#btnListaPruebas");
                        HBox btnConsultarOrden = (HBox) vn.lookup("#btnConsultarOrden");
                        HBox btnAgregarPrueba = (HBox) vn.lookup("#btnAgregarPrueba");

                        btnListaPruebas.setOnMouseClicked((listaEvent) ->{
                            removerElementos();
                            mostrarVentana("laboratorio", "lista-pruebas");

                        });
                        btnConsultarOrden.setOnMouseClicked((pruebaEvent) ->{
                            removerElementos();
                            mostrarVentana("laboratorio", "consultar-orden");
                        });
                        btnAgregarPrueba.setOnMouseClicked((agregarPrueba) ->{
                            removerElementos();
                            mostrarVentana("laboratorio", "agregar-prueba");
                        });

                    }
                    case "btnCitas" ->{
                        System.out.println("CITAS");
                        removerElementos();
                        mostrarVentana("citas");
                        VBox vn = (VBox) contenidoHBox.lookup("#contenidoInicio");
                        HBox btnListaPruebas = (HBox) vn.lookup("#btnListaPruebas");
                        HBox btnConsultarOrden = (HBox) vn.lookup("#btnConsultarOrden");
                        HBox btnAgregarPrueba = (HBox) vn.lookup("#btnAgregarPrueba");

                    }
                    case "btnCerrarSesion" ->{
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("CONFIRMAR");
                        alert.setHeaderText("CERRAR SESIÓN");
                        alert.setContentText("¿Está seguro de cerrar sesión?\n Se perderan todos los datos sin guardar...");

                        Optional<ButtonType> result =  alert.showAndWait();
                        if(result.get() == ButtonType.OK){
                            Stage stage = new Stage();
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("login.fxml"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Scene scene = new Scene(root);
                            Image imagenIcono = new Image(getClass().getResourceAsStream("assets/hospital-logo.png"));
                            stage.getIcons().add(imagenIcono);
                            stage.setScene(scene);
                            stage.setMaximized(true);
                            stage.show();
                            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                        }
                    }
                }
            });
        });



    }
    public VBox mostrarVentana(String nombre){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-views/"+nombre+"/"+nombre+".fxml"));
            VBox vn = (VBox) loader.load();
            vn.setId("contenidoInicio");

            contenidoHBox.getChildren().add(vn);
            return vn;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public VBox mostrarVentana(String nombre, String nombreArchivo){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-views/"+nombre+"/"+nombreArchivo+".fxml"));
            VBox vn = (VBox) loader.load();
            vn.setId("contenidoInicio");

            contenidoHBox.getChildren().add(vn);
            return vn;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void removerElementos(){
        HBox hbox = (HBox) frameAdmin.lookup("#contenidoHBox");
        VBox vbox = (VBox) contenidoHBox.lookup("#contenidoInicio");
        hbox.getChildren().remove(vbox);
    }
    public void usuariosFrame(Button boton){

        boton.fire();


    }
}






