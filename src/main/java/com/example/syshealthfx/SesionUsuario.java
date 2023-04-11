package com.example.syshealthfx;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SesionUsuario {
    private static String usuario;
    private static String rol;

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        SesionUsuario.usuario = usuario;
    }

    public static String getRol() {
        return rol;
    }

    public static void setRol(String rol) {
        SesionUsuario.rol = rol;
    }

    public static String nombreCompleto() throws SQLException {
        SQLClass conn = new SQLClass("root", "", "sys_health_prueba");
        conn.connect();
        ResultSet set = conn.executeQuery("SELECT u.usuario, u.id_usuario, e.nombre, e.apellido_paterno, e.apellido_materno\n" +
                "FROM usuarios u\n" +
                "INNER JOIN empleados e ON u.id_empleado = e.id_empleado "+
                "WHERE u.usuario='"+usuario+"';");
        String nombre = set.getString("nombre");
        conn.disconnect();
        return nombre;
    }
}
