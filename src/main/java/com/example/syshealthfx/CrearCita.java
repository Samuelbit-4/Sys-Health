package com.example.syshealthfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CrearCita implements Initializable {
    @FXML
    private ComboBox<String> seleccionarPaciente;
    @FXML
    private ComboBox<String> seleccionarMedico;
    @FXML
    private ComboBox<String> especialidad;
    @FXML
    private DatePicker seleccionarDia;
    @FXML
    private ComboBox<LocalTime> seleccionarHorario;
    @FXML
    private ScrollPane citasHoy;

    String idMedico ;
    String idPaciente;
    LocalTime horaSeleccionada;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mostrarDatosActualizados();
        seleccionarMedico.valueProperty().addListener((obs, oldVal, newVal) -> {
            try{
                System.out.println("Se seleccionó: " + newVal);
                idMedico = newVal.substring(0,18);
                System.out.println("SUB: " + idMedico.substring(0,18));
            } catch (Exception e){
                System.out.println("ningun valor");
            }
        });

        seleccionarPaciente.valueProperty().addListener((obs, oldVal, newVal) -> {
            try{
                System.out.println("Se seleccionó: " + newVal);
                idPaciente = newVal.substring(0,18);
            } catch (Exception e){
                System.out.println("ningun valor seleccionado");
            }
        });
        seleccionarPaciente.setOnKeyReleased(keyEvent -> {
            String input = seleccionarPaciente.getEditor().getText();
            seleccionarPaciente.getItems().setAll(filterItems(seleccionarPaciente.getItems(), input));
            seleccionarPaciente.show();
        });
        seleccionarHorario.valueProperty().addListener((obs, oldVal, newVal) ->{
            horaSeleccionada = newVal;
        });


    }
    private List<String> filterItems(List<String> items, String input) {
        return items.stream()
                .filter(item -> item.toLowerCase().contains(input.toLowerCase()))
                .collect(Collectors.toList());
    }


    public void mostrarDatosActualizados(){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        try{
            ResultSet rsPacientes = conexion.executeQuery("SELECT * FROM pacientes");
            while (rsPacientes.next()){
                long idPaciente = rsPacientes.getLong(1);
                String nombrePaciente = rsPacientes.getString("nombre") + " " + rsPacientes.getString("apellido_materno") + " " + rsPacientes.getString("apellido_paterno");
                String idNombrePaciente = String.valueOf(idPaciente) + " - " + nombrePaciente;
                seleccionarPaciente.getItems().add(idNombrePaciente);
            }
            conexion.disconnect();
        } catch (SQLException e){
            e.printStackTrace();
        }
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
    @FXML
    public void subirCita(ActionEvent event){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        try{
            String query = "SELECT * FROM citas WHERE id_medico = ? AND fecha_hora = ?";
            PreparedStatement ps = conexion.preparedStatement(query);
            ps.setLong(1, Long.parseLong(idMedico));
            LocalDateTime fechaHora = LocalDateTime.of(seleccionarDia.getValue(), horaSeleccionada);
            ps.setTimestamp(2, Timestamp.valueOf(fechaHora));
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR");
                alert.setTitle("ERROR");
                alert.setContentText("Ya hay una cita programada para ese\n médico en esa fecha y hora.");
                alert.showAndWait();
                return; // no se ejecuta la inserción
            }

            // si no hay citas programadas para ese médico en esa fecha y hora, se inserta la nueva cita
            query = "INSERT INTO citas(id_medico, id_paciente, fecha_hora, descripcion) VALUES(?, ?, ?, ?);";
            ps = conexion.preparedStatement(query);
            ps.setLong(1, Long.parseLong(idMedico));
            ps.setLong(2, Long.parseLong(idPaciente));
            ps.setTimestamp(3, Timestamp.valueOf(fechaHora));
            ps.setString(4, "PRUEBA");
            ps.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("CITA CREADA CON ÉXITO");
            alert.setTitle("ÉXITO");
            alert.setContentText("La cita ha sido creada con éxito");
            alert.showAndWait();
            seleccionarPaciente.getScene().getWindow().hide();

        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
