package com.example.syshealthfx.admincontrollers;

import com.example.syshealthfx.SQLClass;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;

public class TablaPacientes {
    public VBox mostrarTabla() {
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        VBox contenedor = new VBox();
        contenedor.setId("tablaPacientes");
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
            FilteredList<Pacientes> filteredData = new FilteredList<>(listaPacientes);

            conexion.disconnect();


            contenedor.getChildren().add(tablaPacientes);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contenedor;
    }

    public Usuarios buscarEmpleado(String nombre) {
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        Usuarios usuario = null;
        try {
            ResultSet rs = conexion.executeQuery("SELECT empleados.*, departamentos.nombre_departamento, usuarios.*" +
                    "FROM empleados " +
                    "JOIN usuarios ON empleados.id_empleado = usuarios.id_empleado " +
                    "JOIN departamentos ON empleados.id_departamento = departamentos.id_departamento " +
                    "WHERE empleados.nombre='" + nombre + "';");
            if (rs.next()) {
                usuario = new Usuarios(
                        rs.getLong("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getString("nombre_departamento"),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico"),
                        rs.getString("rol"),
                        rs.getString("genero"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getLong("id_empleado")

                );
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return usuario;
    }
    public Pacientes buscarPaciente(String nombre){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        Pacientes pacientes = null;
        try {
            ResultSet rs = conexion.executeQuery("SELECT * FROM pacientes WHERE nombre='"+nombre+"';");
            if (rs.next()) {
                pacientes = new Pacientes(
                        rs.getLong("id_paciente"),
                        rs.getString("nombre"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getString("direccion"),
                        rs.getString("genero"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico"),
                        rs.getDate("fecha_nacimiento")
                );
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return pacientes;
    }
}
