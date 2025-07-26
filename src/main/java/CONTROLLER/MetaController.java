package CONTROLLER;

import MIDDLEWARES.Validar;
import MODEL.Meta;
import PROVIDERS.ResponseProvider;
import SERVICES.MetaService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/metas")
public class MetaController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetas() {
        try {
            return MetaService.getMetas();
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener las metas", 500);
        }
    }
    
    @GET // Indica que este método responde a solicitudes GET
    @Path("/cantidad")
    @Produces(MediaType.APPLICATION_JSON) // Especifica que el método devuelve datos en formato JSON
    public Response getUsuario() {
        
        try {
            return MetaService.getCantidadMetas();
        } catch (Exception e) {
           return ResponseProvider.error("Error al obtener la cantida de metas", 500);
        }
    }
    
    @GET
    @Path("/resumen/usuario/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetasResumen(@PathParam("usuario_id") int usuario_id) {
        try {
            return MetaService.getMetasConTotal(usuario_id);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseProvider.error("Error al obtener las metas", 500);
        }
    }
    
    @GET
    @Path("/{id}/usuario/{usuario_id}/detalles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetaDetalles(
            @PathParam("id") int id,
            @PathParam("usuario_id") int usuario_id
    ) {
        try {
            return MetaService.getMetasCantMovimientos(id, usuario_id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener las metas", 500);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetaById(@PathParam("id") int id) {
        try {
            return MetaService.getMetaById(id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al obtener la meta", 500);
        }
    }

    @POST
    @Validar(entidad = "Meta")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMeta(Meta metaData) {
        try {
            return MetaService.createMeta(metaData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al crear la meta", 500);
        }
    }

    @PUT
    @Path("/{id}/usuario/{usuario_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMeta(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id,
        Meta metaData
    ) {
        try {
            return MetaService.updateMeta(id, usuario_id, metaData);
        } catch (Exception e) {
            return ResponseProvider.error("Error al actualizar la meta", 500);
        }
    }

    @PATCH
    @Path("/{id}/usuario/{usuario_id}/completada")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCompletada(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id,
        @QueryParam("completada") boolean completada
    ) {
        try {
            return MetaService.updateCompletada(id, usuario_id, completada);
        } catch (Exception e) {
            return ResponseProvider.error("Error al actualizar el estado de completitud", 500);
        }
    }

    @DELETE
    @Path("/{id}/usuario/{usuario_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMeta(
        @PathParam("id") int id,
        @PathParam("usuario_id") int usuario_id
    ) {
        try {
            return MetaService.deleteMeta(id, usuario_id);
        } catch (Exception e) {
            return ResponseProvider.error("Error al eliminar la meta", 500);
        }
    }
}