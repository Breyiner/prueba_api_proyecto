package CONTROLLER;

import MODEL.Categoria;
import MIDDLEWARES.Validar;
import PROVIDERS.ResponseProvider;
import SERVICES.CategoriaService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/categorias") // Ruta base para categorías
public class CategoriaController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategorias() {
        try {
            return CategoriaService.getCategorias();
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener las categorías", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoria(@PathParam("id") int id) {
        try {
            return CategoriaService.getCategoriaById(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener la categoría", 500);
        }
    }

    @POST
    @Validar(entidad = "Categorias")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCategoria(Categoria categoriaData) {
        try {
            return CategoriaService.createCategoria(categoriaData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al crear la categoría", 500);
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCategoria(@PathParam("id") int id, Categoria categoriaData) {
        try {
            return CategoriaService.updateCategoria(id, categoriaData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al actualizar la categoría", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategoria(@PathParam("id") int id) {
        try {
            return CategoriaService.deleteCategoria(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al eliminar la categoría", 500);
        }
    }
}
