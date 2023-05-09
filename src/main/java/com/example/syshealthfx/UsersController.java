package com.example.syshealthfx;


import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.MonthView;
import com.example.syshealthfx.admincontrollers.*;

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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.*;
import org.joda.time.LocalDateTime;


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
        nombreInicio.setText("BIENVENIDO: "+SesionUsuario.getUsuario());
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
                            //botonActual.setDisable(true);
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("registro-usuario.fxml"));
                            VBox vn1 = null;
                            try {
                                vn1 = (VBox) loader.load();
                                vn1.setId("contenidoInicio");
                                RegistroController registro = new RegistroController(vn1);
                                VBox vn2 = registro.getV();
                                vn2.setId("contenidoInicio");
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
                                       // botonActual.setDisable(false);
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
                                TextField nombreUsuario = (TextField) vn1.lookup("#nombreUsuario");
                                Button btnBuscar = (Button) vn1.lookup("#btnBuscar");
                                btnVolver.setOnAction((e) ->{
                                    removerElementos();
                                    mostrarVentana("departamentos");
                                });
                                TablaPacientes buscar = new TablaPacientes();
                                btnBuscar.setOnAction(action ->{
                                    System.out.println("HOLA");
                                    Usuarios usuario = buscar.buscarEmpleado(nombreUsuario.getText());
                                    Stage stage = new Stage();

                                    try {
                                        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("admin-views/departamentos/actualizar-empleado.fxml"));
                                        Parent root = null;
                                        root = loader1.load();
                                        ModificarEmpleado modificarEmpleado = loader1.getController();
                                        modificarEmpleado.setUsuario(usuario);

                                        Scene scene = new Scene(root);
                                        stage.setScene(scene);
                                        stage.show();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                nombreUsuario.setOnAction(action ->{
                                    btnBuscar.fire();
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
                                vn1.setId("contenidoInicio");
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
                            Button btnBuscar = (Button) actual.lookup("#btnBuscar");
                            TextField txtNombre = (TextField) actual.lookup("#txtNombrePaciente");
                            txtNombre.setOnAction(event ->{
                                btnBuscar.fire();
                            });
                            TablaPacientes pacientes1 = new TablaPacientes();
                            btnBuscar.setOnAction(event ->{
                                Pacientes paciente = (Pacientes) pacientes1.buscarPaciente(txtNombre.getText());
                                try{
                                    Stage stage = new Stage();
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-views/pacientes/actualizar-paciente.fxml"));
                                    Parent root = null;
                                    root = loader.load();
                                    ModificarPaciente modificarPaciente = loader.getController();
                                    modificarPaciente.setPaciente(paciente);
                                    Scene scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.showAndWait();
                                    VBox contenedor = (VBox) actual.lookup("#tablaPacientes");
                                    actual.getChildren().remove(contenedor);
                                    actual.getChildren().add(tablaPacientes.mostrarTabla());
                                } catch (IOException ese){
                                    ese.printStackTrace();
                                }
                            });
                        });
                        btnRegistrarPacientes.setOnMouseClicked((registrarEvent) ->{
                            removerElementos();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-views/pacientes/registrar-paciente.fxml"));
                            VBox vn1 = null;
                            try{
                                vn1 = (VBox) loader.load();
                                vn1.setId("contenidoInicio");
                                ToggleGroup genero = new ToggleGroup();
                                RadioButton rb1 = new RadioButton("MASCULINO");
                                rb1.setSelected(true);
                                rb1.setStyle("-fx-font-weight: bold");
                                RadioButton rb2 = new RadioButton("FEMENINO");
                                rb2.setStyle("-fx-font-weight: bold");
                                rb1.setToggleGroup(genero);
                                rb2.setToggleGroup(genero);
                                VBox toggle = (VBox) vn1.lookup("#toggleGenero");
                                toggle.spacingProperty().add(5);
                                toggle.getChildren().addAll(rb1, rb2);

                                contenidoHBox.getChildren().add(vn1);

                                TextField txtNombre = (TextField) vn1.lookup("#txtNombre");
                                TextField txtApellidoP = (TextField) vn1.lookup("#txtApellidoP");
                                TextField txtApellidoM = (TextField) vn1.lookup("#txtApellidoM");
                                TextField txtDireccion = (TextField) vn1.lookup("#txtDireccion");
                                TextField txtTelefono = (TextField) vn1.lookup("#txtTelefono");
                                TextField txtCorreo = (TextField) vn1.lookup("#txtCorreo");
                                DatePicker txtFecha = (DatePicker) vn1.lookup("#txtFecha");

                                Button btnRegistrar = (Button) vn1.lookup("#btnRegistrar");
                                Button btnCancelar = (Button) vn1.lookup("#btnCancelar");
                                RadioButton selected = (RadioButton) genero.getSelectedToggle();
                                String generoSelected = selected.getText();
                                btnRegistrar.setOnAction((registroEvento) ->{
                                    RegistroPacientes registroPacientes = new RegistroPacientes();
                                    registroPacientes.subirDatos(
                                            txtNombre.getText(),
                                            txtApellidoP.getText(),
                                            txtApellidoM.getText(),
                                            txtDireccion.getText(),
                                            txtTelefono.getText(),
                                            txtCorreo.getText(),
                                            String.valueOf(txtFecha.getValue()),
                                            generoSelected
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
                       // HBox btnConsultarOrden = (HBox) vn.lookup("#btnConsultarOrden");
                       // HBox btnAgregarPrueba = (HBox) vn.lookup("#btnAgregarPrueba");
                        HBox btnGenerarReportePrueba = (HBox) vn.lookup("#btnGenerarReportePruebas");

                        btnListaPruebas.setOnMouseClicked((listaEvent) ->{
                            removerElementos();
                            VBox vn1 = mostrarVentana("laboratorio", "lista-pruebas");
                            TablaLab tabla = new TablaLab();
                            vn1.getChildren().add(tabla.mostrarTabla());

                        });
                        btnGenerarReportePrueba.setOnMouseClicked((reporte) ->{
                            TablaLab reportepdf = new TablaLab();
                            reportepdf.generarReporteLab();
                        });

                    }
                    case "btnCitas" ->{
                        System.out.println("CITAS");
                        removerElementos();
                        Stage stageActual = (Stage) frameAdmin.getScene().getWindow();

                        Stage stage = new Stage();
                        try{
                            Parent root = FXMLLoader.load(getClass().getResource("admin-views/citas/calendario.fxml"));
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.setMaximized(true);
                            stage.centerOnScreen();
                            stageActual.hide();
                            stage.showAndWait();
                            stageActual.show();
                        } catch (IOException e){

                        }
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

}






