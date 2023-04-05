package com.example.syshealthfx;


import com.example.syshealthfx.admincontrollers.Usuarios;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ResourceBundle;


public class UsersController implements Initializable {

    @FXML
    private AnchorPane frameAdmin;
    @FXML
    private Button usuario;
    @FXML
    private HBox contenidoHBox, buttonModificar;
    @FXML
    private VBox contenidoInicio, contenidoUsuarios, buttonsMenu;

    private SQLClass conexion;
    private Usuarios usuarios;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuario.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                removerElementos();
                System.out.println(actionEvent.getSource());

            }
        });


    }
    public void removerElementos(){
        HBox hbox = (HBox) frameAdmin.lookup("#contenidoHBox");
        VBox vbox = (VBox) hbox.lookup("#contenidoInicio");
        hbox.getChildren().removeAll(vbox);
        usuariosFrame();
    }
    public void usuariosFrame(){
        if (contenidoUsuarios == null){
            contenidoUsuarios = new VBox();
            contenidoUsuarios.setId("contenidoUsuarios");
            contenidoUsuarios.getStyleClass().add("contenido-usuarios");

            HBox hboxHeader = new HBox();
            HBox hboxContainerButtons = new HBox();
            hboxContainerButtons.setAlignment(Pos.TOP_LEFT);
            HBox hboxButton = new HBox();
            hboxContainerButtons.getChildren().add(hboxButton);

            hboxContainerButtons.setStyle("-fx-padding: 20px 0px 10px 0px");
            String imagepath = getClass().getResource("assets/editar.png").toExternalForm();
            Image image = new Image(imagepath);
            ImageView imagev = new ImageView(image);
            imagev.setFitHeight(62);
            imagev.setFitWidth(81);
            Label labelModificar = new Label("AGREGAR USUARIO");
            hboxButton.getStyleClass().add("botones-varios");
            hboxButton.setId("btnModificar");

            hboxButton.getChildren().addAll(imagev, labelModificar);

            hboxHeader.getStyleClass().add("header");
            Label labelTitulo = new Label();
            labelTitulo.setText("USUARIOS");
            hboxHeader.getChildren().add(labelTitulo);
            contenidoHBox.getChildren().add(contenidoUsuarios);
            contenidoUsuarios.getChildren().add(hboxHeader);
            contenidoUsuarios.getChildren().add(hboxContainerButtons);

           try{
               conexion = new SQLClass("root", "", "sys_health");
               conexion.connect();
               ResultSet rs = conexion.executeQuery("SELECT * FROM usuarios");
               Label col1 = new Label();
               col1.setText("ID");
               col1.getStyleClass().add("label-column");
               Label col2 = new Label();
               col2.setText("USUARIO");
               col2.getStyleClass().add("label-column");
               Label col3 = new Label();
               col3.setText("CONTRASEÑA");
               col3.getStyleClass().add("pass");
               Label col4 = new Label();
               col4.getStyleClass().add("label-column");

               GridPane tabla = new GridPane();
               tabla.add(col1, 0, 0);
               tabla.add(col2, 1, 0);
               tabla.add(col3, 2, 0);
               tabla.add(col4, 3, 0);
               tabla.getStyleClass().add("tablas");

               int filaActual = 1;
               while(rs.next()){
                   long id = rs.getLong("id_usuario");
                   String user = rs.getString("usuario");
                   String password = rs.getString("pass");

                   Label dato1 = new Label();
                   dato1.getStyleClass().add("label-data");
                   Label dato2 = new Label();
                   dato2.getStyleClass().add("label-data");
                   Label dato3 = new Label();
                   dato3.getStyleClass().add("pass-data");
                   HBox buttons = new HBox();
                   buttons.setAlignment(Pos.CENTER);
                   buttons.getStyleClass().add("buttons-data");

                   Button btnModificar = new Button("MODIFICAR");
                   btnModificar.getStyleClass().add("modificar");
                   Button btnEliminar = new Button("ELIMINAR");
                   btnEliminar.getStyleClass().add("eliminar");
                   buttons.getChildren().addAll(btnModificar, btnEliminar);
                   dato1.setText(String.valueOf(id));
                   dato2.setText(user);
                   dato3.setText(password);

                   tabla.add(dato1, 0, filaActual);
                   tabla.add(dato2, 1, filaActual);
                   tabla.add(dato3, 2, filaActual);
                   tabla.add(buttons, 3, filaActual);

                   filaActual++;

               }
               conexion.disconnect();
               contenidoUsuarios.getChildren().add(tabla);
               hboxButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                   @Override
                   public void handle(MouseEvent mouseEvent) {
                       FXMLLoader loader = new FXMLLoader(getClass().getResource("registro-usuario.fxml"));
                       try {
                           Parent root = loader.load();
                           Scene scene = new Scene(root);
                           Stage stage = new Stage();
                           stage.setScene(scene);
                           stage.show();
                       } catch (IOException e) {
                           System.out.println("Error al cargar el archivo fxml: " + e.getMessage());
                       }
                   }
               });
           } catch (SQLException e){
               throw new RuntimeException(e);
           }
        }
    }
}





