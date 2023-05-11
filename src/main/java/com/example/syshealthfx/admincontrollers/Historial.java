package com.example.syshealthfx.admincontrollers;

import java.util.Date;

public class Historial{
    private Date fechaHistorial;
    private String descripcionHistorial;
    public Historial(Date fechaHistorial, String descripcionHistorial) {
        this.descripcionHistorial = descripcionHistorial;
        this.fechaHistorial = fechaHistorial;
    }

    public Date getFechaHistorial() {
        return fechaHistorial;
    }

    public void setFechaHistorial(Date fechaHistorial) {
        this.fechaHistorial = fechaHistorial;
    }

    public String getDescripcionHistorial() {
        return descripcionHistorial;
    }

    public void setDescripcionHistorial(String descripcionHistorial) {
        this.descripcionHistorial = descripcionHistorial;
    }
}
