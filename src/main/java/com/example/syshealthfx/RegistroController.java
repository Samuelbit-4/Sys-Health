package com.example.syshealthfx;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegistroController implements Initializable {
    @FXML
    private ComboBox cmbDepartamentos;
    @FXML
    private Button generarUsuario;
    @FXML
    private Button generarPassword;
    @FXML
    private Button vaciar;
    @FXML
    private AnchorPane frame;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidoA;
    @FXML
    private TextField txtApellidoP;
    @FXML
    private TextField txtDomicilio;
    @FXML
    private DatePicker date;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtCedula;
    @FXML
    private ToggleGroup genero;
    @FXML
    private ToggleGroup rol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        genero = new ToggleGroup();
        RadioButton rb1 = new RadioButton("MASCULINO");
        rb1.setSelected(true);
        RadioButton rb2 = new RadioButton("FEMENINO");
        rb1.setToggleGroup(genero);
        rb2.setToggleGroup(genero);
        HBox rbtnGenero = (HBox) frame.lookup("#rbtnGenero");
        rbtnGenero.getChildren().addAll(rb1, rb2);

        rol = new ToggleGroup();
        RadioButton rol1 = new RadioButton("Medico");
        RadioButton rol2 = new RadioButton("Administrador");
        RadioButton rol3 = new RadioButton("Recepcionista");

        rol1.setToggleGroup(rol);
        rol2.setToggleGroup(rol);
        rol3.setToggleGroup(rol);
        rol1.setSelected(true);

        HBox rbtnRol = (HBox) frame.lookup("#rbtnRol");
        VBox vboxDepartamento = (VBox) frame.lookup("#vboxDepartamento");
        SQLClass con = new SQLClass("root", "", "sys_health_prueba");
        ObservableList<String> departamentos = FXCollections.observableArrayList();
        con.connect();
        try{
            ResultSet set = con.executeQuery("SELECT nombre_departamento FROM departamentos");
            while (set.next()){
                String nombreDepartamento = set.getString("nombre_departamento");
                departamentos.add(nombreDepartamento);
            }
            ComboBox<String> cmbDepartamentos = new ComboBox<>(departamentos);
            cmbDepartamentos.promptTextProperty().set("-SELECCIONA");
            cmbDepartamentos.setPrefWidth(211);
            cmbDepartamentos.setId("cmbDepartamentos");
            vboxDepartamento.getChildren().add(cmbDepartamentos);

        } catch (SQLException e){
            e.printStackTrace();
        }
        rbtnRol.setStyle("-fx-alignment: center");
        rbtnRol.setSpacing(5);
        rbtnRol.getChildren().addAll(rol2, rol3, rol1);

        rol.selectedToggleProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue == rol1){
                txtCedula.setDisable(false);
            } else{
                txtCedula.setDisable(true);
            }
        });

       txtUsuario.textProperty().addListener((observable, oldValue, newValue) ->{
           if(!newValue.trim().isEmpty()){
               generarUsuario.setDisable(true);
           } else{
               generarUsuario.setDisable(false);
           }
       });
       txtPassword.textProperty().addListener((observable, oldValue, newValue) ->{
            if(!newValue.trim().isEmpty()){
                generarPassword.setDisable(true);
            } else{
                generarPassword.setDisable(false);
            }
        });

    }
    public void vaciarFormulario(){
        frame.getChildren().forEach((item) ->{
            item.lookupAll("TextField").forEach((items) ->{
                TextField textField = (TextField) items;
                if (textField.getText() != "") {
                    textField.setText("");
                }
            });
        });
    }
    public void cerrarVentana(){
        Alert cerrar = new Alert(Alert.AlertType.CONFIRMATION);
        cerrar.setTitle("CONFIRMACIÓN");
        cerrar.setHeaderText("¿DESEA CANCELAR LA CAPTURA?");
        cerrar.setContentText("Se perderan todos los datos...");
        cerrar.showAndWait();
        ButtonType result = cerrar.getResult();
        if(result == ButtonType.OK){
            frame.getScene().getWindow().hide();
        }
    }
    public void generarUsuario(){
        if(txtNombre.getText() != "" && txtApellidoA.getText() != "" && txtApellidoP.getText() != ""){
           String val1 = txtNombre.getText().trim().substring(0,3);
           String val2 = txtApellidoA.getText().trim().substring(0,2).toUpperCase();
            char[] symbols = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'};
            String val3 = txtApellidoP.getText().trim().substring(0,1).toLowerCase();
           int val4 = (int) ((Math.random() / 1000) * 1000);
           Random rand = new Random();
           int numeroA = rand.nextInt(12);
            System.out.println(val1 + " " + val2 + " " + val3 + " " + val4 + " " + symbols[numeroA]);
            txtUsuario.setText(val1+val2+symbols[numeroA]+val3+val4);
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("UPS!");
            alert.setHeaderText("ERROR");
            alert.setContentText("Error: No es posible generar el usuario, revisa que " +
                    "\nlos datos estén completos");
            alert.showAndWait();
        }
    }
    public void generarPass(){
        char[] letras = {'a', 'b', 'c', 'd', 'e'};
        char[] symbols = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'};
        Random rand = new Random();
        String acumulador;
        String pass = "";
        int contador = 0;
        while(contador < 4){
            int numeroSimbolos = rand.nextInt(12);
            int numeroLetras = rand.nextInt(5);
            acumulador = String.valueOf(letras[numeroLetras])+ String.valueOf(symbols[numeroSimbolos]);
            pass += acumulador;
            contador++;
        }
        txtPassword.setText(pass);
    }
    public void subirUsuario(){
        Random ran = new Random();
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        String query1 = "INSERT INTO empleados(id_empleado, id_departamento, nombre, " +
                "apellido_paterno, apellido_materno, genero, direccion, telefono, correo_electronico, fecha_nacimiento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO medicos(especialidad, cedula_profesional, id_empleado) " +
                "VALUES (?, ?, ?)";
        String query3 = "INSERT INTO usuarios(id_usuario, usuario, contrasena, rol, id_empleado) "+
                "VALUES (?, ?, SHA2(?,256), ?, ?)";
        ComboBox<String> combo = (ComboBox<String>) frame.lookup("#cmbDepartamentos");
        String seleccionado = combo.getValue();
        System.out.println(seleccionado);
        RadioButton rbSelectedRol = (RadioButton) rol.getSelectedToggle();
        String rolSeleccionado = rbSelectedRol.getText();
        String nombre = txtNombre.getText();
        String apellidoP = txtApellidoP.getText();
        String apellidoM = txtApellidoA.getText();
        RadioButton rbSelectedGenero = (RadioButton) genero.getSelectedToggle();
        String selectedGenero = rbSelectedGenero.getText();
        String direccion = txtDomicilio.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        String nacimiento = String.valueOf(date.getValue());
        String usuario = txtUsuario.getText();
        String contra = txtPassword.getText();
        System.out.println(nacimiento);


        try {
            PreparedStatement statement = conexion.preparedStatement(query1);
            int numero = (int) (Math.random() * 10000) + 1;
            int numero2 = (int) (Math.random() * 1000) + 1;
            statement.setInt(1, numero);
            ResultSet set = conexion.executeQuery("SELECT id_departamento FROM departamentos WHERE nombre_departamento='"+seleccionado+"'");
            if (set.next()){
                int idDepartamento = set.getInt("id_departamento");
                statement.setInt(2, idDepartamento);
            }
            statement.setString(3, nombre);
            statement.setString(4, apellidoP);
            statement.setString(5,apellidoM);
            statement.setString(6, selectedGenero);
            statement.setString(7, direccion);
            statement.setString(8, telefono);
            statement.setString(9, correo);
            statement.setDate(10, Date.valueOf(nacimiento));
            int state1 = 0;
            int state2 = 0;
            int state3 = 0;
            switch (rolSeleccionado){
                case "Medico" -> {
                    state1 = statement.executeUpdate();
                    String cedula = txtCedula.getText();
                 PreparedStatement statementM = conexion.preparedStatement(query2);
                 statementM.setString(1, seleccionado);
                 statementM.setString(2, cedula);
                 statementM.setInt(3, numero);
                 state2 = statementM.executeUpdate();
                }
                case "Recepcionista", "Administrador" ->{
                    state1 = statement.executeUpdate();
                }
            }
            PreparedStatement statementU = conexion.preparedStatement(query3);
            statementU.setInt(1, numero2);
            statementU.setString(2, usuario);
            statementU.setString(3, contra);
            statementU.setString(4, rolSeleccionado);
            statementU.setInt(5, numero);
            state3 = statementU.executeUpdate();
            boolean state1and2 = state1 == 1 && state3 == 1;
            boolean stateM = state2 == 1;
            if(state1and2 || stateM){
                Alert alertSi = new Alert(Alert.AlertType.INFORMATION);
                alertSi.setHeaderText("CAPTURA EXITOSA");
                alertSi.setTitle("EXITO");
                alertSi.setContentText("LOS DATOS SE HAN GUARDADO CORRECTAMENTE");
                ButtonType buttonPDF = new ButtonType("MOSTRAR PDF");
                ButtonType buttonRegresar = new ButtonType("REGRESAR");
                alertSi.getButtonTypes().setAll(buttonPDF, buttonRegresar);
                Optional<ButtonType> result = alertSi.showAndWait();

                try{
                    // Creamos el objeto PdfWriter para escribir el PDF
                    PdfWriter writer = new PdfWriter("carta_usuario_"+numero+".pdf");

                    // Creamos el objeto PdfDocument que representa el PDF
                    PdfDocument pdfDoc = new PdfDocument(writer);

                    // Configuramos el tamaño de la página
                    PageSize pageSize = PageSize.A4;

                    // Creamos el objeto Document que contendrá nuestro contenido
                    Document document = new Document(pdfDoc, pageSize);

                    // Creamos un objeto Text con el título centrado
                    Text titulo = new Text("SYS-HEALTH\n").setFontSize(24).setBold().setTextAlignment(TextAlignment.CENTER);

                    // Agregamos el título al documento
                    document.add(new Paragraph(titulo));

                    // Creamos un objeto Text con el segundo título centrado y con color rojo
                    Text subtitulo = new Text("REPORTE DE CREACIÓN DE USUARIO\n").setFontSize(12).setBold().setItalic().setFontColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER);

                    // Agregamos el segundo título al documento
                    document.add(new Paragraph(subtitulo));
                    String nombreCompleto = nombre + " " + apellidoP + " " + apellidoM;

                    LocalDate fechaActual = LocalDate.now();
                    // Creamos un objeto Text con el contenido de la carta
                    Text contenido = new Text("Estimado " + nombreCompleto + ",\n\nLe informamos que ya ha sido registrado en nuestro sistema el día " + fechaActual + ".\n\n").setFontSize(12);

                    Text contenido2 = new Text("" +
                            "Nos ponemos en contacto con usted para informarle que se ha creado exitosamente una cuenta de usuario para usted en nuestro sistema. Le damos la bienvenida y esperamos que disfrute de todas las funcionalidades que " +
                            "nuestro sistema tiene para ofrecerle.\n\n").setFontSize(12);

                    Text contenido3 = new Text(""+
                            "Su nombre de usuario y contraseña son los siguientes:\n" +
                            "\n" + "Nombre de usuario: "+ usuario+"\n"+
                            "Contraseña: "+ contra +"\n\n").setFontSize(12);
                    Text contenido4 = new Text(""+
                            "Le recomendamos encarecidamente que mantenga esta información segura y confidencial para garantizar la seguridad de su cuenta.\n" +
                            "\n" +
                            "Si tiene alguna pregunta o necesita ayuda con su cuenta, no dude en ponerse en contacto con nuestro equipo de soporte técnico. Estamos aquí para ayudarle en todo lo que necesite.")
                            .setFontSize(12);
                    Text contenido5 = new Text(""+
                            "Atentamente,\n" +
                            "\n" +
                            SesionUsuario.nombreCompleto()).setFontSize(12);
                    // Agregamos el contenido al documento
                    document.add(new Paragraph(contenido));
                    document.add(new Paragraph(contenido2));
                    document.add(new Paragraph(contenido3));
                    document.add(new Paragraph(contenido4));
                    document.add(new Paragraph(contenido5));

                    // Cerramos el documento
                    document.close();

                } catch (FileNotFoundException e){

                }


                if (result.isPresent() && result.get() == buttonPDF){
                    System.out.println("si");
                } else{
                    System.out.println("no");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
