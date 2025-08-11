package MODELO;

import MODELO.Movimiento;  // Importa clase modelo Movimiento para mapear datos
import DATABASE.ConnectionDB;  // Importa clase para conexión a BD
import java.sql.Connection;  // Importa conexión SQL
import java.sql.PreparedStatement;  // Importa para consultas preparadas
import java.sql.ResultSet;  // Importa para resultados de consultas
import java.sql.SQLException;  // Importa excepción SQL

public class MovimientoDao {

    // Obtiene todos los movimientos sin filtro
    public static ResultSet getMovimientos() {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM movimientos");  // Prepara consulta
            return pstm.executeQuery();  // Ejecuta y devuelve resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener los movimientos");  // Manejo básico de error
        }
    }
    
    // Obtiene la cantidad total de movimientos
    public static ResultSet getCantidadMovimientos() {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(*) AS cantidad FROM movimientos");  // Consulta cantidad
            ResultSet respuesta = pstm.executeQuery();  // Ejecuta consulta
            return respuesta;  // Retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener la cantidad de movimientos");  // Error
        }
    }

    // Obtiene un movimiento específico por su ID, solo activos (estado_id = 1)
    public static ResultSet getMovimientoById(int id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM movimientos WHERE id = ? AND estado_id = 1");  // Prepara consulta con filtro
            pstm.setInt(1, id);  // Asigna ID
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener el movimiento");  // Error
        }
    }
    
    // Obtiene un movimiento por ID y usuario, incluyendo información del tipo de movimiento (join con categorías y tipos)
    public static ResultSet getMovimientosByUserId(int id, int usuario_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            String query = """
                           SELECT 
                           	tm.id AS tipo_movimiento_id,
                           	m.*
                           FROM movimientos AS m
                           JOIN categorias AS cat ON cat.id = m.categoria_id
                           JOIN tipos_movimiento AS tm ON tm.id = cat.tipo_movimiento_id
                           WHERE m.id = ? AND m.usuario_id = ?;
                           """;
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta compleja
            pstm.setInt(1,  id);  // Asigna ID movimiento
            pstm.setInt(2, usuario_id);  // Asigna ID usuario
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener el movimiento");  // Error
        }
    }
    
    // Obtiene movimientos filtrados por categoría, usuario, tipo de movimiento y mes
    public static ResultSet getMovimientosDetailsByCategoria(int categoria_id, int usuario_id, int tipo_movimiento_id, int mes) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        
        String query =  """
                        SELECT 
                            m.id,
                            cat.icono,
                            cat.nombre as categoria,
                            tm.color,
                            tm.color_bg,
                            m.nombre,
                            m.fecha_creacion,
                            m.monto
                        FROM movimientos m
                        JOIN categorias cat ON m.categoria_id = cat.id
                        JOIN tipos_movimiento tm ON cat.tipo_movimiento_id = tm.id
                        WHERE 
                            m.usuario_id = ?
                            AND cat.id = ?
                            AND tm.id = ?
                            AND MONTH(m.fecha_creacion) = ?
                            AND m.estado_id = 1
                        ORDER BY m.fecha_creacion desc
                        """;
        try {
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta con múltiples filtros
            pstm.setInt(1, usuario_id);  // Usuario
            pstm.setInt(2, categoria_id);  // Categoría
            pstm.setInt(3, tipo_movimiento_id);  // Tipo movimiento
            pstm.setInt(4, mes);  // Mes (numérico)
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener los movimientos");  // Error
        }
    }
    
    // Obtiene movimientos resumidos para un usuario, tipo de movimiento y mes específico
    public static ResultSet getMovimientoResumidos(int usuario_id, int tipo_movimiento_id, int mes) {
        Connection connection = ConnectionDB.connect();  // Abre conexión

        String query = """
                        SELECT 
                            m.id,
                            tm.color,
                            m.nombre,
                            m.fecha_creacion
                        FROM movimientos m
                        JOIN categorias cat ON m.categoria_id = cat.id
                        JOIN tipos_movimiento tm ON cat.tipo_movimiento_id = tm.id
                        WHERE 
                            m.usuario_id = ?
                            AND tm.id = ?
                            AND MONTH(m.fecha_creacion) = ?
                            AND m.estado_id = 1
                        ORDER BY m.fecha_creacion desc;
                       """;
        
        try {
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta resumen
            pstm.setInt(1, usuario_id);
            pstm.setInt(2, tipo_movimiento_id);
            pstm.setInt(3, mes);
            return pstm.executeQuery();  // Ejecuta y devuelve resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener los movimientos");  // Error
        }
    }
    
    // Obtiene movimientos filtrados por usuario, tipo y fecha exacta
    public static ResultSet getMovimientosByDate(int usuario_id, int tipo_movimiento_id, String fecha) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = """
                       SELECT 
                       	m.id,
                           m.nombre,
                           m.monto,
                           c.nombre as categoria,
                           tm.icono,
                           tm.color,
                           tm.color_bg,
                           m.fecha_creacion
                       FROM movimientos m
                       INNER JOIN categorias c on c.id = m.categoria_id
                       INNER JOIN tipos_movimiento tm on tm.id = c.tipo_movimiento_id
                       WHERE 
                       	m.usuario_id = ?
                           AND tm.id = ?
                           AND DATE(m.fecha_creacion) = ?
                           AND m.estado_id = 1
                       ORDER BY m.id DESC;
                       """;
        try {
            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta con fecha exacta
            pstm.setInt(1, usuario_id);
            pstm.setInt(2, tipo_movimiento_id);
            pstm.setString(3, fecha);
            return pstm.executeQuery();  // Ejecuta y retorna resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener los movimientos");  // Error
        }
    }
    
        // Obtiene todos los movimientos sin filtro
    public static ResultSet getMovimientosBYCategoriaId(int categoria_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM movimientos where categoria_id = ?");  // Prepara consulta
            pstm.setInt(1, categoria_id);
            return pstm.executeQuery();  // Ejecuta y devuelve resultado
        } catch (SQLException e) {
            throw new Error("Error al obtener los movimientos");  // Manejo básico de error
        }
    }

    // Inserta un nuevo movimiento, devuelve el id generado
    public static ResultSet createMovimiento(Movimiento movimientoData) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "INSERT INTO movimientos (usuario_id, nombre, monto, descripcion, categoria_id) VALUES (?, ?, ?, ?, ?)";  // SQL insert
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, movimientoData.getUsuario_id());  // Usuario dueño
            pstm.setString(2, movimientoData.getNombre());  // Nombre movimiento
            pstm.setBigDecimal(3, movimientoData.getMonto());  // Monto
            pstm.setString(4, movimientoData.getDescripcion());  // Descripción
            pstm.setInt(5, movimientoData.getCategoria_id());  // Categoría
            pstm.executeUpdate();  // Ejecuta insert
            return pstm.getGeneratedKeys();  // Retorna llave generada (id)
        } catch (SQLException e) {
            throw new Error("Error al crear el movimiento");  // Error
        }
    }
    
    // Inserta movimiento con fecha de creación específica, devuelve id generado
    public static ResultSet createMovimientoDate(Movimiento movimientoData) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "INSERT INTO movimientos (usuario_id, nombre, monto, descripcion, categoria_id, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?)";  // SQL insert con fecha
        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, movimientoData.getUsuario_id());  // Usuario
            pstm.setString(2, movimientoData.getNombre());  // Nombre
            pstm.setBigDecimal(3, movimientoData.getMonto());  // Monto
            pstm.setString(4, movimientoData.getDescripcion());  // Descripción
            pstm.setInt(5, movimientoData.getCategoria_id());  // Categoría
            pstm.setString(6, movimientoData.getFecha_creacion());  // Fecha específica
            pstm.executeUpdate();  // Ejecuta
            return pstm.getGeneratedKeys();  // Retorna llave generada
        } catch (SQLException e) {
            throw new Error("Error al crear el movimiento");  // Error
        }
    }

    // Actualiza un movimiento según id y usuario
    public static int updateMovimiento(int id, int usuario_id, Movimiento movimientoData) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "UPDATE movimientos SET nombre = ?, monto = ?, descripcion = ?, categoria_id = ? WHERE id = ? and usuario_id = ?";  // SQL update
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, movimientoData.getNombre());  // Nuevo nombre
            pstm.setBigDecimal(2, movimientoData.getMonto());  // Nuevo monto
            pstm.setString(3, movimientoData.getDescripcion());  // Nueva descripción
            pstm.setInt(4, movimientoData.getCategoria_id());  // Nueva categoría
            pstm.setInt(5, id);  // ID del movimiento
            pstm.setInt(6, usuario_id);  // Usuario dueño
            return pstm.executeUpdate();  // Ejecuta update y devuelve filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al actualizar el movimiento");  // Error
        }
    }
    
    // Eliminación lógica (soft delete) de un movimiento cambiando su estado
    public static int softDeleteMovimiento(int id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        String query = "UPDATE movimientos SET estado_id = 2 WHERE id = ?";  // Cambio estado a 2 (inactivo)
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, id);  // ID movimiento a borrar lógicamente
            int affectedRow = pstm.executeUpdate();  // Ejecuta update y devuelve filas afectadas
            return affectedRow;  // Retorna filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al eliminar de forma segura el movimiento");  // Error
        }
    }

    // Eliminación física definitiva de un movimiento por id y usuario
    public static int deleteMovimiento(int id, int usuario_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM movimientos WHERE id = ? and usuario_id = ?");  // Delete con filtros
            pstm.setInt(1, id);  // ID
            pstm.setInt(2, usuario_id);  // Usuario dueño
            return pstm.executeUpdate();  // Ejecuta y devuelve filas afectadas
        } catch (SQLException e) {
            throw new Error("Error al eliminar el movimiento");  // Error
        }
    }
}
