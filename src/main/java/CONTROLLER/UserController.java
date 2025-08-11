package CONTROLLER; // Paquete que agrupa los controladores del sistema

import MODELO.UsuarioDao;              // Capa DAO para interacción con BD para usuarios
import MODELO.CantidadRegistrosDTO;    // DTO para manejar cantidad de registros
import MODELO.PasswordDTO;             // DTO para manejo de contraseña (si aplica)
import MODELO.Usuario;                 // Entidad Usuario que representa datos de usuario
import MODELO.UsuarioDTO;              // DTO para transferencia de datos de usuario
import MODELO.UsuarioLoginDTO;         // DTO para login de usuario
import MODELO.UsuarioTablaDTO;         // DTO para datos de usuario en formato tabla
import java.sql.ResultSet;             // Para manejo de resultados de consultas SQL
import java.sql.SQLException;          // Manejo de excepciones SQL
import java.util.ArrayList;            // Implementación de lista dinámica
import java.util.List;                 // Interfaz lista para colecciones
import javax.ws.rs.Consumes;           // Anotación para tipos de contenido entrante
import javax.ws.rs.DELETE;             // Anotación para método HTTP DELETE
import javax.ws.rs.GET;                // Anotación para método HTTP GET
import javax.ws.rs.PATCH;              // Anotación para método HTTP PATCH
import javax.ws.rs.POST;               // Anotación para método HTTP POST
import javax.ws.rs.PUT;                // Anotación para método HTTP PUT
import javax.ws.rs.Path;               // Anotación para definir ruta HTTP
import javax.ws.rs.PathParam;          // Anotación para extraer parámetros de ruta
import javax.ws.rs.Produces;           // Anotación para tipo de contenido saliente
import javax.ws.rs.core.MediaType;     // Constantes para tipos de contenido (ej: JSON)
import javax.ws.rs.core.Response;      // Construcción de respuestas HTTP
import org.json.JSONException;         // Excepción para manejo de JSON
import org.mindrot.jbcrypt.BCrypt;    // Librería para encriptación de contraseñas

@Path("/usuarios") // Ruta base para todas las operaciones REST relacionadas con usuarios
public class UserController {

