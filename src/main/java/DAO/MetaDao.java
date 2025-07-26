package DAO;

import MODEL.Meta;
import UTILS.ConnectionDB;

import java.sql.*;

public class MetaDao {

    public static ResultSet getMetas() {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM metas");
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener las metas");
        }
    }
    
    public static ResultSet getCantidadMetas() {
        
        Connection connection = ConnectionDB.connect();
        
        try {

            PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(*) AS cantidad FROM metas");
            ResultSet respuesta = pstm.executeQuery();
            
            return respuesta;
            
        } catch (SQLException e) {
            throw new Error("Error al obtener la cantidad de metas");
        }
    }
    
    public static ResultSet getMetasConTotal(int usuario_id) {
        Connection connection = ConnectionDB.connect();
        try {
            
            String query = """
                           SELECT 
                               m.id,
                               m.nombre,
                               m.monto,
                               m.fecha_limite,
                               m.fecha_creacion,
                               m.completada,
                               COALESCE(SUM(apm.monto), 0) AS total
                           FROM metas AS m
                           LEFT JOIN aportes_metas AS apm ON m.id = apm.meta_id
                           WHERE m.usuario_id = ?
                           GROUP BY
                               m.id, m.nombre, m.monto, m.fecha_limite, m.fecha_creacion, m.completada
                           ORDER BY
                               MONTH(m.fecha_creacion), m.completada;
                           """;
            
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, usuario_id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener las metas");
        }
    }
    
    public static ResultSet getMetasCantMovimientos(int id, int usuario_id) {
        Connection connection = ConnectionDB.connect();
        try {
            
            String query = """
                           SELECT 
                               m.id,
                               m.nombre,
                               m.descripcion,
                               m.monto,
                               m.fecha_creacion,
                               m.fecha_limite,
                               COUNT(a.id) AS cantidad_aportes
                           FROM metas m
                           LEFT JOIN aportes_metas a ON 
                               m.id = a.meta_id
                           WHERE 
                               m.id = ? AND m.usuario_id = ?
                           GROUP BY 
                               m.id, m.nombre, m.descripcion, m.monto, m.fecha_creacion, m.fecha_limite;
                           """;
            
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, id);
            pstm.setInt(2, usuario_id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener las metas");
        }
    }

    public static ResultSet getMetaById(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM metas WHERE id = ?");
            pstm.setInt(1, id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener la meta");
        }
    }

    public static ResultSet createMeta(Meta meta) {
        Connection connection = ConnectionDB.connect();
        String query = "INSERT INTO metas (usuario_id, nombre, monto, descripcion, fecha_limite) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, meta.getUsuario_id());
            pstm.setString(2, meta.getNombre());
            pstm.setBigDecimal(3, meta.getMonto());
            pstm.setString(4, meta.getDescripcion());
            pstm.setDate(5, meta.getFecha_limite());
            pstm.executeUpdate();
            return pstm.getGeneratedKeys();
        } catch (SQLException e) {
            throw new Error("Error al crear la meta");
        }
    }

    public static int updateMeta(int id, int usuario_id, Meta meta) {
        Connection connection = ConnectionDB.connect();
        String query = "UPDATE metas SET nombre = ?, monto = ?, descripcion = ?, fecha_limite = ? WHERE id = ? AND usuario_id = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, meta.getNombre());
            pstm.setBigDecimal(2, meta.getMonto());
            pstm.setString(3, meta.getDescripcion());
            pstm.setDate(4, meta.getFecha_limite());
            pstm.setInt(5, id);
            pstm.setInt(6, usuario_id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al actualizar la meta");
        }
    }

    public static int updateCompletada(int id, int usuario_id, boolean completada) {
        Connection connection = ConnectionDB.connect();
        String query = "UPDATE metas SET completada = ? WHERE id = ? AND usuario_id = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setBoolean(1, completada);
            pstm.setInt(2, id);
            pstm.setInt(3, usuario_id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al actualizar el estado de completada");
        }
    }

    public static int deleteMeta(int id, int usuario_id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM metas WHERE id = ? AND usuario_id = ?");
            pstm.setInt(1, id);
            pstm.setInt(2, usuario_id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al eliminar la meta");
        }
    }
}