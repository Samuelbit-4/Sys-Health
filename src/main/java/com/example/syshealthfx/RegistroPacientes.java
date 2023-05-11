package com.example.syshealthfx;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.xml.transform.Result;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RegistroPacientes {

    private VBox vn;

    private TextField txtNombre;

    private TextField txtApellidoP;

    private TextField txtApellidoM;

    private TextField txtDireccion;

    private TextField txtTelefono;

    private TextField txtFecha;

    private TextField txtCorreo;

    private Button btnRegistrar;

    private Button btnCancelar;


    public VBox getPacientes(){
        return this.vn;

    }


    public void subirDatos(String nombre, String apellidoP, String apellidoM, String direccion, String telefono, String correoElectronico, String fechaNacimiento, String genero ){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();

        String query = "INSERT INTO pacientes(id_paciente, nombre, apellido_paterno, apellido_materno, genero, direccion, telefono, correo_electronico, fecha_nacimiento)" +
                " VALUES(UUID_SHORT(), ?, ?, ?, ?, ?, ?, ?, ?)";



        try{
           PreparedStatement ps = conexion.preparedStatement(query);
           ps.setString(1, nombre);
           ps.setString(2, apellidoP);
           ps.setString(3, apellidoM);
           ps.setString(4, genero);
           ps.setString(5, direccion);
           ps.setString(6, telefono);
           ps.setString(7, correoElectronico);
           ps.setDate(8, Date.valueOf(fechaNacimiento));
           int state = ps.executeUpdate();
           if(state == 1){
               Alert alertSi = new Alert(Alert.AlertType.INFORMATION);
               alertSi.setHeaderText("CAPTURA EXITOSA");
               alertSi.setTitle("EXITO");
               alertSi.setContentText("LOS DATOS SE HAN GUARDADO CORRECTAMENTE");
               alertSi.showAndWait();
               generarReporte(nombre, apellidoP, apellidoM, direccion, telefono, correoElectronico, fechaNacimiento);
           }
           conexion.disconnect();


        } catch (SQLException e){
            e.printStackTrace();
        }

    }
    public void generarReporte(String nombre, String apellidoP, String apellidoM, String direccion, String telefono, String correoElectronico, String fechaNacimiento ){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        long idPaciente = 0;
        try {
            ResultSet rs = conexion.executeQuery("SELECT id_paciente FROM pacientes WHERE nombre='"+nombre+"' AND telefono='"+telefono+"';");

            while(rs.next()){
               idPaciente = rs.getLong("id_paciente");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PdfWriter writer = null;
        try {
            writer = new PdfWriter("bienvenida-paciente-"+idPaciente+".pdf");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PdfDocument pdfDocument = new PdfDocument(writer);
        PageSize pageSize = PageSize.A4;

        Document document = new Document(pdfDocument, pageSize);
        // Creamos un objeto Text con el título centrado
        Text titulo = new Text("SYS-HEALTH\n").setFontSize(24).setBold();

        // Agregamos el título al documento
        document.add(new Paragraph(titulo));

        // Creamos un objeto Text con el segundo título centrado y con color rojo
        Text subtitulo = new Text("REPORTE DE CREACIÓN DE PACIENTE\nCLAVE DE PACIENTE: "+idPaciente).setFontSize(12).setBold().setItalic().setFontColor(ColorConstants.LIGHT_GRAY);

        // Agregamos el segundo título al documento
        document.add(new Paragraph(subtitulo));
        String nombreCompleto = nombre + " " + apellidoP + " " + apellidoM;

        LocalDate fechaActual = LocalDate.now();
        // Creamos un objeto Text con el contenido de la carta
        Text contenido = new Text("Estimado "+nombreCompleto+

                " Queremos darle la más cordial bienvenida a nuestro sistema de salud [Nombre del Sistema de Salud]. Nos complace contar con usted como nuevo paciente, y nos comprometemos a brindarle la atención médica de la más alta calidad.\n" +
                        "\n" +
                        "Nuestro equipo de profesionales de la salud está altamente capacitado para proporcionarle un servicio personalizado y adaptado a sus necesidades. Estamos comprometidos en brindarle un ambiente acogedor y seguro, y en ofrecerle un trato respetuoso y amable."
        ).setFontSize(12);

        Text contenido2 = new Text(
                "En [Nombre del Sistema de Salud], estamos comprometidos con la mejora continua de nuestros servicios y en garantizar la satisfacción de nuestros pacientes. Nos aseguraremos de que su experiencia con nosotros sea lo más placentera y eficiente posible.\n" +
                        "\n" +
                        "Por favor, no dude en ponerse en contacto con nosotros si tiene alguna pregunta o necesita más información sobre nuestros servicios. Estamos siempre a su disposición."
        ).setFontSize(12);

        Text contenido3 = new Text(
                "Le agradecemos por elegirnos como su proveedor de atención médica, y esperamos poder servirle a usted y a su familia en el futuro cercano."
        ).setFontSize(12);
        Text contenido4 = new Text(""+
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

        document.close();


        Alert alertAviso = new Alert(Alert.AlertType.INFORMATION);
        alertAviso.setTitle("PDF EXITOSO");
        alertAviso.setHeaderText("PDF CREADO CON EXITO");
        alertAviso.setContentText("El PDF se a generado con exito, se encuentra en la raiz del sistema");
        alertAviso.show();
    }

    public void reporteCompleto(String name){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        int count = 0;

        try{
            ResultSet rs = conexion.executeQuery("SELECT COUNT(*) FROM pacientes");
            if(rs.next()){
                count = rs.getInt(1);
            }
            conexion.disconnect();
        } catch (SQLException e){
            e.printStackTrace();
        }
        conexion.connect();


        PdfWriter writer = null;
        try {

            writer = new PdfWriter(name+".pdf");
            PdfDocument pdfDocument = new PdfDocument(writer);
            PageSize pageSize = PageSize.A4;
            Document document = new Document(pdfDocument, pageSize);

            Text titulo = new Text("SYS-HEALTH\n").setFontSize(24).setBold();
            document.add(new Paragraph().add(titulo));

            Text subtitulo = new Text("REPORTE DE CREACIÓN DE PACIENTES\n").setFontSize(12).setBold().setItalic().setFontColor(ColorConstants.LIGHT_GRAY);
            document.add(new Paragraph().add(subtitulo).setFontColor(ColorConstants.LIGHT_GRAY));
            Text contenido1 = new Text("A quien corresponda: \n");

            Text contenido2 = new Text("Espero que esta carta lo/la encuentre bien. Me dirijo a usted para informarle sobre el número de pacientes que hemos registrado y " +
                    "ordenado en nuestro sistema.\n");

            Text contenido3 = new Text("Me complace informarle que hemos registrado y ordenado " + count +
                    " nuevas cuentas para pacientes en el último mes. Este es un testimonio del arduo trabajo y dedicación de " +
                    "nuestro equipo para proporcionar los mejores servicios de atención médica a nuestros pacientes.\n");

            Text contenido4 = new Text("Aprovecho esta oportunidad para agradecerle por su confianza en nosotros. Nos comprometemos a continuar brindando servicios de alta calidad y excelencia en la atención médica a todos nuestros pacientes." +
                    "\n");
            Text contenido5 = new Text("Atentamente: "+SesionUsuario.getUsuario());

            document.add(new Paragraph().add(subtitulo));
            document.add(new Paragraph().add(contenido1));
            document.add(new Paragraph().add(contenido2));
            document.add(new Paragraph().add(contenido3));
            document.add(new Paragraph().add(contenido4));
            document.add(new Paragraph().add(contenido5));
            document.close();

            System.out.println("ya");


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
