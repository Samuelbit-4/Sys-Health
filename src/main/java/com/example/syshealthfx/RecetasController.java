package com.example.syshealthfx;

import com.example.syshealthfx.admincontrollers.Citas;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.svg.converter.SvgConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;

import com.itextpdf.html2pdf.HtmlConverter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class RecetasController{
    Citas citas;
    @FXML
    private TextField txtDiagnostico;
    @FXML
    private ScrollPane medicamentos;
    @FXML
    private HTMLEditor tratamiento;
    @FXML
    private TextArea descipcionTratamiento;
    @FXML
    private HBox medicamentoInicial;
    @FXML
    private Button btnMas;
    private long idPaciente, idMedico, idCita;
    public void crearReceta(){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        try{
            String query = "INSERT INTO recetas(id_paciente, id_medico, fecha, diagnostico, tratamiento, id_cita)" +
                    " VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conexion.preparedStatement(query);
            ps.setLong(1, idPaciente);
            ps.setLong(2, idMedico);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setString(4, txtDiagnostico.getText());
            ps.setString(5, descipcionTratamiento.getText());
            ps.setLong(6, idCita);
            int state = ps.executeUpdate();
            if (state == 1){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Seleccionar ubicación");
                File initialDirectory = new File(System.getProperty("user.home"));
                fileChooser.setInitialDirectory(initialDirectory);
                File selectedDirectory = fileChooser.showSaveDialog(descipcionTratamiento.getScene().getWindow());
                if (selectedDirectory != null) {
                    crearRecetaPDF(selectedDirectory.getAbsolutePath());
                    System.out.println("Ubicación seleccionada: " + selectedDirectory.getAbsolutePath());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("RECETA");
                    alert.setHeaderText("RECETA CREADA CON ÉXITO");
                    alert.setContentText("La receta se ha guardado con éxito");
                    alert.showAndWait();
                    Stage stage = (Stage) descipcionTratamiento.getScene().getWindow();
                    stage.close();
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setItems(Citas citas){
        this.idPaciente = citas.getIdPaciente();
        this.idMedico = citas.getIdMedico();
        this.idCita = citas.getIdCita();
        this.citas = citas;
    }
    public void crearRecetaPDF(String ubicacion) throws IOException {
        // Crear un archivo PDF
        File pdfFile = new File(ubicacion+".pdf");

        // Crear el escritor de PDF
        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));

        // Crear el documento PDF
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Crear el documento para agregar contenido
        Document doc = new Document(pdfDoc);

        // Crear la tabla para mostrar los datos personales
        Table table = new Table(4);
        table.setBorder(Border.NO_BORDER);
        table.setFontSize(12);
        Text titulo = new Text("RECETA MÉDICA");
        Paragraph p = new Paragraph().add(titulo).setTextAlignment(TextAlignment.CENTER).setFontSize(20).setBold();
        Border border = new SolidBorder(ColorConstants.BLACK, 1f);
        p.setPaddingBottom(5);
        doc.add(p);
        // Agregar los datos personales a la tabla
        table.addCell(new Cell().add(new Paragraph("PACIENTE:")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table.addCell(new Cell().add(new Paragraph(citas.getNombreCompletoPaciente())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table.addCell(new Cell().add(new Paragraph("MÉDICO:")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table.addCell(new Cell().add(new Paragraph(citas.getNombreCompletoMedico())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table.addCell(new Cell().add(new Paragraph("FECHA DE NACIMIENTO:")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(citas.getFechaNacimiento()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table.addCell(new Cell().add(new Paragraph("CEDULA PROFESIONAL:")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table.addCell(new Cell().add(new Paragraph(citas.getCedulaMedico())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table.addCell(new Cell().add(new Paragraph("FECHA DE CONSULTA:")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setFontSize(10));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(citas.getFechaHora()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT)).setFontSize(10);
        table.setBorderBottom(border);
        doc.add(table);
        Text diagnostico = new Text("Diagnostico: ").setBold();
        Text descripcion = new Text(txtDiagnostico.getText());
        Paragraph diagnosticoDescripcion = new Paragraph().add(diagnostico).setPaddingTop(5);
        diagnosticoDescripcion.add(descripcion);
        doc.add(diagnosticoDescripcion);

        Text tratamientos = new Text("Tratamiento e indicaciones médicas").setBold();
        Paragraph tratamientoDescripcion = new Paragraph(tratamientos);
        doc.add(tratamientoDescripcion);
        Text descripcionTratamientos = new Text(descipcionTratamiento.getText());
        Paragraph textDescripcion = new Paragraph(descripcionTratamientos);
        doc.add(textDescripcion);

        // Cerrar el documento
        doc.close();
    }

    @FXML
    public void cancelar(){
        Stage stage = (Stage) descipcionTratamiento.getScene().getWindow();
        stage.close();
    }

}
