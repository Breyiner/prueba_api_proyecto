package CONTROLLER; // Paquete que contiene el controlador de usuarios

import MODEL.Ciudad;
import MIDDLEWARES.Validar;
import PROVIDERS.ResponseProvider;
import SERVICES.CiudadService;
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

@Path("/ciudades") // Define la ruta base para todas las operaciones relacionadas con usuarios
public class CiudadController {
    
    @GET // Indica que este método responde a solicitudes GET
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response getCiudades() {
        
        try {
            return CiudadService.getCiudades();
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener las ciudades", 500);
        } 
    }
    
    @GET // Indica que este método responde a solicitudes GET
    @Path("/{id}") // La ruta incluye el ID de la ciudad a buscar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response getCiudad(@PathParam("id") int id) {
        
        try {
            return CiudadService.getCiudadById(id);
        } catch (Exception e) {
           return ResponseProvider.error("Error al obtener la ciudad", 500);
        }
    }
    
    @POST
    @Validar(entidad = "Ciudades")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public Response createCiudad(Ciudad ciudadData) {
        
        try {
            return CiudadService.createCiudad(ciudadData);
        } catch (Exception e) {
           return ResponseProvider.error("Error al crear la ciudad", 500);
        }
    }
    
    @PUT // Indica que este método responde a solicitudes PUT
    @Path("/{id}") // Ruta con el ID de la ciudad a actualizar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    @Consumes(MediaType.APPLICATION_JSON) // Indica que el método acepta datos en formato JSON
    public Response updateCiudad(@PathParam("id") int id, Ciudad ciudadData) {
        try {
            return CiudadService.updateCiudad(id, ciudadData);
        } catch (Exception e) {
           return ResponseProvider.error("Error al actualizar la ciudad", 500);
        }
    }
    
    @DELETE // Indica que este método responde a solicitudes DELETE
    @Path("/{id}") // Ruta con el ID de la ciudad a eliminar
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response deleteCiudad(@PathParam("id") int id) {
     
        try {
            return CiudadService.deleteCiudad(id);
        } catch (Exception e) {
           return ResponseProvider.error("Error al eliminar la ciudad", 500);
        }
    }
}
