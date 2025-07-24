package DAO;

import MODEL.Estado;
import UTILS.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstadoDao {

    public static ResultSet getEstados() {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM estados");
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los estados");
        }
    }

    public static ResultSet getEstadoById(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM estados WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el estado");
        }
    }

    public static ResultSet createEstado(Estado estadoData) {
        Connection connection = ConnectionDB.connect();
        String query = "INSERT INTO estados (nombre) VALUES (?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setString(1, estadoData.getNombre());
            pstm.executeUpdate();
            return pstm.getGeneratedKeys();
        } catch (SQLException e) {
            throw new Error("Error al crear el estado");
        }
    }

    public static int updateEstado(int id, Estado estadoData) {
        Connection connection = ConnectionDB.connect();
        String query = "UPDATE estados SET nombre = ? WHERE id = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, estadoData.getNombre());
            pstm.setInt(2, id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al actualizar el estado");
        }
    }

    public static int deleteEstado(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM estados WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al eliminar el estado");
        }
    }
}