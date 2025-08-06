package MODELO;

import MODELO.TiposMovimiento;
import DATABASE.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TiposMovimientoDao {

    public static ResultSet getTipos() {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM tipos_movimiento");
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los tipos de movimiento");
        }
    }

    public static ResultSet getTipoById(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM tipos_movimiento WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el tipo de movimiento");
        }
    }

    public static ResultSet createTipo(TiposMovimiento tiposMovimientoData) {
        Connection connection = ConnectionDB.connect();
        String query = "INSERT INTO tipos_movimiento (nombre, icono, color) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setString(1, tiposMovimientoData.getNombre());
            pstm.setString(2, tiposMovimientoData.getIcono());
            pstm.setString(3, tiposMovimientoData.getColor());
            pstm.executeUpdate();
            return pstm.getGeneratedKeys();
        } catch (SQLException e) {
            throw new Error("Error al tipo de movimiento");
        }
    }

    public static int updateTipo(int id, TiposMovimiento tiposMovimientoData) {
        Connection connection = ConnectionDB.connect();
        String query = "UPDATE tipos_movimiento SET nombre = ?, icono = ?, color = ? WHERE id = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, tiposMovimientoData.getNombre());
            pstm.setString(2, tiposMovimientoData.getIcono());
            pstm.setString(3, tiposMovimientoData.getColor());
            pstm.setInt(4, id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al actualizar el tipo de movimiento");
        }
    }

    public static int deleteTipo(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM tipos_movimiento WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al eliminar el tipo de movimiento");
        }
    }
}