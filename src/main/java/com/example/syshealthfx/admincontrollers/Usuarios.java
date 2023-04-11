package com.example.syshealthfx.admincontrollers;

public class Usuarios {
    private int idUsuario;
    private String nombreCompleto;
    private String nombreDepartamento;
    private String usuario;

    public Usuarios(int idUsuario, String nombreCompleto, String nombreDepartamento, String usuario) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.nombreDepartamento = nombreDepartamento;
        this.usuario = usuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
