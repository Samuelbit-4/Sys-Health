package com.example.syshealthfx;
import java.sql.*;
public class SQLClass {
    private String host = "localhost";
    private String username;
    private String password;
    private String database;
    private Connection conn;

    public SQLClass(String username, String password, String database){
        this.username = username;
        this.password = password;
        this.database = database;
    }
    public void connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + this.host + "/" + this.database;
            this.conn = DriverManager.getConnection(url, this.username, this.password);
            System.out.println("CONEXIÓN EXITOSA\nBASE DE DATOS: "+this.database);
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    public void disconnect(){
        try{
            if(this.conn != null){
                this.conn.close();
                System.out.println("DESCONECTADO");
            }
        } catch(SQLException e){
            System.out.println("ERROR EN LA DES-CONEXIÓN "+ e.getMessage());
        }

    }
    public ResultSet executeQuery(String query) throws SQLException{
        Statement stmt = this.conn.createStatement();
        ResultSet result = stmt.executeQuery(query);
        return result;
    }
}
