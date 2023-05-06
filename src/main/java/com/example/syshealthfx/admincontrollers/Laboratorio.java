package com.example.syshealthfx.admincontrollers;

import java.util.Date;

public class Laboratorio
{
    private long idOrden;
    private long idPaciente;
    private long idMedico;
    private Date fecha;
    private String tipoAnalisis;

    public Laboratorio(long idOrden, long idPaciente, long idMedico, Date fecha, String tipoAnalisis) {
        this.idOrden = idOrden;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fecha = fecha;
        this.tipoAnalisis = tipoAnalisis;
    }

    public long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(long idOrden) {
        this.idOrden = idOrden;
    }

    public long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public long getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(long idMedico) {
        this.idMedico = idMedico;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipoAnalisis() {
        return tipoAnalisis;
    }

    public void setTipoAnalisis(String tipoAnalisis) {
        this.tipoAnalisis = tipoAnalisis;
    }
}
