package CONTROLLER; // Paquete controlador

import MODELO.CiudadDao; // Acceso a base de datos para ciudades
import MODELO.Ciudad;    // Modelo Ciudad

import javax.ws.rs.*; // Anotaciones para servicios REST
import javax.ws.rs.core.MediaType; // Tipo de contenido JSON
import javax.ws.rs.core.Response;  // Respuestas HTTP
import java.sql.ResultSet;          // Resultado de consultas SQL
import java.sql.SQLException;      // Manejo de excepciones SQL
import java.util.ArrayList;        // Lista dinámica
import java.util.List;             // Interfaz lista
import MODELO.Usuario;                 // Entidad Usuario que representa datos de usuario

@Path("/ciudades") // Ruta base para recursos ciudad
public class CiudadController {

    @GET
    @Produces(MediaType.APPLICATION_JSON) // Responde JSON
    public static Response getCiudades() {
        List<Ciudad> ciudades = new ArrayList<>(); // Lista para almacenar ciudades
        try {
            ResultSet respuesta = CiudadDao.getCiudades(); // Consultar todas las ciudades
            while (respuesta.next()) { // Recorrer resultados
                Ciudad ciudad = new Ciudad(
                    respuesta.getInt("id"),      // ID ciudad
                    respuesta.getString("nombre") // Nombre ciudad
                );
                ciudades.add(ciudad); // Agregar a la lista
            }
            respuesta.close(); // Cerrar recurso ResultSet
            if (!ciudades.isEmpty()) {
                return ResponseProvider.success(ciudades, "Ciudades obtenidas con éxito.", 200); // Éxito con datos
            } else {
                return ResponseProvider.error("No hay ciudades registradas.", 404); // No hay datos
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener las ciudades", 500); // Error DB
        }
    }

    @GET
    @Path("/{id}") // Obtener ciudad por ID
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getCiudad(@PathParam("id") int id) {
        Ciudad ciudad = null;
        try {
            ResultSet respuesta = CiudadDao.getCiudadById(id); // Consultar por ID
            while (respuesta.next()) {
                ciudad = new Ciudad(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
            }
            respuesta.close();
            if (ciudad == null) {
                return ResponseProvider.error("La ciudad no existe.", 404); // No existe ciudad
            } else {
                return ResponseProvider.success(ciudad, "Ciudad obtenida con éxito.", 200); // Éxito
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener la ciudad", 500); // Error DB
        }
    }

    @POST
    @Validar(entidad = "Ciudades") // Validar campos para ciudades
    @Consumes(MediaType.APPLICATION_JSON) // Recibe JSON
    @Produces(MediaType.APPLICATION_JSON)
    public static Response createCiudad(Ciudad ciudadData) {
        try {
            int idGenerado = 0;
            ResultSet ultimoRegistro = CiudadDao.createCiudad(ciudadData); // Insertar ciudad
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1); // Obtener ID generado
                ciudadData.setId(idGenerado);
            }
            ultimoRegistro.close();
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear la ciudad.", 400); // Error creación
            } else {
                return ResponseProvider.success(ciudadData, "Ciudad creada con éxito.", 200); // Éxito creación
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear la ciudad.", 500); // Error DB
        }
    }

    @PUT
    @Path("/{id}") // Actualizar ciudad por ID
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response updateCiudad(@PathParam("id") int id, Ciudad ciudadData) {
        try {
            Response ciudadExistente = getCiudad(id); // Verificar si existe ciudad
            if (ciudadExistente.getStatus() == 404)
                return ResponseProvider.error("Esta ciudad no existe.", 404); // No existe ciudad

            int rowsAffected = CiudadDao.updateCiudad(id, ciudadData); // Actualizar en DB
            if (rowsAffected != 0) {
                ciudadData.setId(id);
                return ResponseProvider.success(ciudadData, "Ciudad actualizada con éxito.", 200); // Éxito actualización
            } else {
                return ResponseProvider.error("Error al actualizar la ciudad.", 400); // Error actualización
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar la ciudad.", 500); // Error DB
        }
    }

    @DELETE
    @Path("/{id}") // Eliminar ciudad por ID
    @Produces(MediaType.APPLICATION_JSON)
    public static Response deleteCiudad(@PathParam("id") int id) {
        try {
            
            Response respuesta = UserController.getUsuariosByCiudadId(id);
            
            if(respuesta.getStatus() == 200) return ResponseProvider.error("Esta ciudad tiene usuarios relacionados, no se puede eliminar.", 409);
            
            int rowsAffected = CiudadDao.deleteCiudad(id); // Eliminar en DB
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Ciudad eliminada con éxito.", 200); // Éxito eliminación
            } else {
                return ResponseProvider.error("Esta ciudad no existe.", 404); // No existe ciudad
            }
        } catch (Exception e) {
            System.out.print(e);
            return ResponseProvider.error("Error interno al eliminar la ciudad.", 500); // Error DB
        }
    }
}
