package CONTROLLER; // Paquete que contiene el controlador de usuarios

import MODELO.UsuarioDao;
import MODELO.CantidadRegistrosDTO;
import MODELO.PasswordDTO;
import MODELO.Usuario; // Importa la clase Usuario que representa la entidad de usuario
import MODELO.UsuarioDTO;
import MODELO.UsuarioLoginDTO;
import MODELO.UsuarioTablaDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes; // Importa la anotación para indicar el tipo de contenido que consume el método
import javax.ws.rs.DELETE; // Importa la anotación para manejar solicitudes DELETE
import javax.ws.rs.GET; // Importa la anotación para manejar solicitudes GET
import javax.ws.rs.PATCH;
import javax.ws.rs.POST; // Importa la anotación para manejar solicitudes POST
import javax.ws.rs.PUT; // Importa la anotación para manejar solicitudes PUT
import javax.ws.rs.Path; // Importa la anotación para definir la ruta del recurso
import javax.ws.rs.PathParam; // Importa la anotación para extraer parámetros de la ruta
import javax.ws.rs.Produces; // Importa la anotación para indicar el tipo de contenido que produce el método
import javax.ws.rs.core.MediaType; // Importa la clase MediaType para definir tipos de contenido
import javax.ws.rs.core.Response; // Importa la clase Response para construir respuestas HTTP
import org.json.JSONException;
import org.mindrot.jbcrypt.BCrypt;

@Path("/usuarios") // Define la ruta base para todas las operaciones relacionadas con usuarios
public class UserController {
    

