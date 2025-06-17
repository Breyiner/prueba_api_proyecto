package CONTROLLER; // Paquete que contiene el controlador de usuarios

import DAO.UsuarioDao; // Importa la clase UsuarioDao que maneja la interacción con la base de datos
import MODEL.Usuario; // Importa la clase Usuario que representa la entidad de usuario
import java.sql.ResultSet; // Importa la clase ResultSet para manejar los resultados de las consultas SQL
import java.sql.SQLException; // Importa la clase SQLException para manejar errores de SQL
import java.util.ArrayList; // Importa la clase ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List para manejar colecciones de objetos
import javax.ws.rs.Consumes; // Importa la anotación para indicar el tipo de contenido que consume el método
import javax.ws.rs.DELETE; // Importa la anotación para manejar solicitudes DELETE
import javax.ws.rs.GET; // Importa la anotación para manejar solicitudes GET
import javax.ws.rs.POST; // Importa la anotación para manejar solicitudes POST
import javax.ws.rs.PUT; // Importa la anotación para manejar solicitudes PUT
import javax.ws.rs.Path; // Importa la anotación para definir la ruta del recurso
import javax.ws.rs.PathParam; // Importa la anotación para extraer parámetros de la ruta
import javax.ws.rs.Produces; // Importa la anotación para indicar el tipo de contenido que produce el método
import javax.ws.rs.core.MediaType; // Importa la clase MediaType para definir tipos de contenido
import javax.ws.rs.core.Response; // Importa la clase Response para construir respuestas HTTP

@Path("/usuarios") // Define la ruta base para todas las operaciones relacionadas con usuarios
public class UserController {
    
