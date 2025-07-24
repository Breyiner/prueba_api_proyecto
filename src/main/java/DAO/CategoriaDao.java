package DAO;

import MODEL.Categoria;
import UTILS.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaDao {

    public static ResultSet getCategorias() {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM categorias");
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener las categorías");
        }
    }

    public static ResultSet getCategoriaById(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM categorias WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener la categoría");
        }
    }

    public static ResultSet createCategoria(Categoria categoriaData) {
        Connection connection = ConnectionDB.connect();
        String query = "INSERT INTO categorias (nombre, icono, tipo_movimiento_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setString(1, categoriaData.getNombre());
            pstm.setString(2, categoriaData.getIcono());
            pstm.setLong(3, categoriaData.getTipo_movimiento_id());
            pstm.executeUpdate();
            return pstm.getGeneratedKeys();
        } catch (SQLException e) {
            throw new Error("Error al crear la categoría");
        }
    }

    public static int updateCategoria(int id, Categoria categoriaData) {
        Connection connection = ConnectionDB.connect();
        String query = "UPDATE categorias SET nombre = ?, icono = ?, tipo_movimiento_id = ? WHERE id = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, categoriaData.getNombre());
            pstm.setString(2, categoriaData.getIcono());
            pstm.setLong(3, categoriaData.getTipo_movimiento_id());
            pstm.setInt(4, id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al actualizar la categoría");
        }
    }

    public static int deleteCategoria(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM categorias WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al eliminar la categoría");
        }
    }
}
