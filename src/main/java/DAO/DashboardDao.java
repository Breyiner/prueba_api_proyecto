package DAO;

import UTILS.ConnectionDB;
import java.sql.*;

public class DashboardDao {

    public static ResultSet getResumenMovimientos(int usuario_id, int mes) {
        Connection connection = ConnectionDB.connect();
        try {
            String query = """
                           SELECT 
                              tm.icono,
                              tm.color,
                              tm.nombre,
                              COALESCE(SUM(m.monto), 0) AS total
                           FROM 
                              tipos_movimiento tm
                           LEFT JOIN categorias c ON c.tipo_movimiento_id = tm.id
                           LEFT JOIN movimientos m ON m.categoria_id = c.id 
                              AND m.usuario_id = ?
                              AND MONTH(m.fecha_creacion) = ?
                           WHERE
                              tm.id != 3
                           GROUP BY 
                              tm.id, tm.icono, tm.color, tm.nombre
                           ORDER BY 
                              tm.id;
                           """;
            
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, usuario_id);
            pstm.setInt(2, mes);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el resumen de movimientos");
        }
    }

    public static ResultSet getResumenMetas(int usuario_id, int mes) {
        Connection connection = ConnectionDB.connect();
        try {
            String query = """
                           SELECT 
                              tm.icono,
                              tm.color,
                              tm.nombre,
                              COALESCE(SUM(apm.monto), 0) AS total
                           FROM 
                              tipos_movimiento tm
                           LEFT JOIN metas m ON m.usuario_id = ?
                           LEFT JOIN aportes_metas apm ON apm.meta_id = m.id 
                              AND MONTH(apm.fecha_creacion) = ?
                           WHERE
                              tm.id = 3
                           GROUP BY 
                              tm.id, tm.icono, tm.color, tm.nombre
                           ORDER BY 
                              tm.nombre;
                           """;
            
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, usuario_id);
            pstm.setInt(2, mes);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el resumen de metas");
        }
    }
    
    public static ResultSet getResumenCategoriasDetalle(int usuario_id, int mes, int tipo_movimiento_id) {
        Connection connection = ConnectionDB.connect();
        try {
            String query = """
                           SELECT 
                               cat.id,
                               tm.id as tipo_movimiento_id,
                               cat.icono,
                               cat.nombre,
                               tm.color,
                               tm.color_bg,
                               COUNT(*) AS cantidad,
                               SUM(m.monto) AS total
                           FROM 
                               movimientos m
                           JOIN categorias cat ON m.categoria_id = cat.id
                           JOIN tipos_movimiento tm ON cat.tipo_movimiento_id = tm.id
                           WHERE 
                               m.usuario_id = ?
                               AND tm.id = ?
                               AND MONTH(m.fecha_creacion) = ?
                           GROUP BY 
                               cat.id, tm.id, cat.icono, cat.nombre, tm.color, tm.color_bg
                           ORDER BY 
                               total DESC;
                           """;
            
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, usuario_id);
            pstm.setInt(2, tipo_movimiento_id);
            pstm.setInt(3, mes);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el resumen detallado de categorías");
        }
    }
    
    public static ResultSet getResumenMetasDetalle(int usuario_id, int mes) {
        Connection connection = ConnectionDB.connect();
        try {
            String query = """
                           SELECT 
                               m.id,
                               tm.id as tipo_movimiento_id,
                               tm.icono,
                               tm.color,
                               tm.color_bg,
                               m.nombre,
                               COUNT(*) AS cantidad,
                               SUM(apm.monto) AS total
                           FROM 
                               aportes_metas apm
                           JOIN metas m ON apm.meta_id = m.id
                           JOIN tipos_movimiento tm ON tm.id = 3
                           WHERE 
                               m.usuario_id = ?
                               AND MONTH(apm.fecha_creacion) = ?
                           GROUP BY 
                                m.id, tm.icono, tm.color, tm.color_bg, m.nombre
                           ORDER BY 
                               total DESC;
                           """;
            
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, usuario_id);
            pstm.setInt(2, mes);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el resumen detallado de metas");
        }
    }
}