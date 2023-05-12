package com.example.syshealthfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModificarCita implements Initializable {
    @FXML
    private ComboBox<String> seleccionarPaciente;
    @FXML
    private ComboBox<String> seleccionarMedico;
    @FXML
    private DatePicker seleccionarDia;
    @FXML
    private ComboBox<LocalTime> seleccionarHorario;

    LocalTime horaSeleccionada;
    String idMedico;
    String idPaciente;
    long idCita;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        seleccionarMedico.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Se seleccionó: " + newVal);
            idMedico = newVal.substring(0,18);
            System.out.println("SUB: " + idMedico.substring(0,18));
        });
        seleccionarPaciente.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Se seleccionó: " + newVal);
            idPaciente = newVal.substring(0,18);
        });
        seleccionarHorario.valueProperty().addListener((obs, oldVal, newVal) ->{
            horaSeleccionada = newVal;
        });
    }
    public void setCita(String paciente, String medico, long idCita){
        seleccionarPaciente.setValue(paciente);
        asignarHorasMedico(medico);
        this.idCita = idCita;
    }
    @FXML
    public void modificarCita() {
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        try {
            String query = "SELECT * FROM citas WHERE id_medico = ? AND fecha_hora = ?";
            PreparedStatement ps = conexion.preparedStatement(query);
            ps.setLong(1, Long.parseLong(idMedico));
            LocalDateTime fechaHora = LocalDateTime.of(seleccionarDia.getValue(), horaSeleccionada);
            ps.setTimestamp(2, Timestamp.valueOf(fechaHora));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR");
                alert.setTitle("ERROR");
                alert.setContentText("Ya hay una cita programada para ese\n médico en esa fecha y hora.");
                alert.showAndWait();
                return; // no se ejecuta la inserción
            }
            query = "UPDATE citas SET id_medico=?, id_paciente=?, fecha_hora=? WHERE id_cita=?";
            ps = conexion.preparedStatement(query);
            ps.setLong(1, Long.parseLong(idMedico));
            ps.setLong(2, Long.parseLong(idPaciente));
            ps.setTimestamp(3, Timestamp.valueOf(fechaHora));
            ps.setLong(4, idCita);
            ps.executeUpdate();
            System.out.println("YAAA CITA");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CITA MODIFICADA");
            alert.setHeaderText("CITA MODIFICADA CON ÉXITO");
            alert.setContentText("La cita se ha modificado con éxito");
            alert.showAndWait();
            Stage stage = (Stage) seleccionarDia.getScene().getWindow();
            stage.close();
            conexion.disconnect();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void borrarCita() throws SQLException {
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        try {
            conexion.setAutoCommit(false); // Habilita el control de transacciones

            // Eliminar las filas en la tabla "recetas" que hacen referencia a la fila en la tabla "citas"
            String queryRecetas = "DELETE FROM recetas WHERE id_cita=?";
            PreparedStatement stRecetas = conexion.preparedStatement(queryRecetas);
            stRecetas.setLong(1, idCita);
            int filasAfectadasRecetas = stRecetas.executeUpdate();

            // Eliminar la fila en la tabla "citas"
            String queryCitas = "DELETE FROM citas WHERE id_cita=?";
            PreparedStatement stCitas = conexion.preparedStatement(queryCitas);
            stCitas.setLong(1, idCita);
            int filasAfectadasCitas = 0;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ELIMINACIÓN...");
            alert.setHeaderText("¿ESTA SEGURO DE ELIMINAR LA CITA?");
            alert.setContentText("Esta accion no puede ser deshecha");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                filasAfectadasCitas = stCitas.executeUpdate();
                if(filasAfectadasCitas == 1 && filasAfectadasRecetas >= 0) {
                    conexion.commit(); // Confirma la transacción
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Éxito");
                    alert1.setHeaderText("Cita eliminada con éxito");
                    alert1.showAndWait();
                    Stage stage = (Stage) seleccionarDia.getScene().getWindow();
                    stage.close();
                } else {
                    conexion.rollback(); // Revierte la transacción
                    Alert alert3 = new Alert(Alert.AlertType.WARNING);
                    alert3.setTitle("Error");
                    alert3.setHeaderText("No se pudo eliminar la cita");
                    alert3.showAndWait();
                }
            }
        } catch(SQLException e) {
            conexion.rollback(); // Revierte la transacción
            e.printStackTrace();
        } finally {
            conexion.setAutoCommit(true); // Restaura el control de transacciones a su valor predeterminado
            conexion.disconnect();
        }

    }
    public void asignarHorasMedico(String medico){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        try{
            String query = "SELECT medicos.id_medico, medicos.especialidad, medicos.cedula_profesional, medicos.id_empleado, empleados.id_empleado, empleados.nombre, empleados.apellido_paterno, empleados.apellido_materno " +
                    "FROM medicos INNER JOIN empleados ON medicos.id_empleado = empleados.id_empleado;";
            ResultSet rsMedico = conexion.executeQuery(query);
            while (rsMedico.next()){
                long cedula = rsMedico.getLong("id_medico");
                String nombreCompleto = rsMedico.getString("nombre") + " " + rsMedico.getString("apellido_paterno") + " " + rsMedico.getString("apellido_materno");
                String seleccionarMedicoTexto = cedula + " - " +  nombreCompleto;
                seleccionarMedico.getItems().add(seleccionarMedicoTexto);
            }
            seleccionarMedico.setValue(medico);
            conexion.disconnect();
        } catch (SQLException e){
            e.printStackTrace();
        }
        LocalTime[] horas = new LocalTime[17]; // arreglo con 17 elementos
        LocalTime hora = LocalTime.of(8, 0, 0); // hora inicial a las 8:00:00

        for (int i = 0; i < horas.length; i++) {
            horas[i] = hora; // asignar la hora actual al arreglo
            hora = hora.plusMinutes(30);
            seleccionarHorario.getItems().add(hora);
        }
    }


}
