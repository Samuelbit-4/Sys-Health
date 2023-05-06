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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TablaLab {

    public TableView<Laboratorio> mostrarTabla(){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        TableView<Laboratorio> tablaLab = new TableView<>();
        ObservableList<Laboratorio> listaLab = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM ordenes_laboratorio";
            ResultSet rs = conexion.executeQuery(query);
            while (rs.next()) {
                Laboratorio laboratorio = new Laboratorio(rs.getLong("id_orden"), rs.getLong("id_paciente"), rs.getLong("id_medico"), rs.getDate("fecha"), rs.getString("tipo_analisis"));
                listaLab.add(laboratorio);
            }
            TableColumn<Laboratorio, Integer> idOrden = new TableColumn<>("ID ORDEN");
            idOrden.setCellValueFactory(new PropertyValueFactory<>("idOrden"));

            TableColumn<Laboratorio, Integer> idPaciente = new TableColumn<>("ID PACIENTE");
            idPaciente.setCellValueFactory(new PropertyValueFactory<>("idPaciente"));

            TableColumn<Laboratorio, Integer> idMedico = new TableColumn<>("ID MEDICO");
            idMedico.setCellValueFactory(new PropertyValueFactory<>("idMedico"));

            TableColumn<Laboratorio, Date> fecha = new TableColumn<>("FECHA DE REALIZACIÓN");
            fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

            TableColumn<Laboratorio, String> tipoDeAnalisis = new TableColumn<>("TIPO DE ANALISIS");
            tipoDeAnalisis.setCellValueFactory(new PropertyValueFactory<>("tipoAnalisis"));


            tablaLab.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tablaLab.setItems(listaLab);

            tablaLab.getColumns().addAll(idOrden, idPaciente, idMedico, fecha, tipoDeAnalisis);

            conexion.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tablaLab;
    }
    public void generarReporteLab(){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        int count = 0;

        try{
            ResultSet rs = conexion.executeQuery("SELECT COUNT(*) FROM ordenes_laboratorio");
            if(rs.next()){
                count = rs.getInt(1);
            }
            conexion.disconnect();
        } catch (SQLException e){

        }
        PdfWriter writer = null;
        try {
            writer = new PdfWriter("reporte-lab.pdf");
            PdfDocument pdfDocument = new PdfDocument(writer);
            PageSize pageSize = PageSize.A4;
            Document document = new Document(pdfDocument, pageSize);

            Text titulo = new Text("SYS-HEALTH\n").setFontSize(24).setBold();
            document.add(new Paragraph().add(titulo).setTextAlignment(TextAlignment.CENTER));

            Text subtitulo = new Text("REPORTE DE PRUEBAS DE LABORATORIO\n").setFontSize(12).setBold().setItalic().setFontColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER);
            document.add(new Paragraph().add(subtitulo).setTextAlignment(TextAlignment.CENTER).setFontColor(ColorConstants.LIGHT_GRAY));
            Text contenido1 = new Text("A quien corresponda: \n");

            Text contenido2 = new Text("Espero que esta carta lo/la encuentre bien. Me dirijo a usted para informarle sobre el número de pruebas de laboratorio que hemos registrado y " +
                    "ordenado en nuestro sistema.\n");

            Text contenido3 = new Text("Me complace informarle que hemos registrado y ordenado " + count +
                    " pruebas de laboratorio en el último mes. Este es un testimonio del arduo trabajo y dedicación de " +
                    "nuestro equipo para proporcionar los mejores servicios de atención médica a nuestros pacientes.\n");

            Text contenido4 = new Text("Aprovecho esta oportunidad para agradecerle por su confianza en nosotros. Nos comprometemos a continuar brindando servicios de alta calidad y excelencia en la atención médica a todos nuestros pacientes." +
                    "\n");
            Text contenido5 = new Text("Atentamente: ");

            document.add(new Paragraph().add(subtitulo).setTextAlignment(TextAlignment.JUSTIFIED));
            document.add(new Paragraph().add(contenido1).setTextAlignment(TextAlignment.JUSTIFIED));
            document.add(new Paragraph().add(contenido2).setTextAlignment(TextAlignment.JUSTIFIED));
            document.add(new Paragraph().add(contenido3).setTextAlignment(TextAlignment.JUSTIFIED));
            document.add(new Paragraph().add(contenido4).setTextAlignment(TextAlignment.JUSTIFIED));
            document.add(new Paragraph().add(contenido5).setTextAlignment(TextAlignment.JUSTIFIED));
            document.close();

            System.out.println("ya");


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
