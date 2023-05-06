package com.example.syshealthfx.admincontrollers;

import com.example.syshealthfx.SQLClass;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TablaPacientes {
    public TableView<Pacientes> mostrarTabla() {
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        TableView<Pacientes> tablaPacientes = new TableView<>();
        ObservableList<Pacientes> listaPacientes = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM pacientes";
            ResultSet rs = conexion.executeQuery(query);
            while (rs.next()) {
                Pacientes pacientes = new Pacientes(rs.getLong("id_paciente"), rs.getString("nombre"), rs.getString("apellido_paterno"), rs.getString("apellido_materno"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("correo_electronico"), rs.getString("fecha_nacimiento"));
                listaPacientes.add(pacientes);
            }
            TableColumn<Pacientes, Integer> idColumn = new TableColumn<>("ID PACIENTE");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("idPaciente"));

            TableColumn<Pacientes, String> nombreColumn = new TableColumn<>("NOMBRE");
            nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

            TableColumn<Pacientes, String> apellidoPColumn = new TableColumn<>("APELLIDO PATERNO");
            apellidoPColumn.setCellValueFactory(new PropertyValueFactory<>("apellidoP"));

            TableColumn<Pacientes, String> apellidoMColumn = new TableColumn<>("APELLIDO MATERNO");
            apellidoMColumn.setCellValueFactory(new PropertyValueFactory<>("apellidoM"));

            TableColumn<Pacientes, String> direccionColumn = new TableColumn<>("DIRECCIÓN");
            direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));

            TableColumn<Pacientes, String> telefonoColumn = new TableColumn<>("TELEFONO");
            telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));

            TableColumn<Pacientes, String> correoColumn = new TableColumn<>("CORREO ELECTRONICO");
            correoColumn.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));

            TableColumn<Pacientes, String> fechaNColumn = new TableColumn<>("FECHA DE NACIMIENTO");
            fechaNColumn.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));

            tablaPacientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tablaPacientes.setItems(listaPacientes);

            tablaPacientes.getColumns().addAll(idColumn, nombreColumn, apellidoPColumn, apellidoMColumn, direccionColumn, telefonoColumn, correoColumn, fechaNColumn);

            conexion.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tablaPacientes;
    }

}
