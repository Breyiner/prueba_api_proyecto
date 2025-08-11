package MODELO;  // Paquete donde está esta clase DAO

import MODELO.TiposMovimiento;  // Importa el modelo TiposMovimiento
import DATABASE.ConnectionDB;   // Importa la clase para conexión a la base de datos
import java.sql.Connection;     // Importa la clase Connection de JDBC
import java.sql.PreparedStatement;  // Para consultas preparadas
import java.sql.ResultSet;          // Para resultados de consulta
import java.sql.SQLException;       // Para manejar excepciones SQL

public class TiposMovimientoDao {

    // Método para obtener todos los tipos de movimiento
    public static ResultSet getTipos() {
        Connection connection = ConnectionDB.connect();  // Abre conexión a la DB
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM tipos_movimiento");  // Prepara consulta
            return pstm.executeQuery();  // Ejecuta consulta y devuelve ResultSet con todos los tipos
        } catch (SQLException e) {
            throw new Error("Error al obtener los tipos de movimiento");  // Lanza error en fallo SQL
        }
    }

    // Método para obtener un tipo de movimiento por su id
    public static ResultSet getTipoById(int id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM tipos_movimiento WHERE id = ?");  // Consulta con parámetro
            pstm.setInt(1, id);  // Asigna id al parámetro
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener el tipo de movimiento");  // Error SQL
        }
    }

    // Método para crear un nuevo tipo de movimiento, devuelve claves generadas
    public static ResultSet createTipo(TiposMovimiento tiposMovimientoData) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "INSERT INTO tipos_movimiento (nombre, icono, color, color_bg) VALUES (?, ?, ?, ?)";  // Consulta de inserción
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);  // Preparar para obtener ID generado
            pstm.setString(1, tiposMovimientoData.getNombre());  // Asignar nombre
            pstm.setString(2, tiposMovimientoData.getIcono());   // Asignar icono
            pstm.setString(3, tiposMovimientoData.getColor());   // Asignar color
            pstm.setString(4, tiposMovimientoData.getColor_bg());   // Asignar color de fondo
            pstm.executeUpdate();  // Ejecutar inserción
            return pstm.getGeneratedKeys();  // Retornar el ID generado
        } catch (SQLException e) {
            throw new Error("Error al tipo de movimiento");  // Error (texto mejorable: falta "crear" o "insertar")
        }
    }

    // Método para actualizar un tipo de movimiento por id
    public static int updateTipo(int id, TiposMovimiento tiposMovimientoData) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "UPDATE tipos_movimiento SET nombre = ?, icono = ?, color = ? WHERE id = ?";  // Consulta actualización
        try {
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta
            pstm.setString(1, tiposMovimientoData.getNombre());  // Nombre nuevo
            pstm.setString(2, tiposMovimientoData.getIcono());   // Icono nuevo
            pstm.setString(3, tiposMovimientoData.getColor());   // Color nuevo
            pstm.setInt(4, id);                                   // Id del registro a actualizar
            return pstm.executeUpdate();  // Ejecuta y retorna número de filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al actualizar el tipo de movimiento");  // Error SQL
        }
    }

    // Método para eliminar un tipo de movimiento por id
    public static int deleteTipo(int id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM tipos_movimiento WHERE id = ?");  // Consulta eliminación
            pstm.setInt(1, id);  // Asigna id
            return pstm.executeUpdate();  // Ejecuta y retorna filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al eliminar el tipo de movimiento");  // Error SQL
        }
    }
}
