package UTILS; // Paquete que contiene utilidades de la aplicación

import java.sql.Connection; // Importa la clase Connection para manejar conexiones a la base de datos
import java.sql.DriverManager; // Importa la clase DriverManager para gestionar los controladores de conexión
import java.sql.SQLException; // Importa la clase SQLException para manejar errores de SQL

/**
 * Clase que maneja la conexión a la base de datos.
 * Proporciona un método para establecer una conexión con la base de datos MySQL.
 */
public class ConnectionDB {
    

    /**
     * Método para establecer una conexión con la base de datos.
     * 
     * @return Connection objeto que representa la conexión a la base de datos o null si falla
     */
    public static Connection connect() {
        
        // Credenciales y URL de conexión a la base de datos
        String userName = "root"; // Nombre de usuario para la base de datos
        String password = "#Aprendiz2024"; // Contraseña para la base de datos
        String url = "jdbc:mysql://localhost:3306/proyecto_finanzas"; // URL de conexión a la base de datos
        Connection connection = null; // Inicializa la conexión como null
        
        try {
            // Carga el controlador JDBC para MySQL
            Class.forName("com.mysql.jdbc.Driver");
            
            // Establece la conexión a la base de datos utilizando las credenciales y la URL
            connection = DriverManager.getConnection(url, userName, password);
            
            System.out.println("Conexión realizada"); // Mensaje de éxito en la conexión
        
        } catch (ClassNotFoundException | SQLException e) {
            // Captura excepciones si el controlador no se encuentra o si hay un error en la conexión
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        
        return connection; // Devuelve la conexión establecida o null si hubo un error
    }
}