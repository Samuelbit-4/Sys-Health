package com.example.syshealthfx.admincontrollers;

public class Usuarios {
    private long id;
    private String usuario;
    private String password;
public Usuarios(long id, String usuario, String password){
    this.id = id;
    this.usuario = usuario;
    this.password = password;
}
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
