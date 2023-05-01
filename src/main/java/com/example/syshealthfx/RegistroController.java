package com.example.syshealthfx;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class RegistroController {
    @FXML
    private ToggleGroup genero;
    @FXML
    private ToggleGroup rol;
    @FXML
    private Button btnVaciar;
    @FXML
    private Button crearUsuario;
    @FXML
    private Button cancelar;
    private VBox frame;
    private TextField txtUsuario, txtPassword, txtCedula;
    private TextField txtNombre, txtApellidoP, txtApellidoM, txtDomicilio;
    private TextField txtTelefono, txtCorreo;

    private DatePicker date;
    private Button generarUsuario, generarPassword;


    public RegistroController(VBox frame){
        this.frame = frame;
        initialize();
    }
    public RegistroController(){
        System.out.println("1");
    }
    public VBox getV(){
        return this.frame;
    }
    public void initialize(){

        txtUsuario = (TextField) frame.lookup("#txtNombre");
        txtPassword = (TextField) frame.lookup("#txtPassword");
        txtCedula = (TextField) frame.lookup("#txtCedula");
        generarUsuario = (Button) frame.lookup("#generarUsuario");
        generarPassword = (Button) frame.lookup("#generarPassword");
        txtNombre = (TextField) frame.lookup("#txtNombre");
        txtApellidoP = (TextField) frame.lookup("#txtApellidoP");
        txtApellidoM = (TextField) frame.lookup("#txtApellidoM");
        txtDomicilio = (TextField) frame.lookup("#txtDomicilio");
        txtTelefono = (TextField) frame.lookup("#txtTelefono");
        txtCorreo = (TextField) frame.lookup("#txtCorreo");
        btnVaciar = (Button) frame.lookup("#btnVaciar");
        cancelar = (Button) frame.lookup("#cancelar");
        date = (DatePicker) frame.lookup("#txtDate");
        btnVaciar.setOnAction((e) ->{
            vaciarFormulario();
        });
        crearUsuario = (Button) frame.lookup("#crearUsuario");
        crearUsuario.setOnAction((e) ->{
            subirUsuario();
            vaciarFormulario();
        });
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
           if(newValue.trim().isEmpty()){
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

    // EN DESARROLLO
    public void generarUsuario(){
        if(txtNombre.getText() != "" && txtApellidoM.getText() != "" && txtApellidoP.getText() != ""){
           String val1 = txtNombre.getText().trim().substring(0,3);
           String val2 = txtApellidoM.getText().trim().substring(0,2).toUpperCase();
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
    //EN DESARROLLO
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
                "VALUES (UUID_SHORT(), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO medicos(especialidad, cedula_profesional, id_empleado) " +
                "VALUES (?, ?, ?)";
        String query3 = "INSERT INTO usuarios(id_usuario, usuario, contrasena, rol, id_empleado) "+
                "VALUES (UUID_SHORT(), ?, SHA2(?,256), ?, ?)";
        ComboBox<String> combo = (ComboBox<String>) frame.lookup("#cmbDepartamentos");
        String seleccionado = combo.getValue();
        System.out.println(seleccionado);
        RadioButton rbSelectedRol = (RadioButton) rol.getSelectedToggle();
        String rolSeleccionado = rbSelectedRol.getText();
        String nombre = txtNombre.getText();
        String apellidoP = txtApellidoP.getText();
        String apellidoM = txtApellidoM.getText();
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
           // statement.setInt(1, numero);
            ResultSet set = conexion.executeQuery("SELECT id_departamento FROM departamentos WHERE nombre_departamento='"+seleccionado+"'");
            if (set.next()){
                int idDepartamento = set.getInt("id_departamento");
                statement.setInt(1, idDepartamento);
            }

            statement.setString(2, nombre);
            statement.setString(3, apellidoP);
            statement.setString(4,apellidoM);
            statement.setString(5, selectedGenero);
            statement.setString(6, direccion);
            statement.setString(7, telefono);
            statement.setString(8, correo);
            statement.setDate(9, Date.valueOf(nacimiento));
            int state1 = 0;
            int state2 = 0;
            int state3 = 0;
            switch (rolSeleccionado){
                case "Medico" -> {
                    state1 = statement.executeUpdate();
                    String cedula = txtCedula.getText();
                    ResultSet idEmpleado = conexion.executeQuery("SELECT id_empleado FROM empleados WHERE nombre='"+nombre+"';");
                    int idEmp = idEmpleado.getInt("id_empleado");
                 PreparedStatement statementM = conexion.preparedStatement(query2);
                 statementM.setString(1, seleccionado);
                 statementM.setString(2, cedula);
                 statementM.setInt(3, idEmp);
                 state2 = statementM.executeUpdate();
                }
                case "Recepcionista", "Administrador" ->{
                    state1 = statement.executeUpdate();
                }
            }
            PreparedStatement statementU = conexion.preparedStatement(query3);
            ResultSet idEmpleado = conexion.executeQuery("SELECT id_empleado FROM empleados WHERE nombre='"+nombre+"';");
            long idEmp = 0;
            if(idEmpleado.next()){
                idEmp = idEmpleado.getLong("id_empleado");
            }
            //statementU.setInt(1, numero2);
            statementU.setString(1, usuario);
            statementU.setString(2, contra);
            statementU.setString(3, rolSeleccionado);
            statementU.setLong(4, idEmp);
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
                    PdfWriter writer = new PdfWriter("carta_usuario_"+idEmp+".pdf");

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
                            "\n"
                            ).setFontSize(12);
                    // Agregamos el contenido al documento
                    document.add(new Paragraph(contenido));
                    document.add(new Paragraph(contenido2));
                    document.add(new Paragraph(contenido3));
                    document.add(new Paragraph(contenido4));
                    document.add(new Paragraph(contenido5));

                    // Cerramos el documento
                    document.close();

                } catch (FileNotFoundException e){
                    System.out.println("HOLA");
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
    public void crearDepartamento(String nombre, String descripcion, VBox vn){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();

        try {
            ResultSet rs = conexion.executeQuery("SELECT nombre, apellido_paterno, apellido_materno FROM empleados");
            VBox containerEmpleados = (VBox) vn.lookup("#listaEmpleados");
            String nombreCompleto = "";
            ComboBox<String> listaEmpleados = new ComboBox<>();
            while (rs.next()){
                nombreCompleto =
                        rs.getString("nombre")
                                + " "
                        +rs.getString("apellido_paterno")
                                + " "
                        +rs.getString("apellido_materno");
                listaEmpleados.getItems().add(nombreCompleto);
            }
            conexion.disconnect();
            containerEmpleados.getChildren().add(listaEmpleados);
            TextField nombreDepartamento = (TextField) vn.lookup("#txtNombreDepartamento");
            TextArea descripcionDepartamento = (TextArea) vn.lookup("#txtDescripcionDepartamento");

            Button registrarDepartamento = (Button) vn.lookup("#btnRegistrarDepartamento");

            registrarDepartamento.setOnAction((register) ->{
                conexion.connect();
                String query = "INSERT INTO departamentos(nombre_departamento, descripcion_departamento) " +
                        "VALUES (?, ?)";
                try{
                    PreparedStatement ps = conexion.preparedStatement(query);
                    ps.setString(1, nombreDepartamento.getText());
                    ps.setString(2, descripcionDepartamento.getText());
                    ps.executeUpdate();
                    System.out.println("YA");
                    conexion.disconnect();
                } catch (SQLException e){
                    throw new RuntimeException(e);
                }

            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void generarReporte(String ruta){

        PdfWriter writer = null;
        try {
            writer = new PdfWriter(ruta+".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            PageSize pageSize = PageSize.A4;
            Document document = new Document(pdfDoc, pageSize);
            Text titulo = new Text("SYS-HEALTH\n").setFontSize(24).setBold();
            Paragraph parrafoTitulo = new Paragraph().add(titulo).setTextAlignment(TextAlignment.CENTER);
            Text subtitle = new Text("REPORTE GENERAL DE DEPARTAMENTOS").setFontSize(16).setFontColor(ColorConstants.LIGHT_GRAY);
            Paragraph parrafoSubtitulo = new Paragraph().add(subtitle).setTextAlignment(TextAlignment.CENTER);

            document.add(parrafoTitulo);
            document.add(parrafoSubtitulo);

            Paragraph contenido1 = new Paragraph("A quien corresponda\n").setTextAlignment(TextAlignment.JUSTIFIED);

            Paragraph parrafo1 = new Paragraph().add("" +
                    "Hemos creado un archivo PDF que contiene una lista de todos los departamentos registrados en nuestra empresa, así como una lista de todos los empleados que se encuentran actualmente en cada departamento. Este archivo PDF es una herramienta muy útil que le permitirá tener una visión general de la estructura de " +
                    "nuestra organización y de los empleados que trabajan en ella.\n").setTextAlignment(TextAlignment.JUSTIFIED);

            Paragraph parrafo2 = new Paragraph().add("" +
                    "A continuación, se presentan dos tablas que conforman la siguiente información\n");

            List contenido2 = new List();
            ListItem item1 = new ListItem("Departamentos registrados");
            ListItem item2 = new ListItem("Empleados registrados");
            ListItem item3 = new ListItem("Numero total de usuarios registrados");

            contenido2.add(item1);
            contenido2.add(item2);
            contenido2.add(item3);

            document.add(contenido1);
            document.add(parrafo1);
            document.add(parrafo2);
            document.add(contenido2);
            

            Text sub1 = new Text("LISTADO DE DEPARTAMENTOS").setBold().setFontSize(20);
            document.add(new Paragraph().add(sub1).setTextAlignment(TextAlignment.CENTER));

            Table tabla = new Table(new float[]{1, 3, 3});
            tabla.setWidth(100);
            tabla.addCell(new Paragraph().add("ID DEPARTAMENTO").setTextAlignment(TextAlignment.CENTER));
            tabla.addCell(new Paragraph().add("NOMBRE DEPARTAMENTO").setTextAlignment(TextAlignment.CENTER));
            tabla.addCell(new Paragraph().add("DESCRIPCION").setTextAlignment(TextAlignment.CENTER));
            SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");

            try{
                conexion.connect();
                ResultSet rs = conexion.executeQuery("SELECT * FROM departamentos");
                while(rs.next()){
                    tabla.addCell(new Paragraph().add(String.valueOf(rs.getInt("id_departamento"))).setTextAlignment(TextAlignment.CENTER));
                    tabla.addCell(new Paragraph().add(rs.getString("nombre_departamento")).setTextAlignment(TextAlignment.CENTER));
                    tabla.addCell(new Paragraph().add(rs.getString("descripcion_departamento")).setTextAlignment(TextAlignment.CENTER));
                }
                conexion.disconnect();
            } catch (SQLException e){
                e.printStackTrace();
            }
            document.add(tabla);
            document.add(new AreaBreak());

            Text sub2= new Text("EMPLEADOS REGISTRADOS").setBold().setFontSize(20);
            document.add(new Paragraph().add(sub2).setTextAlignment(TextAlignment.CENTER));
            Table tabla2 = new Table(4);
            tabla2.addCell(new Paragraph().add("ID EMPLEADO").setTextAlignment(TextAlignment.CENTER));
            tabla2.addCell(new Paragraph().add("ID DEPARTAMENTO").setTextAlignment(TextAlignment.CENTER));
            tabla2.addCell(new Paragraph().add("NOMBRE").setTextAlignment(TextAlignment.CENTER));
            tabla2.addCell(new Paragraph().add("CORREO").setTextAlignment(TextAlignment.CENTER));

            conexion.connect();
            try{
                ResultSet rs = conexion.executeQuery("SELECT id_empleado, id_departamento, nombre, apellido_paterno, apellido_materno, correo_electronico" +
                        " FROM empleados");
                while(rs.next()){
                    tabla2.addCell(new Paragraph().add(String.valueOf(rs.getLong("id_empleado"))).setTextAlignment(TextAlignment.CENTER));
                    tabla2.addCell(new Paragraph().add(String.valueOf(rs.getInt("id_departamento"))).setTextAlignment(TextAlignment.CENTER));

                    tabla2.addCell(new Paragraph().add(rs.getString("nombre") + " " + rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno"))
                            .setTextAlignment(TextAlignment.CENTER));
                    tabla2.addCell(new Paragraph().add(String.valueOf(rs.getString("correo_electronico"))).setTextAlignment(TextAlignment.CENTER));

                }
                document.add(tabla2);
                document.add(new AreaBreak());
                conexion.disconnect();
            } catch (SQLException e){
                e.printStackTrace();
            }
            Text sub3 = new Text("CANTIDAD DE USUARIOS REGISTRADOSs").setBold().setFontSize(20);
            document.add(new Paragraph().add(sub3).setTextAlignment(TextAlignment.CENTER));
            conexion.connect();
            int contadorM = 0;
            int contadorA = 0;
            int contadorR = 0;
            int contadorU = 0;
            try{
                ResultSet rs = conexion.executeQuery("SELECT rol FROM usuarios");
                while(rs.next()){
                    switch (rs.getString("rol")){
                        case "Medico" ->{
                            contadorM++;
                        }
                        case "Administrador" ->{
                            contadorA++;
                        }
                        case "Recepcionista"->{
                            contadorR++;
                        }
                    }
                    contadorU++;
                }
            } catch (SQLException e){
                e.printStackTrace();
            }

           // document.add(tabla2);
            Text info1 = new Text("\nLa cantidad de usuarios registrados en el sistema son:\n");
            document.add(new Paragraph().add(info1).setTextAlignment(TextAlignment.JUSTIFIED));
            List cantidades = new List();
            ListItem cantidadMedicos = new ListItem("MEDICOS: " + String.valueOf(contadorM));
            ListItem cantidadAdmins = new ListItem("ADMINISTRADORES: " + String.valueOf(contadorA));
            ListItem cantidadRecepcion = new ListItem("RECEPCIONISTAS: " + String.valueOf(contadorR));

            cantidades.add(cantidadMedicos);
            cantidades.add(cantidadAdmins);
            cantidades.add(cantidadRecepcion);

            document.add(cantidades);

            Text info2 = new Text("\nDando un total de usuarios de : "+ contadorU);
            document.add(new Paragraph().add(info2).setTextAlignment(TextAlignment.JUSTIFIED));

            Text info3 = new Text("\nSin más por el momento, atentamente: \n");
            document.add(new Paragraph().add(info3).setTextAlignment(TextAlignment.JUSTIFIED));
            document.close();
            System.out.println("REPORTE");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }



    }
}
