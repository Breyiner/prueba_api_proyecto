package CONTROLLER;

import MODEL.TiposMovimiento;
import MIDDLEWARES.Validar;
import PROVIDERS.ResponseProvider;
import SERVICES.TiposMovimientoService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tiposMovimiento") // Ruta base para tipos de movimiento
public class TiposMovimientoController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTiposMovimiento() {
        try {
            return TiposMovimientoService.getTipos();
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los tipos de movimiento", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTipoMovimiento(@PathParam("id") int id) {
        try {
            return TiposMovimientoService.getTipoById(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el tipo de movimiento", 500);
        }
    }

    @POST
    @Validar(entidad = "TiposMovimiento")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTipoMovimiento(TiposMovimiento tipoMovimientoData) {
        try {
            return TiposMovimientoService.createTipo(tipoMovimientoData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al crear el tipo de movimiento", 500);
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTipoMovimiento(@PathParam("id") int id, TiposMovimiento tipoMovimientoData) {
        try {
            return TiposMovimientoService.updateTipo(id, tipoMovimientoData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al actualizar el tipo de movimiento", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTipoMovimiento(@PathParam("id") int id) {
        try {
            return TiposMovimientoService.deleteTipo(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al eliminar el tipo de movimiento", 500);
        }
    }
}