    /**
     * Método para obtener todos los usuarios registrados en la base de datos.
     * @return Response HTTP con la lista de usuarios o mensaje en caso de no haber usuarios.
     * El método responde a peticiones HTTP GET a la ruta /usuarios
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response getUsuarios() {
        List<Usuario> usuarios = new ArrayList<>(); // Lista para almacenar objetos Usuario

        try {
            // Llama a la capa DAO para obtener el conjunto de usuarios de la base de datos
            ResultSet respuesta = UsuarioDao.getUsuarios();

            // Itera el ResultSet para construir objetos Usuario con los datos obtenidos
            while (respuesta.next()) {
                Usuario usuario = new Usuario(
                    respuesta.getInt("id"),                      // ID único del usuario
                    respuesta.getString("nombre"),               // Nombre del usuario
                    respuesta.getString("apellido"),             // Apellido del usuario
                    respuesta.getString("correo"),               // Correo electrónico
                    respuesta.getString("contrasena"),           // Contraseña encriptada (ideal no enviar)
                    Integer.parseInt(respuesta.getString("genero_id")),   // ID del género
                    Integer.parseInt(respuesta.getString("ciudad_id")),   // ID de la ciudad
                    respuesta.getInt("rol_id"),                   // ID del rol asignado
                    Integer.parseInt(respuesta.getString("estado_id"))    // ID del estado del usuario (activo/inactivo)
                );

                // Agrega cada objeto Usuario a la lista
                usuarios.add(usuario);
            }

            // Cierra el ResultSet para liberar recursos y evitar fugas
            respuesta.close();

            // Si la lista tiene usuarios, retorna con código 200 OK y lista; si está vacía retorna mensaje con 200
            if (!usuarios.isEmpty()) {
                return ResponseProvider.success(usuarios, "Usuarios obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(usuarios, "No hay usuarios registrados.", 200);
            }

        } catch (SQLException e) {
            // Si ocurre una excepción SQL, retorna un error 500 indicando problema interno
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }
    
    @GET
    @Path("ciudad/{ciudad_id}")
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response getUsuariosByCiudadId(@PathParam("ciudad_id") int ciudad_id) {
        List<Usuario> usuarios = new ArrayList<>(); // Lista para almacenar objetos Usuario

        try {
            // Llama a la capa DAO para obtener el conjunto de usuarios de la base de datos
            ResultSet respuesta = UsuarioDao.getUsuariosByCiudadId(ciudad_id);

            // Itera el ResultSet para construir objetos Usuario con los datos obtenidos
            while (respuesta.next()) {
                Usuario usuario = new Usuario(
                    respuesta.getInt("id"),                      // ID único del usuario
                    respuesta.getString("nombre"),               // Nombre del usuario
                    respuesta.getString("apellido"),             // Apellido del usuario
                    respuesta.getString("correo"),               // Correo electrónico
                    respuesta.getString("contrasena"),           // Contraseña encriptada (ideal no enviar)
                    Integer.parseInt(respuesta.getString("genero_id")),   // ID del género
                    Integer.parseInt(respuesta.getString("ciudad_id")),   // ID de la ciudad
                    respuesta.getInt("rol_id"),                   // ID del rol asignado
                    Integer.parseInt(respuesta.getString("estado_id"))    // ID del estado del usuario (activo/inactivo)
                );

                // Agrega cada objeto Usuario a la lista
                usuarios.add(usuario);
            }

            // Cierra el ResultSet para liberar recursos y evitar fugas
            respuesta.close();

            // Si la lista tiene usuarios, retorna con código 200 OK y lista; si está vacía retorna mensaje con 200
            if (!usuarios.isEmpty()) {
                return ResponseProvider.success(usuarios, "Usuarios obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay usuarios relacionados.", 404);
            }

        } catch (SQLException e) {
            System.out.print(e);
            // Si ocurre una excepción SQL, retorna un error 500 indicando problema interno
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }
    
    @GET
    @Path("genero/{genero_id}")
    @Produces(MediaType.APPLICATION_JSON) // Indica que la respuesta será en formato JSON
    public static Response getUsuariosByGeneroId(@PathParam("genero_id") int genero_id) {
        List<Usuario> usuarios = new ArrayList<>(); // Lista para almacenar objetos Usuario

        try {
            // Llama a la capa DAO para obtener el conjunto de usuarios de la base de datos
            ResultSet respuesta = UsuarioDao.getUsuariosByGeneroId(genero_id);

            // Itera el ResultSet para construir objetos Usuario con los datos obtenidos
            while (respuesta.next()) {
                Usuario usuario = new Usuario(
                    respuesta.getInt("id"),                      // ID único del usuario
                    respuesta.getString("nombre"),               // Nombre del usuario
                    respuesta.getString("apellido"),             // Apellido del usuario
                    respuesta.getString("correo"),               // Correo electrónico
                    respuesta.getString("contrasena"),           // Contraseña encriptada (ideal no enviar)
                    Integer.parseInt(respuesta.getString("genero_id")),   // ID del género
                    Integer.parseInt(respuesta.getString("ciudad_id")),   // ID de la ciudad
                    respuesta.getInt("rol_id"),                   // ID del rol asignado
                    Integer.parseInt(respuesta.getString("estado_id"))    // ID del estado del usuario (activo/inactivo)
                );

                // Agrega cada objeto Usuario a la lista
                usuarios.add(usuario);
            }

            // Cierra el ResultSet para liberar recursos y evitar fugas
            respuesta.close();

            // Si la lista tiene usuarios, retorna con código 200 OK y lista; si está vacía retorna mensaje con 200
            if (!usuarios.isEmpty()) {
                return ResponseProvider.success(usuarios, "Usuarios obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(null, "No hay usuarios relacionados.", 404);
            }

        } catch (SQLException e) {
            // Si ocurre una excepción SQL, retorna un error 500 indicando problema interno
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }

    /**
     * Método para obtener usuarios en formato tabla con campos detallados para UI.
     * Útil para mostrar listados en front-end con información desglosada.
     * Responde a GET /usuarios/tabla
     */
    @GET
    @Path("/tabla")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getUsuariosTabla() {
        List<UsuarioTablaDTO> usuarios = new ArrayList<>();

        try {
            // Ejecuta la consulta para obtener los usuarios con datos relacionados (rol, ciudad, estado, etc)
            ResultSet respuesta = UsuarioDao.getUsuariosTabla();

            // Itera los resultados para crear objetos UsuarioTablaDTO con datos formateados
            while (respuesta.next()) {
                UsuarioTablaDTO usuario = new UsuarioTablaDTO(
                    respuesta.getInt("id"),           // ID del usuario
                    respuesta.getString("rol"),       // Nombre del rol (ej. Admin, Usuario)
                    respuesta.getString("nombre"),    // Nombre
                    respuesta.getString("apellido"),  // Apellido
                    respuesta.getString("correo"),    // Correo
                    respuesta.getString("genero"),    // Género descriptivo (ej. Masculino)
                    respuesta.getString("ciudad"),    // Ciudad descriptiva
                    respuesta.getString("estado")     // Estado descriptivo (ej. Activo)
                );

                usuarios.add(usuario);
            }

            respuesta.close();

            if (!usuarios.isEmpty()) {
                return ResponseProvider.success(usuarios, "Usuarios obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.success(usuarios, "No hay usuarios registrados.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }

    /**
     * Método para obtener la cantidad total de usuarios registrados en la base de datos.
     * Responde a GET /usuarios/cantidad
     */
    @GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getUsuario() {
        CantidadRegistrosDTO cantidad = null;

        try {
            ResultSet respuesta = UsuarioDao.getCantidadUsuarios();

            // Obtiene el número de usuarios desde la consulta
            while (respuesta.next()) {
                cantidad = new CantidadRegistrosDTO(respuesta.getInt("cantidad"));
            }

            respuesta.close();

            // Retorna error 404 si no pudo obtener cantidad, o éxito con el dato
            if (cantidad == null) {
                return ResponseProvider.error("No se pudo obtener la cantidad de usuarios.", 404);
            } else {
                return ResponseProvider.success(cantidad, "Cantidad de usuarios obtenida con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener la cantidad de usuarios", 500);
        }
    }

    /**
     * Método para obtener un usuario específico a partir de su ID.
     * Responde a GET /usuarios/{id}
     * @param id Identificador único del usuario a buscar
     * @return Usuario encontrado o error si no existe
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getUsuarioById(@PathParam("id") int id) {
        Usuario usuario = null;

        try {
            ResultSet respuesta = UsuarioDao.getUsuarioById(id);

            // Construye el objeto usuario si la consulta devuelve datos
            while (respuesta.next()) {
                usuario = new Usuario(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre"),
                    respuesta.getString("apellido"),
                    respuesta.getString("correo"),
                    "", // Por seguridad no se retorna contraseña
                    respuesta.getInt("genero_id"),
                    respuesta.getInt("ciudad_id"),
                    respuesta.getInt("rol_id"),
                    respuesta.getInt("estado_id")
                );
            }

            respuesta.close();

            // Retorna error 404 si no existe, o el usuario con código 200
            if (usuario == null) {
                return ResponseProvider.error("El usuario no existe.", 404);
            } else {
                return ResponseProvider.success(usuario, "Usuario obtenido con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los usuarios", 500);
        }
    }

    /**
     * Método para crear un nuevo usuario.
     * Recibe un JSON con los datos del usuario y devuelve la creación o error.
     * Responde a POST /usuarios/register
     */
    @POST
    @Path("/register")
    @Validar(entidad = "Usuario") // Valida los datos según reglas predefinidas
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public static Response createUsuario(Usuario usuarioData) {
        try {
            // Verifica si el correo ya está registrado
            Usuario usuarioExistente = getUsuarioByCorreo(usuarioData.getCorreo());

            if (usuarioExistente != null)
                return ResponseProvider.error("Este correo ya fue registrado.", 409); // Conflicto

            
            Response ciudad = CiudadController.getCiudad(usuarioData.getCiudad_id());
            
            if(ciudad.getStatus() !=  200) return ResponseProvider.error("Esta ciudad no existe.", 409); // Conflicto
            
            Response genero = GeneroController.getGenero(usuarioData.getGenero_id());
            
            if(ciudad.getStatus() !=  200) return ResponseProvider.error("Este genero no existe.", 409); // Conflicto
            
            // Obtiene la contraseña en texto plano para hashear
            String contrasenaText = usuarioData.getContrasena();

            // Hashea la contraseña antes de almacenar
            usuarioData.setContrasena(hashPassword(contrasenaText));

            int idGenerado = 0; // Variable para almacenar el ID generado por BD

            // Inserta el usuario en la base de datos
            ResultSet ultimoRegistro = UsuarioDao.createUsuario(usuarioData);

            // Obtiene el ID generado tras inserción
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1);
                usuarioData.setId(idGenerado);
                usuarioData.setContrasena(contrasenaText); // Opcional: conservar contraseña en memoria
            }

            ultimoRegistro.close();

            // Si no se creó correctamente, devuelve error
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el usuario.", 400);
            } else {
                return ResponseProvider.success(usuarioData, "Usuario creado con éxito.", 200);
            }

        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el usuario.", 500);
        }
    }
    
    @POST
    @Path("/login") // Define la ruta para el login de usuarios: POST /usuarios/login
    @Validar(entidad = "UsuarioLogin") // Valida que el DTO cumpla con las reglas de UsuarioLogin
    @Produces(MediaType.APPLICATION_JSON) // La respuesta será en JSON
    @Consumes(MediaType.APPLICATION_JSON) // Se reciben datos JSON
    public static Response loginUser(UsuarioLoginDTO usuarioData) {

        try {
            // Busca el usuario en la BD por correo
            Usuario usuario = getUsuarioByCorreo(usuarioData.getCorreo());

            // Si no existe el correo registrado, retorna error 404
            if(usuario == null) return ResponseProvider.error("Este correo no se encuentra registrado.", 404, null);

            // Si el usuario está inactivo (estado 2), deniega acceso con 404 (podría ser 403)
            if(usuario.getEstado_id() == 2) return ResponseProvider.error("Este usuario no se encuentra registrado.", 404, null);

            // Verifica que la contraseña proporcionada coincida con la almacenada (hasheada)
            if(!checkPassword(
                    usuarioData.getContrasena(),
                    usuario.getContrasena()
            ))
                // Si la contraseña no coincide, error 404 (mejor un 401 o 403 pero mantienes 404)
                return ResponseProvider.error("Contraseña incorrecta.", 404, null);

            else {
                // Si todo es correcto, crea un DTO con datos seguros para responder
                UsuarioDTO usuarioDto = new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getRol_id()
                );

                // Retorna éxito con los datos mínimos necesarios y código 200 OK
                return ResponseProvider.success(usuarioDto, "Inicio de sesión exitoso.", 200);
            }

        } catch (JSONException e) {
            // Si ocurre error en el procesamiento JSON, retorna error 500
            return ResponseProvider.error("Error interno al validar el usuario.", 500);
        }
    }

