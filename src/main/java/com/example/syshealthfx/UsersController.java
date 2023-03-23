package com.example.syshealthfx;

import com.example.syshealthfx.SQLClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


public class UsersController implements Initializable {
    @FXML
    private TableView tablaUsuarios;
    @FXML
    private AnchorPane frameAdmin;
    @FXML
    private Button usuario;
    @FXML
    private VBox contenidoInicio;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuario.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                removerElementos();
                System.out.println("HOLA!");
            }
        });
    }
    public void removerElementos(){
        VBox vbox = (VBox) frameAdmin.lookup("#contenidoInicio");
        frameAdmin.getChildren().remove(vbox);

    }
}





