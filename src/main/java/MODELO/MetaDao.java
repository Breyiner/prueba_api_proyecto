package MODELO;

import MODELO.Meta;  // Importa la clase modelo Meta para manejar objetos Meta
import DATABASE.ConnectionDB;  // Importa clase para conexión a la base de datos

import java.sql.*;  // Importa clases necesarias para SQL

public class MetaDao {

    // Método para obtener todas las metas sin filtro
    public static ResultSet getMetas() {
        Connection connection = ConnectionDB.connect();  // Abre conexión a BD
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM metas");  // Prepara consulta sin filtro
            return pstm.executeQuery();  // Ejecuta consulta y devuelve resultados
        } catch (SQLException e) {
            throw new Error("Error al obtener las metas");  // Error si falla consulta
        }
    }
    
    // Método para obtener la cantidad total de metas registradas
    public static ResultSet getCantidadMetas() {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(*) AS cantidad FROM metas");  // Cuenta todas las metas
            ResultSet respuesta = pstm.executeQuery();  // Ejecuta consulta
            return respuesta;  // Devuelve resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener la cantidad de metas");  // Manejo error
        }
    }
    
    // Método para obtener metas junto con el total aportado para cada una, filtrando por usuario activo
    public static ResultSet getMetasConTotal(int usuario_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        
        // Consulta que une metas y aportes, suma aportes activos, usa LEFT JOIN para incluir metas sin aportes
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
                                AND apm.estado_id = 1
                           WHERE m.usuario_id = ?
                                AND m.estado_id = 1
                           GROUP BY
                               m.id, m.nombre, m.monto, m.fecha_limite, m.fecha_creacion, m.completada
                           ORDER BY
                               m.fecha_creacion, m.completada;
                           """;
            
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta
            pstm.setInt(1, usuario_id);  // Asigna parámetro usuario_id
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            System.out.println(e);  // Imprime error para debug
            throw new Error("Error al obtener las metas");  // Lanza error personalizado
        }
    }
    
    // Método para obtener el total aportado y estado completado para una meta específica
    public static ResultSet getMetaTotales(int meta_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        
        try {
            String query = """
                           SELECT 
                               m.id,
                               m.monto,
                               m.completada,
                               COALESCE(SUM(apm.monto), 0) AS total
                           FROM metas AS m
                           LEFT JOIN aportes_metas AS apm ON m.id = apm.meta_id
                           WHERE m.id = ?
                                AND m.estado_id = 1
                                AND apm.estado_id = 1;
                           """;
            
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta
            pstm.setInt(1, meta_id);  // Asigna parámetro meta_id
            return pstm.executeQuery();  // Ejecuta y devuelve resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener la meta");  // Error personalizado
        }
    }
    
    // Método para obtener una meta específica con la cantidad y suma de aportes, filtrando por id y usuario
    public static ResultSet getMetasCantMovimientos(int id, int usuario_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        
        try {
            String query = """
                           SELECT 
                               m.id,
                               m.nombre,
                               m.descripcion,
                               m.monto,
                               m.fecha_creacion,
                               m.completada,
                               m.fecha_limite,
                               COUNT(a.id) AS cantidad_aportes,
                               COALESCE(SUM(a.monto), 0) AS total
                           FROM metas m
                           LEFT JOIN aportes_metas a ON 
                               m.id = a.meta_id
                               AND a.estado_id = 1
                           WHERE 
                               m.id = ?
                               AND m.usuario_id = ?
                               AND m.estado_id = 1
                           GROUP BY 
                               m.id, m.nombre, m.descripcion, m.monto, m.fecha_creacion, m.completada, m.fecha_limite;
                           """;
            
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta
            pstm.setInt(1, id);  // Asigna id meta
            pstm.setInt(2, usuario_id);  // Asigna usuario_id
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener las metas");  // Error
        }
    }

    // Método para obtener una meta específica por su id, solo activas
    public static ResultSet getMetaById(int id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM metas WHERE id = ? AND estado_id = 1");  // Consulta con filtro id y activo
            pstm.setInt(1, id);  // Asigna id
            return pstm.executeQuery();  // Ejecuta y devuelve resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener la meta");  // Error
        }
    }

    // Método para crear una meta nueva y devolver el id generado
    public static ResultSet createMeta(Meta meta) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "INSERT INTO metas (usuario_id, nombre, monto, descripcion, fecha_limite) VALUES (?, ?, ?, ?, ?)";  // SQL insert
        
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, meta.getUsuario_id());  // Asigna usuario_id
            pstm.setString(2, meta.getNombre());  // Asigna nombre
            pstm.setBigDecimal(3, meta.getMonto());  // Asigna monto
            pstm.setString(4, meta.getDescripcion());  // Asigna descripción
            pstm.setString(5, meta.getFecha_limite());  // Asigna fecha límite
            pstm.executeUpdate();  // Ejecuta insert
            return pstm.getGeneratedKeys();  // Retorna llave generada (id)
        } catch (SQLException e) {
            throw new Error("Error al crear la meta");  // Error
        }
    }

    // Método para actualizar una meta existente filtrando por id y usuario_id
    public static int updateMeta(int id, int usuario_id, Meta meta) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "UPDATE metas SET nombre = ?, monto = ?, descripcion = ?, fecha_limite = ? WHERE id = ? AND usuario_id = ?";  // SQL update
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, meta.getNombre());  // Nuevo nombre
            pstm.setBigDecimal(2, meta.getMonto());  // Nuevo monto
            pstm.setString(3, meta.getDescripcion());  // Nueva descripción
            pstm.setString(4, meta.getFecha_limite());  // Nueva fecha límite
            pstm.setInt(5, id);  // Id meta a actualizar
            pstm.setInt(6, usuario_id);  // Usuario dueño de la meta
            return pstm.executeUpdate();  // Ejecuta y devuelve filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al actualizar la meta");  // Error
        }
    }

    // Método para actualizar el estado de completada (true/false) de una meta
    public static int updateCompletada(int id, boolean completada) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "UPDATE metas SET completada = ? WHERE id = ?";  // Update completada
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setBoolean(1, completada);  // Estado completada nuevo
            pstm.setInt(2, id);  // Id meta a actualizar
            return pstm.executeUpdate();  // Ejecuta y retorna filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al actualizar el estado de completada");  // Error
        }
    }
    
    // Método para eliminación lógica (soft delete) de una meta
    public static int softDeleteMeta(int id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "UPDATE metas SET estado_id = 2 WHERE id = ?";  // Cambia estado a 2 (inactivo)
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, id);  // Id meta a eliminar lógicamente
            int affectedRow = pstm.executeUpdate();  // Ejecuta y obtiene filas afectadas
            return affectedRow;  // Devuelve filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al eliminar de forma segura el movimiento");  // Error
        }
    }

    // Método para eliminar una meta físicamente (borrado definitivo)
    public static int deleteMeta(int id, int usuario_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM metas WHERE id = ? AND usuario_id = ?");  // Delete con filtros
            pstm.setInt(1, id);  // Id meta
            pstm.setInt(2, usuario_id);  // Usuario dueño
            return pstm.executeUpdate();  // Ejecuta y retorna filas afectadas
        } catch (SQLException e) {
            System.out.println(e);  // Imprime error para debug
            throw new Error("Error al eliminar la meta");  // Error
        }
    }
}