    @GET // Indica que este método responde a solicitudes GET
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
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
                    respuesta.getString("contrasena"),
                    Integer.parseInt(respuesta.getString("genero_id")),
                    Integer.parseInt(respuesta.getString("ciudad_id")),
                    respuesta.getInt("rol_id"), // Obtiene el ID del rol del usuario
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
                return ResponseProvider.success(usuarios, "No hay usuarios registrados.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }

    @GET // Indica que este método responde a solicitudes GET
    @Path("/tabla")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public static Response getUsuariosTabla() {
        
        List<UsuarioTablaDTO> usuarios = new ArrayList<>(); // Crea una lista para almacenar los usuarios recuperados
        
        try {
            // Ejecuta la consulta para obtener todos los usuarios mediante la capa DAO
            ResultSet respuesta = UsuarioDao.getUsuariosTabla();
            while (respuesta.next()) { // Cambia esto a un while
                // Crea un objeto Usuario con los datos de cada fila
                UsuarioTablaDTO usuario = new UsuarioTablaDTO(
                    respuesta.getInt("id"),
                    respuesta.getString("rol"),
                    respuesta.getString("nombre"),
                    respuesta.getString("apellido"),
                    respuesta.getString("correo"),
                    respuesta.getString("genero"),
                    respuesta.getString("ciudad"),
                    respuesta.getString("estado")
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
                return ResponseProvider.success(usuarios, "No hay usuarios registrados.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }
    
    @GET // Indica que este método responde a solicitudes GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public static Response getUsuario() {
        
        CantidadRegistrosDTO cantidad = null;
        
        try {

            ResultSet respuesta = UsuarioDao.getCantidadUsuarios();
            while (respuesta.next()) { // Cambia esto a un while
                
                cantidad = new CantidadRegistrosDTO(
                    respuesta.getInt("cantidad")
                );
            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            // Devuelve el usuario con estado 200 OK si existe
            if (cantidad == null) {
                return ResponseProvider.error("No se pudo obtener la cantidad de usuarios.", 404);
            } else {
                return ResponseProvider.success(cantidad, "Cantidad de usuarios obtenida con éxito.", 200);
            }
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            return ResponseProvider.error("Error interno al obtener la cantidad de usuarios", 500);
        }
    }

    @GET // Indica que este método responde a solicitudes GET
    @Path("/{id}") // La ruta incluye el ID del usuario a buscar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public static Response getUsuarioById(@PathParam("id") int id) {
        
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
                    "", // Obtiene la contraseña del usuario
                    respuesta.getInt("genero_id"), // Obtiene el ID del género del usuario
                    respuesta.getInt("ciudad_id"), // Obtiene el ID de la ciudad del usuario
                    respuesta.getInt("rol_id"), // Obtiene el ID del rol del usuario
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
    

    @POST
    @Path("/register")
    @Validar(entidad = "Usuario")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public static Response createUsuario(Usuario usuarioData) {
        
        try {
            
            Usuario usuarioExistente = getUsuarioByCorreo(usuarioData.getCorreo());
            
            if (usuarioExistente != null) return ResponseProvider.error("Este correo ya fué registrado.", 409);
            
            //Se obtiene la contraseña del usuario
            String contrasenaText = usuarioData.getContrasena();
            // se hashea la contraseña
            usuarioData.setContrasena(hashPassword(contrasenaText));
            
            int idGenerado = 0; // Inicializa variable para almacenar el ID generado
            
            // Inserta el nuevo usuario en la base de datos y obtiene el último ID generado
            ResultSet ultimoRegistro = UsuarioDao.createUsuario(usuarioData);
            
            // Si la inserción fue exitosa, asigna el ID generado al objeto usuario
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1);
                usuarioData.setId(idGenerado); // Asigna el ID al objeto usuario
                usuarioData.setContrasena(contrasenaText);
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
    
    @POST
    @Path("/login")
    @Validar(entidad = "UsuarioLogin")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public static Response loginUser(UsuarioLoginDTO usuarioData) {
        
        try {
            Usuario usuario = getUsuarioByCorreo(usuarioData.getCorreo());
        
            if(usuario == null) return ResponseProvider.error("Este correo no se encuentra registrado.", 404, null);
            
            if(usuario.getEstado_id() == 2) return ResponseProvider.error("Este usuario no se encuentra registrado.", 404, null);

            if(!checkPassword(
                    usuarioData.getContrasena(), 
                    usuario.getContrasena()
            ))
                return ResponseProvider.error("Contraseña incorrecta.", 404, null);

            else {
                
                UsuarioDTO usuarioDto = new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getRol_id()
                );
                
                return ResponseProvider.success(usuarioDto, "Inicio de sesión exitoso.", 200);
            }
            
        } catch (JSONException e) {
            return ResponseProvider.error("Error interno al validar el usuario.", 500);
        }
    }

    @PUT // Indica que este método responde a solicitudes PUT
    @Validar(entidad = "Usuario")
    @Path("/{id}") // Ruta con el ID del usuario a actualizar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public static Response updateUsuario(@PathParam("id") int id, Usuario usuarioData) {
        boolean hashValido = true;
        
        try {
           
           Response usuarioExistente = getUsuarioById(id);
            
           if (usuarioExistente.getStatus() == 404) return ResponseProvider.error("Este usuario no existe.", 404);
           

           if(existEmail(id, usuarioData)) return ResponseProvider.error("Este correo ya fué registrado.", 409);
           
           if(usuarioData.getContrasena() != null) {
                //Se obtiene la contraseña del usuario
                String contrasenaText = usuarioData.getContrasena();
                // se hashea la contraseña
                String hashContrasena =  hashPassword(contrasenaText);
                
                int rowsAffectedPswd = UsuarioDao.updateContrasena(id, hashContrasena);
                
                if(rowsAffectedPswd == 0) hashValido = false;
           }
           
            // Intenta actualizar el usuario en la base de datos y devuelve el número de filas afectadas
            int rowsAffectedUsuario = UsuarioDao.updateUsuario(id, usuarioData);
            
            
            if (rowsAffectedUsuario != 0 && hashValido){
                // Si la actualización se realizó, se confirma el éxito con código 200 OK
                return ResponseProvider.success(null, "Usuario actualizado con éxito.", 200);
            }
            else 
                return ResponseProvider.error("Error al actualizar el usuario.", 400);
            
        } catch (Exception e) {
            // Captura cualquier problema interno y devuelve un error 500 con mensaje
            return ResponseProvider.error("Error interno al actualizar el usuario.", 500);
        }
    }
    
    @PATCH // Indica que este método responde a solicitudes PATCH
    @Validar(entidad = "UsuarioPerfil")
    @Path("/{id}") // Ruta con el ID del usuario a actualizar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public static Response partialUpdateUsuario(@PathParam("id") int id, Usuario usuarioData) {
        try {
            if(existEmail(id, usuarioData)) return ResponseProvider.error("Este correo ya fué registrado.", 409);
            
            Usuario usuarioParcial = new Usuario(
                id,
                usuarioData.getNombre(), // Obtiene el nombre del usuario
                usuarioData.getApellido(), // Obtiene el apellido del usuario
                usuarioData.getCorreo(), // Obtiene el correo electrónico del usuario
                usuarioData.getGenero_id(), // Obtiene el ID del género del usuario
                usuarioData.getCiudad_id() // Obtiene el ID de la ciudad del usuario
            );
            
            // Intenta actualizar el usuario en la base de datos y devuelve el número de filas afectadas
            int rowsAffected = UsuarioDao.partialUpdate(id, usuarioParcial);
            
            if (rowsAffected != 0){
                
                usuarioData.setId(id);
                // Si la actualización se realizó, se confirma el éxito con código 200 OK
                return ResponseProvider.success(null, "Usuario actualizado con éxito.", 200);
            }
            else 
                return ResponseProvider.error("Error al actualizar el usuario.", 400);
            
        } catch (Exception e) {
            // Captura cualquier problema interno y devuelve un error 500 con mensaje
            return ResponseProvider.error("Error interno al actualizar el usuario.", 500);
        }
    }
    
    
    @PATCH // Indica que este método responde a solicitudes PUT
    @Path("/password/usuario/{usuario_id}") // Ruta con el ID del usuario a actualizar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public static Response updateContrasena(@PathParam("usuario_id") int usuario_id, PasswordDTO passwordData) {
        try {
            
            if(!passwordData.getNew_password().equals(passwordData.getConfirm_password()))
                return ResponseProvider.error("Las contraseñas no coinciden.", 400, null);
            
            //Se obtiene la contraseña del usuario
            boolean passwordValid = validatePassword(usuario_id, passwordData.getOld_password());
            
            if(!passwordValid) return ResponseProvider.error("Contraseña incorrecta.", 400, null);
            
            // se hashea la contraseña
            String hashPassword = hashPassword(passwordData.getNew_password());
            
            // Ejecuta la actualización de la contraseña en la base de datos y recibe la cantidad de filas afectadas
            int rowsAffected = UsuarioDao.updateContrasena(usuario_id, hashPassword);
            
            if (rowsAffected != 0) 
                // Si contraseña actualizaa correctamente, devuelve código 204 No Content con mensaje
                return ResponseProvider.success(null, "Contraseña actualizada con éxito.", 200);
            else 
                // Si no encontró usuario para actualizar contraseña, devuelve 404 Not Found con mensaje
                return ResponseProvider.error("Este usuario no existe.", 404, null);
            
        } catch(Exception e) {
            return ResponseProvider.error("Error interno al actualizar la contraseña.", 500);
        }
    }
    
    @DELETE // Indica que este método responde a solicitudes DELETE
    @Path("soft/{id}") // Ruta con el ID del usuario a eliminar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public static Response softDeleteUsuario(@PathParam("id") int id) {
     
        try {
            
            // Ejecuta la eliminación de usuario en la base de datos y recibe la cantidad de filas afectadas
            int rowsAffected = UsuarioDao.softDeleteUsuario(id);
            
            if (rowsAffected != 0) 
                // Si usuario eliminado correctamente, devuelve código 204 No Content con mensaje
                return ResponseProvider.success(null, "Usuario eliminado de forma segura.", 200);
            else 
                // Si no encontró usuario para eliminar, devuelve 404 Not Found con mensaje
                return ResponseProvider.error("Este usuario no existe.", 404);
            
        } catch (Exception e) {
            // Para cualquier error interno, retorna un error 500 con mensaje
            return ResponseProvider.error("Error interno al eliminar el usuario.", 500);
        }
    }
    
    @DELETE // Indica que este método responde a solicitudes DELETE
    @Path("/{id}") // Ruta con el ID del usuario a eliminar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public static Response deleteUsuario(@PathParam("id") int id) {
     
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
    
    private static Usuario getUsuarioByCorreo(String correo) {
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
                    respuesta.getString("contrasena"), // Obtiene la contraseña del usuario
                    respuesta.getInt("genero_id"), // Obtiene el ID del género del usuario
                    respuesta.getInt("ciudad_id"), // Obtiene el ID de la ciudad del usuario
                    respuesta.getInt("rol_id"), // Obtiene el ID del rol del usuario
                    respuesta.getInt("estado_id") // Obtiene el ID del estado del usuario
                );
            }
            
            // Cierra el conjunto de resultados para optimizar recursos
            respuesta.close();
            
            // Devuelve el usuario con estado 200 OK si existe
            return usuario;
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 500
            throw new Error("Error al obtener el usuario");
        }
    }
    
    private static boolean existEmail(int id, Usuario usuarioData) {

        boolean existe = false;

        try {
            // Ejecuta la consulta para obtener todos los usuarios mediante la capa DAO
            ResultSet respuesta = UsuarioDao.getUsuarios();
            while (respuesta.next()) {

                if(respuesta.getInt("id") != id && usuarioData.getCorreo().equals(respuesta.getString("correo"))) existe = true;

            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();

            return existe;

        } catch (SQLException e) {
            throw new Error("Error al validar si el correo existe");
        }
    }
    
    public static boolean validatePassword(int usuario_id, String passwordText) {
        try {
            
            String contrasenaHash = obtenerContrasena(usuario_id);
            
            if(contrasenaHash == null) return false;
            
            return !checkPassword(
                    passwordText, 
                    contrasenaHash
            );
            
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
    
    private static String obtenerContrasena(int id) {
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
                    respuesta.getString("contrasena"), // Obtiene la contraseña del usuario
                    respuesta.getInt("genero_id"), // Obtiene el ID del género del usuario
                    respuesta.getInt("ciudad_id"), // Obtiene el ID de la ciudad del usuario
                    respuesta.getInt("rol_id"), // Obtiene el ID del rol del usuario
                    respuesta.getInt("estado_id") // Obtiene el ID del estado del usuario
                );
            }
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            
            // Devuelve el usuario con estado 200 OK si existe
            if (usuario == null) {
                return null;
            } else {
                return usuario.getContrasena();
            }
            
        } catch (SQLException e) {
            return null;
        }
    }
    
    public static String hashPassword(String contrasenaText) {
        
        return BCrypt.hashpw(contrasenaText, BCrypt.gensalt());
    }

    public static boolean checkPassword(String contrasenaText, String hashedContrasena) {
        return BCrypt.checkpw(contrasenaText, hashedContrasena);
    }
}