package MODELO; // Define que esta clase pertenece al paquete MODELO, que contiene clases de acceso a datos y entidades.

import MODELO.Usuario; // Importa la clase que representa la entidad Usuario en el sistema.
import DATABASE.ConnectionDB; // Importa la clase que maneja la conexión a la base de datos.
import java.sql.Connection; // Clase para manejar conexiones a la base de datos.
import java.sql.PreparedStatement; // Clase para ejecutar consultas SQL parametrizadas.
import java.sql.ResultSet; // Clase para manejar los resultados de las consultas SQL.
import java.sql.SQLException; // Clase para manejar excepciones relacionadas con operaciones SQL.

public class UsuarioDao { // Clase que gestiona las operaciones de acceso a datos para la entidad Usuario.

    /**
     * Método para obtener todos los usuarios registrados en la base de datos.
     * Ejecuta una consulta SQL para seleccionar todos los registros de la tabla 'usuarios'.
     * @return ResultSet con los datos de todos los usuarios o lanza una excepción si ocurre un error.
     */
    public static ResultSet getUsuarios() {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        try {
            // Prepara la consulta SQL para seleccionar todos los usuarios.
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios");
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados.
            
            return respuesta; // Devuelve el ResultSet con los usuarios.

        } catch (SQLException e) {
            throw new Error("Error al obtener los usuarios"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener todos los usuarios con información adicional de tablas relacionadas.
     * Realiza una consulta SQL con JOINs para incluir datos de roles, géneros, ciudades y estados.
     * @return ResultSet con los datos detallados de los usuarios o lanza una excepción si ocurre un error.
     */
    public static ResultSet getUsuariosTabla() {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        // Consulta SQL con múltiples JOINs para obtener información detallada de los usuarios.
        String query = """
                        SELECT 
                            u.id,
                            r.nombre AS rol,
                            u.nombre,
                            u.apellido,
                            u.correo,
                            g.nombre AS genero,
                            c.nombre AS ciudad,
                            e.nombre AS estado
                        FROM usuarios u
                        RIGHT JOIN roles r ON u.rol_id = r.id
                        RIGHT JOIN estados e ON u.estado_id = e.id
                        RIGHT JOIN generos g ON u.genero_id = g.id
                        RIGHT JOIN ciudades c ON u.ciudad_id = c.id
                        ORDER BY 
                            r.id,
                            e.id,
                            u.id;
                       """;

        try {
            // Prepara la consulta SQL para obtener los usuarios con datos relacionados.
            PreparedStatement pstm = connection.prepareStatement(query);
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados.
            
            return respuesta; // Devuelve el ResultSet con los usuarios detallados.

        } catch (SQLException e) {
            throw new Error("Error al obtener los usuarios"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener la cantidad total de usuarios registrados.
     * Ejecuta una consulta SQL para contar los registros en la tabla 'usuarios'.
     * @return ResultSet con el conteo de usuarios o lanza una excepción si ocurre un error.
     */
    public static ResultSet getCantidadUsuarios() {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        try {
            // Prepara la consulta SQL para contar los usuarios.
            PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(*) AS cantidad FROM usuarios");
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados.
            
            return respuesta; // Devuelve el ResultSet con el conteo.

        } catch (SQLException e) {
            throw new Error("Error al obtener la cantidad de usuarios"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener un usuario específico por su ID.
     * Ejecuta una consulta SQL para seleccionar el usuario con el ID proporcionado.
     * @param id Identificador único del usuario a buscar.
     * @return ResultSet con los datos del usuario o lanza una excepción si ocurre un error.
     */
    public static ResultSet getUsuarioById(int id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        try {
            // Prepara la consulta SQL para seleccionar un usuario por ID.
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
            pstm.setInt(1, id); // Establece el ID en la consulta parametrizada.
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados.
            
            return respuesta; // Devuelve el ResultSet con el usuario encontrado.

        } catch (SQLException e) {
            throw new Error("Error al obtener el usuario"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener usuarios filtrados por ID de ciudad.
     * Ejecuta una consulta SQL para seleccionar usuarios asociados a una ciudad específica.
     * @param ciudad_id Identificador único de la ciudad.
     * @return ResultSet con los usuarios encontrados o lanza una excepción si ocurre un error.
     */
    public static ResultSet getUsuariosByCiudadId(int ciudad_id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        try {
            // Prepara la consulta SQL para seleccionar usuarios por ID de ciudad.
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios WHERE ciudad_id = ?");
            pstm.setInt(1, ciudad_id); // Establece el ID de la ciudad en la consulta.
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados.
            
            return respuesta; // Devuelve el ResultSet con los usuarios encontrados.

        } catch (SQLException e) {
            throw new Error("Error al obtener los usuarios"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener usuarios filtrados por ID de género.
     * Ejecuta una consulta SQL para seleccionar usuarios asociados a un género específico.
     * @param genero_id Identificador único del género.
     * @return ResultSet con los usuarios encontrados o lanza una excepción si ocurre un error.
     */
    public static ResultSet getUsuariosByGeneroId(int genero_id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        try {
            // Prepara la consulta SQL para seleccionar usuarios por ID de género.
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios WHERE genero_id = ?");
            pstm.setInt(1, genero_id); // Establece el ID del género en la consulta.
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados.
            
            return respuesta; // Devuelve el ResultSet con los usuarios encontrados.

        } catch (SQLException e) {
            throw new Error("Error al obtener los usuarios"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para obtener un usuario específico por su correo electrónico.
     * Ejecuta una consulta SQL para seleccionar el usuario con el correo proporcionado.
     * @param correo Dirección de correo electrónico del usuario a buscar.
     * @return ResultSet con los datos del usuario o lanza una excepción si ocurre un error.
     */
    public static ResultSet getUsuarioByCorreo(String correo) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        try {
            // Prepara la consulta SQL para seleccionar un usuario por correo.
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios WHERE correo = ?");
            pstm.setString(1, correo); // Establece el correo en la consulta parametrizada.
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados.
            
            return respuesta; // Devuelve el ResultSet con el usuario encontrado.

        } catch (SQLException e) {
            throw new Error("Error al obtener el usuario"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para crear un nuevo usuario en la base de datos.
     * Inserta un nuevo registro en la tabla 'usuarios' con los datos proporcionados.
     * @param usuarioData Objeto Usuario con los datos del nuevo usuario.
     * @return ResultSet con las claves generadas (ID del usuario creado) o lanza una excepción si ocurre un error.
     */
    public static ResultSet createUsuario(Usuario usuarioData) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        // Consulta SQL para insertar un nuevo usuario.
        String query = "INSERT INTO usuarios (nombre, apellido, correo, contrasena, genero_id, ciudad_id) VALUES "
                + "(?,?,?,?,?,?)";

        try {
            // Prepara la consulta SQL para insertar un usuario y obtener las claves generadas.
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            // Establece los valores del usuario en la consulta parametrizada.
            pstm.setString(1, usuarioData.getNombre());
            pstm.setString(2, usuarioData.getApellido());
            pstm.setString(3, usuarioData.getCorreo());
            pstm.setString(4, usuarioData.getContrasena());
            pstm.setInt(5, usuarioData.getGenero_id());
            pstm.setInt(6, usuarioData.getCiudad_id());

            pstm.executeUpdate(); // Ejecuta la inserción.

            ResultSet generatedKeys = pstm.getGeneratedKeys(); // Obtiene las claves generadas (ID del usuario).
            
            return generatedKeys; // Devuelve el ResultSet con las claves generadas.

        } catch (SQLException e) {
            throw new Error("Error al crear el usuario"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para actualizar los datos de un usuario existente.
     * Modifica el registro del usuario con el ID proporcionado en la tabla 'usuarios'.
     * @param id Identificador único del usuario a actualizar.
     * @param usuarioData Objeto Usuario con los nuevos datos.
     * @return Número de filas afectadas por la actualización (0 si no se encontró el usuario).
     */
    public static int updateUsuario(int id, Usuario usuarioData) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        // Consulta SQL para actualizar los datos del usuario.
        String query = "UPDATE usuarios SET nombre = ?, apellido = ?, correo = ?, genero_id = ?, ciudad_id = ?, rol_id = ?, estado_id = ? "
                + "WHERE id = ?";

        try {
            // Prepara la consulta SQL para actualizar el usuario.
            PreparedStatement pstm = connection.prepareStatement(query);

            // Establece los nuevos valores del usuario en la consulta parametrizada.
            pstm.setString(1, usuarioData.getNombre());
            pstm.setString(2, usuarioData.getApellido());
            pstm.setString(3, usuarioData.getCorreo());
            pstm.setInt(4, usuarioData.getGenero_id());
            pstm.setInt(5, usuarioData.getCiudad_id());
            pstm.setInt(6, usuarioData.getRol_id());
            pstm.setInt(7, usuarioData.getEstado_id());
            pstm.setInt(8, id); // Establece el ID del usuario a actualizar.

            int affectedRow = pstm.executeUpdate(); // Ejecuta la actualización y obtiene el número de filas afectadas.
            
            return affectedRow; // Devuelve el número de filas afectadas.

        } catch (SQLException e) {
            throw new Error("Error al actualizar el usuario"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para realizar una actualización parcial de los datos de un usuario.
     * Modifica solo los campos proporcionados en el objeto usuarioParcial.
     * @param id Identificador único del usuario a actualizar.
     * @param usuarioParcial Objeto Usuario con los datos a actualizar.
     * @return Número de filas afectadas por la actualización (0 si no se encontró el usuario).
     */
    public static int partialUpdate(int id, Usuario usuarioParcial) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        // Consulta SQL para actualizar un subconjunto de campos del usuario.
        String query = "UPDATE usuarios SET nombre = ?, apellido = ?, correo = ?, genero_id = ?, ciudad_id = ? "
                + "WHERE id = ?";

        try {
            // Prepara la consulta SQL para actualizar el usuario.
            PreparedStatement pstm = connection.prepareStatement(query);

            // Establece los valores proporcionados en la consulta parametrizada.
            pstm.setString(1, usuarioParcial.getNombre());
            pstm.setString(2, usuarioParcial.getApellido());
            pstm.setString(3, usuarioParcial.getCorreo());
            pstm.setInt(4, usuarioParcial.getGenero_id());
            pstm.setInt(5, usuarioParcial.getCiudad_id());
            pstm.setInt(6, id); // Establece el ID del usuario a actualizar.

            int affectedRow = pstm.executeUpdate(); // Ejecuta la actualización y obtiene el número de filas afectadas.
            
            return affectedRow; // Devuelve el número de filas afectadas.

        } catch (SQLException e) {
            throw new Error("Error al actualizar el usuario"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para actualizar la contraseña de un usuario.
     * Modifica el campo 'contrasena' del usuario con el ID proporcionado.
     * @param usuario_id Identificador único del usuario.
     * @param contrasena Nueva contraseña a establecer.
     * @return Número de filas afectadas por la actualización (0 si no se encontró el usuario).
     */
    public static int updateContrasena(int usuario_id, String contrasena) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        // Consulta SQL para actualizar la contraseña del usuario.
        String query = "UPDATE usuarios SET contrasena = ? WHERE id = ?";

        try {
            // Prepara la consulta SQL para actualizar la contraseña.
            PreparedStatement pstm = connection.prepareStatement(query);

            // Establece los valores en la consulta parametrizada.
            pstm.setString(1, contrasena);
            pstm.setInt(2, usuario_id); // Establece el ID del usuario a actualizar.

            int affectedRow = pstm.executeUpdate(); // Ejecuta la actualización y obtiene el número de filas afectadas.
            
            return affectedRow; // Devuelve el número de filas afectadas.

        } catch (SQLException e) {
            throw new Error("Error al actualizar la contraseña"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para realizar una eliminación lógica (soft delete) de un usuario.
     * Actualiza el campo 'estado_id' a 2 para marcar el usuario como eliminado.
     * @param id Identificador único del usuario a eliminar lógicamente.
     * @return Número de filas afectadas por la actualización (0 si no se encontró el usuario).
     */
    public static int softDeleteUsuario(int id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        // Consulta SQL para marcar el usuario como eliminado (estado_id = 2).
        String query = "UPDATE usuarios SET estado_id = 2 WHERE id = ?";

        try {
            // Prepara la consulta SQL para la eliminación lógica.
            PreparedStatement pstm = connection.prepareStatement(query);

            // Establece el ID del usuario en la consulta parametrizada.
            pstm.setInt(1, id); // Establece el ID del usuario a actualizar.

            int affectedRow = pstm.executeUpdate(); // Ejecuta la actualización y obtiene el número de filas afectadas.
            
            return affectedRow; // Devuelve el número de filas afectadas.

        } catch (SQLException e) {
            throw new Error("Error al eliminar de forma segura al usuario"); // Lanza una excepción en caso de error SQL.
        }
    }

    /**
     * Método para eliminar físicamente un usuario de la base de datos.
     * Elimina el registro del usuario con el ID proporcionado.
     * @param id Identificador único del usuario a eliminar.
     * @return Número de filas afectadas por la eliminación (0 si no se encontró el usuario).
     */
    public static int deleteUsuario(int id) {
        Connection connection = ConnectionDB.connect(); // Establece la conexión a la base de datos.

        try {
            // Prepara la consulta SQL para eliminar un usuario por ID.
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?");
            pstm.setInt(1, id); // Establece el ID del usuario a eliminar.
            int affectedRows = pstm.executeUpdate(); // Ejecuta la eliminación y obtiene el número de filas afectadas.
            
            return affectedRows; // Devuelve el número de filas afectadas.

        } catch (SQLException e) {
            throw new Error("Error al eliminar el usuario"); // Lanza una excepción en caso de error SQL.
        }
    }
}