package com.example.syshealthfx;


import com.example.syshealthfx.admincontrollers.Departamentos;
import com.example.syshealthfx.admincontrollers.Pacientes;
import com.example.syshealthfx.admincontrollers.TablaPacientes;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.*;


import java.io.File;
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
                        TablaUsuarios tabla = new TablaUsuarios();
                        VBox vn = (VBox) contenidoHBox.lookup("#contenidoInicio");
                        HBox hb = (HBox) vn.lookup("#btnListaEmpleados");
                        HBox btnRegistro = (HBox) vn.lookup("#btnRegistrar");
                        HBox btnRegistrarDepartamento = (HBox) vn.lookup("#btnRegistroDepartamento");
                        HBox btnReporteDepartamento = (HBox) vn.lookup("#btnPdfDepartamento");
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
                        btnReporteDepartamento.setOnMouseClicked((depa)->{
                            System.out.println("Hola");
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Seleccionar ubicación");

                            File initialDirectory = new File(System.getProperty("user.home"));
                            fileChooser.setInitialDirectory(initialDirectory);

                            File selectedDirectory = fileChooser.showSaveDialog(frameAdmin.getScene().getWindow());


                            if (selectedDirectory != null) {
                                System.out.println("Ubicación seleccionada: " + selectedDirectory.getAbsolutePath());
                                RegistroController reporte = new RegistroController();
                                reporte.generarReporte(selectedDirectory.getAbsolutePath());
                                Alert aviso = new Alert(Alert.AlertType.INFORMATION);
                                aviso.setTitle("PDF CREADO CON EXITO");
                                aviso.setHeaderText("REPORTE GENERADO CON EXITO");
                                aviso.setContentText("El reporte a sido creado con éxito\nSe a guardado en: "+selectedDirectory.getAbsolutePath());
                                aviso.show();
                            }
                        });
                        btnRegistrarDepartamento.setOnMouseClicked((registrar) ->{
                            removerElementos();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-views/departamentos/registrar-departamento.fxml"));
                            VBox vn1 = null;
                            try{
                                vn1 = (VBox) loader.load();
                                contenidoHBox.getChildren().add(vn1);
                                RegistroController registrarDepartamento = new RegistroController();
                                registrarDepartamento.crearDepartamento("", "", vn1);
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
                            VBox actual = mostrarVentana("pacientes", "lista-pacientes");
                            //Pacientes listaPacientes = new Pacientes(actual);
                            TablaPacientes tablaPacientes = new TablaPacientes();
                            actual.getChildren().add(tablaPacientes.mostrarTabla());

                        });
                        btnRegistrarPacientes.setOnMouseClicked((registrarEvent) ->{
                            removerElementos();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-views/pacientes/registrar-paciente.fxml"));
                            VBox vn1 = null;
                            try{
                                vn1 = (VBox) loader.load();

                                // VBox vn2 = registroPacientes.getPacientes();
                                contenidoHBox.getChildren().add(vn1);
                                //Button btnRegistrar = (Button) vn1.lookup("#btnRegistrar");
                                TextField txtNombre = (TextField) vn1.lookup("#txtNombre");
                                TextField txtApellidoP = (TextField) vn1.lookup("#txtApellidoP");
                                TextField txtApellidoM = (TextField) vn1.lookup("#txtApellidoM");
                                TextField txtDireccion = (TextField) vn1.lookup("#txtDireccion");
                                TextField txtTelefono = (TextField) vn1.lookup("#txtTelefono");
                                TextField txtCorreo = (TextField) vn1.lookup("#txtCorreo");
                                //DatePicker txtFecha = (DatePicker) vn1.lookup("#txtFecha");

                                Button btnRegistrar = (Button) vn1.lookup("#btnRegistrar");
                                Button btnCancelar = (Button) vn1.lookup("#btnCancelar");

                                btnRegistrar.setOnAction((registroEvento) ->{
                                    RegistroPacientes registroPacientes = new RegistroPacientes();
                                    registroPacientes.subirDatos(
                                            txtNombre.getText(),
                                            txtApellidoP.getText(),
                                            txtApellidoM.getText(),
                                            txtDireccion.getText(),
                                            txtTelefono.getText(),
                                            txtCorreo.getText(),
                                            "2001-10-03"
                                    );
                                });

                            }catch (IOException es){
                                es.printStackTrace();
                            }
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






