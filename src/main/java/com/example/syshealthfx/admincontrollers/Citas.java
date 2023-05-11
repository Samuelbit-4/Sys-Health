package com.example.syshealthfx.admincontrollers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class Citas {
    private long idCita;
    private long idMedico;
    private Date fechaHora;
    private String descripcion;
    private String cedula;
    private long idEmpleado;
    private String nombreMedico;
    private String apellidoPMedico;
    private String apellidoMMedico;
    private String nombrePaciente;
    private String apellidoPPaciente;
    private String apellidoMPaciente;
    private Date fechaNacimiento;
    private String cedulaMedico;
    private long idPaciente;


    public Citas(long idCita, long idMedico, Date fechaHora, String descripcion, String cedula, long idEmpleado, String nombreMedico, String apellidoPMedico, String apellidoMMedico, String nombrePaciente, String apellidoPPaciente, String apellidoMPaciente, Date fechaNacimiento, String cedulaMedico, long idPaciente) {
        this.idCita = idCita;
        this.idMedico = idMedico;
        this.fechaHora = fechaHora;
        this.descripcion = descripcion;
        this.cedula = cedula;
        this.idEmpleado = idEmpleado;
        this.nombreMedico = nombreMedico;
        this.apellidoPMedico = apellidoPMedico;
        this.apellidoMMedico = apellidoMMedico;
        this.nombrePaciente = nombrePaciente;
        this.apellidoPPaciente = apellidoPPaciente;
        this.apellidoMPaciente = apellidoMPaciente;
        this.fechaNacimiento = fechaNacimiento;
        this.cedulaMedico = cedulaMedico;
        this.idPaciente = idPaciente;
    }

    public long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getCedulaMedico() {
        return cedulaMedico;
    }

    public void setCedulaMedico(String cedulaMedico) {
        this.cedulaMedico = cedulaMedico;
    }

    public long getIdCita() {
        return idCita;
    }

    public void setIdCita(long idCita) {
        this.idCita = idCita;
    }

    public long getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(long idMedico) {
        this.idMedico = idMedico;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getApellidoPMedico() {
        return apellidoPMedico;
    }

    public void setApellidoPMedico(String apellidoPMedico) {
        this.apellidoPMedico = apellidoPMedico;
    }

    public String getApellidoMMedico() {
        return apellidoMMedico;
    }

    public void setApellidoMMedico(String apellidoMMedico) {
        this.apellidoMMedico = apellidoMMedico;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getApellidoPPaciente() {
        return apellidoPPaciente;
    }

    public void setApellidoPPaciente(String apellidoPPaciente) {
        this.apellidoPPaciente = apellidoPPaciente;
    }

    public String getApellidoMPaciente() {
        return apellidoMPaciente;
    }

    public void setApellidoMPaciente(String apellidoMPaciente) {
        this.apellidoMPaciente = apellidoMPaciente;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getNombreCompletoPaciente(){
       return nombrePaciente + " " + apellidoPPaciente + " " + apellidoMPaciente;
    }
    public String getNombreCompletoMedico(){
        return nombreMedico + " " + apellidoPMedico + " " + apellidoMMedico;
    }
}
