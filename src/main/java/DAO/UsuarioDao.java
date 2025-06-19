package DAO; // Paquete que contiene las clases de acceso a datos

import MODEL.Usuario; // Importa la clase Usuario que representa la entidad de usuario
import UTILS.ConnectionDB; // Importa la clase ConnectionDB que maneja la conexión a la base de datos
import java.sql.Connection; // Importa la clase Connection para manejar conexiones a la base de datos
import java.sql.PreparedStatement; // Importa la clase PreparedStatement para ejecutar consultas SQL
import java.sql.ResultSet; // Importa la clase ResultSet para manejar los resultados de las consultas SQL
import java.sql.SQLException; // Importa la clase SQLException para manejar errores de SQL

public class UsuarioDao {
    
    private static final ConnectionDB conn = new ConnectionDB(); // Crea una instancia de ConnectionDB para manejar la conexión a la base de datos
    
    /**
     * Método para obtener todos los usuarios de la base de datos.
     * Realiza una consulta SQL para seleccionar todos los registros de la tabla 'usuarios'.
     * 
     * @return ResultSet que contiene todos los usuarios o null si ocurre un error
     */
    public static ResultSet getUsuarios() {
        
        Connection connection = conn.connect(); // Establece la conexión a la base de datos
        
        try {
            // Prepara la consulta SQL para seleccionar todos los usuarios
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios");
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados
            
            return respuesta; // Devuelve el ResultSet con los usuarios
            
        } catch (SQLException e) {
            throw new Error("Error al obtener los usuarios"); // Devuelve null si ocurre un error en la consulta
        }
    }
    
    /**
     * Método para obtener un usuario específico por su ID.
     * Realiza una consulta SQL para seleccionar el usuario con el ID proporcionado.
     * 
     * @param id Identificador único del usuario a buscar
     * @return ResultSet que contiene el usuario encontrado o null si ocurre un error
     */
    public static ResultSet getUsuarioById(int id) {
        
        Connection connection = conn.connect(); // Establece la conexión a la base de datos
        
        try {
            // Prepara la consulta SQL para seleccionar un usuario por ID
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
            pstm.setInt(1, id); // Establece el ID en la consulta
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados
            
            return respuesta; // Devuelve el ResultSet con el usuario encontrado
            
        } catch (SQLException e) {
            throw new Error("Error al obtener el usuario");
        }
    }
    
    /**
     * Método para obtener un usuario específico por su correo electrónico.
     * Realiza una consulta SQL para seleccionar el usuario con el correo proporcionado.
     * 
     * @param correo Dirección de correo electrónico del usuario a buscar
     * @return ResultSet que contiene el usuario encontrado o null si ocurre un error
     */
    public static ResultSet getUsuarioByCorreo(String correo) {
        
        Connection connection = conn.connect(); // Establece la conexión a la base de datos
        
        try {
            // Prepara la consulta SQL para seleccionar un usuario por correo
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM usuarios WHERE correo = ?");
            pstm.setString(1, correo); // Establece el correo en la consulta
            ResultSet respuesta = pstm.executeQuery(); // Ejecuta la consulta y obtiene los resultados
            
            return respuesta; // Devuelve el ResultSet con el usuario encontrado
            
        } catch (SQLException e) {
            throw new Error("Error al obtener los usuarios");
        }
    }
   
    /**
     * Método para crear un nuevo usuario en la base de datos.
     * Inserta un nuevo registro en la tabla 'usuarios' con los datos proporcionados.
     * 
     * @param usuarioData Objeto Usuario que contiene los datos del nuevo usuario
     * @return ResultSet que contiene las claves generadas o null si ocurre un error
     */
    public static ResultSet createUsuario(Usuario usuarioData) {
        
        Connection connection = conn.connect(); // Establece la conexión a la base de datos
        
        // Consulta SQL para insertar un nuevo usuario en la tabla
        String query = "INSERT INTO usuarios (nombre, apellido, correo, contrasean, genero_id, ciudad_id, estado_id) VALUES "
                + "(?,?,?,?,?,?,?)";
        
        try {
            // Prepara la consulta SQL para insertar un nuevo usuario y obtener las claves generadas
            PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            
            // Establece los valores del usuario en la consulta
            pstm.setString(1, usuarioData.getNombre());
            pstm.setString(2, usuarioData.getApellido());
            pstm.setString(3, usuarioData.getCorreo());
            pstm.setString(4, usuarioData.getContrasena());
            pstm.setInt(5, usuarioData.getGenero_id());
            pstm.setInt(6, usuarioData.getCiudad_id());
            pstm.setInt(7, usuarioData.getEstado_id());

            pstm.executeUpdate(); // Ejecuta la inserción
            
            ResultSet generatedKeys = pstm.getGeneratedKeys(); // Obtiene las claves generadas
            
            return generatedKeys; // Devuelve el ResultSet con las claves generadas
            
        } catch (SQLException e) {
            throw new Error("Error al crear el usuario");
        }
    }
    
    /**
     * Método para actualizar los datos de un usuario existente en la base de datos.
     * Modifica el registro del usuario con el ID proporcionado.
     * 
     * @param id Identificador único del usuario a actualizar
     * @param usuarioData Objeto Usuario que contiene los nuevos datos del usuario
     * @return Número de filas afectadas por la actualización (0 si no se encontró el usuario)
     */
    public static int updateUsuario(int id, Usuario usuarioData) {
        Connection connection = conn.connect(); // Establece la conexión a la base de datos
        
        // Consulta SQL para actualizar los datos del usuario
        String query = "UPDATE usuarios SET nombre = ?, apellido = ?, correo = ?, contrasean = ?, genero_id = ?, ciudad_id = ?, estado_id = ? "
                + "WHERE id = ?";
        
        try {
            // Prepara la consulta SQL para actualizar el usuario
            PreparedStatement pstm = connection.prepareStatement(query);
            
            // Establece los nuevos valores del usuario en la consulta
            pstm.setString(1, usuarioData.getNombre());
            pstm.setString(2, usuarioData.getApellido());
            pstm.setString(3, usuarioData.getCorreo());
            pstm.setString(4, usuarioData.getContrasena());
            pstm.setInt(5, usuarioData.getGenero_id());
            pstm.setInt(6, usuarioData.getCiudad_id());
            pstm.setInt(7, usuarioData.getEstado_id());
            pstm.setInt(8, id); // Establece el ID del usuario a actualizar
            
            int affectedRow = pstm.executeUpdate(); // Ejecuta la actualización y obtiene el número de filas afectadas
            
            return affectedRow; // Devuelve el número de filas afectadas
            
        } catch (SQLException e) {
            throw new Error("Error al actualizar el usuario");
        }
    }
    
    /**
     * Método para eliminar un usuario de la base de datos.
     * Elimina el registro del usuario con el ID proporcionado.
     * 
     * @param id Identificador único del usuario a eliminar
     * @return Número de filas afectadas por la eliminación (0 si no se encontró el usuario)
     */
    public static int deleteUsuario(int id) {
        
        Connection connection = conn.connect(); // Establece la conexión a la base de datos
        
        try {
            // Prepara la consulta SQL para eliminar un usuario por ID
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?");
            pstm.setInt(1, id); // Establece el ID del usuario a eliminar
            int affectedRows = pstm.executeUpdate(); // Ejecuta la eliminación y obtiene el número de filas afectadas
            
            return affectedRows; // Devuelve el número de filas afectadas
            
        } catch (SQLException e) {
            throw new Error("Error al eliminar el usuario");
        }
    }
}