package com.example.syshealthfx.admincontrollers;

import com.example.syshealthfx.SQLClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TablaUsuarios{


    public VBox mostrarTabla(){
        VBox vBox = new VBox();
        vBox.setId("tablaEmpleados");
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        TableView<Usuarios> table = new TableView<>();
        ObservableList<Usuarios> listaUsuarios = FXCollections.observableArrayList();
        try{
            ResultSet rs = conexion.executeQuery("SELECT u.id_usuario, e.nombre, e.apellido_paterno, e.apellido_materno, d.nombre_departamento, u.usuario\n" +
                    "FROM usuarios u\n" +
                    "JOIN empleados e ON u.id_empleado = e.id_empleado\n" +
                    "JOIN departamentos d ON e.id_departamento = d.id_departamento\n");
            while(rs.next()){
                Usuarios usuario = new Usuarios(rs.getLong("id_usuario"), rs.getString("nombre"), rs.getString("apellido_paterno"), rs.getString("apellido_materno"), rs.getString("nombre_departamento"), rs.getString("usuario"));
                listaUsuarios.add(usuario);
            }
            // Agregar las columnas necesarias a la TableView
            TableColumn<Usuarios, Integer> idColumn = new TableColumn<>("ID Usuario");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));

            TableColumn<Usuarios, String> nombreColumn = new TableColumn<>("Nombre");
            nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

            TableColumn<Usuarios, String> apellidoPaternoColumn = new TableColumn<>("Apellido Paterno");
            apellidoPaternoColumn.setCellValueFactory(new PropertyValueFactory<>("apellidoP"));

            TableColumn<Usuarios, String> apellidoMaternoColumn = new TableColumn<>("Apellido Materno");
            apellidoMaternoColumn.setCellValueFactory(new PropertyValueFactory<>("apellidoM"));

            TableColumn<Usuarios, String> departamentoColumn = new TableColumn<>("Departamento");
            departamentoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreDepartamento"));

            TableColumn<Usuarios, String> usuarioColumn = new TableColumn<>("Usuario");
            usuarioColumn.setCellValueFactory(new PropertyValueFactory<>("usuario"));

            // Asignar la ObservableList con los objetos a la TableView
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            table.setItems(listaUsuarios);


            // Agregar las columnas a la TableView
            table.getColumns().addAll(idColumn, nombreColumn, apellidoPaternoColumn, apellidoMaternoColumn, departamentoColumn, usuarioColumn);
            vBox.getChildren().add(table);
            conexion.disconnect();
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        return vBox;
    }

}
