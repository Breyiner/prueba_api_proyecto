package CONTROLLER;

import MODELO.CiudadDao;
import MODELO.Ciudad;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/ciudades")
public class CiudadController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCiudades() {
        List<Ciudad> ciudades = new ArrayList<>();
        try {
            ResultSet respuesta = CiudadDao.getCiudades();
            while (respuesta.next()) {
                Ciudad ciudad = new Ciudad(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
                ciudades.add(ciudad);
            }
            respuesta.close();
            if (!ciudades.isEmpty()) {
                return ResponseProvider.success(ciudades, "Ciudades obtenidas con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay ciudades registradas.", 404);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener las ciudades", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCiudad(@PathParam("id") int id) {
        Ciudad ciudad = null;
        try {
            ResultSet respuesta = CiudadDao.getCiudadById(id);
            while (respuesta.next()) {
                ciudad = new Ciudad(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
            }
            respuesta.close();
            if (ciudad == null) {
                return ResponseProvider.error("La ciudad no existe.", 404);
            } else {
                return ResponseProvider.success(ciudad, "Ciudad obtenida con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener la ciudad", 500);
        }
    }

    @POST
    @Validar(entidad = "Ciudades")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCiudad(Ciudad ciudadData) {
        try {
            int idGenerado = 0;
            ResultSet ultimoRegistro = CiudadDao.createCiudad(ciudadData);
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1);
                ciudadData.setId(idGenerado);
            }
            ultimoRegistro.close();
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear la ciudad.", 400);
            } else {
                return ResponseProvider.success(ciudadData, "Ciudad creada con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear la ciudad.", 500);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCiudad(@PathParam("id") int id, Ciudad ciudadData) {
        try {
            Response ciudadExistente = getCiudad(id);
            if (ciudadExistente.getStatus() == 404)
                return ResponseProvider.error("Esta ciudad no existe.", 404);
            int rowsAffected = CiudadDao.updateCiudad(id, ciudadData);
            if (rowsAffected != 0) {
                ciudadData.setId(id);
                return ResponseProvider.success(ciudadData, "Ciudad actualizada con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar la ciudad.", 400);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar la ciudad.", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCiudad(@PathParam("id") int id) {
        try {
            int rowsAffected = CiudadDao.deleteCiudad(id);
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Ciudad eliminada con éxito.", 200);
            } else {
                return ResponseProvider.error("Esta ciudad no existe.", 404);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar la ciudad.", 500);
        }
    }
}
