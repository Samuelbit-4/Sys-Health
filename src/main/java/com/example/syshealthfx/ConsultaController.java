package com.example.syshealthfx;

import com.example.syshealthfx.admincontrollers.Citas;
import com.example.syshealthfx.admincontrollers.Historial;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class ConsultaController implements Initializable {
    Citas citas;
    Historial historial;
    @FXML
    private TextField nombrePaciente;
    @FXML
    private TextField fecha;
    @FXML
    private TextField cedula;
    @FXML
    private TextField idCita;
    @FXML
    private TextField nombreMedico;
    @FXML
    private TextField idPaciente;
    @FXML
    private ComboBox<Date> historicoFechas;
    @FXML
    private TextArea descripcion;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnCrearReceta;
    @FXML
    private Button btnCrearLab;
    long idPacienteR;
    long idMedicoR;
    long idCitaR;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnGuardar.setDisable(true);
        descripcion.setOnKeyReleased(event ->{
            if(descripcion.getText().isEmpty()){
                btnGuardar.setDisable(true);
            } else{
                btnGuardar.setDisable(false);
            }
        });

    }
    public void setCitas(Citas citas){
        this.citas = citas;
        nombrePaciente.setText(citas.getNombreCompletoPaciente());
        fecha.setText(String.valueOf(citas.getFechaHora()));
        cedula.setText(citas.getCedulaMedico());
        idCita.setText(String.valueOf(citas.getIdCita()));
        idPaciente.setText(String.valueOf(citas.getIdPaciente()));
        nombreMedico.setText(citas.getNombreCompletoMedico());
        listaHistorial(String.valueOf(citas.getIdPaciente()));
        idPacienteR = citas.getIdPaciente();
        idMedicoR = citas.getIdMedico();
        idCitaR = citas.getIdCita();
    }
    public void sinCita(long idCitas, Date fechas, String cedulas, String nombreMedicos){
        nombreMedico.setText(nombreMedicos);
        idCita.setText(String.valueOf(idCitas));
        fecha.setText(String.valueOf(fechas));
        cedula.setText(cedulas);
        
    }

    public void listaHistorial(String idPaciente){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        try{
            String query = "SELECT * FROM historial_medico WHERE id_paciente="+idPaciente;
            ResultSet rs = conexion.executeQuery(query);
            while (rs.next()){
                System.out.println(rs.getDate("fecha"));
               historicoFechas.getItems().add(rs.getDate("fecha"));
            }
            historicoFechas.setOnAction(event ->{
                Date selectedOption = historicoFechas.getValue();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("medicos-views/historial.fxml"));
                try{
                    Parent root = loader.load();
                    HistorialController historialController = loader.getController();
                    historialController.setItems(selectedOption, citas.getNombreCompletoPaciente(), citas.getNombreCompletoMedico());
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.showAndWait(); // Mostrar la ventana y esperar a que se cierre
                    stage.close(); // Cerrar la ventana antes de que finalice el método
                    Stage stages = (Stage) btnCrearLab.getScene().getWindow();
                    stages.close();
                    stages.show();
                } catch (IOException e){
                    e.printStackTrace();
                }
                event.consume(); // Indicar que el evento ha sido manejado
            });
        } catch (SQLException e){
            e.printStackTrace();
        }
        conexion.disconnect();
    }
    @FXML
    public void guardarHistorial(){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        try{
            String query = "INSERT INTO historial_medico(id_paciente, fecha, descripcion) VALUES(?, ?, ?);";
            PreparedStatement ps = conexion.preparedStatement(query);
            ps.setLong(1, Long.valueOf(idPaciente.getText()));
            ps.setDate(2, java.sql.Date.valueOf(fecha.getText().substring(0,10)));
            if (descripcion.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("UPS!");
                alert.setHeaderText("ERROR!");
                alert.setContentText("La descripción que tratas de guardar no contiene\ninformación, verificalo.");
                alert.showAndWait();
            } else{
                ps.setString(3, descripcion.getText());
                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ÉXITO");
                alert.setHeaderText("DATOS GUARDADOS");
                alert.setContentText("Los datos de la consulta se han guardado con éxito.");
                alert.showAndWait();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void crearReceta(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("medicos-views/recetas.fxml"));
        try{
            Parent root = loader.load();
            RecetasController recetasController = loader.getController();
            recetasController.setItems(citas);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
            btnCrearReceta.setDisable(true);

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void crearLaboratorio(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("medicos-views/laboratorio.fxml"));
        try{
            Parent root = loader.load();
            LaboratorioController laboratorioController = loader.getController();
            laboratorioController.setItems(citas);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
            btnCrearLab.setDisable(true);

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void terminarCita(){
        if (btnCrearReceta.isDisable() == false && btnCrearLab.isDisable() == false ) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("CONFIRMACIÓN");
            alerta.setHeaderText("¿...?");
            alerta.setContentText("La consulta no guardó receta ni orden de laboratorio");
            alerta.showAndWait();
            try {
                guardarSinLab();
                guardarSinReceta();
                Stage stages = (Stage) btnCrearLab.getScene().getWindow();
                stages.close();
            } catch (SQLException s) {
                s.printStackTrace();
            }
        } else if (btnCrearReceta.isDisable() == false) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("CONFIRMACIÓN");
            alerta.setHeaderText("¿...?");
            alerta.setContentText("La consulta no guardó ninguna receta");
            alerta.showAndWait();
            try {
                guardarSinReceta();
                Stage stages = (Stage) btnCrearLab.getScene().getWindow();
                stages.close();
            } catch (SQLException s) {
                s.printStackTrace();
            }
        } else if (btnCrearLab.isDisable() == false) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("CONFIRMACIÓN");
            alerta.setHeaderText("¿...?");
            alerta.setContentText("La consulta no guardó ninguna orden de laboratorio");
            alerta.showAndWait();
            try {
                guardarSinLab();
                Stage stages = (Stage) btnCrearLab.getScene().getWindow();
                stages.close();
            } catch (SQLException s) {
                s.printStackTrace();
            }
        }

    }
    public void guardarSinReceta() throws SQLException {
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();

        String query = "INSERT INTO recetas(id_paciente, id_medico, fecha, diagnostico, tratamiento, id_cita)" +
                " VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conexion.preparedStatement(query);
        ps.setLong(1, citas.getIdPaciente());
        ps.setLong(2, citas.getIdMedico());
        ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
        ps.setString(4, "SIN DIAGNOSTICO");
        ps.setString(5, "SIN TRATAMIENTO");
        ps.setLong(6, citas.getIdCita());
        int state = ps.executeUpdate();
    }
    public void guardarSinLab() throws SQLException{
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();

        String query = "INSERT INTO ordenes_laboratorio(id_paciente, id_medico, fecha, tipo_analisis)" +
                    " VALUES(?, ?, ?, ?)";
        PreparedStatement ps = conexion.preparedStatement(query);
        ps.setLong(1, citas.getIdPaciente());
        ps.setLong(2,citas.getIdMedico());
        ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
        ps.setString(4, "SIN ANALISIS");
        int state = ps.executeUpdate();
    }

}
