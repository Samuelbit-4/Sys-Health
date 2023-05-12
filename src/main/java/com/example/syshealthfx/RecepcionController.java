package com.example.syshealthfx;

import com.example.syshealthfx.admincontrollers.Pacientes;
import com.example.syshealthfx.admincontrollers.TablaPacientes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RecepcionController implements Initializable {
    @FXML
    private AnchorPane frameAdmin;
    @FXML
    private Button btnPacientes;
    @FXML
    private Button btnCitas;
    @FXML
    private Button cerrarSesion;
    @FXML
    private HBox contenidoHBox;
    @FXML
    private Button btnInicio;
    @FXML
    private Label textInicio;
    @FXML
    private VBox contenidoInicio;
    @FXML
    private HBox registrarUsuario;
    @FXML
    private HBox buscarPaciente;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textInicio.setText("BIENVENIDO: "+SesionUsuario.getUsuario());
        registrarUsuario.setOnMouseClicked(event ->{
            btnPacientes.fire();
        });
        buscarPaciente.setOnMouseClicked(event ->{
            btnPacientes.fire();
        });
    }
    @FXML
    public void mostrarInicio(ActionEvent actionEvent){
        Stage stage = new Stage();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("recepcion-views/index-recepcion.fxml"));
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
    @FXML
    public void mostrarPacientes(){
        removerElementos();
        System.out.println("PACIENTES");
        mostrarVentana("pacientes");
        VBox vn = (VBox) contenidoHBox.lookup("#contenidoInicio");
        HBox btnListaPacientes = (HBox) vn.lookup("#btnListaPacientes");
        HBox btnRegistrarPacientes = (HBox) vn.lookup("#btnRegistrarPaciente");
        HBox btnGenerarPDF = (HBox) vn.lookup("#btnReportePacientes");
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
        btnGenerarPDF.setOnMouseClicked((pdf) ->{
            RegistroPacientes registro = new RegistroPacientes();
            HBox hbox = (HBox) pdf.getSource();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar ubicación");
            File initialDirectory = new File(System.getProperty("user.home"));
            fileChooser.setInitialDirectory(initialDirectory);
            File selectedDirectory = fileChooser.showSaveDialog(hbox.getScene().getWindow());
            if (selectedDirectory != null) {
                System.out.println("Ubicación seleccionada: " + selectedDirectory.getAbsolutePath());
                registro.reporteCompleto(selectedDirectory.getAbsolutePath());
                Alert aviso = new Alert(Alert.AlertType.INFORMATION);
                aviso.setTitle("PDF CREADO CON EXITO");
                aviso.setHeaderText("REPORTE GENERADO CON EXITO");
                aviso.setContentText("El reporte a sido creado con éxito\nSe a guardado en: "+selectedDirectory.getAbsolutePath());
                aviso.show();
            }
        });
    }
    @FXML
    public void ventanaCitas(){
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
            stage.setResizable(false);
            Image image = new Image(getClass().getResourceAsStream("assets/doctor.png"));
            stage.getIcons().add(image);
            stage.setTitle("CITAS");
            stageActual.hide();
            stage.showAndWait();
            stageActual.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void cerrarSesionEvent(ActionEvent actionEvent){
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
