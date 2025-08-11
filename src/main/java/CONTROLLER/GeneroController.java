package CONTROLLER;

import MODELO.GeneroDao;
import MODELO.Genero;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/generos")
public class GeneroController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getGeneros() {
        List<Genero> generos = new ArrayList<>();
        try {
            ResultSet respuesta = GeneroDao.getGeneros();
            while (respuesta.next()) {
                Genero genero = new Genero(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
                generos.add(genero);
            }
            respuesta.close();
            if (!generos.isEmpty()) {
                return ResponseProvider.success(generos, "Géneros obtenidos con éxito.", 200);
            } else {
                return ResponseProvider.error("No hay géneros registrados.", 404);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener los géneros", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getGenero(@PathParam("id") int id) {
        Genero genero = null;
        try {
            ResultSet respuesta = GeneroDao.getGeneroById(id);
            while (respuesta.next()) {
                genero = new Genero(
                    respuesta.getInt("id"),
                    respuesta.getString("nombre")
                );
            }
            respuesta.close();
            if (genero == null) {
                return ResponseProvider.error("El género no existe.", 404);
            } else {
                return ResponseProvider.success(genero, "Género obtenido con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al obtener el género", 500);
        }
    }

    @POST
    @Validar(entidad = "Generos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response createGenero(Genero generoData) {
        try {
            int idGenerado = 0;
            ResultSet ultimoRegistro = GeneroDao.createGenero(generoData);
            while (ultimoRegistro.next()) {
                idGenerado = ultimoRegistro.getInt(1);
                generoData.setId(idGenerado);
            }
            ultimoRegistro.close();
            if (idGenerado == 0) {
                return ResponseProvider.error("Error al crear el género.", 400);
            } else {
                return ResponseProvider.success(generoData, "Género creado con éxito.", 200);
            }
        } catch (SQLException e) {
            return ResponseProvider.error("Error interno al crear el género.", 500);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response updateGenero(@PathParam("id") int id, Genero generoData) {
        try {
            Response generoExistente = getGenero(id);
            if (generoExistente.getStatus() == 404) return ResponseProvider.error("Este género no existe.", 404);

            int rowsAffected = GeneroDao.updateGenero(id, generoData);
            if (rowsAffected != 0) {
                generoData.setId(id);
                return ResponseProvider.success(generoData, "Género actualizado con éxito.", 200);
            } else {
                return ResponseProvider.error("Error al actualizar el género.", 400);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al actualizar el género.", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response deleteGenero(@PathParam("id") int id) {
        try {
            
            Response haveUsers = UserController.getUsuariosByGeneroId(id);
            
            if(haveUsers.getStatus() == 200) return ResponseProvider.error("Este género tiene usuarios relacionados, no se puede eliminar.", 409);
                        
            int rowsAffected = GeneroDao.deleteGenero(id);
            if (rowsAffected != 0) {
                return ResponseProvider.success(null, "Género eliminado con éxito.", 200);
            } else {
                return ResponseProvider.error("Este género no existe.", 404);
            }
        } catch (Exception e) {
            return ResponseProvider.error("Error interno al eliminar el género.", 500);
        }
    }
}
