package com.example.syshealthfx;

import com.example.syshealthfx.admincontrollers.Historial;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class HistorialController implements Initializable{
    Historial historial;
    @FXML
    private Label fecha;
    @FXML
    private Label nombrePaciente;
    @FXML
    private Label nombreMedico;
    @FXML
    private TextField txtDiagnostico;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextArea txtMedicamento;
    @FXML
    private TextArea txtLaboratorio;
    private Date fechas;
    private String nombreP;
    private String nombreM;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }
    public void setItems(Date fechas, String nombrePacientes, String nombreMedicos){
        this.fechas = fechas;
        this.nombreP = nombrePacientes;
        this.nombreM = nombreMedicos;
        fecha.setText("FECHA DE CONSULTA: "+fechas);
        nombrePaciente.setText("PACIENTE: "+ nombrePacientes);
        nombreMedico.setText("MÉDICO: "+nombreMedicos);
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        System.out.println("fecha mandada "+String.valueOf(fechas));
        String query = "SELECT citas.*, ordenes_laboratorio.id_orden, ordenes_laboratorio.fecha, ordenes_laboratorio.tipo_analisis, recetas.id_receta, recetas.diagnostico, recetas.tratamiento, historial_medico.id_historial, historial_medico.fecha, historial_medico.descripcion " +
                "FROM citas " +
                "JOIN ordenes_laboratorio ON ordenes_laboratorio.id_paciente = citas.id_paciente " +
                "JOIN recetas ON recetas.id_paciente = citas.id_paciente " +
                "JOIN historial_medico ON historial_medico.id_paciente = citas.id_paciente " +
                "WHERE historial_medico.fecha = '"+String.valueOf(fechas)+"';";
        try {
            ResultSet rs = conexion.executeQuery(query);
            if (rs.next()){
                System.out.println("HOLA?");
                txtDiagnostico.setText(rs.getString(10));
                txtDescripcion.setText(rs.getString(5));
                txtMedicamento.setText(rs.getString("tratamiento"));
                txtLaboratorio.setText(rs.getString("tipo_analisis"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrar(){
        Stage stage = (Stage) txtLaboratorio.getScene().getWindow();
        stage.close();
    }


}
