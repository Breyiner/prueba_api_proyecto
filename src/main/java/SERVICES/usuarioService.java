package SERVICES;

import DAO.UsuarioDao;
import MODEL.Usuario;
import PROVIDERS.ResponseProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

public class usuarioService {
    
    public static Response getUsuarios() {
        List<Usuario> usuarios = new ArrayList<>(); // Crea una lista para almacenar los usuarios recuperados
        
        try {
            // Ejecuta la consulta para obtener todos los usuarios mediante la capa DAO
            ResultSet respuesta = UsuarioDao.getUsuarios();
            while (respuesta.next()) { // Cambia esto a un while
                // Crea un objeto Usuario con los datos de cada fila
                Usuario usuario = new Usuario(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre"),
                    respuesta.getString("apellido"),
                    respuesta.getString("correo"),
                    respuesta.getString("contrasean"),
                    Integer.parseInt(respuesta.getString("genero_id")),
                    Integer.parseInt(respuesta.getString("ciudad_id")),
                    Integer.parseInt(respuesta.getString("estado_id"))
                );
                // Añade el objeto Usuario a la lista de usuarios
                usuarios.add(usuario);
            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            // Devuelve la lista de usuarios con estado 200 OK si hay usuarios
            if (!usuarios.isEmpty()) {
                return ResponseProvider.success(usuarios, "Usuarios obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay usuarios registrados.", 404);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }
    
    public static Response getUsuarioById(int id) {
        Usuario usuario = null; // Variable para almacenar el usuario encontrado
        
        try {
            // Realiza la consulta para recuperar el usuario por su ID a través de la capa DAO
            ResultSet respuesta = UsuarioDao.getUsuarioById(id);
            
            // Si la consulta devuelve datos, crea el objeto Usuario
            while (respuesta.next()) { 
                usuario = new Usuario(
                    respuesta.getInt("id"), // Obtiene el ID del usuario
                    respuesta.getString("nombre"), // Obtiene el nombre del usuario
                    respuesta.getString("apellido"), // Obtiene el apellido del usuario
                    respuesta.getString("correo"), // Obtiene el correo electrónico del usuario
                    respuesta.getString("contrasean"), // Obtiene la contraseña del usuario
                    respuesta.getInt("genero_id"), // Obtiene el ID del género del usuario
                    respuesta.getInt("ciudad_id"), // Obtiene el ID de la ciudad del usuario
                    respuesta.getInt("estado_id") // Obtiene el ID del estado del usuario
                );
            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            
            // Devuelve el usuario con estado 200 OK si existe
            if (usuario == null) {
                return ResponseProvider.error("El usuario no existe.", 404);
            } else {
                return ResponseProvider.success(usuario, "Usuario obtenido con éxito.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }
    
    public static Response getUsuarioByCorreo(String correo) {
        Usuario usuario = null; // Variable para almacenar el usuario buscado
        
        try {
            // Consulta a la base de datos para encontrar usuario con el correo indicado
            ResultSet respuesta = UsuarioDao.getUsuarioByCorreo(correo);
            
            // Si existe, crea un objeto Usuario con la información recibida
            while (respuesta.next()) { 
                usuario = new Usuario(
                    respuesta.getInt("id"), // Obtiene el ID del usuario
                    respuesta.getString("nombre"), // Obtiene el nombre del usuario
                    respuesta.getString("apellido"), // Obtiene el apellido del usuario
                    respuesta.getString("correo"), // Obtiene el correo electrónico del usuario
                    respuesta.getString("contrasean"), // Obtiene la contraseña del usuario
                    respuesta.getInt("genero_id"), // Obtiene el ID del género del usuario
                    respuesta.getInt("ciudad_id"), // Obtiene el ID de la ciudad del usuario
                    respuesta.getInt("estado_id") // Obtiene el ID del estado del usuario
                );
            }
            
            // Cierra el conjunto de resultados para optimizar recursos
            respuesta.close();
            
            // Devuelve el usuario con estado 200 OK si existe
            if (usuario == null) {
                return ResponseProvider.error("El usuario no existe.", 404);
            } else {
                return ResponseProvider.success(usuario, "Usuario obtenido con éxito.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }
    
    public static Response createUsuario(Usuario usuarioData) {
        try {
            
            Response usuarioExistente = getUsuarioByCorreo(usuarioData.getCorreo());
            
            if (usuarioExistente.getStatus() == 200) return ResponseProvider.error("Este correo ya fué registrado.", 409);
            
            int idGenerado = 0; // Inicializa variable para almacenar el ID generado
            
            // Inserta el nuevo usuario en la base de datos y obtiene el último ID generado
            ResultSet ultimoRegistro = UsuarioDao.createUsuario(usuarioData);
            
            // Si la inserción fue exitosa, asigna el ID generado al objeto usuario
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1);
                usuarioData.setId(idGenerado); // Asigna el ID al objeto usuario
            }
            
            // Cierra el conjunto de resultados para optimizar recursos
            ultimoRegistro.close();
            
            // Devuelve el usuario con estado 200 OK si se crea con exito
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el usuario.", 400);
            } else {
                return ResponseProvider.success(usuarioData, "Usuario creado con éxito.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al crear el usuario.", 500);
        }
    }
    
    public static Response updateUsuario(int id, Usuario usuarioData) {
        try {
           
           Response usuarioExistente = getUsuarioById(id);
            
           if (usuarioExistente.getStatus() == 404) return ResponseProvider.error("Este usuario no existe.", 404);
           
 
           if(existEmail(id, usuarioData)) return ResponseProvider.error("Este correo ya fué registrado.", 409);
           
            // Intenta actualizar el usuario en la base de datos y devuelve el número de filas afectadas
            int rowsAffected = UsuarioDao.updateUsuario(id, usuarioData);
            
            if (rowsAffected != 0){
                
                usuarioData.setId(id);
                // Si la actualización se realizó, se confirma el éxito con código 200 OK
                return ResponseProvider.success(usuarioData, "Usuario actualizado con éxito.", 200);
            }
            else 
                return ResponseProvider.error("Error al actualizar el usuario.", 400);
            
        } catch (Exception e) {
            // Captura cualquier problema interno y devuelve un error 500 con mensaje
            return ResponseProvider.error("Error interno al actualizar el usuario.", 500);
        }
    }
    
    private static boolean existEmail(int id, Usuario usuarioData) {

        boolean valido = false;

        try {
            // Ejecuta la consulta para obtener todos los usuarios mediante la capa DAO
            ResultSet respuesta = UsuarioDao.getUsuarios();
            while (respuesta.next()) { // Cambia esto a un while

                if(respuesta.getInt("id") != id && usuarioData.getCorreo().equals(respuesta.getString("correo"))) valido = true;

            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();

            return valido;

        } catch (SQLException e) {
            throw new Error("Error al validar si el correo existe");
        }
    }
    
    public static Response deleteUsario(int id) {
        try {
            
            // Ejecuta la eliminación de usuario en la base de datos y recibe la cantidad de filas afectadas
            int rowsAffected = UsuarioDao.deleteUsuario(id);
            
            if (rowsAffected != 0) 
                // Si usuario eliminado correctamente, devuelve código 204 No Content con mensaje
                return ResponseProvider.success(null, "Usuario eliminado con éxito.", 200);
            else 
                // Si no encontró usuario para eliminar, devuelve 404 Not Found con mensaje
                return ResponseProvider.error("Este usuario no existe.", 404);
            
        } catch (Exception e) {
            // Para cualquier error interno, retorna un error 500 con mensaje
            return ResponseProvider.error("Error interno al eliminar el usuario.", 500);
        }
    }
}
