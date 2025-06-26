package CONTROLLER; // Paquete que contiene el controlador de usuarios

import MIDDLEWARES.Validar;
import MODEL.Usuario; // Importa la clase Usuario que representa la entidad de usuario
import PROVIDERS.ResponseProvider;
import SERVICES.usuarioService;
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
    

    @GET // Indica que este método responde a solicitudes GET
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response getUsuarios() {
        
        try {
            return usuarioService.getUsuarios();
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los usuarios", 500);
        } 
    }
    

    @GET // Indica que este método responde a solicitudes GET
    @Path("/{id}") // La ruta incluye el ID del usuario a buscar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response getUsuario(@PathParam("id") int id) {
        
        try {
            return usuarioService.getUsuarioById(id);
        } catch (Exception e) {
           return ResponseProvider.error("Error al obtener el usuario", 500);
        }
    }
    

    @GET // Indica que este método responde a solicitudes GET
    @Path("correo/{correo}") // La ruta incluye el correo a buscar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response getUsuarioByCorreo(@PathParam("correo") String correo) {
        try {
            return usuarioService.getUsuarioByCorreo(correo);
        } catch (Exception e) {
           return ResponseProvider.error("Error al obtener el usuario", 500);
        }
    }


    @POST // Indica que este método responde a solicitudes POST
    @Validar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public Response createUsuario(Usuario usuarioData) {
        
        try {
            return usuarioService.createUsuario(usuarioData);
        } catch (Exception e) {
           return ResponseProvider.error("Error al crear el usuario", 500);
        }
    }
    

    @PUT // Indica que este método responde a solicitudes PUT
    @Path("/{id}") // Ruta con el ID del usuario a actualizar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public Response updateUsuario(@PathParam("id") int id, Usuario usuarioData) {
        try {
            return usuarioService.updateUsuario(id, usuarioData);
        } catch (Exception e) {
           return ResponseProvider.error("Error al actualizar el usuario", 500);
        }
    }
    
    @DELETE // Indica que este método responde a solicitudes DELETE
    @Path("/{id}") // Ruta con el ID del usuario a eliminar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response deleteUsuario(@PathParam("id") int id) {
     
        try {
            return usuarioService.deleteUsario(id);
        } catch (Exception e) {
           return ResponseProvider.error("Error al eliminar el usuario", 500);
        }
    }
}