    @PUT // Método para actualizar un usuario completo
    @Validar(entidad = "Usuario") // Valida el DTO Usuario
    @Path("/{id}") // Ruta con el ID del usuario a actualizar
    @Produces(MediaType.APPLICATION_JSON) // Respuesta en JSON
    @Consumes(MediaType.APPLICATION_JSON) // Recibe datos JSON
    public static Response updateUsuario(@PathParam("id") int id, Usuario usuarioData) {
        boolean hashValido = true; // Bandera para controlar si la actualización de contraseña fue exitosa

        try {
            // Verifica si el usuario existe (usa método que devuelve Response para verificar status)
            Response usuarioExistente = getUsuarioById(id);

            if (usuarioExistente.getStatus() == 404) 
                return ResponseProvider.error("Este usuario no existe.", 404);

            // Valida que el correo no esté duplicado en otro usuario distinto al actual
            if(existEmail(id, usuarioData)) 
                return ResponseProvider.error("Este correo ya fué registrado.", 409);
            
            // Verifica si la ciudad existe
            Response ciudad = CiudadController.getCiudad(usuarioData.getCiudad_id());
            
            //valida que sea diferente a estado 200
            if(ciudad.getStatus() !=  200) return ResponseProvider.error("Esta ciudad no existe.", 409); // Conflicto
            
            // Verifica si el genero existe
            Response genero = GeneroController.getGenero(usuarioData.getGenero_id());
            
            //valida que sea diferente a estado 200
            if(ciudad.getStatus() !=  200) return ResponseProvider.error("Este genero no existe.", 409); // Conflicto

            // Verifica si el rol existe
            Response rol = RolController.getRol(usuarioData.getRol_id());
            
            //valida que sea diferente a estado 200
            if(rol.getStatus() !=  200) return ResponseProvider.error("Este rol no existe.", 409); // Conflicto
            
            // Verifica si el estado existe
            Response estado = EstadoController.getEstado(usuarioData.getEstado_id());
            
            //valida que sea diferente a estado 200
            if(estado.getStatus() !=  200) return ResponseProvider.error("Este estado no existe.", 409); // Conflicto

            // Si la contraseña está incluida en la actualización, la procesa aparte
            if(usuarioData.getContrasena() != null) {
                // Obtiene la contraseña en texto plano
                String contrasenaText = usuarioData.getContrasena();

                // Hashea la contraseña
                String hashContrasena =  hashPassword(contrasenaText);

                // Actualiza la contraseña en la BD
                int rowsAffectedPswd = UsuarioDao.updateContrasena(id, hashContrasena);

                // Si no se actualizó la contraseña correctamente, marca error
                if(rowsAffectedPswd == 0) hashValido = false;
            }

            // Actualiza los demás campos del usuario en la BD
            int rowsAffectedUsuario = UsuarioDao.updateUsuario(id, usuarioData);

            // Si actualizó datos y contraseña correctamente, devuelve éxito
            if (rowsAffectedUsuario != 0 && hashValido){
                return ResponseProvider.success(null, "Usuario actualizado con éxito.", 200);
            }
            else 
                return ResponseProvider.error("Error al actualizar el usuario.", 400);

        } catch (Exception e) {
            // Captura errores inesperados y retorna error interno 500
            return ResponseProvider.error("Error interno al actualizar el usuario.", 500);
        }
    }

