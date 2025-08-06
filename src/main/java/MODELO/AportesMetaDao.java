package MODELO;

import MODELO.AportesMeta;
import DATABASE.ConnectionDB;

import java.sql.*;

public class AportesMetaDao {

    public static ResultSet getAportes() {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM aportes_metas");
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes");
        }
    }
    
    public static ResultSet getCantidadAportes() {
        
        Connection connection = ConnectionDB.connect();
        
        try {

            PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(*) AS cantidad FROM aportes_metas");
            ResultSet respuesta = pstm.executeQuery();
            
            return respuesta;
            
        } catch (SQLException e) {
            throw new Error("Error al obtener la cantidad de aportes");
        }
    }

    public static ResultSet getAportesByMetaId(int meta_id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM aportes_metas WHERE meta_id = ? AND estado_id = 1 ORDER BY fecha_creacion DESC");
            pstm.setInt(1, meta_id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes de la meta");
        }
    }

    public static ResultSet getAporteById(int id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM aportes_metas WHERE id = ? AND estado_id = 1");
            pstm.setInt(1, id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener el aporte");
        }
    }
    
    public static ResultSet getAportesDetalladosByParametros(int meta_id, int usuario_id, int mes) {
        Connection connection = ConnectionDB.connect();
        String query = """
            SELECT 
                apm.id,
                m.id AS meta_id,
                tm.icono,
                tm.color,
                tm.color_bg,
                m.nombre,
                apm.fecha_creacion,
                apm.monto
            FROM 
                aportes_metas apm
            JOIN metas m ON apm.meta_id = m.id
            JOIN tipos_movimiento tm 
            WHERE 
                m.id = ?
                AND tm.id = 3
                AND m.usuario_id = ?
                AND MONTH(apm.fecha_creacion) = ?
                AND apm.estado_id = 1
            GROUP BY 
                apm.id, m.id, tm.icono, tm.color, tm.color_bg, m.nombre, apm.fecha_creacion, apm.monto
            ORDER BY 
                apm.fecha_creacion DESC
        """;

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, meta_id);
            pstm.setInt(2, usuario_id);
            pstm.setInt(3, mes);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes detallados");
        }
    }
    
    public static ResultSet getAportesDetalladosByMeta(int meta_id, int usuario_id) {
        Connection connection = ConnectionDB.connect();
        String query = """
            SELECT 
                apm.id,
                m.id AS meta_id,
                tm.icono,
                tm.color,
                tm.color_bg,
                m.nombre,
                apm.fecha_creacion,
                apm.monto
            FROM 
                aportes_metas apm
            JOIN metas m ON apm.meta_id = m.id
            JOIN tipos_movimiento tm 
            WHERE 
                m.id = ?
                AND tm.id = 3
                AND m.usuario_id = ?
                AND apm.estado_id = 1
            GROUP BY 
                apm.id, m.id, tm.icono, tm.color, tm.color_bg, m.nombre, apm.fecha_creacion, apm.monto
            ORDER BY 
                apm.fecha_creacion DESC
        """;

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, meta_id);
            pstm.setInt(2, usuario_id);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes detallados");
        }
    }
    
    public static ResultSet getAportesResumidos(int usuario_id, int mes) {
        Connection connection = ConnectionDB.connect();
        String query = """
                       SELECT 
                        apm.id,
                        m.nombre,
                        tm.color,
                        apm.fecha_creacion
                       FROM aportes_metas AS apm
                       INNER JOIN metas AS m ON m.id = apm.meta_id
                       INNER JOIN tipos_movimiento AS tm ON tm.id = 3
                       WHERE
                       	m.usuario_id = ?
                        AND MONTH(apm.fecha_creacion) = ?
                        AND apm.estado_id = 1
                       ORDER BY
                       	apm.id DESC;
                       """;
        
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, usuario_id);
            pstm.setInt(2, mes);
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes resumidos");
        }
    }
    
    public static ResultSet getAportesByDate(int usuario_id, String fecha) {
        Connection connection = ConnectionDB.connect();
        String query = """
                       SELECT 
                           apm.id,
                           m.nombre,
                           apm.monto,
                           tm.icono,
                           tm.color,
                           tm.color_bg,
                           apm.fecha_creacion
                        FROM aportes_metas apm
                        INNER JOIN metas m on m.id = apm.meta_id
                        INNER JOIN tipos_movimiento tm on tm.id = 3
                        WHERE 
                            m.usuario_id = ?
                           AND DATE(m.fecha_creacion) = ?
                           AND m.estado_id = 1
                           AND apm.estado_id = 1
                        ORDER BY
                        m.id DESC;
                       """;
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, usuario_id);
            pstm.setString(2, fecha);
           
            return pstm.executeQuery();
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes");
        }
    }

    public static ResultSet createAporte(AportesMeta aporte) {
        Connection connection = ConnectionDB.connect();
        String query = "INSERT INTO aportes_metas (meta_id, monto, descripcion) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, aporte.getMeta_id());
            pstm.setBigDecimal(2, aporte.getMonto());
            pstm.setString(3, aporte.getDescripcion());
            pstm.executeUpdate();
            return pstm.getGeneratedKeys();
        } catch (SQLException e) {
            throw new Error("Error al crear el aporte");
        }
    }

    public static int updateAporte(int id, int meta_id, AportesMeta aporte) {
        Connection connection = ConnectionDB.connect();
        String query = "UPDATE aportes_metas SET monto = ?, descripcion = ? WHERE id = ? AND meta_id = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setBigDecimal(1, aporte.getMonto());
            pstm.setString(2, aporte.getDescripcion());
            pstm.setInt(3, id);
            pstm.setInt(4, meta_id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al actualizar el aporte");
        }
    }
    
    public static int softDeleteAportes(int id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos
        
        String query = "UPDATE aportes_metas SET estado_id = 2 WHERE id = ?";
        
        try {
            
            PreparedStatement pstm = connection.prepareStatement(query);
            
            // Establece los nuevos valores del usuario en la consulta
            pstm.setInt(1, id);
            
            int affectedRow = pstm.executeUpdate(); // Ejecuta la actualización y obtiene el número de filas afectadas
            
            return affectedRow; // Devuelve el número de filas afectadas
            
        } catch (SQLException e) {
            throw new Error("Error al eliminar de forma segura el movimiento");
        }
    }

    public static int deleteAporte(int id, int meta_id) {
        Connection connection = ConnectionDB.connect();
        String query = "DELETE FROM aportes_metas WHERE id = ? AND meta_id = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, id);
            pstm.setInt(2, meta_id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al eliminar el aporte");
        }
    }

    // Para eliminar TODOS los aportes de una meta (útil cuando eliminas la meta completa)
    public static int deleteAllAportesByMetaId(int meta_id) {
        Connection connection = ConnectionDB.connect();
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM aportes_metas WHERE meta_id = ?");
            pstm.setInt(1, meta_id);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Error al eliminar los aportes de la meta");
        }
    }
}