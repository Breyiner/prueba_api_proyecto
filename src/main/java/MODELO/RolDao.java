package MODELO;

import MODELO.Rol;
import DATABASE.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RolDao {

    public static ResultSet getRoles() {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM roles");
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los roles");
        }
    }

    public static ResultSet getRolById(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM roles WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el rol");
        }
    }

    public static ResultSet createRol(Rol rolData) {
        Connection connection = ConnectionDB.connect();
        String query = "INSERT INTO roles (nombre) VALUES (?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setString(1, rolData.getNombre());
            pstm.executeUpdate();
            return pstm.getGeneratedKeys();
        } catch (SQLException e) {
            throw new Error("Error al crear el rol");
        }
    }

    public static int updateRol(int id, Rol rolData) {
        Connection connection = ConnectionDB.connect();
        String query = "UPDATE roles SET nombre = ? WHERE id = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, rolData.getNombre());
            pstm.setInt(2, id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al actualizar el rol");
        }
    }

    public static int deleteRol(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM roles WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al eliminar el rol");
        }
    }
}