package UTILS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionDB {
    
    String userName = "root";
    String password = "#Aprendiz2024";
    String url = "jdbc:mysql://localhost:3306/prueba";
    
    
    public Connection connect() {
        
        Connection connection = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            connection = DriverManager.getConnection(url, userName, password);
            
            System.out.println("Conexión realizada");
        
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        
        return connection;
    }
}
