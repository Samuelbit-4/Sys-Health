package com.example.syshealthfx;


import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.LocalDateTextField;


import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Calendario implements Initializable {

    @FXML
    private AnchorPane frame;
    @FXML
    private VBox container;
    @FXML
    private Text year;
    @FXML
    private Text month;
    @FXML
    private FlowPane calendar;
    @FXML
    private Text diaActual;
    @FXML
    private Text horaActual;

    @FXML
    private Button agendarCita;
    @FXML
    private Button modificarCita;
    @FXML
    private ScrollPane citasHoy;
    ZonedDateTime dateFocus;
    ZonedDateTime today;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
        mostrarCitas();
        actualizarHora();

    }
    public void mostrarCitas(){
        SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
        conexion.connect();
        ResultSet rs = null;
        try {
            String query = "SELECT citas.id_cita, citas.fecha_hora, citas.descripcion ," +
                    "medicos.id_medico,medicos.id_empleado, empleados.nombre, empleados.apellido_paterno, " +
                    "empleados.apellido_materno,pacientes.id_paciente, pacientes.nombre, " +
                    "pacientes.apellido_paterno, pacientes.apellido_materno " +
                    "FROM citas " +
                    "JOIN medicos ON citas.id_medico = medicos.id_medico " +
                    "JOIN empleados ON medicos.id_empleado = empleados.id_empleado " +
                    "JOIN pacientes ON citas.id_paciente = pacientes.id_paciente " +
                    "WHERE citas.fecha_hora >= CURDATE() " + // Agregar esta cláusula para verificar que la fecha actual no sea anterior
                    "ORDER BY citas.fecha_hora ASC;";
            rs = conexion.executeQuery(query);
            VBox contenedorCita = new VBox();
            while(rs.next()){
                VBox citaC = new VBox();
                citaC.setId("#contenedorCita");
                citaC.getChildren().add(new Label(String.valueOf("ID: "+ rs.getLong("id_cita"))));
                citaC.getChildren().add(new Label("FECHA - HORA : "+String.valueOf(rs.getTimestamp(2))));
                citaC.getChildren().add(new Label("MEDICO: "+ rs.getString(6) + " " + rs.getString(7) + " " + rs.getString(8)));
                citaC.getChildren().add(new Label("PACIENTE: " + rs.getString(10) + " " + rs.getString(11) + " " + rs.getString(12)));
                contenedorCita.getStyleClass().add("contenedor-cita");
                contenedorCita.setAlignment(Pos.TOP_CENTER);
                citaC.setPrefWidth(660);
                contenedorCita.getChildren().add(citaC);
                String paciente = String.valueOf(rs.getLong("id_paciente")) + " - " + rs.getString(10) + " " + rs.getString(11) + " " + rs.getString(12);
                String medico = String.valueOf(rs.getLong("id_medico"))+ " - " + rs.getString(6) + " " + rs.getString(7) + " " + rs.getString(8);
                long idCita = rs.getLong("id_cita");
                citaC.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-views/citas/modificar-cita.fxml"));
                        try {
                            Parent root = loader.load();
                            ModificarCita controller = loader.getController();
                            controller.setCita(paciente, medico, idCita);
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            agendarCita.getScene().getRoot().setDisable(true);
                            stage.showAndWait();
                            agendarCita.getScene().getRoot().setDisable(false);
                            FXMLLoader ventanaAnterior = new FXMLLoader(getClass().getResource("admin-views/citas/calendario.fxml"));
                            Parent rootAnterior = ventanaAnterior.load();
                            Scene sceneAnterior = new Scene(rootAnterior);
                            Stage stageAnterior = (Stage) agendarCita.getScene().getWindow();
                            stageAnterior.setScene(sceneAnterior);
                            stageAnterior.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

            citasHoy.setContent(contenedorCita);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void actualizarHora() {

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                horaActual.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        };
        timer.start();

    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    public void agregarCitaEvent(ActionEvent event) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-views/citas/crear-cita.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            agendarCita.getScene().getRoot().setDisable(true);
            stage.showAndWait();
            agendarCita.getScene().getRoot().setDisable(false);
            FXMLLoader ventanaAnterior = new FXMLLoader(getClass().getResource("admin-views/citas/calendario.fxml"));
            Parent rootAnterior = ventanaAnterior.load();
            Scene sceneAnterior = new Scene(rootAnterior);
            Stage stageAnterior = (Stage) agendarCita.getScene().getWindow();
            stageAnterior.setScene(sceneAnterior);
            stageAnterior.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void modificarCitaEvent(ActionEvent event){

    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        //List of activities for a given month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        // Check if current date is in the current month
                        boolean isInCurrentMonth = (dateFocus.getMonthValue() == dateFocus.withDayOfMonth(currentDate).getMonthValue());
                        if (isInCurrentMonth) {
                            List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                            if (calendarActivities != null) {
                                createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                            }
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();


        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if (!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {

            List<CalendarActivity> calendarActivities = new ArrayList<>();
            int year = dateFocus.getYear();
            int month = dateFocus.getMonthValue();
            SQLClass conexion = new SQLClass("root", "", "sys_health_prueba");
            conexion.connect();
            try {
                PreparedStatement stmt = conexion.preparedStatement("SELECT citas.id_cita, citas.fecha_hora, pacientes.nombre, pacientes.apellido_paterno, pacientes.apellido_materno " +
                        "FROM citas " +
                        "JOIN pacientes ON citas.id_paciente = pacientes.id_paciente " +
                        "WHERE YEAR(citas.fecha_hora) = ? AND MONTH(citas.fecha_hora) = ?");
                stmt.setInt(1, year);
                stmt.setInt(2, month);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    Timestamp timestamp = rs.getTimestamp("fecha_hora");
                    ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
                    String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno");
                    calendarActivities.add(new CalendarActivity(zonedDateTime, nombreCompleto, rs.getLong("id_cita")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return createCalendarMap(calendarActivities);
        }

    }



