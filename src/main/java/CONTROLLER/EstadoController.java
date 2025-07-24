package CONTROLLER;

import MODEL.Estado;
import MIDDLEWARES.Validar;
import PROVIDERS.ResponseProvider;
import SERVICES.EstadoService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/estados")
public class EstadoController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEstados() {
        try {
            return EstadoService.getEstados();
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los estados", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEstado(@PathParam("id") int id) {
        try {
            return EstadoService.getEstadoById(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el estado", 500);
        }
    }

    @POST
    @Validar(entidad = "Estados")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEstado(Estado estadoData) {
        try {
            return EstadoService.createEstado(estadoData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al crear el estado", 500);
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEstado(@PathParam("id") int id, Estado estadoData) {
        try {
            return EstadoService.updateEstado(id, estadoData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al actualizar el estado", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEstado(@PathParam("id") int id) {
        try {
            return EstadoService.deleteEstado(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al eliminar el estado", 500);
        }
    }
}