package com.microsoft.sqlserver;
import java.sql.*;
public class Sqlconection{
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/pruebas";
        String username = "root";
        String password = "";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection to MySQL database has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
