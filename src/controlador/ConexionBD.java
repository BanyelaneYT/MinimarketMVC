package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
   
    private static final String URL = "jdbc:mysql://localhost:3306/bd_tambo?useSSL=false&serverTimezone=UTC";
    
    private static final String USUARIO = "root";   
    
    private static final String CONTRASENA = "";   


    public static Connection conectar() throws SQLException {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC de MySQL no encontrado. Asegúrate de que el JAR del driver está en el classpath.");
            throw new SQLException("Driver JDBC de MySQL no encontrado", e);
        }
    }

    public static void cerrarConexion(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión a la BD cerrada correctamente."); // Mensaje opcional
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    public static Connection obtenerConexion() {
        try {
            return conectar();
        } catch (SQLException e) {
            // Manejar la excepción de forma más robusta aquí si es necesario,
            // por ejemplo, mostrando un error fatal al usuario.
            System.err.println("Error fatal al obtener la conexión: " + e.getMessage());
            return null; // Devuelve null si la conexión falla
        }
    }
}