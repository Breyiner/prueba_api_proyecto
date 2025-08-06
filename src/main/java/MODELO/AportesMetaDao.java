package MODELO;

import MODELO.AportesMeta;  // Importa la clase modelo AportesMeta para usar en métodos que crean o actualizan aportes
import DATABASE.ConnectionDB;  // Importa la clase para conexión a base de datos

import java.sql.*;  // Importa clases SQL para conexión, ejecución de consultas y manejo de resultados

public class AportesMetaDao {

    // Método para obtener todos los aportes sin filtro
    public static ResultSet getAportes() {
        Connection connection = ConnectionDB.connect();  // Abre conexión a base de datos
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM aportes_metas");  // Prepara consulta SQL sin filtros
            return pstm.executeQuery();  // Ejecuta consulta y devuelve el resultado
        } catch (SQLException e) {  // Captura errores de SQL
            throw new Error("Error al obtener los aportes");  // Lanza error personalizado
        }
    }
    
    // Método para obtener la cantidad total de aportes
    public static ResultSet getCantidadAportes() {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(*) AS cantidad FROM aportes_metas");  // Cuenta total de registros
            ResultSet respuesta = pstm.executeQuery();  // Ejecuta consulta
            return respuesta;  // Retorna resultado con la cantidad
        } catch (SQLException e) {
            throw new Error("Error al obtener la cantidad de aportes");
        }
    }

    // Método que devuelve aportes activos filtrando por id de meta, ordenados por fecha (desc)
    public static ResultSet getAportesByMetaId(int meta_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            // Prepara consulta filtrando por meta_id y estado activo
            PreparedStatement pstm = connection.prepareStatement(
                "SELECT * FROM aportes_metas WHERE meta_id = ? AND estado_id = 1 ORDER BY fecha_creacion DESC"
            );
            pstm.setInt(1, meta_id);  // Asigna valor meta_id al parámetro ?
            return pstm.executeQuery();  // Ejecuta consulta
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes de la meta");
        }
    }

    // Método para obtener aporte activo específico por su id
    public static ResultSet getAporteById(int id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            // Prepara consulta para un solo aporte activo por id
            PreparedStatement pstm = connection.prepareStatement(
                "SELECT * FROM aportes_metas WHERE id = ? AND estado_id = 1"
            );
            pstm.setInt(1, id);  // Asigna id
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener el aporte");
        }
    }
    
    // Método para obtener aportes detallados filtrando por meta_id, usuario_id y mes
    public static ResultSet getAportesDetalladosByParametros(int meta_id, int usuario_id, int mes) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        
        // Consulta que une tablas aportes_metas, metas y tipos_movimiento para obtener info detallada
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
                m.id = ?             -- filtro por meta_id
                AND tm.id = 3        -- tipo movimiento fijo (ejemplo)
                AND m.usuario_id = ? -- filtro usuario
                AND MONTH(apm.fecha_creacion) = ? -- filtro mes
                AND apm.estado_id = 1 -- solo activos
            GROUP BY 
                apm.id, m.id, tm.icono, tm.color, tm.color_bg, m.nombre, apm.fecha_creacion, apm.monto
            ORDER BY 
                apm.fecha_creacion DESC
        """;

        try {
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara la consulta con parámetros
            pstm.setInt(1, meta_id);  // Asigna meta_id
            pstm.setInt(2, usuario_id);  // Asigna usuario_id
            pstm.setInt(3, mes);  // Asigna mes
            return pstm.executeQuery();  // Ejecuta y retorna resultados
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes detallados");
        }
    }
    
    // Método para obtener aportes detallados filtrando solo por meta_id y usuario_id (sin filtro de mes)
    public static ResultSet getAportesDetalladosByMeta(int meta_id, int usuario_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        
        // Consulta similar a la anterior pero sin filtro por mes
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
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta
            pstm.setInt(1, meta_id);  // Asigna meta_id
            pstm.setInt(2, usuario_id);  // Asigna usuario_id
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes detallados");
        }
    }
    
    // Método para obtener un resumen de aportes por usuario y mes (solo info básica)
    public static ResultSet getAportesResumidos(int usuario_id, int mes) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        
        // Consulta con joins para obtener nombre de meta, color y fecha creación
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
                m.usuario_id = ?       -- filtro usuario
                AND MONTH(apm.fecha_creacion) = ?  -- filtro mes
                AND apm.estado_id = 1  -- solo activos
            ORDER BY
                apm.id DESC;
        """;
        
        try {
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta
            pstm.setInt(1, usuario_id);  // Asigna usuario_id
            pstm.setInt(2, mes);  // Asigna mes
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes resumidos");
        }
    }
    
    // Método para obtener aportes filtrados por usuario y fecha específica
    public static ResultSet getAportesByDate(int usuario_id, String fecha) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        
        // Consulta con filtros por usuario y fecha exacta, solo activos
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
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta
            pstm.setInt(1, usuario_id);  // Asigna usuario_id
            pstm.setString(2, fecha);  // Asigna fecha (formato YYYY-MM-DD)
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener los aportes");
        }
    }

    // Método para crear un aporte nuevo, retorna el ID generado
    public static ResultSet createAporte(AportesMeta aporte) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "INSERT INTO aportes_metas (meta_id, monto, descripcion) VALUES (?, ?, ?)";  // SQL Insert
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, aporte.getMeta_id());  // Asigna meta_id
            pstm.setBigDecimal(2, aporte.getMonto());  // Asigna monto
            pstm.setString(3, aporte.getDescripcion());  // Asigna descripción
            pstm.executeUpdate();  // Ejecuta insert
            return pstm.getGeneratedKeys();  // Retorna llave generada (id nuevo)
        } catch (SQLException e) {
            throw new Error("Error al crear el aporte");
        }
    }

    // Método para actualizar un aporte existente
    public static int updateAporte(int id, int meta_id, AportesMeta aporte) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "UPDATE aportes_metas SET monto = ?, descripcion = ? WHERE id = ? AND meta_id = ?";  // Update SQL
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setBigDecimal(1, aporte.getMonto());  // Nuevo monto
            pstm.setString(2, aporte.getDescripcion());  // Nueva descripción
            pstm.setInt(3, id);  // ID aporte a actualizar
            pstm.setInt(4, meta_id);  // Meta_id para confirmar pertenencia
            return pstm.executeUpdate();  // Ejecuta y retorna cantidad filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al actualizar el aporte");
        }
    }
    
    // Método para eliminar un aporte de forma lógica (soft delete)
    public static int softDeleteAportes(int id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "UPDATE aportes_metas SET estado_id = 2 WHERE id = ?";  // Cambia estado a 2 (inactivo)
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, id);  // ID aporte a marcar como eliminado
            int affectedRow = pstm.executeUpdate();  // Ejecuta y obtiene número de filas afectadas
            return affectedRow;  // Retorna número filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al eliminar de forma segura el movimiento");
        }
    }

    // Método para eliminar un aporte físicamente (borrado definitivo)
    public static int deleteAporte(int id, int meta_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "DELETE FROM aportes_metas WHERE id = ? AND meta_id = ?";  // Delete SQL con filtros
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, id);  // ID aporte
            pstm.setInt(2, meta_id);  // meta_id para confirmar pertenencia
            return pstm.executeUpdate();  // Ejecuta y retorna filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al eliminar el aporte");
        }
    }

    // Método para eliminar todos los aportes de una meta (usado al eliminar la meta)
    public static int deleteAllAportesByMetaId(int meta_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM aportes_metas WHERE meta_id = ?");  // Delete masivo
            pstm.setInt(1, meta_id);  // meta_id filtro
            return pstm.executeUpdate();  // Ejecuta y retorna filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al eliminar los aportes de la meta");
        }
    }
}
