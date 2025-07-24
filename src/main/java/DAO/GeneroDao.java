package DAO;

import MODEL.Genero;
import UTILS.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneroDao {

    public static ResultSet getGeneros() {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM generos");
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los géneros");
        }
    }

    public static ResultSet getGeneroById(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM generos WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el género");
        }
    }

    public static ResultSet createGenero(Genero generoData) {
        Connection connection = ConnectionDB.connect();
        String query = "INSERT INTO generos (nombre) VALUES (?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setString(1, generoData.getNombre());
            pstm.executeUpdate();
            return pstm.getGeneratedKeys();
        } catch (SQLException e) {
            throw new Error("Error al crear el género");
        }
    }

    public static int updateGenero(int id, Genero generoData) {
        Connection connection = ConnectionDB.connect();
        String query = "UPDATE generos SET nombre = ? WHERE id = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, generoData.getNombre());
            pstm.setInt(2, id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al actualizar el género");
        }
    }

    public static int deleteGenero(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM generos WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al eliminar el género");
        }
    }
}