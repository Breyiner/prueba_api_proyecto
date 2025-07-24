package CONTROLLER;

import MODEL.Genero;
import MIDDLEWARES.Validar;
import PROVIDERS.ResponseProvider;
import SERVICES.GeneroService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/generos")
public class GeneroController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGeneros() {
        try {
            return GeneroService.getGeneros();
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los géneros", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenero(@PathParam("id") int id) {
        try {
            return GeneroService.getGeneroById(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el género", 500);
        }
    }

    @POST
    @Validar(entidad = "Generos")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGenero(Genero generoData) {
        try {
            return GeneroService.createGenero(generoData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al crear el género", 500);
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGenero(@PathParam("id") int id, Genero generoData) {
        try {
            return GeneroService.updateGenero(id, generoData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al actualizar el género", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGenero(@PathParam("id") int id) {
        try {
            return GeneroService.deleteGenero(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al eliminar el género", 500);
        }
    }
}