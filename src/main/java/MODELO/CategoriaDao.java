package MODELO;  // Paquete donde está la clase

import MODELO.Categoria;  // Importa la clase modelo Categoria
import DATABASE.ConnectionDB;  // Importa la clase para conexión a BD
import java.sql.Connection;  // Clases para manejo SQL
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaDao {  // Clase para operaciones CRUD con la tabla categorias

    // Método para obtener todas las categorías
    public static ResultSet getCategorias() {
        Connection connection = ConnectionDB.connect();  // Conecta a la BD
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM categorias");  // Prepara consulta
            return pstm.executeQuery();  // Ejecuta y retorna resultados
        } catch (SQLException e) {
            throw new Error("Error al obtener las categorías");  // Si falla, lanza error
        }
    }
    
    // Obtener categorías filtradas por tipo_movimiento_id
    public static ResultSet getCategoriasByMovimientoId(int tipo_movimiento_id) {
        Connection connection = ConnectionDB.connect();  // Conexión BD
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM categorias WHERE tipo_movimiento_id = ?");  // Consulta con parámetro
            pstm.setInt(1, tipo_movimiento_id);  // Asigna valor al parámetro
            return pstm.executeQuery();  // Ejecuta y retorna resultados
        } catch (SQLException e) {
            throw new Error("Error al obtener las categorías");  // Error si falla
        }
    }

    // Obtener una categoría por su id
    public static ResultSet getCategoriaById(int id) {
        Connection connection = ConnectionDB.connect();  // Conecta a BD
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM categorias WHERE id = ?");  // Consulta parametrizada
            pstm.setInt(1, id);  // Asigna id al parámetro
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener la categoría");  // Lanza error si falla
        }
    }

    // Crear una nueva categoría y devolver el id generado
    public static ResultSet createCategoria(Categoria categoriaData) {
        Connection connection = ConnectionDB.connect();  // Conecta BD
        String query = "INSERT INTO categorias (nombre, icono, tipo_movimiento_id) VALUES (?, ?, ?)";  // Query insert
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);  // Prepara statement para obtener clave generada
            pstm.setString(1, categoriaData.getNombre());  // Setea nombre
            pstm.setString(2, categoriaData.getIcono());  // Setea icono
            pstm.setLong(3, categoriaData.getTipo_movimiento_id());  // Setea tipo_movimiento_id
            pstm.executeUpdate();  // Ejecuta insert
            return pstm.getGeneratedKeys();  // Retorna las claves generadas (id)
        } catch (SQLException e) {
            throw new Error("Error al crear la categoría");  // Lanza error si falla
        }
    }

    // Actualizar categoría por id con nuevos datos
    public static int updateCategoria(int id, Categoria categoriaData) {
        Connection connection = ConnectionDB.connect();  // Conecta BD
        String query = "UPDATE categorias SET nombre = ?, icono = ?, tipo_movimiento_id = ? WHERE id = ?";  // Query update
        try {
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara statement
            pstm.setString(1, categoriaData.getNombre());  // Setea nombre
            pstm.setString(2, categoriaData.getIcono());  // Setea icono
            pstm.setLong(3, categoriaData.getTipo_movimiento_id());  // Setea tipo_movimiento_id
            pstm.setInt(4, id);  // Setea id para filtro WHERE
            return pstm.executeUpdate();  // Ejecuta y retorna número filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al actualizar la categoría");  // Lanza error si falla
        }
    }

    // Eliminar categoría por id
    public static int deleteCategoria(int id) {
        Connection connection = ConnectionDB.connect();  // Conecta BD
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM categorias WHERE id = ?");  // Query delete con parámetro
            pstm.setInt(1, id);  // Asigna id
            return pstm.executeUpdate();  // Ejecuta y retorna filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al eliminar la categoría");  // Lanza error si falla
        }
    }
}
