package CONTROLLER;

import MODEL.Movimiento;
import MIDDLEWARES.Validar;
import PROVIDERS.ResponseProvider;
import SERVICES.MovimientoService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/movimientos")
public class MovimientoController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovimientos() {
        try {
            return MovimientoService.getMovimiento();
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los movimientos", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovimientoById(@PathParam("id") int id) {
        try {
            return MovimientoService.getMovimientoById(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el movimiento", 500);
        }
    }

    @GET
    @Path("/usuario/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovimientosByUsuario(@PathParam("usuario_id") int usuario_id) {
        try {
            return MovimientoService.getMovimientosByUser(usuario_id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener los movimientos del usuario", 500);
        }
    }

    @GET
    @Path("/categoria")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovimientosPorCategoria(
        @QueryParam("usuario_id") int usuario_id,
        @QueryParam("tipo_movimiento_id") int tipo_movimiento_id,
        @QueryParam("mes") int mes
    ) {
        try {
            return MovimientoService.getMovimientosByCategoria(usuario_id, tipo_movimiento_id, mes);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener movimientos por categoría", 500);
        }
    }

    @POST
    @Validar(entidad = "Movimiento")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovimiento(Movimiento movimientoData) {
        try {
            return MovimientoService.createMovimiento(movimientoData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al crear el movimiento", 500);
        }
    }

    @PUT
    @Path("/{id}/usuario/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovimiento(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id,
        Movimiento movimientoData
    ) {
        try {
            return MovimientoService.updateMovimiento(id, usuario_id, movimientoData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al actualizar el movimiento", 500);
        }
    }

    @DELETE
    @Path("/{id}/usuario/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovimiento(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id
    ) {
        try {
            return MovimientoService.deleteMovimiento(id, usuario_id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al eliminar el movimiento", 500);
        }
    }
}