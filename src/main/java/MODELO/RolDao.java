package MODELO; // Define que esta clase pertenece al paquete MODELO, que contiene clases de acceso a datos y entidades.

import MODELO.Rol; // Importa la clase que representa la entidad Rol en el sistema.
import DATABASE.ConnectionDB; // Importa la clase que maneja la conexión a la base de datos.
import java.sql.Connection; // Clase para manejar conexiones a la base de datos.
import java.sql.PreparedStatement; // Clase para ejecutar consultas SQL parametrizadas.
import java.sql.ResultSet; // Clase para manejar los resultados de las consultas SQL.
import java.sql.SQLException; // Clase para manejar excepciones relacionadas con operaciones SQL.

public class RolDao { // Clase que gestiona las operaciones de acceso a datos para la entidad Rol.

    /**
     * Método para obtener todos los roles registrados en la base de datos.
     * Ejecuta una consulta SQL para seleccionar todos los registros de la tabla 'roles'.
     * @return ResultSet con los datos de todos los roles o lanza una excepción si ocurre un error.
     */
    public static ResultSet getRoles() {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        try {
            // Prepara la consulta SQL para seleccionar todos los roles.
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM roles");
            return pstm.executeQuery(); // Ejecuta la consulta y devuelve los resultados.
        } catch (SQLException e) {
            throw new Error("Error al obtener los roles"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener un rol específico por su ID.
     * Ejecuta una consulta SQL para seleccionar el rol con el ID proporcionado.
     * @param id Identificador único del rol a buscar.
     * @return ResultSet con los datos del rol o lanza una excepción si ocurre un error.
     */
    public static ResultSet getRolById(int id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        try {
            // Prepara la consulta SQL para seleccionar un rol por ID.
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM roles WHERE id = ?");
            pstm.setInt(1, id); // Establece el ID en la consulta parametrizada.
            return pstm.executeQuery(); // Ejecuta la consulta y devuelve los resultados.
        } catch (SQLException e) {
            throw new Error("Error al obtener el rol"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para crear un nuevo rol en la base de datos.
     * Inserta un nuevo registro en la tabla 'roles' con los datos proporcionados.
     * @param rolData Objeto Rol con los datos del nuevo rol.
     * @return ResultSet con las claves generadas (ID del rol creado) o lanza una excepción si ocurre un error.
     */
    public static ResultSet createRol(Rol rolData) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.
        String query = "INSERT INTO roles (nombre) VALUES (?)"; // Consulta SQL para insertar un nuevo rol.

        try {
            // Prepara la consulta SQL para insertar un rol y obtener las claves generadas.
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setString(1, rolData.getNombre()); // Establece el nombre del rol en la consulta.
            pstm.executeUpdate(); // Ejecuta la inserción.
            return pstm.getGeneratedKeys(); // Devuelve el ResultSet con las claves generadas.
        } catch (SQLException e) {
            throw new Error("Error al crear el rol"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para actualizar los datos de un rol existente.
     * Modifica el registro del rol con el ID proporcionado en la tabla 'roles'.
     * @param id Identificador único del rol a actualizar.
     * @param rolData Objeto Rol con los nuevos datos.
     * @return Número de filas afectadas por la actualización (0 si no se encontró el rol).
     */
    public static int updateRol(int id, Rol rolData) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.
        String query = "UPDATE roles SET nombre = ? WHERE id = ?"; // Consulta SQL para actualizar el rol.

        try {
            // Prepara la consulta SQL para actualizar el rol.
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, rolData.getNombre()); // Establece el nuevo nombre del rol.
            pstm.setInt(2, id); // Establece el ID del rol a actualizar.
            return pstm.executeUpdate(); // Ejecuta la actualización y devuelve el número de filas afectadas.
        } catch (SQLException e) {
            throw new Error("Error al actualizar el rol"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para eliminar un rol de la base de datos.
     * Elimina el registro del rol con el ID proporcionado.
     * @param id Identificador único del rol a eliminar.
     * @return Número de filas afectadas por la eliminación (0 si no se encontró el rol).
     */
    public static int deleteRol(int id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        try {
            // Prepara la consulta SQL para eliminar un rol por ID.
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM roles WHERE id = ?");
            pstm.setInt(1, id); // Establece el ID del rol a eliminar.
            return pstm.executeUpdate(); // Ejecuta la eliminación y devuelve el número de filas afectadas.
        } catch (SQLException e) {
            throw new Error("Error al eliminar el rol"); // Lanza una excepción en caso de error SQL.
        }
    }
}