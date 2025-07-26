package CONTROLLER;

import SERVICES.DashboardService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import PROVIDERS.ResponseProvider;

@Path("/dashboard")
public class DashboardController {

    @GET
    @Path("/movimientos/usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenMovimientos(
            @PathParam("usuario_id") int usuario_id,
            @PathParam("mes") int mes) {
        try {
            return DashboardService.getResumenMovimientos(usuario_id, mes);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el resumen de movimientos", 500);
        }
    }

    @GET
    @Path("/metas/usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenMetas(
            @PathParam("usuario_id") int usuario_id,
            @PathParam("mes") int mes) {
        try {
            return DashboardService.getResumenMetas(usuario_id, mes);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el resumen de metas", 500);
        }
    }

    @GET
    @Path("/completo/usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenCompleto(
            @PathParam("usuario_id") int usuarioId,
            @PathParam("mes") int mes) {
        try {
            return DashboardService.getResumenCompleto(usuarioId, mes);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el resumen completo", 500);
        }
    }
    
    @GET
    @Path("/categorias/usuario/{usuario_id}/mes/{mes}/tipo/{tipo_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenCategoriasDetalle(
            @PathParam("usuario_id") int usuario_id,
            @PathParam("mes") int mes,
            @PathParam("tipo_id") int tipo_id) {
        try {
            return DashboardService.getResumenCategoriasDetalle(usuario_id, mes, tipo_id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el resumen detallado de categorías", 500);
        }
    }
    
    
        
    @GET
    @Path("/metas/detalle/usuario/{usuario_id}/mes/{mes}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResumenMetasDetalle(
            @PathParam("usuario_id") int usuarioId,
            @PathParam("mes") int mes) {
        try {
            return DashboardService.getResumenMetasDetalle(usuarioId, mes);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener el resumen detallado de metas", 500);
        }
    }

}