    @PATCH // Método para actualización parcial (perfil)
    @Validar(entidad = "UsuarioPerfil") // Valida DTO UsuarioPerfil (menos campos)
    @Path("/{id}") // Ruta con el ID del usuario a actualizar parcialmente
    @Produces(MediaType.APPLICATION_JSON) // Respuesta JSON
    @Consumes(MediaType.APPLICATION_JSON) // Datos entrantes JSON
    public static Response partialUpdateUsuario(@PathParam("id") int id, Usuario usuarioData) {
        try {
            // Verifica que el correo no esté duplicado para otro usuario distinto
            if(existEmail(id, usuarioData)) return ResponseProvider.error("Este correo ya fué registrado.", 409);
            
            Response ciudad = CiudadController.getCiudad(usuarioData.getCiudad_id());
            
            if(ciudad.getStatus() !=  200) return ResponseProvider.error("Esta ciudad no existe.", 409); // Conflicto
            
            Response genero = GeneroController.getGenero(usuarioData.getGenero_id());
            
            if(ciudad.getStatus() !=  200) return ResponseProvider.error("Este genero no existe.", 409); // Conflicto

            // Crea un objeto Usuario con solo los campos permitidos para actualización parcial
            Usuario usuarioParcial = new Usuario(
                id,
                usuarioData.getNombre(),
                usuarioData.getApellido(),
                usuarioData.getCorreo(),
                usuarioData.getGenero_id(),
                usuarioData.getCiudad_id()
            );

            // Actualiza solo los campos indicados en la BD
            int rowsAffected = UsuarioDao.partialUpdate(id, usuarioParcial);

            if (rowsAffected != 0){
                usuarioData.setId(id);
                return ResponseProvider.success(null, "Usuario actualizado con éxito.", 200);
            }
            else 
                return ResponseProvider.error("Error al actualizar el usuario.", 400);

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el usuario.", 500);
        }
    }