    /**
     * Método para obtener todos los usuarios disponibles en la base de datos.
     * Este método responde a las solicitudes HTTP GET en la ruta "/usuarios".
     * Construye una lista de objetos Usuario a partir de los datos recuperados,
     * y retorna esa lista en formato JSON con un estado HTTP 200 OK.
     * Si ocurre algún error durante la consulta, retorna un estado HTTP 404 Not Found.
     * 
     * @return Response que contiene la lista de usuarios en formato JSON o un error 404 si no se encuentran usuarios
     */
    @GET // Indica que este método responde a solicitudes GET
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response getUsuarios() {
        
        List<Usuario> usuarios = new ArrayList<Usuario>(); // Crea una lista para almacenar los usuarios recuperados
        
        try {
            // Ejecuta la consulta para obtener todos los usuarios mediante la capa DAO
            ResultSet respuesta = UsuarioDao.getUsuarios();
            
            // Recorre cada fila resultado de la consulta
            while (respuesta.next()) {     
                // Crea un objeto Usuario con los datos de cada fila
                Usuario usuario = new Usuario(
                    Integer.parseInt(respuesta.getString("id")), // Obtiene y convierte el ID del usuario a entero
                    respuesta.getString("nombre"), // Obtiene el nombre del usuario
                    respuesta.getString("apellido"), // Obtiene el apellido del usuario
                    respuesta.getString("correo"), // Obtiene el correo electrónico del usuario
                    respuesta.getString("contrasean"), // Obtiene la contraseña del usuario
                    Integer.parseInt(respuesta.getString("genero_id")), // Obtiene el ID del género del usuario
                    Integer.parseInt(respuesta.getString("ciudad_id")), // Obtiene el ID de la ciudad del usuario
                    Integer.parseInt(respuesta.getString("estado_id")) // Obtiene el ID del estado del usuario
                );
                // Añade el objeto Usuario a la lista de usuarios
                usuarios.add(usuario);
            }
            
            // Cierra el ResultSet para liberar recursos
            respuesta.close();
            
            // Devuelve la lista de usuarios con estado 200 OK
            return Response.ok(usuarios).build();
            
        } catch (SQLException e) {
            // Si ocurre un error en la consulta, devuelve un estado 404 Not Found
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    /**
     * Método para obtener un usuario específico usando su ID único.
     * Responde a solicitudes GET en "/usuarios/{id}".
     * Busca el usuario en la base de datos y, si existe, devuelve su información en JSON con estado 200.
     * Si no se encuentra o hay un error, retorna un 404 Not Found.
     * 
     * @param id Identificador único del usuario a buscar
     * @return Response que contiene el usuario en formato JSON o un error 404 si no se encuentra el usuario
     */
    @GET // Indica que este método responde a solicitudes GET
    @Path("/{id}") // La ruta incluye el ID del usuario a buscar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response getUsuario(@PathParam("id") int id) {
        
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
            
            // Devuelve el usuario encontrado, o null si no existe
            return Response.ok(usuario).build(); // Devuelve el usuario con código 200 OK
            
        } catch (SQLException e) {
            // En caso de error o si no se encuentra el usuario, responde con 404 Not Found
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    /**
     * Método para recuperar un usuario mediante su correo electrónico.
     * Permite buscar usuarios sin necesidad de conocer su ID.
     * Responde a solicitudes GET en "/usuarios/correo/{correo}".
     * Devuelve el usuario correspondiente en JSON o 404 si no existe.
     * 
     * @param correo Dirección de correo electrónico usada para la búsqueda
     * @return Response que contiene el usuario en formato JSON o un error 404 si no se encuentra el usuario
     */
    @GET // Indica que este método responde a solicitudes GET
    @Path("correo/{correo}") // La ruta incluye el correo a buscar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response getUsuarioByCorreo(@PathParam("correo") String correo) {
        System.out.println(correo); // Imprime el correo recibido para depuración
        
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
            
            // Retorna el usuario encontrado o null si no hubo coincidencias
            return Response.ok(usuario).build(); // Devuelve el usuario en formato JSON
            
        } catch (SQLException e) {
            // Si ocurre un error o no se encuentra el usuario, indica no encontrado
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Método para crear un nuevo usuario en la base de datos.
     * Recibe los datos en formato JSON y los inserta usando la capa DAO.
     * Devuelve el usuario creado junto con el ID asignado y el estado HTTP 201 Created.
     * Si ocurre un problema en la inserción, responde con un error interno del servidor.
     * 
     * @param usuarioData Objeto Usuario con los datos que se desean guardar
     * @return Response que contiene el nuevo usuario en formato JSON o un error 500 si ocurre un problema
     */
    @POST // Indica que este método responde a solicitudes POST
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public Response createUsuario(Usuario usuarioData) {
        
        try {
            int idGenerado = 0; // Inicializa variable para almacenar el ID generado
            
            // Inserta el nuevo usuario en la base de datos y obtiene el último ID generado
            ResultSet ultimoRegistro = UsuarioDao.createUsuario(usuarioData);
            
            // Si la inserción fue exitosa, asigna el ID generado al objeto usuario
            if (ultimoRegistro.next()) idGenerado = ultimoRegistro.getInt(1);
            usuarioData.setId(idGenerado); // Asigna el ID al objeto usuario
            
            // Retorna el nuevo usuario con estado 201 Created
            return Response.status(Response.Status.CREATED).entity(usuarioData).build();
            
        } catch (SQLException e) {
            // En caso de error durante la inserción, responde con error 500 Internal Server Error
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Método para actualizar los datos de un usuario existente por su ID.
     * Recibe la nueva información en formato JSON y actualiza el registro correspondiente.
     * Devuelve un mensaje confirmando la actualización o un error si el usuario no fue encontrado.
     * En caso de problemas internos, envía un error 500.
     * 
     * @param id Identificador del usuario que se desea actualizar
     * @param usuarioData Nuevos datos para actualizar el usuario
     * @return Response que indica éxito con mensaje, error 404 si no se encuentra el usuario o error 500 por fallo interno
     */
    @PUT // Indica que este método responde a solicitudes PUT
    @Path("/{id}") // Ruta con el ID del usuario a actualizar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public Response updateUsuario(@PathParam("id") int id, Usuario usuarioData) {
     
       try {
            // Intenta actualizar el usuario en la base de datos y devuelve el número de filas afectadas
            int rowsAffected = UsuarioDao.updateUsuario(id, usuarioData);
            
            if (rowsAffected != 0) 
                // Si la actualización se realizó, se confirma el éxito con código 200 OK
                return Response.ok("Usuario actualizado con éxito").build();
            else 
                // Si no encontró usuario para actualizar, devuelve 404 Not Found con mensaje
                return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
            
        } catch (Exception e) {
            // Captura cualquier problema interno y devuelve un error 500 con mensaje
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();
        }
    }

    /**
     * Método para eliminar un usuario a partir de su ID.
     * Si la eliminación es exitosa devuelve código 204 No Content.
     * Si no existe el usuario, responde con 404 Not Found.
     * Cualquier otro error genera un 500 Internal Server Error.
     * 
     * @param id Identificador del usuario que se desea eliminar
     * @return Response con código 204 sin contenido si éxito, 404 si no se encuentra usuario o 500 en error interno
     */
    @DELETE // Indica que este método responde a solicitudes DELETE
    @Path("/{id}") // Ruta con el ID del usuario a eliminar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response deleteUsuario(@PathParam("id") int id) {
     
        try {
            // Ejecuta la eliminación de usuario en la base de datos y recibe la cantidad de filas afectadas
            int rowsAffected = UsuarioDao.deleteUsuario(id);
            
            if (rowsAffected != 0) 
                // Si usuario eliminado correctamente, devuelve código 204 No Content con mensaje
                return Response.status(Response.Status.NO_CONTENT).entity("Usuario eliminado").build();
            else 
                // Si no encontró usuario para eliminar, devuelve 404 Not Found con mensaje
                return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
            
        } catch (Exception e) {
            // Para cualquier error interno, retorna un error 500 con mensaje
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();
        }
    }
}