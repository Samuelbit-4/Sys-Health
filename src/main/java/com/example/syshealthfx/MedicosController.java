package com.example.syshealthfx;

import com.example.syshealthfx.admincontrollers.Citas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class MedicosController implements Initializable {
    @FXML
    private Label txtInicio;
    @FXML
    private VBox citasHoy;
    @FXML
    private Button btnCerrarSesion;
    Citas datoCitas;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mostrarCitas();
    }

    private void mostrarCitas(){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();

        try{
            String query = "SELECT usuarios.id_empleado, empleados.nombre, empleados.apellido_paterno, empleados.apellido_materno FROM usuarios JOIN empleados ON usuarios.id_empleado = empleados.id_empleado WHERE usuarios.usuario ='"+SesionUsuario.getUsuario()+"';";
            String nombre = "";
            /*String query = "SELECT citas.*, medicos.cedula_profesional, medicos.id_empleado, empleados.nombre, empleados.apellido_paterno, empleados.apellido_materno, " +
                    "pacientes.nombre, pacientes.apellido_paterno, pacientes.apellido_materno, pacientes.fecha_nacimiento FROM citas " +
                    "JOIN medicos ON citas.id_medico = medicos.id_medico " +
                    "JOIN empleados ON medicos.id_empleado = empleados.id_empleado " +
                    "JOIN pacientes ON citas.id_paciente = pacientes.id_paciente;" +
                    "WHERE empleados.nombre=?";*/
            ResultSet rs = conexion.executeQuery(query);

            if (rs.next()){
                nombre = rs.getString("nombre");
                System.out.println("NOMBRE SI");
            }
            txtInicio.setText("BIENVENIDO: "+nombre);
            String query2 = "SELECT citas.id_cita, citas.fecha_hora, citas.descripcion ," +
                    "medicos.id_medico,medicos.id_empleado, empleados.nombre, empleados.apellido_paterno, " +
                    "empleados.apellido_materno,pacientes.id_paciente, pacientes.nombre, " +
                    "pacientes.apellido_paterno, pacientes.apellido_materno " +
                    "FROM citas " +
                    "JOIN medicos ON citas.id_medico = medicos.id_medico " +
                    "JOIN empleados ON medicos.id_empleado = empleados.id_empleado " +
                    "JOIN pacientes ON citas.id_paciente = pacientes.id_paciente " +
                    "WHERE citas.fecha_hora >= CURDATE() AND empleados.nombre='"+nombre+"'" + // Agregar esta cláusula para verificar que la fecha actual no sea anterior
                    "ORDER BY citas.fecha_hora ASC;";
            rs = conexion.executeQuery(query2);
            VBox contenedorCita = new VBox();
            if (!rs.next()){
                citasHoy.getChildren().add(new Label("SIN CITAS"));
            }
            while (rs.next()){
                VBox citaC = new VBox();
                citaC.setId("#contenedorCita");
                citaC.getChildren().add(new Label(String.valueOf("ID: "+ rs.getLong("id_cita"))));
                citaC.getChildren().add(new Label("FECHA - HORA : "+String.valueOf(rs.getTimestamp(2))));
                citaC.getChildren().add(new Label("MEDICO: "+ rs.getString(6) + " " + rs.getString(7) + " " + rs.getString(8)));
                citaC.getChildren().add(new Label("PACIENTE: " + rs.getString(10) + " " + rs.getString(11) + " " + rs.getString(12)));
                contenedorCita.getStyleClass().add("contenedor-cita");
                contenedorCita.setAlignment(Pos.TOP_CENTER);

                contenedorCita.getChildren().add(citaC);
                String paciente = String.valueOf(rs.getLong("id_paciente")) + " - " + rs.getString(10) + " " + rs.getString(11) + " " + rs.getString(12);
                String medico = String.valueOf(rs.getLong("id_medico"))+ " - " + rs.getString(6) + " " + rs.getString(7) + " " + rs.getString(8);
                long idCita = rs.getLong("id_cita");
                citasHoy.getChildren().add(contenedorCita);
                citaC.setOnMouseClicked(event ->{
                    if(event.getClickCount() == 2){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("medicos-views/consulta-paciente.fxml"));
                        try{
                            Parent root = loader.load();
                            ConsultaController consultaController = loader.getController();

                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.showAndWait();


                        } catch (IOException e){
                            e.printStackTrace();
                        }

                    }
                });
            }



        } catch (SQLException e){

        }
    }
    @FXML
    public void cerrarSesion(ActionEvent actionEvent){
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
