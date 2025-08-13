package MODELO; // Define que esta clase pertenece al paquete MODELO, que contiene clases de acceso a datos y entidades.

import MODELO.Meta; // Importa la clase que representa la entidad Meta en el sistema.
import DATABASE.ConnectionDB; // Importa la clase que maneja la conexión a la base de datos.
import java.sql.*; // Importa clases necesarias para operaciones SQL (Connection, PreparedStatement, ResultSet, SQLException).

public class MetaDao { // Clase que gestiona las operaciones de acceso a datos para la entidad Meta.

    /**
     * Método para obtener todas las metas registradas en la base de datos, sin filtros.
     * Ejecuta una consulta SQL para seleccionar todos los registros de la tabla 'metas'.
     * @return ResultSet con los datos de todas las metas o lanza una excepción si ocurre un error.
     */
    public static ResultSet getMetas() {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM metas"); // Prepara la consulta SQL.
            return pstm.executeQuery(); // Ejecuta la consulta y devuelve los resultados.
        } catch (SQLException e) {
            throw new Error("Error al obtener las metas"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener la cantidad total de metas registradas.
     * Ejecuta una consulta SQL para contar todos los registros en la tabla 'metas'.
     * @return ResultSet con el conteo de metas o lanza una excepción si ocurre un error.
     */
    public static ResultSet getCantidadMetas() {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(*) AS cantidad FROM metas"); // Prepara la consulta para contar metas.
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados.
            return respuesta; // Devuelve el ResultSet con el conteo.
        } catch (SQLException e) {
            throw new Error("Error al obtener la cantidad de metas"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener metas de un usuario activo con el total aportado.
     * Une la tabla 'metas' con 'aportes_metas' usando LEFT JOIN para incluir metas sin aportes.
     * @param usuario_id Identificador único del usuario.
     * @return ResultSet con los datos de las metas y el total aportado o lanza una excepción si ocurre un error.
     */
    public static ResultSet getMetasConTotal(int usuario_id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        // Consulta SQL con LEFT JOIN para obtener metas activas y suma de aportes.
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
            PreparedStatement pstm = connection.prepareStatement(query); // Prepara la consulta SQL.
            pstm.setInt(1, usuario_id); // Asigna el ID del usuario.
            return pstm.executeQuery(); // Ejecuta la consulta y devuelve los resultados.
        } catch (SQLException e) {
            System.out.println(e); // Imprime el error para depuración (no recomendado en producción).
            throw new Error("Error al obtener las metas"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener el total aportado y el estado completado de una meta específica.
     * Une la tabla 'metas' con 'aportes_metas' para calcular la suma de aportes activos.
     * @param meta_id Identificador único de la meta.
     * @return ResultSet con los datos de la meta y el total aportado o lanza una excepción si ocurre un error.
     */
    public static ResultSet getMetaTotales(int meta_id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

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
            PreparedStatement pstm = connection.prepareStatement(query); // Prepara la consulta SQL.
            pstm.setInt(1, meta_id); // Asigna el ID de la meta.
            return pstm.executeQuery(); // Ejecuta la consulta y devuelve los resultados.
        } catch (SQLException e) {
            throw new Error("Error al obtener la meta"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener una meta específica con la cantidad y suma de aportes.
     * Une la tabla 'metas' con 'aportes_metas' para contar y sumar aportes activos.
     * @param id Identificador único de la meta.
     * @param usuario_id Identificador único del usuario.
     * @return ResultSet con los datos de la meta, cantidad de aportes y total o lanza una excepción si ocurre un error.
     */
    public static ResultSet getMetasCantMovimientos(int id, int usuario_id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

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
            PreparedStatement pstm = connection.prepareStatement(query); // Prepara la consulta SQL.
            pstm.setInt(1, id); // Asigna el ID de la meta.
            pstm.setInt(2, usuario_id); // Asigna el ID del usuario.
            return pstm.executeQuery(); // Ejecuta la consulta y devuelve los resultados.
        } catch (SQLException e) {
            throw new Error("Error al obtener las metas"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener una meta específica por su ID, solo metas activas.
     * @param id Identificador único de la meta.
     * @return ResultSet con los datos de la meta o lanza una excepción si ocurre un error.
     */
    public static ResultSet getMetaById(int id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.
        try {
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM metas WHERE id = ? AND estado_id = 1"); // Prepara la consulta con filtro de estado activo.
            pstm.setInt(1, id); // Asigna el ID de la meta.
            return pstm.executeQuery(); // Ejecuta la consulta y devuelve los resultados.
        } catch (SQLException e) {
            throw new Error("Error al obtener la meta"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para crear una nueva meta en la base de datos.
     * Inserta un nuevo registro en la tabla 'metas' con los datos proporcionados.
     * @param meta Objeto Meta con los datos de la nueva meta.
     * @return ResultSet con las claves generadas (ID de la meta creada) o lanza una excepción si ocurre un error.
     */
    public static ResultSet createMeta(Meta meta) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.
        String query = "INSERT INTO metas (usuario_id, nombre, monto, descripcion, fecha_limite) VALUES (?, ?, ?, ?, ?)"; // Consulta SQL para insertar una meta.

        try {
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, meta.getUsuario_id()); // Asigna el ID del usuario.
            pstm.setString(2, meta.getNombre()); // Asigna el nombre de la meta.
            pstm.setBigDecimal(3, meta.getMonto()); // Asigna el monto de la meta.
            pstm.setString(4, meta.getDescripcion()); // Asigna la descripción de la meta.
            pstm.setString(5, meta.getFecha_limite()); // Asigna la fecha límite.
            pstm.executeUpdate(); // Ejecuta la inserción.
            return pstm.getGeneratedKeys(); // Devuelve el ResultSet con las claves generadas.
        } catch (SQLException e) {
            throw new Error("Error al crear la meta"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para actualizar una meta existente.
     * Modifica el registro de la meta con el ID y usuario proporcionados.
     * @param id Identificador único de la meta.
     * @param usuario_id Identificador único del usuario.
     * @param meta Objeto Meta con los nuevos datos.
     * @return Número de filas afectadas por la actualización (0 si no se encontró la meta).
     */
    public static int updateMeta(int id, int usuario_id, Meta meta) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.
        String query = "UPDATE metas SET nombre = ?, monto = ?, descripcion = ?, fecha_limite = ? WHERE id = ? AND usuario_id = ?"; // Consulta SQL para actualizar la meta.

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, meta.getNombre()); // Nuevo nombre.
            pstm.setBigDecimal(2, meta.getMonto()); // Nuevo monto.
            pstm.setString(3, meta.getDescripcion()); // Nueva descripción.
            pstm.setString(4, meta.getFecha_limite()); // Nueva fecha límite.
            pstm.setInt(5, id); // ID de la meta a actualizar.
            pstm.setInt(6, usuario_id); // ID del usuario dueño.
            return pstm.executeUpdate(); // Ejecuta la actualización y devuelve el número de filas afectadas.
        } catch (SQLException e) {
            throw new Error("Error al actualizar la meta"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para actualizar el estado de completada de una meta.
     * Modifica el campo 'completada' de la meta con el ID proporcionado.
     * @param id Identificador único de la meta.
     * @param completada Nuevo estado de completada (true/false).
     * @return Número de filas afectadas por la actualización (0 si no se encontró la meta).
     */
    public static int updateCompletada(int id, boolean completada) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.
        String query = "UPDATE metas SET completada = ? WHERE id = ?"; // Consulta SQL para actualizar el estado completada.

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setBoolean(1, completada); // Nuevo estado completada.
            pstm.setInt(2, id); // ID de la meta a actualizar.
            return pstm.executeUpdate(); // Ejecuta la actualización y devuelve el número de filas afectadas.
        } catch (SQLException e) {
            throw new Error("Error al actualizar el estado de completada"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para realizar una eliminación lógica (soft delete) de una meta.
     * Actualiza el campo 'estado_id' a 2 para marcar la meta como inactiva.
     * @param id Identificador único de la meta.
     * @return Número de filas afectadas por la actualización (0 si no se encontró la meta).
     */
    public static int softDeleteMeta(int id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.
        String query = "UPDATE metas SET estado_id = 2 WHERE id = ?"; // Consulta SQL para marcar la meta como inactiva.

        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, id); // ID de la meta a eliminar lógicamente.
            int affectedRow = pstm.executeUpdate(); // Ejecuta la actualización y obtiene el número de filas afectadas.
            return affectedRow; // Devuelve el número de filas afectadas.
        } catch (SQLException e) {
            throw new Error("Error al eliminar de forma segura el movimiento"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para eliminar físicamente una meta de la base de datos.
     * Elimina el registro de la meta con el ID y usuario proporcionados.
     * @param id Identificador único de la meta.
     * @param usuario_id Identificador único del usuario.
     * @return Número de filas afectadas por la eliminación (0 si no se encontró la meta).
     */
    public static int deleteMeta(int id, int usuario_id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.
        try {
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM metas WHERE id = ? AND usuario_id = ?"); // Consulta SQL para eliminar la meta.
            pstm.setInt(1, id); // ID de la meta.
            pstm.setInt(2, usuario_id); // ID del usuario dueño.
            return pstm.executeUpdate(); // Ejecuta la eliminación y devuelve el número de filas afectadas.
        } catch (SQLException e) {
            System.out.println(e); // Imprime el error para depuración (no recomendado en producción).
            throw new Error("Error al eliminar la meta"); // Lanza una excepción en caso de error SQL.
        }
    }
}