    @PATCH // Método para actualizar contraseña específica
    @Path("/password/usuario/{usuario_id}") // Ruta para actualizar contraseña del usuario
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public static Response updateContrasena(@PathParam("usuario_id") int usuario_id, PasswordDTO passwordData) {
        try {
            // Verifica que las contraseñas nuevas coincidan
            if(!passwordData.getNew_password().equals(passwordData.getConfirm_password()))
                return ResponseProvider.error("Las contraseñas no coinciden.", 400, null);

            // Valida que la contraseña antigua sea correcta
            boolean passwordValid = validatePassword(usuario_id, passwordData.getOld_password());

            if(!passwordValid) return ResponseProvider.error("Contraseña incorrecta.", 400, null);

            // Hashea la nueva contraseña
            String hashPassword = hashPassword(passwordData.getNew_password());

            // Actualiza la contraseña en BD y obtiene filas afectadas
            int rowsAffected = UsuarioDao.updateContrasena(usuario_id, hashPassword);

            if (rowsAffected != 0) 
                return ResponseProvider.success(null, "Contraseña actualizada con éxito.", 200);
            else 
                return ResponseProvider.error("Este usuario no existe.", 404, null);

        } catch(Exception e) {
            return ResponseProvider.error("Error interno al actualizar la contraseña.", 500);
        }
    }

