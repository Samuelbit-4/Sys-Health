package com.example.syshealthfx;

import com.example.syshealthfx.admincontrollers.Pacientes;
import com.example.syshealthfx.admincontrollers.Usuarios;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModificarPaciente implements Initializable {
    Pacientes pacientes;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidoP;
    @FXML
    private TextField txtApellidoM;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private DatePicker date;
    @FXML
    private VBox vboxGenero;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnCancelar;


    ToggleGroup genero;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genero = new ToggleGroup();
        RadioButton rb1 = new RadioButton("MASCULINO");
        rb1.setSelected(true);
        rb1.setStyle("-fx-font-weight: bold");
        RadioButton rb2 = new RadioButton("FEMENINO");
        rb2.setStyle("-fx-font-weight: bold");
        rb1.setToggleGroup(genero);
        rb2.setToggleGroup(genero);

        vboxGenero.spacingProperty().add(5);
        vboxGenero.getChildren().addAll(rb1, rb2);
    }
    public void setPaciente(Pacientes pacientes) {
        this.pacientes = pacientes;

        // Aquí puedes llenar los campos de la ventana con los datos del objeto usuario
        txtNombre.setText(pacientes.getNombre());
        txtApellidoP.setText(pacientes.getApellidoP());
        txtApellidoM.setText(pacientes.getApellidoM());
        if(!(pacientes.getGenero() == "MASCULINO")){
            genero.getSelectedToggle().setSelected(true);
        }

        date.setValue(LocalDate.parse(String.valueOf(pacientes.getFechaNaci())));
        txtDireccion.setText(pacientes.getDireccion());
        txtCorreo.setText(pacientes.getCorreoElectronico());
        txtTelefono.setText(pacientes.getTelefono());

    }
    @FXML
    public void actualizarPaciente(){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        String query = "UPDATE pacientes SET nombre=?, apellido_paterno=?, genero=?, direccion=?, telefono=?, correo_electronico=?, fecha_nacimiento=?" +
                " WHERE id_paciente="+pacientes.getIdPaciente();
        try{
            RadioButton seleccionR = (RadioButton) genero.getSelectedToggle();
            String generoS = seleccionR.getText();
            PreparedStatement ps = conexion.preparedStatement(query);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtApellidoP.getText());
            ps.setString(3, generoS);
            ps.setString(4, txtDireccion.getText());
            ps.setString(5, txtTelefono.getText());
            ps.setString(6, txtCorreo.getText());
            ps.setDate(7, Date.valueOf(date.getValue()));
            int state = ps.executeUpdate();
            if (state == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ACTUALIZACION");
                alert.setHeaderText("DATOS ACTUALIZADOS CON ÉXITO");
                alert.show();
                Stage stage = (Stage) btnModificar.getScene().getWindow();
                stage.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void cancelar(){
        Stage stage = (Stage) btnModificar.getScene().getWindow();
        stage.close();
    }
}
