package MODELO;  // Paquete donde se encuentra la clase

import DATABASE.ConnectionDB;  // Importa conexión a base de datos
import java.sql.*;  // Importa clases SQL necesarias

public class DashboardDao {  // DAO para obtener datos del dashboard (resúmenes)

    // Método para obtener resumen de movimientos (ingresos, egresos) de un usuario por mes
    public static ResultSet getResumenMovimientos(int usuario_id, int mes) {
        Connection connection = ConnectionDB.connect();  // Abre conexión a BD
        try {
            String query = """
                           SELECT 
                             tm.icono,        -- Ícono del tipo de movimiento
                             tm.color,        -- Color asociado
                             tm.nombre,       -- Nombre del tipo movimiento (ej. ingreso, gasto)
                             COALESCE(SUM(m.monto), 0) AS total  -- Suma total monto (0 si no hay)
                           FROM 
                             tipos_movimiento tm
                           LEFT JOIN categorias c ON c.tipo_movimiento_id = tm.id  -- Categorías asociadas
                           LEFT JOIN movimientos m ON 
                               m.categoria_id = c.id 
                               AND m.usuario_id = ?                -- Filtra por usuario
                               AND MONTH(m.fecha_creacion) = ?    -- Filtra por mes
                               AND m.estado_id = 1                 -- Solo movimientos activos
                           WHERE
                             tm.id != 3  -- Excluye tipo de movimiento id=3 (metas)
                           GROUP BY 
                             tm.id, tm.icono, tm.color, tm.nombre  -- Agrupa para sumar
                           ORDER BY 
                             tm.id;  -- Ordena por id de tipo movimiento
                           """;

            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara la consulta
            pstm.setInt(1, usuario_id);  // Parámetro usuario_id
            pstm.setInt(2, mes);          // Parámetro mes
            return pstm.executeQuery();   // Ejecuta y retorna ResultSet
        } catch (SQLException e) {
            System.out.println(e);       // Imprime error en consola para debugging
            throw new Error("Error al obtener el resumen de movimientos");  // Propaga error
        }
    }

    // Método para obtener resumen de metas con sus aportes por usuario y mes
    public static ResultSet getResumenMetas(int usuario_id, int mes) {
        Connection connection = ConnectionDB.connect();  // Conexión a BD
        try {
            String query = """
                           SELECT 
                               tm.icono,               -- Ícono del tipo movimiento
                               tm.color,               -- Color asociado
                               tm.nombre,              -- Nombre (siempre 'Metas' en este caso)
                               COALESCE(SUM(apm.monto), 0) AS total  -- Suma de aportes (0 si no hay)
                           FROM 
                               tipos_movimiento tm
                           LEFT JOIN metas m ON 
                               m.usuario_id = ?         -- Filtra metas por usuario
                               AND m.estado_id = 1      -- Solo activas
                           LEFT JOIN aportes_metas apm ON 
                               apm.meta_id = m.id
                               AND MONTH(apm.fecha_creacion) = ?  -- Filtra aportes por mes
                               AND apm.estado_id = 1    -- Solo activos
                           WHERE
                               tm.id = 3  -- Solo tipo_movimiento para metas
                           GROUP BY 
                               tm.id, tm.icono, tm.color, tm.nombre
                           ORDER BY 
                               tm.nombre;
                           """;

            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta
            pstm.setInt(1, usuario_id);  // Parámetro usuario_id
            pstm.setInt(2, mes);          // Parámetro mes
            return pstm.executeQuery();   // Ejecuta y retorna ResultSet
        } catch (SQLException e) {
            throw new Error("Error al obtener el resumen de metas");  // Error manejado
        }
    }
    
    // Método para obtener resumen detallado por categorías (cantidad y total) según tipo de movimiento
    public static ResultSet getResumenCategoriasDetalle(int usuario_id, int mes, int tipo_movimiento_id) {
        Connection connection = ConnectionDB.connect();  // Abre conexión
        try {
            String query = """
                           SELECT 
                               cat.id,              -- ID categoría
                               tm.id as tipo_movimiento_id,  -- ID tipo movimiento
                               cat.icono,           -- Icono categoría
                               cat.nombre,          -- Nombre categoría
                               tm.color,            -- Color tipo movimiento
                               tm.color_bg,         -- Color de fondo tipo movimiento
                               COUNT(*) AS cantidad, -- Cantidad de movimientos
                               SUM(m.monto) AS total  -- Total acumulado
                           FROM 
                               movimientos m
                           JOIN categorias cat ON m.categoria_id = cat.id  -- Relaciona con categorías
                           JOIN tipos_movimiento tm ON cat.tipo_movimiento_id = tm.id  -- Relaciona tipo movimiento
                           WHERE 
                               m.usuario_id = ?                    -- Filtra usuario
                               AND tm.id = ?                      -- Filtra tipo movimiento
                               AND MONTH(m.fecha_creacion) = ?   -- Filtra mes
                               AND m.estado_id = 1                -- Solo activos
                           GROUP BY 
                               cat.id, tm.id, cat.icono, cat.nombre, tm.color, tm.color_bg  -- Agrupa por campos para resumen
                           ORDER BY 
                               total DESC;  -- Ordena descendente por total
                           """;

            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta
            pstm.setInt(1, usuario_id);           // Usuario
            pstm.setInt(2, tipo_movimiento_id);   // Tipo movimiento
            pstm.setInt(3, mes);                   // Mes
            return pstm.executeQuery();            // Ejecuta y retorna ResultSet
        } catch (SQLException e) {
            throw new Error("Error al obtener el resumen detallado de categorías");  // Manejo de error
        }
    }
    
    // Método para obtener resumen detallado de metas con cantidad y total aportado
    public static ResultSet getResumenMetasDetalle(int usuario_id, int mes) {
        Connection connection = ConnectionDB.connect();  // Conexión BD
        try {
            String query = """
                           SELECT 
                               m.id,                   -- ID meta
                               tm.id as tipo_movimiento_id,  -- Tipo movimiento (3 para metas)
                               tm.icono,               -- Ícono
                               tm.color,               -- Color
                               tm.color_bg,            -- Fondo color
                               m.nombre,               -- Nombre meta
                               COUNT(*) AS cantidad,   -- Cantidad de aportes
                               SUM(apm.monto) AS total  -- Total aportado
                           FROM 
                               aportes_metas apm
                           JOIN metas m ON apm.meta_id = m.id  -- Une con metas
                           JOIN tipos_movimiento tm ON tm.id = 3  -- Solo tipo movimiento metas
                           WHERE 
                               m.usuario_id = ?                  -- Filtra usuario
                               AND MONTH(apm.fecha_creacion) = ? -- Filtra mes aportes
                               AND apm.estado_id = 1             -- Solo activos
                               AND m.estado_id = 1               -- Metas activas
                           GROUP BY 
                               m.id, tm.icono, tm.color, tm.color_bg, m.nombre  -- Agrupa para resumen
                           ORDER BY 
                               total DESC;  -- Ordena por total descendente
                           """;

            PreparedStatement pstm = connection.prepareStatement(query);  // Prepara consulta
            pstm.setInt(1, usuario_id);  // Parámetro usuario
            pstm.setInt(2, mes);          // Parámetro mes
            return pstm.executeQuery();   // Ejecuta y retorna ResultSet
        } catch (SQLException e) {
            throw new Error("Error al obtener el resumen detallado de metas");  // Manejo error
        }
    }
}
