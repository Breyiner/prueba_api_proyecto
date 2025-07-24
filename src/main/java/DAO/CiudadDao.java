/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import MODEL.Ciudad;
import UTILS.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Windows 10
 */
public class CiudadDao {
    
    /**
     * Método para obtener todas las ciudades de la base de datos.
     * Realiza una consulta SQL para seleccionar todos los registros de la tabla 'ciudades'.
     * 
     * @return ResultSet que contiene todas las ciudades o null si ocurre un error
     */
    public static ResultSet getCiudades() {
        
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos
        
        try {
            // Prepara la consulta SQL para seleccionar todas las ciudades
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM ciudades");
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados
            
            return respuesta; // Devuelve el ResultSet con las ciudades
            
        } catch (SQLException e) {
            throw new Error("Error al obtener las ciudades"); // Devuelve null si ocurre un error en la consulta
        }
    }
    
    public static ResultSet getCiudadById(int id) {
        
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos
        
        try {
            // Prepara la consulta SQL para seleccionar todas las ciudades
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM ciudades where id = ?");
            pstm.setInt(1, id); // Establece el ID en la consulta
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados
            
            return respuesta; // Devuelve el ResultSet con las ciudades
            
        } catch (SQLException e) {
            throw new Error("Error al obtener la ciudad"); // Devuelve null si ocurre un error en la consulta
        }
    }
    
    public static ResultSet createCiudad(Ciudad ciudadData) {
        
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos
        
        // Consulta SQL para insertar un nuevo usuario en la tabla
        String query = "INSERT INTO ciudades (nombre) VALUES (?)";
        
        try {
            // Prepara la consulta SQL para insertar una nueva ciudad y obtener las claves generadas
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            
            // Establece los valores de la ciudad en la consulta
            pstm.setString(1, ciudadData.getNombre());

            pstm.executeUpdate(); // Ejecuta la inserción
            
            ResultSet generatedKeys = pstm.getGeneratedKeys(); // Obtiene las claves generadas
            
            return generatedKeys; // Devuelve el ResultSet con las claves generadas
            
        } catch (SQLException e) {
            throw new Error("Error al crear la ciudad");
        }
    }
    
    public static int updateCiudad(int id, Ciudad ciudadData) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos
        
        // Consulta SQL para actualizar los datos de la ciudad
        String query = "UPDATE ciudades SET nombre = ? WHERE id = ?";
        
        try {
            // Prepara la consulta SQL para actualizar la ciudad
            PreparedStatement pstm = connection.prepareStatement(query);
            
            // Establece los nuevos valores de la ciudad en la consulta
            pstm.setString(1, ciudadData.getNombre());
            pstm.setInt(2, id); // Establece el ID del usuario a actualizar
            
            int affectedRow = pstm.executeUpdate(); // Ejecuta la actualización y obtiene el número de filas afectadas
            
            return affectedRow; // Devuelve el número de filas afectadas
            
        } catch (SQLException e) {
            throw new Error("Error al actualizar la ciudad");
        }
    }
    
    public static int deleteCiudad(int id) {
        
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos
        
        try {
            // Prepara la consulta SQL para eliminar una ciudad por ID
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM ciudades WHERE id = ?");
            pstm.setInt(1, id); // Establece el ID del usuario a eliminar
            int affectedRows = pstm.executeUpdate(); // Ejecuta la eliminación y obtiene el número de filas afectadas
            
            return affectedRows; // Devuelve el número de filas afectadas
            
        } catch (SQLException e) {
            throw new Error("Error al eliminar la ciudad");
        }
    }
}
