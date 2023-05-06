package com.example.syshealthfx.admincontrollers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Citas {
    private String titulo;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public Citas(String titulo, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
}
