package DAO;

import MODEL.Movimiento;
import UTILS.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovimientoDao {

    
    public static ResultSet getMovimientos() {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM movimientos");
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los movimientos");
        }
    }

    public static ResultSet getMovimientoById(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM movimientos WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el movimiento");
        }
    }
    
    public static ResultSet getMovimientosByUserId(int usuario_id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM movimientos WHERE usuario_id = ?");
            pstm.setInt(1, usuario_id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el movimiento");
        }
    }
    
    public static ResultSet getMovimientosByCategoria(int usuario_id, int tipo_movimiento_id, int mes) {
        
        Connection connection = ConnectionDB.connect();
        String query =  """
                        SELECT 
                            m.id,
                            cat.icono,
                            cat.nombre as categoria,
                            tm.color,
                            m.nombre,
                            m.fecha_creacion,
                            m.monto
                        FROM 
                            movimientos m
                        JOIN categorias cat ON m.categoria_id = cat.id
                        JOIN tipos_movimiento tm ON cat.tipo_movimiento_id = tm.id
                        WHERE 
                            m.usuario_id = ?
                            AND tm.id = ?
                            AND MONTH(m.fecha_creacion) = ?
                        ORDER BY 
                            m.fecha_creacion desc
                        """;
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, usuario_id);
            pstm.setInt(2, tipo_movimiento_id);
            pstm.setInt(3, mes);
           
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los movimientos");
        }
        
    }

    public static ResultSet createMovimiento(Movimiento movimientoData) {
        Connection connection = ConnectionDB.connect();
        String query = "INSERT INTO movimientos (usuario_id, nombre, monto, descripcion, categoria_id) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, movimientoData.getUsuario_id());
            pstm.setString(2, movimientoData.getNombre());
            pstm.setBigDecimal(3, movimientoData.getMonto());
            pstm.setString(4, movimientoData.getDescripcion());
            pstm.setInt(5, movimientoData.getCategoria_id());
            pstm.executeUpdate();
            return pstm.getGeneratedKeys();
        } catch (SQLException e) {
            throw new Error("Error al crear el movimiento");
        }
    }

    public static int updateMovimiento(int id, int usuario_id, Movimiento movimientoData) {
        Connection connection = ConnectionDB.connect();
        String query = "UPDATE movimientos SET nombre = ?, monto = ?, descripcion = ?, categoria_id = ? WHERE id = ? and usuario_id = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, movimientoData.getNombre());
            pstm.setBigDecimal(2, movimientoData.getMonto());
            pstm.setString(3, movimientoData.getDescripcion());
            pstm.setInt(4, movimientoData.getCategoria_id());
            pstm.setInt(5, id);
            pstm.setInt(6, usuario_id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al actualizar el movimiento");
        }
    }

    public static int deleteMovimiento(int id, int usuario_id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM movimientos WHERE id = ? and usuario_id = ?");
            pstm.setInt(1, id);
            pstm.setInt(2, usuario_id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al eliminar el movimiento");
        }
    }
}
