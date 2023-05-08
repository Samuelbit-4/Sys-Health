package com.example.syshealthfx;

import com.example.syshealthfx.admincontrollers.Usuarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModificarEmpleado implements Initializable {
    @FXML
    private Label label;
    @FXML
    private VBox contenedor;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidoP;
    @FXML
    private TextField txtApellidoM;
    @FXML
    private DatePicker date;
    @FXML
    private TextField txtDomicilio;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtCedula;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtTelefono;
    @FXML
    private Button modificarUsuario;
    @FXML
    private Button vaciar;
    @FXML
    private Button cancelar;
    @FXML
    private HBox rbtGenero;
    @FXML
    private HBox rbtRol;
    @FXML
    private VBox vboxDepartamento;
    private Usuarios usuario;

    ToggleGroup genero;
    ToggleGroup rol;
    ComboBox<String> cmbDepartamentos;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genero = new ToggleGroup();
        RadioButton rb1 = new RadioButton("MASCULINO");
        rb1.setSelected(true);
        RadioButton rb2 = new RadioButton("FEMENINO");
        rb1.setToggleGroup(genero);
        rb2.setToggleGroup(genero);

        rbtGenero.getChildren().addAll(rb1, rb2);
        rol = new ToggleGroup();
        RadioButton rol1 = new RadioButton("Medico");
        RadioButton rol2 = new RadioButton("Administrador");
        RadioButton rol3 = new RadioButton("Recepcionista");

        rol1.setToggleGroup(rol);
        rol2.setToggleGroup(rol);
        rol3.setToggleGroup(rol);
        rol2.setSelected(true);
        rbtRol.getChildren().addAll(rol1, rol2, rol3);
        SQLClass con = new SQLClass("root", "", "sys_health_prueba");
        ObservableList<String> departamentos = FXCollections.observableArrayList();
        con.connect();
        try{
            ResultSet set = con.executeQuery("SELECT nombre_departamento FROM departamentos");
            while (set.next()){
                String nombreDepartamento = set.getString("nombre_departamento");
                departamentos.add(nombreDepartamento);
            }
            cmbDepartamentos = new ComboBox<>(departamentos);
            cmbDepartamentos.promptTextProperty().set("-SELECCIONA");
            cmbDepartamentos.setPrefWidth(211);
            cmbDepartamentos.setId("cmbDepartamentos");
            vboxDepartamento.getChildren().add(cmbDepartamentos);

        } catch (SQLException e){
            e.printStackTrace();
        }
        RadioButton selectedReadioButton = (RadioButton) rol.getSelectedToggle();
        String selectedText = selectedReadioButton.getText();
        if (selectedText != "Medico"){
            txtCedula.setDisable(true);
        }
        rol.selectedToggleProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue == rol1){
                txtCedula.setDisable(false);
            } else{
                txtCedula.setDisable(true);
            }
        });
    }
    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;

        // Aquí puedes llenar los campos de la ventana con los datos del objeto usuario
        txtNombre.setText(usuario.getNombre());
        txtApellidoP.setText(usuario.getApellidoP());
        txtApellidoM.setText(usuario.getApellidoM());
        if(!(usuario.getGenero() == "MASCULINO")){
            genero.getSelectedToggle().setSelected(true);
        }

        date.setValue(LocalDate.parse(String.valueOf(usuario.getFechaNacimiento())));
        txtDomicilio.setText(usuario.getDomicilio());
        txtCorreo.setText(usuario.getCorreoElectronico());
        txtTelefono.setText(usuario.getTelefono());
        cmbDepartamentos.setValue(usuario.getNombreDepartamento());
        txtUsuario.setText(usuario.getUsuario());
    }
    @FXML
    public void actualizarUsuario(){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        String query = "UPDATE empleados SET id_departamento=?, nombre=?, apellido_paterno=?, apellido_materno=?, genero=?, " +
                "direccion=?, telefono=?, correo_electronico=?, fecha_nacimiento=? WHERE id_empleado="+usuario.getIdEmpleado();
        String consulta = "SELECT id_departamento FROM departamentos WHERE nombre_departamento='"+cmbDepartamentos.getValue()+"';";

        try{
            ResultSet rs = conexion.executeQuery(consulta);
            long idDepartamento = 0;
            if(rs.next()){
                idDepartamento = rs.getLong("id_departamento");
            }
            RadioButton selectedReadioButton = (RadioButton) genero.getSelectedToggle();
            String selectedText = selectedReadioButton.getText();
            PreparedStatement ps = conexion.preparedStatement(query);
            ps.setLong(1,idDepartamento);
            ps.setString(2, txtNombre.getText());
            ps.setString(3, txtApellidoP.getText());
            ps.setString(4, txtApellidoM.getText());
            ps.setString(5, selectedText);
            ps.setString(6, txtDomicilio.getText());
            ps.setString(7, txtTelefono.getText());
            ps.setString(8, txtCorreo.getText());
            ps.setDate(9, Date.valueOf(date.getValue()));

            int state1 = 0, state2 = 0, state3 = 0;
            RadioButton button = (RadioButton) rol.getSelectedToggle();
            String rolSeleccionado = button.getText();
            String query2 = "UPDATE medicos SET cedula WHERE id_empleado='"+usuario.getIdEmpleado()+"';";
            switch (rolSeleccionado){
                case "Medico" ->{
                    state1 = ps.executeUpdate();

                    PreparedStatement ps2 = conexion.preparedStatement(query2);
                    ps2.setString(1, txtCedula.getText());
                    state2 = ps2.executeUpdate();
                }
                case "Recepcionista", "Administrador" ->{
                    state1 = ps.executeUpdate();
                }
            }
            String query3 = "UPDATE usuarios SET usuario=?, contrasena=SHA2(?, 256) WHERE id_usuario="+usuario.getIdUsuario();
            PreparedStatement ps3 = conexion.preparedStatement(query3);
            ps3.setString(1, txtUsuario.getText());
            ps3.setString(2, txtPassword.getText());
            state3 = ps3.executeUpdate();
            boolean state1and2 = state1 == 1 && state3 == 1;
            boolean stateM = state2 == 1;
            if(state1and2 || stateM) {
                Alert alertSi = new Alert(Alert.AlertType.INFORMATION);
                alertSi.setHeaderText("CAPTURA EXITOSA");
                alertSi.setTitle("EXITO");
                alertSi.setContentText("LOS DATOS SE HAN GUARDADO CORRECTAMENTE");
                txtCedula.getScene().getWindow().hide();
            }


        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void vaciarFormulario(){
        contenedor.lookupAll("TextField").forEach(txt ->{
            TextField text = (TextField) txt;
            text.setText("");
        });
        date.setValue(null);
    }
    @FXML
    public void cancelar(){
        Stage stage = (Stage) txtApellidoM.getScene().getWindow();
        stage.close();
    }
}