    @DELETE // Método para eliminación lógica de usuario (soft delete)
    @Path("soft/{id}") // Ruta para soft delete del usuario por ID
    @Produces(MediaType.APPLICATION_JSON)
    public static Response softDeleteUsuario(@PathParam("id") int id) {
        try {
            // Ejecuta soft delete y obtiene filas afectadas
            int rowsAffected = UsuarioDao.softDeleteUsuario(id);

            if (rowsAffected != 0) 
                return ResponseProvider.success(null, "Usuario eliminado de forma segura.", 200);
            else 
                return ResponseProvider.error("Este usuario no existe.", 404);

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el usuario.", 500);
        }
    }

    @DELETE // Método para eliminación física definitiva de usuario
    @Path("/{id}") // Ruta para delete definitivo por ID
    @Produces(MediaType.APPLICATION_JSON)
    public static Response deleteUsuario(@PathParam("id") int id) {
        try {
            // Ejecuta eliminación física y obtiene filas afectadas
            int rowsAffected = UsuarioDao.deleteUsuario(id);

            if (rowsAffected != 0) 
                return ResponseProvider.success(null, "Usuario eliminado con éxito.", 200);
            else 
                return ResponseProvider.error("Este usuario no existe.", 404);

        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el usuario.", 500);
        }
    }

    private static Usuario getUsuarioByCorreo(String correo) {
        Usuario usuario = null; // Inicializa usuario resultado

        try {
            // Consulta usuario por correo
            ResultSet respuesta = UsuarioDao.getUsuarioByCorreo(correo);

            // Si existe, construye objeto Usuario
            while (respuesta.next()) { 
                usuario = new Usuario(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre"),
                    respuesta.getString("apellido"),
                    respuesta.getString("correo"),
                    respuesta.getString("contrasena"),
                    respuesta.getInt("genero_id"),
                    respuesta.getInt("ciudad_id"),
                    respuesta.getInt("rol_id"),
                    respuesta.getInt("estado_id")
                );
            }

            respuesta.close();
            return usuario;

        } catch (SQLException e) {
            // Error crítico, se lanza error para manejo arriba
            throw new Error("Error al obtener el usuario");
        }
    }

    private static boolean existEmail(int id, Usuario usuarioData) {
        boolean existe = false;

        try {
            // Consulta todos los usuarios
            ResultSet respuesta = UsuarioDao.getUsuarios();

            // Recorre para verificar si otro usuario tiene el mismo correo
            while (respuesta.next()) {
                // Si otro usuario (distinto ID) tiene el mismo correo, marca true
                if(respuesta.getInt("id") != id && usuarioData.getCorreo().equals(respuesta.getString("correo"))) 
                    existe = true;
            }

            respuesta.close();
            return existe;

        } catch (SQLException e) {
            throw new Error("Error al validar si el correo existe");
        }
    }

    public static boolean validatePassword(int usuario_id, String passwordText) {
        try {
            // Obtiene contraseña hash actual del usuario
            String contrasenaHash = obtenerContrasena(usuario_id);

            if(contrasenaHash == null) return false;

            // Verifica que la contraseña dada coincida con la almacenada
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
        Usuario usuario = null;

        try {
            // Consulta usuario por ID para obtener contraseña
            ResultSet respuesta = UsuarioDao.getUsuarioById(id);

            while (respuesta.next()) { 
                usuario = new Usuario(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre"),
                    respuesta.getString("apellido"),
                    respuesta.getString("correo"),
                    respuesta.getString("contrasena"),
                    respuesta.getInt("genero_id"),
                    respuesta.getInt("ciudad_id"),
                    respuesta.getInt("rol_id"),
                    respuesta.getInt("estado_id")
                );
            }

            respuesta.close();

            // Si usuario no existe, retorna null; si existe, retorna la contraseña hash
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
        // Genera hash bcrypt para la contraseña en texto plano
        return BCrypt.hashpw(contrasenaText, BCrypt.gensalt());
    }

    public static boolean checkPassword(String contrasenaText, String hashedContrasena) {
        // Verifica que el texto plano coincida con el hash almacenado
        return BCrypt.checkpw(contrasenaText, hashedContrasena);
    }

}