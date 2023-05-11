package com.example.syshealthfx;

import com.example.syshealthfx.admincontrollers.Citas;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class LaboratorioController {
    Citas citas;
    @FXML
    private TextField txtDiagnostico;
    @FXML
    private TextArea txtAnalisis;
    private long idPaciente, idMedico, idCita;

    public void setItems(Citas citas){
        this.citas = citas;
        this.idPaciente = citas.getIdPaciente();
        this.idMedico = citas.getIdMedico();
        this.idCita = citas.getIdCita();
    }
    public void crearOrdenDeLaboratorio(){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        try{
            String query = "INSERT INTO ordenes_laboratorio(id_paciente, id_medico, fecha, tipo_analisis)" +
                    " VALUES(?, ?, ?, ?)";
            PreparedStatement ps = conexion.preparedStatement(query);
            ps.setLong(1, idPaciente);
            ps.setLong(2, idMedico);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setString(4, txtAnalisis.getText());
            int state = ps.executeUpdate();
            if (state == 1){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Seleccionar ubicación");
                File initialDirectory = new File(System.getProperty("user.home"));
                fileChooser.setInitialDirectory(initialDirectory);
                File selectedDirectory = fileChooser.showSaveDialog(txtAnalisis.getScene().getWindow());
                if (selectedDirectory != null) {
                    crearOrdenPDF(selectedDirectory.getAbsolutePath());
                    System.out.println("Ubicación seleccionada: " + selectedDirectory.getAbsolutePath());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ÉXITO");
                    alert.setHeaderText("ORDEN CREADA CON ÉXITO");
                    alert.setContentText("La orden se ha guardado con éxito");
                    alert.showAndWait();
                    Stage stage = (Stage) txtAnalisis.getScene().getWindow();
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
    public void crearOrdenPDF(String ruta) throws IOException{
        // Crear un archivo PDF
        File pdfFile = new File(ruta+".pdf");

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
        Text titulo = new Text("ORDEN DE LABORATORIO");
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

        Text tratamientos = new Text("Analisis a realizar: ").setBold();
        Paragraph tratamientoDescripcion = new Paragraph(tratamientos);
        doc.add(tratamientoDescripcion);
        Text descripcionTratamientos = new Text(txtAnalisis.getText());
        Paragraph textDescripcion = new Paragraph(descripcionTratamientos);
        doc.add(textDescripcion);

        // Cerrar el documento
        doc.close();
    }

    @FXML
    public void cancelar(){
        Stage stage = (Stage) txtAnalisis.getScene().getWindow();
        stage.close();
    }
}
